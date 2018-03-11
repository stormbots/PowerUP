package org.usfirst.frc.team2811.robot;

import edu.wpi.first.wpilibj.Timer;

public class TinyTimer {
	
	public double lasttime = 0;
	public double presenttime = 0;
	Timer timer = new Timer();
	public double creationtime = 0;
	
	public TinyTimer(){
		creationtime = Timer.getFPGATimestamp();
	}
	
	
	public void update() {
		lasttime=presenttime;
		presenttime = Timer.getFPGATimestamp() - creationtime;
//		System.out.printf("newUpdate: last(%f) present(%f)\n", lasttime,presenttime);
	}
		
	public double getSeconds() {
		return presenttime;
	}
	
	public double getMillis() {
		return presenttime*1000;
	}
	
	/** Returns true exactly once as the clock passes the provided time
	 * @param time, in milliseconds
	 * @return
	 */
	public boolean atTime(long timems) {
		double time = timems/1000.0;
//		System.out.printf("Timerstuff: last(%f) present(%f)\n", lasttime,presenttime);
		return time >= lasttime && time < presenttime;
	}


	public void reset() {
		creationtime = Timer.getFPGATimestamp();		
	}
	
		
}
