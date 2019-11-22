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
}