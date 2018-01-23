package org.usfirst.frc.team2811.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ElevatorOutput {
	
	 WPI_TalonSRX eMotor = new WPI_TalonSRX(12);
	 double eVelocity = 0;
	 double elevatorPos = 0;
	
	private double feedBack (double V, double P, double K){ //Used
		double S= 1;
		if(V<P) S = -1;
		double Vx = S*K*Math.sqrt(Math.abs(V-P));
		if(Vx>1)Vx=1;
		if(Vx<-1)Vx=-1;
		return Vx;	
	}
	
	public enum Mode{ //Just to switch in code (no buttons).
		MANUAL, BUTTON,
	}
	
	public Mode mode = Mode.MANUAL; //Switch between Manual and Button mode here.
	
		
	public void update(){
		if(mode == Mode.MANUAL) {
			if(eMotor.getSelectedSensorPosition(0) >= 45000 && eVelocity > 0) { //Keeps elevator from going too high.
				eVelocity = 0; //If elevator too high, set velocity to zero.
			}
			if(eMotor.getSelectedSensorPosition(0) <= 0 && eVelocity < 0) { //Keeps elevator from going too low; is this necessary/does this cause problems?
				eVelocity = 0; //If elevator too low, set velocity to zero.
			}
			eMotor.set(ControlMode.PercentOutput, eVelocity); //If elevator is within the set positions, use the joystick for velocity.
			//eMotor.setSelectedSensorPosition(5000, 0, 20);
		}
		
		else if(mode == Mode.BUTTON) {
			eMotor.set(ControlMode.PercentOutput, feedBack(elevatorPos, eMotor.getSelectedSensorPosition(0), 0.0019)); 
			
			//Takes an inputed position and adjusts the velocity to get there.
			//Last value needs to be tested and adjusted.
		}
		SmartDashboard.putNumber("Position", eMotor.getSelectedSensorPosition(0));
	}
	
	public double moveJoystick (double stickValue) { //Sets velocity of motor anywhere from 1 to -1 based on position of joystick.
		eVelocity = stickValue;
		return eVelocity;
	}
	
	public void reset() {
		eMotor.setSelectedSensorPosition(0, 0, 20);
		//eMotor.getSensorCollection().setQuadraturePosition(5000, 10); //This sets position to zero. Is this favorable over limit switches?
	}
	
	public double moveToPos (double inputPosition) { //Allows a position to be set in the main code
		elevatorPos = inputPosition;
		return elevatorPos;
	}
	

}
