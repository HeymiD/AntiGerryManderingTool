package com.gerrymander.demo.algorithm;

import com.gerrymander.demo.*;
import com.gerrymander.demo.measures.DefaultMeasures;
import com.gerrymander.demo.measures.DistrictInterface;
import com.gerrymander.demo.models.DAO.PrecinctDAO;
import com.gerrymander.demo.models.concrete.District;
import com.gerrymander.demo.models.concrete.Precinct;
import com.gerrymander.demo.models.concrete.State;

import javax.swing.text.html.HTMLDocument;
import java.util.*;
import java.util.Comparator;
import java.util.Map.Entry;

public class Algorithm
{
    private State state;
    private HashMap<String, String> precinctDistrictMap; //precinctID --> districtID

    //calculates an aggregate measure score (double) for a given district
    private MeasureFunction<Precinct, District> districtScoreFunction;
    private HashMap<District, Double> currentScores;
    private District currentDistrict = null;
    public Map<FACTOR,Double> factors;
    public Map<Measure,Double> weights;
    public Double popThreshMax;
    public Double popThreshMin;
    public Double voteThresh;
    public int targetNumDist;
    public String[] demString;


    public MeasureFunction<Precinct, District> getDistrictScoreFunction() {
        return districtScoreFunction;
    }

    public void setDistrictScoreFunction(MeasureFunction<Precinct, District> districtScoreFunction) {
        this.districtScoreFunction = districtScoreFunction;
    }

    public Algorithm(State state,
                     MeasureFunction<Precinct, District> districtScoreFunction) {
        this.state = state;
        this.precinctDistrictMap = new HashMap<String, String>();
        this.districtScoreFunction = districtScoreFunction;
        allocatePrecincts(state);
        updateScores();
    }

    public Algorithm(State state){
        this.state = state;
    }

    // Determine the initial districts of precincts using a bfs
    // Very fast, but not ideal for compactness
    // Experimental - use a modified breadth first search
    public void allocatePrecinctsExp(State state) {
        HashSet<Precinct> unallocatedPrecincts = new HashSet<Precinct>();
        // Populate unallocated precincts
        for (District d : state.getDistricts()) {
            for (Precinct p : d.getPrecincts()) {
                unallocatedPrecincts.add(p);
            }
        }
        // Hash districts to numbers
        HashMap<Integer, DistrictInterface> myDistricts = new HashMap<>();
        int num_districts = 0;
        for (District d : state.getDistricts()) {
            myDistricts.put(num_districts, d);
            num_districts += 1;
        }
        // Strip out all precincts from each district
        for (District d : state.getDistricts()) {
            for (Precinct p : d.getPrecincts()) {
                d.removePrecinct(p);
            }
        }
        // Go through precincts in a BFS and just add them to the district corresponding to
        // (total_population / current_population) * num_districts
        // Whenever we run out, just grab a new district from the set.
        ArrayList<Precinct> ptp = new ArrayList<Precinct>();
        int curPop = 0;
        DistrictInterface prevDist = myDistricts.get(0);
        while (!unallocatedPrecincts.isEmpty()) {
            Precinct nextPrec = unallocatedPrecincts.iterator().next();
            unallocatedPrecincts.remove(nextPrec);
            ptp.add(nextPrec);
            while (!ptp.isEmpty()) {
                Precinct curPrec = ptp.get(0);
                curPop += curPrec.getPopulation();
                DistrictInterface curDist =
                        myDistricts.get((int)Math.ceil((curPop * 1.0) / state.getPopulation() * num_districts) - 1);
                // Add the current precinct to the current district
                System.out.println(ptp.size() + " " + unallocatedPrecincts.size());
                precinctDistrictMap.put(curPrec.getID(), curDist.getID());

                ptp.remove(0);
                unallocatedPrecincts.remove(curPrec);
                curDist.addPrecinct(curPrec);
                // If we've moved on to a new district, discard our current queue
                if (prevDist != curDist) {
                    ptp = new ArrayList<Precinct>();
                    prevDist = curDist;
                }
                // Add any new connections
                for (String s : curPrec.getNeighborIDs()) {
                    Precinct n = state.getPrecinct(s);
                    if (unallocatedPrecincts.contains(n) && !ptp.contains(n)) {
                        ptp.add(n);
                    }
                }
            }
        }
    }

