package org.usfirst.frc.team2811.robot;

import com.ctre.phoenix.motorcontrol.can.*;

import org.usfirst.frc.team2811.robot.Robot.RobotLocation;
import org.usfirst.frc.team2811.robot.Robot.ScaleConfig;
import org.usfirst.frc.team2811.robot.Robot.SwitchConfig;
import org.usfirst.frc.team2811.robot.Robot.TargetLocation;

import com.ctre.phoenix.motorcontrol.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
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

public class Chassis extends RobotModule {
	WPI_TalonSRX leadL = new WPI_TalonSRX(2);
	TalonSRX frontL = new TalonSRX(1);
	TalonSRX rearL = new TalonSRX(0);
	WPI_TalonSRX leadR = new WPI_TalonSRX(5);
	TalonSRX frontR = new TalonSRX(4);
	TalonSRX rearR = new TalonSRX(3);
	DifferentialDrive driver = new DifferentialDrive(leadL, leadR);
	Preferences prefs = Preferences.getInstance();
	
	//Initialization is robot specific, done in constructor
	Solenoid LeftShiftA;
	Solenoid LeftShiftB;
	Solenoid RightShiftA;
	Solenoid RightShiftB;
	

	Motion345 left345 = new Motion345(10000, 3, 0, 200);
	Motion345 right345 = new Motion345(10000, 3, 0, 200);
	
	boolean braking = true;
	boolean isTank = false;
	
	public double left1 = 0;
	public double left2 = 0;
	public double left3 = 0;
	public double right1 = 0;
	public double right2 = 0;
	public double right3 = 0;
	public double t1=2.5;
	public double t2=2.5;
	public double t3=2.5;


	/**
	 * the constructor: 
	 *initializes the slave talons
	 */
	public Chassis() {
		if(prefs.getBoolean("compbot", true)) {
			//Comp Bot
			LeftShiftA = new Solenoid(2);
			LeftShiftB = new Solenoid(3);
			RightShiftA = new Solenoid(6); //not actually used
			RightShiftB = new Solenoid(7);	//not actually used
		}
		else {
			//practice bot
			LeftShiftA = new Solenoid(2);
			LeftShiftB = new Solenoid(3);
			RightShiftA = new Solenoid(4);
			RightShiftB = new Solenoid(5);		
		}

		//initializes the slaves and shifters

		shiftLow();
		bind();
		prefs.getBoolean("compbot", false); // true if on the compbot
	}
	
