import java.util.ArrayList;
import java.util.Collections;

public class TankAI {
	
	private GameManager game;
	
	private Tank control;
	
	private double[] boundaryDistances;
	
	private ArrayList<Tank> targetTanks;
	private double[] targetTankRelativePos;
	
	private Bullet targetBullet;
	
	private String[] priorities;
	
	public TankAI(Tank tank, GameManager game) {
		this.control = tank;
		this.game = game;
		targetTanks = new ArrayList<Tank>();
		boundaryDistances = new double[2];
		targetTankRelativePos = new double[2];
		priorities = new String[2];
	}
	
	public void turn() {
		
		setTargets();										//Choose tank to focus on
		setPriorities();
		setDPos(0);
		setDPos(1);
	}
	
	private void setTargets() {

		setTargetTanks();
		setTargetBullet();
		
	}
	private void setTargetTanks() {
		targetTanks.add(0, closestTank(0));
		targetTanks.add(1, closestTank(1));
	}
	private Tank closestTank(int axis) {
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
		this.targetTankRelativePos[axis] = relativePos(closestTank, axis);
		return closestTank;
	}
	
	private double distance(double one, double two) {
		
		return Math.abs(two - one);
	}
	private double distanceFromTank(Tank tank, int axis) {
		
		return distance(control.pos()[axis], tank.pos()[axis]);
	}
	private double distanceFromBullet(Bullet bullet) {
		
		return Math.sqrt(Math.pow(distance(control.pos()[0], bullet.pos()[0]), 2) - 
				distance(control.pos()[1], bullet.pos()[1]));
	}
	private void setBoundaryDistances() {
		
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

		return tank.pos()[axis] - control.pos()[axis];
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
	
	
	private void setPriorities() {
		if(targetBullet != null) {
			priorities[0] = "bullet";
			priorities[1] = "bullet";
			System.out.println("aaa");
			return;
		}
		
		priorities[0] = "tank";
		priorities[1] = "tank";
		setBoundaryDistances();
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
		int other = 1;
		if(axis == 1) other = 0;
		switch(priorities[axis]) {
			case "bullet":
				if(targetBullet.dPos()[1] != 0 ) {
					int[] orthagonalDPos = new int[2];
					orthagonalDPos[1] = 5;
					orthagonalDPos[0] = -(targetBullet.dPos()[1] * orthagonalDPos[1]) / targetBullet.dPos()[0];
					control.setD(0, orthagonalDPos[0]);
					control.setD(1, orthagonalDPos[1]);
					System.out.println("bullet" + targetBullet.dPos()[0] + " " + targetBullet.dPos()[1] + "tank" + control.dPos()[0] +
							" " + control.dPos()[1]);
				}
				else {
					int[] orthagonalDPos = new int[2];
					orthagonalDPos[0] = 5;
					orthagonalDPos[1] = -(targetBullet.dPos()[0] * orthagonalDPos[0]) / targetBullet.dPos()[1];
					control.setD(0, orthagonalDPos[0]);
					control.setD(1, orthagonalDPos[1]);
				}
				break;
			case "tank":
				if(Math.abs(targetTankRelativePos[axis])  < 200) {
					tooClose(axis);
					break;
				}
				else if(Math.abs(targetTankRelativePos[axis])  > 225) {
					tooFar(axis);
					break;
				}
				//else closeEnough(axis);
				break;
			case "boundary":
				if(boundaryDistances[axis] < 0) {
					control.setD(axis, 5);
				}
				else control.setD(axis, -5);
				
				//if(boundaryDistances[other] < 0)
				//	control.setD(other, 5);
				//else control.setD(other, -5);
		}
	}

	
	
	private void tooClose(int axis) {
	
		if(targetTankRelativePos[axis] > 0) {
			control.setD(axis, -5);
		}
		else 
			control.setD(axis, 5);
		return;
	}
	private void tooFar(int axis) {
		if(targetTankRelativePos[axis] > 0) {
			control.setD(axis, 5);
		}
		else 
			control.setD(axis, -5);
		return;

	}
	private void closeEnough(int axis) {
			control.setD(axis, 0);

	}
	
}
