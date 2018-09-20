package com.fuzzy.test;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

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
		attractionBlock.setVariable( "att_distance", 25 );
		attractionBlock.setVariable( "att_position", 150 );
		
		alignmentBlock.setVariable( "ali_distance", 25 );
		alignmentBlock.setVariable( "ali_direction", 45 );
		alignmentBlock.setVariable( "ali_speed", 55 );
		
		repulsionBlock.setVariable( "rep_distance", 25 );
		repulsionBlock.setVariable( "rep_position", 45 );
		
		// Evaluate
		attractionBlock.evaluate();
		alignmentBlock.evaluate();
		repulsionBlock.evaluate();
		
		// Display output charts
		Variable attractionFlightDirection = attractionBlock.getVariable( "att_flight_direction" );
		Variable attractionFlightSpeed = attractionBlock.getVariable( "att_flight_speed" );
		
		Variable alignmentFlightDirection = alignmentBlock.getVariable( "ali_flight_direction" );
		Variable alignmentFlightSpeed = alignmentBlock.getVariable( "ali_flight_speed" );
		
		Variable repulsionFlightDirection = repulsionBlock.getVariable( "rep_flight_direction" );
		Variable repulsionFlightSpeed = repulsionBlock.getVariable( "rep_flight_speed " );
		
		JFuzzyChart.get().chart( attractionFlightDirection, attractionFlightDirection.getDefuzzifier(), true );
		JFuzzyChart.get().chart( attractionFlightSpeed, attractionFlightSpeed.getDefuzzifier(), true );
		
		JFuzzyChart.get().chart( alignmentFlightDirection, alignmentFlightDirection.getDefuzzifier(), true );
		JFuzzyChart.get().chart( alignmentFlightSpeed, attractionFlightSpeed.getDefuzzifier(), true );
		
		JFuzzyChart.get().chart( repulsionFlightDirection, repulsionFlightDirection.getDefuzzifier(), true );
		JFuzzyChart.get().chart( repulsionFlightSpeed, repulsionFlightSpeed.getDefuzzifier(), true );
		
		// Print Rule sets
		System.out.println( attraction );
		System.out.println( alignment );
		System.out.println( repulsion );
		
	}
}
