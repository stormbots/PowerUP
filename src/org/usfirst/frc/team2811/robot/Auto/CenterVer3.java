package org.usfirst.frc.team2811.robot.Auto;

import org.usfirst.frc.team2811.robot.Elevator.ElevatorPosition;
import org.usfirst.frc.team2811.robot.Chassis;
import org.usfirst.frc.team2811.robot.Elevator;
import org.usfirst.frc.team2811.robot.Motion345;
import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.TinyTimer;

/**
 * Example Auto command doing a basic thing. 
 * Copy and rename this file to write a new auto command
 * @author stormbots
 *
 */
public class CenterVer3 extends AutoSequence {

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
	double left10;
	double right10;
	double left11;
	double right11;
	double left12;
	double right12;
	double left13;
	double right13;
	double left14;
	double right14;
	double backward = -24;
	
	long t0 = 0000;
	long t1 = 500;
	long t2 = 3000;
	long t3 = 1500;
	long t4 = 500;
	long t5 = 500;
	long t6 = 500;
	long t7 = 500;
	long t8 = 1500;
	long t9 = 1500;
	long t10 = 500;
	long t11 = 500;
	long t12 = 500;
	long t13 = 500;
	long t14 = 1500;
	
	public CenterVer3(boolean toLeft) {
		if(toLeft) {			
			left1 = 0;
			right1 = 14;
			left2 = 102;
			right2 = 102;
			left3 = -32;
			right3 = -32;
			left4 = 20;
			right4 = -20;
			left5 = 10;
			right5 = 10;
			left6 = -10;
			right6 = -10;
			left7 = -20;
			right7 = 20;
			left8 = 32;
			right8 = 32;
			left9 = 0;
			right9 = 0;
			left10 = 0;
			right10 = 0;
			left11 = 0;
			right11 = 0;
			left12 = 0;
			right12 = 0;
		}
		else {
			// 1 - turn to face switch
			left1 = 17;
			right1 = 0;

			// 2 - drive to the switch
			left2 = 102;
			right2 = 102;

			// 3 - back away
			left3 = -32;
			right3 = -32;

			// 4 - turn to cube pile
			left4 = -20;
			right4 = 20;

			// 5 - forward to grab cube
			left5 = 10;
			right5 = 10;

			// 6 - back away from cube pile
			left6 = -10;
			right6 = -10;

			// 7 - turn to switch
			left7 = 20;
			right7 = -20;

			// 8 - drive to the switch
			left8 = 32;
			right8 = 32;

			// 9 - back away
			left9 = -32;
			right9 = -32;

			// 10 - turn to cube pile
			left10 = -20;
			right10 = 20;

			// 11 - forward to grab cube
			left11 = 10;
			right11 = 10;

			// 12 - back away from cube pile
			left12 = -10;
			right12 = -10;

			// 13 - turn to switch
			left13 = 20;
			right13 = -20;

			// 14 - drive to the switch
			left14 = 32;
			right14 = 32;
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
		if(timer.atTime(t0)){ // step 1
			Robot.drive.setProfile(left1, right1, t1);
			Robot.elevator.setPos(ElevatorPosition.SWITCH);
		}
		if(timer.atTime(t1)){ // step 2
			Robot.drive.setProfile(left2, right2, t2);
		}
		if(timer.atTime(t2+t1)){ // step 3
			Robot.intake.ejectCube();
			Robot.drive.setProfile(left3, right3, t3);
		}
		if(timer.atTime(t3+t2+t1)) { // step 4
			Robot.elevator.setPos(ElevatorPosition.FLOOR);
			Robot.intake.stopMotor();
			Robot.intake.squeezeOpen(true);
			Robot.drive.setProfile(left4, right4, t4);
		}
		if(timer.atTime(t4+t3+t2+t1)) { // step 5
			Robot.drive.setProfile(left5, right5, t5);
		}
		if(timer.atTime(t5+t4+t3+t2+t1)) { // step 6
			Robot.intake.grabCube();
			Robot.intake.squeezeOpen(false);
			Robot.drive.setProfile(left6, right6, t6);
		}
		if(timer.atTime(t6+t5+t4+t3+t2+t1)) { // step 7
			Robot.intake.stopMotor();
			Robot.elevator.setPos(ElevatorPosition.SWITCH);
			Robot.drive.setProfile(left7, right7, t7);
		}
		if(timer.atTime(t7+t6+t5+t4+t3+t2+t1)) { // step 8
			Robot.drive.setProfile(left8, right8, t8);
		}
		if(timer.atTime(t8+t7+t6+t5+t4+t3+t2+t1)) {
			Robot.intake.ejectCube();
			Robot.elevator.setPos(ElevatorPosition.SWITCH);
		}
		if(timer.atTime(1000+t8+t7+t6+t5+t4+t3+t2+t1)) {
			Robot.intake.stopMotor();
		}
		if(timer.atTime(14900)) {
			Robot.drive.setMode(Chassis.Mode.ARCADE);
		}

		timer.update();
	}
}
