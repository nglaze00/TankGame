import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import java.util.Random;
import java.lang.Thread;

import javax.swing.JFrame;

public class GameManager extends JFrame {

	private GraphicsManager graphics;
	private ArrayList<Tank> tanks;
	private ArrayList<Bullet> bullets;
	private ArrayList<Color> colors;
	
	public GameManager(int width, int height, int players, int startLives, int tankSize, int reloadTime, ArrayList<Color> colors) {
		this.colors = colors;

		tanks = new ArrayList<Tank>();
		bullets = new ArrayList<Bullet>();
		
		for(int i = 0; i < players; i++) {
			addTank(startLives, randPos(width, height, tankSize), tankSize, reloadTime, colors.get(i));
		}
		graphics = new GraphicsManager(width, height, this);
		play();
	}
	
	public void play() {
		while(true) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(Tank tank : tanks) {
				tank.move();
				
			}
			for(Bullet bullet : bullets) {
				bullet.move();
				
			}
		}
	}
	
	
	public void addTank(int startLives, int[] startPos, int tankSize, int reloadTime, Color color) {
		tanks.add(new Tank(startLives, startPos, tankSize, reloadTime, color));
	}
	public void addBullet(Bullet bullet) {
		bullets.add(bullet);
	}
	
	public ArrayList<Tank> tanks(){
		return tanks;
	}
	
	public ArrayList<Bullet> bullets(){
		return bullets;
	}
	
	public static int[] randPos(int width, int height, int tSize) {
		Random rand = new Random();
		return new int[] {rand.nextInt(width - tSize), rand.nextInt(height - tSize)};
	}

}
