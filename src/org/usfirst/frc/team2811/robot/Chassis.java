package org.usfirst.frc.team2811.robot;

import com.ctre.phoenix.motorcontrol.can.*;

import org.usfirst.frc.team2811.robot.Robot.RobotLocation;
import org.usfirst.frc.team2811.robot.Robot.TargetLocation;

import com.ctre.phoenix.motorcontrol.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/* Inputs:
 *   Joystick Y & X axis
 *   One Joystick Button
 *   Encouders on the TalonCRXs
 * 
 * Outputs:
 *   drive velocity to yhr motors
 *   shifts the gear
 * 
 * Summary:
 *   uses stick input and arcade drive to move around
 *   uses a single button to turn the shifter on or off
 *   later uses a timer amd PID/FB to run a autonomous 
 *  comp IDs -     left(2,  3, 4)  right(5,  6, 7)
 *  practice IDs - left(12, 1, 2)  right(13, 3, 4)
 * 
 */

public class Chassis extends RobotModule {
	WPI_TalonSRX leadL = new WPI_TalonSRX(12);//2
	Talon frontL = new Talon(0);
	Talon rearL = new Talon(1);
	//WPI_TalonSRX frontL = new WPI_TalonSRX(3);
	//WPI_TalonSRX rearL = new WPI_TalonSRX(4);
	WPI_TalonSRX leadR = new WPI_TalonSRX(13);//5
	Talon frontR = new Talon(2);
	Talon rearR = new Talon(3);
	//WPI_TalonSRX frontR = new WPI_TalonSRX(6);
	//WPI_TalonSRX rearR = new WPI_TalonSRX(7);
	DifferentialDrive driver = new DifferentialDrive(leadL, leadR);
	//Solenoid highGear = new Solenoid(3);
	private int step = 0;
	CXTIMER clock = new CXTIMER();
	Motion345 leadMleft = new Motion345(2900, 5, 17300, 200);
	Motion345 leadMright = new Motion345(2900, 5, -17300, 200);
	/**
	 * the constructor: 
	 *initializes the slave talons
	 */
	public Chassis() {
		//initializes the slaves
		bind();
	}

	/**
	 * sets the slaves:
	 *   With TALONs, uses set functions.
	 *   With TALONCRXs, uses follow functions.
	 */
	public void bind() {
		frontL.set(leadL.get());
		rearL.set(leadL.get());
		frontR.set(leadR.get());
		rearR.set(leadR.get());
		// set slave talons
		//frontL.follow(leadL);
		//rearL.follow(leadL);
		//frontR.follow(leadR);
		//rearR.follow(leadR);
	}
	
	/**
	 * Drives:
	 *   gets the stick axi, and plug that into the 2 mtr arcade drive.
	 *   Then binds the slaves to the leads
	 * @param stick
	 */
	void update(Joystick stick,Joystick driver2, Joystick functions) {
		//updates the lead talons, then updates the slave talons
		double targetLeft = 0.0;
		double targetRight = 0.0;
		double targetLeftVel = 0.0;
		double targetRightVel = 0.0;
		clock.Update();
		if(stick.getRawButton(10)) {
			clock.ckTime(true, 5000);
			targetRight = leadMright.getPos(clock.getTimeSec());
			targetLeft = leadMleft.getPos(clock.getTimeSec());
			targetRightVel = FB(leadR.getSelectedSensorPosition(0), targetRight, 0.01);
			targetLeftVel = FB(leadL.getSelectedSensorPosition(0), targetLeft, 0.01);
			leadL.set(ControlMode.PercentOutput, targetLeftVel);
			leadR.set(ControlMode.PercentOutput, targetRightVel);
		}
		else {
			driver.arcadeDrive(-stick.getY(), -stick.getX());
			clock.reset();
		}
		bind();
		SmartDashboard.putNumber("Right Pos", leadR.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Left Pos", leadL.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Right Vel", leadR.getMotorOutputPercent());
		SmartDashboard.putNumber("Left Vel", leadL.getMotorOutputPercent());
		SmartDashboard.putNumber("Target Left Velocity", targetLeftVel);
		SmartDashboard.putNumber("Target Right Velocity", targetRightVel);
		SmartDashboard.putNumber("Target Right Position" , targetRight);
		SmartDashboard.putNumber("Target Left Position", targetLeft);
	}
	/*
	public void shift() {
		// toggles the code
		highGear.set(!highGear.get());
	}
	*/
	
	
	// ---- Autonomous Stuff ----
	
