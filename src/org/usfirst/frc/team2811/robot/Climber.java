package org.usfirst.frc.team2811.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Climber extends RobotModule {
	
	WPI_TalonSRX mtr1 = new WPI_TalonSRX(9);
	WPI_TalonSRX mtr2 = new WPI_TalonSRX(10);
	
	//bool climbingmode = true;
		
	void update(Joystick driver1,Joystick driver2, Joystick stick) {
		if(stick.getRawButton(5)) { //updated for new controller
			mtr1.set(ControlMode.PercentOutput, stick.getY()); //replace getY with fixed value on robot
			mtr2.set(ControlMode.PercentOutput, stick.getY());
		}
		else {
			mtr1.set(ControlMode.PercentOutput, 0);
			mtr2.set(ControlMode.PercentOutput, 0);
		}
		
		//Look for button
		// if climbingmode == false, 
			//detach
//			climbingmode = true
		//while button pressed
			//target = maxposition
		// else 
//			target = current position
		
		//fb to selected position
		
		SmartDashboard.putNumber("ClimberPosition1", mtr1.getSelectedSensorPosition(0));
	}
	
	public void disabledPeriodic(){
		SmartDashboard.putNumber("Climber Position (disabled)", mtr1.getSelectedSensorPosition(0));
	}
	
//	public void getClimberPosition() {
	//-1..1
	//lerp (read sensor, 0, maxclimbheijght,-1,1)
//	}
	
//	public void setPosition(pos) {
	//-1..1
	//lerp (pos, -1,1,0,maxheight)
//	}

//	public void detach() {
	// setSelectedSensorPositon(-30);
//	}
}
