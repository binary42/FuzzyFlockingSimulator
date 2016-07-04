/**
 * Heavily drawn from Michael Lalena's CFlock Simulator Applet found in
 * com.boids.lalena
 * 
 * This is a cut-n-paste of Lalena's sim, with mods for my use case.
 * 
 */

package com.fuzzy.simulator;

import java.awt.*;
import java.applet.*;
import java.util.Vector;

public class CSimulator extends Applet implements Runnable {
	
	// This class holds the CAnimats and controls their movement.
    CFlock flock;
    
    // Thread for moving the CAnimats
    Thread thread;
    
    // Canvas that displays the CAnimats moving around
    SimulatorCanvas canvas;
    
    // Panel containing all the sliders that control the CAnimats
    Panel controls;
    
    // These are the upper limits for the sliders
    final int MAXIMUM_ANIMATS = 50;
    final int MAXIMUM_SPEED = 30;

    final int MAXIMUM_DISTANCE = 200;
    
    // These are the default values for the sliders, also used for a reset
    int DEFAULT_NUMBER_GREEN = 10;
    int DEFAULT_NUMBER_BLUE = 15;
    int DEFAULT_NUMBER_RED = 3;
    
    int DEFAULT_GREEN_THETA = 15;
    int DEFAULT_BLUE_THETA = 10;
    int DEFAULT_RED_THETA = 20;
    
    int DEFAULT_GREEN_SPEED = 5;
    int DEFAULT_BLUE_SPEED = 6;
    int DEFAULT_RED_SPEED = 7;
   
    int DEFAULT_OBSTACLE_SEPARATE = 30;
    int DEFAULT_OBSTACLE_DETECT = 60;
    
    // These are the values set by the sliders
    int numberOfGreenAnimats;
    int numberOfBlueAnimats;
    
    int numberOfRedAnimats;
    int greenAnimatSpeed;
    int blueAnimatSpeed;
    
    int redAnimatSpeed;
    int greenAnimatMaxTheta;
    
    int blueAnimatMaxTheta;
    int redAnimatMaxTheta;

    int separateDistance;
    int detectDistance;
    
    // These are the scrollbar sliders themselves
    Scrollbar greenMunberAnimatsScrollbar = new Scrollbar( Scrollbar.HORIZONTAL );
    Scrollbar blueNumberAnimatsScrollbar = new Scrollbar( Scrollbar.HORIZONTAL );
    Scrollbar redNumberAnimatsScrollbar = new Scrollbar( Scrollbar.HORIZONTAL );
    
    Scrollbar greenMaxThetaScrollbar = new Scrollbar( Scrollbar.HORIZONTAL );
    Scrollbar blueMaxThetaScrollbar = new Scrollbar( Scrollbar.HORIZONTAL );
    Scrollbar redMaxThetaScrollbar = new Scrollbar( Scrollbar.HORIZONTAL );
    
    Scrollbar greenSpeedScrollbar = new Scrollbar( Scrollbar.HORIZONTAL );
    Scrollbar blueSpeedScrollbar = new Scrollbar( Scrollbar.HORIZONTAL );
    Scrollbar redSpeedScrollbar = new Scrollbar( Scrollbar.HORIZONTAL );
    
    Scrollbar detectScrollbar = new Scrollbar( Scrollbar.HORIZONTAL );
    Scrollbar separateScrollbar = new Scrollbar( Scrollbar.HORIZONTAL );

    // Top Title Bar
    Label titleLabel = new Label( "CFlocking Simulator by binary42. Thanks to M. Lalena" );
        
    // These are the labels on the sliders
    Label controlsLabel = new Label( "Controls" );
    Label blueLabel = new Label( "Blue CAnimats" );
    Label greenLabel = new Label( "Green CAnimats" );
    
    Label redLabel = new Label( "Red CAnimats" );
    Label speedLabel = new Label( "Speed" );
    Label turnLabel = new Label( "Max Turn" );
    
