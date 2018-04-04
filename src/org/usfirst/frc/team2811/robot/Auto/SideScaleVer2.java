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
public class SideScaleVer2 extends AutoSequence {

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
	double left6;
	double right6;
	double left7;
	double right7;
	double left8;
	double right8;
	double left9;
	double right9;
	
	long t0 = 0000;
	long raiseScale = 3000;
	long t1 = 7500;
	long t2 = 750;
	long t3 = 500;
	long t4 = 500;
	long t5 = 750;
	long t6 = 1500;
	long t7 = 500;
	long t8 = 1500;
	long t9 = 1500;

	public SideScaleVer2(boolean isLeft) {
		double inside1 = 270;
		double outside1 = 270;
		double inside2 = -20;
		double outside2 = 20;
		double inside3 = 15;
		double outside3 = 15;
		double inside4 = -15;
		double outside4 = -15;
		double inside5 = -20;
		double outside5 = 20;
		double inside6 = 50;
		double outside6 = 50;
		double inside7 = 0;
		double outside7 = 7;
		double inside8 = 80;
		double outside8 = 80;
		double inside9 = -80;
		double outside9 = -80;
		
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
			left6 = outside6;
			right6 = inside6;
			left7 = outside7;
			right7 = inside7;
			left8 = outside8;
			right8 = inside8;
			left9 = outside9;
			right9 = inside9;
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
			left6 = inside6;
			right6 = outside6;
			left7 = inside7;
			right7 = outside7;
			left8 = inside8;
			right8 = outside8;
			left9 = inside9;
			right9 = outside9;
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
		if(timer.atTime(raiseScale)) {
			Robot.elevator.setPos(ElevatorPosition.SCALEHIGH);
		}
		if(timer.atTime(t1)) {
			Robot.drive.setProfile(left2, right2, t2);
		}
		if(timer.atTime(t2+t1)){
			Robot.drive.setProfile(left3, right3, t3);
		}
		if(timer.atTime(t3+t2+t1)) {
			Robot.intake.ejectCube();
			Robot.drive.setProfile(left4, right4, t4);
		}
		if(timer.atTime(t4+t3+t2+t1)) {
			Robot.intake.stopMotor();
			Robot.intake.squeezeOpen(true);
			Robot.elevator.setPos(ElevatorPosition.FLOOR);
			Robot.drive.setProfile(left5, right5, t5);
		}
		if(timer.atTime(t5+t4+t3+t2+t1)) {
			Robot.drive.setProfile(left6, right6, t6);
		}
		if(timer.atTime(t6+t5+t4+t3+t2+t1)) {
			Robot.drive.setProfile(left7, right7, t7);
		}
		if(timer.atTime(t7+t6+t5+t4+t3+t2+t1)) {
			Robot.drive.setProfile(left8, right8, t8);
		}
		if(timer.atTime(t8+t7+t6+t5+t4+t3+t2+t1)) {
			Robot.intake.squeezeOpen(false);
			Robot.intake.grabCube();
			Robot.drive.setProfile(left9, right9, t9);
		}
		
		if(timer.atTime(14900)) {
			Robot.intake.stopMotor();
			Robot.drive.setMode(Chassis.Mode.ARCADE);
		}
		
		timer.update();
	}
}
