/**
 * Again, heavily drawn from Lalena's sim. Contains constructor and general
 * movement method for sim. The fuzzy alg. is in CFlock Move
 */
package com.fuzzy.simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

import com.fuzzy.controller.CFuzzyController;
import com.fuzzy.controller.CFuzzyStruct;

// Rule class structure
class CRuleStruct{
	public static String rule1 = "alignment";
	public static String rule2 = "attraction";
	public static String rule3 = "repulsion";
}

public class CAnimat {

	public static int DetectionRange;
	public static int SeparationRange;
	public static boolean showRanges;
	
	protected Point location = new Point( 0, 0 );
	private int _currentTheta;
	protected Color _color;
	
	protected static Dimension s_map = new Dimension( CFlock.DIM, CFlock.DIM );
	
	private double _currentSpeed;
	private int _maxTurnTheta;
	
    // Each Animat gets a fuzzy controller
    private CFuzzyController _animatController;
    
	public CAnimat( Color colorIn ) {
		this((int)( Math.random() * s_map.width ), (int)( Math.random() * s_map.height ), (int)( Math.random() * 360 ), colorIn );
	}
	
	public CAnimat( int xIn, int yIn, int thetaIn, Color colorIn ){
		location.x = xIn;
		location.y = yIn;
		
		_currentTheta = thetaIn;
		_color = colorIn;
		
		// Hard code # of rule sets for now...
		_animatController = new CFuzzyController( 3 );
		InitController();
	}

	public CAnimat() {
		// Hard code # of rule sets for now...
		_animatController = new CFuzzyController( 3 );
		InitController();
	}

	private void InitController()
	{
		Vector<String> files = new Vector<String>( 3 );
		// Hardcode order TODO - change this hack
		files.add( "../rules/alignment_rules.fcl" );
		files.add( "../rules/attraction_rules.fcl" );
		files.add( "../rules/repulsion_rules.fcl" );
		
		_animatController.LoadFCL( files );
	}
	
	public static void SetMapSize( Dimension sizeIn ) {
		s_map = sizeIn;
	}

	public void SetSpeed( int speedIn ) {
		_currentSpeed = speedIn;
	}

	public void SetMaxTurnTheta( int thetaIn ) {
		_maxTurnTheta = thetaIn;
	}

	public Color GetColor() {
		
		return _color;
	}
	
	public void Draw( Graphics g) {
        g.setColor( this._color);
        g.fillArc( location.x - 12, location.y - 12, 24, 24, _currentTheta + 180 - 20, 40 );
        
        if( showRanges) {
            DrawRanges( g );
        }
    }
	// Lalena
	public void DrawRanges( Graphics g ) {
        DrawCircles(g, location.x, location.y );
        
        boolean top = ( location.y < DetectionRange );
        boolean bottom = ( location.y > s_map.height - DetectionRange );
       
        
        if (location.x < DetectionRange) { // if left
            DrawCircles(g, s_map.width + location.x, location.y );
            
            if (top) {
                DrawCircles(g, s_map.width + location.x, s_map.height + location.y );
            }
            else if ( bottom ) {
                DrawCircles(g, s_map.width + location.x, location.y - s_map.height );
            }
        } else if ( location.x > s_map.width - DetectionRange ) { // if right
            DrawCircles(g, location.x - s_map.width, location.y );
            
            if ( top ) {
                DrawCircles(g, location.x - s_map.width, s_map.height + location.y );
            }
            else if ( bottom ) {
                DrawCircles(g, location.x - s_map.width, location.y - s_map.height );
            }
        }
        
        if ( top ) {
            DrawCircles( g, location.x, s_map.height + location.y );
        }
        else if ( bottom ) {
            DrawCircles( g, location.x, location.y - s_map.height );
        }
    }
	
