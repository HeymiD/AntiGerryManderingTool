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
	
	public void addToResult(String precintId, Set<DEMOGRAPHIC> demographicMajority, PARTYNAME majorityParty)
	{
		numMajorityMinorityPrecincts++;
		majorityMinorityPrecincts.add(precintId);
		for(DEMOGRAPHIC demographic: demographicMajority){
			int numDemogrpahicMajority = demographicCompositions.get(demographic);
			demographicCompositions.put(demographic, Integer.valueOf(numDemogrpahicMajority++));
		}
		int numPartyMajority = votingCompositions.get(majorityParty);
		votingCompositions.put(majorityParty, Integer.valueOf(numPartyMajority++));
		return;
	}
}
