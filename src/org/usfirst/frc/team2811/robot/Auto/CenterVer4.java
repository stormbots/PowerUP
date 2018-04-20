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
	double left14;
	double right14;
	
	long ts = 1;
	
	long t0 = 0000;
	long t1 = 1500*ts; // turn								 // 
	long t2 = 3000*ts; // drive to switch 					 // 
	long t3 = 1000*ts; // turn to switch					 // 
	long t4 = 2000*ts; // back away from switch				 // 
	long t5 = (750+500)*ts; // turn to cube pile			 // 
	long t6 = 1000*ts; // forward to cube pile				 // 
	long t7 = 1250*ts; // slow cube grab move				// 
	long t8 = 750*ts; // back from cube pile				// 
	long t9 = (750+500)*ts; // turn to the switch			// 13000 at end
	long t10 = 1000*ts; // forward to the switch			// 14000 at end
	long t11 = 1000*ts; // back away from the switch		// 15000 at end
	long t12 = (750+500)*ts; // turn to the cube pile		// 
	long t13 = 750*ts; // forward to grab cube				// 
	long t14 = 750*ts; // back from cubes					// 
	
	public CenterVer4(boolean toLeft) {
		
		if(toLeft) {			
			// 1 - turn to face switch
			left1 = 1;
			right1 = 23.5;

			// 2 - drive to the switch
			left2 = 104.5-6+2;
			right2 = 104.5-6+2;
	
			// 3 - turn to face switch
			left3 = right1;
			right3 = left1;
			
			// 4 - back away
			left4 = -36;  // -19
			right4 = -36; // -19
	
			// 5 - turn to cube pile
			left5 = 14;   // 18
			right5 = -14; // 18

			// 6 - forward to cube pile
			left6 = 36;
			right6 = 36;
			
			// 7 - slow to grab cube
			left7 = -6;
			right7 = -6;
			
			// 8 - back away from cube pile
			left8 = -30;
			right8 = -30;

			// 9 - turn to switch
			left9 = right5;
			right9 = left5;

			// 10 - drive to the switch
			left10 = 36;
			right10 = 36;

			// 11 - back away
			left11 = -25;
			right11 = -25;

			// 12 - turn to cube pile
			left12 = 20;
			right12 = -20;
			
			// 13 - forward to grab cube
			left13 = 17;
			right13 = 17;

			// 14 - back away from cube pile
			left14 = -17;
			right14 = -17;
		}
		
		else { //right side
			// 1 - turn to face switch
			left1 = 20.5 - 1;
			right1 = 1;

			// 2 - drive to the switch
			left2 = 104.5-6;
			right2 = 104.5-6;
	
			// 3 - turn to face switch
			left3 = right1;
			right3 = left1;
			
			// 4 - back away
			left4 = -44;  // -22
			right4 = -44; // -22
	
			// 5 - turn to cube pile
			left5 = -14; // -20
			right5 = 14; // 20

			// 6 - quick forward to grab cube
			left6 = 33;  // 40
			right6 = 33; // 40
			
			// 7 - slow to grab cube
			left7 = 8;
			right7 = 14;
			
			// 8 - back away from cube pile
			left8 = -42;
			right8 = -42;

			// 9 - turn to switch
			left9 = 16;
			right9 = -16;

			// 10 - drive to the switch
			left10 = 36;
			right10 = 36;

			// 11 - back away
			left11 = -12;
			right11 = -12;

			//cube 3 --->
			
			// 12 - turn to cube pile
			left12 = -20;
			right12 = 20;
			
			// 13 - forward to grab cube
			left13 = 18;
			right13 = 18;

			// 14 - back away from cube pile
			left14 = -17;
			right14 = -17;
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
			Robot.intake.grabCube();
			Robot.drive.setProfile(left7, right7, t7);
		}
		if(timer.atTime(-125+t7+t6+t5+t4+t3+t2+t1)) { // step squeeze
			Robot.intake.squeezeOpen(false);
		}
		if(timer.atTime(t7+t6+t5+t4+t3+t2+t1)) { // step 8
			Robot.intake.squeezeOpen(false);
			Robot.drive.setProfile(left8, right8, t8);
		}
		if(timer.atTime(t8+t7+t6+t5+t4+t3+t2+t1)) { // step 9
			Robot.intake.stopMotor();
			Robot.elevator.setPos(ElevatorPosition.SWITCH);
			Robot.drive.setProfile(left9, right9, t9);
		}
		if(timer.atTime(t9+t8+t7+t6+t5+t4+t3+t2+t1)) { // step 10
			Robot.drive.setProfile(left10, right10, t10);
		}
		if(timer.atTime(t10+t9+t8+t7+t6+t5+t4+t3+t2+t1)){ // step 11
			Robot.intake.ejectCube();
			Robot.drive.setProfile(left11, right11, t11);
		}
		if(timer.atTime(t11+t10+t9+t8+t7+t6+t5+t4+t3+t2+t1)) { // step 12
//			Robot.elevator.setPos(ElevatorPosition.FLOOR);
			Robot.intake.stopMotor();
			Robot.intake.squeezeOpen(true);
			Robot.drive.setProfile(left12, right12, t12);
		}
		if(timer.atTime(t12+t11+t9+t8+t7+t6+t5+t4+t3+t2+t1)) { // step 13
			Robot.intake.grabCube();
			Robot.drive.setProfile(left13, right13, t13);
		}
		if(timer.atTime(t13+t12+t10+t9+t8+t7+t6+t5+t4+t3+t2+t1)) { // step 14
			Robot.intake.squeezeOpen(false);
			Robot.drive.setProfile(left14, right14, t14);
		}
		
		if(timer.atTime(14900)) {
			Robot.drive.setMode(Chassis.Mode.ARCADE);
		}

		timer.update();
	}
}
