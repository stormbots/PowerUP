package org.usfirst.frc.team2811.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Utility;


/**
 * the purpose of this class is to provide an accurate way to implement many timers on a scanning based system
 * the timer class has a static set of variables that represent the time since last scan in milliseconds
 * these are used to accumulate time for each active timer instance in the system without have many asynchronous interrupts
 * These timers can be stopped, started and reset by the code
 */
public class CXTIMER {
	
	
	// this are static variables that are shared by all instances of the timer
	private static long mylasttime=(long) 0;  // the last count from the system timer
	private static long mytimeticks=(long) 0; // Number of time ticks to add this pass though the main loop
	private static long mytimeticksrm=(long) 0; // the remainder numbers of ticks 
	
	private long mycurtime=(long)0;  // the current number of ticks this timer has accumulated

	public double lasttime = 0;
	public double presenttime = 0;
	
	public CXTIMER(){
		mycurtime=0;
	}
	
	
	/**
	 * Updates all timers, keeping track of time in milliseconds. Will also track fractional milliseconds, and add them as 
	 * needed for long term accuracy.  
	 * 
	 * This function should be called once per control loop, and will update all timer instances. 
	 * 
	 * The system clocks can be microseconds or nanoseconds the truncation of time to milliseconds can create a long 
	 * term lost of time by keeping the truncated number of raw ticks as a remainder and adding in whole millisecond 
	 * ticks as needed will insure long term accuracy Most timers built this way would not care about this 
	 * small loss of time but if this is used to build long term timers over days and weeks the time loss can 
	 * become significant
	 */
	public void Update() {
		long sf=1000; // take it from microseconds to milliseconds
		long maxsf=0x7fffffff; // mask off any sign bit because of actual unsigned count
		long curt=Utility.getFPGATime();  // gets number of microsecond ticks in the current counter
		
		// test code
		//long sf=1;
		//long curt=System.currentTimeMillis();
		
		curt=curt&maxsf; // fix for signed VS unsigned
		
		
		if(curt>=mylasttime) 
			mytimeticks=curt-mylasttime;  // not roll over just normal
		else
			mytimeticks=maxsf-(mylasttime-curt)+1; // handle roll over case
		// the roll over case is designed to keep with math limits and use normal operands
		// it would be simpler to do a two complement but with the uncertainty about the 
		// compilers actual methods a pure operator methods was used. 
		
		// because of the scaling a simple integer division will always leave some
		// number of ticks out and overtime will cause the timer to lose accuracy
		// keeping the sum of ticks from the remainder allows for long term accuracy
		// 
		mytimeticksrm+=(mytimeticks%sf); // add any remainder to remainder
		// Now get the whole number part of the ticks
		mytimeticks=mytimeticks/sf;  // get milliseconds from microseconds clock
		// if enough fractional ticks have accumulated increase the tick count by 1
		while (mytimeticksrm>sf) {mytimeticks++;mytimeticksrm-=sf;} // add odd time 
		// keep this count to use as the last count in then next call
		mylasttime=curt;
	}
	
	
	/**
	 * @return accumulated time in milliseconds counts
	 */
	public long getTime() {
		return mycurtime; // returns 
	}

	/**
	 * @return the number of millisecond ticks since last update
	 */
	public long getTicks() {
		return mytimeticks;
	}
	
	/**
	 * @return time in seconds
	 */
	public double getTimeSec() {
		return (double)mycurtime/1000.0; 
	}
	
	//  
	// the boolean variable allows the timer to be paused and reset
	
	/**
	 * Check to see if a running timer has exceeded a time limit
	 * 
	 * @param tmon if true, reset timer
	 * @param tmlimit 
	 * @return true if timer running time is less than tmlimit
	 */
	public boolean ckTime(boolean tmon, long tmlimit) {
		boolean tmexp=false;  // set up to return no running or not expired
		
		if(!tmon) {    // don't run the timer set accumulated time to 0
			mycurtime=0;
		    
		}
		else {
		mycurtime+=mytimeticks;  // add time ticks since last time
			if(mycurtime>=tmlimit) { // check for time limit
				mycurtime=tmlimit; // if reached time limit set the limit and return true
				tmexp=true;
			}
		}
		return tmexp;
	}
	
	/**
	 * Resets the timer to zero
	 */
	public void reset() {
		mycurtime=0;	
	}

		
}
