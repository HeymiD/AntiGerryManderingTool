package com.gerrymander.demo;

import javax.persistence.Id;
import javax.persistence.IdClass;
import java.awt.*;
import java.io.Serializable;


public class Election implements Serializable {
	public String pctkey;
	public ELECTIONTYPE electiontype;
	public PARTYNAME party;
	
	public Election() { }

}
