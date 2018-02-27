package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.Robot.RobotLocation;
import org.usfirst.frc.team2811.robot.Robot.ScaleConfig;
import org.usfirst.frc.team2811.robot.Robot.SwitchConfig;
import org.usfirst.frc.team2811.robot.Robot.TargetLocation;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2811.robot.FB;

/**
 * Class using WPI_TalonSRX to move Elevator w/ joystick and by setting a position.
 * Inputs: eMotor address
 * Outputs: eVelocity, elevatorPos 
 */

public class Elevator extends RobotModule {
	
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
	 }
	 
	
	public enum Mode{ 
		MANUALVELOCITY, MANUALPOSITION, BUTTON, HOMING //Used to change how the elevator is controlled
	}
	
	public Mode mode = Mode.MANUALVELOCITY;
	private boolean homed=false; 
	
	public void changeMode (Mode newMode) {
		mode = newMode;
	}
		
	void init(){
		//reset();
	}
	
	
	public void disabledPeriodic(){
		//update constants and stuff from flash
		maxPos = prefs.getDouble("elevatorTopLimit", 93000);
		
	}
	
	void update(Joystick driver1,Joystick driver2, Joystick functions1) { //Only using functions1
		
		double breakpoint = 0.0;
		
		//TODO: new joystick needs to be plugged in with slider at zero, or moved, otherwise it reads incorrectly
		// Fix should be to make sure that the joystick input is -1 before enabling the elevator
		if(prefs.getBoolean("compbot", false)) {
			//comp bot
			currentPos = eMotor.getSelectedSensorPosition(0);
		}
		else {
			//prac bot
			currentPos = -eMotor.getSelectedSensorPosition(0);
		}
		double stickValue = -functions1.getRawAxis(3);

		if(functions1.getRawButtonPressed(11)) {
			if(mode == Mode.MANUALPOSITION) {
				mode = Mode.MANUALVELOCITY;
			}
			else if(mode == Mode.MANUALVELOCITY) {
				mode = Mode.MANUALPOSITION;
			}
			else {
				//this shouldn't happen
				mode = Mode.MANUALPOSITION;
			}
		}
		
		if(mode == Mode.HOMING) {
			if(!LimitSwitch.get()) {
				eVelocity = 0.1;
			}
			else{
				mode = Mode.MANUALPOSITION;
				homed = true;
				reset();
			}
		}
		
		else if(mode == Mode.MANUALVELOCITY) {
			
			eVelocity = stickValue;

			
			
		
		}
		//Position setlist, currently unused
		/*else if(mode == Mode.BUTTON) {
			
			eVelocity = FB.FB(elevatorPos, -currentPos, 0.02);
			//Takes an inputed position and adjusts the velocity to get there.
			//Last value needs to be tested and adjusted.
			
		}*/
		
		else if(mode == Mode.MANUALPOSITION) {
			
			
			elevatorPos = ( (stickValue-(-1)) / (1-(-1)) * (maxPos-minPos) + minPos ); //Maps controller y-axis to elevator position.
			
			//Not applicable to practice robot, not used
			/*if(elevatorPos < midBP && currentPos > midBP) {
				breakpoint = midBP;
			}
			else if(elevatorPos > midBP && currentPos < midBP) {
				breakpoint = midBP;
			}
			else {
				breakpoint = elevatorPos;
				
			}*/
			
			eVelocity = FB.FB(elevatorPos, currentPos, 0.007);			
			
		}
		
		//Invert motor phase. The Talon command to do this does not seem to work.
		if(currentPos >= maxPos && eVelocity > 0) { //Keeps elevator from going too high.
			eVelocity = 0; 
		}
		else if(!LimitSwitch.get()) {
			reset();
			homed = true;
			if(eVelocity < 0) {
				eVelocity = 0;
			}
		}
		else if(functions1.getRawButton(3)) {
			eVelocity = -0.5;
		}
		else if(currentPos <= softLimit && eVelocity < 0) { //Keeps elevator from going too low.
			eVelocity = 0; 
		}
		else {
		}
		
	
		//Set Motor phase, currently not needed 
		//eVelocity = -eVelocity;
		
		
		eMotor.set(ControlMode.PercentOutput, eVelocity);
		SmartDashboard.putNumber("Elevator Current Position", currentPos);
		SmartDashboard.putNumber("Breakpoint", breakpoint);
		SmartDashboard.putNumber("Desired Position", elevatorPos);
		SmartDashboard.putNumber("Voltage", eMotor.getOutputCurrent());
		SmartDashboard.putNumber("Joystick Position", functions1.getY());
		SmartDashboard.putNumber("elevatorVelocity", eVelocity);
		SmartDashboard.putBoolean("LimitSwitch", LimitSwitch.get());
		
	}
		
	public void reset() {
		eMotor.setSelectedSensorPosition(0, 0, 20); //First argument is desired position, second is the type of loop? (0 or 1), third is the timeout.
		
	}
		
	void autoInit(RobotLocation robotLocation, TargetLocation targetLocation, SwitchConfig switchConfig, ScaleConfig scaleConfig) { //Elevator only cares about targetLocation
		eMotor.setSelectedSensorPosition(0, (int) initializePos, 20);
		eMotor.setSelectedSensorPosition((int) initializePos, 0, 20);// maybe just in case? Shouldn't do anything.
		
		if(robotLocation == RobotLocation.CENTER) {
			autoActiveStep = 3;
			autoActiveTime = 1;
		}
		else {
			autoActiveStep = 1;
			autoActiveTime = 5.5;
		}
		
		elevatorPos=initializePos;
		if(targetLocation == TargetLocation.SWITCH) {
			autoPosition = switchPos; // bug fix 201802222021  =scaleHighPos
		}
		else if(targetLocation == TargetLocation.SCALE) {
			autoPosition = scaleHighPos;
		}
		else if(targetLocation == TargetLocation.MOVE_ONLY) {
			autoPosition = switchPos;
		}
		else {
			autoPosition = initializePos;		
		}
	}
	
	void auto(int stepAuto, double time) {
		
		if(time > autoActiveTime && stepAuto == autoActiveStep) {
			elevatorPos = autoPosition;
		}
		
		//TODO may need to set these motor phases
		
		if(prefs.getBoolean("compbot", false)) {
			//comp bot
			SmartDashboard.putNumber("ElevatorCurrentPos", eMotor.getSelectedSensorPosition(0));
			eMotor.set(ControlMode.PercentOutput, FB.FB(elevatorPos, eMotor.getSelectedSensorPosition(0), 0.01));
		}
		else {
			//prac bot
			eMotor.set(ControlMode.PercentOutput, FB.FB(elevatorPos, -eMotor.getSelectedSensorPosition(0), 0.01));
			SmartDashboard.putNumber("ElevatorCurrentPos", -eMotor.getSelectedSensorPosition(0));
		}
			
		SmartDashboard.putNumber("ElevatorAutoPos", autoPosition);
		SmartDashboard.putNumber("Elevatorvelocity", eMotor.getMotorOutputPercent());
		SmartDashboard.putNumber("ElevatorPos", elevatorPos);
		
	}
	
	
	

}
