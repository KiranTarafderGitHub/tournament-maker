package com.kiran.league.maker.persist.entity;

public enum TournamentType {
	
	RR1L("Round Robin 1 Leg",1),RR2L("Round Robin 2 Leg",2);
	
	private String displayName;
	
	private int legCount;

	TournamentType(String displayName, int legCount ) {
		this.displayName = displayName;
		this.legCount = legCount;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	
	public int getLegCount() {
		return legCount;
	}

	public void setLegCount(int legCount) {
		this.legCount = legCount;
	}

	public static TournamentType getTournamentForDisplayName(String displayName)
	{
		switch(displayName)
        {
            case "Round Robin 1 Leg":
                return RR1L;
            case "Round Robin 2 Leg":
                return RR2L;
            
        }
        throw new IllegalArgumentException("TournamentType " + displayName + " not supported");
	}
}
