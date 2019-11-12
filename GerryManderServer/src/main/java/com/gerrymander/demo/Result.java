package com.gerrymander.demo;

import java.util.*;

import com.gerrymander.demo.MeasuresCodeFiles.MeasuresCodeFiles.Party;

public class Result {
	
//	+ state: String
//	+precinctDemographics: Map<int,  Map<DEMOGRAPHIC, double>>
//	+precinctParty: Map<int, Party>
	
	public String stateName;
	public Map<Integer,DEMOGRAPHIC> precinctDemographics;
	public Map<Integer, Party> precinctParty;
	
	public Result() {}
	
	public void addToResult(int precintId, DEMOGRAPHIC dem, Double ratio, Party party) 
	{
		return;
	}
	
}
