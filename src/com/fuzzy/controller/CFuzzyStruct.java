package com.fuzzy.controller;

import net.sourceforge.jFuzzyLogic.rule.Variable;

public class CFuzzyStruct {
	private Variable _flightSpeed;
	private Variable _flightDirection;
	private double	 _flightSpeedWeight;
	private double 	 _flightDirectionWeight;
	
	CFuzzyStruct( Variable speedIn, Variable directionIn )
	{
		_flightSpeed = speedIn;
		_flightDirection = directionIn;
	}
	
	public void SetFlightSpeed( Variable speedIn )
	{
		_flightSpeed = speedIn;
	}
	
	public Variable GetFlightSpeed()
	{
		return _flightSpeed;
	}
	
	public void SetFlightDirection( Variable directionIn )
	{
		_flightDirection = directionIn;
	}
	
	public Variable GetFlightDirection()
	{
		return _flightDirection;
	}
	
	public void SetFlightSpeedWeight( double weightIn )
	{
		_flightSpeedWeight = weightIn;
	}
	
	public double GetFlightSpeedWeight()
	{
		return _flightSpeedWeight;
	}
	
	public void SetFlightDirectionWeight( double weightIn )
	{
		_flightDirectionWeight = weightIn;
	}
	
	public double GetFlightDirectionWeight()
	{
		return _flightDirectionWeight;
	}
}
