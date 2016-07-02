package com.fuzzy.controller;

import net.sourceforge.jFuzzyLogic.rule.Variable;

public class CFuzzyStruct {
	private Variable _flightSpeed;
	private Variable _flightDirection;
	
	CFuzzyStruct( Variable velocityIn, Variable directionIn )
	{
		_flightSpeed = velocityIn;
		_flightDirection = directionIn;
	}
	
	public void SetVelocity( Variable velocityIn )
	{
		_flightSpeed = velocityIn;
	}
	
	public double GetFlightSpeed()
	{
		return _flightSpeed;
	}
	
	public void SetDirection( Variable directionIn )
	{
		_flightDirection = directionIn;
	}
	
	public double GetFlightDirection()
	{
		return _flightDirection;
	}
}
