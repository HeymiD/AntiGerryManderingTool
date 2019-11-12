package com.gerrymander.demo;

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
	public String getSate(@RequestParam("state") String stateName)
	{
		System.out.println(stateName);
		s = new State(stateName);
		if(!(s.districts.equals(""))) 
		{
			System.out.println("String loaded");
		}
		a = new Algorithm();
//		ArrayList<String> districts_geoData = new ArrayList<String>();
//		for (District d:s.oldDistricts) 
//			{
//				districts_geoData.add(d.geoData);
//			}
		
//			String districts="";
//			mv.addObject("districts", districts);
		return s.districts;
		
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
