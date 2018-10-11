package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.Elevator.Mode;
import org.usfirst.frc.team2811.robot.ClimberSequence.ClimberModeVer1;
import org.usfirst.frc.team2811.robot.ClimberSequence.ClimberModeVer2;
import org.usfirst.frc.team2811.robot.ClimberSequence.ClimberModeVer3;
import org.usfirst.frc.team2811.robot.ClimberSequence.ClimberModeVer4;
import org.usfirst.frc.team2811.robot.ClimberSequence.ClimberSequence;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class OI {
	double turnScaleValue = 0.75;
	Joystick driver = new Joystick(0);
	Joystick functions = new Joystick(3);
	Preferences prefs = Preferences.getInstance();
	
	ClimberSequence climberSequence = new ClimberModeVer4();
	boolean climberEngaged = false;
	
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
		

				
		//INTAKE
		if(functions.getRawButton(1)) {
			Robot.intake.grabCube();
		}
		else if(functions.getRawButton(4)) {
			Robot.intake.ejectCube();
		}
		else if(functions.getRawButton(9)) {
			Robot.intake.ejectCubeSlow();
		}
		else {
			Robot.intake.stopMotor();
		}
		
		if(functions.getRawButtonPressed(2)) {
			System.out.println("Pressed the button for intake!");
			Robot.intake.squeezeToggle();
		}
		
		if(functions.getRawButtonPressed(10)) {
			Robot.intake.tiltToggle();
		}
		
		if(Timer.getMatchTime() <= 1 && Timer.getMatchTime() >= 0) {
			Robot.intake.tiltBackward(true);
			Robot.intake.squeezeOpen(false);
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
		if(functions.getRawButtonPressed(8)) {
			climberSequence.init();
			climberEngaged = true;
		}
		if(climberEngaged) {
			climberSequence.run();
		}
		if(functions.getRawButtonPressed(12)) {
			climberEngaged = false;
			climberSequence.cancel();
		}

	}
}
