 
package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.Robot.RobotLocation;
import org.usfirst.frc.team2811.robot.Robot.ScaleConfig;
import org.usfirst.frc.team2811.robot.Robot.SwitchConfig;
import org.usfirst.frc.team2811.robot.Robot.TargetLocation;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/** INTAKE CLASS
 * SUMMARY- The update method is set in the main code, and sets all the actions to each button 
 * which is called into the Robot.java. The 4 main actions are input, output, squeeze, and 
 * tilt (4 buttons). The constructor at the beginning intiates some code needed for the code to properly run.
 *  
 * INPUTS-Two Cantalons (12 and 13 for testing, 8 and 9 for competitions) (controlled by double velocity).
 * 		  Two Solenoids (1 and 2 for testing, unknown ID for competitions).
 * 		  One Infared DigitalInput (4 for testing, unknown ID for competitions).
 * OUTPUTS-Taking in Powercubes and holding it (squeeze), outputing the Powercubes, and tilting the base up and down.
 * @author StormBots
 */
public class Intake extends RobotModule {
	WPI_TalonSRX motor1 = new WPI_TalonSRX(6);
	WPI_TalonSRX motor2 = new WPI_TalonSRX(7);
	
	Solenoid squeezeSolenoidA;
	Solenoid squeezeSolenoidB;	
	Solenoid tiltSolenoidA = new Solenoid(0);
	Solenoid tiltSolenoidB = new Solenoid(1);
	
	DigitalInput redEye = new DigitalInput(0);
	Boolean squeezing = false;
	Boolean tiltRun = true;
	Boolean deployCube = false;
	Preferences prefs = Preferences.getInstance();
	
	//Invert these if the cylinders do the wrong thing
	Boolean squeezeInvert=true;
	Boolean tiltInvert=true;
	
	double velocity = 0.0;
	
	
	/**INTAKE CONSTRUCTOR
	 * Constructor used to set some code up that drives the motors opposite directions
	 * and turns Pneumatics off for saftey reasons.
	 */
	public Intake() {
		
		motor2.follow(motor1);
		motor2.setInverted(true);
		if(prefs.getBoolean("compbot", false)){
			//comp bot
			squeezeSolenoidA = new Solenoid(4); 
			squeezeSolenoidB = new Solenoid(5);
		}
		else {
			squeezeSolenoidA = new Solenoid(6); 
			squeezeSolenoidB = new Solenoid(7);

		}

		squeezeOpen(false);
		tiltBackward(true);
	}
	
	public void squeezeOpen(boolean open) {
		if(open) {
			squeezeSolenoidA.set(squeezeInvert);
			squeezeSolenoidB.set(!squeezeInvert);
		}
		else {
			squeezeSolenoidA.set(!squeezeInvert);
			squeezeSolenoidB.set(squeezeInvert);
		}
	}
	
	public void tiltBackward(boolean backward) {
		if(backward) {
			tiltSolenoidA.set(tiltInvert);
			tiltSolenoidB.set(!tiltInvert);

		}
		else {
			tiltSolenoidA.set(!tiltInvert);
			tiltSolenoidB.set(tiltInvert);
		}
	}
	
	/**AUTO INIT METHOD
	 *Sets deployCube to deploy depending on whether we are move only (false)
	 *or anything else (true) (whether we are dropping cube or not).
	 * @param deploy
	 */
	void autoInit(
			RobotLocation robotLocation, 
			TargetLocation targetLocation, 
			SwitchConfig switchConfig, 
			ScaleConfig scaleConfig) {
		
		if(robotLocation==RobotLocation.LEFT && targetLocation==TargetLocation.MOVE_ONLY) {
			deployCube = false;
		}
		else if(robotLocation==RobotLocation.RIGHT && targetLocation==TargetLocation.MOVE_ONLY) {
			deployCube = false;
		}
		else {
			deployCube = true;
		}
	}
	
	/**AUTO
	 *adds steps 4 and 5 of autonomous, the dropping the cube off in the 
	 *scale steps.
	 * @param step
	 * @param time
	 */
	public void auto(int step, double time) {
		//possibly implement a step 3 code for opening elevator?
		//if (step==3 && deployCube) {
		//	
		tiltBackward(false);
		if(step==1) {
			tiltBackward(false);
			tiltRun=false;
		}
		
		if (step==4 && deployCube) {
			velocity = -0.5;
		}
		if (step==5) {
			velocity = 0;
		}
		motor1.set(ControlMode.PercentOutput, velocity);
	}
	
	/**TELEOP INIT
	 *Nothing currently.
	 */
	public void init() {
		tiltBackward(false);
	}
	
	/**UPDATE (TELEOP PERIODIC) METHOD
	 *Sets each "action" to a specified button that can be pulled into 
	 *the Robot.java as Intake.update(); for teleop control. Squeeze and 
	 *Tilt are toggle buttons while the intake and output need to be held
	 *down. (button 7 is for debug only). 
	 * @param stick
	 */
	void update(Joystick driver1,Joystick driver2, Joystick stick) {
		velocity = 0;

		
		//get cube
		if (stick.getRawButton(1) && redEye.get()) {
			velocity = 0.5;
		}
		//put cube
		else if (stick.getRawButton(4)) {
			velocity = -0.5;
		}
		else {
			velocity = 0;
		}
				
		//squeeze toggle 
		if(stick.getRawButtonPressed(2)) {
			squeezing = !squeezing;
		}
		if(squeezing) {
			squeezeOpen(false);
		}
		else {
			squeezeOpen(true);
		}
		
		//put cube hold into power saving 
		//if(redEye.get() && squeezeRun) {
			//velocity = 0.05;		
		//}
		//wrong voltage on practice bot
		
		//tilt toggle
		if(stick.getRawButtonPressed(10)) {
			tiltRun = !tiltRun;
		}
		if(tiltRun) {
			tiltBackward(false);
		}
		else {
			tiltBackward(true);
		}
		
		//debug Y-axis control
		/*if(stick.getRawButton(7)) {
			velocity = stick.getY();
		}*/
		
		motor1.set(ControlMode.PercentOutput,velocity);
		
		SmartDashboard.putNumber("Velocity", velocity);
		SmartDashboard.putBoolean("RedEye", redEye.get());   
		SmartDashboard.putBoolean("Squeeze Intake", squeezing);
		SmartDashboard.putBoolean("Tilt Base", tiltRun);
	}
	

	/**
	 * Return cube state
	 * @return 0 if no cube, 1 if cube
	 */
	public double getLightPattern() {
		if( redEye.get() ) return 1;
		return 0;
	}
}