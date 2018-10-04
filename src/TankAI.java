
//FIX:
//1. Tank chasing
//2. Boundary avoiding
//3. Bullet avoiding




import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

public class TankAI {
	
	private GameManager game;
	
	private Tank control;
	
	private int[] boundaryRelativeDistances;
	
	private Tank targetTank;
	private double[] point;
	private double targetTankDistance;
	
	private Bullet targetBullet;
	
	private String priority;
	
	public TankAI(Tank tank, GameManager game) {
		this.control = tank;
		this.game = game;
		boundaryRelativeDistances = new int[2];
		point = new double[2];
	}
	
	public void turn() {
		
<<<<<<< HEAD
		setTargets();										
		setPriority();
		setDPos();
=======
		setTargets();										//Choose tank to focus on
		setPriorities();
		setDPos(0);
		setDPos(1);
		if(control.reloadLeft() == 0) {
			fireBullet();
		}
		
>>>>>>> cc235cb... successfully implemented bot firing bullets
	}
	
	private void setTargets() {

		setTargetTank();
		setTargetBullet();
		
	}
	private void setTargetTank() {
		targetTank = closestTank();
		this.point[0] = objDistanceAxial(targetTank, control, 0);
		this.point[1] = objDistanceAxial(targetTank, control, 1);
		this.targetTankDistance = objDistance(control, targetTank);
	}
	private Tank closestTank() {
		Tank closestTank = new Tank(0, new int[] {}, 0, 0, Color.RED);
		double closestDist = Double.MAX_VALUE;
		for(Tank tank : game.tanks()) {
			if(!tank.equals(control)){
				if(objDistance(tank, control) < closestDist) {
					closestTank = tank;
					closestDist = objDistance(tank, control);
				}
				
			}
			
		}
		
		return closestTank;
	}
	private void setTargetBullet() {
		for(Bullet bullet : game.bullets()) {
			if(bullet.isCloseTo(control)){
				targetBullet = bullet;
				return;
			}
		}
		targetBullet = null;
	}
	
	private double distance(int[] point1, int[] point2) {
		return Math.sqrt(Math.pow(point1[0] - point2[0], 2) + Math.pow(point1[1] - point2[1], 2));
	}
	private double objDistanceAxial(Movable other, Movable center, int axis) {	//Axial distance relative to center
		return Math.abs(relativePos(other, center, axis));
	}
	private double objDistance(Movable obj1, Movable obj2) {
		return distance(obj1.pos(), obj2.pos());
	}
	private double relativePos(Movable other, Movable center, int axis) {

		return other.pos()[axis] - center.pos()[axis];
	}

	private void setBoundaryDistances() {
		
		int left = -control.pos()[0];
		int right = game.boardSize()[0] - control.pos()[0];
		int up = -control.pos()[1];
		int down = game.boardSize()[1] - control.pos()[1];
		boundaryRelativeDistances[0] = right;
		if(Math.abs(left) < Math.abs(right)) {
			boundaryRelativeDistances[0] = left;
		}
		boundaryRelativeDistances[1] = down;
		if(Math.abs(up) < Math.abs(down)) {
			boundaryRelativeDistances[1] = up;
		}

	}

<<<<<<< HEAD
=======
	private void setTargetBullet() {
		for(Bullet bullet : game.bullets()) {
			if(bullet.isCloseTo(control) && bullet.owner() != game.tanks().indexOf(control)) {
				targetBullet = bullet;
				return;
			}
		}
		targetBullet = null;
	}
	
>>>>>>> cc235cb... successfully implemented bot firing bullets
	
	private void setPriority() {
		if(targetBullet != null) {
			priority = "bullet";
			System.out.println("aaa");
			return;
		}
		priority = "tank";
		setBoundaryDistances();
		//System.out.println(boundaryDistances[0] + " " + boundaryDistances[1] + " " + targetTankRelativePos[0] + " " + targetTankRelativePos[1]);
		if(Math.abs(boundaryRelativeDistances[0]) < 100 || Math.abs(boundaryRelativeDistances[1]) < 100) {					//boundary must be within 150 units
			priority = "boundary";
		}
		System.out.println(priority);

		//System.out.println(priorities[0] + " " + priorities[1]);
	}

