package org.usfirst.frc.team2811.robot;

import edu.wpi.first.wpilibj.Joystick;

public class OI {

	double turnScaleValue = 0.75;
	Joystick driver = new Joystick(0);
	OI(){

	}
	
	void update(){
		//Do the drive-related stuff
		Robot.drive.arcadeDrive(
			driver.getRawAxis(3),
			driver.getRawAxis(0)*turnScaleValue,
			true);
		
		// TODO: Check buttons, and execute the expected action
		// These are examples, please validate them! D:
		
		if(driver.getRawButtonPressed(2)) {
			Robot.intake.squeezeOpen(true); // TODO The action for this should actually toggle.
		}
		
		//Hold to shift example
		if(driver.getRawButton(8)) {
			Robot.drive.shiftHigh();
		}
		else {
			Robot.drive.shiftLow(); 
		}
		
	}
}
