package com.fuzzy.controller;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class CFuzzyController {
	
	private int _numberFCL;
	private Vector<String> _fclFileNames;
	
	private Vector<FIS> _files;
	private Vector<FunctionBlock> _blocks;
	
	private Vector<CFuzzyStruct> _fuzzyOutputs = new Vector<CFuzzyStruct>();
	
	// Weights for rules sets
	private Vector<Double> _weights;
	
	public CFuzzyController( int numFCLIn )
	{
		_numberFCL = numFCLIn;
		_fclFileNames = new Vector<String>( _numberFCL );
		
		_files = new Vector<FIS>( _numberFCL );
		_blocks = new Vector<FunctionBlock>(0);

		_weights = new Vector<Double>( _numberFCL );
	}
	
	public void SetNumberFCL( int numFCLIn )
	{
		_numberFCL = numFCLIn;
		
	}
	
	public int GetNumberFCL()
	{
		return _numberFCL;
	}
	
	public void SetWeights( double wp, double wa, double wr )
	{
		_weights.add( new Double(wp) );// alignment
		_weights.add( new Double(wa) );// attraction
		_weights.add( new Double(wr) );// repulsion
		
		int index = 0;
		for( CFuzzyStruct fuzzy : _fuzzyOutputs)
		{
			fuzzy.SetFlightDirectionWeight(_weights.get(index));
			index++;
		}
		
		index = 0;
		for( CFuzzyStruct fuzzy : _fuzzyOutputs)
		{
			fuzzy.SetFlightSpeedWeight(_weights.get(index));
			index++;
		}
	}
	
	/**
	 * Order really matters in this impl!
	 * @param fileNamesIn
	 */
	public void LoadFCL( Vector<String> fileNamesIn )
	{
		// Load fcl files
		for( String file : fileNamesIn )
		{	
			_files.add( FIS.load( file ) );
		}
	
		if( _files.isEmpty() )
		{
			System.err.println( "Error loading .fcl files." );
			System.exit( -1 );
		}
		
		// Load function blocks from the fcls
		for( FIS file : _files )
		{
			_blocks.add( file.getFunctionBlock( null ) );
		}
	}
	
	public void DisplayFuzzyGraphs()
	{
		for( FunctionBlock block : _blocks )
		{
			JFuzzyChart.get().chart( block );
		}
	}
	
	public void DisplayRules()
	{
		for( FIS file : _files )
		{
			System.out.print( file );
		}
	}
	
	public void SetVariable( String ruleNameIn, String varIn, double valIn )
	{
		// Hard codd 3-tuple as in CAnimat - alignment, attraction, repulsion
		if( ruleNameIn == "alignment" )
		{
			_files.elementAt( 0 ).setVariable( varIn, valIn );
		}
		if( ruleNameIn == "attraction" )
		{
			_files.elementAt( 1 ).setVariable( varIn, valIn );
		}
		if( ruleNameIn == "repulsion" )
		{
			_files.elementAt( 2 ).setVariable( varIn, valIn );
		}
	}
	
	public void Evaluate()
	{
		_fuzzyOutputs.clear();
		
		for( FIS file : _files )
		{
			file.evaluate();
			// Order is going to matter due to different weights for the fuzzy fusion decision
			_fuzzyOutputs.add( new CFuzzyStruct( file.getVariable( "flight_speed" ), file.getVariable( "flight_direction" ) ) );
		}
	}
	
	/**
	 * Return full vector of values.
	 * Unit testing only. GetNewPositionAndVelocity returns new fuzzy struct decision.
	 * @param varIn
	 * @param 
	 * @return
	 */
	public Vector<CFuzzyStruct> GetVariableResult( String varIn )
	{
		return _fuzzyOutputs;
	}
	
	public int GetWeightedAvgSumDirections()
	{
		int sum = 0;
		
		for( CFuzzyStruct fuzzy : _fuzzyOutputs )
		{
			double val = fuzzy.GetFlightDirection().getValue();
			
			if( val >= 0.0 )
			{
				sum += ( val * fuzzy.GetFlightDirectionWeight() ) ;
			}
		}
		
		sum /= _numberFCL;
		
		return sum;
	}
	
	public double GetWeightedAvgSumSpeed()
	{
		double sum = 0;
		
		for( CFuzzyStruct fuzzy : _fuzzyOutputs )
		{
			double val = fuzzy.GetFlightSpeed().getValue();
			if( val >= 0.0 )
			{
				sum += ( val * fuzzy.GetFlightSpeedWeight() ) ;
			}
		}
		
		sum /= _numberFCL;
		
		return sum;
	}
}