    // Determine the initial districts of precincts
    public void allocatePrecincts(State state) {
        HashSet<Precinct> unallocatedPrecincts = new HashSet<Precinct>();
        // Populate unallocated precincts
        for (District d : state.getDistricts()) {
            unallocatedPrecincts.addAll(d.getPrecincts());
        }
        // For each district, select a random precinct
        HashSet<Precinct> seedPrecincts = new HashSet<Precinct>();
        for (District d : state.getDistricts()) {
            int selectedIndex = (int) Math.floor(Math.random() * unallocatedPrecincts.size());
            Precinct selectedPrecinct = null;
            int i = 0;
            for (Precinct p : unallocatedPrecincts) {
                if (i == selectedIndex) {
                    seedPrecincts.add(p);
                    selectedPrecinct = p;
                    for (District dp : state.getDistricts()) {
                        if (dp.getPrecincts().contains(p)) {
                            dp.removePrecinct(p);
                        }
                    }
                    d.addPrecinct(p);
                    precinctDistrictMap.put(p.getID(), d.getID());
                }
                i++;
            }
            unallocatedPrecincts.remove(selectedPrecinct);
        }
        // Strip out all but one precinct from each district
        for (District d : state.getDistricts()) {
            for (Precinct p : d.getPrecincts()) {
                if (!seedPrecincts.contains(p)) {
                    d.removePrecinct(p);
                }
            }
        }
        // For each district, add new precincts until a population threshold is met
        System.out.println(unallocatedPrecincts.size() + " before initial alloc.");
        int idealPop = state.getPopulation() / state.getDistricts().size();
        boolean outOfMoves = false;
        Set<District> ds = state.getDistricts();
        int highestPop = -1;
        while (outOfMoves == false) {
            outOfMoves = true;
            for (District d : ds) {
                // Add precincts until population > highestPop
                for (Precinct pd : d.getPrecincts()) {
                    for (String s : pd.getNeighborIDs()) {
                        Precinct p = state.getPrecinct(s);
                        if (unallocatedPrecincts.contains(p)) {
                            unallocatedPrecincts.remove(p);
                            d.addPrecinct(p);
                            precinctDistrictMap.put(p.getID(), d.getID());
                            outOfMoves = false;
                            if (d.getPopulation() >= highestPop) {
                                highestPop = d.getPopulation();
                                break;
                            }
                        }
                        if (d.getPopulation() >= highestPop) {
                            highestPop = d.getPopulation();
                            break;
                        }
                    }

                }
            }
        }
        // Allocate remaining unallocated precincts
        System.out.println("Adding remaining precincts.");
        int loopCheck = -1;
        while (!unallocatedPrecincts.isEmpty()) {
            if (loopCheck != unallocatedPrecincts.size()) {
                loopCheck = unallocatedPrecincts.size();
            } else {
                // Set to default if all else fails.
                for (Precinct p : unallocatedPrecincts) {
                    precinctDistrictMap.put(p.getID(), p.getOriginalDistrictID());
                    state.getDistrict(p.getOriginalDistrictID()).addPrecinct(p);
                }
                unallocatedPrecincts.clear();
            }
            System.out.println(unallocatedPrecincts.size() + " left.");
            HashSet<Precinct> newlyAdded = new HashSet<Precinct>();
            for (Precinct p : unallocatedPrecincts) {
                String myDistrict = null;
                for (String s : p.getNeighborIDs()) {
                    if (precinctDistrictMap.get(s) != null) {
                        myDistrict = precinctDistrictMap.get(s);
                    }
                }
                if (myDistrict != null) {
                    precinctDistrictMap.put(p.getID(), myDistrict);
                    state.getDistrict(myDistrict).addPrecinct(p);
                    newlyAdded.add(p);
                }
            }
            unallocatedPrecincts.removeAll(newlyAdded);
        }
    }

    public String getPrecinctDistrictID(Precinct p) {
        return precinctDistrictMap.get(p.getID());
    }

    public Move<Precinct, District> makeMove() {
        if (currentDistrict == null) {
            currentDistrict = getWorstDistrict();
        }
        District startDistrict = currentDistrict;
        Move m = getMoveFromDistrict(startDistrict);
        if (m == null) {
            return makeMove_secondary();
        }
        return m;
    }

