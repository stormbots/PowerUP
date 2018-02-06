package org.usfirst.frc.team2811.robot;

import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team2811.robot.BlinkenPattern;

public class Lighting {
	Talon light = new Talon(4);
	BlinkenPattern pattern = BlinkenPattern.C12_END_TO_END_BLEND_C1_TO_C2;
	
	Preferences prefs = Preferences.getInstance();
	double pwmvalue=0.0;
	
	private SendableChooser<BlinkenPattern> selection = new SendableChooser<>();

	Lighting(){
		//init();
	}
	
	public void robotInit() {
		selection.addDefault(BlinkenPattern.SOLID_BLUE   .toString(), BlinkenPattern.SOLID_BLUE   );
//		selection.addObject(BlinkenPattern.C12_SINELON.toString(), BlinkenPattern.C12_SINELON);
		selection.addObject(BlinkenPattern.RED_WHITE_STROBE.toString(), BlinkenPattern.RED_WHITE_STROBE);
		selection.addObject(BlinkenPattern.GREEN_WHITE_STROBE.toString(), BlinkenPattern.GREEN_WHITE_STROBE);
		selection.addObject(BlinkenPattern.BLUE_SHAKE .toString(), BlinkenPattern.BLUE_SHAKE );
		selection.addObject(BlinkenPattern.GREEN_SHAKE  .toString(), BlinkenPattern.GREEN_SHAKE  );
		selection.addObject(BlinkenPattern.BLUE_SPARKLING .toString(), BlinkenPattern.BLUE_SPARKLING );
		selection.addObject(BlinkenPattern.WAVY_BLUE .toString(), BlinkenPattern.WAVY_BLUE );
		
		SmartDashboard.putData("Best light", selection);
		
		//SOLID_BLUE
		//SOLID_YELLOW 
	}
	
	public void init() {
//		SmartDashboard.putNumber("Lighting Pattern", 0);
	}
	
	public void autoInit() {
		
	}
	
	public void auto() {
		light.set(pattern.pwm());	
	}
	
	void update(Joystick driver1,Joystick driver2, Joystick functions1) {
		//		light.set(joystick.getY());
		
		pwmvalue=prefs.getDouble("testLightValue", 0);
		
		pwmvalue = selection.getSelected().pwm();
		
		SmartDashboard.putNumber("pwmcheck",pwmvalue);
		
		light.set(pwmvalue);
	}
} 


