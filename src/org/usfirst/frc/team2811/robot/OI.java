package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.Elevator.Mode;

import edu.wpi.first.wpilibj.Joystick;

public class OI {
	
	boolean intakeOpen = false;
	double turnScaleValue = 0.75;
	Joystick driver = new Joystick(0);
	Joystick functions = new Joystick(3);
	OI(){

	}
	
	void update(){
		//Do the drive-related stuff
		
		//DRIVE SYSTEM
		Robot.drive.arcadeDrive(
			driver.getRawAxis(3),
			driver.getRawAxis(0)*turnScaleValue,
			true);
		
		if(driver.getRawButton(8)) {
			Robot.drive.shiftHigh();
		}
		else {
			Robot.drive.shiftLow(); 
		}
		
		
		
		//ELEVATOR
		Robot.elevator.setPos(functions.getRawAxis(3));
		Robot.elevator.setVel(functions.getRawAxis(3));
		
		if(functions.getRawButtonPressed(11)) {
			switch(Robot.elevator.getMode()) {
			case MANUALPOSITION:
				Robot.elevator.setMode(Mode.MANUALVELOCITY);
				break;
			case MANUALVELOCITY:
				Robot.elevator.setMode(Mode.MANUALPOSITION);
				break;
			default:
				Robot.elevator.setMode(Mode.MANUALPOSITION);
			}
		}
		
		if(functions.getRawButtonPressed(3)) {
			Robot.elevator.setMode(Mode.HOMING);
		}
		if(functions.getRawButtonReleased(3)) {
			Robot.elevator.setMode(Mode.MANUALPOSITION);
		}
		
//		if(Mode)
		
		// TODO: Check buttons, and execute the expected action
		// These are examples, please validate them! D:
		
		if(functions.getRawButtonPressed(2)) {
			if(intakeOpen = false) {
				Robot.intake.squeezeOpen(true); // TODO The action for this should actually toggle.
				intakeOpen = true;
			}
			else {
				Robot.intake.squeezeOpen(false); //Do we need to create a separate function for closing the intake?
				intakeOpen = false;
			}
		}
		
		//Hold to shift example
		
		
		
		
		
		
		
		
	}
}
