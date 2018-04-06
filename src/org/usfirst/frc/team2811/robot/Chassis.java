package org.usfirst.frc.team2811.robot;

import com.ctre.phoenix.motorcontrol.can.*;

import com.ctre.phoenix.Util;
import com.ctre.phoenix.motorcontrol.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

/* Inputs:
 *   Joystick Y & X axis
 *   One Joystick Button
 *   Encoders on the TalonCRXs
 * 
 * Outputs:
 *   drive velocity to motors
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

public class Chassis {
	WPI_TalonSRX leadL = new WPI_TalonSRX(2);
	TalonSRX frontL = new TalonSRX(1);
	TalonSRX rearL = new TalonSRX(0);
	WPI_TalonSRX leadR = new WPI_TalonSRX(5);
	TalonSRX frontR = new TalonSRX(4);
	TalonSRX rearR = new TalonSRX(3);
	DifferentialDrive driver = new DifferentialDrive(leadL, leadR);
	Preferences prefs = Preferences.getInstance();
	PowerDistributionPanel pdp = new PowerDistributionPanel();
	
	//Now identical in both robots
	Solenoid LeftShiftA = new Solenoid(2);
	Solenoid LeftShiftB = new Solenoid(3);
	Solenoid RightShiftA = new Solenoid(4); // practice bot only
	Solenoid RightShiftB = new Solenoid(5); // practice bot only


	Motion345 left345 = new Motion345(10000, 3, 0, 200);
	Motion345 right345 = new Motion345(10000, 3, 0, 200);
	TinyTimer profileTimer = new TinyTimer();
	public enum Mode{PROFILE,TANK,ARCADE,DISABLED}
	Mode mode = Mode.ARCADE;
	double leftPower = 0; 
	double rightPower = 0;
	
	//Drive configuration values. Will be overwritten by disabledperiodic
	double scaleFactorL = 27400 / 137; // 210; // compbot default
	double scaleFactorR = 54400 / 137; // 405.4; // compbot default
	
	double arcadeTurn = 0; 
	double arcadePower = 0;
	boolean squaredInputs = true;

	boolean braking = true;

	
	/**
	 * the constructor: 
	 *initializes the slave talons
	 */
	public Chassis() {
		braking(true);
		resetEnc();

		//initializes the slaves and shifters

		shiftLow();
		bind();
		
		double voltageRampRate = 0.075;
		leadL.configOpenloopRamp(voltageRampRate, 30);
		leadR.configOpenloopRamp(voltageRampRate, 30);
		frontL.configOpenloopRamp(voltageRampRate, 30);
		frontR.configOpenloopRamp(voltageRampRate, 30);
		rearL.configOpenloopRamp(voltageRampRate, 30);
		rearR.configOpenloopRamp(voltageRampRate, 30);
	}
	
	/** Fetch preferences and adjust variables as needed */
	public void disabledPeriodic() {
		if(prefs.getBoolean("compbot", Robot.compbot)) {
			//compbot
			
			//One encoder a 128 instead of a 256
			scaleFactorL = 199.8; 
			scaleFactorR = 405.4;
		}else {
			//practice bot
			scaleFactorL = 291; 
			scaleFactorR = 291;
		}
		//braking(false);
	}

	
	/**
	 * sets braking or coasting based on input
	 * @param brake
	 */
	public void braking(boolean brake) {
		
		// need to fix for coast on disable
		leadL.setNeutralMode(NeutralMode.Brake);
		frontL.setNeutralMode(NeutralMode.Brake);
		rearL.setNeutralMode(NeutralMode.Brake);
		leadR.setNeutralMode(NeutralMode.Brake);
		frontR.setNeutralMode(NeutralMode.Brake);
		rearR.setNeutralMode(NeutralMode.Brake);
	}
	

	/**
	 * sets the slaves:
	 *   With TALONs, uses set functions.
	 *   With TALONCRXs, uses follow functions.
	 */
	public void bind() {
		// set slave talons
		frontL.follow(leadL);
		rearL.follow(leadL);
		frontR.follow(leadR);
		rearR.follow(leadR);
	}
	
	void init() {
		braking(true);
		shiftLow();
	}
	
	public void shiftLow() {
		// sets the gear to low
		if(prefs.getBoolean("compbot", Robot.compbot)) {
			//comp bot
			LeftShiftA.set(false);
			LeftShiftB.set(true);			
		}
		else {
			//prac bot
			LeftShiftA.set(false);
			LeftShiftB.set(true);		
			RightShiftA.set(false);
			RightShiftB.set(true);
		}
		
	}

	public void shiftHigh() {
		// sets the gear to high
		if(prefs.getBoolean("compbot", Robot.compbot)) {
			//comp bot
			LeftShiftA.set(true);
			LeftShiftB.set(false);			
		}
		else {
			//prac bot
			LeftShiftA.set(true);
			LeftShiftB.set(false);
			RightShiftA.set(true);
			RightShiftB.set(false);
		}
	}
	
	
	/**
	 * re-initializes the lead talonSRX encoders
	 */
	public void resetEnc() {
		leadL.setSelectedSensorPosition(0, 0, 20);
		leadR.setSelectedSensorPosition(0, 0, 20);
	}
	
	/** Set the Chassis operational mode
	 * @param mode
	 */
	public void setMode(Mode newmode) {
		mode = newmode;
	}
	
	/** Set the target output power for Tankdrive mode
	 * @param left
	 * @param right
	 */
	public void tankMode(double left,double right,boolean squaredInputs) {
		leftPower = left;
		rightPower = right;
	}
	
	/** Configure the chassis to perform a motion-profile squence of set distance and duration.
	 * 
	 * @param inchesLeft
	 * @param inchesRight
	 * @param time (in ms)
	 */
	public void setProfile(double inchesLeft,double inchesRight, double time) {
		//Convert our input time from ms to seconds
		time  = time / 1000; 

		System.out.printf("Creating auto [ %f , %f ] over %f s\n",inchesLeft,inchesRight,time);

		// create our motion profile
		left345.setMove(10_000, time, inchesLeft*scaleFactorL, 200);
		right345.setMove(10_000, time, inchesRight*scaleFactorR, 200);
		//TODO: Actually find some values for max speed and a good tolerance
		
		// Reset our timer to start executing
		profileTimer.reset();
		resetEnc();

		//set our chassis mode to use the profile!
		mode = Mode.PROFILE;
	}
	
	/** Update function that handles all busywork the chassis has been 
	 * set up to perform.
	 */
	public void newUpdate(){
		//Generate a power modifier to ensure we avoid brownouts
		double mod = Utilities.lerp(pdp.getVoltage(), 8.25, 6.5, 1.0, 0.0);
		mod = Utilities.clamp(mod, 0, 1);

		SmartDashboard.putString("Chassis Mode", mode.toString());
		
		switch(mode) {
		case DISABLED:
			driver.tankDrive(0,0);	
			return;
		case PROFILE:	
			profileTimer.update();
			
			if(prefs.getBoolean("compbot", Robot.compbot)) {
				//compbot
				leftPower = left345.getVelPosFb(profileTimer.getSeconds(), -leadL.getSelectedSensorPosition(0), 0.022);
				rightPower = right345.getVelPosFb(profileTimer.getSeconds(), leadR.getSelectedSensorPosition(0), 0.022);				
		

				SmartDashboard.putNumber("Chassis Profile Left",         leftPower);
				SmartDashboard.putNumber("Chassis Profile Right",        rightPower);
				SmartDashboard.putNumber("Chassis Profile Left Sensor",  leadL.getSelectedSensorPosition(0));
				SmartDashboard.putNumber("Chassis Profile Sensor",       leadR.getSelectedSensorPosition(0));
			
			}
			else {
				//practice bot
				leftPower = -left345.getVelPosFb(profileTimer.getSeconds(), -leadL.getSelectedSensorPosition(0), 0.023);
				rightPower = -right345.getVelPosFb(profileTimer.getSeconds(), leadR.getSelectedSensorPosition(0), 0.023);
			}

			SmartDashboard.putNumber("Chassis Profile Left",         leftPower);
			SmartDashboard.putNumber("Chassis Profile Right",        rightPower);
			SmartDashboard.putNumber("Chassis Profile Left Sensor",  leadL.getSelectedSensorPosition(0));
			SmartDashboard.putNumber("Chassis Profile Sensor",       leadR.getSelectedSensorPosition(0));
			
			
						
			//NOTE: EXPECTED FALLTHROUGH TO TANK MODE
		case TANK:
			//Set the motor
			if (prefs.getBoolean("compbot", Robot.compbot)){
				//comp bot
				driver.tankDrive(leftPower, rightPower);
			}
			else {
				//prac bot
				driver.tankDrive(rightPower, leftPower);	
			}
			break;
		case ARCADE:
			//Not enabled for now, pending an update for OI related stuff.
			if(prefs.getBoolean("compbot", Robot.compbot)) {
				//comp bot
				driver.arcadeDrive(-arcadePower*mod, -arcadeTurn*mod, squaredInputs);
			}
			else {
				//prac bot
				driver.arcadeDrive(arcadePower*mod, -arcadeTurn*mod, squaredInputs);
			}			
			break;
		}
		
		SmartDashboard.putNumber("Chassis Left Raw Enc",  leadL.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Chassis Left Mod Enc",  leadL.getSelectedSensorPosition(0)*2);
		SmartDashboard.putNumber("Chassis Right Enc",  leadR.getSelectedSensorPosition(0));

	}
	
	/** Set the the arcadeDrive functions
	 * @param power
	 * @param turn
	 * @param squared inputs (more precise for small movements)
	 */
	public void arcadeDrive(double power,double turn, boolean squared) {
		arcadePower = power;
		arcadeTurn = turn;
		squaredInputs = squared;
	}

}
