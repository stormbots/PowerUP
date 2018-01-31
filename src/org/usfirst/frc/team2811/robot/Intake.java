//DOCUMENTATION NOT UP TO DATE DUE TO LAST MINUTE CHANGES TO CODE
package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.Robot.RobotLocation;
import org.usfirst.frc.team2811.robot.Robot.TargetLocation;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

/** INTAKE CLASS
 * SUMMARY- There are methods for each action and those are built into the update method, 
 * which is called into the Robot.java. The 7 main actions are input, output, stop, squeeze, letgo, 
 * tilt, and untilt. The constructor at the beginning intiates some code needed for the code to properly run.
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
	DigitalInput redEye = new DigitalInput(1);
	Boolean intakeRun = false;
	Boolean squeezeRun = false;
	Boolean tiltRun = false;
	
	/**INTAKE CONSTRUCTOR
	 * Constructor used to set some code up that drives the motors opposite directions
	 * and turns Pneumatics off for safety reasons.
	 */
	public Intake() {
		motor2.follow(motor1);
		motor2.setInverted(true);
		squeezeSolenoid.set(false);
		tiltSolenoid.set(false);
	}
	
	/**INPUT METHOD
	 * Sets motor speeds for grabbing Powercubes.
	 * Infared stops Powercube.
	 */
	void input() {
		motor1.set(ControlMode.PercentOutput, 0.50);
	}
	
	/**OUTPUT METHOD
	 * Sets motor speeds for spouting Powercubes out.
	 */
	void output() { 
		motor1.set(ControlMode.PercentOutput, -0.50);
	}
	
	/**STOP METHOD
	 * Sets motors and solenoids to stop "working".
	 */
	void stop() {
		motor1.set(ControlMode.PercentOutput, 0);
		squeezeSolenoid.set(false);
		tiltSolenoid.set(false);
	}
	
	/**SQUEEZE METHOD
	 * Sets solenoid to "squeeze" the Powercube.
	 */
	void squeeze() {
		squeezeSolenoid.set(true);
	}
	
	/**LETGO METHOD
	 *Sets solenoid to let go of the Powercube.
	 */
	void letgo() {
		squeezeSolenoid.set(false);
	}
	
	/**TILT METHOD
	 * Sets solenoid to tilt base of Intake upwards.
	 */
	void tilt() { 
		tiltSolenoid.set(true);
	}
	
	/**UNTILT METHOD
	 *Sets solenoid to tilt base of Intake downwards.
	 */
	void untilt() {
		tiltSolenoid.set(true);
	}
	
	/**UPDATE METHOD
	 *Sets each "action" to a specified button that can be pulled into 
	 *the Robot.java as Intake.update(); for teleop control. There is and extra action
	 *for manual debugging of motors.
	 * @param stick
	 */
	void update(Joystick driver1,Joystick driver2, Joystick stick) {
		//get cube
		if (stick.getRawButton(1)&&!redEye.get()) {
			input();
		}
		//put cube
		else if (stick.getRawButton(2)) {
			output();
		}
		else {
			stop();
		}
		//squeeze
		if(stick.getRawButton(3)) {
			squeezeRun=true;
		}
		//letgo
		if(stick.getRawButton(4)) {
			squeezeRun=false;
		}
		//tilt 
		if(stick.getRawButton(5)) {
			tiltRun=true;
		}
		//untilt
		if(stick.getRawButton(6)) {
			untilt();
		}
		//debug Y-axis control
		if(stick.getRawButton(7)) {
			motor1.set(stick.getY());
		}
		//stop
		if (stick.getRawButton(8)) {
			stop();
		}
	}
	
	void autoInit(RobotLocation robotLocation, TargetLocation targetLocation,int delay, boolean deliverCube) {

	}
	void auto(int step, double time) {
		
	}

}