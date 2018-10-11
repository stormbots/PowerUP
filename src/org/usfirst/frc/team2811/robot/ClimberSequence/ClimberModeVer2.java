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
public class ClimberModeVer2 extends ClimberSequence {

	TinyTimer timer = new TinyTimer();	
	double fwd = 13;
	
	long t0 = 0000;
	long driveTime = 1000;
	long t1 = 4000;
	long t2 = 500;
	long t3 = 5000;
	long t4 = 6000;
	
	
	ElevatorPosition position = ElevatorPosition.SCALEHIGH;

	public ClimberModeVer2() {
	}
	
	public void init() {
		timer = new TinyTimer(); //instead of reset, create new one to avoid a atTime(0) bug
		Robot.drive.resetEnc();
		Robot.drive.setMode(Chassis.Mode.PROFILE);
		Robot.elevator.setMaxHeight(ElevatorPosition.CLIMB_AWAY);
		//Robot.elevator.setMinHeight(ElevatorPosition.SWITCH);
		Robot.climber.setMode(Climber.Mode.CLOSEDLOOP);
	}

	/**
	 * Execute a sequence of steps 
	 * At each time interval, configure the appropriate subsystem so it's update() will perform the desired actions. 
	 * 
	 * For now, leave autos on a time-based scale to make migration easier. We will worry about event-driven autos later
	 * eg, if(have-cube) then intake.close() then elevator.move_pos(switch) then drive.setProfile(24,24,1000)
	 */
	public void run() {

		SmartDashboard.putNumber("Climber Timer", timer.getSeconds());
		Robot.elevator.setPos(position);

		//Run everything else		
		if(timer.atTime(t0)){ // step 1
			// move climber and elevator to the proper height
			position = ElevatorPosition.SCALEHIGH;
		}
		if(timer.atTime(t1)){ // step 1
			// drive away from the wall
			Robot.drive.setProfile(fwd, fwd, driveTime);
		}
		if(timer.atTime(driveTime/2+t1)){ // step 2
			// tilt backwards really quick
			Robot.intake.tiltBackward(true);
		}
		if(timer.atTime(driveTime+t1)){ // step 3
			// take the elevator down and detach the hook
			position = ElevatorPosition.FLOOR;
		}
		if(timer.atTime(t4+t3+t2+t1)){ // step 4
			// pull us up! 
		}

		timer.update();
	}

	@Override
	public void cancel() {
		Robot.drive.setMode(Mode.ARCADE);
		//Robot.elevator.setMaxHeight(ElevatorPosition.SCALEHIGH);
		//Robot.elevator.setMinHeight(ElevatorPosition.Switch);
		Robot.climber.setMode(Climber.Mode.DISABLED);
	}

}
