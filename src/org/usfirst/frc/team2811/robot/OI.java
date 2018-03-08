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
		Robot.drive.arcadeDrive(
			driver.getRawAxis(3),
			driver.getRawAxis(0)*turnScaleValue,
			true);
		
		Robot.elevator.setPos(functions.getRawAxis(3));
		Robot.elevator.setVel(functions.getRawAxis(3));
		
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
		if(driver.getRawButton(8)) {
			Robot.drive.shiftHigh();
		}
		else {
			Robot.drive.shiftLow(); 
		}
		
		if(functions.getRawButtonPressed(11)) {
			if(Robot.elevator.mode == Mode.MANUALPOSITION){
				Robot.elevator.changeMode(Mode.MANUALVELOCITY);
			}
			else if(Robot.elevator.mode == Mode.MANUALVELOCITY) {
				Robot.elevator.changeMode(Mode.MANUALPOSITION);
			}
			else {
				Robot.elevator.changeMode(Mode.MANUALPOSITION);
			}
		}
		
		
	}
}
