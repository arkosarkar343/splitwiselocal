package com.splitwise.dto;

import java.util.List;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Category {
	private Integer id;
	private String name;
	private List<String> descriptions;
	private Integer splitwiseId;
	
	@JsonIgnore private List<Pattern> patterndescriptions;

	public Category() {}
	
	public Category(Integer id, String name, List<String> descriptions) {
		super();
		this.id = id;
		this.name = name;
		this.descriptions = descriptions;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(List<String> descriptions) {
		this.descriptions = descriptions;
	}

	public Integer getSplitwiseId() {
		return splitwiseId;
	}

	public void setSplitwiseId(Integer splitwiseId) {
		this.splitwiseId = splitwiseId;
	}
	
	public List<Pattern> getPatterndescriptions() {
		return patterndescriptions;
	}

	public void setPatterndescriptions(List<Pattern> patterndescriptions) {
		this.patterndescriptions = patterndescriptions;
	}
	
	
}
