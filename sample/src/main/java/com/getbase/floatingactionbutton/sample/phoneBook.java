package com.getbase.floatingactionbutton.sample;

public class phoneBook {
	public String sampleName;
	public String sampleNumber;
	
	
	public phoneBook(String name, String number){
		this.sampleName	= name;
		this.sampleNumber = number;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public String getSampleNumber() {
		return sampleNumber;
	}

	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}
}
