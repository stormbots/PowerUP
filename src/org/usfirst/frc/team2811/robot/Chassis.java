package org.usfirst.frc.team2811.robot;

import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;


public class Chassis {
	WPI_TalonSRX leadL = new WPI_TalonSRX(12);//2
	Talon frontL = new Talon(1);
	Talon rearL = new Talon(2);
	//WPI_TalonSRX frontL = new WPI_TalonSRX(3);
	//WPI_TalonSRX rearL = new WPI_TalonSRX(4);
	WPI_TalonSRX leadR = new WPI_TalonSRX(13);//5
	Talon frontR = new Talon(3);
	Talon rearR = new Talon(4);
	//WPI_TalonSRX frontR = new WPI_TalonSRX(6);
	//WPI_TalonSRX rearR = new WPI_TalonSRX(7);
	DifferentialDrive driver = new DifferentialDrive(leadL, leadR);
	//Solenoid highGear = new Solenoid(3);
	
	
	public Chassis() {
		//initializes the slaves, inverts the right side drive chain
		bind();
	}

	
	public void bind() {
		frontL.set(leadL.get());
		rearL.set(leadL.get());
		frontR.set(leadR.get());
		rearR.set(leadR.get());
		// set slave talons
		//frontL.follow(leadL);
		//rearL.follow(leadL);
		//frontR.follow(leadR);
		//rearR.follow(leadR);
	}
	
	public void update(Joystick stick) {
		//updates the lead talons, then updates the slave talons
		driver.arcadeDrive(stick.getY(), stick.getX());
		bind();
	}
	/*
	public void shift() {
		// toggles the code
		highGear.set(!highGear.get());
	}
	*/
	
	
	// ---- Autonomous Stuff ----
	
	
	private double FB(double posActual, double posTarget, double K) {
		double S = 1;
		if(posActual > posTarget) {
			S = -1;
		}
		double VX = S*K*Math.sqrt(Math.abs(posActual-posTarget));
		if(VX > 1) {
			VX = 1;
		}
		if(VX < -1) {
			VX = -1;
		}
		return VX;
	}
	
	
	public void moveFwd() {
		leadL.setSelectedSensorPosition(2000, 0, 20);
		leadR.setSelectedSensorPosition(-2000, 0, 20);
	}	
}
