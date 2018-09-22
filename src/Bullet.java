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
	
	
	public void draw(Graphics g) {
		
	    g.setColor(Color.BLACK);  
	    g.fillRect(pos[0] - 5, pos[1] - 5, 10,10);
	}
}

