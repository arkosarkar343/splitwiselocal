package com.splitwise.dto;

public class SplitwiseTransaction {
	private String cost;
	private String description;
	private String details;
	private String date;
	private String repeat_interval;
	private String currency_code;
	private int category_id;
	private int group_id;
	private boolean split_equally;
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRepeat_interval() {
		return repeat_interval;
	}
	public void setRepeat_interval(String repeat_interval) {
		this.repeat_interval = repeat_interval;
	}
	public String getCurrency_code() {
		return currency_code;
	}
	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public int getGroup_id() {
		return group_id;
	}
	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}
	public boolean isSplit_equally() {
		return split_equally;
	}
	public void setSplit_equally(boolean split_equally) {
		this.split_equally = split_equally;
	}
	@Override
	public String toString() {
		return "SplitwiseTransaction [cost=" + cost + ", description=" + description + ", details=" + details
				+ ", date=" + date + ", repeat_interval=" + repeat_interval + ", currency_code=" + currency_code
				+ ", category_id=" + category_id + ", group_id=" + group_id + ", split_equally=" + split_equally + "]";
	}
	
	
	
	
}
