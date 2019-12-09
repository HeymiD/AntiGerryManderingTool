package com.gerrymander.demo.models.concrete;


import com.gerrymander.demo.*;
import com.gerrymander.demo.measures.StateInterface;
import com.gerrymander.demo.models.DAO.DistrictDAO;
//import com.gerrymander.demo.models.DAO.DistrictDAOInterface;
//import com.gerrymander.demo.models.Service.DistrictService;
import com.gerrymander.demo.models.DAO.PrecinctDAO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.PublicKey;
import java.sql.SQLException;
import java.util.*;

public class State
		implements StateInterface<Precinct, District> {
	private String name;//name is saved for later storage into the database
	private HashMap<String, District> districts;
//	@OneToMany
//	@MapKey(name="state")
	private Map<String, Precinct> precincts;
	@Transient
	public Map<String,District> oldDistricts;
	public Set<Cluster> clusters;
	public Set<Cluster> majMinClusters;
	public Result majMinPrecinctStats;
	public double userVoteThreshold;
	public double userDemographicThreshold;
	public ELECTIONTYPE userSelectedElection;
//	public boolean clusterListModified;
	public int population;

	public State(String name, Set<Precinct> inPrecincts) {
		this.name = name;
		this.districts = new HashMap<>();
		this.precincts = new HashMap<>();
		for (Precinct p : inPrecincts) {
			String districtID = p.getOriginalDistrictID();
			District d = districts.get(districtID);
			if (d == null) {
				d = new District();
				d.setID(districtID);
				d.setState(this);
				districts.put(districtID, d);

			}
			d.addPrecinct(p);
			this.precincts.put(p.getID(), p);
		}
		this.majMinPrecinctStats = new Result(this.name,this.precincts.size());
        this.population = districts.values().stream().mapToInt(District::getPopulation).sum();
        oldDistricts = new HashMap<String, District>();


	}
	public State(String name){
		this.name = name;
		this.districts = new HashMap<>();
		this.precincts = new HashMap<String, Precinct>();
//		this.population = districts.values().stream().mapToInt(District::getPopulation).sum();
		oldDistricts = new HashMap<String, District>();
		clusters = new HashSet<Cluster>();
		this.population=0;
	}

	public void putDistrict(District d){districts.put(d.getID(),d);}

	public String getName(){return name;}

	public Set<Precinct> getPrecincts(){
        return new HashSet<>(precincts.values());
		}

	public void setPrecincts(Map<String,Precinct> precincts){
		this.precincts=precincts;
	}

	public void addPrecinct(Precinct precinct){precincts.put(precinct.getID(),precinct);}

	public Set<District> getDistricts() {
		return new HashSet<>(districts.values());
	}

	public District getDistrict(String distID) {
		return districts.get(distID);
	}

	public Precinct getPrecinct(String precID) {
		return precincts.get(precID);
	}

	@Override
	public int getPopulation() {
		return this.population;
	}

	public Result findMajMinPrecincts(Double blockThreshold, Double votingThreshold, ELECTIONTYPE election){
		for(Precinct precinct : this.getPrecincts()){
			precinct.setIsMajorityMinority(precinct.findVotingBlock(majMinPrecinctStats,blockThreshold,
					votingThreshold,election));
		}
		return majMinPrecinctStats;
	}
	public boolean makeMajMinClusters(){

		boolean clusterListModified = false;
		for (Cluster c : clusters){
			Set<Cluster> neighbors = new HashSet<Cluster>();
			for (Edge edge: c.edges){
				neighbors.add(edge.getNeighbor(c));
			}
			for (Cluster neighbor: neighbors){
				boolean undo = false;
				Cluster combined = combineClusters(c,neighbor);
				int oldNumMajMinCluster = majMinClusters.size();
				if (combined.checkMajorityMinority(userDemographicThreshold,userVoteThreshold,userSelectedElection)){
					if(!majMinClusters.contains(combined)){
						majMinClusters.add(combined);
					}
				}
				if(majMinClusters.size()<oldNumMajMinCluster){
					undo_combineCluster(combined,c,neighbor);
					undo=true;
				}
				if(undo==false){
					clusterListModified=true;

				}
			}
		}
		return clusterListModified;
	}

	public Cluster combineClusters(Cluster c1, Cluster c2){

		clusters.remove(c1);
		districts.remove(c1.getID());
		if (majMinClusters.contains(c1)){
			majMinClusters.remove(c1);
		}
		Cluster combinedCluster = c1;
		clusters.remove(c2);
		districts.remove(c2.getID());
		for(DEMOGRAPHIC demograhpic: DEMOGRAPHIC.values()){
			int combined = combinedCluster.getClusterDemographics().get(demograhpic) + c2.getClusterDemographics().get(demograhpic);
			combinedCluster.getClusterDemographics().put(demograhpic,combined);
		}
		((District)combinedCluster).population = ((District)combinedCluster).population+((District)c2).population;
		for(PARTYNAME partyname :PARTYNAME.values()){
			int combined = combinedCluster.getElections().get(userSelectedElection).getVotes().get(partyname) +
					c2.getElections().get(userSelectedElection).getVotes().get(partyname);
			combinedCluster.getElections().get(userSelectedElection).getVotes().put(partyname, combined);
		}
		if (majMinClusters.contains(c2)){
			majMinClusters.remove(c2);
		}
		((District)combinedCluster).internalEdges = ((District)c1).internalEdges+((District)c2).internalEdges+1;
		Set<Edge[]> commonNeighbors = findCommonNeighbors(combinedCluster,c2);
		Set<Edge> averageEdges = new HashSet<Edge>();
		for (Edge[] edge_pair:commonNeighbors){
			try{
				averageEdges.add(makeAverageEdge(edge_pair[0],edge_pair[1]));
				for(Edge e:edge_pair){
					if(combinedCluster.edges.contains(e)){
						combinedCluster.edges.remove(e);
					}
				}
			}
			catch (NullPointerException e){

			}
		}
		((District)combinedCluster).externalEdges = combinedCluster.edges.size()-((District)combinedCluster).internalEdges;
		for (Edge e:averageEdges){
			combinedCluster.edges.add(e);
		}
		clusters.add(combinedCluster);
		districts.put(combinedCluster.getID(),combinedCluster);
		return combinedCluster;
	}
	public void undo_combineCluster(Cluster combined, Cluster c1, Cluster c2){
		if (majMinClusters.contains(combined)){
			majMinClusters.remove(combined);
		}
		clusters.remove(combined);
		districts.remove(combined.getID());
//		for(DEMOGRAPHIC demograhpic: DEMOGRAPHIC.values()){
//			int combinedDem = combined.getClusterDemographics().get(demograhpic) - c2.getClusterDemographics().get(demograhpic);
//			c1.getClusterDemographics().put(demograhpic,combinedDem);
//		}
//		for(PARTYNAME partyname :PARTYNAME.values()){
//			int combinedElection = combined.getElections().get(userSelectedElection).getVotes().get(partyname) -
//					c2.getElections().get(userSelectedElection).getVotes().get(partyname);
//			c1.getElections().get(userSelectedElection).getVotes().put(partyname, combinedElection);
//		}
		if(c1.checkMajorityMinority(userDemographicThreshold,userVoteThreshold,userSelectedElection)){
			majMinClusters.add(c1);
		}
		clusters.add(c1);
		districts.put(c1.getID(),c1);
		if(c2.checkMajorityMinority(userDemographicThreshold,userVoteThreshold,userSelectedElection)){
			majMinClusters.add(c2);
		}
		clusters.add(c2);
		districts.put(c2.getID(),c2);
		return;
	}
	public Set<Edge[]> findCommonNeighbors(Cluster c1, Cluster c2){
		Set<Edge[]> commonNeighbors = new HashSet<Edge[]>();
		for (Edge edge:c1.edges){
			for (Edge edge2: c2.edges){
				if(edge.getNeighbor(c1).getID()==edge2.getNeighbor(c2).getID()){
					Edge[] commonEdges = new Edge[2];
					commonEdges[0]=edge;
					commonEdges[1]=edge2;
					commonNeighbors.add(commonEdges);
				}
			}
		}
		for(Edge e:c2.edges){
			c1.edges.add(e);
		}
		for(Edge e:c1.edges){
			e.clusters.remove(e.src);
			e.clusters.add(c1);
			e.src=c1;
		}
		for(Edge e:c1.edges){
			Cluster neighbor = e.getNeighbor(c1);
			for(Edge edge:neighbor.edges){
				if(edge.dest.getID()==c1.getID()){
					edge.clusters.remove(edge.dest);
					edge.dest=c1;
					edge.clusters.add(c1);
				}
			}
		}
		return commonNeighbors;
	}

	public Edge makeAverageEdge(Edge e1,Edge e2){
		if ((e1.src.getID()!=e2.src.getID()) ||(e1.dest.getID()!=e2.dest.getID())){
			System.out.print("They are not common edges.");
			return null;
		}
		Edge averageEdge = new Edge(e1.src,e1.dest);
		averageEdge.weight=(e1.weight+e2.weight)/2;
		return averageEdge;
	}




}