	/**
	 * finds a velocity:
	 *   basic square root function
	 * 
	 * @param posActual
	 * @param posTarget
	 * @param K
	 * @return
	 */
	private double FB(double posActual, double posTarget, double K) {
		double S = 1;
		if(posActual > posTarget) {
			S = -1;
		}
		double VX = S*K*Math.sqrt(Math.abs(posActual-posTarget));
		if(VX > 1) {
			VX = 1;
		}
		if(VX < -1) {
			VX = -1;
		}
		return VX;
	}
	
	/**
	 * re-initializes the lead talonSRX encoders
	 */
	public void resetEnc() {
		leadL.setSelectedSensorPosition(0, 0, 20);
		leadR.setSelectedSensorPosition(0, 0, 20);
		clock.Update();
	}
	
	/**
	 * auto forward:
	 *   uses an variable position, no pid yet
	 * 
	 * @param pos
	 */
	public void moveAuto(int posL, int posR) {
		double targL = FB(leadL.getSelectedSensorPosition(0), posL, 0.08);
		double targR = FB(leadR.getSelectedSensorPosition(0), posR, 0.08);	
		leadL.set(ControlMode.PercentOutput, targL);
		leadR.set(ControlMode.PercentOutput, targR);
	}
	/* DONT BOTHER WITH THIS STUFF (shoud be in autonomous periodic)
	public void theSwitch() {
		switch(step) {
		
			case 0:
				if(clock.ckTime(true, 2500)) {
					step++;
					resetEnc();
					moveAuto(1000, 1000);
					//set up and start Case 1
				}
				else {
					moveAuto(2000, 2000);
					//keep at Case 0
				}
				break;
				
			case 1:
				if(clock.ckTime(true, 2500)) {
					step++;
					resetEnc();
					moveAuto(2000, 2000);
					//set up and start Case 2
				}
				else {
					moveAuto(1000, 1000);
					//keep at Case 1
				}
				break;
				
			case 2:
				if(clock.ckTime(true, 2500)) {
					step++;
					resetEnc();
					moveAuto(1000, 1000);
					//set up and start Case 3
				}
				else {
					moveAuto(2000, 2000);
					//keep at Case 2
				}
				break;
				
			case 3:
				if(clock.ckTime(true, 2500)) {
					step++;
					resetEnc();
					moveAuto(2000, 2000);
					//set up and start Case 4
				}
				else {
					moveAuto(1000, 1000);
					//keep at Case 3
				}
				break;
				
			case 4:
				if(clock.ckTime(true, 2500)) {
					step++;
					resetEnc();
					moveAuto(1000, 1000);
					//set up and start Case 5
				}
				else {
					moveAuto(2000, 2000);
					//keep at Case 4
				}
				break;
				
			case 5:
				if(clock.ckTime(true, 2500)) {
					step++;
					resetEnc();
					moveAuto(2000, 2000);
					//set up and start Default
				}
				else {
					moveAuto(1000, 1000);
					//keep at Case 5
				}
				break;
				
			default:
				moveAuto(0, 0);
				//keep at default
				break;
				
		}
	}
		*/
	
	void autoInit(RobotLocation robotLocation, TargetLocation targetLocation,int delay, boolean deliverCube) {
	}

	void auto(int step, double time) {
		
	}

}
