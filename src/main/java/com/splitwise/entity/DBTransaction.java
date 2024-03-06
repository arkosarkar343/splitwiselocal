package com.splitwise.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transactions")
public class DBTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String description;
	private String type;
	private String cardHolderName;
	private LocalDateTime  date;
	private BigDecimal amount;
	private Integer categoryId;
	private boolean filedWithSplitwise;
	private Long expenseId;
	
	public DBTransaction() {}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCardHolderName() {
		return cardHolderName;
	}
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}
	public LocalDateTime  getDate() {
		return date;
	}
	public void setDate(LocalDateTime  date) {
		this.date = date;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public boolean isFiledWithSplitwise() {
		return filedWithSplitwise;
	}
	public void setFiledWithSplitwise(boolean filedWithSplitwise) {
		this.filedWithSplitwise = filedWithSplitwise;
	}
	
	
	
	public Long getExpenseId() {
		return expenseId;
	}
	public void setExpenseId(Long expenseId) {
		this.expenseId = expenseId;
	}
	@Override
	public String toString() {
		return "DBTransaction [id=" + id + ", description=" + description + ", type=" + type + ", cardHolderName="
				+ cardHolderName + ", date=" + date + ", amount=" + amount + ", categoryId=" + categoryId
				+ ", filedWithSplitwise=" + filedWithSplitwise + "]";
	}
	
}
