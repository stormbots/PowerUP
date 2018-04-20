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
public class SideScaleVer3 extends AutoSequence {

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
	
	long t0 = 0000;
	long raiseScale = 4000; // 3.5 seconds to raise the scale
	long t1 = 7500; // 7.5 seconds to move to scale
	long t2 = 1000; // pivot towards scale
	long t3 = 750; // pivot towards switch cubes
	long t4 = 3250; // drive to cube
	long elevatorDelay = 1500; // 0.75 seconds after drive started
//	long t5 = 2750; // drive away from cube
//	long t6 = 750; // turn to scale
//	long t7 = 750; // turn away from scale
//	long t8 = 3000; // drive towards 3rd cube

	public SideScaleVer3(boolean isLeft) {
		// 1 - move forward
		double inside1 = 270-6;
		double outside1 = 270-6;
		
		// 2 - small rotation about the center
		double inside2 = -7;
		double outside2 = 7;
		
		// 3 - something less than 90 degree rotation about the center
		double inside3 = -60;
		double outside3 = 60;
		
		// 4 - forward to the 2nd cube
		double inside4 = -35; // 80 to go forward
		double outside4 = -35; // 80 to go forward
		
		// 5 - back up to scale
		double inside5 = -90;
		double outside5 = -90;
		
		// 6 - turn back to the scale
		double inside6 = 16;
		double outside6 = -16;
		
		// 7 - turn away from the scale
		double inside7 = -16;
		double outside7 = 16;
		
		// 8 - drive forward to 3rd cube
		double inside8 = 80;
		double outside8 = 80;
		
		if(isLeft) {
			left1 = outside1+24;
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
			Robot.intake.ejectCube();
			Robot.drive.setProfile(left3, right3, t3);
		}
		if(timer.atTime(t3+t2+t1)) {
			Robot.intake.stopMotor();
			Robot.intake.squeezeOpen(true);
			Robot.elevator.setPos(ElevatorPosition.FLOOR);
			Robot.drive.setProfile(left4, right4, t4);
		}
//		if(timer.atTime(t4+t3+t2+t1)) {
//			Robot.intake.grabCube();
//			Robot.intake.squeezeOpen(false);
//			Robot.drive.setProfile(left5, right5, t5);
//		}
//		if(timer.atTime(elevatorDelay+t4+t3+t2+t1)) {
//			Robot.elevator.setPos(ElevatorPosition.SCALEHIGH);
//		}
//		if(timer.atTime(t5+t4+t3+t2+t1)) {
//			Robot.drive.setProfile(left6, right6, t6);
//		}
//		if(timer.atTime(t6+t5+t4+t3+t2+t1)) {
//			Robot.intake.ejectCube();
//			Robot.drive.setProfile(left7, right7, t7);
//		}
//		if(timer.atTime(t7+t6+t5+t4+t3+t2+t1)) {
//			Robot.intake.stopMotor();
//			Robot.intake.squeezeOpen(true);
//			Robot.elevator.setPos(ElevatorPosition.FLOOR);
//			Robot.drive.setProfile(left8, right8, t8);
//		}

		if(timer.atTime(14900)) {
			Robot.intake.stopMotor();
			Robot.drive.setMode(Chassis.Mode.ARCADE);
		}
		
		timer.update();
	}
}
