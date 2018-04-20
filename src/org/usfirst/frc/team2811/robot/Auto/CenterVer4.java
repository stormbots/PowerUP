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
public class CenterVer4 extends AutoSequence {

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
	
	long ts = 1;
	
	long t0 = 0000;
	long t1 = 1000+500*ts; // turn							// 1000
	long t2 = 3000*ts; // drive to switch 					// 3000
	long t3 = 1000*ts; // turn to switch					// 1000
	long t4 = 2000*ts; // back away from switch				// 2000
	long t5 = 750+500*ts; // turn to cube pile				// 1000
	long t6 = 750*ts; // forward to cube pile				// 750
	long t7 = 750*ts; // back from cube pile				// 750
	long t8 = 750+500*ts; // turn to the switch				// 1000
	long t9 = 2000*ts; // forward to the switch				// 2000
	long t10 = 1500*ts; // back away from the switch		// 1500
	long t11 = 750+500*ts; // turn to the cube pile			// 1000
	long t12 = 750*ts; // forward to grab cube				// 750
	long t13 = 750*ts; // back from cubes					// 750
	
	public CenterVer4(boolean toLeft) {
		
		if(toLeft) {			
			// 1 - turn to face switch
			left1 = 1;
			right1 = 17;

			// 2 - drive to the switch
			left2 = 104-4.5+2;
			right2 = 104-4.5+2;
	
			// 3 - turn to face switch
			left3 = 17;
			right3 = 1;
			
			// 4 - back away
			left4 = -44;
			right4 = -44;
	
			// 5 - turn to cube pile
			left5 = 16;
			right5 = -16;

			// 6 - forward to grab cube
			left6 = 18-3.5;
			right6 = 18-3.5;

			// 7 - back away from cube pile
			left7 = -18-3.5;
			right7 = -18-3.5;

			// 8 - turn to switch
			left8 = -14;
			right8 = 14;

			// 9 - drive to the switch
			left9 = 49;
			right9 = 49;

			// 10 - back away
			left10 = -25;
			right10 = -25;

			// 11 - turn to cube pile
			left11 = 20-0.5;
			right11 = -20-0.5;

			// 12 - forward to grab cube
			left12 = 17;
			right12 = 17;

			// 13 - back away from cube pile
			left13 = -17;
			right13 = -17;
		}
		
		else {
			// 1 - turn to face switch
			left1 = 15+3.5+7;
			right1 = 1;

			// 2 - drive to the switch
			left2 = 104-5-1+0.5;
			right2 = 104-5-1+0.5;
	
			// 3 - turn to face switch
			left3 = 1;
			right3 = 15+3.5+7;
			
			// 4 - back away
			left4 = -36;
			right4 = -36;
	
			// 5 - turn to cube pile
			left5 = -20;
			right5 = 20;

			// 6 - forward to grab cube
			left6 = 36;
			right6 = 36;

			// 7 - back away from cube pile
			left7 = -30;
			right7 = -30;

			// 8 - turn to switch
			left8 = right5;
			right8 = left5;

			// 9 - drive to the switch
			left9 = 36;
			right9 = 36;

			// 10 - back away
			left10 = -25;
			right10 = -25;

			// 11 - turn to cube pile
			left11 = -20-0.5;
			right11 = 20-0.5;

			// 12 - forward to grab cube
			left12 = 18;
			right12 = 18;

			// 13 - back away from cube pile
			left13 = -17;
			right13 = -17;
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
			Robot.drive.setProfile(left3, right3, t3);
		}
		if(timer.atTime(t3+t2+t1)){ // step 4
			Robot.intake.ejectCube();
			Robot.drive.setProfile(left4, right4, t4);
		}
		if(timer.atTime(t4+t3+t2+t1)) { // step 5
			Robot.elevator.setPos(ElevatorPosition.FLOOR);
			Robot.intake.stopMotor();
			Robot.intake.squeezeOpen(true);
			Robot.drive.setProfile(left5, right5, t5);
		}
		if(timer.atTime(t5+t4+t3+t2+t1)) { // step 6
			Robot.intake.grabCube();
			Robot.drive.setProfile(left6, right6, t6);
		}
		if(timer.atTime(t6+t5+t4+t3+t2+t1)) { // step 7
			Robot.intake.squeezeOpen(false);
			Robot.drive.setProfile(left7, right7, t7);
		}
		if(timer.atTime(t7+t6+t5+t4+t3+t2+t1)) { // step 8
			Robot.intake.stopMotor();
			Robot.elevator.setPos(ElevatorPosition.SWITCH);
			Robot.drive.setProfile(left8, right8, t8);
		}
		if(timer.atTime(t8+t7+t6+t5+t4+t3+t2+t1)) { // step 9
			Robot.drive.setProfile(left9, right9, t9);
		}
		if(timer.atTime(t9+t8+t7+t6+t5+t4+t3+t2+t1)){ // step 10
			Robot.intake.ejectCube();
			Robot.drive.setProfile(left10, right10, t10);
		}
		if(timer.atTime(t10+t9+t8+t7+t6+t5+t4+t3+t2+t1)) { // step 11
			Robot.elevator.setPos(ElevatorPosition.FLOOR);
			Robot.intake.stopMotor();
			Robot.intake.squeezeOpen(true);
			Robot.drive.setProfile(left11, right11, t11);
		}
		if(timer.atTime(t11+t9+t8+t7+t6+t5+t4+t3+t2+t1)) { // step 12
			Robot.intake.grabCube();
			Robot.drive.setProfile(left12, right12, t12);
		}
		if(timer.atTime(t12+t10+t9+t8+t7+t6+t5+t4+t3+t2+t1)) { // step 13
			Robot.intake.squeezeOpen(false);
			Robot.drive.setProfile(left13, right13, t13);
		}
		
		if(timer.atTime(14900)) {
			Robot.drive.setMode(Chassis.Mode.ARCADE);
		}

		timer.update();
	}
}
