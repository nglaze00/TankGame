import java.awt.Color;
import java.awt.Graphics;
import java.util.Timer;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class Tank {

	private int[] pos;
	private int[] dPos;
	private int[] lastDPos;
	private int tSize;
	private Color color;
	
	private int shields;
	private int reloadLeft;
		
	public Tank(int startShields, int[] startPos, int size, Color color) {
		pos = startPos;
		dPos = new int[] {0, 0};
		lastDPos = new int[] {0, 0};
		shields = startShields;
		reloadLeft = 0;
		
		tSize = size;
		this.color = color;
	}
	
	public int[] pos() {
		return pos;
	}
	public int[] dPos() {
		return dPos;
	}

	public int lives() {
		return shields;
	}
	
	public int tSize() {
		return tSize;
	}

	
	public boolean damageTank() {
		shields -= 1;
		if (shields < 0) {
			return false;
		}
		else return true;
	}
	
	public void setDx(int dx) {
		dPos[0] = dx;
		if(dx != 0) {
			lastDPos[0] = dx;
		}
	}
	
	public void setDy(int dy) {
		dPos[1] = dy;
		if(dy != 0) {
			lastDPos[1] = dy;
		}
	}
	
	public void move() {
		pos[0] += dPos[0];
		pos[1] += dPos[1];
	}
	
	public void draw(Graphics g) {
		
	    g.setColor(color);  
	    g.fillRect(pos[0], pos[1], tSize,tSize);
	}

	public Bullet fireBullet() {
		if(reloadLeft != 0) {
			return null;
			
		}
		return new Bullet(this.pos.clone(), new int[] {dPos[0] * 5, dPos[1] * 5});
		
	}
	
	
}