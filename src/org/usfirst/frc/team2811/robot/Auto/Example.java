package org.usfirst.frc.team2811.robot.Auto;

import org.usfirst.frc.team2811.robot.Chassis;
import org.usfirst.frc.team2811.robot.Elevator.ElevatorPosition;
import org.usfirst.frc.team2811.robot.Elevator.Mode;
import org.usfirst.frc.team2811.robot.Motion345;
import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.TinyTimer;

/**
 * Example Auto command doing a basic thing. 
 * Copy and rename this file to write a new auto command
 * @author stormbots
 *
 */
public class Example extends AutoSequence {

	TinyTimer timer = new TinyTimer();
	
	public Example() {
		//Add any code here
		
		//Delete this warning on a real auto, it's so we can use this for testing
		System.err.println("WARNING: Using Example auto sequence!");
	}
	
	/**
	 * Execute a sequence of steps 
	 * At each time interval, configure the appropriate subsystem so it's update() will perform the desired actions. 
	 * 
	 * For now, leave autos on a time-based scale to make migration easier. We will worry about event-driven autos later
	 * eg, if(have-cube) then intake.close() then elevator.move_pos(switch) then drive.setProfile(24,24,1000)
	 */
	public void run() {
		
		//Example: Approximate movements to drop on the switch then back up
		if(timer.atTime(0)){
			System.out.println("Pivot Left");
			Robot.drive.setProfile(-5,5,1000);
			//Robot.elevator.setPos(switch height);
		}
		if(timer.atTime(1000)){
			System.out.println("Pivot Right");
			Robot.drive.setProfile(5,-5,1000);
			// note, this move lasts for 3 seconds
			// but the next action starts before it's done!
		}
		if(timer.atTime(1500)){
			System.out.println("Elevator Up");
			Robot.elevator.setPos(ElevatorPosition.SWITCH);
		}
		if(timer.atTime(2000)){
			System.out.println("Drive Forward");
			Robot.drive.setProfile(5,5,1000);
			// note, this move lasts for 3 seconds
			// but the next action starts before it's done!
		}
		if(timer.atTime(2500)){
			System.out.println("Eject cube");
			Robot.intake.ejectCube();
		}
		if(timer.atTime(3000)){
			System.out.println("Reverse");
			Robot.drive.setProfile(-5,-5,1000);
		}
		if(timer.atTime(3500)){
			System.out.println("Elevator Down");
			System.out.println("Elevator Intake Close");
			Robot.drive.setProfile(0,0,1000);
			Robot.intake.squeezeOpen(false);
			Robot.elevator.setPos(ElevatorPosition.FLOOR);
		}
		if(timer.atTime(3500)){
			System.out.println("Disabling drive");
			Robot.drive.setMode(Chassis.Mode.DISABLED);
		}
		timer.update();
	}
	
	
}
