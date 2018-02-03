package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.Robot.RobotLocation;
import org.usfirst.frc.team2811.robot.Robot.TargetLocation;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This is a stub class that can replace any other module with a blank, 
 * non-operational version. 
 * 
 * <br/> 
 * 
 * The purpose of this class is to quickly eliminate modules that use hardware
 * not currently attached to the robot, which cause code failures due to failed 
 * communication. 
 * <br/>
 * Feel free to auto-add methods as needed to make sure that any classes being replaced 
 * by this module work appropriately. We'll never put code in here, just method stubs.
 * 
 * <br/> 
 * Usage:
 * <br/> 
 * For normal operation, replace module initializers as 
 * <br>
 * <pre>Chassis chassis = new Chassis()</pre> with 
 * <br>
 * <pre>RobotModule chassis = new Chassis()</pre>. Code should run normally. 
 * <br/>
 * To "remove" a module, replace the initializer with 
 * <br>
 * <pre>RobotModule chassis = new RobotModule()</pre>. <br>
 * <br>
 * 
 * @author stormbots
 *
 */
public class RobotModule {

	//These are standard methods used by all modules
	
	/**
	 * Run during during teleopInit
	 */
	void init() {
		
	};
	
	/**
	 * Run every cycle during teleopPeriodic
	 * @param joystick 
	 */
	void update(Joystick driver1,Joystick driver2, Joystick functions1) {
		
	};
	
	/**
	 * Run during teleopInit
	 */
	void autoInit(RobotLocation robotLocation, TargetLocation targetLocation,int delay, boolean deliverCube) {
		
	}
	
	/**
	 * Run every cycle during auto
	 */
	void auto(int step, double time) {
		
	}

	public void resetEnc() {
		// TODO Auto-generated method stub
		
	}
}
