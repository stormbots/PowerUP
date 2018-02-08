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
	RobotModule intake = new RobotModule();
	Chassis drive = new Chassis();
	RobotModule climber = new RobotModule();
	
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	
	private SendableChooser<Double> delaySelection =  new SendableChooser<>();
	private SendableChooser<RobotLocation> startPosition = new SendableChooser<>();
	private SendableChooser<Boolean> switchAbility = new SendableChooser<>();
	private SendableChooser<Boolean> scaleAbility = new SendableChooser<>();
	private SendableChooser<TargetLocation> locationPreference = new SendableChooser<>();
	
	int astep = 0;
	CXTIMER autotimer = new CXTIMER();

	public static enum RobotLocation{LEFT, RIGHT,CENTER};
	public static enum TargetLocation{SWITCH, SCALE, MOVE_ONLY};
	public static enum SwitchConfig{UNKNOWN, LEFT, RIGHT};
	public static enum ScaleConfig{UNKNOWN, LEFT, RIGHT};
	public static enum TeamColor{RED, BLUE};
	
	public int step = 0;
	
	public static RobotLocation robotLocation = RobotLocation.CENTER; 
	public static TargetLocation targetLocation = TargetLocation.SWITCH;
	public static SwitchConfig switchConfig = SwitchConfig.UNKNOWN;
	public static ScaleConfig scaleConfig = ScaleConfig.UNKNOWN;
	
	static String fieldData = "";
	
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
		
		delaySelection.addDefault("0 (default)", 0.0);
		delaySelection.addObject("1", 1.0);
		delaySelection.addObject("2", 2.0);
		delaySelection.addObject("4", 4.0);
		delaySelection.addObject("6", 6.0);
		SmartDashboard.putData("Delay (sec)", delaySelection);
		
		startPosition.addDefault("field default", RobotLocation.LEFT);
		startPosition.addObject("left", RobotLocation.LEFT);
		startPosition.addObject("center", RobotLocation.CENTER);
		startPosition.addObject("right", RobotLocation.RIGHT);
		SmartDashboard.putData("Robot Position", startPosition);
		
		switchAbility.addDefault("yes", true);
		switchAbility.addObject("no", false);
		SmartDashboard.putData("Switch", switchAbility);
		
		scaleAbility.addDefault("yes", true);
		scaleAbility.addObject("no", false);
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
		//TODO: Parse the field string into the appropriate values for use in auto commands
		// robotLocation = RobotLocation.CENTER
		// targetLocation = TargetLocation.SWITCH
		astep = 0;
		
		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		SmartDashboard.putString("fieldstepup", gameData);
				
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
		
		
		//set our auto delay time
		step0timer = delaySelection.getSelected().longValue()*1000;

		// Determine driver strategy
		if(robotLocation == RobotLocation.CENTER) {
			targetLocation = TargetLocation.SWITCH;
		}
		else if(robotLocation == RobotLocation.LEFT) {
			if(locationPreference.getSelected()==TargetLocation.SCALE ){
				
				if(scaleAbility.getSelected()==true && scaleConfig == ScaleConfig.LEFT) {
					targetLocation = TargetLocation.SCALE;
				}
				else if(switchAbility.getSelected()==true && switchConfig == SwitchConfig.LEFT) {
					targetLocation = TargetLocation.SWITCH;
				}
				else {
					targetLocation = TargetLocation.MOVE_ONLY;
				}
				
			}
			else if(locationPreference.getSelected()==TargetLocation.SWITCH) {
				
				if(switchAbility.getSelected()==true && switchConfig == SwitchConfig.LEFT) {
					targetLocation = TargetLocation.SWITCH;
				}
				else if(scaleAbility.getSelected()==true && scaleConfig == ScaleConfig.LEFT) {
					targetLocation = TargetLocation.SCALE;
				}
				else {
					targetLocation = TargetLocation.MOVE_ONLY;
				}
			}
		}
		
		else if(robotLocation == RobotLocation.RIGHT) {
			if(locationPreference.getSelected()==TargetLocation.SCALE ){
				
				if(scaleAbility.getSelected()==true && scaleConfig == ScaleConfig.RIGHT) {
					targetLocation = TargetLocation.SCALE;
				}
				else if(switchAbility.getSelected()==true && switchConfig == SwitchConfig.RIGHT) {
					targetLocation = TargetLocation.SWITCH;
				}
				else {
					targetLocation = TargetLocation.MOVE_ONLY;
				}
				
			}
			else if(locationPreference.getSelected()==TargetLocation.SWITCH) {
				
				if(switchAbility.getSelected()==true && switchConfig == SwitchConfig.RIGHT) {
					targetLocation = TargetLocation.SWITCH;
				}
				else if(scaleAbility.getSelected()==true && scaleConfig == ScaleConfig.RIGHT) {
					targetLocation = TargetLocation.SCALE;
				}
				else {
					targetLocation = TargetLocation.MOVE_ONLY;
				}
			}
		}
		
		else {
			//auto select based off field
		}
	
			
		
		
		
		/* delay = selecton of delay
		 * 
		 * 
		 * if position == center
		 * 		do we go to switch? 
		 * 
		 *  if position == left 
		 *  	if pref == scale
		 *  		if scale == yes && scaleConfig == left
		 *  			go to scale
		 *  		else if switch == yes and switchconfig == left
		 *  			go to switch
		 *  		else
		 *  			move only
		 *  
		 *  
		 * 
		 * 			
		 */
				
				
		
		

		/*  THIS IS WHERE THE ROBOT CODE SENDS THE DATA TO THE MODULES */
		elevator.autoInit(robotLocation, targetLocation, switchConfig, scaleConfig);
		intake.autoInit(robotLocation, targetLocation, switchConfig, scaleConfig);
		drive.autoInit(robotLocation, targetLocation, switchConfig, scaleConfig);
		climber.autoInit(robotLocation, targetLocation, switchConfig, scaleConfig);
		drive.autoInit(robotLocation, targetLocation, switchConfig, scaleConfig);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		autotimer.Update();

		// Handle timer and step progression
		// Anything that runs once on a timer edge should be added here
		switch (astep) {
		case 0:
			if(autotimer.ckTime(true, step0timer)) {
				astep++;
				autotimer.reset();
			}
			break;
		case 1:
			if(autotimer.ckTime(true, step1timer)) {
				astep++;
				autotimer.reset();
			}
			break;
		case 2:
			if(autotimer.ckTime(true, step2timer)) {
				astep++;
				autotimer.reset();
			}
			break;
		case 3:
			if(autotimer.ckTime(true, step3timer)) {
				astep++;
				autotimer.reset();
			}
			break;
		case 4:
			if(autotimer.ckTime(true, step4timer)) {
				astep++;
				autotimer.reset();
			}
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
			break;
		default:
			break;
		}
		
		//Handle continuous updates for various modules
		elevator.auto(step, autotimer.getTimeSec());
		intake.auto(step, autotimer.getTimeSec());
		drive.auto(step, autotimer.getTimeSec());
		climber.auto(step, autotimer.getTimeSec());

		SmartDashboard.putNumber("Step", astep);
	}
	
	public void teleopInit() {
		drive.resetEnc();
		autotimer.Update();
		autotimer.reset();
		elevator.init();
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
