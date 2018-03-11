package org.usfirst.frc.team2811.robot.Auto;

import org.usfirst.frc.team2811.robot.CXTIMER;
import org.usfirst.frc.team2811.robot.Motion345;
import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.TinyTimer;

/**
 * Base Auto class so that it can be extended and used as a generic type 
 * @author stormbots
 *
 */
public abstract class AutoSequence {

	TinyTimer timer = new TinyTimer();
	
	public AutoSequence() {
		//Add any code here
	}
	
	/**
	 * Execute a sequence of steps 
	 * At each time interval, configure the appropriate subsystem so it's update() will perform the desired actions. 
	 * 
	 * For now, leave autos on a time-based scale to make migration easier. We will worry about event-driven autos later
	 * eg, if(have-cube) then intake.close() then elevator.move_pos(switch) then drive.setProfile(24,24,1000)
	 */
	public abstract void run();
}
