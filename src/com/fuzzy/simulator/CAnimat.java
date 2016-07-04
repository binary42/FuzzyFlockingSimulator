/**
 * Again, heavily drawn from Lalena's sim.
 */
package com.fuzzy.simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

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

	public CAnimat(Color colorIn ) {
		this((int)(Math.random() * map.width), (int)(Math.random() * map.height), (int)(Math.random() * 360), colorIn );
	}
	
	public CAnimat( int xIn, int yIn, int thetaIn, Color colorIn ){
		location.x = xIn;
		location.y = yIn;
		
		_currentTheta = thetaIn;
		_color = colorIn;
	}

	public CAnimat() {
		// TODO Auto-generated constructor stub
	}

	public static void setMapSize( Dimension sizeIn ) {
		s_map = sizeIn;
	}

	public void setSpeed( int speedIn ) {
		_currentSpeed = speedIn;
	}

	public void setMaxTurnTheta( int thetaIn ) {
		_maxTurnTheta = thetaIn;
	}

	public Color getColor() {
		
		return _color;
	}
	
	public void draw( Graphics g) {
        g.setColor( this._color);
        g.fillArc( location.x - 12, location.y - 12, 24, 24, _currentTheta + 180 - 20, 40 );
        
        if( showRanges) {
            drawRanges( g );
        }
    }
	
	public void drawRanges( Graphics g ) {
        drawCircles(g, location.x, location.y );
        
        boolean top = ( location.y < DetectionRange );
        boolean bottom = ( location.y > s_map.height - DetectionRange );
        // if the obstacle radius overlaps an edge, then display a second obstacle
        // with a center point outside the range of the displayed area.
        // This way, the birds can react to it before wrapping, and it will also get drawn.
        if (location.x < DetectionRange) { // if left
            drawCircles(g, s_map.width + location.x, location.y );
            if (top) {
                drawCircles(g, s_map.width + location.x, s_map.height + location.y );
            }
            else if ( bottom ) {
                drawCircles(g, s_map.width + location.x, location.y - s_map.height );
            }
        } else if ( location.x > s_map.width - DetectionRange ) { // if right
            drawCircles(g, location.x - s_map.width, location.y );
            if ( top ) {
                drawCircles(g, location.x - s_map.width, s_map.height + location.y );
            }
            else if ( bottom ) {
                drawCircles(g, location.x - s_map.width, location.y - s_map.height );
            }
        }
        if ( top ) {
            drawCircles( g, location.x, s_map.height + location.y );
        }
        else if ( bottom ) {
            drawCircles( g, location.x, location.y - s_map.height );
        }
    }
	
	protected void drawCircles( Graphics g, int x, int y ) {
        g.setColor( new Color((int)_color.getRed()/2, (int)_color.getGreen()/2, (int)_color.getBlue()/2 ) );
        g.drawOval( x-DetectionRange, y-DetectionRange, 2*DetectionRange, 2*DetectionRange );
        g.setColor( _color);
        g.drawOval( x-SeparationRange, y-SeparationRange, 2*SeparationRange, 2*SeparationRange );
    }
}
