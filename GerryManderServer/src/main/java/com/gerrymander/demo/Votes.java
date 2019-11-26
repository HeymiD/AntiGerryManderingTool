package com.gerrymander.demo;
import com.gerrymander.demo.models.concrete.Precinct;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="elections_texas")
public class Votes {
    @ManyToOne
    private Precinct precinct;
    @Column(name = "type")
    private String election;
    @MapKeyColumn(name="Party")
    @Enumerated(EnumType.STRING)
    @Column(name="votes")
    private Map<PARTYNAME, Integer> votes;


    public Votes(){
//        votes = new HashMap<PARTYNAME, Integer>();
//        votes.put(PARTYNAME.REPUBLICAN,rep);
//        votes.put(PARTYNAME.DEMOCRAT,dem);
//        votes.put(PARTYNAME.GREEN,green);
//        votes.put(PARTYNAME.LIBERTARIAN,lib);

    }


    public String getElection() {
        return election;
    }

    public void setElection(String election) {
        this.election = election;
    }

    public Map<PARTYNAME, Integer> getVotes() {
        return votes;
    }

    public void setVotes(Map<PARTYNAME, Integer> votes) {
        this.votes = votes;
    }

//    public int getYear() {
//        return year;
//    }

//    public void setYear(int year) {
//        this.year = year;
//    }

    public PARTYNAME getWinningParty(){
        PARTYNAME winningParty = null;
        long winningVotes = 0;
        for(PARTYNAME party : PARTYNAME.values()){
            if(votes.get(party) > winningVotes){
                winningVotes = votes.get(party);
                winningParty = party;
            }
        }
        return winningParty;
    }
    public double calculateWinningPartyRatio(PARTYNAME party){
        Double totalVotes = 0.0;
        for(PARTYNAME parties : PARTYNAME.values()){
            totalVotes += votes.get(parties);
        }
        return votes.get(party)/totalVotes;
    }
}