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
		if(prefs.getBoolean("compbot", true)) {
			mtr1 = new WPI_TalonSRX(9);
			mtr2 = new WPI_TalonSRX(10);
			mtr2.follow(mtr1);
		}
		else {
			mtr1 = new WPI_TalonSRX(8);
			mtr2 = new WPI_TalonSRX(20); // does not exist
		}
	}
	
	enum Mode{INITIALCLIMB,ENDINGCLIMB,DISABLED}
	Mode mode = Mode.DISABLED;
	public void setMode(Mode newMode) {
		mode = newMode;
	}
	public Mode getMode () {
		return mode;
	}
	
	public void bind() {
		
	}
		
	void newUpdate(Joystick driver1,Joystick driver2, Joystick stick) {
		
		switch(mode) {
		
		case INITIALCLIMB:
			
			break;
		
		case ENDINGCLIMB:
			
			break;
			
		case DISABLED:
			
			break;
			
		default:
			
			break;
		}
		
		/*if(stick.getRawButton(5)) { //updated for new controller
			mtr1.set(ControlMode.PercentOutput, stick.getY()); //replace getY with fixed value on robot
			mtr2.set(ControlMode.PercentOutput, stick.getY());
		}
		else {
			mtr1.set(ControlMode.PercentOutput, 0);
			mtr2.set(ControlMode.PercentOutput, 0);
		}*/
		SmartDashboard.putNumber("ClimberCurrent", mtr1.getOutputCurrent());
		SmartDashboard.putNumber( "ClimberCurrent", mtr2.getOutputCurrent());
		
	}
}
