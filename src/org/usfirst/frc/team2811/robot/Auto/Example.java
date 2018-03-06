package org.usfirst.frc.team2811.robot.Auto;

import org.usfirst.frc.team2811.robot.CXTIMER;
import org.usfirst.frc.team2811.robot.Motion345;
import org.usfirst.frc.team2811.robot.Robot;

/**
 * Example Auto command doing a basic thing. 
 * Copy and rename this file to write a new auto command
 * @author stormbots
 *
 */
public class Example extends AutoSequence {

	CXTIMER timer = new CXTIMER();
	
	public Example() {
		//Add any code here
	}
	
	/**
	 * Execute a sequence of steps 
	 * At each time interval, configure the appropriate subsystem so it's update() will perform the desired actions. 
	 * 
	 * For now, leave autos on a time-based scale to make migration easier. We will worry about event-driven autos later
	 * eg, if(have-cube) then intake.close() then elevator.move_pos(switch) then drive.setProfile(24,24,1000)
	 */
	public void run() {
		
		//Example: Approximate movements to drop on the switch then back up
		if(timer.atTime(0)){
			Robot.drive.setProfile(48,24,3000);
			//Robot.elevator.setPos(switch height);
		}
		if(timer.atTime(3000)){
			Robot.drive.setProfile(24,48,3000);
			// note, this move lasts for 3 seconds
			// but the next action starts at 4 seconds!
		}
		if(timer.atTime(4000)){
			//Robot.elevator.setPos(like, slightly higher or something);
		}
		if(timer.atTime(6000)){
			Robot.drive.setProfile(10,10,1000);
		}
		if(timer.atTime(7000)){
			//Robot.intake.open()
		}
		if(timer.atTime(8000)){
			Robot.drive.setProfile(-24,-24,1000);
		}
		if(timer.atTime(9000)){
			//Robot.intake.close()
			//Robot.elevator.setPos(floor)
		}
	}
	
	
}
