package com.gerrymander.demo;
import java.util.Map;

public class Votes {
    private String election;
    private Map<PARTYNAME, Long> votes;

    public String getElection() {
        return election;
    }

    public void setElection(String election) {
        this.election = election;
    }

    public Map<PARTYNAME, Long> getVotes() {
        return votes;
    }

    public void setVotes(Map<PARTYNAME, Long> votes) {
        this.votes = votes;
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
    public long calculateWinningPartyRatio(PARTYNAME party){
        Long totalVotes = Long.valueOf(0);
        for(PARTYNAME parties : PARTYNAME.values()){
            totalVotes += votes.get(parties);
        }
        return votes.get(party)/totalVotes;
    }
}