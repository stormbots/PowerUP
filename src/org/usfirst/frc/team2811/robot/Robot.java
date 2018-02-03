/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2811.robot;

import edu.wpi.first.wpilibj.DriverStation;
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
	RobotModule elevator = new Elevator();
	RobotModule intake = new Intake();
	RobotModule drive = new Chassis();
	RobotModule climber = new Climber();
	
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	
	int astep = 0;
	CXTIMER autotimer = new CXTIMER();

	public static enum RobotLocation{LEFT,RIGHT,CENTER};
	public static enum TargetLocation{SWITCH,SCALE,MOVE_ONLY};
	public static enum TeamColor{RED,BLUE};

	RobotLocation robotLocation =  RobotLocation.CENTER;
	TargetLocation targetLocation =  TargetLocation.SWITCH;
	
	static String fieldData = "XXX";
	
	static double delayTime = 0;
	static boolean deliverCube = true;
	
	long step0timer = 4000;
	long step1timer = 4000;
	long step2timer = 3000;
	long step3timer = 3000;
	long step4timer = 3000;
	long step5timer = 3000;
	long step6timer = 3000;
	
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
		astep = 0;
		
		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		SmartDashboard.putString("fieldstepup", gameData);
		
		m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
		autotimer.Update();
		autotimer.reset();
		
		//Fix this 
		int delay = 0;
		elevator.autoInit(robotLocation, targetLocation, delay, deliverCube);
		drive.autoInit(robotLocation, targetLocation, delay, deliverCube);
		intake.autoInit(robotLocation, targetLocation, delay, deliverCube);
		
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		autotimer.Update();
		switch (astep) {
		case 0:
			if(autotimer.ckTime(true, step0timer)) {
				astep++;
				autotimer.reset();
			}
			drive.auto(astep,autotimer.getTimeSec());
			break;
		case 1:
			if(autotimer.ckTime(true, step1timer)) {
				astep++;
				autotimer.reset();
			}
			drive.auto(astep,autotimer.getTimeSec());
			break;
		case 2:
			if(autotimer.ckTime(true, step2timer)) {
				astep++;
				autotimer.reset();
			}
			drive.auto(astep,autotimer.getTimeSec());
			break;
		case 3:
			if(autotimer.ckTime(true, step3timer)) {
				astep++;
				autotimer.reset();
			}
			drive.auto(astep,autotimer.getTimeSec());
			elevator.auto(astep, autotimer.getTimeSec());
			intake.auto(astep, autotimer.getTimeSec());
			break;
		case 4:
			if(autotimer.ckTime(true, step4timer)) {
				astep++;
				autotimer.reset();
			}
			elevator.auto(astep, autotimer.getTimeSec());
			intake.auto(astep,autotimer.getTimeSec());
			break;
		case 5:
			if(autotimer.ckTime(true, step5timer)) {
				astep++;
				autotimer.reset();
			}
			break;
		case 6:
			if(autotimer.ckTime(true, step6timer)) {
				astep++;
				autotimer.reset();
			}
			drive.auto(astep,autotimer.getTimeSec());
			elevator.auto(astep, autotimer.getTimeSec());
			intake.auto(astep, autotimer.getTimeSec());
			break;
		default:
			drive.auto(astep,autotimer.getTimeSec());
			elevator.auto(astep, autotimer.getTimeSec());
			intake.auto(astep, autotimer.getTimeSec());
			break;
		}
		SmartDashboard.putNumber("Step", astep);
		
	}
	public void teleopInit() {
		drive.resetEnc();
		autotimer.Update();
		autotimer.reset();
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		autotimer.Update();
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
