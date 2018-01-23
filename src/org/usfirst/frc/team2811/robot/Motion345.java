package org.usfirst.frc.team2811.robot;


// 345 polynomial is a specific curve as a function of time that has very smooth acceleration and velocity
// the acceleration and jerk are 0 at the end points with no abrupt changes in the motion


public class Motion345 {
		
		double maxvel=0.0;// in counts per second
		double traveltime=0.0;// in seconds
		double movcnt=0.0; // counts of move  Can be positive or negative
		double tolerancemv=10.0; // Tolerance of how close to the target position the actual position needs to be to call it done
		
		
		Motion345(double vel,double travel_time_move, double move_count, double move_tolerance){
			maxvel=vel;
			traveltime=travel_time_move;
			movcnt=move_count;
			tolerancemv=move_tolerance;
			
		}
		
		// set move parameters
		public void setMove(double vel,double travel_time_move, double move_count, double move_tolerance){
			maxvel=vel; // counts per second
			traveltime=travel_time_move; // time in seconds
			movcnt=move_count; // size of the move in counts and it is signed for positive and negative 
			tolerancemv=move_tolerance;
			
		}
		
		// given the time , the actual position and the gain factor K return a velocity to drive the motor
		// this works with position error to generate a velocity based on the square root of the position error
		// The ideal position is derived from the 345 polynomial motion profile scaled by the size of the move in counts
		// and the time for the move with a know max velocity in counts per second
		public double getVelPosFb(double t, double pactual,double k) {
			double pideal=getPos(t);
			return FB(pactual,pideal,k);
		}
		
		// based on the time of the move and the counts of the move return the velocity in counts per second
		// assumes max velocity in counts per second is at motor speed =1.0 
		public double getVel(double t) {
			double v=0.0;
			v=(movcnt/traveltime)*V345(t)/maxvel; // produces a value -1 to 1 based on move count and time for move
			if(v>1.0)v=1.0;
			if(v<-1.0)v=-1.0;
			return v;
		}
		
		// based on how far to move return the position in counts
		public double getPos(double t) {
			double p=0.0;
			p=movcnt*P345(t);
			return p;
		}
		
		// calculated the position from 0-1 over time 0-1 for the motion
		public double P345(double t) {
			double t2=t*t;
			double t3=t2*t;
			return (10*t3)-(15*t2*t2)+(6*t2*t3);
		}
		
		// calculated the relative velocity for the smooth motion
		// ranges from 1-1.875 over the motion
		public double V345(double t) {
			double t2=t*t;
			double t3=t2*t;
			return (30*t2)-(60*t3)+(30*t2*t2);
		}
		
		// used to provide closed loop control to adjust velocity if position error occurs
		// ptarget is the target position, pactual is actual position and k is the gain constant for corrected velocity
		
		public double FB(double ptarget, double pactual, double k) {
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
		
		// check if the move is done by comparing difference of position target to position actual and tolerance
		public boolean ckdone(double pt, double pa) {
			boolean done=false;
			
			if(Math.abs(pt-pa)<tolerancemv) done=true;
			return done;
		}

}
