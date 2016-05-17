package com.getbase.floatingactionbutton.sample;

public class dumpclass {
	public String sampletext;
	public String sampleimg;
	
	
	public dumpclass(String data, String img){
		this.sampletext	=	data;
		this.sampleimg = img;
	}

	public String getSampleimg() {
		return sampleimg;
	}

	public void setSampleimg(String sampleimg) {
		this.sampleimg = sampleimg;
	}

	public String getSampletext() {
		return sampletext;
	}

	public void setSampletext(String sampletext) {
		this.sampletext = sampletext;
	}
}
