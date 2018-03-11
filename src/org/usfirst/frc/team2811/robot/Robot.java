/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2811.robot;

import org.usfirst.frc.team2811.robot.Auto.AutoSequence;
import org.usfirst.frc.team2811.robot.Auto.Center;
import org.usfirst.frc.team2811.robot.Auto.Example;
import org.usfirst.frc.team2811.robot.Auto.SideEscape;
import org.usfirst.frc.team2811.robot.Auto.SideScale;
import org.usfirst.frc.team2811.robot.Auto.SideSwitch;
import org.usfirst.frc.team2811.robot.Elevator.ElevatorPosition;

import edu.wpi.first.wpilibj.CameraServer;
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
	
	Joystick stickDrive1 = new Joystick(0);
	Joystick stickDrive2 = new Joystick(2);
	Joystick stickFunctions = new Joystick(3);
	public static Elevator elevator = new Elevator();
	public static Intake intake = new Intake();
	public static Chassis drive = new Chassis();
	public static Climber climber = new Climber();
	Lighting lighting = new Lighting();
	public OI oi = new OI();
	AutoSequence autoChoice = new SideEscape();
		
	private SendableChooser<Integer> delaySelection =  new SendableChooser<>();
	private SendableChooser<RobotLocation> startPosition = new SendableChooser<>();
	private SendableChooser<Boolean> switchAbility = new SendableChooser<>();
	private SendableChooser<Boolean> scaleAbility = new SendableChooser<>();
	private SendableChooser<TargetLocation> locationPreference = new SendableChooser<>();
	
	
	CXTIMER autotimer = new CXTIMER();

	public static enum RobotLocation{LEFT, RIGHT,CENTER, AUTO};
	public static enum TargetLocation{SWITCH, SCALE, MOVE_ONLY};
	public static enum SwitchConfig{UNKNOWN, LEFT, RIGHT};
	public static enum ScaleConfig{UNKNOWN, LEFT, RIGHT};
	public static enum TeamColor{RED, BLUE};
		
	public static RobotLocation robotLocation = RobotLocation.AUTO; 
	public static TargetLocation targetLocation = TargetLocation.SCALE;
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
		CameraServer.getInstance().startAutomaticCapture();
		
		delaySelection.addDefault("0 (default)", 0);
		delaySelection.addObject("1", 1);
		delaySelection.addObject("2", 2);
		delaySelection.addObject("4", 4);
		delaySelection.addObject("6", 6);
		SmartDashboard.putData("Delay (sec)", delaySelection);
		
		startPosition.addDefault("field default", RobotLocation.AUTO);
		startPosition.addObject("left", RobotLocation.LEFT);
		startPosition.addObject("center", RobotLocation.CENTER);
		startPosition.addObject("right", RobotLocation.RIGHT);
		SmartDashboard.putData("Robot Position", startPosition);
		
		switchAbility.addDefault("yes (switch)", true);
		switchAbility.addObject("no (switch)", false);
		SmartDashboard.putData("Switch", switchAbility);
		
		scaleAbility.addDefault("yes (scale)", true);
		scaleAbility.addObject("no (scale)", false);
		SmartDashboard.putData("Scale", scaleAbility);
		
		locationPreference.addDefault("scale", TargetLocation.SCALE);
		locationPreference.addObject("switch", TargetLocation.SWITCH);
		SmartDashboard.putData("Preference", locationPreference);
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
		drive.setMode(Chassis.Mode.PROFILE);

		robotLocation = RobotLocation.CENTER;
		targetLocation = TargetLocation.SWITCH;
		autotimer.Update();
		autotimer.reset();
		
		//elevator.resetScaleTo(ElevatorPosition.AUTO_STARTUP);

		
		fieldData = DriverStation.getInstance().getGameSpecificMessage();
		SmartDashboard.putString("fieldstepup", fieldData);
				
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
		
		robotLocation = startPosition.getSelected();
			if(robotLocation==RobotLocation.AUTO) {
				switch(DriverStation.getInstance().getLocation()) {
				case 1: robotLocation = RobotLocation.LEFT;break;
				case 2: robotLocation = RobotLocation.CENTER;break;
				case 3: robotLocation = RobotLocation.RIGHT;break;
				}
			}

		// Determine driver strategy
		if(robotLocation == RobotLocation.CENTER) {
			targetLocation = TargetLocation.SWITCH;
			if(robotLocation == RobotLocation.LEFT) {
				autoChoice = new Center(true);
			}
			else {
				autoChoice = new Center(false);
			}
		}
		
		else if(robotLocation == RobotLocation.LEFT) {
			if(scaleConfig == ScaleConfig.LEFT && switchConfig == SwitchConfig.LEFT) {
				if(switchAbility.getSelected()==true && scaleAbility.getSelected()==true) {
					targetLocation = locationPreference.getSelected();
					if(targetLocation == TargetLocation.SWITCH) {
						autoChoice = new SideSwitch(true);
					}
					if(targetLocation == TargetLocation.SCALE) {
						autoChoice = new SideScale(true);
					}
				}
				else if(switchAbility.getSelected()==true){
					targetLocation = TargetLocation.SWITCH;
					autoChoice = new SideSwitch(true); 
				}
				else if(scaleAbility.getSelected()==true){
					targetLocation = TargetLocation.SCALE;
					autoChoice = new SideScale(true);
				}
				else {
					targetLocation = TargetLocation.MOVE_ONLY;
					autoChoice = new SideEscape();
				}
			}
			else if(scaleConfig == ScaleConfig.LEFT && scaleAbility.getSelected()==true) {
				targetLocation = TargetLocation.SCALE;
				autoChoice = new SideScale(true);
			}
			else if(switchConfig == SwitchConfig.LEFT && switchAbility.getSelected()==true) {
				targetLocation = TargetLocation.SWITCH;
				autoChoice = new SideSwitch(true);
			}
			else {
				autoChoice = new SideEscape();
				targetLocation = TargetLocation.MOVE_ONLY;
			}
		}
		
		else if(robotLocation == RobotLocation.RIGHT){
			if(scaleConfig == ScaleConfig.RIGHT && switchConfig == SwitchConfig.RIGHT) {
				if(switchAbility.getSelected()==true && scaleAbility.getSelected()==true) {
					targetLocation = locationPreference.getSelected();
					if(targetLocation == TargetLocation.SWITCH) {
						autoChoice = new SideSwitch(false);
					}
					if(targetLocation == TargetLocation.SCALE) {
						autoChoice = new SideScale(false);
					}
				}
				else if(switchAbility.getSelected()==true){
					targetLocation = TargetLocation.SWITCH;
					autoChoice = new SideSwitch(false);
				}
				else if(scaleAbility.getSelected()==true){
					targetLocation = TargetLocation.SCALE;
					autoChoice = new SideScale(false);
				}
				else {
					autoChoice = new SideEscape();
					targetLocation = TargetLocation.MOVE_ONLY;
				}
			}
			else if(scaleConfig == ScaleConfig.RIGHT && scaleAbility.getSelected()==true) {
				targetLocation = TargetLocation.SCALE;
				autoChoice = new SideScale(false);
			}
			else if(switchConfig == SwitchConfig.RIGHT && switchAbility.getSelected()==true) {
				targetLocation = TargetLocation.SWITCH;
				autoChoice = new SideSwitch(false);
			}
			else {
				targetLocation = TargetLocation.MOVE_ONLY;
				autoChoice = new SideEscape();
			}
		}
		
		else {
			//auto select based off field
		}
		
		autoChoice.run();

		
		//else for l/r

		System.out.println(fieldData);
		System.out.println(robotLocation);
		System.out.println(targetLocation);
		System.out.println(switchConfig);
		System.out.println(scaleConfig);
		
		SmartDashboard.putString("FieldData", fieldData.toString());
		SmartDashboard.putString("robotLocation", robotLocation.toString());
		SmartDashboard.putString("targetLocation", targetLocation.toString());
		SmartDashboard.putString("switchConfig", switchConfig.toString());
		SmartDashboard.putString("scaleConfig", scaleConfig.toString());

		
		drive.newUpdate();
		intake.newUpdate();
		//elevator.newUpdate();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		autotimer.Update();

		// Run the auto we selected. It will then command the various subsystems indirectly
		drive.shiftLow();
		intake.tiltBackward(false);
		//elevator.setPos(ElevatorPosition.SWITCH);
		autoChoice.run();

		//SmartDashboard.putNumber("Step", );
		
		drive.newUpdate();
		//elevator.newUpdate();
		intake.newUpdate();
	}
	
	public void teleopInit() {
		drive.resetEnc();
		autotimer.Update();
		autotimer.reset();
		intake.init();
		drive.setMode(Chassis.Mode.ARCADE);
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

		// update any timer to update all timers.
		autotimer.Update();
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
		drive.disabledPeriodic();
		elevator.disabledPeriodic();
//		intake.disabledPeriodic();
		climber.disabledPeriodic();
	}
	
}
