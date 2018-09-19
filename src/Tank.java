import java.awt.Color;
import java.awt.Graphics;
import java.util.Timer;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class Tank {

	private int[] pos;
	private int[] dPos;
	private int tSize;
	private Color color;
	
	private int shields;
		
	public Tank(int startShields, int[] startPos, int size, Color color) {
		pos = startPos;
		shields = startShields;
		dPos = new int[] {0, 0};
		tSize = size;
		this.color = color;
	}
	
	public int[] pos() {
		return pos;
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
	}
	
	public void drawTank(Graphics g) {
		
	    g.setColor(color);  
	    g.fillRect(pos[0], pos[1], tSize,tSize);
	}
	
	
}