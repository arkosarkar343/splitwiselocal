package com.splitwise.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.splitwise.entity.DBTransaction;

@Repository
public interface TransactionsRepository extends CrudRepository<DBTransaction, Integer>{
	public Optional<List<DBTransaction>> findByDescriptionAndTypeAndAmountAndDate(String desc, String type,
			BigDecimal amount, LocalDateTime date);
	public List<DBTransaction> findByFiledWithSplitwiseOrderByDateDesc(boolean filedWithSplitwise);
	public Optional<DBTransaction> findById(Integer id);
}