	/**
	 * sets braking or coasting based on input
	 * @param brake
	 */
	public void braking(boolean brake) {
		NeutralMode mode = NeutralMode.Brake;
		if(brake == false) {
			mode = NeutralMode.Coast;
		}
		
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
	
	/**
	 * Drives:
	 *   gets the stick axi, and plug that into the 2 motor arcade drive.
	 *   Then binds the slaves to the leads
	 * @param stick
	 */
	void update(Joystick stickDrive, Joystick stickL, Joystick functions) {
		//updates the lead talons, then updates the slave talons

		if(stickDrive.getRawButton(8)) {
			shiftHigh();
		}
		else {
			shiftLow(); 
		}
		
		double joyAdjustment = 0.75;
		if(prefs.getBoolean("compbot", false)) {
			//comp bot
			driver.arcadeDrive(stickDrive.getRawAxis(3), -stickDrive.getRawAxis(0)*joyAdjustment,true);
		}
		else {
			//prac bot
			driver.arcadeDrive(-stickDrive.getRawAxis(3), -stickDrive.getRawAxis(0)*joyAdjustment,true);
		}


		bind();

		SmartDashboard.putNumber("Pos Right", -leadR.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Pos Left", -leadL.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Vel Right", leadR.getMotorOutputPercent());
		SmartDashboard.putNumber("Vel Left", leadL.getMotorOutputPercent());
	}
	
	public void shiftLow() {
		// sets the gear to low
		if(prefs.getBoolean("compbot", true)) {
			//comp bot
			LeftShiftA.set(true);
			LeftShiftB.set(false);			
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
		if(prefs.getBoolean("compbot", false)) {
			//comp bot
			LeftShiftA.set(false);
			LeftShiftB.set(true);			
		}
		else {
			//prac bot
			LeftShiftA.set(true);
			LeftShiftB.set(false);
			RightShiftA.set(true);
			RightShiftB.set(false);
		}
	}
	
	
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
		
		double scaleFactorL;
		double scaleFactorR;
		
		braking(true);
		shiftLow();
		double scale = prefs.getDouble("chassisScaleFactor", 291);
		if(prefs.getBoolean("compbot", false)) {
			//comp bot
			scaleFactorL = 199.8;//change to preference
			scaleFactorR = 405.4;//change to preference
		}
		else {
			//prac bot
			scaleFactorL = scale;
			scaleFactorR = scale;			
		}
		

		if(robotLocation == RobotLocation.LEFT) {
			SmartDashboard.putString("RobotLoc", "left");
			if(targetLocation == TargetLocation.SCALE) {
				SmartDashboard.putString("TargLoc", "scale");

				left1 = 270.1284*scaleFactorL;
				right1 = -260.0641*scaleFactorR;
				left2 = 0;
				right2 = 0;
				left3 = 0;
				right3 = 0;
				t1=8.0; t2=0; t3=0;
			}
			if(targetLocation == TargetLocation.SWITCH) {
				SmartDashboard.putString("TargLoc", "switch");

				left1 = 133.6405*scaleFactorL;
				right1 = -108.5790*scaleFactorR;
				left2 = 0;
				right2 = 0;
				left3 = 0;
				right3 = 0;
				t1=8.0; t2=0; t3=0;

			}
			if(targetLocation == TargetLocation.MOVE_ONLY) {
				SmartDashboard.putString("TargLoc", "move");

				left1 = 120*scaleFactorL;
				right1 = -120*scaleFactorR;
				left2 = 0;
				right2 = 0;
				left3 = 0;
				right3 = 0;
				t1=8.0; t2=0; t3=0;

			}
		}
		else if(robotLocation == RobotLocation.CENTER) {
			SmartDashboard.putString("RobotLoc", "center");
			SmartDashboard.putString("TargLoc", "switch");

			t1=2.5; t2=2.5;	t3=2.5;
			if(switchConfig == switchConfig.LEFT) {
				left1 = 29.0597*scaleFactorL;
				right1 = -68.3296*scaleFactorR;
				left2 = 70.3296*scaleFactorL;																																																											;
				right2 = -29.0597*scaleFactorR;
				left3 = 37*scaleFactorL;
				right3 = -37*scaleFactorR;
				SmartDashboard.putString("SwitchConfig", "left");

			}
			else {
				SmartDashboard.putString("SwitchConfig", "right");
				left1 = 81.4712*scaleFactorL;
				right1 = -41.5013*scaleFactorR;
				left2 = 41.5013*scaleFactorL;
				right2 = -81.4712*scaleFactorR;
				left3 = 8*scaleFactorL;
				right3 = -8*scaleFactorR;
			}
		}
		else if(robotLocation == RobotLocation.RIGHT) {
			SmartDashboard.putString("RobotLoc", "right");

			if(targetLocation == TargetLocation.SCALE) {
				left1 = 262.0641*scaleFactorL;
				right1 = -270.1284*scaleFactorR;
				left2 = 0;
				right2 = 0;
				left3 = 0;
				right3 = 0;
				t1=8.0;	t2=0.0;	t3=0.0;
				SmartDashboard.putString("TargLoc", "scale");

			}
			if(targetLocation == TargetLocation.SWITCH) {
				left1 = 108.5790*scaleFactorL;
				right1 = -133.6405*scaleFactorR;
				left2 = 0;
				right2 = 0;
				left3 = 0;
				right3 = 0;
				t1=8.0;	t2=0.0;	t3=0.0;
				SmartDashboard.putString("TargLoc", "switch");

			}
			if(targetLocation == TargetLocation.MOVE_ONLY) {
				SmartDashboard.putString("TargLoc", "move");

				left1 = 120*scaleFactorL;
				right1 = -120*scaleFactorR;
				left2 = 0;
				right2 = 0;
				left3 = 0;
				right3 = 0;
				t1=8.0;	t2=0.0;	t3=0.0;

			}
		}
		
		resetEnc();
	}

	
	/**
	 * auto forward:
	 *   uses an variable position, no pid yet
	 * 
	 * @param pos
	 */
	void auto(int step, double time) {
		
		double rightV = 0;
		double leftV = 0;
		
		switch(step) {
			
			case 0:
				if(time == 0) {
					resetEnc();
				}
				break;
					
			case 1:
				if(time == 0) {
					resetEnc();
				}
				left345.setMove(10000, t1, left1, 200);
				right345.setMove(10000, t1, right1, 200);
				//keep at Case 1
				break;
				
			case 2:
				if(time == 0) {
					resetEnc();
				}
				left345.setMove(10000, t2, left2, 200);
				right345.setMove(10000, t2, right2, 200);
				//keep at Case 2
				break;
				
			case 3:
				if(time == 0) {
					resetEnc();
				}
				left345.setMove(10000, t3, left3, 200);
				right345.setMove(10000, t3, right3, 200);
				//keep at Case 3
				break;
				
			case 4:
				if(time == 0) {
					resetEnc();
				}
				//keep at Case 4
				break;
				
			case 5:
				if(time == 0) {
					resetEnc();
				}
				//keep at Case 5
				break;
				
			default:
				if(time == 0) {
					resetEnc();
				}
				//keep at default
				break;
		}
		
		if(step > 0 && step < 4 && time > 0.1) {
			if(prefs.getBoolean("compbot", false)) {
				//comp bot
				leftV = left345.getVelPosFb(time, -leadL.getSelectedSensorPosition(0), 0.018);
				rightV = -right345.getVelPosFb(time, -leadR.getSelectedSensorPosition(0), 0.018);
			}
			else {
				//prac bot
				leftV = -left345.getVelPosFb(time, -leadL.getSelectedSensorPosition(0), 0.018);
				rightV = right345.getVelPosFb(time, -leadR.getSelectedSensorPosition(0), 0.018);				
			}	
		}
		
		
		if (prefs.getBoolean("compbot", false)){
			//comp bot
			driver.tankDrive(leftV, rightV);
		}
		else {
			//prac bot
			driver.tankDrive(rightV, leftV);	
		}
		
		
		SmartDashboard.putNumber("leftV", leftV);
		SmartDashboard.putNumber("rightV", rightV);
		SmartDashboard.putNumber("Pos Right", leadR.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Pos Left", -leadL.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Vel Right", leadR.getMotorOutputPercent());
		SmartDashboard.putNumber("Vel Left", leadL.getMotorOutputPercent());
		SmartDashboard.putNumber("time", time);
		SmartDashboard.putNumber("step", step);
		SmartDashboard.putNumber("right1", right1);
		SmartDashboard.putNumber("left1", left1);
		SmartDashboard.putNumber("leftTarg", left345.getPos(time));
		SmartDashboard.putNumber("rightTarg", right345.getPos(time));
	}
	
	public void disabledPeriodic() {
		//braking(false);
	}

}
