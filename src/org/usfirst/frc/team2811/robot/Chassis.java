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
import edu.wpi.first.wpilibj.Timer;

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
	public enum Mode{PROFILE,TANK,ARCADE,DISABLED,VELPROFILE}
	Mode mode = Mode.ARCADE;
	double leftPower = 0; 
	double rightPower = 0;
	
	//Drive configuration values. Will be overwritten by disabledperiodic
	//also these values are bubkis
	double scaleFactorL = 100; // OVERWRITTEN DO NOT USE
	double scaleFactorR = 100; // OVERWRITTEN DO NOT USE 

	double arcadeTurn = 0; 
	double arcadePower = 0;
	boolean squaredInputs = true;

	boolean braking = true;
	private double leftFBGain=0.024; //GOLD on compbot
	private double rightFBGain=0.024; // GOLD on compbot

	SimpleCsvLogger logfileLeft = new SimpleCsvLogger();
	SimpleCsvLogger logfileRight = new SimpleCsvLogger();
	
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
			//Bad calibration values for use with Centerv3 auto 
			//when paired with the incorrect calibration values
			//scaleFactorL = 199.8; 
			//scaleFactorR = 400.4; 

			scaleFactorL = (27400 / 137) + 2 - 2; //is GOLD
			scaleFactorR = (54400 / 137) + 1; //is GOLD
			
			//Factor =        (ticks/inches) * (expected/actual)
			scaleFactorL = (30815.0/153.125) * (130/129) * (130/129.5);
			scaleFactorR = (60935.0/152.125) * (130/128.5);
			
		}else {
			//practice bot
			//good, but 1/2" short over 130 inches
//			scaleFactorL = 36986/124.75; 
//			scaleFactorR = 36419/123.25;
			
			//Attempt to adjust for slight shortcoming
			scaleFactorL = 36986/124.75*(130/129.5); 
			scaleFactorR = 36419/123.25*(130/129.5);
			
			//works well, slight oscilation is possible
			leftFBGain = 0.024;
			rightFBGain = 0.024;

			//Debug for feed forward
			//leftFBGain = 0.0;
			//rightFBGain = 0.0;
		}
		//braking(false);
		logfileLeft.close();
		logfileRight.close();
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
	
	void autonomousInit() {
		String[] label = {"time","target","position","error","velocity","%output","current lead","current rear","current front","sys voltage"};
		String[] units = {"sec","ticks","ticks","ticks","ticks/sec","-1..1","amps","amps","amps","Volts"};
		logfileLeft.init("chassisLeft", units, label);
		logfileRight.init("chassisRight", units, label);
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
	
	
	public void setProfileGains(double leftGain,double rightGain) {
		leftFBGain = leftGain;
		rightFBGain = rightGain;
	}
	
	public void setProfileCalibration(double leftCal,double rightCal) {
		scaleFactorL = leftCal;
		scaleFactorR = rightCal;
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
		left345.setMove(25_200, time, inchesLeft*scaleFactorL, 200);
		right345.setMove(25_200, time, inchesRight*scaleFactorR, 200);
		//TODO: Actually find some values for max speed and a good tolerance
		
		// Reset our timer to start executing
		profileTimer.reset();
		resetEnc();

		//set our chassis mode to use the profile!
		mode = Mode.PROFILE;
	}
	
	double maxleft=0,maxright=0;
	double lastLeft=0,lastRight=0;
	double lastTime=0;
	double maxDeltaLeft=0;
	double maxDeltaRight=0;
	/** Update function that handles all busywork the chassis has been 
	 * set up to perform.
	 */
	public void newUpdate(){
		//Generate a power modifier to ensure we avoid brownouts
		double mod = Utilities.lerp(pdp.getVoltage(), 8, 6.5, 1.0, 0.0);
		mod = Utilities.clamp(mod, 0, 1);

		SmartDashboard.putString("Chassis Mode", mode.toString());
		
		switch(mode) {
		case DISABLED:
			driver.tankDrive(0,0);	
			return;
		case VELPROFILE:
			//leftPower = left345.getVelPosFbFF(profileTimer.getSeconds(), -leadL.getSelectedSensorPosition(0), 0.023);
			//rightPower = right345.getVelPosFbFF(profileTimer.getSeconds(), leadR.getSelectedSensorPosition(0), 0.023);
			//driver.tankDrive(leftPower, rightPower);

			break;
		case PROFILE:	
			profileTimer.update();
			
			if(prefs.getBoolean("compbot", Robot.compbot)) {
				//compbot
				leftPower = left345.getVelPosFb(profileTimer.getSeconds(), -leadL.getSelectedSensorPosition(0), leftFBGain); // 24 is GOLD
				rightPower = right345.getVelPosFb(profileTimer.getSeconds(), leadR.getSelectedSensorPosition(0), rightFBGain); // 24 is GOLD
				System.out.println( right345.getVel(profileTimer.getSeconds()));

				SmartDashboard.putNumber("Chassis Profile Left",         leftPower);
				SmartDashboard.putNumber("Chassis Profile Right",        rightPower);
				SmartDashboard.putNumber("Chassis Profile Left Sensor",  leadL.getSelectedSensorPosition(0));
				SmartDashboard.putNumber("Chassis Profile Sensor",       leadR.getSelectedSensorPosition(0));
			
			}
			else {
				//practice bot
				leftPower = -left345.getVelPosFb(profileTimer.getSeconds(), -leadL.getSelectedSensorPosition(0), leftFBGain);
				rightPower = -right345.getVelPosFb(profileTimer.getSeconds(), leadR.getSelectedSensorPosition(0), rightFBGain);
			}

			SmartDashboard.putNumber("Chassis Left Power",         leftPower);
			SmartDashboard.putNumber("Chassis Right Power",        rightPower);
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
		
		double leftv = leadL.getSelectedSensorVelocity(0);
		if(leftv > maxleft) { maxleft = leftv; }
		
		double rightv = leadR.getSelectedSensorVelocity(0);
		if(rightv > maxright) { maxright = rightv; }
		
		SmartDashboard.putNumber("Chassis Left Max Velocity",  maxleft);
		SmartDashboard.putNumber("Chassis Left Max Velocity*2",  maxleft*2);
		SmartDashboard.putNumber("Chassis Right Max Velocity",  maxright);
	
		
		double deltaLeft=lastLeft-leadL.getSelectedSensorPosition(0);
		double deltaRight=lastRight-leadR.getSelectedSensorPosition(0);
		
		if(deltaLeft > maxDeltaLeft) {maxDeltaLeft = deltaLeft;}
		if(deltaRight > maxDeltaRight) {maxDeltaRight = deltaRight;}
		
		lastLeft=leadL.getSelectedSensorPosition(0);
		lastRight=leadR.getSelectedSensorPosition(0);
		
		SmartDashboard.putNumber("Chassis Left Max Velocity (per loop)", maxDeltaLeft );
		SmartDashboard.putNumber("Chassis Left Max Velocity (per loop)*2",  maxDeltaLeft*2);
		SmartDashboard.putNumber("Chassis Right Max Velocity (per loop)",  maxDeltaRight);

		double deltaTime = Timer.getFPGATimestamp() - lastTime;
		lastTime = Timer.getFPGATimestamp();
		
		if(rightv > maxright) { maxright = rightv; }

		SmartDashboard.putNumber("Chassis Left Max Velocity (per second)", maxDeltaLeft/deltaTime );
		SmartDashboard.putNumber("Chassis Left Max Velocity (per second)*2",  maxDeltaLeft/deltaTime*2);
		SmartDashboard.putNumber("Chassis Right Max Velocity (per second)",  maxDeltaRight/deltaTime);
		
		
		SmartDashboard.putNumber("Chassis Left Raw Enc",  leadL.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Chassis Left Mod Enc",  leadL.getSelectedSensorPosition(0)*2);
		SmartDashboard.putNumber("Chassis Right Enc",  leadR.getSelectedSensorPosition(0));
		
		double leftError = left345.getPos(profileTimer.getSeconds()) - -leadL.getSelectedSensorPosition(0);
		SmartDashboard.putNumber("Chassis Left Error", leftError);
		
		double rightError = right345.getPos(profileTimer.getSeconds()) - leadR.getSelectedSensorPosition(0);
		SmartDashboard.putNumber("Chassis Right Error", rightError);
		
		//write any number of things to this
//		 String[] label = {"time","target","position","error","velocity","current","%output"};
//		 String[] units = {"sec","ticks","ticks","ticks","ticks/sec","amps","-1..1"};

		logfileLeft.writeData(
				Timer.getMatchTime(),
				left345.getPos(profileTimer.getSeconds()),
				-leadL.getSelectedSensorPosition(0),
				leftError,
				deltaLeft, //velocity for now
				leftPower,
				leadL.getOutputCurrent(), //current
				rearL.getOutputCurrent(), //current
				frontL.getOutputCurrent(), //current
				pdp.getVoltage()
				);
		
		logfileRight.writeData(
				Timer.getMatchTime(),
				right345.getPos(profileTimer.getSeconds()),
				leadR.getSelectedSensorPosition(0),
				rightError,
				deltaRight, //velocity for now
				rightPower,
				leadR.getOutputCurrent(), //current
				rearR.getOutputCurrent(), //current
				frontR.getOutputCurrent(), //current
				pdp.getVoltage()
				);
		
		SmartDashboard.putNumber("leadL", leadL.getOutputCurrent());
		SmartDashboard.putNumber("leadR", leadR.getOutputCurrent());		
		SmartDashboard.putNumber("rearL", rearL.getOutputCurrent());
		SmartDashboard.putNumber("rearR", rearR.getOutputCurrent());
		SmartDashboard.putNumber("frontL", frontL.getOutputCurrent());
		SmartDashboard.putNumber("frontR", frontR.getOutputCurrent());
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
