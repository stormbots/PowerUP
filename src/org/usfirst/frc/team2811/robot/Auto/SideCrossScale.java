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
public class SideCrossScale extends AutoSequence {

	TinyTimer timer = new TinyTimer();
	double left1;
	double right1;
	double left2;
	double right2;
	double left3;
	double right3;
	double left4;
	double right4;
	double left5;
	double right5;
	double backward = -24;
	
	long t0 = 0000;
	long t1 = 7500;
	long t2 = 750;
	long t3 = 4000;
	long t4 = 750;
	long t5 = 1000;
	long t6 = 1000;



	public SideCrossScale(boolean isLeft) {
		double inside1 = 185;
		double outside1 = 185;
		double inside2 = 3.9270; // not full 90 degree turn
		double outside2 = 43.1969;
		double inside3 = 110; // distance to the end of the scale = 131.84; // this current is too far
		double outside3 = 110;
		double inside4 = 43.1969; // turn is really not tested
		double outside4 = 3.9270;
		double inside5 = 60; // increase time?  going too fast
		double outside5 = 60;
		
		if(isLeft) {
			left1 = outside1;
			right1 = inside1;
			left2 = outside2;
			right2 = inside2;
			left3 = outside3;
			right3 = inside3;
			left4 = outside4;
			right4 = inside4;
			left5 = outside5;
			right5 = inside5;
		}
		else {
			left1 = inside1;
			right1 = outside1;
			left2 = inside2;
			right2 = outside2;
			left3 = inside3;
			right3 = outside3;
			left4 = inside4;
			right4 = outside4;
			left5 = inside5;
			right5 = outside5;
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
			Robot.drive.setProfile(left1, right1, 7500);
		}
		if(timer.atTime(t1)){
			Robot.drive.setProfile(left2, right2, 750);
		}
		if(timer.atTime(t2+t1)) {
			Robot.drive.setProfile(left3, right3, 4000);
		}
		if(timer.atTime(t3+t2+t1)) {
			Robot.drive.setProfile(left4, right4, 750);
		}
		if(timer.atTime(t4+t3+t2+t1)) {
			Robot.drive.setProfile(left5, left5, 1000);
		}
		if(timer.atTime(t5+t4+t3+t2+t1)) {
			Robot.drive.setProfile(backward, backward, 1000);
		}
		
		if(timer.atTime(t0)) {
			Robot.elevator.setPos(ElevatorPosition.SWITCH);
		}
		if(timer.atTime(t4+t3+t2+t1)) {
			Robot.elevator.setPos(ElevatorPosition.SCALEHIGH);
		}
		if(timer.atTime(t6+t5+t4+t3+t2+t1)) {
			Robot.intake.stopMotor();
			Robot.elevator.setPos(ElevatorPosition.SWITCH);
		}
		if(timer.atTime(14800)) {
			Robot.drive.setMode(Chassis.Mode.ARCADE);
		}
		
		timer.update();
	}
}