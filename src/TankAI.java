
public class TankAI {
	
	private Tank control;
	double[] controlCenter;
	private GameManager game;
	private Tank target;
	
	private double targetDistance;
	double[] targetCenter;
	private double[] targetRelativePos;
	
	public TankAI(Tank tank, GameManager game) {
		this.control = tank;
		this.game = game;
	}
	
	public void turn() {
		setTarget();										//Choose tank to focus on
		
		setDPos();											//Choose motion direction - chase or run from target
	}
	
	private void setTarget() {
		int closest = Integer.MAX_VALUE;
		for(Tank tank : game.tanks()) {
			if(!tank.equals(control)){
				if(distanceTo(tank) < closest) {
					this.target = tank;
					this.targetDistance = distanceTo(tank);
				}
				
			}
		}
		this.targetRelativePos = relativePos(target);
	}
	
	private double distanceTo(Tank tank) {
		controlCenter = new double[] {(control.pos()[0] + control.size()) / 2, (control.pos()[1] + control.size()) / 2};
		targetCenter = new double[] {(tank.pos()[0] + tank.size()) / 2, (tank.pos()[1] + tank.size()) / 2};
		
		return Math.sqrt(Math.pow(controlCenter[0] - targetCenter[0], 2) + Math.pow(controlCenter[1] - targetCenter[1], 2));
	}
	
	private double[] relativePos(Tank tank) {

		return new double[] {targetCenter[0] - controlCenter[0], targetCenter[1] - controlCenter[1]};
	}
	
	private void setDPos() {
		//TARGET
		if(targetDistance < 125) {				//distance at which bot runs
			tooClose();

		}
		else if(targetDistance > 200) {			//distance at which bot chases
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
