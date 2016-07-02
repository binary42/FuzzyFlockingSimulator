package com.fuzzy.controller;

import java.util.Vector;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

public class CFuzzyController {
	
	private int _numberFCL;
	private Vector<String> _fclFileNames;
	
	private Vector<FIS> _files;
	private Vector<FunctionBlock> _blocks;
	
	CFuzzyController( int numFCLIn )
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
		// TODO - multiple fcls?
	}
	
	public void Evaluate()
	{
		// TODO - multiple fcls?
	}
	
	public void GetVariableResult( String varIn, boolean displayChart )
	{
		// TODO - multiple fcls?
	}
}
