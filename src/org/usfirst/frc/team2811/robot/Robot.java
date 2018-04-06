/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.Auto.AutoSelector;
import org.usfirst.frc.team2811.robot.Auto.AutoSequence;
import org.usfirst.frc.team2811.robot.Auto.Center;
import org.usfirst.frc.team2811.robot.Auto.CenterVer2;
import org.usfirst.frc.team2811.robot.Auto.CenterVer3;
import org.usfirst.frc.team2811.robot.Auto.Example;
import org.usfirst.frc.team2811.robot.Auto.SideCrossScale;
import org.usfirst.frc.team2811.robot.Auto.SideEscape;
import org.usfirst.frc.team2811.robot.Auto.SideScale;
import org.usfirst.frc.team2811.robot.Auto.SideScaleVer2;
import org.usfirst.frc.team2811.robot.Auto.SideScaleVer3;
import org.usfirst.frc.team2811.robot.Auto.SideSwitch;
import org.usfirst.frc.team2811.robot.Auto.Testing1;
import org.usfirst.frc.team2811.robot.Elevator.ElevatorPosition;
import org.usfirst.frc.team2811.robot.Chassis.Mode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	public static Elevator elevator = new Elevator();
	public static Intake intake = new Intake();
	public static Chassis drive = new Chassis();
	public static Climber climber = new Climber();
	Lighting lighting = new Lighting();
	public OI oi = new OI();
	static boolean compbot = true;
	
	AutoSequence autoChoice = new SideEscape();
	AutoSelector autoSelector = new AutoSelector();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		//Only do this if we need to for things to work consistently, which may be the case
		//CameraServer.getInstance().startAutomaticCapture();

		drive.resetEnc();
		elevator.reset();
		climber.resetEnc();
		autoSelector.putSmartDashboard();
		SmartDashboard.putString("Climb Range", "83500 - 87500");
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {		
		//Figure out the optimal auto sequence to perform
		autoChoice = autoSelector.getBestAuto();
		//autoChoice = new CenterVer3(true); // COMMENT OUT BEFORE GOING TO THE FIELD
		
		// Do auto mode initialization 
		drive.shiftLow();
		intake.tiltBackward(false);
		elevator.setMode(Elevator.Mode.MANUALPOSITION);
		elevator.resetTo(ElevatorPosition.AUTO_STARTUP);
		elevator.setPos(ElevatorPosition.SWITCH);
		intake.squeezeOpen(false);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		// Run the auto we selected. It will then command the various subsystems indirectly
		autoChoice.run();
		
		// Run our subsystem update sequences
		drive.newUpdate();
		elevator.newUpdate();
		intake.newUpdate();
	}
	
	/** Ensure robot is ready for human operator in matches */
	public void teleopInit() {
		oi.climberEngaged = false;
		oi.climberSequence.cancel();
		CameraServer.getInstance().startAutomaticCapture();
		drive.resetEnc();
		drive.setMode(Chassis.Mode.ARCADE);
		drive.setMode(Mode.ARCADE);
		elevator.setMode(Elevator.Mode.MANUALPOSITION);
		intake.tiltBackward(false);
		climber.setMode(Climber.Mode.MANUAL);
	}


	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		
		// Handle all the user inputs, and apply any changes to the appropriate system
		oi.update();
		
		// Because OI now sets any configuration changes, these functions no longer care about stick inputs
		// They just do whatever they were told if they even have anything to do at this point.
		drive.newUpdate();
		elevator.newUpdate();
		intake.newUpdate();
		climber.newUpdate();
		
//		SmartDashboard.putNumber("Timer Per Loop",timer1.timer)
		
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		lighting.testPeriodic();
		intake.tiltBackward(true);
		intake.squeezeOpen(false);
	}
	
	public void disabledPeriodic() {
		// Constantly update and print our auto-selection
		// to see the auto we'll execute before enabling
		autoSelector.putSmartDashboard();
		autoChoice=autoSelector.getBestAuto();
		
		drive.disabledPeriodic();
		elevator.disabledPeriodic();
		intake.disabledPeriodic();
		climber.disabledPeriodic();
		
		SmartDashboard.putBoolean("Am I Compbot?", Preferences.getInstance().getBoolean("compbot", Robot.compbot));
	}
	
}
