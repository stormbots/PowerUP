package org.usfirst.frc.team2811.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class Climber extends RobotModule {
	
	WPI_TalonSRX mtr1 = new WPI_TalonSRX(12);
		
	void update(Joystick driver1,Joystick driver2, Joystick stick) {
		if(stick.getRawButton(3)) {
		mtr1.set(ControlMode.PercentOutput, stick.getY());
		}
		else {
			mtr1.set(ControlMode.PercentOutput, 0);
		}
		SmartDashboard.putNumber("ClimberCurrent", mtr1.getOutputCurrent());
		
	}
}
