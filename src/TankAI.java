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
	
	private int bulletScoreWeight = 1000;
	
	private int tankAvoidWeight = 100;
	
	private int boundaryAvoidWeight = 200;
	
	
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
	private void chooseDirection() {
	
		double[][] dirChoices = {{5, 0}, {-5, 0}, {0, 5}, {0, -5}, 
								 {3, 3}, {-3, 3}, {3, -3}, {-3, -3}};
		
		for(double[] testDir : dirChoices) {
			double distToClosestBulletPath = computeDistToClosestBulletPath(testDir);
			double avgSquaredDistToTank = computeAvgSquaredDistToTank(testDir);
		}

		
	}

	private Tank closestTank(int axis) {
		/**
		 * Returns closest tank
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
	
	private double pointDistance(double[] one, double[] two) {
		
		return Math.sqrt(Math.pow(one[0] - two[0], 2) + Math.pow(one[1] - two[1], 2));
	}
	
	private double distanceFromBulletPath(Bullet bullet, double[] testDir) {
		/**
		 * Computes distance of closest encounter between a bullet and the control tank
		 */
		double[] futureTankPos = control.pos();
		double[] futureBulletPos = bullet.pos();
		double prevDist = pointDistance(futureTankPos, futureBulletPos);
		while(true) {
			futureTankPos[0] += testDir[0];
			futureTankPos[1] += testDir[1];
			futureBulletPos[0] += bullet.dPos()[0];
			futureBulletPos[1] += bullet.dPos()[1];
			double dist = pointDistance(futureTankPos, futureBulletPos);
			if(prevDist <= dist) {
				return prevDist;
			}
			else {
				prevDist = dist;
			}
			
		}
	}
	
	private void updateBoundaryDistances() {
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

	private double computeAvgSquaredDistToTank(double[] testDir) {
		double distSum = 0;
		for(Tank tank: game.tanks()) {
			if(!tank.equals(control)){
				distSum += Math.pow(pointDistance(control.pos(), tank.pos()), 2);
			}
		}
		return distSum / game.tanks().size();
	}
	
	private double computeDistToClosestBulletPath(double[] testDir) {
		/**
		 * Computes distance to bullet path that will most nearly intersect with the tank, assuming it moves in the given direction
		 */
		if(game.bullets().size() > 0) {
			Bullet closestBullet = game.bullets().get(0);
			double closestDist = Double.MAX_VALUE;
			for(Bullet bullet : game.bullets()) {
				
				if(bullet.owner() != control.owner()) {
					double dist = distanceFromBulletPath(bullet, testDir);
					if(dist < closestDist) {
						closestDist = dist;
					}
					
					
				}			
			}
			return closestDist;
		}
	
		else {
			return -1;
		}
		
		
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
	private double[] calculateBulletVector(double[] controlPos, double[] targetPos, double[] targetDPos) {
		/**
		 * Compute vector along which to fire bullet
		 */
		//See docs for math  (TODO: write math docs lol)
		int bulletSpeed = 15;
		double[] targetControlVector = new double[] {(controlPos[0] - targetPos[0]), (controlPos[1] - targetPos[1])};
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
}
