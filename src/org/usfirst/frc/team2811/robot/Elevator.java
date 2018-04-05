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
	
	 WPI_TalonSRX eMotorA;
	 WPI_TalonSRX eMotorB;
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
		if(prefs.getBoolean("compbot", Robot.compbot)) {
			eMotorA = new WPI_TalonSRX(8);
			eMotorB = new WPI_TalonSRX(9);
		}
		else {
			eMotorA = new WPI_TalonSRX(8);
			eMotorB = new WPI_TalonSRX(9);
		}
		
		reset();
		double voltageRampRate = 0.2;
		eMotorA.configOpenloopRamp(voltageRampRate, 30);
		eMotorB.configOpenloopRamp(voltageRampRate, 30);
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
	public void disabledPeriodic() {
		maxPos = prefs.getDouble("elevatorTopLimit", 92000);
		SmartDashboard.putNumber("ElevatorPos (Disabled)", eMotorA.getSelectedSensorPosition(0));
	}
	
	public void bind() {
		eMotorB.follow(eMotorA);
		// Investigate whther this makes the thing follow forever like expected
		// eMotorB.set(ControlMode.Follower, 8);
	}
		
	public void reset() {
		eMotorA.setSelectedSensorPosition(0, 0, 20); //First argument is desired position, second is the type of loop? (0 or 1), third is the timeout.
	}
	
	public void resetTo(ElevatorPosition position) {
		eMotorA.setSelectedSensorPosition(0, (int) position.ticks(), 20);
		eMotorA.setSelectedSensorPosition((int) position.ticks(), 0, 20);// maybe just in case? Shouldn't do anything.
	}
		
	public void setMaxHeight(ElevatorPosition newMaxPos) {
		maxPos = newMaxPos.ticks;
	}
	
	public void setVel(double velocity) {
		eVelocity = velocity;
	}
		
	/**
	 * @param position between -1 and 1
	 */
	public void setPos(double position) {
		Utilities.clamp(position,-1,1);
		elevatorPos = Utilities.lerp(position, -1, 1, 0, maxPos);
		elevatorPos = Utilities.clamp(elevatorPos, 0, maxPos);
	}
	
	/**
	 * 	setPos(ElevatorPosition.SWITCH);
	 * @param position
	 */
	public void setPos(ElevatorPosition position) {
		elevatorPos = position.ticks();
	}

	public enum ElevatorPosition{
		FLOOR(0),
		SWITCH (38_000),
		SCALEHIGH(92_000),
		AUTO_STARTUP(38_000),
		CLIMB_WALL(85_300), //Comp Bot
		CLIMB_AWAY(79421.875) //Comp Bot
		//CLIMB_WALL(42_000), //Practice Bot
		//CLIMB_AWAY(42_000), //Practice Bot
		;
		
		double ticks = 0;
		ElevatorPosition(double ticks){this.ticks = ticks;}
		double ticks() {return this.ticks;};
	}
	
	void newUpdate() {

		
		if(prefs.getBoolean("compbot", Robot.compbot)) {
			//comp bot
			currentPos = eMotorA.getSelectedSensorPosition(0);
		}
		else {
			//prac bot
			currentPos = eMotorA.getSelectedSensorPosition(0);
		}
		
		switch(mode) {
		case MANUALPOSITION:
			Utilities.clamp(elevatorPos, 0, maxPos);
			eVelocity = FB.FB(elevatorPos, currentPos, 0.005);
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

		if(prefs.getBoolean("compbot", Robot.compbot)) {
			// nothing is done in here
			//eVelocity = -eVelocity; //maybE?
		}
		else {
		}
		
		if(prefs.getBoolean("compbot", Robot.compbot)) {
			eVelocity *= 1;			
		}
		else {
		}
		
		eMotorA.set(ControlMode.PercentOutput, eVelocity);
		bind();

		SmartDashboard.putNumber("Elevator Current Position", currentPos);
		SmartDashboard.putNumber("Elevator Desired Position", elevatorPos);
		SmartDashboard.putNumber("Elevator Voltage", eMotorA.getMotorOutputVoltage());
		SmartDashboard.putNumber("Elevator Velocity", -eVelocity);
		SmartDashboard.putBoolean("Elevator Limit Switch is pressed", !LimitSwitch.get());
	}
}
