package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.Robot.RobotLocation;
import org.usfirst.frc.team2811.robot.Robot.ScaleConfig;
import org.usfirst.frc.team2811.robot.Robot.SwitchConfig;
import org.usfirst.frc.team2811.robot.Robot.TargetLocation;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
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

	 double eVelocity = 0;
	 double elevatorPos = 0; //Used as the position you want the elevator to go to.
	 
	 double floorPos = 0.0;         //
	 double portalPos = 10000;      //
	 double switchPos = 20000;      // Set heights (estimated) for each location the elevator needs to get to.
	 double scaleLowPos = 25000;    //
	 double scaleHighPos = 30000;   //
	 double maxPos = 85000;
	 double minPos = 0;
	 double softLimit = -2000;
	 
	 double autoPosition = 0.0; //Where you want to go to during auto.
	 double currentPos = 0.0;
	 double midBP = 45000/2;
	 
	//FB.FB() replaces feedBack()
	
	public enum Mode{ 
		MANUALVELOCITY, MANUALPOSITION, BUTTON, HOMING //Used to change how the elevator is controlled
	}
	
	public Mode mode = Mode.MANUALPOSITION;
	private boolean homed=false; 
	
	public void changeMode (Mode newMode) {
		mode = newMode;
	}
		
	void init(){
		reset();
	}
	
	void update(Joystick driver1,Joystick driver2, Joystick functions1) { //Only using functions1
		double breakpoint = 0.0;
		//practice currentPos = -eMotor.getSelectedSensorPosition(0);
		currentPos = -eMotor.getSelectedSensorPosition(0);

		double stickValue = -functions1.getRawAxis(3);

		
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
			
			//eMotor.set(ControlMode.PercentOutput, FB.FB(elevatorPos, eMotor.getSelectedSensorPosition(0), 0.02)); 
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
		else if(functions1.getRawButton(5)) {
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
		SmartDashboard.putNumber("Current Position", currentPos);
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
		eMotor.setSelectedSensorPosition(0, 25000, 20);
		
		elevatorPos=25000;
		if(targetLocation == TargetLocation.SWITCH) {
			autoPosition = scaleHighPos;
		}
		else if(targetLocation == TargetLocation.SCALE) {
			autoPosition = scaleHighPos;
		}
		else if(targetLocation == TargetLocation.MOVE_ONLY) {
			autoPosition = switchPos;
		}
		else {
			autoPosition = 25000;		
		}
	}
	
	void auto(int stepAuto, double time) {
		if(time > 3 && stepAuto == 3) {
			elevatorPos = autoPosition;
		}
		
		//TODO may need to set these motor phases
		eMotor.set(ControlMode.PercentOutput, FB.FB(elevatorPos, -eMotor.getSelectedSensorPosition(0), 0.01));
		SmartDashboard.putNumber("ElevatorCurrentPos", -eMotor.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("ElevatorAutoPos", autoPosition);
		SmartDashboard.putNumber("Elevatorvelocity", eMotor.getMotorOutputPercent());
		SmartDashboard.putNumber("ElevatorPos", elevatorPos);
		
	}
	
	
	

}
