package com.fuzzy.test;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

public class CFlockGraphRules {
	public static void main( String args[] )
	{
		// Fuzzy rule files
		String attractionRules = "rules/attraction_rules.fcl";
		String alignmentRules = "rules/alignment_rules.fcl";
		String repulsionRules = "rules/repulsion_rules.fcl";
		
		// Load rules
		FIS attraction = FIS.load( attractionRules, true );
		FIS alignment = FIS.load( alignmentRules, true );
		FIS repulsion = FIS.load( repulsionRules, true );
		
		// Check load error
		if( attraction == null || alignment == null || repulsion == null )
		{
			System.err.println( "Error loading .fcl files." );
			System.exit( -1 );
		}
		
		// Get the function block for each rule set
		FunctionBlock attractionBlock = attraction.getFunctionBlock( null );
		FunctionBlock alignmentBlock = alignment.getFunctionBlock( null );
		FunctionBlock repulsionBlock = repulsion.getFunctionBlock( null );
		
		// Display the function block charts
		JFuzzyChart.get().chart( attractionBlock );
		JFuzzyChart.get().chart( alignmentBlock );
		JFuzzyChart.get().chart( repulsionBlock );
		
		// Test parameters
		// TODO
		
		// Display output charts
		// TODO
		
		// Print Rule sets
		System.out.println( attraction );
		System.out.println( alignment );
		System.out.println( repulsion );
		
		System.exit( 0 );
	}
}
