import java.util.ArrayList;
import java.util.Collections;

public class TankAI {
	
	private GameManager game;
	
	private Tank control;
	private double controlSpeed;
	
	private double[] boundaryDistances;
	
	private ArrayList<Tank> targetTanks;
	private double[] targetTankVector;
	
	private Bullet targetBullet;
	private int dPosLock;
	
	private String[] priorities;
	
	
	public TankAI(Tank tank, GameManager game) {
		this.control = tank;
		this.game = game;
		this.targetTanks = new ArrayList<Tank>();
		this.boundaryDistances = new double[2];
		this.targetTankVector = new double[2];
		this.priorities = new String[2];
		this.dPosLock = 20;
		this.controlSpeed = Math.sqrt(50);
	}
	
	public void turn() {
		/**
		 * Updates AI's direction of movement & target
		 */
		
		/**setTargets();										
		setPriorities();**/
		chooseDirection();
		setDPos(0);
		setDPos(1);
		if(control.reloadLeft() == 0) {
			fireBullet();
		}

		
		
	}
	public void chooseDirection
	
	private void setTargets() {

		setTargetTanks();
		setTargetBullet();
		
	}
	private void setTargetTanks() {
		/**
		 * Sets closest two tanks as targets
		 */
		targetTanks.add(0, closestTank(0));
		targetTanks.add(1, closestTank(1));
	}
	private Tank closestTank(int axis) {
		/**
		 * Retursn closest tank
		 */
		Tank closestTank = game.tanks().get(0);
		double closestDist = Double.MAX_VALUE;
		for(Tank tank : game.tanks()) {
			if(!tank.equals(control)){
				if(distanceFromTank(tank, axis) < closestDist) {
					closestTank = tank;
					closestDist = distanceFromTank(tank, axis);
				}
				
			}
			
		}
		this.targetTankVector[axis] = relativePos(closestTank, axis);
		return closestTank;
	}
	
	private double distance(double one, double two) {
		
		return Math.abs(two - one);
	}
	private double distanceFromTank(Tank tank, int axis) {
		
		return distance(control.pos()[axis], tank.pos()[axis]);
	}
	private double distanceFromBullet(Bullet bullet) {
		
		return Math.sqrt(Math.pow(distance(control.pos()[0], bullet.pos()[0]), 2) 
				+ Math.pow(distance(control.pos()[1], bullet.pos()[1]), 2));
	}
	private void setBoundaryDistances() {
		/**
		 * Updates how far self is from each boundary
		 */
		
		double left = -control.pos()[0];
		double right = game.boardSize()[0] - control.pos()[0];
		double up = -control.pos()[1];
		double down = game.boardSize()[1] - control.pos()[1];
		boundaryDistances[0] = right;
		if(Math.abs(left) < Math.abs(right)) {
			boundaryDistances[0] = left;
		}
		boundaryDistances[1] = down;
		if(Math.abs(up) < Math.abs(down)) {
			boundaryDistances[1] = up;
		}

	}
	private double relativePos(Tank tank, int axis) {
		/**
		 * Computes relative position between self and tank.
		 */

		return tank.pos()[axis] - control.pos()[axis];
	}

	private void setTargetBullet() {
		/**
		 * Sets closest bullet as target for self to avoid
		 */
		Bullet old = targetBullet;
		if(game.bullets().size() > 0) {
			Bullet closestBullet = game.bullets().get(0);
			double closestDist = Double.MAX_VALUE;
			for(Bullet bullet : game.bullets()) {
				double dist = distanceFromBullet(bullet);
				if(dist < closestDist && dist < 150 && bullet.owner() != game.tanks().indexOf(control)) {	//150: distance at which tries to avoid bullet
					targetBullet = bullet;
					closestDist = dist;
				}			
			}
		}
		else {
			targetBullet = null;
		}
		
		
	}
	
	
	private void setPriorities() {
		/**
		 * Sets self action priority as one of the following:
		 * 		a. Avoid boundary
		 * 		b. Avoid bullet
		 * 		c. Adjust distance from enemy tank
		 */
		setBoundaryDistances();
		if(targetBullet != null) {
			priorities[0] = "bullet";
			priorities[1] = "bullet";
			return;
		}
		
		priorities[0] = "tank";
		priorities[1] = "tank";
		//System.out.println(boundaryDistances[0] + " " + boundaryDistances[1] + " " + targetTankRelativePos[0] + " " + targetTankRelativePos[1]);
		if(Math.abs(boundaryDistances[0]) < 150) {					//boundary must be within 300 units
			priorities[0] = "boundary";
		}
		if(Math.abs(boundaryDistances[1]) < 150) {
			priorities[1] = "boundary";
		}
		//System.out.println(priorities[0] + " " + priorities[1]);

		
	}
	
