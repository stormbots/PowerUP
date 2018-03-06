package org.usfirst.frc.team2811.robot;

import com.ctre.phoenix.motorcontrol.can.*;

import org.usfirst.frc.team2811.robot.Robot.RobotLocation;
import org.usfirst.frc.team2811.robot.Robot.ScaleConfig;
import org.usfirst.frc.team2811.robot.Robot.SwitchConfig;
import org.usfirst.frc.team2811.robot.Robot.TargetLocation;

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

public class Chassis extends RobotModule {
	WPI_TalonSRX leadL = new WPI_TalonSRX(2);
	TalonSRX frontL = new TalonSRX(1);
	TalonSRX rearL = new TalonSRX(0);
	WPI_TalonSRX leadR = new WPI_TalonSRX(5);
	TalonSRX frontR = new TalonSRX(4);
	TalonSRX rearR = new TalonSRX(3);
	DifferentialDrive driver = new DifferentialDrive(leadL, leadR);
	Preferences prefs = Preferences.getInstance();
	PowerDistributionPanel pdp = new PowerDistributionPanel();
	
	//Initialization is robot specific, done in constructor
	Solenoid LeftShiftA;
	Solenoid LeftShiftB;
	Solenoid RightShiftA;
	Solenoid RightShiftB;
	

	Motion345 left345 = new Motion345(10000, 3, 0, 200);
	Motion345 right345 = new Motion345(10000, 3, 0, 200);
	CXTIMER profileTimer = new CXTIMER();
	enum Mode{PROFILE,TANK,ARCADE,DISABLED}
	Mode mode = Mode.ARCADE;
	double leftPower = 0; 
	double rightPower = 0;
	double scaleFactorL = 199.8; // compbot default
	double scaleFactorR = 405.4; // compbot default
	
	double arcadeTurn = 0; 
	double arcadePower = 0;
	boolean squaredInputs = true;

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
		
		double voltageRampRate = 0.075;
		leadL.configOpenloopRamp(voltageRampRate, 30);
		leadR.configOpenloopRamp(voltageRampRate, 30);
		frontL.configOpenloopRamp(voltageRampRate, 30);
		frontR.configOpenloopRamp(voltageRampRate, 30);
		rearL.configOpenloopRamp(voltageRampRate, 30);
		rearR.configOpenloopRamp(voltageRampRate, 30);
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
		
		// get pdp voltage
		// calculate a scale factor, 
		// mod = util.lerp(voltage,8.25,6.5,1,0
		// constrain mod to 0... 1
		//apply to all aracade drive outputs
		
		double mod = Utilities.lerp(pdp.getVoltage(), 8.25, 6.5, 1.0, 0.0);
		mod = Utilities.clamp(mod, 0, 1);
		
