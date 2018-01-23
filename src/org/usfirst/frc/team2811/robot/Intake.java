package org.usfirst.frc.team2811.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

public class Intake { 
	TalonSRX motor1 = new TalonSRX(8);
	TalonSRX motor2 = new TalonSRX(9);
	Compressor compressor = new Compressor(0);
	DoubleSolenoid squeezeSolenoid = new DoubleSolenoid(1,2);
	DoubleSolenoid tiltSolenoid = new DoubleSolenoid(3,4);
	
	enum Mode {INPUT,OUTPUT,STOP,SQUEEZE,TILT}
	Mode mode = Mode.STOP;
	
	// this code works as long as kOff is the middleground between kForward and kReverse
	public Intake() {
		motor2.follow(motor1);
		motor2.setInverted(true);
		compressor.setClosedLoopControl(true);
		squeezeSolenoid.set(DoubleSolenoid.Value.kOff);
		tiltSolenoid.set(DoubleSolenoid.Value.kOff);
	}
	
	void intake() {
		motor1.set(ControlMode.PercentOutput, 0.50);
	}
	void output() {
		motor1.set(ControlMode.PercentOutput, -0.50);
		squeezeSolenoid.set(DoubleSolenoid.Value.kReverse);
	}
	void stop() {
		motor1.set(ControlMode.PercentOutput, 0);
		squeezeSolenoid.set(DoubleSolenoid.Value.kOff);
		tiltSolenoid.set(DoubleSolenoid.Value.kOff);
	}
	void squeeze() {
		squeezeSolenoid.set(DoubleSolenoid.Value.kForward);
	}
	void tilt() {
		tiltSolenoid.set(DoubleSolenoid.Value.kForward);
	}
	void update(){
		if (mode==Mode.INPUT) {
			intake();
		}
		else if (mode==Mode.OUTPUT) {
			output();
		}
		else if(mode==Mode.SQUEEZE) {
			squeeze();
		}
		else if(mode==Mode.TILT) {
			tilt();
		}
		else {
			mode=Mode.STOP;
		}		
	}
	
}