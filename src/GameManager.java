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
	
	public GameManager(int width, int height, int players, int startShields, int tankSize, int reloadTime, ArrayList<Color> colors) {
		this.colors = colors;

		tanks = new ArrayList<Tank>();
		bullets = new ArrayList<Bullet>();
		boardSize = new int[] {width, height};
		
		for(int i = 0; i < players; i++) {
			addTank(startShields, randPos(width, height, tankSize), tankSize, reloadTime, colors.get(i));
		}
		graphics = new GraphicsManager(width, height, this);
		
		bot = new TankAI(tanks.get(1), this);
		play();
		System.out.println("Player " + tanks.get(0).color() + " wins!");
	}
	
	public void play() {
		
		while(tanks.size() > 1) {
			bot.turn();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			moveTanks();
			moveBullets();
			
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
		for(Bullet bullet : bullets) {
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
		int[] movedOjb1 = new int[] {obj1.pos()[0] + obj1.dPos()[0], obj1.pos()[1] + obj1.dPos()[1]};
		

		boolean xOverlaps = (movedOjb1[0] <= obj2.pos()[0] + obj2.size() && movedOjb1[0] >= obj2.pos()[0]) ||
				(movedOjb1[0] + obj1.size() <= obj2.pos()[0] + obj2.size() && movedOjb1[0] + obj1.size() >= obj2.pos()[0]);

		boolean yOverlaps = (movedOjb1[1] <= obj2.pos()[1] + obj2.size() && movedOjb1[1] >= obj2.pos()[1]) || 
				movedOjb1[1] + obj1.size()  <= obj2.pos()[1] + obj2.size() && movedOjb1[1] + obj1.size() >= obj2.pos()[1];
		
		if(xOverlaps && yOverlaps) {
			return true;
		}
		
		return false;
	}
	
	public boolean boundaryOverlapX(Movable obj1) {
		int movedOjb1X = obj1.pos()[0] + obj1.dPos()[0];

		boolean xOverlaps = movedOjb1X < 0 || movedOjb1X + obj1.size() + 15 > boardSize[0];	//15 & 35 hardcoded to line up with window
		boolean yOverlaps = movedOjb1X < 0 || movedOjb1X + obj1.size() + 35 > boardSize[1];
		
		return xOverlaps;
	}
	public boolean boundaryOverlapY(Movable obj1) {
		int movedOjb1Y = obj1.pos()[1] + obj1.dPos()[1];
		
		boolean yOverlaps = movedOjb1Y < 0 || movedOjb1Y + obj1.size() + 35 > boardSize[1];
		
		return yOverlaps;
	}
	
	public void addTank(int startShields, int[] startPos, int tankSize, int reloadTime, Color color) {
		tanks.add(new Tank(startShields, startPos, tankSize, reloadTime, color));
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
	public int[] boardSize() {
		return boardSize;
	}
	
	public static int[] randPos(int width, int height, int tSize) {
		Random rand = new Random();
		return new int[] {rand.nextInt(width - tSize), rand.nextInt(height - tSize)};
	}

}
