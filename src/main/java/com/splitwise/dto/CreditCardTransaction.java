package com.splitwise.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreditCardTransaction {
	@JsonProperty("Description") private String description;
	@JsonProperty("Type") private String type;
	@JsonProperty("Card Holder Name") private String cardHolderName;
	@JsonProperty("Date") private String date;
	@JsonProperty("Time") private String time;
	@JsonProperty("Amount") private String amount;
	
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "CreditCardTransaction [description=" + description + ", type=" + type + ", cardHolderName="
				+ cardHolderName + ", date=" + date + ", time=" + time + ", amount=" + amount + "]";
	}

	
}
