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
	private int reloadTime;
		
	public Tank(int startShields, int[] startPos, int size, int reloadTime, Color color) {
		pos = startPos;
		dPos = new int[] {0, 0};
		lastDPos = new int[] {0, 0};
		shields = startShields;
		reloadLeft = 0;
		this.reloadTime = reloadTime;
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
	}
		
	public void setDy(int dy) {
		dPos[1] = dy;
	}
	
	public void move() {
		pos[0] += dPos[0];
		pos[1] += dPos[1];
		if(reloadLeft > 0) {
			reloadLeft -= 1;
		}
		if(dPos[0] != 0 || dPos[1] != 0) {
			lastDPos = dPos.clone();
			
		}
		//System.out.println("ld:" + lastDPos[0] + " " + lastDPos[1]);
	}
	
	public void draw(Graphics g) {
		
	    g.setColor(color);  
	    g.fillRect(pos[0], pos[1], tSize,tSize);
	}

	public Bullet fireBullet() {
		if(reloadLeft != 0) {
			return null;
			
		}
		reloadLeft = reloadTime;
		return new Bullet(new int[] {pos[0] + (tSize / 2) - 5, pos[1] + (tSize / 2) - 5}, new int[] {lastDPos[0] * 5, lastDPos[1] * 5});
		
	}
	
	
}