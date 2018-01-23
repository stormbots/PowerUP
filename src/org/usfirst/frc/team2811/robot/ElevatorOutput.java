package org.usfirst.frc.team2811.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class ElevatorOutput {
	
	 TalonSRX eMotor = new TalonSRX(14);
	 double eVelocity = 0;
	 double elevatorPos = 0;
	
	private double feedBack (double V, double P, double K){
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
	
	public Mode mode = Mode.MANUAL;
		
	public void update(){
		if(mode == Mode.MANUAL) {
			if(eMotor.getSensorCollection().getQuadraturePosition() >= 10000 && eVelocity > 0) { //Keeps elevator from going too high.
				eVelocity = 0;
			}
			if(eMotor.getSensorCollection().getQuadraturePosition() <= 0 && eVelocity < 0) { //Keeps elevator from going too low; is this necessary/does this cause problems?
				eVelocity = 0;
			}
			eMotor.set(ControlMode.PercentOutput, eVelocity);	
		}
		
		else if(mode == Mode.BUTTON) {
			eMotor.set(ControlMode.PercentOutput, feedBack(elevatorPos, eMotor.getSensorCollection().getQuadraturePosition(), 0.0019)); //Last value needs to be tested and adjusted.
		}
		
	}
	
	public double moveJoystick (double stickValue) { //Sets velocity of motor anywhere from 1 to -1 based on position of joystick.
		eVelocity = stickValue;
		return eVelocity;
	}
	
	public void reset() {
		eMotor.getSensorCollection().setQuadraturePosition(0, 10); //This sets position to zero. Is this favorable over limit switches?
	}
	
	public double moveToPos (double inputPosition) {
		elevatorPos = inputPosition;
		return elevatorPos;
	}
	

}
