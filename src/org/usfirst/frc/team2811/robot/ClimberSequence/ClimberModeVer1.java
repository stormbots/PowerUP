package org.usfirst.frc.team2811.robot.ClimberSequence;

import org.usfirst.frc.team2811.robot.Elevator.ElevatorPosition;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team2811.robot.Chassis;
import org.usfirst.frc.team2811.robot.Climber;
import org.usfirst.frc.team2811.robot.Elevator;
import org.usfirst.frc.team2811.robot.Chassis.Mode;
import org.usfirst.frc.team2811.robot.Motion345;
import org.usfirst.frc.team2811.robot.Robot;
import org.usfirst.frc.team2811.robot.TinyTimer;


/**
 * Example Auto command doing a basic thing. 
 * Copy and rename this file to write a new auto command
 * @author stormbots
 *
 */
public class ClimberModeVer1 extends ClimberSequence {

	TinyTimer timer = new TinyTimer();	
	double fwd = 6;
	
	long t0 = 0000;
	long driveTime = 1000;
	long t1 = 5000;
	long t2 = 250;
	long t3 = 5000;
	long t4 = 6000;

	public ClimberModeVer1() {
	}
	
	public void init() {
		timer.reset();
		Robot.drive.resetEnc();
		Robot.drive.setMode(Chassis.Mode.PROFILE);
		Robot.elevator.setMaxHeight(Elevator.ElevatorPosition.CLIMB_AWAY);
		Robot.climber.setMode(Climber.Mode.CLOSEDLOOP);			
		Robot.climber.setPosition(1);
		Robot.climber.detach();
	}

	/**
	 * Execute a sequence of steps 
	 * At each time interval, configure the appropriate subsystem so it's update() will perform the desired actions. 
	 * 
	 * For now, leave autos on a time-based scale to make migration easier. We will worry about event-driven autos later
	 * eg, if(have-cube) then intake.close() then elevator.move_pos(switch) then drive.setProfile(24,24,1000)
	 */
	public void run() {

		//Move the climber and elevator
		double height = Robot.climber.getClimberPosition();
		Robot.elevator.setPos(height);
		SmartDashboard.putNumber("ClimberHeight", height);

		//Run everything else
		if(timer.atTime(t0)){ // step 1
			// drive away from the wall
			Robot.drive.setProfile(fwd, fwd, driveTime);
			// move climber and elevator to the proper height
			Robot.elevator.setPos(ElevatorPosition.CLIMB_AWAY);
		}
		if(timer.atTime(t1)){ // step 2
			// tilt backwards really quick
			Robot.intake.tiltBackward(true);
		}
		if(timer.atTime(t2+t1)){ // step 3
			// take the elevator down and detach the hook
			Robot.elevator.setPos(ElevatorPosition.FLOOR);
		}
		if(timer.atTime(t3+t2+t1)){ // step 4
			// pull us up! 
		}

		timer.update();
	}

	@Override
	public void cancel() {
		Robot.drive.setMode(Mode.ARCADE);
		Robot.elevator.setMaxHeight(Elevator.ElevatorPosition.SCALEHIGH);
		Robot.climber.setMode(Climber.Mode.DISABLED);
	}

}
