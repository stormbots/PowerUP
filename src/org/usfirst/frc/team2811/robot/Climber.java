package org.usfirst.frc.team2811.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team2811.robot.Elevator.Mode;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Climber {
	
	WPI_TalonSRX mtr1;
	Preferences prefs = Preferences.getInstance();
	
	public Climber() {
		if(prefs.getBoolean("compbot", Robot.compbot)) {
			mtr1 = new WPI_TalonSRX(10);
		}
		else {
			mtr1 = new WPI_TalonSRX(10);
		}
		resetEnc();
	}
	
	public enum Mode{CLOSEDLOOP, DISABLED, MANUAL}
	Mode mode = Mode.CLOSEDLOOP;
	private double power = 0;
	//private double maxClimbHeight = 124162;//works with lots of slack
	private double maxClimbHeight = 94_000+8000;
	//private double maxClimbHeight = 4000;
	private double targetPosition = 0;
		
	public void setMode(Mode newMode) {
		mode = newMode;
	}
	public Mode setMode () {
		return mode;
	}
	
	public void resetEnc() {
		mtr1.setSelectedSensorPosition(0, 0, 20);
	}
	
	public void setPower(double power) {
		this.power = power;
	}
	
	void newUpdate() {
//		void newUpdate(Joystick driver1,Joystick driver2, Joystick stick) {
		SmartDashboard.putNumber("ClimberTargetPos", targetPosition);
		SmartDashboard.putNumber("ClimberCurrentPos", mtr1.getSelectedSensorPosition(0));
		SmartDashboard.putString("ClimberMode", mode.toString());
		
		switch(mode) {
		
		case CLOSEDLOOP:
			
			//positive motor output generates negative direction on climber
			power = FB.FB(targetPosition, mtr1.getSelectedSensorPosition(0), 0.005);
			
			break;
	
	
		case MANUAL:
			
			break;
		
		case DISABLED:
			mtr1.set(ControlMode.PercentOutput, 0);
			return;

		default:
			break;
		}
		
		//Make a positive power everywhere else correspond to the "up" direction
		if(prefs.getBoolean("compbot", Robot.compbot)) {
			power = -power ; 
		}
		else {
			power = -power ; 
		}
			
		mtr1.set(ControlMode.PercentOutput, power); //replace getY with fixed value on robot
				
		SmartDashboard.putNumber("ClimberPower", power);
		SmartDashboard.putNumber("ClimberPosition", mtr1.getSelectedSensorPosition(0));
	}
	
	public void disabledPeriodic(){
		SmartDashboard.putNumber("Climber Position (disabled)", mtr1.getSelectedSensorPosition(0));
	}
	
	
	
 	public double getClimberPosition() {
		//-1..1
 		double position = Utilities.lerp(mtr1.getSelectedSensorPosition(0), 0, maxClimbHeight,-1,1);
 		return Utilities.clamp(position, -1,1);
	}
	
 	/** 
 	 * Accepts value between -1..1 representing all the way up and down
 	 * @param position
 	 */
	public void setPosition(double position) {
		position = Utilities.clamp(position,-1,1);
		position = Utilities.lerp(position, -1,1,0,maxClimbHeight );
		this.targetPosition = position;
	}

	boolean detached = false; 
	public void detach() {
		if(detached) return;
		detached = true;
		//TODO: May need to reverse first and second arguments?
		mtr1.setSelectedSensorPosition(0, -6_000, 20);
	}

}
