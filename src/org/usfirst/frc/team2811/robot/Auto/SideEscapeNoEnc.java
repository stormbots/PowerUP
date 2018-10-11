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
public class SideEscapeNoEnc extends AutoSequence {

	TinyTimer timer = new TinyTimer();
	double left1;
	double right1;

	public SideEscapeNoEnc() {
		double inside1 = 0.6;
		double outside1 = 0.6;

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
			Robot.drive.setMode(Chassis.Mode.TANK);
			Robot.drive.tankMode(left1, right1);
			Robot.elevator.setPos(ElevatorPosition.SWITCH);
		}
		if(timer.atTime(3000)) {
			Robot.drive.setMode(Chassis.Mode.TANK);
			Robot.drive.tankMode(0, 0);
			Robot.drive.setMode(Chassis.Mode.ARCADE);
		}
		if(timer.atTime(5000)) {
			Robot.elevator.setPos(ElevatorPosition.SWITCH);
			Robot.drive.setMode(Chassis.Mode.ARCADE);
		}
		
		timer.update();
		
	}
}
