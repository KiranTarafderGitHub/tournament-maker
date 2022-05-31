package com.kiran.league.maker.common.bean.rest;

import org.springframework.web.multipart.MultipartFile;

public class RestoreBean {

	MultipartFile jsonFile;

	public MultipartFile getJsonFile() {
		return jsonFile;
	}

	public void setJsonFile(MultipartFile jsonFile) {
		this.jsonFile = jsonFile;
	}

	@Override
	public String toString() {
		return "RestoreBean [jsonFile=" + jsonFile + "]";
	}
	
}
