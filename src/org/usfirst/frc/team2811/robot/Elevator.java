package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.Robot.RobotLocation;
import org.usfirst.frc.team2811.robot.Robot.ScaleConfig;
import org.usfirst.frc.team2811.robot.Robot.SwitchConfig;
import org.usfirst.frc.team2811.robot.Robot.TargetLocation;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2811.robot.FB;

/**
 * Class using WPI_TalonSRX to move Elevator w/ joystick and by setting a position.
 * Inputs: eMotor address
 * Outputs: eVelocity, elevatorPos 
 */

public class Elevator extends RobotModule {
	
	 WPI_TalonSRX eMotor = new WPI_TalonSRX(12);
	 double eVelocity = 0;
	 double elevatorPos = 0; //Used as the position you want the elevator to go to.
	 
	 double floorPos = 0.0;         //
	 double portalPos = 10000;      //
	 double switchPos = 20000;      // Set heights (estimated) for each location the elevator needs to get to.
	 double scaleLowPos = 25000;    //
	 double scaleHighPos = 30000;   //
	 
	 double autoPosition = 0.0; //Where you want to go to during auto.
	 double currentPos = 0.0;
	 double midBP = 45000/2;
	 
	//FB.FB() replaces feedBack()
	
	public enum Mode{ 
		MANUALVELOCITY, MANUALPOSITION, BUTTON,  //Used to change how the elevator is controlled
	}
	
	public Mode mode = Mode.MANUALPOSITION; 
	
	public void changeMode (Mode newMode) {
		mode = newMode;
	}
		
	void init(){
		reset();
	}
	
	void update(Joystick driver1,Joystick driver2, Joystick functions1) { //Only using functions1
		 double breakpoint = 0.0;
		currentPos = eMotor.getSelectedSensorPosition(0);
		
		if(mode == Mode.MANUALVELOCITY) {
			
			if(currentPos >= 45000 && eVelocity > 0) { //Keeps elevator from going too high.
				eVelocity = 0; 
			}
			else if(currentPos <= 0 && eVelocity < 0) { //Keeps elevator from going too low.
				eVelocity = 0; 
			}
			else {
				eVelocity = functions1.getY();
			}
			
		
		}
		/*else if(mode == Mode.BUTTON) {
			
			//eMotor.set(ControlMode.PercentOutput, FB.FB(elevatorPos, eMotor.getSelectedSensorPosition(0), 0.02)); 
			eVelocity = FB.FB(elevatorPos, currentPos, 0.02);
			//Takes an inputed position and adjusts the velocity to get there.
			//Last value needs to be tested and adjusted.
			
		}*/
		
		else if(mode == Mode.MANUALPOSITION) {
			
			
			elevatorPos = ( (functions1.getY()-(-1)) / (1-(-1)) * (45000-0) + 0 ); //Maps controller y-axis to elevator position.
			
			if(elevatorPos < midBP && currentPos > midBP) {
				breakpoint = midBP;
			}
			else if(elevatorPos > midBP && currentPos < midBP) {
				breakpoint = midBP;
			}
			else {
				breakpoint = elevatorPos;
				
			}
			
			eVelocity = FB.FB(breakpoint, currentPos, 0.002);
				
			//figure out "real" elevator position, as above
			//figure out the "fake" elevator position, at the breakpoints
			//Set fb to the nearest breakpoint
			//if close to the breakpoint, then we can move to the next breakpoint down or "real" position
			
			//eVelocity =  FB.FB(elevatorPos, currentPos, 0.02);
			
		}
		
		eMotor.set(ControlMode.PercentOutput, -eVelocity);
		SmartDashboard.putNumber("Current Position", eMotor.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Breakpoint", breakpoint);
		SmartDashboard.putNumber("Desired Position", elevatorPos);
		
		
	}
	
	/*public double moveJoystick (double stickValue) { 
		eVelocity = stickValue;
		return eVelocity;
	}*/
	
	public void reset() {
		eMotor.setSelectedSensorPosition(0, 0, 20); //First argument is desired position, second is the type of loop? (0 or 1), third is the timeout.
		
	}
	
	/*public double moveToPos (double inputPosition) { 
		elevatorPos = inputPosition;
		return elevatorPos;
	}*/
	
	void autoInit(RobotLocation robotLocation, TargetLocation targetLocation, SwitchConfig switchConfig, ScaleConfig scaleConfig) { //Elevator only cares about targetLocation
		eMotor.setSelectedSensorPosition(3000, 0, 20);
		if(targetLocation == TargetLocation.SWITCH) {
			autoPosition = switchPos;
		}
		if(targetLocation == TargetLocation.SCALE) {
			autoPosition = scaleHighPos;
		}
		if(targetLocation == TargetLocation.MOVE_ONLY) {
			autoPosition = switchPos;
		}
		elevatorPos = switchPos;
	}
	
	void auto(int stepAuto, double time) {
		if(time > 3 && stepAuto == 3) {
			elevatorPos = autoPosition;
		}
		eMotor.set(ControlMode.PercentOutput, FB.FB(elevatorPos, eMotor.getSelectedSensorPosition(0), 0.02));
	}
	
	
	

}
