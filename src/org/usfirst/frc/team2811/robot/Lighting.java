package org.usfirst.frc.team2811.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import org.usfirst.frc.team2811.robot.BlinkenPattern;

public class Lighting {
	Talon light = new Talon(4);
	BlinkenPattern pattern = BlinkenPattern.C12_END_TO_END_BLEND_C1_TO_C2;
	
	Lighting(){
		init();
	}
	
	public void init() {
		
	}
	
	public void autoInit() {
		
	}
	
	public void auto() {
		light.set(pattern.pwm());
		
	}
	
	public void update(Joystick joystick) {
		light.set(joystick.getY());
		
	}
}