	private void setDPos(int axis) {
		/**
		 * Sets direction for self to move next frame
		 */
		if(dPosLock > 0) {
			dPosLock--;
			return;
		}
		int other = 1;
		if(axis == 1) other = 0;
		if(control.pos()[axis] < 50){
			control.setD(axis, this.controlSpeed);
			this.dPosLock = 5;
			return;
		}
		else if(control.pos()[axis] > 950) {
			control.setD(axis, -this.controlSpeed);
			this.dPosLock = 5;
			return;
		}
		switch(priorities[axis]) {
			case "bullet":
				// Chooses vector orthogonal to bullet vector that points closer to the center of the field
				ArrayList<double[]> vectors = orthagonalScaledVectors(targetBullet.dPos(), this.controlSpeed);
				int vectorChoice;
				
				if(control.pos()[1] > (targetBullet.pos()[1] + targetBullet.dPos()[1]/targetBullet.dPos()[0] * (control.pos()[0] 
						- targetBullet.pos()[0]))) {
					vectorChoice = 0;
				}
				else {
					vectorChoice = 1;
				}
				control.setD(0, vectors.get(vectorChoice)[0]);
				control.setD(1, vectors.get(vectorChoice)[1]);
				break;
			case "tank":
				// Maintains distance from closest enemy tank between 200 & 225 units
				if(Math.abs(targetTankVector[axis])  < 200) {
					tooClose(axis);
					break;
				}
				else if(Math.abs(targetTankVector[axis])  > 225) {
					tooFar(axis);
					break;
				}
				//else closeEnough(axis);
				break;
			case "boundary":
				// Stay far enough away from boundary
				if(boundaryDistances[axis] < 0) {
					control.setD(axis, 5);
				}
				else control.setD(axis, -5);

		}
	}
	private void tooClose(int axis) {
	/**
	 * Move away from target tank
	 */
		if(targetTankVector[axis] > 0) {
			control.setD(axis, -5);
		}
		else 
			control.setD(axis, 5);
		return;
	}
	private void tooFar(int axis) {
		/**
		 * Move towards target tank
		 */
		if(targetTankVector[axis] > 0) {
			control.setD(axis, 5);
		}
		else 
			control.setD(axis, -5);
		return;

	}
	private void closeEnough(int axis) {
			control.setD(axis, 0);
	}
	
