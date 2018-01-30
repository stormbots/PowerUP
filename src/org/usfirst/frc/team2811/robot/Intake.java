
package org.usfirst.frc.team2811.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

/** INTAKE CLASS
 * SUMMARY- The update method is set in the main code, and sets all the actions to each button 
 * which is called into the Robot.java. The 5 main actions are input, output, stop, squeeze, and 
 * tilt (5 buttons). The constructor at the beginning intiates some code needed for the code to properly run.
 *  
 * INPUTS-Two Cantalons (12 and 13 for testing, 8 and 9 for competitions).
 * 		  Two Solenoids (1 and 2 for testing, unknown ID for competitions).
 * 		  One Infared DigitalInput (1 for testing, unknown ID for competitions).
 * OUTPUTS-Taking in Powercubes and holding it (squeeze), outputing the Powercubes, and tilting the base up and down.
 * @author StormBots
 */
public class Intake extends RobotModule {
	WPI_TalonSRX motor1 = new WPI_TalonSRX(12); 
	WPI_TalonSRX motor2 = new WPI_TalonSRX(13); 
	Solenoid squeezeSolenoid = new Solenoid(1);
	Solenoid tiltSolenoid = new Solenoid(2);
	DigitalInput redEye = new DigitalInput(4);
	Boolean squeezeRun = false;
	Boolean tiltRun = false;
	Boolean deployCube = false;
	
	
	/**INTAKE CONSTRUCTOR
	 * Constructor used to set some code up that drives the motors opposite directions
	 * and turns Pneumatics off for saftey reasons.
	 */
	public Intake() {
		//motor2.follow(motor1);
		//motor2.setInverted(true);
		//motor1.follow(motor2);
		//motor1.setInverted(true);
		squeezeSolenoid.set(false);
		tiltSolenoid.set(false);
	}
	
	
	/**STOP METHOD
	 * Sets all parts (motor and solenoids) to "stop" or 0 (or false)
	 */
	void stop() {
		motor1.set(ControlMode.PercentOutput, 0);
		motor2.set(ControlMode.PercentOutput, 0);
		squeezeSolenoid.set(false);
		tiltSolenoid.set(false);
	}
	
	/**AUTO INIT METHOD
	 *Sets deployCube to deploy so that we can either set deploy =true/false
	 *in case we aren't using Auto code later on.
	 * @param deploy
	 */
	public void autoInit(Boolean deploy) {
		deployCube=deploy;
		
	}
	/**AUTO
	 *adds steps 4 and 5 of autonomous, the dropping the cube off in the 
	 *scale steps.
	 * @param step
	 * @param time
	 */
	public void auto(int step, double time) {
		if (step==4 && deployCube) {
			motor1.set(ControlMode.PercentOutput, -0.50);
			motor2.set(ControlMode.PercentOutput, 0.50);
		}
		if (step==5) {
			motor1.set(ControlMode.PercentOutput, 0);
			motor2.set(ControlMode.PercentOutput, 0);
		}
	}
	
	/**TELEOP INIT
	 *Nothing currently.
	 */
	public void init() {
		
	}
	
	/**UPDATE (TELEOP PERIODIC) METHOD
	 *Sets each "action" to a specified button that can be pulled into 
	 *the Robot.java as Intake.update(); for teleop control. Squeeze and 
	 *Tilt are toggle buttons while the intake and output need to be held
	 *down.
	 * @param stick
	 */
	void update(Joystick stick){
		//get cube
		if (stick.getRawButton(1) && redEye.get()) {
			motor1.set(ControlMode.PercentOutput, 0.50);
			motor2.set(ControlMode.PercentOutput, -0.50);
		}
		//put cube
		else if (stick.getRawButton(2)) {
			motor1.set(ControlMode.PercentOutput, -0.50);
			motor2.set(ControlMode.PercentOutput, 0.50);
		}
		else {
			motor1.set(ControlMode.PercentOutput, 0);
			motor2.set(ControlMode.PercentOutput, 0);
		}
				
		//squeeze toggle 
		if(stick.getRawButtonPressed(3)) {
			squeezeRun = ! squeezeRun;
		}
		if(squeezeRun) {
			squeezeSolenoid.set(true);
		}
		else {
			squeezeSolenoid.set(false);
		}
		
		
		//tilt toggle
		if(stick.getRawButtonPressed(4)) {
			tiltRun = !tiltRun;
		}
		if(tiltRun) {
			tiltSolenoid.set(true);
		}
		else {
			tiltSolenoid.set(false);
		}
		
		
		//debug Y-axis control
		if(stick.getRawButton(7)) {
			motor1.set(stick.getY());
			motor2.set(stick.getY());
		}
		//stop
		if (stick.getRawButtonPressed(8)) {
			stop();
		}
	}
}