    Label numberCAnimatsLabel = new Label( "Number CAnimats" );
    
    Label detectLabel = new Label( "Detection Distance" );
    Label separateLabel = new Label( "Separate To Avoid Dist" );
    
    // The reset button
    Button resetButton = new Button( "Reset" );
    
    // Checking this checkbox will show the detect and separation ranges for each CAnimat
    Checkbox showRangesCheckbox = new Checkbox( "Show Dist Ranges" );
    
    /**
     * This is the java init function.
     * This creates the canvas, the sliders, and the CFlock of CAnimats.
     */
    public void init() {
        this.resize(500, 500);
        canvas = new SimulatorCanvas();
        canvas.simulator = this;
        
        controls = new Panel();
        addControls();
        resetValues();
        
        setLayout( new BorderLayout() );
        titleLabel.setAlignment( Label.CENTER );
        
        add("Center", canvas);
        add("South", titleLabel);
        add("North", controls);
        
        canvas.requestFocus();
        
        CAnimat.setMapSize( canvas.getSize() );
        CFlock.setMapSize( canvas.getSize() );
    }
    
    /**
     * This will reset the user configurable parameters that control the CAnimats.
     * It will reset the slider values and the CFlock object.
     */
    public void resetValues() {
        numberOfGreenAnimats = DEFAULT_NUMBER_GREEN;
        numberOfBlueAnimats = DEFAULT_NUMBER_BLUE;
        numberOfRedAnimats = DEFAULT_NUMBER_RED;
        
        greenAnimatMaxTheta = DEFAULT_GREEN_THETA;
        blueAnimatMaxTheta = DEFAULT_BLUE_THETA;
        redAnimatMaxTheta = DEFAULT_RED_THETA;
        
        greenAnimatSpeed = DEFAULT_GREEN_SPEED;
        blueAnimatSpeed = DEFAULT_BLUE_SPEED;
        redAnimatSpeed = DEFAULT_RED_SPEED;
        
        separateDistance = DEFAULT_OBSTACLE_SEPARATE;
        detectDistance = DEFAULT_OBSTACLE_DETECT;
        
        setControlValues();
        
        CAnimat.SeparationRange = separateDistance;
        CAnimat.DetectionRange = detectDistance;
        
        flock = new CFlock();
        
        CFlock.SeparationRange = separateDistance;
        CFlock.DetectionRange = detectDistance;
        
        for( int i=0; i < numberOfGreenAnimats; ++i) {
        	CAnimat animat = new CAnimat( Color.green );
            animat.setSpeed( greenAnimatSpeed );
            
            animat.setMaxTurnTheta( greenAnimatMaxTheta );
            
            CFlock.addAnimat( animat );
        }
        for( int i=0; i < numberOfBlueAnimats; ++i ) {
        	CAnimat animat = new CAnimat( Color.blue );
        	animat.setSpeed( blueAnimatSpeed );
        	
        	animat.setMaxTurnTheta( blueAnimatMaxTheta );
            
        	CFlock.addAnimat( animat );
        }
        for( int i=0; i < numberOfRedAnimats; ++i ) {
            CAnimat CAnimat = new CAnimat();
            CAnimat.setSpeed( redAnimatSpeed );
            
            CAnimat.setMaxTurnTheta( redAnimatMaxTheta );
            
            CFlock.addAnimat( CAnimat );
        }
    }

    /**
     * This is the java applet start function.
     * It starts the thread that controls the animats.
     */
    public void start() {
        if ( thread == null ) {
            thread = new Thread( this );
            thread.start();
        }
    }
    
    /**
     * This is the java applet stop function.
     * It stops the thread that controls the animats.
     */
    public void stop() {
        if ( thread != null ) {
            thread.stop();
            thread = null;
        }
    }
    
