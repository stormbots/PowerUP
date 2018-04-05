 
package org.usfirst.frc.team2811.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake {
	WPI_TalonSRX motorL = new WPI_TalonSRX(6);
	WPI_TalonSRX motorR = new WPI_TalonSRX(7);
	
	//Now same on both robots! :D
	Solenoid squeezeSolenoidA = new Solenoid(6); 
	Solenoid squeezeSolenoidB = new Solenoid(7);

	Solenoid tiltSolenoidA = new Solenoid(0);
	Solenoid tiltSolenoidB = new Solenoid(1);
	
	DigitalInput redEye = new DigitalInput(0);
	Boolean intakeOpen = false;
	Boolean tiltedBack = true;
	Preferences prefs = Preferences.getInstance();
	
	//Invert these if the cylinders do the wrong thing
	Boolean squeezeInvert=true;
	Boolean tiltInvert=true;
	
	double velocity = 0.0;
	
	
	public Intake() {
		squeezeOpen(false);
		tiltBackward(true);
	}
	
	/** Fetch preferences and adjust variables as needed */
	public void disabledPeriodic() {
		if(prefs.getBoolean("compbot", Robot.compbot)){
			//Comp bot
			tiltInvert = false;
			squeezeInvert = false;
		}
		else {
			//Practice bot
			tiltInvert = false;
			squeezeInvert = false;
		}
	}

	
	public void ejectCube() {
		velocity = -0.5;
	}
	
	public void ejectCubeSlow() {
		velocity = -0.3;
	}
	
	public void grabCube() {
		velocity = 0.75;
	}
	
	public void stopMotor() {
		velocity = 0.0;
	}
	
	public void squeezeOpen(boolean open) {
		intakeOpen = open;
		if(open) {
			squeezeSolenoidA.set(!squeezeInvert);
			squeezeSolenoidB.set(squeezeInvert);
			
		}
		else {
			squeezeSolenoidA.set(squeezeInvert);
			squeezeSolenoidB.set(!squeezeInvert);
		}
	}
	
	public void squeezeToggle() {
		if(intakeOpen) {
			squeezeOpen(false);
			
		}
		else {
			squeezeOpen(true);
		}
	}
	
	
	public void tiltBackward(boolean backward) {
		tiltedBack = backward;
		if(backward) {
			tiltSolenoidA.set(!tiltInvert);
			tiltSolenoidB.set(tiltInvert);

		}
		else {
			tiltSolenoidA.set(tiltInvert);
			tiltSolenoidB.set(!tiltInvert);
		}
	}
	
	public void tiltToggle() {
		if(tiltedBack) {
			tiltBackward(false);
		}
		else {
			tiltBackward(true);
		}
	}
		
	void newUpdate() {
		motorL.set(ControlMode.PercentOutput, velocity);
		motorR.set(ControlMode.PercentOutput, -velocity);
		
		SmartDashboard.putNumber("Intake Velocity", velocity);
		SmartDashboard.putBoolean("Intake Has Cube", hasCube());   
		SmartDashboard.putBoolean("Intake Open", intakeOpen);
		SmartDashboard.putBoolean("Intake Tilted Back", tiltedBack);
	}

	/**
	 * Return cube state
	 * @return true if we have a cube
	 */
	public boolean hasCube() {
		return !redEye.get();
	}
	

}