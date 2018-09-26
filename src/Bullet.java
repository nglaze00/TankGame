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
		try{
			double t = (this.pos()[0] - tank.pos()[0]) / (tank.dPos()[0] - this.dPos()[0]);
			return Math.abs((this.dPos()[1] * t + this.pos()[1]) - (tank.dPos()[1] * t + tank.pos()[1])) <= tank.size() * 2; //bullet distance within tank size
		}
		catch(ArithmeticException e) {
			double t = (this.pos()[1] - tank.pos()[1]) / (tank.dPos()[1] - this.dPos()[1]);
			return Math.abs((this.dPos()[0] * t + this.pos()[0]) - (tank.dPos()[0] * t + tank.pos()[0])) <= tank.size() * 2; 
		}
		
	}
	
	
	public void draw(Graphics g) {
		
	    g.setColor(Color.BLACK);  
	    g.fillRect(pos[0] - 5, pos[1] - 5, 10,10);
	}
}

