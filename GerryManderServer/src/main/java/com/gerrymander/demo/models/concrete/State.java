package com.gerrymander.demo.models.concrete;


import com.gerrymander.demo.*;
import com.gerrymander.demo.measures.StateInterface;
import com.gerrymander.demo.models.DAO.DistrictDAO;
//import com.gerrymander.demo.models.DAO.DistrictDAOInterface;
//import com.gerrymander.demo.models.Service.DistrictService;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.PublicKey;
import java.util.*;
public class State
		implements StateInterface<Precinct, District> {

	private String name;//name is saved for later storage into the database
	private HashMap<String, District> districts;
	@OneToMany
	private HashMap<String, Precinct> precincts;
	@OneToMany
	public HashMap<Integer,District> oldDistricts;
	public LinkedList<Cluster> clusters;
	public ArrayList<Cluster> majMinClusters;
	public Result majMinPrecincts;
//	public boolean clusterListModified;
	private int population;

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
		this.majMinPrecincts = new Result(this.name,this.precincts.size());
        this.population = districts.values().stream().mapToInt(District::getPopulation).sum();
        oldDistricts = new HashMap<Integer,District>();

	}

	public Set<Precinct> getPrecincts(){
        return new HashSet<>(precincts.values());
		}

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

	public Result findmajMinPrecincts(Long blockThreshold, Long votingThreshold, ELECTIONTYPE election,
									  Set<DEMOGRAPHIC> combinedDemographics){
		for(Precinct precinct : this.getPrecincts()){
			precinct.setIsMajorityMinority(precinct.findVotingBlock(majMinPrecincts,blockThreshold,
					votingThreshold,election,combinedDemographics ));
		}
		return majMinPrecincts;
	}
	public boolean makeMajMinClusters(){

		boolean clusterListModified = false;
		for (Cluster c : clusters){
			ArrayList<Cluster> neighbors = new ArrayList<Cluster>();
			for (Edge edge: c.edges){
				neighbors.add(edge.getNeighbor(c));
			}
			for (Cluster neighbor: neighbors){
				boolean undo = false;
				Cluster combined = combineClusters(c,neighbor);
				int oldNumMajMinCluster = majMinClusters.size();
				if (combined.checkMajMin()){
					if(!majMinClusters.contains(combined)){
						majMinClusters.add(combined);
					}
				}
				if(majMinClusters.size()<oldNumMajMinCluster){
					undo_combineCluster(c,neighbor,combined);
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
		clusters.remove(c2);
		if (majMinClusters.contains(c2)){
			majMinClusters.remove(c2);
		}
		ArrayList<Edge[]> commonNeighbors = findCommonNeighbors(c1,c2);
		ArrayList<Edge> averageEdges = new ArrayList<Edge>();
		for (Edge[] edge_pair:commonNeighbors){
			try{
				averageEdges.add(makeAverageEdge(edge_pair[0],edge_pair[1]));
				for(Edge e:edge_pair){
					if(c1.edges.contains(e)){
						c1.edges.remove(e);
					}
				}
			}
			catch (NullPointerException e){

			}
		}
		for (Edge e:averageEdges){
			c1.edges.add(e);
		}
		return c1;
	}
	public void undo_combineCluster(Cluster combined, Cluster c1, Cluster c2){
		if (majMinClusters.contains(combined)){
			majMinClusters.remove(combined);
		}
		clusters.remove(combined);
		if(c1.checkMajMin()){
			majMinClusters.add(c1);
		}
		clusters.add(c1);
		if(c2.checkMajMin()){
			majMinClusters.add(c2);
		}
		clusters.add(c2);
		return;
	}
	public ArrayList<Edge[]> findCommonNeighbors(Cluster c1, Cluster c2){
		ArrayList<Edge[]> commonNeighbors = new ArrayList<Edge[]>();
		for (Edge edge:c1.edges){
			for (Edge edge2: c2.edges){
				if(edge.getNeighbor(c1).ID==edge2.getNeighbor(c2).ID){
					Edge[] commonEdges = new Edge[2];
					commonEdges[0]=edge;
					commonEdges[1]=edge2;
					commonNeighbors.add(commonEdges);
				}
			}
		}
		return commonNeighbors;
	}

	public Edge makeAverageEdge(Edge e1,Edge e2){
		if ((e1.src.ID!=e2.src.ID) ||(e1.dest.ID!=e2.dest.ID)){
			System.out.print("They are not common edges.");
			return null;
		}
		Edge averageEdge = new Edge(e1.src,e1.dest);
		averageEdge.weight=(e1.weight+e2.weight)/2;
		return averageEdge;
	}


}