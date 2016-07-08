/**
 * Again, heavily drawn from Lalena's sim. Contains constructor and general
 * movement method for sim. The fuzzy alg. is in CFlock Move
 */
package com.fuzzy.simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import com.boids.lalena.Bird;

public class CAnimat {

	public static int DetectionRange;
	public static int SeparationRange;
	public static boolean showRanges;
	
	protected Point location;
	private int _currentTheta;
	protected Color _color;
	
	protected static Dimension s_map;
	
	private double _currentSpeed;
	private int _maxTurnTheta;

	public CAnimat( Color colorIn ) {
		this((int)( Math.random() * s_map.width ), (int)( Math.random() * s_map.height ), (int)( Math.random() * 360 ), colorIn );
		s_map = new Dimension( 500, 500 );
	}
	
	public CAnimat( int xIn, int yIn, int thetaIn, Color colorIn ){
		location.x = xIn;
		location.y = yIn;
		
		_currentTheta = thetaIn;
		_color = colorIn;
		
		s_map = new Dimension( 500, 500 );
	}

	public CAnimat() {
		s_map = new Dimension( 500, 500 );
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
	// Lalena
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
}
