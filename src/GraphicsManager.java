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
		container.add(new DrawTanks(game.tanks()));
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.WHITE);
		
		frame.addKeyListener(new KeyListener(){
            @Override
               public void keyPressed(KeyEvent e) {
                   if(e.getKeyCode() == KeyEvent.VK_UP){
                       game.tanks().get(0).setDy(-1);
                   }
               }

               @Override
               public void keyTyped(KeyEvent e) {
                   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
               }
               @Override
               public void keyReleased(KeyEvent e) {
            	   if(e.getKeyCode() == KeyEvent.VK_UP){
                       game.tanks().get(0).setDy(0);
                   }               
               }
       });
	frame.setVisible(true);

	}
	

}
