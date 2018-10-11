package org.usfirst.frc.team2811.robot.Auto;

import org.usfirst.frc.team2811.robot.Chassis;
import org.usfirst.frc.team2811.robot.Motion345;
import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.TinyTimer;
import org.usfirst.frc.team2811.robot.Elevator.ElevatorPosition;

/**
 * Example Auto command doing a basic thing. 
 * Copy and rename this file to write a new auto command
 * @author stormbots
 *
 */
public class SideSwitch extends AutoSequence {

	TinyTimer timer = new TinyTimer();
	double left1;
	double right1;
	double left2;
	double right2;
	double left3;
	double right3;
	double backward = -24;
	
	long ts = 1;
	
	long t0 = 0000;
	long t1 = 5000*ts;
	long t2 = 1000*ts;
	long t3 = 1000*ts;
	long t4 = 1000*ts;
	
	public SideSwitch(boolean isLeft) {
		double inside1 = 124;
		double outside1 = 124;
		double inside2 = 6;
		double outside2 = 59;
		double inside3 = 18;
		double outside3 = 18;
		
		if(isLeft) {
			left1 = outside1;
			right1 = inside1;
			left2 = outside2;
			right2 = inside2;
			left3 = outside3;
			right3 = inside3;
		}
		else {
			left1 = inside1;
			right1 = outside1;
			left2 = inside2;
			right2 = outside2;
			left3 = inside3;
			right3 = outside3;
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
		if(timer.atTime(t0)){
			Robot.drive.setProfile(left1, right1, t1);
			Robot.elevator.setPos(ElevatorPosition.SWITCH);
		}
		if(timer.atTime(t1)) {
			Robot.drive.setProfile(left2, right2, t2);
		}
		if(timer.atTime(t2+t1)) {
			Robot.drive.setProfile(left3, right3, t3);
		}
		if(timer.atTime(t3+t2+t1)) {
			Robot.intake.ejectCube();
			Robot.drive.setProfile(backward, backward, t4);
		}
		if(timer.atTime(t4+t3+t2+t1)) {
			Robot.intake.stopMotor();
			Robot.elevator.setPos(ElevatorPosition.FLOOR);
		}
		if(timer.atTime(14900)) {
			Robot.drive.setMode(Chassis.Mode.ARCADE);
		}
		
		timer.update();

	}
}
