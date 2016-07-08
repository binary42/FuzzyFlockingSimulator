# FuzzyFlockingSimulator
Implementation of flocking algorithms, including fuzzy model animats. Java and Eclipse centric using FCL and JFuzzyLogic library

This is an implementation of Bajec's thesis work in fuzzy simulation of flocking, 2D. "Fuzzy Model for a Computer Simulation of Bird Flocking" Iztok Lebar Bajec - University of Ljubljana

I've used Micheal Lalena's Applet simulator (many great thanks are given as this greatly decreased my dev time), that used Reynolds 3 rule impl of flocking. Found here - http://www.lalena.com/AI/Flock/Flock.aspx and Reynolds can be referenced from Bajec's paper.

I've included the original simulator code from Lalena in com.boids.lalena. My hacked up version is in com.fuzzy.simulator. The fuzzy control bits are in com.fuzzy.controller.

Again, credit Lalena, Bajec, Reynolds and Heppner. I just wrote out the fuzzy bits into the sim. 

##Current Status
---Still in Dev. Not Functioning---