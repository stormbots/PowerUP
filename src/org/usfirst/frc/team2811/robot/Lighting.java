package org.usfirst.frc.team2811.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Joystick;



import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.awt.List;
import java.lang.reflect.Array;

import org.usfirst.frc.team2811.robot.BlinkenPattern;

public class Lighting {
	Talon light = new Talon(4);
	BlinkenPattern pattern = BlinkenPattern.C12_END_TO_END_BLEND_C1_TO_C2;
	
	Preferences prefs = Preferences.getInstance();
	double pwmvalue=0.0;
	
	private SendableChooser<BlinkenPattern> selection = new SendableChooser<>();

	Lighting(){
		selection.addDefault(BlinkenPattern.BRIGHT_DEFAULT_BLUE.toString(), BlinkenPattern.BRIGHT_DEFAULT_BLUE);	
		BlinkenPattern[] coolPatterns = {
			  
		//POSSIBLE BLUE ALLIANCE COLORS
		BlinkenPattern.BLUE_SONIC,
		BlinkenPattern.WAVY_BLUE,
		BlinkenPattern.CODE_BLUE,
		BlinkenPattern.BLUE_LARSON,
		BlinkenPattern.LIGHT_BLUE,
		BlinkenPattern.BRIGHT_DEFAULT_BLUE,
		
		//POSSIBLE RED ALLIANCE COLORS
		BlinkenPattern.RED_SONIC,
		BlinkenPattern.RED_WHITE,
		BlinkenPattern.WAVY_RED,  
		BlinkenPattern.RED_LARSON,
		BlinkenPattern.CODE_RED,
		BlinkenPattern.SOLID_RED,
		
		//OTHER COLORS:
		BlinkenPattern.BLINKING_RAINBOW,
		BlinkenPattern.RAINBOW_SONIC,
		BlinkenPattern.RAINBOW_SONIC_V2,
		BlinkenPattern.GREEN_WHITE_SONIC,
		BlinkenPattern.BLINKING_RAINBOW_V2,
		BlinkenPattern.GREEN_WHITE,
		BlinkenPattern.SLOW_RAINBOW,
		BlinkenPattern.WAVY_GREEN,
		BlinkenPattern.LAVENDER_WHITE_LARSON,
		BlinkenPattern.YELLOW_LARSON,
		BlinkenPattern.SOLID_PURPLE,
		BlinkenPattern.SOLID_ORANGE,
		BlinkenPattern.LIME_GREEN, 
		BlinkenPattern.LIGHT_GREEN,
		BlinkenPattern.SOLID_GREEN,
		BlinkenPattern.SOLID_LAVENDER,
		BlinkenPattern.SOLID_MAGENTA,
	  };
	  // Run through and add them to our list
	  for (BlinkenPattern pattern: coolPatterns) {
		  selection.addObject(pattern.toString(), pattern);
	  }

		
	  SmartDashboard.putData("Best light", selection);
	}
	
	public void init() {
		setColorPattern();
	}
	
	public void autoInit() {
		setColorPattern();
		
	}
	
	public void auto() {
		light.set(pattern.pwm());	
	}
	
	void update(Joystick driver1,Joystick driver2, Joystick functions1) {
		BlinkenPattern tele = pattern;
		
		//Look for do we have the switch! If so, change pattern
		if(Robot.intake.getLightPattern() == 1) {
			tele = BlinkenPattern.LIME_GREEN;
		};
		
		light.set(tele.pwm());
	}
	
	void testPeriodic() {
		pwmvalue = selection.getSelected().pwm();
		SmartDashboard.putNumber("pwmcheck",pwmvalue);
		light.set(pwmvalue);
	}
	
	void setColorPattern() {
		//figure out team color
		Alliance alliance = DriverStation.getInstance().getAlliance();
		if( alliance == Alliance.Blue) {
			//blue patterns!
			pattern = BlinkenPattern.CODE_BLUE;
		}
		else if ( alliance == Alliance.Red) {
			pattern = BlinkenPattern.CODE_RED;
		}
		else { //invalid
			pattern = BlinkenPattern.CODE_BLUE;
		}

	}
} 


