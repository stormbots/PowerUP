package org.usfirst.frc.team2811.robot.Auto;

import org.usfirst.frc.team2811.robot.TinyTimer;
import org.usfirst.frc.team2811.robot.Motion345;
import org.usfirst.frc.team2811.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

/**
 * Example Auto command doing a basic thing. 
 * Copy and rename this file to write a new auto command
 * @author stormbots
 *
 */
public class SideEscape extends AutoSequence {

	TinyTimer timer = new TinyTimer();
	double left1;
	double right1;
	
	public SideEscape() {
		double inside1 = 130;
		double outside1 = 130;

		left1 = inside1;
		right1 = outside1;
	}
	
	/**
	 * Execute a sequence of steps 
	 * At each time interval, configure the appropriate subsystem so it's update() will perform the desired actions. 
	 * 
	 * For now, leave autos on a time-based scale to make migration easier. We will worry about event-driven autos later
	 * eg, if(have-cube) then intake.close() then elevator.move_pos(switch) then drive.setProfile(24,24,1000)
	 */
	public void run() {
		System.out.println("RUNNINGAUTO");

		//Example: Approximate movements to drop on the switch then back up
		if(timer.atTime(0)){
			Robot.drive.setProfile(left1, right1, 8000);
			//Robot.elevator.setPos(switch height);
		}
		
		timer.update();
		
	}
}