	private void fireBullet() {
		/**
		 * Fire bullet such that it will intersect target tank's current trajectory
		 */
		double[] bulletVector = calculateBulletVector(control.pos(), targetTanks.get(0).pos(), targetTanks.get(0).dPos());
		//System.out.println(bulletVector[0] + " " + bulletVector[1]);
		double[] prevDPos = control.dPos().clone();
		Bullet bullet = control.fireBullet(bulletVector);
		bullet.setOwner(game.tanks().indexOf(control));
		game.bullets().add(bullet);

	}
	private double[] calculateBulletVector(int[] controlPos, int[] targetPos, double[] targetDPos) {
		/**
		 * Compute vector along which to fire bullet
		 */
		//See docs for math lol (TODO: write math docs)
		int bulletSpeed = 15;
		int[] targetControlVector = new int[] {(controlPos[0] - targetPos[0]), (controlPos[1] - targetPos[1])};
		double targetSpeed = Math.sqrt(Math.pow(targetDPos[0], 2) + Math.pow(targetDPos[1], 2));
		if(targetDPos[0] == 0 && targetDPos[1] == 0) {
			double bulletVectorMagnitude = Math.sqrt(Math.pow(targetControlVector[0],  2) + Math.pow(targetControlVector[1], 2));
		
			double[] bulletUnitVector = new double[] {-targetControlVector[0] / bulletVectorMagnitude, -targetControlVector[1] / bulletVectorMagnitude};
			return new double[] {bulletUnitVector[0] * bulletSpeed, bulletUnitVector[1] * bulletSpeed};
		}
		//System.out.println(targetControlVector[0] + " " + targetControlVector[1]);
		//System.out.println(targetDPos[0] + " " + targetDPos[1]);
		double targetControlCosine = ((targetControlVector[0] * targetDPos[0])
				+ (targetControlVector[1] * targetDPos[1])) 
				/ (Math.sqrt(Math.pow(targetDPos[0], 2) + Math.pow(targetDPos[1], 2)) * Math.sqrt(Math.pow(targetControlVector[0], 2) + Math.pow(targetControlVector[1], 2)));
		double targetControlAngle = Math.acos(targetControlCosine);
		//System.out.println((targetControlAngle / Math.PI) + "pi");
		
		
		double angleToHorizontal = Math.abs(Math.atan(targetControlVector[1]*1.0/targetControlVector[0]));
		//System.out.println(targetControlVector[1]*1.0/targetControlVector[0]);
		//System.out.println("    " + (angleToHorizontal / Math.PI) + "pi");
		double bulletFireAngleTriangle = Math.asin(targetSpeed * Math.sin(targetControlAngle) / bulletSpeed);
		double bulletFireAngle;
		if(controlPos[1] < targetPos[1]) {
			if(targetDPos[1] > 0) {
				bulletFireAngle = angleToHorizontal + bulletFireAngleTriangle;
			}
			else {
				bulletFireAngle = angleToHorizontal - bulletFireAngleTriangle;
			}
			if(controlPos[0] > targetPos[0]) {
				bulletFireAngle = Math.PI - bulletFireAngle;
			}
		}
		else{
			if(targetDPos[1] < 0) {
				bulletFireAngle = -(angleToHorizontal + bulletFireAngleTriangle);
			}
			else {
				bulletFireAngle = -angleToHorizontal + bulletFireAngleTriangle;
			}
			if(controlPos[0] > targetPos[0]) {
				bulletFireAngle = -Math.PI - bulletFireAngle;
			}
		}
		
		
		return new double[] {bulletSpeed * Math.cos(bulletFireAngle), bulletSpeed * Math.sin(bulletFireAngle)};
	}
	
	private ArrayList<double[]> orthagonalScaledVectors(double[] vector, double fMag) {
		/**
		 * Compute vectors orthogonal to vector
		 */
		ArrayList<double[]> orthagonalVectors = new ArrayList<double[]>();
		for(int i = 0; i < 2; i++) {
			double[] orthagonal = new double[2];
			orthagonal[1] = 1 - (2*i);
			orthagonal[0] = -(vector[1] / vector[0]) * orthagonal[1];
			orthagonalVectors.add(scaleVectorTo(orthagonal, fMag));
		}
		return orthagonalVectors;
	}
	private double[] scaleVectorTo(double[] vector, double fMag) {
		/**
		 * Scales a vector by fMag.
		 */
		double iMag = Math.sqrt(Math.pow(vector[0], 2) + Math.pow(vector[1], 2));
		return new double[] {vector[0] / iMag * fMag, vector[1] / iMag * fMag};
	}
	
	private boolean vectorsIntersect(int[] p1, int[] d1, int[] p2, int[] d2) {
		/**
		 * Returns whether two vectors will intersect.
		 */
		if(d2[0] - d1[0] != 0) {
			int time = (p1[0] - p2[0])/(d2[0] - d1[0]);
			return Math.abs(time * d2[1] + p2[1] - time * d1[1] + p1[1]) < 50;
		}
		else if(d2[1] - d1[1] != 0) {
			int time = (p1[1] - p2[1])/(d2[1] - d1[1]);
			return Math.abs(time * d2[0] + p2[0] - time * d1[0] + p1[0]) < 50;
		}
		else return true;
	}
}
