package org.usfirst.frc.team2811.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team2811.robot.Elevator.Mode;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Climber {
	
	WPI_TalonSRX mtr1;
	WPI_TalonSRX mtr2;
	Preferences prefs = Preferences.getInstance();
	
	public Climber() {
		if(prefs.getBoolean("compbot", false)) {
			mtr1 = new WPI_TalonSRX(10);
			mtr2 = new WPI_TalonSRX(9);
		}
		else {
			mtr1 = new WPI_TalonSRX(10);
		}
	}
	
	enum Mode{INITIALCLIMB, ENDINGCLIMB, DISABLED, MANUAL}
	Mode mode = Mode.DISABLED;
	private double power = 0;
	public void setMode(Mode newMode) {
		mode = newMode;
	}
	public Mode getMode () {
		return mode;
	}
			
	
	public void setPower(double power) {
		this.power = power;
	}
	
	void newUpdate() {
//		void newUpdate(Joystick driver1,Joystick driver2, Joystick stick) {
		
		switch(mode) {
		
		case INITIALCLIMB:
			
			break;
		
		case ENDINGCLIMB:
			
			break;
			
		case MANUAL:
			
			break;
		
		case DISABLED:
			mtr1.set(ControlMode.PercentOutput, 0);
			return;

		default:
			
			break;
		}
		
		

		mtr1.set(ControlMode.PercentOutput, power); //replace getY with fixed value on robot
		SmartDashboard.putNumber("ClimberPower", power);
	}
}
