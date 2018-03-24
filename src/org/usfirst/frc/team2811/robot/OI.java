package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.Elevator.Mode;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;

enum ClimberMode{ENABLED, DISABLED}

public class OI {
	
	ClimberMode climberMode = ClimberMode.DISABLED;
	boolean intakeOpen = false;
	boolean tiltedBack = false;
	double turnScaleValue = 0.75;
	Joystick driver = new Joystick(0);
	Joystick functions = new Joystick(3);
	Preferences prefs = Preferences.getInstance();
	OI(){

	}
	
	
	void update() {
		
		//DRIVE
		Robot.drive.arcadeDrive(
			driver.getRawAxis(3)*-1,
			driver.getRawAxis(0)*turnScaleValue,
			true);
		
		if(driver.getRawButton(8)) {
			Robot.drive.shiftHigh();
		}
		else {
			Robot.drive.shiftLow(); 
		}
		
		
		
		//ELEVATOR
		//note this slider is +1 at bottom -1 at top
		Robot.elevator.setPos(-functions.getRawAxis(3));
		Robot.elevator.setVel(-functions.getRawAxis(3));
		
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
		

		
		//CLIMBER
		if(functions.getRawButtonPressed(5)) {
			// enable climber mode
		}
		if(functions.getRawButtonReleased(5)) {
			// disable climber mode or go to climber mode stage 2? 
		}
		
		//DEBUG FOR HOLDING
		if(functions.getRawButton(5)) {
			Robot.climber.setPower(functions.getRawAxis(1));
		}
			
		//hit a button to enable climber mode
		if(functions.getRawButton(8)) {
			Robot.elevator.setMaxHeight(Elevator.ElevatorPosition.CLIMB);
			Robot.climber.setMode(Climber.Mode.CLOSEDLOOP);
			Robot.climber.setPosition(1);
			climberMode = ClimberMode.ENABLED;
		}
		else if(functions.getRawButton(12)) {
			Robot.elevator.setMaxHeight(Elevator.ElevatorPosition.SCALEHIGH);
			Robot.climber.setMode(Climber.Mode.DISABLED);
			climberMode = ClimberMode.DISABLED;
		}
		
		if(climberMode == ClimberMode.ENABLED) {
			double height = Robot.climber.getClimberPosition();
			Robot.elevator.setPos(height);
		}
		else {
			//probably do nothing here.
		}
		
		//INTAKE
		if(functions.getRawButton(1)) {
			Robot.intake.grabCube();
		}
		else if(functions.getRawButton(4)) {
			Robot.intake.ejectCube();
		}
		else {
			Robot.intake.stopMotor();
		}
		
		if(functions.getRawButtonPressed(2)) {
			if(intakeOpen == false) {
				Robot.intake.squeezeOpen(true); // TODO The action for this should actually toggle.
				intakeOpen = true;
			}
			else if(intakeOpen == true) {
				Robot.intake.squeezeOpen(false); //Do we need to create a separate function for closing the intake?
				intakeOpen = false;
			}
		}
		
		if(functions.getRawButtonPressed(10)) {
			if(tiltedBack == true) {
				Robot.intake.tiltBackward(false);
				tiltedBack = false;	
			}
			else if(tiltedBack == false) {
				Robot.intake.tiltBackward(true);
				tiltedBack =  true;
			}
		}		
		
	}
}