    /**
     * This is the java applet run function.
     * It loops forever moving the CAnimats, and controls the logic
     * when a CAnimat is removed.
     */
    public void run() {
        while ( true ) {
            CAnimat.setMapSize(canvas.getSize());
            
            CFlock.setMapSize(canvas.getSize());
            
            Vector removedCAnimats = CFlock.move();
            
            for ( int i = 0; i < removedCAnimats.size(); ++i ) {
                CAnimat CAnimat = (CAnimat)removedCAnimats.elementAt(i);
                if ( CAnimat.getColor().equals( Color.red )) {
                    numberOfRedAnimats = redNumberAnimatsScrollbar.getValue() - 1;
                    redNumberAnimatsScrollbar.setValue( numberOfRedAnimats );
                }
                else if ( CAnimat.getColor().equals( Color.blue )) {
                    numberOfBlueAnimats = blueNumberAnimatsScrollbar.getValue() - 1;
                    blueNumberAnimatsScrollbar.setValue( numberOfBlueAnimats );
                }
                else if ( CAnimat.getColor().equals( Color.green )) {
                    numberOfGreenAnimats = greenMunberAnimatsScrollbar.getValue() - 1;
                    greenMunberAnimatsScrollbar.setValue( numberOfGreenAnimats );
                }
            }
            
            canvas.validate();
            canvas.setVisible( true );
            canvas.repaint();
            canvas.invalidate();
            
            invalidate();
            repaint();
            
            this.repaint( canvas.getLocation().x, canvas.getLocation().y, canvas.getSize().width, canvas.getSize().height );
            
            try {
                Thread.sleep( 20 ); // interval between steps
            } catch (InterruptedException e) {}
        }
    }

