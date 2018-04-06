package org.usfirst.frc.team2811.robot.Auto;

import org.usfirst.frc.team2811.robot.TinyTimer;
import org.usfirst.frc.team2811.robot.Elevator.ElevatorPosition;
import org.usfirst.frc.team2811.robot.Chassis;
import org.usfirst.frc.team2811.robot.Motion345;
import org.usfirst.frc.team2811.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

/**
 * Example Auto command doing a basic thing. 
 * Copy and rename this file to write a new auto command
 * @author stormbots
 *
 */
public class Testing1 extends AutoSequence {

	TinyTimer timer = new TinyTimer();

	public Testing1() {

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

		if(timer.atTime(1000)) {
			Robot.drive.setProfile(-80, 80, 2000);
		}
		
		//Example: Approximate movements to drop on the switch then back up
//		if(timer.atTime(0)){
//			Robot.elevator.setPos(-0.9);
//		}
//		if(timer.atTime(1000)) {
//			Robot.elevator.setPos(-0.8);
//		}
//		if(timer.atTime(2000)) {
//			Robot.elevator.setPos(-0.7);
//		}
//		if(timer.atTime(3000)) {
//			Robot.elevator.setPos(-0.6);
//		}
//		if(timer.atTime(4000)) {
//			Robot.elevator.setPos(-0.5);
//		}
//		if(timer.atTime(5000)) {
//			Robot.elevator.setPos(-0.4);
//		}
//		if(timer.atTime(6000)) {
//			Robot.elevator.setPos(-0.3);
//		}
//		if(timer.atTime(7000)) {
//			Robot.elevator.setPos(-0.2);
//		}
//		if(timer.atTime(8000)) {
//			Robot.elevator.setPos(-0.1);
//		}
//		if(timer.atTime(9000)) {
//			Robot.elevator.setPos(0.0);
//		}
		
		timer.update();
	}
}
