package org.usfirst.frc.team2811.robot;

import com.ctre.phoenix.motorcontrol.can.*;

import org.usfirst.frc.team2811.robot.Robot.RobotLocation;
import org.usfirst.frc.team2811.robot.Robot.ScaleConfig;
import org.usfirst.frc.team2811.robot.Robot.SwitchConfig;
import org.usfirst.frc.team2811.robot.Robot.TargetLocation;

import com.ctre.phoenix.motorcontrol.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

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
	Talon frontL = new Talon(1);
	Talon rearL = new Talon(2);
	//WPI_TalonSRX frontL = new WPI_TalonSRX(3);
	//WPI_TalonSRX rearL = new WPI_TalonSRX(4);
	WPI_TalonSRX leadR = new WPI_TalonSRX(13);//5
	Talon frontR = new Talon(3);
	Talon rearR = new Talon(4);
	//WPI_TalonSRX frontR = new WPI_TalonSRX(6);
	//WPI_TalonSRX rearR = new WPI_TalonSRX(7);
	DifferentialDrive driver = new DifferentialDrive(leadL, leadR);
	//Solenoid highGear = new Solenoid(3);
	
	Motion345 left345 = new Motion345(10000, 3, 0, 200);
	Motion345 right345 = new Motion345(10000, 3, 0, 200);
	
	public double left1 = 0;
	public double left2 = 0;
	public double left3 = 0;
	public double right1 = 0;
	public double right2 = 0;
	public double right3 = 0;

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
	void update(Joystick stickR, Joystick stickL, Joystick functions) {
		//updates the lead talons, then updates the slave talons
		if(stickL.getRawButton(2)) {
			driver.tankDrive(stickL.getY(), stickR.getY());
		}
		else {
			driver.arcadeDrive(stickR.getY(), stickR.getX());
		}

		bind();
	}
	/*
	public void shift() {
		// toggles the code
		highGear.set(!highGear.get());
	}
	*/
	
	
	// ---- Autonomous Stuff ----
	
	
	/**
	 * re-initializes the lead talonSRX encoders
	 */
	public void resetEnc() {
		leadL.setSelectedSensorPosition(0, 0, 20);
		leadR.setSelectedSensorPosition(0, 0, 20);
	}
	
	void autoInit(
			RobotLocation robotLocation, 
			TargetLocation targetLocation, 
			SwitchConfig switchConfig, 
			ScaleConfig scaleConfig) {
		//save RobotLocation and TargetLocation to class fields, as we'll need in auto
		
		
		if(robotLocation == RobotLocation.LEFT) {
			if(targetLocation == TargetLocation.SCALE) {
				if(scaleConfig == scaleConfig.LEFT) {
					left1 = 1133415.0982;
					right1 = 1110241.5762;
					left2 = 0;
					right2 = 0;
					left3 = 0;
					right3 = 0;
				}
				else {
					left1 = 0;
					right1 = 0;
					left2 = 0;
					right2 = 0;
					left3 = 0;
					right3 = 0;
				}
			}
			if(targetLocation == TargetLocation.SWITCH) {
				if(switchConfig == switchConfig.LEFT) {
					left1 = 603770.7021;
					right1 = 537792.7431;
					left2 = 0;
					right2 = 0;
					left3 = 0;
					right3 = 0;
				}
				else {
					left1 = 0;
					right1 = 0;
					left2 = 0;
					right2 = 0;
					left3 = 0;
					right3 = 0;
				}
			}
			if(targetLocation == TargetLocation.MOVE_ONLY) {
				left1 = 444000.0000;
				right1 = 444000.0000;
				left2 = 0;
				right2 = 0;
				left3 = 0;
				right3 = 0;
			}
		}
		if(robotLocation == RobotLocation.CENTER) {
			if(switchConfig == switchConfig.LEFT) {
				left1 = 412648.195;
				right1 = 958971.1575;
				left2 = 958971.1575																																																														;
				right2 = 412648.195;
				left3 = 299700.0000;
				right3 = 299700.0000;
			}
			else {
				left1 = 210683.0573;
				right1 = 74102.3167;
				left2 = 74102.3167;
				right2 = 210683.0573;
				left3 = 299700.0000;
				right3 = 299700.0000;
			}
		}
		if(robotLocation == RobotLocation.RIGHT) {
			if(targetLocation == TargetLocation.SCALE) {
				if(scaleConfig == scaleConfig.LEFT) {
					left1 = 0;
					right1 = 0;
					left2 = 0;
					right2 = 0;
					left3 = 0;
					right3 = 0;
				}
				else {
					left1 = 1110241.5762;
					right1 = 1133415.0982;
					left2 = 0;
					right2 = 0;
					left3 = 0;
					right3 = 0;
				}
			}
			if(targetLocation == TargetLocation.SWITCH) {
				if(switchConfig == switchConfig.LEFT) {
					left1 = 0;
					right1 = 0;
					left2 = 0;
					right2 = 0;
					left3 = 0;
					right3 = 0;
				}
				else {
					left1 = 537792.7431;
					right1 = 603770.7021;
					left2 = 0;
					right2 = 0;
					left3 = 0;
					right3 = 0;
				}
			}
			if(targetLocation == TargetLocation.MOVE_ONLY) {
				left1 = 444000.0000;
				right1 = 444000.0000;
				left2 = 0;
				right2 = 0;
				left3 = 0;
				right3 = 0;
			}
		}
	}

	
	/**
	 * auto forward:
	 *   uses an variable position, no pid yet
	 * 
	 * @param pos
	 */
	void auto(int step, double time) {
		switch(step) {
			
			case 0:
				if(time >= 0) {
					resetEnc();
					driver.tankDrive(left1, right1);
					//set up and start Case 1
					}
					else {
						driver.tankDrive(0, 0);
						//keep at Case 0
					}
					break;
					
			case 1:
				if(time >= 3) {
					resetEnc();
					left345.setMove(10000, 3, left2, 200);
					right345.setMove(10000, 3, right2, 200);
					driver.tankDrive(left345.getVel(time), right345.getVel(time));
					//set up and start Case 2
				}
				else {
					left345.setMove(10000, 3, left1, 200);
					right345.setMove(10000, 3, right1, 200);
					driver.tankDrive(left345.getVel(time), right345.getVel(time));
					//keep at Case 1
				}
				break;
				
			case 2:
				if(time >= 3) {
					resetEnc();
					left345.setMove(10000, 3, left3, 200);
					right345.setMove(10000, 3, right3, 200);
					driver.tankDrive(left345.getVel(time), right345.getVel(time));
					//set up and start Case 3
				}
				else {
					left345.setMove(10000, 3, left2, 200);
					right345.setMove(10000, 3, right2, 200);
					driver.tankDrive(left345.getVel(time), right345.getVel(time));
					//keep at Case 2
				}
				break;
				
			case 3:
				if(time >= 3) {
					resetEnc();
					driver.tankDrive(0, 0);
					//set up and start Case 4
				}
				else {
					left345.setMove(10000, 3, left3, 200);
					right345.setMove(10000, 3, right3, 200);
					driver.tankDrive(left345.getVel(time), right345.getVel(time));
					//keep at Case 3
				}
				break;
				
			case 4:
				if(time >= 3) {
					resetEnc();
					driver.tankDrive(0, 0);
					//set up and start Case 5
				}
				else {
					driver.tankDrive(0, 0);
					//keep at Case 4
				}
				break;
				
			case 5:
				if(time >= 3) {
					resetEnc();
					driver.tankDrive(0, 0);
					//set up and start Default
				}
				else {
					driver.tankDrive(0, 0);
					//keep at Case 5
				}
				break;
				
			default:
				driver.tankDrive(0, 0);
				//keep at default
				break;
		}
	}
	

}
