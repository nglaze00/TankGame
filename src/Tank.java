import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Timer;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class Tank implements Movable{

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

	public int shields() {
		return shields;
	}
	
	public int size() {
		return tSize;
	}

	public Color color() {
		return color;
	}
	
	public boolean damage() {
		shields -= 1;
		System.out.println("Tank damaged!");
		if (shields < 0) {
			return false;
		}
		else return true;
	}
	
	public void setD(int axis, int d) {
		dPos[axis] = d;
	}
	
	public void moveX() {
		pos[0] += dPos[0];
	}
	
	public void moveY() {
		pos[1] += dPos[1];
	}
	public void setLastPos() {
		if(dPos[0] != 0 || dPos[1] != 0) {
			lastDPos[0] = dPos.clone()[0];
			lastDPos[1] = dPos.clone()[1];
		}
	}

	public void reload() {
		if(reloadLeft > 0) {
			reloadLeft--;
		}
	}
	
	public void draw(Graphics g) {
		
	    g.setColor(color);  
	    g.fillRect(pos[0] - tSize/2, pos[1] - tSize/2, tSize,tSize);
	}

	public Bullet fireBullet() {
		if(reloadLeft != 0) {
			return null;
			
		}
		reloadLeft = reloadTime;
		return new Bullet(new int[] {pos[0], pos[1]}, new int[] {lastDPos[0] * 5, lastDPos[1] * 5});
		
	}
	
	
	public boolean equals(Tank tank2) {
		if(this.pos() == tank2.pos()) {
			return true;
		}
		else return false;
	}
	
	public String toString() {
		return color.toString();
	}
	
}