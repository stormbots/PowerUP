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
	
	long t0 = 0000;
	long t1 = 1000; // turn
	long t2 = 3000; // to scale
	long t3 = 2000; // back away from scale
	long t4 = 750; // turn to cube pile
	long t5 = 750; // forward to cube pile
	long t6 = 750; // back from cube pile
	long t7 = 750; // turn to the switch
	long t8 = 2000; // forward to the switch
	long t9 = 1500; // back away from the switch
	long t10 = 750; // turn to the cube pile
	long t11 = 750; // forward to grab cube
	long t12 = 750; // back from cubes
	long t13 = 750; // turn to the exchange
	long t14 = 3000; // drive forward towards the exchange
	
	public CenterVer3(boolean toLeft) {
		if(toLeft) {			
			// 1 - turn to face switch
			left1 = 0;
			right1 = 14;

			// 2 - drive to the switch
			left2 = 104;
			right2 = 104;
	
			// 3 - back away
			left3 = -42;
			right3 = -42;
	
			// 4 - turn to cube pile
			left4 = 20;
			right4 = -20;

			// 5 - forward to grab cube
			left5 = 18;
			right5 = 18;

			// 6 - back away from cube pile
			left6 = -18;
			right6 = -18;

			// 7 - turn to switch
			left7 = -20;
			right7 = 20;

			// 8 - drive to the switch
			left8 = 42;
			right8 = 42;

			// 9 - back away
			left9 = -28;
			right9 = -28;

			// 10 - turn to cube pile
			left10 = 20;
			right10 = -20;

			// 11 - forward to grab cube
			left11 = 15;
			right11 = 15;

			// 12 - back away from cube pile
			left12 = -15;
			right12 = -15;
			
			// 13 - turn to exchange
			left13 = 0; // 19
			right13 = 0; // -19

			// 14 - forward to deploy the cube into the exchange
			left14 = 0; // 40
			right14 = 0; // 40
		}
		
		else {
			// 1 - turn to face switch
			left1 = 17;
			right1 = 0;

			// 2 - drive to the switch
			left2 = 104-5;
			right2 = 104-5;
	
			// 3 - back away
			left3 = -42;
			right3 = -42;
	
			// 4 - turn to cube pile
			left4 = -20;
			right4 = 20;

			// 5 - forward to grab cube
			left5 = 18;
			right5 = 18;

			// 6 - back away from cube pile
			left6 = -18;
			right6 = -18;

			// 7 - turn to switch
			left7 = 20;
			right7 = -20;

			// 8 - drive to the switch
			left8 = 42;
			right8 = 42;

			// 9 - back away
			left9 = -28;
			right9 = -28;

			// 10 - turn to cube pile
			left10 = -20;
			right10 = 20;

			// 11 - forward to grab cube
			left11 = 15;
			right11 = 15;

			// 12 - back away from cube pile
			left12 = -15;
			right12 = -15;
			
			// 13 - turn to exchange
			left13 = 0; // -19
			right13 = 0; // 19

			// 14 - forward to deploy the cube into the exchange
			left14 = 0; // 40
			right14 = 0; // 40
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
			Robot.intake.grabCube();
			Robot.drive.setProfile(left5, right5, t5);
		}
		if(timer.atTime(t5+t4+t3+t2+t1)) { // step 6
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
		if(timer.atTime(t8+t7+t6+t5+t4+t3+t2+t1)){ // step 9
			Robot.intake.ejectCube();
			Robot.drive.setProfile(left9, right9, t9);
		}
		if(timer.atTime(t9+t8+t7+t6+t5+t4+t3+t2+t1)) { // step 10
			Robot.elevator.setPos(ElevatorPosition.FLOOR);
			Robot.intake.stopMotor();
			Robot.intake.squeezeOpen(true);
			Robot.drive.setProfile(left10, right10, t10);
		}
		if(timer.atTime(t10+t9+t8+t7+t6+t5+t4+t3+t2+t1)) { // step 11
			Robot.intake.grabCube();
			Robot.drive.setProfile(left11, right11, t11);
		}
		if(timer.atTime(t11+t10+t9+t8+t7+t6+t5+t4+t3+t2+t1)) { // step 12
			Robot.intake.squeezeOpen(false);
			Robot.drive.setProfile(left12, right12, t12);
		}
		if(timer.atTime(t12+t11+t10+t9+t8+t7+t6+t5+t4+t3+t2+t1)) { // step 13
			Robot.intake.stopMotor();
			Robot.drive.setProfile(left13, right13, t13);
		}
		if(timer.atTime(t13+t12+t11+t10+t9+t8+t7+t6+t5+t4+t3+t2+t1)) { // step 14
			Robot.drive.setProfile(left14, right14, t14);
		}
		
		if(timer.atTime(14900)) {
			Robot.drive.setMode(Chassis.Mode.ARCADE);
		}

		timer.update();
	}
}
