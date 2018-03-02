package org.usfirst.frc.team2811.robot;

public class Utilities {
	static double lerp(double x, double in_min, double in_max, double out_min, double out_max) {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	
	static double clamp(double x, double min, double max) {
		if(x < min) {
			return min;
		}
		if(x > max) {
			return max;
		}
		return x;
	}
}