    /**
     * This event is fired when a user changes one of the panel parameters.
     *
     * @param  ev The event parameters
     */
    public boolean handleEvent(Event ev) { // check for control panel actions
        if ( ev.target == greenMunberAnimatsScrollbar ) {
            if ( greenMunberAnimatsScrollbar.getValue() < numberOfGreenAnimats ) {
                for ( int i = 0; i < numberOfGreenAnimats - greenMunberAnimatsScrollbar.getValue(); ++i ) {
                    CFlock.removeCAnimat( Color.green );
                }
            }
            else {
                for ( int i = 0; i < greenMunberAnimatsScrollbar.getValue() - numberOfGreenAnimats; ++i ) {
                    CAnimat animat = new CAnimat( Color.green );
                    animat.setSpeed( greenAnimatSpeed );
                    
                    animat.setMaxTurnTheta( greenAnimatMaxTheta );
                    
                    CFlock.addAnimat( animat );
                }
            }
            numberOfGreenAnimats = greenMunberAnimatsScrollbar.getValue();
        }
        else if ( ev.target == blueNumberAnimatsScrollbar ) {
            if ( blueNumberAnimatsScrollbar.getValue() < numberOfBlueAnimats ) {
                for ( int i = 0; i < numberOfBlueAnimats - blueNumberAnimatsScrollbar.getValue(); ++i ) {
                    CFlock.removeCAnimat( Color.blue );
                }
            }
            else {
                for ( int i = 0; i < blueNumberAnimatsScrollbar.getValue() - numberOfBlueAnimats; ++i ) {
                    CAnimat animat = new CAnimat( Color.blue );
                    animat.setSpeed( blueAnimatSpeed );
                    
                    animat.setMaxTurnTheta( blueAnimatMaxTheta );
                    
                    CFlock.addAnimat( animat );
                }
            }
            numberOfBlueAnimats = blueNumberAnimatsScrollbar.getValue();
        }
        else if ( ev.target == redNumberAnimatsScrollbar ) {
            if ( redNumberAnimatsScrollbar.getValue() < numberOfRedAnimats ) {
                for ( int i = 0; i < numberOfRedAnimats - redNumberAnimatsScrollbar.getValue(); ++i ) {
                    CFlock.removeCAnimat( Color.red );
                }
            }
            else {
                for ( int i = 0; i < redNumberAnimatsScrollbar.getValue() - numberOfRedAnimats; ++i ) {
                    CAnimat animat = new CAnimat();
                    animat.setSpeed( redAnimatSpeed );
                    
                    animat.setMaxTurnTheta( redAnimatMaxTheta );
                    
                    CFlock.addAnimat( animat );
                }
            }
            numberOfRedAnimats = redNumberAnimatsScrollbar.getValue();
        }
        else if ( ( ev.target == greenSpeedScrollbar) || ( ev.target == greenMaxThetaScrollbar ) ) {
            greenAnimatSpeed = greenSpeedScrollbar.getValue();
            greenAnimatMaxTheta = greenMaxThetaScrollbar.getValue();
           
            CFlock.setCAnimatParameters( Color.green, greenAnimatSpeed, greenAnimatMaxTheta );
            
            return true;
        }
        else if ( ( ev.target == blueSpeedScrollbar ) || (ev.target == blueMaxThetaScrollbar ) ) {
            blueAnimatSpeed = blueSpeedScrollbar.getValue();
            blueAnimatMaxTheta = blueMaxThetaScrollbar.getValue();
            
            CFlock.setCAnimatParameters(Color.blue, blueAnimatSpeed, blueAnimatMaxTheta );
        }
        else if ( ( ev.target == redSpeedScrollbar ) || (ev.target == redMaxThetaScrollbar ) ) {
            redAnimatSpeed = redSpeedScrollbar.getValue();
            redAnimatMaxTheta = redMaxThetaScrollbar.getValue();
            
            CFlock.setCAnimatParameters( Color.red, redAnimatSpeed, redAnimatMaxTheta );
        }
        else if ( ev.target == detectScrollbar ) {
            detectDistance = detectScrollbar.getValue();
            
            CAnimat.DetectionRange = detectDistance;
            
            CFlock.DetectionRange = detectDistance;
            
            if ( detectDistance < separateDistance ) {
                separateDistance = detectDistance;
                
                separateScrollbar.setValues( separateDistance, 1, 0, MAXIMUM_DISTANCE );
                CAnimat.SeparationRange = separateDistance;
                
                CFlock.SeparationRange = separateDistance;
            }
        }
        else if (ev.target == separateScrollbar) {
            separateDistance = separateScrollbar.getValue();
           
            CAnimat.SeparationRange = separateDistance;
            
            CFlock.SeparationRange = separateDistance;
            
            if ( detectDistance <separateDistance ) {
                detectDistance = separateDistance;
                detectScrollbar.setValues(detectDistance, 1, 0, MAXIMUM_DISTANCE );
                
                CAnimat.DetectionRange = detectDistance;
                
                CFlock.DetectionRange = detectDistance;
            }
        }
        else if ( ev.target == resetButton ) {
            resetValues();
        }
        else if ( ev.target == showRangesCheckbox ) {
            CAnimat.showRanges = showRangesCheckbox.getState();
        }
        else {
            return super.handleEvent(ev); // pass on unprocessed events
        }
        return true;
    }
    
