package org.usfirst.frc.team2811.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

public class Intake {
	//motors
	WPI_TalonSRX motor1 = new WPI_TalonSRX(12); //8
	WPI_TalonSRX motor2 = new WPI_TalonSRX(13); //9
	//pneumatics
	Solenoid squeezeSolenoid = new Solenoid(1);
	Solenoid tiltSolenoid = new Solenoid(2); 
	
	public Intake() {
		//sets the motors to work together and starts the pnuematics off
		motor2.follow(motor1);
		motor2.setInverted(true);
		squeezeSolenoid.set(false);
		tiltSolenoid.set(false);
	}
	
	void input() {
		//motors move to take in cube
		motor1.set(ControlMode.PercentOutput, 0.50);
	}
	
	void output() {
		//motors move to spew cube outward 
		motor1.set(ControlMode.PercentOutput, -0.50);
	}
	
	void stop() {
		motor1.set(ControlMode.PercentOutput, 0);
		squeezeSolenoid.set(false);
		tiltSolenoid.set(false);
	}
	
	void squeeze() {
		squeezeSolenoid.set(true);
	}
	
	void letgo() {
		squeezeSolenoid.set(false);
	}
	
	void tilt() { 
		tiltSolenoid.set(true);
	}
	
	void untilt() {
		tiltSolenoid.set(true);
	}
	
	void update(Joystick stick){
		//update area for use in main robot code
		if (stick.getRawButton(1)) {
			input();
		}
		else if (stick.getRawButton(2)) {
			output();
		}
		else if(stick.getRawButton(3)) {
			squeeze();
		}
		else if(stick.getRawButton(4)) {
			letgo();
		}
		else if(stick.getRawButton(5)) {
			tilt();
		}
		else if(stick.getRawButton(6)) {
			untilt();
		}
		else if(stick.getRawButton(7)) {
			//debug for input/output
			motor1.set(stick.getY());
		}
		else {
			stop();
		}	
	}
}