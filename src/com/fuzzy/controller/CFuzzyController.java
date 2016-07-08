package com.fuzzy.controller;

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
	
	private Vector<CFuzzyStruct> _fuzzyOutputs;
	
	public CFuzzyController( int numFCLIn )
	{
		_numberFCL = numFCLIn;
		_fclFileNames = new Vector<String>( _numberFCL )	;
	}
	
	public void SetNumberFCL( int numFCLIn )
	{
		_numberFCL = numFCLIn;
		
	}
	
	public int GetNumberFCL()
	{
		return _numberFCL;
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
	
	public void SetVariable( String varIn, double valIn )
	{
		for( FIS file : _files )
		{
			file.setVariable( varIn,  valIn );
		}
	}
	
	public void Evaluate()
	{
		for( FIS file : _files )
		{
			file.evaluate();
			// Order is going to matter due to different weights for the fuzzy fusion decision
			_fuzzyOutputs.add( new CFuzzyStruct( file.getVariable( "flight_speed" ), file.getVariable( "flight_direction" ) ) );
		}
	}
	
	/**
	 * Not sure about this one. May be best to return full vector of values.
	 * Unit testing only. GetNewPositionAndVelocity returns new fuzzy struct decision.
	 * @param varIn
	 * @param displayChart
	 * @return
	 */
	public Variable GetVariableResult( String varIn, boolean displayChart )
	{
		for( FunctionBlock block : _blocks )
		{
			
		}
		// Must reset the _fuzzyOutput vector for now
		_fuzzyOutputs.clear();
		
		return null;
	}
	
	public CFuzzyStruct GetNewPositionAndVelocity()
	{
		// TODO - Compute uncertain actions for each rule
		
		// TODO - Compute fuzzy weighted sum - Velocity
		
		// TODO - Compute Position
		return null;
	}
}