	private void setDPos() {
		switch(priority) {
			case "bullet":
<<<<<<< HEAD
				setDPosBullet();
				return;
=======
				if(targetBullet.dPos()[1] == 0) {
					//uhh put this in a method
					double[] orthagonalDPos = new double[2];
					orthagonalDPos[1] = 5;
					orthagonalDPos[0] = -(targetBullet.dPos()[1] * orthagonalDPos[1]) / targetBullet.dPos()[0];
					control.setD(0, orthagonalDPos[0]);
					control.setD(1, orthagonalDPos[1]);
					System.out.println("bullet" + targetBullet.dPos()[0] + " " + targetBullet.dPos()[1] + "tank" + control.dPos()[0] +
							" " + control.dPos()[1]);
				}
				else {
					double[] orthagonalDPos = new double[2];
					orthagonalDPos[0] = 5;
					orthagonalDPos[1] = -(targetBullet.dPos()[0] * orthagonalDPos[0]) / targetBullet.dPos()[1];
					control.setD(0, orthagonalDPos[0]);
					control.setD(1, orthagonalDPos[1]);
				}
				break;
>>>>>>> cc235cb... successfully implemented bot firing bullets
			case "tank":
				setDPosTank();
				return;
			case "boundary":
				setDPosBoundary();
				//if(boundaryDistances[other] < 0)
				//	control.setD(other, 5);
				//else control.setD(other, -5);
		}
	}
	
	private void setDPosTank() {
		if(targetTankDistance  < 200) tooClose(targetTank.pos());
		else if(targetTankDistance  > 225) tooFar(targetTank.pos());
	}
	private void setDPosBullet() {
		if(targetBullet.dPos()[1] != 0) {
			control.setD(0, -control.dPos()[0]);
			control.setD(1, targetBullet.dPos()[1]);
			System.out.println("bullet" + targetBullet.dPos()[0] + " " + targetBullet.dPos()[1] + "tank" + control.dPos()[0] +
					" " + control.dPos()[1]);
		}
		else {
			control.setD(1, -control.dPos()[1]);
			control.setD(0, 0);
		}
	}

	private void setDPosBoundary() {
		if(Math.abs(boundaryRelativeDistances[0]) < Math.abs(boundaryRelativeDistances[1])) {
			if(boundaryRelativeDistances[0] < 0) {
				control.setD(0, 5);
			}
			else control.setD(0, -5);
		}
		else {
			if(boundaryRelativeDistances[1] < 0) {
				control.setD(1, 5);
			}
			else control.setD(1, -5);
		}
	}
	
	private void tooClose(int[] otherPoint) {
		//Sets control to move away from given point along closer axis
		System.out.println("close");
		int axis = 0;
		if(Math.abs(control.pos()[0] - otherPoint[0]) > Math.abs(control.pos()[1] - otherPoint[1])){
			axis = 1;
		}
		if(otherPoint[axis] > 0) {
			control.setD(axis, 5);
		}
		else 
			control.setD(axis, -5);
		return;

	}
<<<<<<< HEAD
	private void tooFar(int[] otherPoint) {
		System.out.println("far");
		//Sets control to move closer to given point along farther axis
		int axis = 0;
		if(Math.abs(control.pos()[0] - otherPoint[0]) < Math.abs(control.pos()[1] - otherPoint[1])){
			axis = 1;
		}
		System.out.println(axis);
		if(otherPoint[axis] > 0) {
			control.setD(axis, -5);
=======
	private void closeEnough(int axis) {
			control.setD(axis, 0);
	}
	
	private void fireBullet() {
		double[] bulletVector = calculateBulletVector(control.pos(), targetTanks.get(0).pos(), targetTanks.get(0).dPos());
		//System.out.println(bulletVector[0] + " " + bulletVector[1]);
		double[] prevDPos = control.dPos().clone();
		Bullet bullet = control.fireBulletAimed(bulletVector);
		bullet.setOwner(game.tanks().indexOf(control));
		game.addBullet(bullet);

	}
	private double[] calculateBulletVector(int[] controlPos, int[] targetPos, double[] targetDPos) {
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
		
		
		System.out.println(" " + (bulletFireAngle / Math.PI) + "pi");
		return new double[] {bulletSpeed * Math.cos(bulletFireAngle), bulletSpeed * Math.sin(bulletFireAngle)};
	}
	
	private boolean vectorsIntersect(int[] p1, int[] d1, int[] p2, int[] d2) {
		if(d2[0] - d1[0] != 0) {
			int time = (p1[0] - p2[0])/(d2[0] - d1[0]);
			System.out.println(time * d2[1] + p2[1] - time * d1[1] + p1[1]);
			return Math.abs(time * d2[1] + p2[1] - time * d1[1] + p1[1]) < 50;
		}
		else if(d2[1] - d1[1] != 0) {
			int time = (p1[1] - p2[1])/(d2[1] - d1[1]);
			System.out.println(time * d2[0] + p2[0] - time * d1[0] + p1[0]);
			return Math.abs(time * d2[0] + p2[0] - time * d1[0] + p1[0]) < 50;
>>>>>>> cc235cb... successfully implemented bot firing bullets
		}
		else 
			control.setD(axis, 5);
		return;

	}
	
	
}
