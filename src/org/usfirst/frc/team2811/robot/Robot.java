/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.Robot.RobotLocation;
import org.usfirst.frc.team2811.robot.Robot.ScaleConfig;
import org.usfirst.frc.team2811.robot.Robot.SwitchConfig;
import org.usfirst.frc.team2811.robot.Robot.TargetLocation;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	
	Joystick stickDrive1 = new Joystick(1);
	Joystick stickDrive2 = new Joystick(2);
	Joystick stickFunctions = new Joystick(3);
	RobotModule elevator = new RobotModule();
	RobotModule intake = new RobotModule();
	Chassis drive = new Chassis();
	RobotModule climber = new RobotModule();
	
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();

	public static enum RobotLocation{LEFT, RIGHT,CENTER};
	public static enum TargetLocation{SWITCH, SCALE, MOVE_ONLY};
	public static enum SwitchConfig{UNKNOWN, LEFT, RIGHT};
	public static enum ScaleConfig{UNKNOWN, LEFT, RIGHT};
	public static enum TeamColor{RED, BLUE};
	
	public CXTIMER timer = new CXTIMER();
	public int step = 0;
	
	RobotLocation robotLocation = RobotLocation.CENTER; 
	TargetLocation targetLocation = TargetLocation.SWITCH;
	public static SwitchConfig switchConfig = SwitchConfig.UNKNOWN;
	public static ScaleConfig scaleConfig = ScaleConfig.UNKNOWN;
	
	static String fieldData = "";
	
	static double delayTime = 0;
	static boolean deliverCube = true;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		//TODO: Parse the field string into the appropriate values for use in auto commands
		// robotLocation = RobotLocation.CENTER
		// targetLocation = TargetLocation.SWITCH
				
		//get field data
		
		if(fieldData.length()>0){
			if(fieldData.charAt(0)=='L') {
				switchConfig = SwitchConfig.LEFT;
			}
			else {
				switchConfig = SwitchConfig.RIGHT;
			}
			
			if(fieldData.charAt(1)=='L') {
				scaleConfig = ScaleConfig.LEFT;
			}
			else {
				scaleConfig = ScaleConfig.RIGHT;
			}
		}
		else {}

		/*  THIS IS WHERE THE ROBOT CODE SENDS THE DATA TO THE MODULES
		elevator.autoInit(robotLocation, targetLocation, switchConfig, scaleConfig);
		intake.autoInit(robotLocation, targetLocation, switchConfig, scaleConfig);
		drive.autoInit(robotLocation, targetLocation, switchConfig, scaleConfig);
		climber.autoInit(robotLocation, targetLocation, switchConfig, scaleConfig);
		*/
		
		drive.autoInit(robotLocation, targetLocation, switchConfig, scaleConfig);
		
		m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		switch(step) {
		
		case 0:
			if(timer.ckTime(true, 3)) {
				step++;
				elevator.auto(step, timer.getTimeSec());
				intake.auto(step, timer.getTimeSec());
				drive.auto(step, timer.getTimeSec());
				climber.auto(step, timer.getTimeSec());
				//set up and start Case 1
				}
			else {
				
				elevator.auto(step, timer.getTimeSec());
				intake.auto(step, timer.getTimeSec());
				drive.auto(step, timer.getTimeSec());
				climber.auto(step, timer.getTimeSec());
				//keep at Case 0
			}
			break;
				
		case 1:
			if(timer.ckTime(true, 3)) {
				step++;
				elevator.auto(step, timer.getTimeSec());
				intake.auto(step, timer.getTimeSec());
				drive.auto(step, timer.getTimeSec());
				climber.auto(step, timer.getTimeSec());
				//set up and start Case 2
			}
			else {
				elevator.auto(step, timer.getTimeSec());
				intake.auto(step, timer.getTimeSec());
				drive.auto(step, timer.getTimeSec());
				climber.auto(step, timer.getTimeSec());
				//keep at Case 1
			}
			break;
			
		case 2:
			if(timer.ckTime(true, 3)) {
				step++;
				elevator.auto(step, timer.getTimeSec());
				intake.auto(step, timer.getTimeSec());
				drive.auto(step, timer.getTimeSec());
				climber.auto(step, timer.getTimeSec());
				//set up and start Case 3
			}
			else {
				elevator.auto(step, timer.getTimeSec());
				intake.auto(step, timer.getTimeSec());
				drive.auto(step, timer.getTimeSec());
				climber.auto(step, timer.getTimeSec());
				//keep at Case 2
			}
			break;
			
		case 3:
			if(timer.ckTime(true, 3)) {
				step++;
				elevator.auto(step, timer.getTimeSec());
				intake.auto(step, timer.getTimeSec());
				drive.auto(step, timer.getTimeSec());
				climber.auto(step, timer.getTimeSec());
				//set up and start Case 4
			}
			else {
				elevator.auto(step, timer.getTimeSec());
				intake.auto(step, timer.getTimeSec());
				drive.auto(step, timer.getTimeSec());
				climber.auto(step, timer.getTimeSec());
				//keep at Case 3
			}
			break;
			
		case 4:
			if(timer.ckTime(true, 3)) {
				step++;
				elevator.auto(step, timer.getTimeSec());
				intake.auto(step, timer.getTimeSec());
				drive.auto(step, timer.getTimeSec());
				climber.auto(step, timer.getTimeSec());
				//set up and start Case 5
			}
			else {
				elevator.auto(step, timer.getTimeSec());
				intake.auto(step, timer.getTimeSec());
				drive.auto(step, timer.getTimeSec());
				climber.auto(step, timer.getTimeSec());
				//keep at Case 4
			}
			break;
			
		case 5:
			if(timer.ckTime(true, 3)) {
				step++;
				elevator.auto(step, timer.getTimeSec());
				intake.auto(step, timer.getTimeSec());
				drive.auto(step, timer.getTimeSec());
				climber.auto(step, timer.getTimeSec());
				//set up and start Default
			}
			else {
				elevator.auto(step, timer.getTimeSec());
				intake.auto(step, timer.getTimeSec());
				drive.auto(step, timer.getTimeSec());
				climber.auto(step, timer.getTimeSec());
				//keep at Case 5
			}
			break;
			
		default:
			elevator.auto(step, timer.getTimeSec());
			intake.auto(step, timer.getTimeSec());
			drive.auto(step, timer.getTimeSec());
			climber.auto(step, timer.getTimeSec());
			//keep at default
			break;
		}
	
	}
	public void teleopInit() {
		
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		elevator.update(stickDrive1,stickDrive2,stickFunctions);
		intake.update(stickDrive1,stickDrive2,stickFunctions);
		drive.update(stickDrive1,stickDrive2,stickFunctions);
		climber.update(stickDrive1,stickDrive2,stickFunctions);

	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
