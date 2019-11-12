package com.gerrymander.demo;

import java.util.*;

import com.gerrymander.demo.MeasuresCodeFiles.MeasuresCodeFiles.*;

//@Table(name="OldDistricts")
public class District extends BaseDistrict{
	
//	+id: int
//	+precincts: Set<Precinct>
//	+demographics  Map<DEMOGRAPHIC,double>
//	+totPop: int
//	+electionData: List<Election>
//	+isMajMin():boolean
//	+geoData: String
	
	public int id;
	public ArrayList<Precinct> precincts;
	public HashMap<DEMOGRAPHIC,Double> demographics;
	public int totPop;
	public ArrayList<Election> electionData;
	public String geoData;
	
	public District()
	{
		super();
	}
	
	public boolean isMajMin() 
	{
		for (DEMOGRAPHIC minority: demographics.keySet())
		{
			if ((demographics.get(minority)/totPop)>=0.5)
			{
				return true;
			}
		}
		return false;
	}

}
