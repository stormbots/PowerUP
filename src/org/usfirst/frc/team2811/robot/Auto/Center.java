package org.usfirst.frc.team2811.robot.Auto;

import org.usfirst.frc.team2811.robot.CXTIMER;
import org.usfirst.frc.team2811.robot.Elevator.ElevatorPosition;
import org.usfirst.frc.team2811.robot.Motion345;
import org.usfirst.frc.team2811.robot.Robot;

/**
 * Example Auto command doing a basic thing. 
 * Copy and rename this file to write a new auto command
 * @author stormbots
 *
 */
public class Center extends AutoSequence {

	CXTIMER timer = new CXTIMER();
	double left1;
	double right1;
	double left2;
	double right2;
	double left3;
	double right3;
	
	public Center(boolean toLeft) {
		if(toLeft) {
			left1 = 29.0597;
			right1 = 68.3296;
			left2 = 68.3296;																																																											;
			right2 = 29.0597;
			left3 = 37;
			right3 = 37;
		}
		else {
			left1 = 81.4712;
			right1 = 41.5013;
			left2 = 41.5013;
			right2 = 81.4712;
			left3 = 2;
			right3 = 2;
		}
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
		if(timer.atTime(0000)){
			Robot.drive.setProfile(left1, right1, 2000);
		}
		if(timer.atTime(2000)){
			Robot.drive.setProfile(left2, right2, 2000);
		}
		if(timer.atTime(4000)){
			Robot.drive.setProfile(left3, right3, 1000);
			//Robot.elevator.setPos(switch height);
		}
		if(timer.atTime(5000)) {
			Robot.intake.ejectCube();
		}
		if(timer.atTime(6000)) {
			Robot.intake.stopMotor();
		}
	}
}
