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
//	public String districts;



	public State(String name) throws FileNotFoundException {
		super();
		oldDistricts = new ArrayList<District>();
		for(int i=1;i<32;i++)
		{
			File dstrFile =
                    new File("C:/Users/heymi/Desktop/CLASSES/4THYEAR/CSE308/GerryManderServer/src/main/resources/static/TexasDistricts/District"
					+Integer.toString(i)+".txt");
			System.out.println(dstrFile.getName());

			District dis = new District();
			dis.id=i;
			Scanner scnr = new Scanner(dstrFile);
			String district="";
			while(scnr.hasNextLine())
            {
				String line = scnr.nextLine();
				district+=line;
			}
			//System.out.println(district);
			dis.geoData=district;
			oldDistricts.add(dis);
            //System.out.println(dis.geoData);
			district="";
			scnr.close();
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
