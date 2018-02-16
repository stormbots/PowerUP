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
	WPI_TalonSRX leadL = new WPI_TalonSRX(2);//
	TalonSRX frontL = new TalonSRX(1);
	TalonSRX rearL = new TalonSRX(0);
	WPI_TalonSRX leadR = new WPI_TalonSRX(5);//
	TalonSRX frontR = new TalonSRX(4);
	TalonSRX rearR = new TalonSRX(3);
	DifferentialDrive driver = new DifferentialDrive(leadL, leadR);
	Solenoid leftShiftA = new Solenoid(2);
	Solenoid leftShiftB = new Solenoid(3);
	Solenoid rightShiftA = new Solenoid(4);
	Solenoid rightShiftB = new Solenoid(5);

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



	/**
	 * the constructor: 
	 *initializes the slave talons
	 */
	public Chassis() {
		//initializes the slaves and shifters
		leftShiftA.set(false);
		leftShiftB.set(true);
		rightShiftA.set(false);
		rightShiftB.set(true);
		bind();
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
		leftShiftA.set(true);
		leftShiftB.set(false);
		rightShiftA.set(true);
		rightShiftB.set(false);
	}
	
	/**
	 * Drives:
	 *   gets the stick axi, and plug that into the 2 mtr arcade drive.
	 *   Then binds the slaves to the leads
	 * @param stick
	 */
	void update(Joystick stickDrive, Joystick stickL, Joystick functions) {
		//updates the lead talons, then updates the slave talons
			
		shiftLow();
		if(stickDrive.getRawButtonPressed(8)) {
			shiftHigh();
		}
		
		driver.arcadeDrive(-stickDrive.getRawAxis(3), -stickDrive.getRawAxis(0));

		SmartDashboard.putNumber("Pos Right", -leadR.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Pos Left", -leadL.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Vel Right", leadR.getMotorOutputPercent());
		SmartDashboard.putNumber("Vel Left", leadL.getMotorOutputPercent());
		bind();
	}
	
	public void shiftLow() {
		// sets the gear to low
		leftShiftA.set(false);
		leftShiftB.set(true);
		rightShiftA.set(false);
		rightShiftB.set(true);
	}

	public void shiftHigh() {
		// sets the gear to high
		leftShiftA.set(true);
		leftShiftB.set(false);
		rightShiftA.set(true);
		rightShiftB.set(false);
	}
	
	
	// ---- Autonomous Stuff ----
	
	
	/**
	 * re-initializes the lead talonSRX encoders
	 */
	public void resetEnc() {
		leadL.setSelectedSensorPosition(0, 0, 20);
		leadL.getSelectedSensorPosition(0);
		leadR.setSelectedSensorPosition(0, 0, 20);
		leadR.getSelectedSensorPosition(0);
	}
	
	void autoInit(
			RobotLocation robotLocation, 
			TargetLocation targetLocation, 
			SwitchConfig switchConfig, 
			ScaleConfig scaleConfig) {
		//save RobotLocation and TargetLocation to class fields, as we'll need in auto
		
		braking(true);
		shiftLow();
		double scaleFactor = 0.078;

		if(robotLocation == RobotLocation.LEFT) {
			if(targetLocation == TargetLocation.SCALE) {
				left1 = 1133415.0982*scaleFactor;
				right1 = -1110241.5762*scaleFactor;
				left2 = 0;
				right2 = 0;
				left3 = 0;
				right3 = 0;
			}
			if(targetLocation == TargetLocation.SWITCH) {
				left1 = 603770.7021*scaleFactor;
				right1 = -537792.7431*scaleFactor;
				left2 = 0;
				right2 = 0;
				left3 = 0;
				right3 = 0;
			}
			if(targetLocation == TargetLocation.MOVE_ONLY) {
				left1 = 444000.0000*scaleFactor;
				right1 = -444000.0000*scaleFactor;
				left2 = 0;
				right2 = 0;
				left3 = 0;
				right3 = 0;
			}
		}
		if(robotLocation == RobotLocation.CENTER) {
			if(switchConfig == switchConfig.LEFT) {
				left1 = 412648.195*scaleFactor;
				right1 = -958971.1575*scaleFactor;
				left2 = 958971.1575*scaleFactor;																																																											;
				right2 = -412648.195*scaleFactor;
				left3 = 299700.0000*scaleFactor;
				right3 = -299700.0000*scaleFactor;
			}
			else {
				left1 = 210683.0573*scaleFactor;
				right1 = -74102.3167*scaleFactor;
				left2 = 74102.3167*scaleFactor;
				right2 = -210683.0573*scaleFactor;
				left3 = 299700.0000*scaleFactor;
				right3 = -299700.0000*scaleFactor;
			}
		}
		if(robotLocation == RobotLocation.RIGHT) {
			if(targetLocation == TargetLocation.SCALE) {
				left1 = 1110241.5762*scaleFactor;
				right1 = -1133415.0982*scaleFactor;
				left2 = 0;
				right2 = 0;
				left3 = 0;
				right3 = 0;
			}
			if(targetLocation == TargetLocation.SWITCH) {
				left1 = 537792.7431*scaleFactor;
				right1 = -603770.7021*scaleFactor;
				left2 = 0;
				right2 = 0;
				left3 = 0;
				right3 = 0;
			}
			if(targetLocation == TargetLocation.MOVE_ONLY) {
				left1 = 444000.0000*scaleFactor;
				right1 = -444000.0000*scaleFactor;
				left2 = 0;
				right2 = 0;
				left3 = 0;
				right3 = 0;
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
				left345.setMove(10000, 5, left1, 200);
				right345.setMove(10000, 5, right1, 200);
				//keep at Case 1
				break;
				
			case 2:
				if(time == 0) {
					resetEnc();
				}
				left345.setMove(10000, 3, left2, 200);
				right345.setMove(10000, 3, right2, 200);
				//keep at Case 2
				break;
				
			case 3:
				if(time == 0) {
					resetEnc();
				}
				left345.setMove(10000, 3, left3, 200);
				right345.setMove(10000, 3, right3, 200);
				//keep at Case 3
				break;
				
			case 4:
				if(time == 0) {
					resetEnc();
				}
				driver.tankDrive(0, 0);
				//keep at Case 4
				break;
				
			case 5:
				if(time == 0) {
					resetEnc();
				}
				driver.tankDrive(0, 0);
				//keep at Case 5
				break;
				
			default:
				if(time == 0) {
					resetEnc();
				}
				driver.tankDrive(0, 0);
				//keep at default
				break;
		}
		
		if(step > 0 && step < 4 && time > 0.1) {
			leftV = -left345.getVelPosFb(time, -leadL.getSelectedSensorPosition(0), 0.0065);
			rightV = right345.getVelPosFb(time, -leadR.getSelectedSensorPosition(0), 0.0065);
		}
	

		driver.tankDrive(rightV, leftV);
		
		SmartDashboard.putNumber("leftV", leftV);
		SmartDashboard.putNumber("rightV", rightV);
		SmartDashboard.putNumber("Pos Right", -leadR.getSelectedSensorPosition(0));
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
	
	public void disabled() {
		//braking(false);
	}

}
