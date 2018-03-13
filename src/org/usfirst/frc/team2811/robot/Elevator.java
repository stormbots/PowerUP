package org.usfirst.frc.team2811.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2811.robot.FB;

public class Elevator {
	
	 WPI_TalonSRX eMotor = new WPI_TalonSRX(8);
	 DigitalInput LimitSwitch = new DigitalInput(1);
	 Preferences prefs = Preferences.getInstance();

	 double eVelocity = 0;
	 double elevatorPos = 0; //Used as the position you want the elevator to go to.
	 
	 double floorPos = 0.0;         //
	 double portalPos = 10000;      //
	 double switchPos = 36000;      // Set heights (estimated) for each location the elevator needs to get to.
	 double scaleLowPos = 70000;    //
	 double scaleHighPos = 90000;   //
	 double initializePos = 36000;
	 double maxPos = 92000;
	 double minPos = 0;
	 double softLimit = -2000;
	 double autoActiveStep = 3;
	 double autoActiveTime = 1;
	 
	 double autoPosition = 0.0; //Where you want to go to during auto.
	 double currentPos = 0.0;
	 
	 
	 public Elevator() {
		reset();
		double voltageRampRate = 0.2;
		eMotor.configOpenloopRamp(voltageRampRate, 30);
	 }
	 
	
	public enum Mode{ 
		MANUALVELOCITY, MANUALPOSITION, BUTTON, HOMING //Used to change how the elevator is controlled
	, DISABLED}
	
	private Mode mode = Mode.MANUALPOSITION;
	private boolean homed=false; 
	
	public void setMode(Mode newMode) {
		mode = newMode;
	}
	public Mode getMode () {
		return mode;
	}
	
	/** Fetch preferences and adjust variables as needed */
	public void disabledPeriodic(){
		maxPos = prefs.getDouble("elevatorTopLimit", 92000);
	}
	
	
		
	public void reset() {
		eMotor.setSelectedSensorPosition(0, 0, 20); //First argument is desired position, second is the type of loop? (0 or 1), third is the timeout.
	}
	
	public void resetTo(ElevatorPosition position) {
		eMotor.setSelectedSensorPosition(0, (int) position.ticks(), 20);
		eMotor.setSelectedSensorPosition((int) position.ticks(), 0, 20);// maybe just in case? Shouldn't do anything.
	}
		

	
	public void setVel(double velocity) {
		eVelocity = velocity;
	}
		
	/**
	 * @param position between -1 and 1
	 */
	public void setPos(double position) {
		Utilities.clamp(position,-1,1);
		elevatorPos = Utilities.lerp(position, 1, -1, 0, maxPos);
	}
	
	/**
	 * 	setPos(ElevatorPosition.SWITCH);
	 * @param position
	 */
	public void setPos(ElevatorPosition position) {
		setPos(position.ticks());
	}

	public enum ElevatorPosition{
		FLOOR(0),
		SWITCH (25_000),
		SCALEHIGH(92_000),
		AUTO_STARTUP(25_000),
		;
		
		double ticks = 0;
		ElevatorPosition(double ticks){this.ticks = ticks;}
		double ticks() {return this.ticks;};
	}
	
	void newUpdate() {
		
		if(prefs.getBoolean("compbot", true)) {
			//comp bot
			currentPos = -eMotor.getSelectedSensorPosition(0);
		}
		else {
			//prac bot
			currentPos = -eMotor.getSelectedSensorPosition(0);
		}
		
		switch(mode) {
		case MANUALPOSITION:
			Utilities.clamp(elevatorPos, 0, maxPos);
			eVelocity = FB.FB(elevatorPos, currentPos, 0.007);			
			//expected fallthrough to velocity mode
		case MANUALVELOCITY:
			//no need to manipulate velocity
			break;
		case HOMING:
			if(!LimitSwitch.get()) {
				eVelocity = 0;
			}
			else {
				eVelocity = -0.3;
			}
			break;
		default: 
			//disabled
			break;
		}
		
		//manipulate our velocity
		if(!LimitSwitch.get() && eVelocity <0) {
			eVelocity = 0;
		}
		
		//check for limit switch and reset if found
		if(!LimitSwitch.get()) {
			homed = true;
			reset();
		}

		eMotor.set(ControlMode.PercentOutput, eVelocity);

		SmartDashboard.putNumber("Elevator Current Position", currentPos);
		SmartDashboard.putNumber("Elevator Desired Position", elevatorPos);
		SmartDashboard.putNumber("Elevator Voltage", eMotor.getMotorOutputVoltage());
		SmartDashboard.putNumber("Elevator Velocity", eVelocity);
		SmartDashboard.putBoolean("Elevator Limit Switch is pressed", !LimitSwitch.get());
	}
}