    /**
     * Adds the slider and other controls to the panel.
     */
    public void addControls() {
        
        controls.setLayout( new GridLayout( 6, 4, 5, 5 ) );
        controls.setBackground( new Color( 224, 224, 224 ) );
        
        controlsLabel.setAlignment( Label.CENTER );
        greenLabel.setAlignment( Label.CENTER );
        blueLabel.setAlignment( Label.CENTER );
        
        redLabel.setAlignment( Label.CENTER );
        numberCAnimatsLabel.setAlignment( Label.RIGHT );
        speedLabel.setAlignment( Label.RIGHT );
        
        turnLabel.setAlignment( Label.RIGHT );

        detectLabel.setAlignment( Label.RIGHT );
        
        separateLabel.setAlignment( Label.RIGHT );

        controls.add( controlsLabel );
        controls.add( greenLabel );
        controls.add( blueLabel );
        controls.add( redLabel );
        
        controls.add( numberCAnimatsLabel );
        controls.add( greenMunberAnimatsScrollbar );
        controls.add( blueNumberAnimatsScrollbar );
        controls.add( redNumberAnimatsScrollbar );
        controls.add( speedLabel );
        controls.add( greenSpeedScrollbar );
        controls.add( blueSpeedScrollbar );
        
        controls.add( redSpeedScrollbar );
        controls.add( turnLabel );
        controls.add( greenMaxThetaScrollbar );
        controls.add( blueMaxThetaScrollbar );
        controls.add( redMaxThetaScrollbar );
        
        controls.add( resetButton );
        controls.add( detectLabel );
        controls.add( detectScrollbar );

        controls.add( showRangesCheckbox );
        
        controls.add( separateLabel );
        controls.add( separateScrollbar );

    }

    /**
     * Sets the values of the slider controls on the panel.
     */
    public void setControlValues() {
        greenMunberAnimatsScrollbar.setValues( numberOfGreenAnimats, 1, 0, MAXIMUM_ANIMATS );
        blueNumberAnimatsScrollbar.setValues( numberOfBlueAnimats, 1, 0, MAXIMUM_ANIMATS );
        
        redNumberAnimatsScrollbar.setValues( numberOfRedAnimats, 1, 0, MAXIMUM_ANIMATS );
        greenSpeedScrollbar.setValues( redAnimatSpeed, 1, 0, MAXIMUM_SPEED );
        blueSpeedScrollbar.setValues( blueAnimatSpeed, 1, 0, MAXIMUM_SPEED );
       
        redSpeedScrollbar.setValues( redAnimatSpeed, 1, 0, MAXIMUM_SPEED );
        greenMaxThetaScrollbar.setValues( greenAnimatMaxTheta, 1, 0, 180 );
        blueMaxThetaScrollbar.setValues( blueAnimatMaxTheta, 1, 0, 180 );
        
        redMaxThetaScrollbar.setValues( redAnimatMaxTheta, 1, 0, 180 );
        detectScrollbar.setValues( detectDistance, 1, 0, MAXIMUM_DISTANCE );
        separateScrollbar.setValues( separateDistance, 1, 0, MAXIMUM_DISTANCE );

    }
    
    public String getAppletInfo() {
	return "CFlocking Simulator by Michael LaLena";
    }
    
    class SimulatorCanvas extends Canvas {

        // Image Object for the canvas
        Image canvasImage;
        
        // Graphics Object for the canvas
        Graphics canvasGraphics;
    
        // Reference back to the main applet, so we can get to the CFlock.
        CSimulator simulator;
        
        /**
         * This is the java applet update function.
         * It calls paint directly. No need to reset anything first.
         */
        public synchronized void update( Graphics g ) {
            paint( g );
        }

        /**
         * This is the java applet paint function.
         * It draws the CAnimats on the map.
         */
        public synchronized void paint( Graphics g ) {
            if ( canvas.getWidth() == 0 ) {
                return;
            }
            if (( canvasImage == null ) ||
                        ( canvasImage.getWidth(this) != canvas.getWidth() ) ||
                        ( canvasImage.getHeight(this) != canvas.getHeight() )) {
                if ( canvasGraphics != null ) {
                    canvasGraphics.dispose();
                }
                canvasImage = createImage(canvas.getWidth(), canvas.getHeight());
                canvasGraphics = canvasImage.getGraphics();
            }

            if ( canvasGraphics != null ) {
                canvasGraphics.setColor( Color.white );
                
                canvasGraphics.fillRect( 0, 0, canvas.getWidth(), canvas.getHeight() );
                simulator.flock.draw( canvasGraphics );
                
                canvas.getGraphics().drawImage(canvasImage, 0, 0, this);
            }
        }
    }
}