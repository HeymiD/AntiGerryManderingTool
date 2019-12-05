package com.gerrymander.demo;

import java.util.*;


public class Result {

//	+ state: String
//	+precinctDemographics: Map<int,  Map<DEMOGRAPHIC, double>>
//	+precinctParty: Map<int, Party>

	private String stateName;
	private int numPrecincts;
	private int numMajorityMinorityPrecincts = 0;
	public Set<String> majorityMinorityPrecincts;
	public HashMap<DEMOGRAPHIC, Integer> demographicCompositions;
	public HashMap<PARTYNAME, Integer> votingCompositions;

	public Result(String stateName, int numPrecincts) {
		this.stateName = stateName;
		this.numPrecincts = numPrecincts;
	}

	public void addToResult(String precintId, DEMOGRAPHIC largestDemographic, PARTYNAME majorityParty)
	{
		numMajorityMinorityPrecincts++;
		majorityMinorityPrecincts.add(precintId);
		int demographicMajority = demographicCompositions.get(largestDemographic);
		demographicCompositions.put(largestDemographic, Integer.valueOf(demographicMajority++));
		int numPartyMajority = votingCompositions.get(majorityParty);
		votingCompositions.put(majorityParty, Integer.valueOf(numPartyMajority++));
		return;
	}

	public String toString(){
		return "{\"numPrecincts\":" + numPrecincts + ",\"numMajMinPrecincts\":" + numMajorityMinorityPrecincts +
				",\"White\":" + demographicCompositions.get(DEMOGRAPHIC.WHITE)/numPrecincts * 100 +
				",\"Black\":" + demographicCompositions.get(DEMOGRAPHIC.AFROAM)/numPrecincts * 100 +
				",\"Hispanic\":" + demographicCompositions.get(DEMOGRAPHIC.HISPANIC)/numPrecincts * 100 +
				",\"Pacific\":" + demographicCompositions.get(DEMOGRAPHIC.PACISLAND)/numPrecincts * 100 +
				",\"Asian\":" + demographicCompositions.get(DEMOGRAPHIC.ASIAN)/numPrecincts * 100 +
				",\"Native\":" + demographicCompositions.get(DEMOGRAPHIC.NATIVE)/numPrecincts * 100 +
				",\"Other\":" + demographicCompositions.get(DEMOGRAPHIC.OTHER)/numPrecincts * 100 +
				",\"Democrat\":" + votingCompositions.get(PARTYNAME.DEMOCRAT)/numPrecincts * 100 +
				",\"Republican\":" + votingCompositions.get(PARTYNAME.REPUBLICAN)/numPrecincts * 100 +
				",\"Green\":" + votingCompositions.get(PARTYNAME.GREEN)/numPrecincts * 100 +
				",\"Libertarian\":" + votingCompositions.get(PARTYNAME.LIBERTARIAN)/numPrecincts * 100 + "}";
				
	}
}
