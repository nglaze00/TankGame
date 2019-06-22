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
	private int[] boardSize;
	
	private TankAI bot;
	
	Random rand;
	
	public GameManager(int width, int height, int players, int startShields, int tankSize, int reloadTime, ArrayList<Color> colors, boolean hasBot) {
		this.colors = colors;

		tanks = new ArrayList<Tank>();
		bullets = new ArrayList<Bullet>();
		boardSize = new int[] {width, height};
		
		while(tanks.size() < players) {
			tanks.add(new Tank(startShields, randPos(width, height, tankSize), tankSize, reloadTime, colors.get(tanks.size()), tanks.size()));
		}
		graphics = new GraphicsManager(width, height, this);
		if(hasBot = true) {
			tanks.add(new Tank(startShields, randPos(width, height, tankSize), tankSize, reloadTime, Color.BLACK, tanks.size()));
		}
		bot = new TankAI(tanks.get(tanks.size() - 1), this);
		play();
		System.out.println("Player " + tanks.get(0).owner() + " wins!");
	}
	
	public void play() {
		
		while(tanks.size() > 1) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			moveTanks();
			moveBullets();
			bot.turn();
		}
	}
	
	
	public void moveTanks() {
		for (Tank tank : tanks) {
			
			boolean overlaps = false;
			if(!boundaryOverlapX(tank) && !boundaryOverlapY(tank)) {
				for(Tank other : tanks) {
					if(!tank.equals(other)) {
						if(willOverlap(tank, other)) {
							overlaps = true;
							break;
						}
					}
				}
				if(!overlaps) {
					tank.moveX();
					tank.moveY();
				}
			}
			else {
				if(!boundaryOverlapX(tank)) {
					tank.moveX();
				}
				if(!boundaryOverlapY(tank)) {
					tank.moveY();
				}
			}
			tank.reload();
			tank.setLastPos();
		}
	}
	public void moveBullets() {
		ArrayList<Bullet> bulletsRemove = new ArrayList<>();
		ArrayList<Tank> tanksRemove = new ArrayList<>();
		
		for(int i = 0; i < bullets.size(); i++) {
			Bullet bullet = bullets.get(i);
			if(boundaryOverlapX(bullet) || boundaryOverlapY(bullet)) {
				bulletsRemove.add(bullet);
			}
			for(Tank tank : tanks) {
				if(willOverlap(bullet, tank) && tanks.indexOf(tank) != bullet.owner()) {
					if(!tank.damage()) {
						tanksRemove.add(tank);
					}
					bulletsRemove.add(bullet);
					break;
				}
			}
			bullet.moveX();
			bullet.moveY();
		}
		for(Bullet bullet : bulletsRemove) {
			bullets.remove(bullet);
		}
		for(Tank tank : tanksRemove) {
			tanks.remove(tank);
		}
	}
	
	
	public static boolean willOverlap(Movable obj1, Movable obj2) {
		double[] movedOjb1 = new double[] {obj1.pos()[0] + obj1.dPos()[0], obj1.pos()[1] + obj1.dPos()[1]};
		

		boolean xOverlaps = Math.abs(movedOjb1[0] - obj2.pos()[0]) <= obj2.size() / 2 + 10;		//Assumes same size
		boolean yOverlaps = Math.abs(movedOjb1[1] - obj2.pos()[1]) <= obj2.size() / 2 + 10;
		
		if(xOverlaps && yOverlaps) {
			return true;
		}
		
		return false;
	}
	
	public boolean boundaryOverlapX(Movable obj1) {
		double movedOjb1X = obj1.pos()[0] + obj1.dPos()[0];

		boolean xOverlaps = movedOjb1X - obj1.size() / 2 < 0 || movedOjb1X + obj1.size() / 2 + 15 > boardSize[0];	//15 & 35 hardcoded to line up with window
		
		return xOverlaps;
	}
	public boolean boundaryOverlapY(Movable obj1) {
		double movedOjb1Y = obj1.pos()[1] + obj1.dPos()[1];
		
		boolean yOverlaps = movedOjb1Y - obj1.size() / 2 < 0 || movedOjb1Y + obj1.size() / 2 + 35 > boardSize[1];
		
		return yOverlaps;
	}
	
	public ArrayList<Tank> tanks(){
		return tanks;
	}
	
	public ArrayList<Bullet> bullets(){
		return bullets;
	}
	public int[] boardSize() {
		return boardSize;
	}
	
	public int[] randPos(int width, int height, int tSize) {
		rand = new Random();
		while(true) {
			int[] startPos = new int[] {rand.nextInt(width - (tSize * 2)) + tSize, rand.nextInt(height - (tSize * 2)) + tSize};
			if(validStart(startPos)) {
				return startPos;
			}
		}
			
	}
	public boolean validStart(int[] startPos) {
		for(Tank tank : tanks) {
			if(Math.sqrt(Math.pow(startPos[0] - tank.pos()[0], 2) + Math.pow(startPos[1] - tank.pos()[1], 2)) < 100) {
				return false;
			}
		}
		return true;
	}

}
