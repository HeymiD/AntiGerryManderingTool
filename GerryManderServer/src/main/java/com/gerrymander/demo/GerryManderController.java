package com.gerrymander.demo;

import java.io.FileNotFoundException;
import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class GerryManderController
{
	State s;
	Algorithm a;

//	@RequestMapping("/")
//	public String home()
//	{
//		//System.out.print("Running...");
//		return "Map.html";
//	}
	@RequestMapping("/state")
	public String getSate(@RequestParam("state") String stateName, @RequestParam("count") int count)
	{
        System.out.println(stateName);
        if (count==0)
        {
            try {
                s = new State(stateName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(!(s.oldDistricts.equals("")))
            {
                System.out.println("Districts loaded");
            }
            a = new Algorithm();
            System.out.println(s.oldDistricts.get(count).geoData.substring(0,300));
        }
        return s.oldDistricts.get(count).geoData;
//		System.out.println(stateName);
//		try {
//			s = new State(stateName);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		if(!(s.oldDistricts.equals("")))
//		{
//			System.out.println("Districts loaded");
//		}
//		a = new Algorithm();
//		String districts_geoData = "{\n" +
//				"\"type\": \"FeatureCollection\",\n" +
//				"\"name\": \"New_Texs_Pr\",\n" +
//				"\"crs\": { \"type\": \"name\", \"properties\": { \"name\": \"urn:ogc:def:crs:EPSG::900913\" } },\n" +
//				"\"features\": [";
//		for (int i=0; i<s.oldDistricts.size();i++)
//		{
//			districts_geoData+=s.oldDistricts.get(i).geoData+",";
//			if (i==s.oldDistricts.size()-1)
//			{
//				districts_geoData+=s.oldDistricts.get(i).geoData;
//                //System.out.println("District"+Integer.toString(i)+": "+s.oldDistricts.get(i));
//			}
//		}
//		districts_geoData+="]}";


//		ArrayList<String> districts_geoData = new ArrayList<String>();
//		for (District d:s.oldDistricts)
//			{
//				districts_geoData.add(d.geoData);
//			}

//			String districts="";
//			mv.addObject("districts", districts);

		
//		s = new State(stateName);
//		ArrayList<String> districts_geoData = new ArrayList<String>();
//		for (District d:s.oldDistricts) 
//		{
//			districts_geoData.add(d.geoData);
//		}
//		m.addAttribute("districts",districts_geoData);
////		String districts="";
////		mv.addObject("districts", districts);
//		
//		return "jsonTemplate";
	}
	
//	@RequestMapping(value = "/s")
//    public String getAllEmployeesJSON(Model model) 
//    {
//        model.addAttribute("employees", getEmployeesCollection());
//        return "jsonTemplate";
//    }
	
	
	
}
