/**
 * Again, heavily drawn from Lalena's sim.
 */
package com.fuzzy.simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;


import com.fuzzy.controller.CFuzzyController;

public class CFlock {

	public static int SeparationRange;
	public static int DetectionRange;
	
	public static final int DIM = 600;
	
	private Vector<CAnimat> _animats;

    private static Dimension s_map;
    
    CFlock()
    {
    	s_map = new Dimension( DIM, DIM );
    	_animats = new Vector<CAnimat>( 40, 10 );
    }
	
	public static void SetMapSize( Dimension sizeIn ) {
		s_map = sizeIn;
	}

	synchronized void AddAnimat( CAnimat animatIn ) {
		_animats.addElement( animatIn );
	}
	// Lalena
	synchronized void RemoveCAnimat( Color colorIn ) {
		for( CAnimat animat : _animats )
		{
			if( animat.GetColor() == colorIn )
			{
				_animats.removeElement( animat );
				break;
			}
		}
	}

	public void SetCAnimatParameters( Color colorIn, int speedIn, int thetaIn ) {
		for( CAnimat animat : _animats )
		{
			if( animat.GetColor() == colorIn )
			{
				animat.SetSpeed( speedIn );
				animat.SetMaxTurnTheta( thetaIn );
			}
		}
	}

	public void draw( Graphics canvasGraphicsIn ) {
		for( CAnimat animat : _animats)
		{
			animat.draw( canvasGraphicsIn );
		}
	}
	// departure from Lalena - fuzzy bits
	synchronized public Vector<CAnimat> Move()
	{
		int movingAnimat = 0;
		Vector<CAnimat> removedAnimats = new Vector<CAnimat>( 2, 1 );
		
		while( movingAnimat < _animats.size() )
		{
			CAnimat animat = (CAnimat)_animats.elementAt( movingAnimat );

			animat.Move( GeneralHeading( animat ) );
			
			movingAnimat++;
		}
		
		return removedAnimats;
	}
	
	public Point SumPoints( Point p1, double w1, Point p2, double w2 ) {
        return new Point( (int)( w1*p1.x + w2*p2.x ), (int)( w1*p1.y + w2*p2.y ) );
    }
	
	public int SumHeading( int p1, double w1, int p2, double w2 ) {
        return ( (int)( w1*p1 + w2*p2 ));
    }
	
	public double SizeOfPoint( Point p ) {
        return Math.sqrt( Math.pow( p.x, 2 ) + Math.pow( p.y, 2 ) );
    }
	
	public Point normalisePoint( Point p, double n ) {
        if ( SizeOfPoint( p ) == 0.0 ) {
            return p;
        }
        else {
            double weight = n / SizeOfPoint(p);
            return new Point( (int)( p.x * weight ), (int)( p.y * weight ) );
        }
    }
	// Here's the departure of Lalena's impl of Reynolds flocking. This
	// bit is an impl from Bajec's thesis - University of Ljubljana "Fuzzy Model for a Computer Simulation of Bird Flocking"
	private int GeneralHeading( CAnimat animat )
	{
		Point target = new Point( 0, 0 );
		
		int testHeading = animat.GetCurrentHeading();
		int heading = animat.GetCurrentHeading();
		int targetrepheading = animat.GetCurrentHeading();
		int numAnimats = 0;
		
		for( CAnimat otherAnimat : _animats )
		{// TODO -- THIS IS ALL WRONG FOR THE FUZZY WORK....
			
			Point otherLocation = ClosetLocation( animat.GetLocation(), otherAnimat.GetLocation() );  
			
			// get distance to the other Bird. Note, this distance accounts for
            // the fact that the shortest path may be through the edge of the map -- Lalena
			int animatDistance = animat.GetDistance( otherAnimat );
			
			// Similar to Lalena's, animats of same type attract one another and display flocking
			// others repel ... animat.Equals( otherAnimat ) && 
			
			if( animatDistance > 0 && animatDistance <= DetectionRange )
			{
				if( animat.Equals( otherAnimat ) )
				{
					// Flock
					testHeading = animat.GetFuzzyVelocityAndHeading( otherAnimat );
					
					testHeading = (testHeading + 360 ) % 360;
					
					heading = SumHeading( animat.GetCurrentHeading(), 1.0, testHeading, 1.0);
					
				}
					
//				numAnimats++;
			}else
			{
				return animat.GetCurrentHeading();
				
			}
		}
		
		 // if no birds are close enough to detect, continue moving is same direction. - Lalena
//        if ( numAnimats == 0) {
//            return animat.GetCurrentHeading();
//        }

        return heading;
	}
	// Lalena
	private Point ClosetLocation( Point point1In, Point point2In ) {
		int dX = Math.abs(point2In.x - point1In.x);
        int dY = Math.abs(point2In.y - point1In.y);
        int x = point2In.x;
        int y = point2In.y;
        
        // now see if the distance between birds is closer if going off one
        // side of the map and onto the other.
        if ( Math.abs( s_map.width - point2In.x + point1In.x ) < dX ) {
            dX = s_map.width - point2In.x + point1In.x;
            x = point2In.x - s_map.width;
        }
        if ( Math.abs( s_map.width - point1In.x + point2In.x ) < dX ) {
            dX = s_map.width - point1In.x + point2In.x;
            x = point2In.x + s_map.width;
        }
        
        if ( Math.abs( s_map.height - point2In.y + point1In.y ) < dY ) {
            dY = s_map.height - point2In.y + point1In.y;
            y = point2In.y - s_map.height;
        }
        if ( Math.abs( s_map.height - point1In.y + point2In.y ) < dY ) {
            dY = s_map.height - point1In.y + point2In.y;
            y = point2In.y + s_map.height;
        }
        
        return new Point( x, y );
	}
}
