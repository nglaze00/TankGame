
public class TankAI {
	
	private GameManager game;
	
	private Tank control;
	private double[] controlCenter;
	double boundaryDistance;
	
	private Tank targetTank;
	private double[] targetCenter;
	private double[] targetRelativePos;
	private double targetDistance;
	
	private Bullet targetBullet;
	
	private String priority;
	private double priorityDistance;
	
	public TankAI(Tank tank, GameManager game) {
		this.control = tank;
		this.game = game;
	}
	
	public void turn() {
		setTargets();										//Choose tank to focus on
		setPriority();
		targetDPos();											//Choose motion direction - chase or run from target
	}
	
	private void setTargets() {
		setTargetTank();
		
		setTargetBullet();
		
	}
	
	private double distanceToTank(Tank tank) {
		controlCenter = new double[] {(control.pos()[0] + control.size()) / 2, (control.pos()[1] + control.size()) / 2};
		targetCenter = new double[] {(tank.pos()[0] + tank.size()) / 2, (tank.pos()[1] + tank.size()) / 2};
		
		return distance(controlCenter, targetCenter);
	}
	private double distance(double[] one, double[] two) {
		
		return Math.sqrt(Math.pow(two[0] - one[0], 2) + Math.pow(two[1] - one[1], 2));
	}
	private double distance(double[] one, int[] two) {
		
		return Math.sqrt(Math.pow(two[0] - one[0], 2) + Math.pow(two[1] - one[1], 2));
	}
	private double distanceFromBoundary() {
		double left = controlCenter[0];
		double right = game.boardSize()[0] - controlCenter[0];
		double up = controlCenter[1];
		double down = game.boardSize()[1] - controlCenter[1];
		return Math.min(left, Math.min(right, Math.min(up, down)));
	}
	
	private void setTargetTank() {
		int closest = Integer.MAX_VALUE;
		for(Tank tank : game.tanks()) {
			if(!tank.equals(control)){
				if(distanceToTank(tank) < closest) {
					this.targetTank = tank;
					this.targetDistance = distanceToTank(tank);
				}
				
			}
			
		}
		this.targetRelativePos = relativePos(targetTank);

	}
	private void setTargetBullet() {
		double closestDist = Double.MAX_VALUE;
		for(Bullet bullet : game.bullets()) {
			double dist = distance(controlCenter, bullet.pos());
			if(distance(controlCenter, bullet.pos()) < closestDist) {
				this.targetBullet = bullet;
				closestDist = dist;
			}
		}
	}
	
	private double[] relativePos(Tank tank) {

		return new double[] {targetCenter[0] - controlCenter[0], targetCenter[1] - controlCenter[1]};
	}
	
	private void setPriority() {
		if(targetBullet.willHit(control)) {					//bullet must be on course to hit control
			priority = "bullet";
			return;
		}
		
		priority = "target";
		priorityDistance = targetDistance;
		
		boundaryDistance = distanceFromBoundary();
		if(boundaryDistance / 2 > targetDistance) {					//boundary must be TWICE as close as target
			priority = "boundary";
			priorityDistance = boundaryDistance;
		}
		
	}
	
	private void targetDPos() {
		//TARGET
		if(targetDistance < 125) {				//distance at which bot runs
			tooClose();

		}
		else if(targetDistance > 150) {			//distance at which bot chases
			tooFar();
		}
		else closeEnough();
		
		
		//BOUNDARIES -- HERE
	}
	
	private void tooClose() {
		if(targetRelativePos[0] > 0) {
			control.setDx(-5);
		}
		else 
			control.setDx(5);
		
		if(targetRelativePos[1] > 0) {
			control.setDy(-5);
		}
		else control.setDy(5);
	}
	private void tooFar() {
		if(targetRelativePos[0] > 0) {
			control.setDx(5);
		}
		else 
			control.setDx(-5);
		
		if(targetRelativePos[1] > 0) {
			control.setDy(5);
		}
		else control.setDy(-5);
	}
	private void closeEnough() {
		control.setDx(0);
		control.setDy(0);
	}
	
}
