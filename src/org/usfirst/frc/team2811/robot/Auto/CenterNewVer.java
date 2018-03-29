package org.usfirst.frc.team2811.robot.Auto;

import org.usfirst.frc.team2811.robot.Elevator.ElevatorPosition;
import org.usfirst.frc.team2811.robot.Chassis;
import org.usfirst.frc.team2811.robot.Motion345;
import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.TinyTimer;

/**
 * Example Auto command doing a basic thing. 
 * Copy and rename this file to write a new auto command
 * @author stormbots
 *
 */
public class CenterNewVer extends AutoSequence {

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
	double backward = -24;
	
	double t0;
	double t1;
	double t2;
	double t3;
	double t4;
	double t5;
	double t6;
	double t7;
	double t8;
	double t9;
	
	public CenterNewVer(boolean toLeft) {
		if(toLeft) {
			left1 = 29.0597;
			right1 = 68.3296;
			left2 = 68.3296;																																																											;
			right2 = 29.0597;
			left3 = 37;
			right3 = 37;
		}
		
		
		/*
		 * robot center is 4 inches toward the switch, 157.69 from the wall
		 * switch outer edge is 85.25 inches from the wall, use 80 inches from the wall
		 * we need a 77.96 inch lateral distance
		 * over a 101 inch vertical distance
		 * 6 inches forward + turn
		 * tan(0) = 77.96/101 = angle
		 * 37.6639
		 * 
		 * ______
		 * |    /
		 * |   /
		 * |  /
		 * | /
		 * |/
		 */
		else {
			left1 = 17;
			right1 = 0;
			left2 = 102;
			right2 = 102;
			left3 = -32;
			right3 = -32;
			left4 = -20;
			right4 = 20;
			left5 = 10;
			right5 = 10;
			left6 = -10;
			right6 = -10;
			left7 = 20;
			right7 = -20;
			left8 = 32;
			right8 = 32;
			left9 = 0;
			right9 = 0;
			
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
		
		long t0 = 0000;
		long t1 = 1000;
		long t2 = 3000;
		long t3 = 2000;
		long t4 = 1000;
		long t5 = 750;
		long t6 = 750;
		long t7 = 1000;
		long t8 = 2000;
		long t9;
		
		//Example: Approximate movements to drop on the switch then back up
		if(timer.atTime(t0)){
			Robot.drive.setProfile(left1, right1, t1);
			Robot.elevator.setPos(ElevatorPosition.SWITCH);
		}
		if(timer.atTime(t1)){
			Robot.drive.setProfile(left2, right2, t2);
		}
		if(timer.atTime(t2+t1)){
			Robot.intake.ejectCube();
			Robot.drive.setProfile(left3, right3, t3);
		}
		if(timer.atTime(t3+t2+t1)) {
			Robot.elevator.setPos(ElevatorPosition.FLOOR);
			Robot.intake.stopMotor();
			Robot.intake.squeezeOpen(true);
			Robot.drive.setProfile(left4, right4, t4);
		}
		if(timer.atTime(t4+t3+t2+t1)) {
			Robot.intake.grabCube();
			Robot.drive.setProfile(left5, right5, t5);
		}
		if(timer.atTime(t5+t4+t3+t2+t1)) {
			Robot.intake.grabCube();
			Robot.intake.squeezeOpen(false);
			Robot.drive.setProfile(left6, right6, t6);
		}
		if(timer.atTime(t6+t5+t4+t3+t2+t1)) {
			Robot.intake.stopMotor();
			Robot.elevator.setPos(ElevatorPosition.SWITCH);
			Robot.drive.setProfile(left7, right7, t7);
		}
		if(timer.atTime(t7+t6+t5+t4+t3+t2+t1)) {
			Robot.drive.setProfile(left8, right8, t8);
		}
		if(timer.atTime(t8+t7+t6+t5+t4+t3+t2+t1)) {
			Robot.intake.ejectCube();
			Robot.elevator.setPos(ElevatorPosition.SWITCH);
		}
		if(timer.atTime(1000+t8+t7+t6+t5+t4+t3+t2+t1)) {
			Robot.intake.stopMotor();
		}
//		if(timer.atTime(14800)) {
//			Robot.drive.setMode(Chassis.Mode.ARCADE);
//		}

		timer.update();
	}
}
