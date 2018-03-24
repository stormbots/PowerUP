package org.usfirst.frc.team2811.robot.Auto;

import org.usfirst.frc.team2811.robot.Elevator.ElevatorPosition;
import org.usfirst.frc.team2811.robot.Chassis;
import org.usfirst.frc.team2811.robot.Motion345;
import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.TinyTimer;

import edu.wpi.first.wpilibj.Preferences;

/**
 * Example Auto command doing a basic thing. 
 * Copy and rename this file to write a new auto command
 * @author stormbots
 *
 */
public class SideScale extends AutoSequence {

	TinyTimer timer = new TinyTimer();
	double left1;
	double right1;
	double left2;
	double right2;
	double backward = -24;

	public SideScale(boolean isLeft) {
		double inside1 = 265;
		double outside1 = 265;
		double inside2 = 0;
		double outside2 = 24;
		
		if(isLeft) {
			left1 = outside1;
			right1 = inside1;
			left2 = outside2;
			right2 = inside2;
		}
		else {
			left1 = inside1;
			right1 = outside1;
			left2 = inside2;
			right2 = outside2;
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
			Robot.drive.setProfile(left1, right1, 8000);
			//Robot.elevator.setPos(switch height);
		}
		if(timer.atTime(3500)) {
			Robot.elevator.setPos(ElevatorPosition.SCALEHIGH);
		}
		if(timer.atTime(8000)){
			Robot.drive.setProfile(left2, right2, 2000);
		}
		if(timer.atTime(10000)) {
			Robot.intake.ejectCube();
		}
		if(timer.atTime(10500)) {
			Robot.drive.setProfile(backward, backward, 2000);
		}
		if(timer.atTime(11000)) {
			Robot.intake.stopMotor();
			Robot.elevator.setPos(ElevatorPosition.SWITCH);
		}
		if(timer.atTime(12500)) {
			Robot.drive.setMode(Chassis.Mode.ARCADE);
		}
		
		timer.update();
	}
}
