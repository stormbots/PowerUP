package org.usfirst.frc.team2811.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
//!!import com.ctre.phoenix.motorcontrol.can.*;
//!!import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Climber extends RobotModule {
	
	//stick.getRawButton(5) (robot)
	//find positions
	
	Talon mtr1 = new Talon(1);
	Talon mtr2 = new Talon(2);
	//double TarPos;
	double TarVel;
	
	public void setVelocity(double velocity) {
		TarVel = velocity;
	}
		
	void update(Joystick driver1,Joystick driver2, Joystick stick) {
		mtr1.set(stick.getY());
		mtr2.set(stick.getY());
		//? SmartDashboard.putNumber("velocity", TarVel);
		//SmartDashboard.putNumber("position", TarPos);
		
	}
}