    public Move getMoveFromDistrict(District startDistrict) {
        Set<Precinct> precincts = startDistrict.getBorderPrecincts();
        //Set<Precinct> precincts = startDistrict.getPrecincts();
        for (Precinct p : precincts) {
            Set<String> neighborIDs = p.getNeighborIDs();
            for (String n : neighborIDs) {
                if (startDistrict.getPrecinct(n) == null) {
                    District neighborDistrict = state.getDistrict(precinctDistrictMap.get(n));
                    //System.out.println("Start district: " + startDistrict.getID() + ", Neighbor District: " + neighborDistrict.getID() + ", Precinct: " + p.getID());
                    Move move = testMove(neighborDistrict, startDistrict, p);
                    if (move != null) {
                        System.out.println("Moving p to neighborDistrict(neighborID = " + n + ")");
                        currentDistrict = startDistrict;
                        return move;
                    }
                    move = testMove(startDistrict, neighborDistrict, neighborDistrict.getPrecinct(n));
                    if (move != null) {
                        System.out.println("Moving n to Start district: " + startDistrict.getID());
                        currentDistrict = startDistrict;
                        return move;
                    }
                    System.out.println("Move not found for precinct " + n);
                }
            }
        }
        System.out.println("Move not found for district " + startDistrict.getID());
        return null;
    }

    // Returns the confirmed move if successful, otherwise returns null.
    private Move testMove(District to, District from, Precinct p) {
        Move m = new Move<>(to, from, p);
        double initial_score = currentScores.get(to) + currentScores.get(from);
        m.execute();
        if (!checkContiguity(p, from)) {
            m.undo();
            return null;
        }
        double to_score = rateDistrict(to);
        double from_score = rateDistrict(from);
        double final_score = (to_score + from_score );
        double change = final_score - initial_score;
        if (change <= 0) {
            m.undo();
            return null;
        }
        currentScores.put(to, to_score);
        currentScores.put(from, from_score);
        precinctDistrictMap.put(p.getID(), to.getID());
        return m;
    }

    // Manually force a move. Return true if both parameters are valid.
    public Move<Precinct, District> manualMove(String precinct, String district) {
        District from = state.getDistrict(precinctDistrictMap.get(precinct));
        if (from == null) {
            return null;
        }
        Precinct p = from.getPrecinct(precinct);
        District to = state.getDistrict(district);
        if (to == null) {
            return null;
        }
        if (p == null) {
            return null;
        }
        Move<Precinct, District> m = new Move<>(to, from, p);
        double initial_score = currentScores.get(to) + currentScores.get(from);
        m.execute();
        double to_score = rateDistrict(to);
        double from_score = rateDistrict(from);
        currentScores.put(to, to_score);
        currentScores.put(from, from_score);
        precinctDistrictMap.put(p.getID(), to.getID());
        return m;
    }

    public District getWorstDistrict() {
        District worstDistrict = null;
        double minScore = Double.POSITIVE_INFINITY;
        for (District d : state.getDistricts()) {
            double score = currentScores.get(d);
            if (score < minScore) {
                worstDistrict = d;
                minScore = score;
            }
        }
        return worstDistrict;
    }

    public Move<Precinct, District> makeMove_secondary() {
        List<District> districts = getWorstDistricts();
        //districts.remove(0);
        while (districts.size() > 0) {
            District startDistrict = districts.get(0);
            Move m  = getMoveFromDistrict(startDistrict);
            if (m != null) {
                return m;
            }
            districts.remove(0);
        }
        return null;
    }

