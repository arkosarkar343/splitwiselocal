package com.splitwise.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.splitwise.dto.Category;
import com.splitwise.dto.CreateExpenseResponse;
import com.splitwise.dto.CreditCardTransaction;
import com.splitwise.dto.SplitwiseTransaction;
import com.splitwise.entity.DBTransaction;
import com.splitwise.repository.TransactionsRepository;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class MyController {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private TransactionsRepository tranRepository;
	@Autowired
	private List<Category> categories;
	@Value("${app.splitwise.url}")
	private String splitwiseUrl;
	@Value("${app.splitwise.groupId}")
	private int groupId;
	
	private final Logger log = LoggerFactory.getLogger(MyController.class);

	private CsvSchema schema = CsvSchema.builder().addColumn("Description").addColumn("Type")
			.addColumn("Card Holder Name").addColumn("Date").addColumn("Time").addColumn("Amount").build();

	@PostMapping("/savetransactions")
	private String getResponse(@RequestParam("csvFile") MultipartFile file) throws IOException {
		MappingIterator<CreditCardTransaction> it = new CsvMapper().readerFor(CreditCardTransaction.class).with(schema)
				.readValues(file.getInputStream());
		
		List<CreditCardTransaction> allTransactions = it.readAll();
		allTransactions.remove(0);// header
		var originalCountRecords = allTransactions.size();
		
		// Remove PAYMENT type transactions
		allTransactions = allTransactions.stream()
				.filter(tran -> !tran.getType().equalsIgnoreCase("payment"))
				.filter(tran -> !tran.getType().equalsIgnoreCase("interest"))
				.collect(Collectors.toList());
		var duplicateRecords = new ArrayList<DBTransaction>();
		
		
		List<DBTransaction> uniqueTrans = new ArrayList<>();
		allTransactions.stream().forEach(tran -> {
			DBTransaction dbTransaction = new DBTransaction();
			dbTransaction.setDescription(tran.getDescription());
			dbTransaction.setType(tran.getType());
			dbTransaction.setCardHolderName(tran.getCardHolderName());
			dbTransaction.setDate(getSqlDate(tran));
			dbTransaction.setAmount(new BigDecimal(tran.getAmount()));

			Optional<Category> optionalCategory = findCategoryByDescription(parsedDescription(tran.getDescription()));
			dbTransaction.setCategoryId(optionalCategory.isPresent() ? optionalCategory.get().getId() : null);
			log.debug("Finding transaction by desc - {}", dbTransaction.getDescription());
			Optional<List<DBTransaction>> optionalTran = tranRepository.findByDescriptionAndTypeAndAmountAndDate(
					dbTransaction.getDescription(), dbTransaction.getType(), dbTransaction.getAmount(),
					dbTransaction.getDate());
			if (optionalTran.isPresent() && optionalTran.get().size() > 0) {
				duplicateRecords.addAll(optionalTran.get());
				log.debug("found duplicate transaction - {}", optionalTran.get());
			} else {
				uniqueTrans.add(dbTransaction);
			}
		});
		tranRepository.saveAll(uniqueTrans);
		var countDbInsertedRecords = uniqueTrans.size();
		return "Uploaded "+originalCountRecords+";"
				+ " Created "+ countDbInsertedRecords+ ";"+
		" Duplicate "+ duplicateRecords.size()+ ";";
	}

	@GetMapping("/categories")
	private List<Category> getAllCategories() {
		log.debug("Returning all categories");
		return categories;
	}

	@GetMapping("/transactions")
	private List<DBTransaction> getAllTransactions() throws IOException {
		return tranRepository.findByFiledWithSplitwiseOrderByDateDesc(false);
	}

	@PostMapping("/savecategory/{id}")
	private ResponseEntity<DBTransaction> saveCategory(@PathVariable(value = "id") Integer id,
			@RequestParam Integer categoryId) {
		Optional<DBTransaction> optionalDBTran = tranRepository.findById(id);
		Optional<Category> optionalCategory = categories.stream().filter(c -> c.getId() == categoryId).findFirst();
		if (optionalCategory.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}
		optionalDBTran.ifPresent(tran -> {
			tran.setCategoryId(categoryId);
		});
		DBTransaction savedTran = tranRepository.save(optionalDBTran.get());
		return ResponseEntity.ok(savedTran);
	}

	@PostMapping("/sendtosplitwise/{id}")
	private ResponseEntity<String> sendToSplitwise(@PathVariable(value = "id") Integer id) {
		log.debug("Updating transaction with id - {}",id);
		Optional<DBTransaction> optionalDBTran = tranRepository.findById(id);
		if(optionalDBTran.isPresent()) {
			var  creditCardTransaction = optionalDBTran.get();
			if(creditCardTransaction.isFiledWithSplitwise()) {
				log.error("Transaction already filed to splitwise - {}", id);
				return ResponseEntity.badRequest().body("Transaction already filed to Splitwise "+id);
				
			}
			if(creditCardTransaction.getCategoryId() == null) {
				log.error("Transaction is missing categoryId - {}", id);
				return ResponseEntity.badRequest().body("Transaction is missing categoryId "+id);
			}
		} 
		optionalDBTran.ifPresent(creditCardTransaction -> {
			SplitwiseTransaction splitwiseTransaction = new SplitwiseTransaction();
			float cost = creditCardTransaction.getAmount().floatValue();
			if (cost < 1) {
				splitwiseTransaction.setCost(String.valueOf(-1 * cost));
			} else {
				splitwiseTransaction.setCost(String.valueOf(cost));
			}
			splitwiseTransaction.setDescription(parsedDescription(creditCardTransaction.getDescription()));
			splitwiseTransaction.setDetails(null);
			splitwiseTransaction.setDate(
					creditCardTransaction.getDate().format(DateTimeFormatter.ofPattern("YYYY-MM-dd'T'HH:mm:ss")) + "Z");
			splitwiseTransaction.setRepeat_interval("never");
			splitwiseTransaction.setCurrency_code("CAD");
			splitwiseTransaction.setGroup_id(groupId);
			splitwiseTransaction.setSplit_equally(true);
			splitwiseTransaction.setCategory_id(findSplitwiseId(creditCardTransaction.getCategoryId()));
			// send to splitwise
			var expenseResponse = restTemplate.postForEntity(splitwiseUrl, splitwiseTransaction,
					CreateExpenseResponse.class).getBody();
			
			if(expenseResponse.errors()!= null && expenseResponse.errors().base() != null 
					&& expenseResponse.errors().base().size()> 0) {
			log.error("Failed to parse response from splitwise - {}", expenseResponse.errors().base());	
			}
			else {
				creditCardTransaction.setExpenseId(expenseResponse.expenses().get(0).id());
				creditCardTransaction.setFiledWithSplitwise(true);
				// update db
				tranRepository.save(creditCardTransaction);
				log.debug("Successfully created transaction in splitwise - {}", splitwiseTransaction);				
			}
			
		});

		return ResponseEntity.ok(String.valueOf(id));
	}

	private int findSplitwiseId(int id) {
		return categories.stream().filter(c -> c.getId() == id).findFirst().get().getSplitwiseId();
	}

	private Optional<Category> findCategoryByDescription(String desc) {
		return categories.stream().filter(
				c -> c.getPatterndescriptions().stream().anyMatch(patternDesc -> patternDesc.matcher(desc).find()))
				.findFirst();
	}

	private LocalDateTime getSqlDate(CreditCardTransaction tran) {
		String[] split = tran.getDate().split("/");
		String[] split2 = tran.getTime().substring(0, 5).split(":");

		return LocalDateTime.of(Integer.valueOf(split[2]), Integer.valueOf(split[0]), Integer.valueOf(split[1]),
				Integer.valueOf(split2[0]), Integer.valueOf(split2[1]));
	}

	private static String parsedDescription(String description) {
		int indexOfDoubleSpace = description.indexOf("  ");// Added for Refund description without multiple spaces "ADJ-BLS INTERNATIONAL FZE"
		String temp = indexOfDoubleSpace == -1? description: description.substring(0, indexOfDoubleSpace);
		return temp.replaceAll("[^a-zA-Z\s]", "").strip();
	}

}
