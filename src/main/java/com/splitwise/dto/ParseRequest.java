package com.splitwise.dto;

public class ParseRequest {

	private String input;
	private int group_id;
	private boolean autoSave;
	
	
	public ParseRequest(String input, int group_id, boolean autoSave) {
		super();
		this.input = input;
		this.group_id = group_id;
		this.autoSave = autoSave;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public int getGroup_id() {
		return group_id;
	}
	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}
	public boolean isAutoSave() {
		return autoSave;
	}
	public void setAutoSave(boolean autoSave) {
		this.autoSave = autoSave;
	}
	
	
}
