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
	double left3;
	double right3;
	double backward = -16;
	
//	long t0 = 0000;
//	long t1 = 7500;
//	long t2 = 1000;
//	long t3 = 2000;

	public SideScale(boolean isLeft) {
		double inside1 = 308;
		double outside1 = 308;
		double inside2 = -20;
		double outside2 = 20;
		double inside3 = 6;
		double outside3 = 6;
		
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
		if(timer.atTime(0000)){
			Robot.drive.setProfile(left1, right1, 7500);
			//Robot.elevator.setPos(switch height);
		}
		if(timer.atTime(3500)) {
			Robot.elevator.setPos(ElevatorPosition.SCALEHIGH);
		}
		if(timer.atTime(7500)){
			Robot.drive.setProfile(left2, right2, 1000);
		}
		if(timer.atTime(8500)){
			Robot.drive.setProfile(left3, right3, 2000);
		}
		if(timer.atTime(10000)) {
			Robot.drive.setProfile(0, 0, 2000);
			Robot.intake.ejectCube();
		}
		if(timer.atTime(10500)) {
			Robot.drive.setProfile(backward, backward, 2000);
		}
		if(timer.atTime(12500)) {
			Robot.intake.stopMotor();
			Robot.elevator.setPos(ElevatorPosition.FLOOR);
		}
		if(timer.atTime(14900)) {
			Robot.drive.setMode(Chassis.Mode.ARCADE);
		}
		
		timer.update();
	}
}