	protected void DrawCircles( Graphics g, int x, int y ) {
        g.setColor( new Color((int)_color.getRed()/2, (int)_color.getGreen()/2, (int)_color.getBlue()/2 ) );
        g.drawOval( x-DetectionRange, y-DetectionRange, 2*DetectionRange, 2*DetectionRange );
        
        g.setColor( _color);
        g.drawOval( x-SeparationRange, y-SeparationRange, 2*SeparationRange, 2*SeparationRange );
    }
	
	// Lalena
	public void Move( int newHeadingIn ) {
        // determine if it is better to turn left or right for the new heading
        int left = ( newHeadingIn - _currentTheta + 360 ) % 360;
        int right = ( _currentTheta - newHeadingIn + 360 ) % 360;
        
        // after deciding which way to turn, find out if we can turn that much
        int thetaChange = 0;
        if (left < right) {
            // if left > than the max turn, then we can't fully adopt the new heading
            thetaChange = Math.min( _maxTurnTheta, left );
        }
        else {
            // right turns are negative degrees
            thetaChange = -Math.min( _maxTurnTheta, right );
        }
        
        // Make the turn
        _currentTheta = ( _currentTheta + thetaChange + 360 ) % 360;
        
        // Now move currentSpeed pixels in the direction the animat now faces.
        // Note: Because values are truncated, a speed of 1 will result in no
        // movement unless the animat is moving exactly vertically or horizontally.
        location.x += (int)( _currentSpeed * Math.cos( _currentTheta * Math.PI/180) ) + s_map.width;
        location.x %= s_map.width;
        
        location.y -= (int)( _currentSpeed * Math.sin( _currentTheta * Math.PI/180) ) - s_map.height;
        location.y %= s_map.height;
    }

	public Point GetLocation() {
		return location;
	}
	
	public int GetCurrentHeading()
	{
		return _currentTheta;
	}
	
	public double GetCurrentSpeed()
	{
		return _currentSpeed;
	}
	public int GetLocationDegrees( CAnimat otherAnimatIn )
	{
		
		return 0;
	}
	// Lalena ---------------
	public int GetDistance( CAnimat otherAnimatIn ) {
        int dX = otherAnimatIn.GetLocation().x - location.x;
        int dY = otherAnimatIn.GetLocation().y - location.y;
        
        return (int)Math.sqrt( Math.pow( dX, 2 ) + Math.pow( dY, 2 ));
	}
	
	public int GetDistance( Point pointIn ) {
        int dX = pointIn.x - location.x;
        int dY = pointIn.y - location.y;
        
        return (int)Math.sqrt( Math.pow( dX, 2 ) + Math.pow( dY, 2 ));
    }
	// --------------- 
	public CFuzzyStruct GetFuzzyVelocityAndHeading( CAnimat otherAnimatIn )
	{
		// Algorithm ---------------------------------------------------------
		// Set variables for each fuzzy attribute
		
		// Direction - Distance difference in degrees from neighbor
		_animatController.SetVariable( CRuleStruct.rule1, "distance", this.GetDistance( otherAnimatIn ) );
		_animatController.SetVariable( CRuleStruct.rule1, "direction", this.GetCurrentHeading() );
		_animatController.SetVariable( CRuleStruct.rule1, "speed", this.GetCurrentSpeed() );
		
		// Position needs degree -180 to 180 of location of other animat
		_animatController.SetVariable( CRuleStruct.rule2, "position", this.GetLocationDegrees( otherAnimatIn ) );
		_animatController.SetVariable( CRuleStruct.rule2, "distance", this.GetDistance( otherAnimatIn ) );

		
		_animatController.SetVariable( CRuleStruct.rule3, "distance", this.GetDistance( otherAnimatIn ) );
		_animatController.SetVariable( CRuleStruct.rule3, "direction", this.GetCurrentHeading() );

		
		// Evaluate for each rules set - alignment/attraction/repulsion
		_animatController.Evaluate();
		
		// Get New position and velocity from controller
		
		// Set new velocity and heading
		
		return null;
	}
}
