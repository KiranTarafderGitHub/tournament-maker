package com.kiran.league.maker.persist.dto;

public enum Result {

	WIN("Win",3),DRAW("Draw",1),LOSS("Loss",0);
	
	private String displayName;
	
	private Integer point;
	
	private Result(String displayName,Integer point) {
		this.displayName=displayName;
		this.point=point;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}
	
	
}
