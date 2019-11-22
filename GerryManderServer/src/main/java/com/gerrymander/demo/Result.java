package com.gerrymander.demo;

import java.util.*;


public class Result {
	
//	+ state: String
//	+precinctDemographics: Map<int,  Map<DEMOGRAPHIC, double>>
//	+precinctParty: Map<int, Party>
	
	public String stateName;
	public Map<Integer,DEMOGRAPHIC> precinctDemographics;
	public Map<Integer, PARTYNAME> precinctParty;
	
	public Result() {}
	
	public void addToResult(int precintId, DEMOGRAPHIC dem, Double ratio, PARTYNAME party)
	{
		return;
	}
	
}
