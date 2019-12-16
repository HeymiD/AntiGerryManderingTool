package com.gerrymander.demo.models.concrete;


import com.gerrymander.demo.*;
import com.gerrymander.demo.algorithm.SortByPopulation;
import com.gerrymander.demo.measures.StateInterface;
//import com.gerrymander.demo.models.DAO.DistrictDAOInterface;
//import com.gerrymander.demo.models.Service.DistrictService;

import java.util.*;

public class State
		implements StateInterface<Precinct, District> {
	private String name;//name is saved for later storage into the database
	private HashMap<String, District> districts;
//	@OneToMany
//	@MapKey(name="state")
	private Map<String, Precinct> precincts;
//	@Transient
	public Map<String,Cluster> oldDistricts;
	public ArrayList<Cluster> clusters;
	public ArrayList<Cluster> majMinClusters;
	public Result majMinPrecinctStats;
	public double userVoteThreshold;
	public double userDemographicThreshold;
	public ELECTIONTYPE userSelectedElection=ELECTIONTYPE.Presidential2016;
//	public boolean clusterListModified;
	public int population;
	public Set<Cluster> combinedClusters;
	public Set<Cluster> visited;
	public String[] demString;

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
        oldDistricts = new HashMap<String, Cluster>();


	}
	public State(String name){
		this.name = name;
		this.districts = new HashMap<>();
		this.precincts = new HashMap<String, Precinct>();
//		this.population = districts.values().stream().mapToInt(District::getPopulation).sum();
		oldDistricts = new HashMap<String, Cluster>();
		clusters = new ArrayList<Cluster>();
		this.population=0;
		majMinClusters=new ArrayList<Cluster>();
		combinedClusters = new HashSet<Cluster>();
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
	public boolean makeMajMinClusters() {
	    combinedClusters.clear();
	    int cSize = clusters.size();
        boolean clusterListModified = false;
        Iterator<Cluster> iterator = clusters.iterator();
        while (iterator.hasNext()) {
            Cluster currCluster = iterator.next();
            if(currCluster.population<=600000){
//                currCluster = iterator.next();
                List<Cluster> neighbors = new LinkedList<>();
                for(Edge e:currCluster.edges){
                    Cluster nCluster = e.getNeighbor(currCluster);
                    if(nCluster!=null &&
                            !nCluster.getID().equals(currCluster.getID())){
                        neighbors.add(nCluster);
                    }
                }
//            Collections.sort(neighbors,new SortByPopulation());
                for(Cluster n: neighbors){
                    Cluster neighbor = findCluster(n.getID());
                    if(neighbor.population>300000){
                        continue;
                    }
                    if (testCombineClusters(currCluster, neighbor) != null) {
                        clusterListModified = true;
                        combineClusters(currCluster, neighbor);
                        combinedClusters.add(neighbor);
                        iterator.remove();
                        cSize--;
                        break;
                    }
                }
            }
        }
        return clusterListModified;
    }

    //Neighbor always eats current so current gets removed
	public void combineClusters(Cluster current, Cluster neighbor) {

        System.out.println("Combining "+current.getID()+" and "+neighbor.getID());
        neighbor.precinctsCluster.addAll(current.precinctsCluster);
        neighbor.precinctsCluster.forEach(p->{p.newDistrictID=neighbor.getID();});
//        System.out.println("Num of Precincts: "+neighbor.precinctsCluster.size());
        System.out.println("OLD POP: "+neighbor.population);
        neighbor.setPopulation(neighbor.getPopulation()+current.getPopulation());
        System.out.println("Pop: "+neighbor.population);
        System.out.println("CurPop: "+current.population);

        for (DEMOGRAPHIC demograhpic : DEMOGRAPHIC.values()) {
            int combined = current.getClusterDemographics().get(demograhpic) + neighbor.getClusterDemographics().get(demograhpic);
            neighbor.getClusterDemographics().put(demograhpic, combined);
        }
        for (PARTYNAME partyname : PARTYNAME.values()) {
            int combined = neighbor.getElections().get(userSelectedElection).getVotes().get(partyname) +
                    current.getElections().get(userSelectedElection).getVotes().get(partyname);
            neighbor.getElections().get(userSelectedElection).getVotes().put(partyname, combined);
        }
        neighbor.setGop_vote(current.getGOPVote() + neighbor.getGOPVote());
        neighbor.setDem_vote(neighbor.getDEMVote() + current.getDEMVote());
        int internalEdges = 0;
        int externalEdges = 0;
        for (Precinct p : neighbor.precinctsCluster) {
            for (String neighborID : p.getNeighborIDs()) {
                if (neighbor.precinctsCluster.stream().anyMatch(precinct -> {
                    return precinct.getID().equals(neighborID);
                })) {
                    internalEdges++;
                }
                else{externalEdges++;}
            }
        }
        neighbor.internalEdges = internalEdges;
        neighbor.externalEdges = externalEdges;
        for(Edge e:neighbor.edges){
            if(e.neighbor1.getID().equals(e.neighbor2.getID())){
                System.out.println("NeighborPreExisting: SELF LOOP");
            }
        }
        for (Edge edge : current.edges) {
            edge.updateNeighbor(current, neighbor);
            if (!edge.neighbor1.getID().equals(edge.neighbor2.getID())) {
                neighbor.edges.add(edge);
//                System.out.println("Edge\n n1: "+edge.neighbor1.getID()
//                        +" n2: "+edge.neighbor2.getID());
//                for(Edge e:neighbor.edges){
//                    if(e.neighbor1.getID().equals(e.neighbor2.getID())){
//
//                        System.out.println("SIKIMA");
//                        System.exit(0);
//                    }
//                }
            }
        }
        Iterator<Edge> iterator = neighbor.edges.iterator();
        while(iterator.hasNext()){
            Edge e = iterator.next();
            if(e.neighbor1.getID().equals(e.neighbor2.getID())
                    ||findCluster(e.neighbor1.getID())==null || findCluster(e.neighbor1.getID())==null){
                System.out.println("YIKES!");
                iterator.remove();
            }
        }


//        for (Edge edge : neighbor.edges) {
//            System.out.println("Current Id: "+current.getID());
//            System.out.println("Edge: ");
//            System.out.println("Neighbor1: "+edge.neighbor1.getID()+" POP: "+edge.neighbor1.getPopulation());
//            System.out.println("Neighbor2: "+edge.neighbor2.getID()+" POP: "+edge.neighbor2.getPopulation());
//        }

    }

	public Cluster testCombineClusters(Cluster c1, Cluster c2){
        Cluster combinedCluster = new Cluster(c1.getID());
        combinedCluster.clusterDemographics.putAll(c1.getClusterDemographics());
		combinedCluster.elections.putAll(c1.elections);
		combinedCluster.setDem_vote(c1.getDEMVote());
		combinedCluster.setGop_vote(c1.getGOPVote());
		combinedCluster.population=c1.getPopulation()+c2.getPopulation();
		combinedCluster.externalEdges =c1.externalEdges+c2.externalEdges-1;

        for(DEMOGRAPHIC demograhpic: DEMOGRAPHIC.values()){
            int combined = combinedCluster.getClusterDemographics().get(demograhpic) + c2.getClusterDemographics().get(demograhpic);
            combinedCluster.getClusterDemographics().put(demograhpic,combined);
        }
        for(PARTYNAME partyname :PARTYNAME.values()){
            int combined = combinedCluster.getElections().get(userSelectedElection).getVotes().get(partyname) +
                    c2.getElections().get(userSelectedElection).getVotes().get(partyname);
            combinedCluster.getElections().get(userSelectedElection).getVotes().put(partyname, combined);
        }
        combinedCluster.setGop_vote(c1.getGOPVote()+c2.getGOPVote());
        combinedCluster.setDem_vote(c1.getDEMVote()+c2.getDEMVote());
//        if(combinedCluster.population>700000){
//            return null;
//        }
        boolean majmin = false;
        int newSizeMajMinClusters = 0;
        if(combinedCluster.checkMajorityMinority(userDemographicThreshold,userVoteThreshold,userSelectedElection,demString)){
            majmin=true;
            newSizeMajMinClusters++;
        }
        if(majmin==false){
            return null;
        }
        if(c1.checkMajorityMinority(userDemographicThreshold,userVoteThreshold,userSelectedElection,demString)){
            newSizeMajMinClusters--;
        }
        if(c2.checkMajorityMinority(userDemographicThreshold,userVoteThreshold,userSelectedElection,demString)){
            newSizeMajMinClusters--;
        }
        if(newSizeMajMinClusters>0){
            System.out.println("Non Majority minorities combining:");
            System.out.println("C1: "+c1.getID());
            System.out.println("C2: "+c2.getID());
            return combinedCluster;
        }
        if(newSizeMajMinClusters==0){
            return combinedCluster;
        }
        else{

            return null;
        }
    }

    public Cluster findCluster(String ID){
        for(Cluster c:clusters){
            if(c.getID().equals(ID)){
                return c;
            }
        }
        System.out.println("Neighbor NOT FOUND ID was "+ID);
        return null;
    }
}
