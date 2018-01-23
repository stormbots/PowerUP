package org.usfirst.frc.team2811.robot;


// 345 polynomial is a specific curve as a function of time that has very smooth acceleration and velocity
// the acceleration and jerk are 0 at the end points with no abrupt changes in the motion


public class FB {
	/**
	 * Provides closed loop to controls 
	 * @param ptarget target position
	 * @param pactual current position as measured by sensors
	 * @param k gain constant for corrected velocity
	 * @return output velocity
	 */
	public static double FB(double ptarget, double pactual, double k) {
		double vo=0.0;
		if(ptarget>pactual) {
			vo=k*Math.sqrt(ptarget-pactual);
		}
		else {
			vo=-k*Math.sqrt(pactual-ptarget);
		}
		if(vo>1.0)vo=1.0;
		if(vo<-1.0)vo=-1.0;
		return vo;
	}

}