    // Returns a list of districts sorted from worst to best
    public List<District> getWorstDistricts() {
        List<Entry<District, Double>> list = new LinkedList<>(currentScores.entrySet());
        Collections.sort(list, new Comparator<Entry<District, Double>>(){
            @Override
            public int compare(Entry<District, Double> o1, Entry<District, Double> o2){
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        List<District> to_return = new ArrayList<District>();
        for(Entry<District, Double> entry : list)
            to_return.add(entry.getKey());
        return to_return;
    }

    public double calculateObjectiveFunction() {
        double score = 0;
        for (District d : state.getDistricts()) {
            score += currentScores.get(d);
        }
        return score;
    }

    public double rateDistrict(District d) {
        return districtScoreFunction.calculateMeasure(d);
    }
    public void updateScores() {
        currentScores = new HashMap<District, Double>();
        for (District d : state.getDistricts()){
            // TODO = resovlve districtRating
            currentScores.put(d, rateDistrict(d));
        }
    }

    /**
     * Check contiguity for moving Precinct p out of District d
     * @param p precinct to move
     * @param d district to move p out of
     * @return true if contiguous
     */
    private boolean checkContiguity(Precinct p, District d) {
        HashSet<Precinct> precinctsToCheck = new HashSet<Precinct>();
        HashSet<Precinct> neighborsToExplore = new HashSet<Precinct>(); // Potential sources of exploration
        HashSet<Precinct> exploredNeighbors = new HashSet<Precinct>(); // Neighbors already explored
        exploredNeighbors.add(p); // Add the precinct being moved, to ensure it won't be used.
        // If a neighbor is in the district that's losing a precinct, we need to make sure they're still contiguous
        for (String p_id : p.getNeighborIDs()) {
            // If neighbor is in the district we're moving p out of
            if ((precinctDistrictMap.get(p_id) != null) &&
                (precinctDistrictMap.get(p_id)).equals(d.getID())) {
                Precinct neighbor = d.getPrecinct(p_id);
                precinctsToCheck.add(neighbor);
            }
        }
        // If there are no same - district neighbors for the node, return false.
        if (precinctsToCheck.size() == 0) {
            return false;
        }
        // Add an arbitrary same - district neighbor to the sources of exploration
        Precinct initialPrecinctToCheck = precinctsToCheck.iterator().next();
        neighborsToExplore.add(initialPrecinctToCheck);
        //we can remove the initial precinct to check since we're assuming it to be in our search tree
        precinctsToCheck.remove(initialPrecinctToCheck);
        // While we still need IDs and still have neighbors to explore
        while (neighborsToExplore.size() != 0) {
            // Take an arbitrary precinct from the sources of exploration
            Precinct precinctToExplore = neighborsToExplore.iterator().next();
            for (String p_id : precinctToExplore.getNeighborIDs()) {
                // We only care about neighbors in our district, d.
                if (precinctDistrictMap.get(p_id).equals(d.getID())) {
                    Precinct neighbor = d.getPrecinct(p_id);
                    // If we've hit one of our needed precincts, check it off.
                    if (precinctsToCheck.contains(neighbor)) {
                        precinctsToCheck.remove(neighbor);
                        if (precinctsToCheck.size() == 0) {
                            return true;
                        }
                    }
                    // Add any neighbors in same district to neighborsToExplore if not in exploredNeighbors
                    if (!exploredNeighbors.contains(neighbor)) {
                        //if its a border precinct we need to check it otherwise we DONT
                        //EXCEPT -- we didn't actually execute the move at this point
                        if(d.getBorderPrecincts().contains(neighbor)) {
                            neighborsToExplore.add(neighbor);
                        } else {
                            exploredNeighbors.add(neighbor);
                        }
                    }
                }
            }
            // Check this precinct off
            exploredNeighbors.add(precinctToExplore);
            neighborsToExplore.remove(precinctToExplore);
        }
        return (precinctsToCheck.size() == 0);
    }

    public Result phaseZero(Double blockThreshold, Double votingThreshold, ELECTIONTYPE election){
        return state.findMajMinPrecincts(blockThreshold,votingThreshold,election);
    }

    public boolean phaseOne(boolean update){
        if (!update){
            while(2*targetNumDist<state.clusters.size()){
//                Collections.sort(state.clusters,new SortByPopulation());
                if(!state.makeMajMinClusters()){
//                    Collections.sort(state.clusters,new SortByPopulation());
                    makeNonMMClusters();
                    int newStateSize = 0;
                    for(Cluster c:state.clusters){
                        newStateSize+=c.population;
                    }
                    int pop = 0;
                    for(Cluster c:state.clusters){
                        pop+=c.getPopulation();
                        System.out.println("CID: "+c.getID()+" POP: "+c.getPopulation());
                    }
                    System.out.println("New State Size: "+newStateSize+" Old size: "+state.population);
                }
                System.out.println("Number of Clusters"+state.clusters.size());
            }
            for(Cluster c:state.clusters){
                System.out.println("CID: "+c.getID()+" POP: "+c.getPopulation());
            }
            int pop = 0;
            int numClusters = 0;
            for(Cluster c:state.clusters){
                numClusters+=c.precinctsCluster.size();
                pop+=c.getPopulation();
//                    System.out.println("CID: "+c.getID()+" POP: "+c.getPopulation());
            }
            System.out.println("StatePop: "+state.population+" NewPop: "+pop);
            System.out.println("New Precinct Size: "+numClusters);

            System.out.println("FINAL ITERATION");
            finalIteration();
            int newStateSize = 0;
            state.clusters.forEach(c->{
                System.out.println("CLUSTER: "+c.getID()+" POP: "+c.population
                        +"\n MajMin: "
                        +c.checkMajorityMinority(state.userDemographicThreshold,
                        state.userVoteThreshold,state.userSelectedElection,demString));
            });
            for(Cluster c:state.clusters){
                newStateSize+=c.population;
            }
            System.out.println("New State Size: "+newStateSize+" Old size: "+state.population);
            return true;
        }
        else{
            if((2*targetNumDist)>=state.clusters.size()){
                System.out.println("FINAL ITERATION");
                finalIteration();
                state.clusters.forEach(c->{
                    System.out.println("CLUSTER: "+c.getID()+" POP: "+c.population
                            +"\n MajMin: "
                            +c.checkMajorityMinority(state.userDemographicThreshold,
                            state.userVoteThreshold,state.userSelectedElection,demString));
                });
                return true;

            }
            else{
                if(!state.makeMajMinClusters()){
//                    Collections.sort(state.clusters,new SortByPopulation());
                    makeNonMMClusters();
                    if(state.combinedClusters.isEmpty()){
                        finalIteration();
                        state.clusters.forEach(c->{
                            System.out.println("CLUSTER: "+c.getID()+" POP: "+c.population
                                    +"\n MajMin: "
                                    +c.checkMajorityMinority(state.userDemographicThreshold,
                                    state.userVoteThreshold,state.userSelectedElection,demString));
                        });
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void finalIteration(){

        int cSize = state.clusters.size();
        Collections.sort(state.clusters,new SortByPopulation());
//        int lowerQuartile = state.clusters.size()/4;
//        int upperQuartile = lowerQuartile*3;
        Iterator<Cluster> iterator = state.clusters.iterator();
        int counter=0;
        while (iterator.hasNext()) {
//            if(targetNumDist >= cSize){
//                return;
//            }
//
            Cluster currCluster = iterator.next();
            if(currCluster.checkMajorityMinority(state.userDemographicThreshold,
                    state.userVoteThreshold,state.userSelectedElection,demString)
                    && currCluster.population>400000){
                currCluster = iterator.next();
            }
            if(currCluster.population<200000){
                for (Edge edge: currCluster.edges){
                    Cluster neighbor = edge.getNeighbor(currCluster);
                    if(neighbor.population<500000){
                        state.combineClusters(currCluster, neighbor);
                        iterator.remove();
                        cSize--;
                        break;
                    }
                }
            }
            if(targetNumDist >= cSize){
                return;
            }
            counter++;
        }

        if(cSize>targetNumDist) {

            Collections.sort(state.clusters, new SortByPopulation());
            Iterator<Cluster> newIterator = state.clusters.iterator();
            while (newIterator.hasNext()) {
                if (targetNumDist >= cSize) {
                    return;
                }
                Cluster currCluster = newIterator.next();
                if (currCluster.checkMajorityMinority(state.userDemographicThreshold,
                        state.userVoteThreshold, state.userSelectedElection, demString)
                        && currCluster.population > 400000) {
                    if (newIterator.hasNext()) {
                        currCluster = newIterator.next();
                    } else {
                        break;
                    }
                }
                Set<Cluster> neighbors = new HashSet<Cluster>();
                for (Edge edge : currCluster.edges) {
                    neighbors.add(edge.getNeighbor(currCluster));
                }
                for (Cluster neighbor : neighbors) {
                    if (!neighbor.checkMajorityMinority(state.userDemographicThreshold,
                            state.userVoteThreshold, state.userSelectedElection, demString)) {

                    }
                }

            }
            while (targetNumDist < cSize) {
                Collections.sort(state.clusters, new SortByPopulation());
                Iterator<Cluster> newIterator2 = state.clusters.iterator();
                while (newIterator2.hasNext()) {
                    if (targetNumDist >= cSize) {
                        return;
                    }
                    Cluster currCluster = newIterator2.next();
                    Set<Cluster> neighbors = new HashSet<Cluster>();
                    for (Edge edge : currCluster.edges) {
                        neighbors.add(edge.getNeighbor(currCluster));
                    }
                    for (Cluster neighbor : neighbors) {
                        state.combineClusters(currCluster, neighbor);
                        newIterator2.remove();
                        cSize--;
                        break;
                    }
                }

            }
        }
        return;
    }

    public Cluster findMostJoinableCluster(Cluster c,Set<Cluster> neighbors){

        Cluster mostJoinableCluster = null;
        double max_NonMMJoinability = -999999999999999999999.0;
        for (Cluster neighbor:neighbors){
//            System.out.println("EDGE ID: "+neighbor.getID());
            double joinability = calculateNonMMJoinability(c,neighbor);
//            System.out.println("Joinability: "+joinability);
            if(joinability>=max_NonMMJoinability){
                max_NonMMJoinability=joinability;
                mostJoinableCluster=neighbor;
            }
        }
//        System.out.println("Value: "+max_NonMMJoinability);
        System.out.println("MJC: "+mostJoinableCluster.getID());
        return mostJoinableCluster;
    }

    public double calculateNonMMJoinability(Cluster c1,Cluster c2){

        Cluster combinedCluster = new Cluster(c1.getID());
        combinedCluster.clusterDemographics.putAll(c1.getClusterDemographics());
        combinedCluster.elections.putAll(c1.elections);
        combinedCluster.setDem_vote(c1.getDEMVote());
        combinedCluster.setGop_vote(c1.getGOPVote());
        combinedCluster.setState(c1.getState());
        combinedCluster.precinctsCluster.addAll(c1.precinctsCluster);
        combinedCluster.precinctsCluster.addAll(c2.precinctsCluster);

        int internalEdges = 0;
        int externalEdges = 0;
        for (Precinct p : combinedCluster.precinctsCluster) {
            for (String neighborID : p.getNeighborIDs()) {
                if (combinedCluster.precinctsCluster.stream().anyMatch(precinct -> {
                    return precinct.getID().equals(neighborID);
                })) {
                    internalEdges++;
                }
                else{
                    externalEdges++;
                }
            }
        }
        if((externalEdges+internalEdges)==0){
            System.out.println("HOUSTON WE HAVE A PROBLEM!");
        }
        combinedCluster.internalEdges = internalEdges;
        combinedCluster.externalEdges = externalEdges;
        for(DEMOGRAPHIC demograhpic: DEMOGRAPHIC.values()){
            int combined = combinedCluster.getClusterDemographics().get(demograhpic) + c2.getClusterDemographics().get(demograhpic);
            combinedCluster.getClusterDemographics().put(demograhpic,combined);
        }
        ((District)combinedCluster).population = ((District)combinedCluster).population+((District)c2).population;
        for(PARTYNAME partyname :PARTYNAME.values()){
            int combined = combinedCluster.getElections().get(state.userSelectedElection).getVotes().get(partyname) +
                    c2.getElections().get(state.userSelectedElection).getVotes().get(partyname);
            combinedCluster.getElections().get(state.userSelectedElection).getVotes().put(partyname, combined);
        }
        combinedCluster.setGop_vote(c1.getGOPVote()+c2.getGOPVote());
        combinedCluster.setDem_vote(c1.getDEMVote()+c2.getDEMVote());

        double nonMMJoinability=0.0;
        for(FACTOR f:FACTOR.values()){

            if(f==FACTOR.MAJMIN){
                double largestPopulationRatio = combinedCluster.calculateDemographicSize(combinedCluster.getLargestDemographic(),
                        combinedCluster.population);

                nonMMJoinability+=factors.get(f)*
                        f.calculateMeasureMajMin(popThreshMax,popThreshMin,largestPopulationRatio);
            }
            else{
                if(f == FACTOR.EFFICIENCY_GAP){
                    continue;
                }
                nonMMJoinability+=factors.get(f)*f.calculateMeasure(combinedCluster);
            }
//            else{
//                if(f==FACTOR.EQPOP){
//                    nonMMJoinability+=1.5*f.calculateMeasure(combinedCluster);
//                }
//                else{
//                    nonMMJoinability+=0.0005*f.calculateMeasure(combinedCluster);
//                }
//
//            }
        }
        return nonMMJoinability;
    }

    public void makeNonMMClusters(){
        int ogSize = state.clusters.size();
        int cSize = state.clusters.size();
        Iterator<Cluster> iterator = state.clusters.iterator();
        while (iterator.hasNext()) {
            if(2*targetNumDist>cSize){
                return;
            }
            Cluster currCluster = iterator.next();
            if(currCluster.checkMajorityMinority(state.userDemographicThreshold,
                    state.userVoteThreshold,state.userSelectedElection,demString)
                    && currCluster.population>500000){
                continue;
            }
            Set<Cluster> neighbors = new HashSet<Cluster>();
            for (Edge edge: currCluster.edges){
                Cluster n = edge.getNeighbor(currCluster);
                if(n.population<250000){
                    neighbors.add(n);
                }

            }
            if(!neighbors.isEmpty()){
                Cluster mostJoinableCluster = findMostJoinableCluster(currCluster,neighbors);
//            System.out.println("Size of most joinable: "+mostJoinableCluster.precinctsCluster.size());
//            System.out.println("Size of current: "+currCluster.precinctsCluster.size());
                state.combineClusters(currCluster,mostJoinableCluster);
                state.combinedClusters.add(mostJoinableCluster);
                iterator.remove();
                cSize--;
            }
//            int totP = 0;
//            for(Cluster c:state.clusters){
//                totP+=c.precinctsCluster.size();
//            }
//            if(totP!=8936){
//                System.out.println("Size of current after: "+currCluster.precinctsCluster.size());
//                System.out.println("Precincts: "+totP);
//                System.exit(0);
//            }

        }
    }

    public void initClusters(){
        for(Precinct precinct:state.getPrecincts()){
            Cluster newCluster = new Cluster(precinct.getID());
            newCluster.precinctsCluster.add(precinct);
            newCluster.setDem_vote(precinct.getElections().
                    get(state.userSelectedElection).getVotes().get(PARTYNAME.DEMOCRAT));
            newCluster.setGop_vote(precinct.getElections().
                    get(state.userSelectedElection).getVotes().get(PARTYNAME.REPUBLICAN));
            newCluster.population=precinct.getPopulation();
            Map<DEMOGRAPHIC,Integer> dems = new HashMap<DEMOGRAPHIC,Integer>();
            for(DEMOGRAPHIC d:DEMOGRAPHIC.values()){
                dems.put(d,precinct.getPrecinctDemographics().get(d));
            }
            newCluster.setClusterDemographics(dems);
            newCluster.elections=new HashMap<ELECTIONTYPE,Votes>();
            for(ELECTIONTYPE e:ELECTIONTYPE.values()){
                Votes v = new Votes();
                Map<PARTYNAME,Integer> votes = new HashMap<PARTYNAME,Integer>();
                for(PARTYNAME p:PARTYNAME.values()){
                    votes.put(p,precinct.getElections().get(e).getVotes().get(p));
                }
                v.setVotes(votes);
                newCluster.elections.put(e,v);
            }
            for(Precinct p:newCluster.precinctsCluster){
                newCluster.externalEdges+=p.getNeighborIDs().size();
            }
            newCluster.setState(state);
            precinct.newDistrictID=newCluster.getID();
            state.clusters.add(newCluster);
        }
    }

    public void phase2Init(){
        precinctDistrictMap = new HashMap<String, String>();
        setDistrictScoreFunction(DefaultMeasures.defaultMeasuresWithWeights(weights));
        for(Cluster c: state.clusters){
            for(Precinct p: c.precinctsCluster){
                c.precincts.put(p.getID(),p);
                precinctDistrictMap.put(p.getID(),c.getID());
            }
            c.findBorderPrecincts();
            GerryManderController.districtQueue.add(c);
            state.putDistrict(c);
        }
        updateScores();
        for(int i=0;i<1;i++){
            for(District d: state.getDistricts()){
                GerryManderController.districtQueue.add(d);
            }
        }

    }





}
