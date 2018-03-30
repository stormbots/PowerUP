package org.usfirst.frc.team2811.robot.Auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoSelector {
	
	private SendableChooser<RobotLocation> startPosition = new SendableChooser<>();
	private SendableChooser<Boolean> switchAbility = new SendableChooser<>();
	private SendableChooser<Boolean> scaleAbility = new SendableChooser<>();
	private SendableChooser<TargetLocation> locationPreference = new SendableChooser<>();

	public enum RobotLocation{LEFT, RIGHT,CENTER, AUTO};
	public enum TargetLocation{SWITCH, SCALE, MOVE_ONLY};
	public enum SwitchConfig{UNKNOWN, LEFT, RIGHT};
	public enum ScaleConfig{UNKNOWN, LEFT, RIGHT};
	public enum TeamColor{RED, BLUE};
		
	public RobotLocation robotLocation = RobotLocation.AUTO; 
	public TargetLocation targetLocation = TargetLocation.SCALE;
	public SwitchConfig switchConfig = SwitchConfig.UNKNOWN;
	public ScaleConfig scaleConfig = ScaleConfig.UNKNOWN;
	
	public AutoSelector() {
		putSmartDashboard();
	}
	
	public void putSmartDashboard() {		
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
		
		locationPreference.addDefault("prefer scale", TargetLocation.SCALE);
		locationPreference.addObject("prefer switch", TargetLocation.SWITCH);
		SmartDashboard.putData("Preference", locationPreference);
	}
	
	/**
	 * Calculate the auto to use based on the DriverStation or FMS 
	 * field data, and print it to the SmartDashboard
	 * @return the optimal 
	 */
	public AutoSequence getBestAuto() {
		String data = DriverStation.getInstance().getGameSpecificMessage();
		AutoSequence auto = getBestAuto(data);
		SmartDashboard.putString("Optimal Auto", auto.getClass().getSimpleName()+" ("+data+")");
		return auto;
	};
	
	/**
	 * Calculate the best auto using a provided string
	 * Expects either blank string "" if FMS not connected, or 
	 * String of length 3, containing any combination of 'L' or 'R' characters
	 * @param fieldData ,  eg "LLL", "RRR", "RLR" "LRL"
	 * @return
	 */
	public AutoSequence getBestAuto(String fieldData) {
		/*
		 * Parse the field data string
		 */
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
		
		/*
		 * Figure out where the robot is placed on the field. 
		 * By default, we use the FMS indicator
		 */
		robotLocation = startPosition.getSelected();
			if(robotLocation==RobotLocation.AUTO) {
				switch(DriverStation.getInstance().getLocation()) {
				case 1: robotLocation = RobotLocation.LEFT;break;
				case 2: robotLocation = RobotLocation.CENTER;break;
				case 3: robotLocation = RobotLocation.RIGHT;break;
				}
			}

		/*
		 * Calculate the auto based on the switch and scale, 
		 * taking into account drive team's preferences 
		 */
		if(robotLocation == RobotLocation.CENTER) {
			targetLocation = TargetLocation.SWITCH;
			if(switchConfig == SwitchConfig.LEFT) {
				return new CenterNewVer(true);
			}
			else {
				return new CenterNewVer(false);
			}
		}
		else if(robotLocation == RobotLocation.LEFT) {
			if(scaleConfig == ScaleConfig.LEFT && switchConfig == SwitchConfig.LEFT) {
				if(switchAbility.getSelected()==true && scaleAbility.getSelected()==true) {
					targetLocation = locationPreference.getSelected();
					if(targetLocation == TargetLocation.SWITCH) {
						return new SideSwitch(true);
					}
					if(targetLocation == TargetLocation.SCALE) {
						return new SideScale(true);
					}
				}
				else if(switchAbility.getSelected()==true){
					targetLocation = TargetLocation.SWITCH;
					return new SideSwitch(true); 
				}
				else if(scaleAbility.getSelected()==true){
					targetLocation = TargetLocation.SCALE;
					return new SideScale(true);
				}
				else {
					targetLocation = TargetLocation.MOVE_ONLY;
					return new SideCrossScale(true);
				}
			}
			else if(scaleConfig == ScaleConfig.LEFT && scaleAbility.getSelected()==true) {
				targetLocation = TargetLocation.SCALE;
				return new SideScale(true);
			}
			else if(switchConfig == SwitchConfig.LEFT && switchAbility.getSelected()==true) {
				targetLocation = TargetLocation.SWITCH;
				return new SideSwitch(true);
			}
			else {
				targetLocation = TargetLocation.MOVE_ONLY;
				return new SideCrossScale(true);
			}
		}
		
		/*
		 * Same as above, but for RIGHT instead of LEFT
		 */
		else if(robotLocation == RobotLocation.RIGHT){
			if(scaleConfig == ScaleConfig.RIGHT && switchConfig == SwitchConfig.RIGHT) {
				if(switchAbility.getSelected()==true && scaleAbility.getSelected()==true) {
					targetLocation = locationPreference.getSelected();
					if(targetLocation == TargetLocation.SWITCH) {
						return new SideSwitch(false);
					}
					if(targetLocation == TargetLocation.SCALE) {
						return new SideScale(false);
					}
				}
				else if(switchAbility.getSelected()==true){
					targetLocation = TargetLocation.SWITCH;
					return new SideSwitch(false);
				}
				else if(scaleAbility.getSelected()==true){
					targetLocation = TargetLocation.SCALE;
					return new SideScale(false);
				}
				else {
					targetLocation = TargetLocation.MOVE_ONLY;
					return new SideCrossScale(false);
				}
			}
			else if(scaleConfig == ScaleConfig.RIGHT && scaleAbility.getSelected()==true) {
				targetLocation = TargetLocation.SCALE;
				return new SideScale(false);
			}
			else if(switchConfig == SwitchConfig.RIGHT && switchAbility.getSelected()==true) {
				targetLocation = TargetLocation.SWITCH;
				return new SideSwitch(false);
			}
			else {
				targetLocation = TargetLocation.MOVE_ONLY;
				return new SideCrossScale(false);
			}
		}
		
		/*
		 * If we somehow managed to get invalid data, or some other failure state
		 * just move forward
		 */
		return new SideEscape();
		
//		SmartDashboard.putString("FieldData", fieldData.toString());
//		SmartDashboard.putString("robotLocation", robotLocation.toString());
//		SmartDashboard.putString("targetLocation", targetLocation.toString());
//		SmartDashboard.putString("switchConfig", switchConfig.toString());
//		SmartDashboard.putString("scaleConfig", scaleConfig.toString());
	};
	
	

}
