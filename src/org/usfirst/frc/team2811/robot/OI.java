package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.Elevator.Mode;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
			driver.getRawAxis(3)*-1,			 // make #3 for Lily ... make #1 for Caden
			driver.getRawAxis(0)*turnScaleValue, // make #0 for Lily ... make #2 for Caden
			true);
		
		if(driver.getRawButton(8)) {
			Robot.drive.shiftHigh();
		}
		else {
			Robot.drive.shiftLow(); 
		}
		
		if(driver.getRawButton(7)) { //check and make sure that this button is the left trigger, or changed for what Lily wants
			turnScaleValue = 1;
		}
		else {
			turnScaleValue = 0.75;
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
			Robot.climber.setMode(Climber.Mode.MANUAL);
			Robot.climber.setPower(functions.getRawAxis(1));
		}
		SmartDashboard.putNumber("climber velocity", functions.getRawAxis(1));
		
		//hit a button to enable climber mode
		if(functions.getRawButtonPressed(8)) { // note: a negative motor will make a posotive increase in position ticks
			//Robot.climber.detach();
			Robot.elevator.setMaxHeight(Elevator.ElevatorPosition.CLIMB);
			Robot.climber.setMode(Climber.Mode.CLOSEDLOOP);			
			Robot.climber.setPosition(1);
			climberMode = ClimberMode.ENABLED;
		}
		if(functions.getRawButton(12)) {
			Robot.elevator.setMaxHeight(Elevator.ElevatorPosition.SCALEHIGH);
			Robot.climber.setMode(Climber.Mode.DISABLED);
			climberMode = ClimberMode.DISABLED;
		}
		
		if(climberMode == ClimberMode.ENABLED) {
			double height = Robot.climber.getClimberPosition();
			Robot.elevator.setPos(height);
			SmartDashboard.putNumber("ClimberHeight", height);
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
			System.out.println("Pressed the button for intake!");
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
		
		if(Timer.getMatchTime() <= 1 && Timer.getMatchTime() >= 0) {
			Robot.intake.tiltBackward(true);
			Robot.intake.squeezeOpen(false);
		}
	}
}
