package com.gerrymander.demo;

import com.gerrymander.demo.models.concrete.District;
import com.gerrymander.demo.models.concrete.Precinct;

import java.util.*;

public class Cluster extends District {
	
	public ArrayList<Edge> edges;
	public ArrayList<Precinct> precincts;
	public int ID;

	public Cluster(int id){ID=id;}
	public boolean checkMajMin(){
		boolean value = false;
		return value;
	}
}
