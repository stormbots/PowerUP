package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.Robot.RobotLocation;
import org.usfirst.frc.team2811.robot.Robot.TargetLocation;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Class using WPI_TalonSRX to move Elevator w/ joystick and by setting a position.
 * Inputs: eMotor address
 * Outputs: eVelocity, elevatorPos 
 */

public class ElevatorOutput extends RobotModule {
	
	 WPI_TalonSRX eMotor = new WPI_TalonSRX(12);
	 double eVelocity = 0;
	 double elevatorPos = 0;
	 double floorPos = 0.0;
	 double portalPos = 10000;
	 double switchPos = 20000;
	 double scaleLowPos = 25000;
	 double scaleHighPos = 30000;
	 double autoPosition = 0.0;
	
	 
	private double feedBack (double V, double P, double K){ 
		double S= 1;
		if(V<P) S = -1;
		double Vx = S*K*Math.sqrt(Math.abs(V-P));
		if(Vx>1)Vx=1;
		if(Vx<-1)Vx=-1;
		return Vx;	
	}
	
	public enum Mode{ 
		MANUALMOTOR, MANUALPOSITION, BUTTON,
	}
	
	public Mode mode = Mode.MANUALMOTOR; 
	
	public void changeMode (Mode newMode) {
		mode = newMode;
	}
		
	void update(Joystick driver1,Joystick driver2, Joystick functions1) {
		if(mode == Mode.MANUALMOTOR) {
			
			if(eMotor.getSelectedSensorPosition(0) >= 45000 && eVelocity > 0) { //Keeps elevator from going too high.
				eVelocity = 0; 
			}
			if(eMotor.getSelectedSensorPosition(0) <= 0 && eVelocity < 0) { //Keeps elevator from going too low.
				eVelocity = 0; 
			}
			eMotor.set(ControlMode.PercentOutput, eVelocity); //If elevator is within the set positions, use the joystick for velocity.
		
		}
		
		else if(mode == Mode.BUTTON) {
			
			eMotor.set(ControlMode.PercentOutput, feedBack(elevatorPos, eMotor.getSelectedSensorPosition(0), 0.02)); 
			
			//Takes an inputed position and adjusts the velocity to get there.
			//Last value needs to be tested and adjusted.
			
		}
		
		else if(mode == Mode.MANUALPOSITION) {
			
			elevatorPos = ( (functions1.getY()-(-1)) / (1-(-1)) * (45000-0) + 0 );
			eMotor.set(ControlMode.PercentOutput, feedBack(elevatorPos, eMotor.getSelectedSensorPosition(0), 0.02));
			
		}
		SmartDashboard.putNumber("Position", eMotor.getSelectedSensorPosition(0));
	}
	
	public double moveJoystick (double stickValue) { 
		eVelocity = stickValue;
		return eVelocity;
	}
	
	public void reset() {
		eMotor.setSelectedSensorPosition(0, 0, 20); //First argument is desired position, second is the type of loop? (0 or 1), third is the timeout.
		
	}
	
	public double moveToPos (double inputPosition) { 
		elevatorPos = inputPosition;
		return elevatorPos;
	}
	
	void autoInit(RobotLocation robotLocation, TargetLocation targetLocation,int delay, boolean deliverCube) {
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
		eMotor.set(ControlMode.PercentOutput, feedBack(elevatorPos, eMotor.getSelectedSensorPosition(0), 0.02));
	}
}
