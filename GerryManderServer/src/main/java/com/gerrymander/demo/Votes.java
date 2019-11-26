package com.gerrymander.demo;
import com.gerrymander.demo.models.concrete.Precinct;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="elections_texas_formatted")
public class Votes {
    @ManyToOne
    private Precinct precinct;
    @Id
    @Column(name="PCTKEY")
    private String pctkey;
    @Column(name = "type")
    private String election;
    @MapKeyColumn(name="Party")
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name="votes")
    private Map<PARTYNAME, Integer> votes;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ELECTIONTYPE electiontype;

    public Votes(){ }

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

    public ELECTIONTYPE getElectiontype() {
        return electiontype;
    }

    public void setElectiontype(ELECTIONTYPE electiontype) {
        this.electiontype = electiontype;
    }

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