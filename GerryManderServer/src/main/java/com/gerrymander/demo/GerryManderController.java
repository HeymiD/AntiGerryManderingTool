package com.gerrymander.demo;

import java.io.FileNotFoundException;
import java.util.*;

import com.gerrymander.demo.algorithm.Algorithm;
import com.gerrymander.demo.models.DAO.DistrictDAO;
import com.gerrymander.demo.models.DAO.PrecinctDAO;
import com.gerrymander.demo.models.concrete.District;
import com.gerrymander.demo.models.concrete.Precinct;
import com.gerrymander.demo.models.concrete.State;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.ModelAndView;

@RestController
public class GerryManderController
{
	State s;
	Algorithm a;

//	HTTP monitoring: browser
//    web sockets approach
//    use jpa init state, read manual jpa java ee tut

//	@RequestMapping("/")
//	public String home()
//	{
//		//System.out.print("Running...");
//		return "Map.html";
//	}
	@RequestMapping("/state")
	public String getSate(@RequestParam("state") String stateName,
                          @RequestParam("count") int count, @RequestParam("elecType") String elecType) {
        System.out.println(stateName);
        Set<Precinct> precincts = new HashSet<Precinct>();
        System.out.println("count:" + count);
        if (count==0)
        {
            s = new State(stateName,precincts);
        }
        int disId = count+1;
        District dis = DistrictDAO.get(""+disId);
        PrecinctDAO.initPrecinctsforDistrict("U.S. Rep "+disId,ELECTIONTYPE.valueOf(elecType),dis);
        s.oldDistricts.put(disId,dis);

//        System.out.println("D.ID: "+s.oldDistricts.get(count).getID());
        System.out.println();
        return dis.getGeoData();
	}

    @RequestMapping("/precincts")
    public String getPrecincts(@RequestParam("state") String stateName,
                               @RequestParam("dstrID") int dstrID,
                               @RequestParam("elecType") String elecType) throws InterruptedException {
        dstrID++;
	    District dis = s.oldDistricts.get(dstrID);
	    return JSONMaker.makeJSONCollection(dis.getPrecincts());
    }
	
//	@RequestMapping(value = "/s")
//    public String getAllEmployeesJSON(Model model) 
//    {
//        model.addAttribute("employees", getEmployeesCollection());
//        return "jsonTemplate";
//    }
	
	
	
}
