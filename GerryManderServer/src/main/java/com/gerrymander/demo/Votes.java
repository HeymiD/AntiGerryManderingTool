package com.gerrymander.demo;
import com.gerrymander.demo.models.concrete.Precinct;
import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
//@Table(name="complete_texas_elections_formatted")
//@IdClass(Election.class)
//@SqlResultSetMapping(name="VotesResult",
//        entities={
//                @EntityResult(entityClass=Votes.class, fields={
//                        @FieldResult(name="id", column="order_id"),
//                        @FieldResult(name="quantity", column="order_quantity"),
//                        @FieldResult(name="item", column="order_item")})},
//        columns={
//                @ColumnResult(name="item_name")}
//)

public class Votes {
//    @ManyToOne
//    @JoinColumn(name = "PCTKEY")
//    private Precinct precinct;
////    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private String id;
////    @Column(name = "type")
////    private String election;
////    @MapKeyColumn(name="Party")
////    @MapKeyEnumerated(EnumType.STRING)
////    @Column(name="votes")
    private Map<PARTYNAME, Integer> votes;
////    @Column(name = "type")
////    @Enumerated(EnumType.STRING)
    private ELECTIONTYPE electiontype;
//    @Id
//    @Column(name="Party")
//    private PARTYNAME party;
//    @Id
//    @Column(name= "type")
//    @Enumerated(EnumType.STRING)
//    private ELECTIONTYPE electiontype;
//    @Id
//    @Column(name="PCTKEY")
//    private String pctkey;
////    @Column(name="votes")
////    private int votes;
    public Votes(){ }
//    public String getPctkey(){return pctkey;}
//    public void setPctkey(String pctkey){this.pctkey=pctkey;}
//    public PARTYNAME getParty(){return party;}
//    public void setParty(PARTYNAME party){this.party=party;}
    public Map<PARTYNAME, Integer> getVotes(){return votes;}
    public void setVotes(Map<PARTYNAME,Integer> totVotes){this.votes=totVotes;}
//    public String getElection() {
//        return election;
//    }
//
//    public void setElection(String election) {
//        this.election = election;
//    }

//    public Map<PARTYNAME, Integer> getVotes() {
//        return votes;
//    }
//
//    public void setVotes(Map<PARTYNAME, Integer> votes) {
//        this.votes = votes;
//    }
    public ELECTIONTYPE getElectiontype() {
        return electiontype;
    }

    public void setElectiontype(ELECTIONTYPE electiontype) {
        this.electiontype = electiontype;
    }

    public PARTYNAME getWinningParty(){
        PARTYNAME winningParty = null;
        int winningVotes = Integer.MIN_VALUE;
        for(PARTYNAME party : PARTYNAME.values()){
            if(votes.get(party) >= winningVotes){
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