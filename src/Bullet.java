import java.awt.Color;
import java.awt.Graphics;

public class Bullet implements Movable{
	
	private int[] pos;
	private int[] dPos;
	private int owner;
	private int tSize;
	
	public Bullet(int[] pos, int[] dPos) {
		this.pos = pos;
		this.dPos = dPos;
		this.tSize = 10;
	}
	
	public void setOwner(int owner) {
		this.owner = owner; 
	}
	public int owner() {
		return owner;
	}

	public int[] pos() {
		return pos;
	}
	public void moveX() {
		pos[0] += dPos[0];
	}
	public void moveY() {
		pos[1] += dPos[1];
	}
	public int[] dPos() {
		return dPos;
	}
	public int size() {
		return tSize;
	}
	
	public boolean isCloseTo(Tank tank) {
		double t;
		int tAxis = 1;
		if(tank.dPos()[0] - this.dPos()[0] == 0) {
			tAxis = 0;
			t = (this.pos()[1] - tank.pos()[1]) / (tank.dPos()[1] - this.dPos()[1]);
		}
		else t = (this.pos()[0] - tank.pos()[0]) / (tank.dPos()[0] - this.dPos()[0]);
		
		//double[] tankNewPos = new double[] {tank.pos()[0] + tank.dPos()[0] * t, tank.pos()[1] + tank.dPos()[1] * t};
		//double[] bulletNewPos = new double[] {this.pos()[0] + this.dPos()[0] * t, this.pos()[1] + this.dPos()[1] * t};
		return Math.abs((this.dPos()[tAxis] * t + this.pos()[tAxis]) - (tank.dPos()[tAxis] * t + tank.pos()[tAxis])) <= tank.size() * 5;

	}

	
	
	public void draw(Graphics g) {
		
	    g.setColor(Color.BLACK);  
	    g.fillRect(pos[0] - 5, pos[1] - 5, 10,10);
	}
}

