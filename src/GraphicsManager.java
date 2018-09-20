import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class GraphicsManager {

	private JFrame frame;
	private int height;
	private int width;
	private Graphics g;
	private Container container;
	
	public GraphicsManager(int width, int height, GameManager game) {
		frame = new JFrame("Tank War!");
		container = frame.getContentPane();
		container.add(new DrawObjects(game.tanks(), game.bullets()));
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.WHITE);
		
		frame.addKeyListener(new KeyListener(){
			@Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
				if(key == KeyEvent.VK_UP){
                    game.tanks().get(0).setDy(-5);
                }
                if(key == KeyEvent.VK_DOWN){
                    game.tanks().get(0).setDy(5);
                }
                if(key == KeyEvent.VK_LEFT){
                    game.tanks().get(0).setDx(-5);
                }
                if(key == KeyEvent.VK_RIGHT){
                    game.tanks().get(0).setDx(5);
                }
                if(key == KeyEvent.VK_ENTER) {
                	Bullet bullet = game.tanks().get(0).fireBullet();
                	if (bullet != null) {
                		if(bullet.dPos()[0] != 0 || bullet.dPos()[1] != 0) {
                			bullet.setOwner(0);
                			game.addBullet(bullet);
                		}
                	}
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                //
            }
            @Override
            public void keyReleased(KeyEvent e) {
            	int key = e.getKeyCode();
            	if(key == KeyEvent.VK_UP){
                    game.tanks().get(0).setDy(0);
                }
                if(key == KeyEvent.VK_DOWN){
                    game.tanks().get(0).setDy(0);
                }
                if(key == KeyEvent.VK_LEFT){
                    game.tanks().get(0).setDx(0);
                }
                if(key == KeyEvent.VK_RIGHT){
                    game.tanks().get(0).setDx(0);
                }           
            }
       });
	frame.setVisible(true);

	}
	

}
