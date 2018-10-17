import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

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
		frame.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				Tank tank = game.tanks().get(0);
				int[] target = new int[] {e.getX(), e.getY()};
				int[] tankPos = tank.pos();
				double[] bulletDir = new double[] {target[0] - tankPos[0], target[1] - tankPos[1]};
				double dirMag = Math.sqrt(Math.pow(bulletDir[0], 2) + Math.pow(bulletDir[1], 2));
				double[] bulletVector = new double[] {15*bulletDir[0]/dirMag, 15*bulletDir[1]/dirMag};
	            Bullet bullet = tank.fireBullet(bulletVector);
		        if(bullet != null) {
	            	if(bullet.dPos()[0] != 0 || bullet.dPos()[1] != 0) {
		             	bullet.setOwner(0);
		               	game.bullets().add(bullet);
		            }   
		        }
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		frame.addKeyListener(new KeyListener(){
			
			
			@Override
            public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_W){
                    game.tanks().get(0).setD(1, 0);
                }
                if(key == KeyEvent.VK_S){
                    game.tanks().get(0).setD(1, 0);
                }
                if(key == KeyEvent.VK_A){
                    game.tanks().get(0).setD(0, 0);
                }
                if(key == KeyEvent.VK_D){
                    game.tanks().get(0).setD(0, 0);
                }
				
            }
			@Override
            public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_W){
                    game.tanks().get(0).setD(1, -5);
                }
                if(key == KeyEvent.VK_S){
                    game.tanks().get(0).setD(1, 5);
                }
                if(key == KeyEvent.VK_A){
                    game.tanks().get(0).setD(0, -5);
                }
                if(key == KeyEvent.VK_D){
                    game.tanks().get(0).setD(0, 5);
                }
            }
			@Override
            public void keyTyped(KeyEvent e) {
            	
            }
            
       });
	frame.setVisible(true);

	}
	

}
