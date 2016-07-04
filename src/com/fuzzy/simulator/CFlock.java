/**
 * Again, heavily drawn from Lalena's sim.
 */
package com.fuzzy.simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

public class CFlock {

	public static int SeparationRange;
	public static int DetectionRange;
	
	private Vector<CAnimat> _animats;

    private static Dimension s_map;
    
    CFlock()
    {
    	s_map = new Dimension( 500, 500 );
    	_animats = new Vector<CAnimat>( 40, 10 );
    }
	
	public void SetMapSize( Dimension sizeIn ) {
		s_map = sizeIn;
	}

	synchronized void AddAnimat( CAnimat animatIn ) {
		_animats.addElement( animatIn );
	}

	synchronized void RemoveCAnimat( Color colorIn ) {
		for( int i = 0; i < _animats.size(); ++i )
		{
			CAnimat animat = (CAnimat)_animats.elementAt( i );
			
			if( animat.GetColor() == colorIn )
			{
				_animats.removeElementAt( i );
				break;
			}
		}
	}

	public void SetCAnimatParameters( Color colorIn, int speedIn, int thetaIn ) {
		for( int i = 0; i < _animats.size(); ++i )
		{
			CAnimat animat = (CAnimat)_animats.elementAt( i );
			
			if( animat.GetColor() == colorIn )
			{
				animat.SetSpeed( speedIn );
				animat.SetMaxTurnTheta( thetaIn );
			}
		}
	}

	public void Draw( Graphics canvasGraphicsIn ) {
		for(int i = 0; i < _animats.size(); ++i )
		{
			CAnimat animat = (CAnimat)_animats.elementAt( i );
			animat.Draw( canvasGraphicsIn );
		}
	}
	
	synchronized public Vector<CAnimat> Move()
	{
		int movingAnimat = 0;
		Vector<CAnimat> movedAnimats = new Vector<CAnimat>( 2, 1 );
		
		while( movingAnimat < _animats.size() )
		{
			CAnimat animat = (CAnimat)_animats.elementAt( movingAnimat );
			animat.Move( GeneralHeading( animat ) );
			
		}
		
		return movedAnimats;
	}
	
	public Point SumPoints( Point p1, double w1, Point p2, double w2 ) {
        return new Point( (int)( w1*p1.x + w2*p2.x ), (int)( w1*p1.y + w2*p2.y ) );
    }
	
	public double SizeOfPoint(Point p) {
        return Math.sqrt( Math.pow( p.x, 2 ) + Math.pow( p.y, 2 ) );
    }
	
	public Point normalisePoint( Point p, double n ) {
        if ( SizeOfPoint(p) == 0.0 ) {
            return p;
        }
        else {
            double weight = n / SizeOfPoint(p);
            return new Point((int)(p.x * weight), (int)(p.y * weight));
        }
    }
	
	private int GeneralHeading( CAnimat animat )
	{
		
	}
}
