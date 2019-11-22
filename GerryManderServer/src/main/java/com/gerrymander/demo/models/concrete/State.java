package com.gerrymander.demo.models.concrete;


import com.gerrymander.demo.Cluster;
import com.gerrymander.demo.Result;
import com.gerrymander.demo.measures.StateInterface;
import com.gerrymander.demo.models.DAO.DistrictDAO;
//import com.gerrymander.demo.models.DAO.DistrictDAOInterface;
//import com.gerrymander.demo.models.Service.DistrictService;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class State
		implements StateInterface<Precinct, District> {

	private String name;//name is saved for later storage into the database

	private HashMap<String, District> districts;
	private HashMap<String, Precinct> precincts;
	public ArrayList<District> oldDistricts;
	public LinkedList<Cluster> clusters;
	public ArrayList<Cluster> majMinClusters;
	public Result majMinPrecincts;
	public boolean clusterListModified;
	private int population;

	public State(String name, Set<Precinct> inPrecincts) throws FileNotFoundException {
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
        this.population = districts.values().stream().mapToInt(District::getPopulation).sum();
//		oldDistricts = new ArrayList<District>();
        oldDistricts = new ArrayList<District>();
        int i = 1;
        try{
            District dis = DistrictDAO.get(""+i);
            while (dis!=null){
                oldDistricts.add(dis);
                dis=DistrictDAO.get(""+i);
                i++;
            }
        }

        catch (NullPointerException e){
            System.out.println("Id of district: "+i);
        }


//		for(int i=1;i<37;i++) {
//			File dstrFile =
//					new File("C:/Users/heymi/Desktop/CLASSES/4THYEAR/CSE308/GerryManderServer/src/main/resources/static/Data/Texas_Districts/District_"
//							+Integer.toString(i)+".geojson");
//			System.out.println(dstrFile.getName());
//
//			District dis = new District(""+i,this);
//			Scanner scnr = new Scanner(dstrFile);
//			String district="";
//			while(scnr.hasNextLine())
//			{
//				String line = scnr.nextLine();
//				district+=line;
//			}
//			//System.out.println(district);
//			dis.setGeoData(district);
//			oldDistricts.add(dis);
//			//System.out.println(dis.geoData);
//			district="";
//			scnr.close();
//	    }
	}

	public Set<Precinct> getPrecincts(){
        return new HashSet<>(precincts.values());
		}

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
}