package com.gerrymander.demo;

import com.gerrymander.demo.MeasuresCodeFiles.MeasuresCodeFiles.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


//+name: String
//+oldDistricts: Set<District>
//+clusters: Collection<Cluster>
//+majMinClusters: Set<Cluster>
//+precincts: Set<Precinct>
//+majMinPrecincts: Result
//+numIterations: int
//+clusterListModified: boolean
public class State extends BaseState{
	
	public ArrayList<District> oldDistricts;
	public ArrayList<District> newDistricts;
	public ArrayList<Precinct> precincts;
	public LinkedList<Cluster> clusters;
	public Result majMinPrecincts;
	public boolean clusterListModified;
	public int numIterations;
	public ArrayList<Cluster> majMinClusters;
	public String districts;
	
	
	public State(String name) 
	{
		super();
		districts="";
        File dstrFile = new File("src/main/resources/static/U.S._Congressional_Districts.geojson");
        Scanner scnr;
		try {
			scnr = new Scanner(dstrFile);
			while(scnr.hasNextLine()){
	            String line = scnr.nextLine();
	            districts+=line;
	        } 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
             
		
		//make and fetch oldDistricts
//		public static void viewTable(Connection con, String dbName)
//			    throws SQLException {
//
//			    Statement stmt = null;
//			    String query = "select COF_NAME, SUP_ID, PRICE, " +
//			                   "SALES, TOTAL " +
//			                   "from " + dbName + ".COFFEES";
//			    try {
//			        stmt = con.createStatement();
//			        ResultSet rs = stmt.executeQuery(query);
//			        while (rs.next()) {
//			            String coffeeName = rs.getString("COF_NAME");
//			            int supplierID = rs.getInt("SUP_ID");
//			            float price = rs.getFloat("PRICE");
//			            int sales = rs.getInt("SALES");
//			            int total = rs.getInt("TOTAL");
//			            System.out.println(coffeeName + "\t" + supplierID +
//			                               "\t" + price + "\t" + sales +
//			                               "\t" + total);
//			        }
//			    } catch (SQLException e ) {
//			        JDBCTutorialUtilities.printSQLException(e);
//			    } finally {
//			        if (stmt != null) { stmt.close(); }
//			    }
//			}
		
	}

}