		double joyAdjustment = 0.75;
		if(prefs.getBoolean("compbot", false)) {
			//comp bot
			driver.arcadeDrive(stickDrive.getRawAxis(3)*mod, -stickDrive.getRawAxis(0)*joyAdjustment*mod,true);
		}
		else {
			//prac bot
			driver.arcadeDrive(-stickDrive.getRawAxis(3)*mod, -stickDrive.getRawAxis(0)*joyAdjustment*mod,true);
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
				left1 = 265*scaleFactorL;
				right1 = -265*scaleFactorR;
				
				//left1 = 270.1284*scaleFactorL;
				//right1 = -260.0641*scaleFactorR;
				//left1 = 270.7794*scaleFactorL;
				//right1 = -262.4143*scaleFactorR;
				left2 = 24*scaleFactorL;
				right2 = -0*scaleFactorR;

				left3 = 0;
				right3 = 0;
				t1=8.0; t2=3; t3=0;
			}
			if(targetLocation == TargetLocation.SWITCH) {
				SmartDashboard.putString("TargLoc", "switch");
				left1 = 130.6405*scaleFactorL;
				right1 = -114.5790*scaleFactorR;
				
				//left1 = 133.6405*scaleFactorL;
				//right1 = -108.5790*scaleFactorR;
				//left1 = 139.3341*scaleFactorL;
				//right1 = -111.9587*scaleFactorR;
				left2 = 0;
				right2 = 0;
				left3 = 0;
				right3 = 0;
				t1=8.0; t2=0; t3=0;

			}
			if(targetLocation == TargetLocation.MOVE_ONLY) {
				SmartDashboard.putString("TargLoc", "move");

				left1 = 130*scaleFactorL;
				right1 = -130*scaleFactorR;
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

			t1=2.0; t2=2.0;	t3=1.0;
			if(switchConfig == switchConfig.LEFT) {
				left1 = 29.0597*scaleFactorL;
				right1 = -68.3296*scaleFactorR;
				left2 = 68.3296*scaleFactorL;																																																											;
				right2 = -29.0597*scaleFactorR;
				left3 = 37*scaleFactorL;
				right3 = -37*scaleFactorR;
				
//				left1 = 26.7035*scaleFactorL;
//				right1 = -65.9734*scaleFactorR;
//				left2 = 65.9734*scaleFactorL;																																																											;
//				right2 = -26.7035*scaleFactorR;
//				left3 = 43*scaleFactorL;
//				right3 = -43*scaleFactorR;
				SmartDashboard.putString("SwitchConfig", "left");

			}
			else {
				SmartDashboard.putString("SwitchConfig", "right");
				left1 = 81.4712*scaleFactorL;
				right1 = -41.5013*scaleFactorR;
				left2 = 41.5013*scaleFactorL;
				right2 = -81.4712*scaleFactorR;
				left3 = 2*scaleFactorL;
				right3 = -2*scaleFactorR;
				
//				left1 = 58.1194*scaleFactorL;
//				right1 = -18.8495*scaleFactorR;
//				left2 = 18.8495*scaleFactorL;
//				right2 = -58.1194*scaleFactorR;
//				left3 = 53*scaleFactorL;
//				right3 = -53*scaleFactorR;
			}
		}
		else if(robotLocation == RobotLocation.RIGHT) {
			SmartDashboard.putString("RobotLoc", "right");

			if(targetLocation == TargetLocation.SCALE) {
				left1 = 265*scaleFactorL;
				right1 = -265*scaleFactorR;
				
				//left1 = 255.0641*scaleFactorL;
				//right1 = -261.1284*scaleFactorR;
				
				//left1 = 262.0641*scaleFactorL;
				//right1 = -270.1284*scaleFactorR;
				
				//left1 = 261.2217*scaleFactorL;//262.4143*scaleFactorL;
				//right1 = -267.3818*scaleFactorR;//270.7794*scaleFactorR;
				left2 = 0*scaleFactorL;
				right2 = -24*scaleFactorR;
				left3 = 0;
				right3 = 0;
				t1=8.0;	t2=3.0;	t3=0.0;
				SmartDashboard.putString("TargLoc", "scale");

			}
			if(targetLocation == TargetLocation.SWITCH) {
				left1 = 114.5790*scaleFactorL;
				right1 = -130.6405*scaleFactorR;
				
				//left1 = 111.9587*scaleFactorL;
				//right1 = -139.3341*scaleFactorR;
				left2 = 0;
				right2 = 0;
				left3 = 0;
				right3 = 0;
				t1=8.0;	t2=0.0;	t3=0.0;
				SmartDashboard.putString("TargLoc", "switch");

			}
			if(targetLocation == TargetLocation.MOVE_ONLY) {
				SmartDashboard.putString("TargLoc", "move");

				left1 = 130*scaleFactorL;
				right1 = -130*scaleFactorR;
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
				leftV = left345.getVelPosFb(time, -leadL.getSelectedSensorPosition(0), 0.023);
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
	
	/** Set the Chassis operational mode
	 * @param mode
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
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
		// create our motion profile
		//TODO: Actually find some values for max speed and a good tolerance
		left345 = new Motion345(10_000, time, inchesLeft*scaleFactorL, 200);
		right345 = new Motion345(10_000, time, inchesRight*scaleFactorR, 200);
		
		// Reset our timer to start executing
		profileTimer.reset();

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
		
		switch(mode) {
		case DISABLED:
			driver.tankDrive(0,0);	
			return;
		case PROFILE:
			//get next motion profile thing
			leftPower = left345.getVelPosFb(profileTimer.getTime(), -leadL.getSelectedSensorPosition(0), 0.023);
			rightPower = -right345.getVelPosFb(profileTimer.getTime(), -leadR.getSelectedSensorPosition(0), 0.023);
			//NOTE: EXPECTED FALLTHROUGH TO TANK MODE
		case TANK:
			//Set the motor
			if (prefs.getBoolean("compbot", false)){
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
			if(prefs.getBoolean("compbot", false)) {
				//comp bot
				driver.arcadeDrive(arcadePower*mod, -arcadeTurn*mod, squaredInputs);
			}
			else {
				//prac bot
				driver.arcadeDrive(-arcadePower*mod, -arcadeTurn*mod, squaredInputs);
			}
			break;
		}
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
