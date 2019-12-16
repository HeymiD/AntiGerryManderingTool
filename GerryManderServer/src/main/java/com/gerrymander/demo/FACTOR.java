package com.gerrymander.demo;

import com.gerrymander.demo.models.concrete.District;
import com.gerrymander.demo.models.concrete.Precinct;
import com.gerrymander.demo.models.concrete.State;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.Map;

public enum FACTOR {

	COMPACTNESS{
		/*
            Compactness:
            perimeter / (circle perimeter for same area)
        */
		public double calculateMeasure(Cluster d) {
			double internalEdges = d.getInternalEdges();
			double totalEdges = internalEdges + d.getExternalEdges();
//			System.out.println("In: "+internalEdges+"\n Out: "+d.getExternalEdges()+
//					"\n Total Edges: "+totalEdges);
			return internalEdges / totalEdges;
		}

		@Override
		public double calculateMeasureMajMin(double upperBound, double lowerBound, double population) {
			return 0;
		}
	},
	POLFAIR{
		/**
		 * Partisan fairness:
		 * 100% - underrepresented party's winning margin
		 * OR
		 * underrepresented party's losing margin
		 * (We want our underrepresented party to either win by a little or lose by a lot - fewer wasted votes)
		 */
		public double calculateMeasure(Cluster d) {
			// Temporary section
			int totalVote = 0;
			int totalGOPvote = 0;
			int totalDistricts = 0;
			int totalGOPDistricts = 0;
			State state = d.getState();
			for (Cluster sd : state.clusters) {
				totalVote += sd.getGOPVote();
				totalVote += sd.getDEMVote();
				totalGOPvote += sd.getGOPVote();
				totalDistricts += 1;
				if (sd.getGOPVote() > sd.getDEMVote()) {
					totalGOPDistricts += 1;
				}
			}
//			System.out.println("Nan totalVote: "+totalVote);
			int idealDistrictChange = ((int) Math.round(totalDistricts * ((1.0 * totalGOPvote) / totalVote))) - totalGOPDistricts;
			// End temporary section
			if (idealDistrictChange == 0) {
				return 1.0;
			}
			int gv = d.getGOPVote();
			int dv = d.getDEMVote();
			int tv = gv + dv;
			int margin = gv - dv;
			if (tv == 0) {
				return 1.0;
			}
			int win_v = Math.max(gv, dv);
			int loss_v = Math.min(gv, dv);
			int inefficient_V;
			if (idealDistrictChange * margin > 0) {
				inefficient_V = win_v - loss_v;
			} else {
				inefficient_V = loss_v;
			}
//			System.out.println("Nan tv: "+tv);
			return 1.0 - ((inefficient_V * 1.0) / tv);
		}

		@Override
		public double calculateMeasureMajMin(double upperBound, double lowerBound, double population) {
			return 0;
		}
	},
	EQPOP{
		public double calculateMeasure(Cluster d) {
			//we will square before we return--this gives lower measure values
			// for greater error
			State state = d.getState();
			int idealPopulation = state.getPopulation() / state.clusters.size();
			int truePopulation = d.getPopulation();
//			System.out.println("Nan id pop: "+idealPopulation);
			if (idealPopulation >= truePopulation) {
				return 1-Math.pow(
						Math.abs( idealPopulation-(double)truePopulation)/idealPopulation ,1.25);
			}
			return 1-Math.pow(
					Math.abs( truePopulation -(double)idealPopulation)
							/idealPopulation, 1.25);
		}

		@Override
		public double calculateMeasureMajMin(double upperBound, double lowerBound, double population) {
			return 0;
		}

	},
	COUNTY{
		public double calculateMeasure(Cluster c){
			int totCounties = c.precinctsCluster.size();
			int maxNumberCounties = 0;
			Map<Integer,Integer> countyAmounts = new HashMap<Integer,Integer>();
			for(Precinct p:c.precinctsCluster){
				try{
					int countyAmount = countyAmounts.get(p.cnty);
					countyAmount++;
					if(maxNumberCounties<countyAmount){
						maxNumberCounties=countyAmount;
					}
					countyAmounts.put(p.cnty,countyAmount);
				}
				catch(NullPointerException e){
					countyAmounts.put(p.cnty,1);
					if(maxNumberCounties<countyAmounts.get(p.cnty)){
						maxNumberCounties=countyAmounts.get(p.cnty);
					}
				}
			}
			return ((double)maxNumberCounties)/totCounties;
		}

		@Override
		public double calculateMeasureMajMin(double upperBound, double lowerBound, double population) {
			return 0;
		}
	},
	MAJMIN{
		@Override
		public double calculateMeasure(Cluster cluster) {
			return 0;
		}

		@Override
		public double calculateMeasureMajMin(double upperBound, double lowerBound, double population) {
			double avg = (upperBound+lowerBound)/2;
			double x = Math.abs((population-avg));
			return -2.0*x+1;
		}
	},
	EFFICIENCY_GAP {
		/**
		 * Wasted votes:
		 * Statewide: abs(Winning party margin - losing party votes)
		 */
		@Override
		public double calculateMeasure(Cluster d) {
			int iv_g = 0;
			int iv_d = 0;
			int tv = 0;
			State state = d.getState();
			for (String sdId : state.oldDistricts.keySet()) {
				Cluster sd = state.oldDistricts.get(sdId);
				int gv = sd.getGOPVote();
				int dv = sd.getDEMVote();
				if (gv > dv) {
					iv_d += dv;
					iv_g += (gv - dv);
				} else if (dv > gv) {
					iv_g += gv;
					iv_d += (dv - gv);
				}
				tv += gv;
				tv += dv;
			}
			return 1.0 - ((Math.abs(iv_g - iv_d) * 1.0) / tv);
		}
		@Override
		public double calculateMeasureMajMin(double upperBound, double lowerBound, double population) {
			return 0;
		}
	};
	public abstract double calculateMeasure(Cluster cluster);

	public abstract double calculateMeasureMajMin(double upperBound, double lowerBound, double population);
}
