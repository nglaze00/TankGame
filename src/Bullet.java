import java.awt.Color;
import java.awt.Graphics;

public class Bullet {
	
	private int[] pos;
	private int[] dPos;
	private int owner;
	
	public Bullet(int[] pos, int[] dPos) {
		this.pos = pos;
		this.dPos = dPos;
	}
	
	public void setOwner(int owner) {
		this.owner = owner; 
	}

	public void move() {
		pos[0] += dPos[0];
		pos[1] += dPos[1];
	}
	public int[] dPos() {
		return dPos;
	}
	
	
	public void draw(Graphics g) {
		
	    g.setColor(Color.BLACK);  
	    g.fillRect(pos[0], pos[1], 10,10);
	}
}

