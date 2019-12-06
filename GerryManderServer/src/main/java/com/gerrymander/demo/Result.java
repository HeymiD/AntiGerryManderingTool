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
	public Map<DEMOGRAPHIC, Integer> demographicCompositions;
	public Map<PARTYNAME, Integer> votingCompositions;

	public Result(String stateName, int numPrecincts) {
		this.stateName = stateName;
		this.numPrecincts = numPrecincts;
		majorityMinorityPrecincts = new HashSet<String>();
		demographicCompositions= new HashMap<DEMOGRAPHIC, Integer>();
		for(DEMOGRAPHIC d:DEMOGRAPHIC.values()){
			demographicCompositions.put(d,0);
		}
		votingCompositions = new HashMap<PARTYNAME, Integer>();
		for(PARTYNAME p:PARTYNAME.values()){
			votingCompositions.put(p,0);
		}
	}

	public void addToResult(String precintId, DEMOGRAPHIC largestDemographic, PARTYNAME majorityParty)
	{
		numMajorityMinorityPrecincts++;
		majorityMinorityPrecincts.add(precintId);
		int demographicMajority = demographicCompositions.get(largestDemographic);
		demographicMajority++;
		System.out.println("DemogMajor: "+demographicCompositions.get(largestDemographic));
		demographicCompositions.put(largestDemographic, (demographicMajority));
		int numPartyMajority = votingCompositions.get(majorityParty);
		numPartyMajority++;
		System.out.println("PartyMajor : "+votingCompositions.get(majorityParty));
		votingCompositions.put(majorityParty,(numPartyMajority));
		return;
	}

	public String toString(){
		return "{\"numPrecincts\":" + numPrecincts + ",\"numMajMinPrecincts\":" + numMajorityMinorityPrecincts +
				",\"White\":" + ((double)demographicCompositions.get(DEMOGRAPHIC.WHITE))/numPrecincts * 100 +
				",\"Black\":" + ((double)demographicCompositions.get(DEMOGRAPHIC.AFROAM))/numPrecincts * 100 +
				",\"Hispanic\":" + ((double)demographicCompositions.get(DEMOGRAPHIC.HISPANIC))/numPrecincts * 100 +
				",\"Pacific\":" + ((double)demographicCompositions.get(DEMOGRAPHIC.PACISLAND))/numPrecincts * 100 +
				",\"Asian\":" + ((double)demographicCompositions.get(DEMOGRAPHIC.ASIAN))/numPrecincts * 100 +
				",\"Native\":" + ((double)demographicCompositions.get(DEMOGRAPHIC.NATIVE))/numPrecincts * 100 +
				",\"Other\":" + ((double)demographicCompositions.get(DEMOGRAPHIC.OTHER))/numPrecincts * 100 +
				",\"Democrat\":" + ((double)votingCompositions.get(PARTYNAME.DEMOCRAT))/numPrecincts * 100 +
				",\"Republican\":" + ((double)votingCompositions.get(PARTYNAME.REPUBLICAN))/numPrecincts * 100 +
				",\"Green\":" + ((double)votingCompositions.get(PARTYNAME.GREEN))/numPrecincts * 100 +
				",\"Libertarian\":" + ((double)votingCompositions.get(PARTYNAME.LIBERTARIAN))/numPrecincts * 100 + "}";
				
	}
}
