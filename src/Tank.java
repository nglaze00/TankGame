import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Timer;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class Tank implements Movable{

	private int[] pos;
	private double[] dPos;
	private double[] lastDPos;
	private int tSize;
	private Color color;
	
	private int shields;
	private int reloadLeft;
	private int reloadTime;
	
	private int owner;
		
	public Tank(int startShields, int[] startPos, int size, int reloadTime, Color color, int owner) {
		pos = startPos;
		dPos = new double[] {0, 0};
		lastDPos = new double[] {0, 0};
		shields = startShields;
		reloadLeft = 0;
		this.reloadTime = reloadTime;
		tSize = size;
		this.color = color;
		this.owner = owner;
	}
	
	public int[] pos() {
		return pos;
	}
	public double[] dPos() {
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
	public void setD(int axis, double d) {
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

	public Bullet fireBullet() {
		if(reloadLeft != 0) {
			return null;
			
		}
		reloadLeft = reloadTime;
		return new Bullet(new int[] {pos[0], pos[1]}, new double[] {lastDPos[0] * 3, lastDPos[1] * 3});
		
	}
	public Bullet fireBullet(double[] vector) {
		if(reloadLeft != 0) {
			return null;
		}
		reloadLeft = reloadTime;
		return new Bullet(new int[] {pos[0], pos[1]}, new double[] {vector[0], vector[1]});
	}

	public void reload() {
		if(reloadLeft > 0) {
			reloadLeft--;
		}
	}
	public int reloadLeft() { return reloadLeft; }
	
	public void draw(Graphics g) {
	    g.setColor(color);  
	    g.fillRect(pos[0] - tSize/2, pos[1] - tSize/2, tSize,tSize);
	    String[] tankInfo = tankInfo();
	    g.drawString(tankInfo[0], 100 * owner, 920);
	    g.drawString(tankInfo[1], 100 * owner, 930);
	    g.drawString(tankInfo[2], 100 * owner, 940);
	    
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
	public String[] tankInfo() {
		String[] s = new String[3];
		s[0] = "Tank " + owner;
		s[1] = "Shields left: " + this.shields();
		if(this.reloadLeft() > 0) {
			s[2] = "reloading " + this.reloadLeft();
		}
		else s[2] = "READY TO FIRE!";
		return s;
	}
	
}