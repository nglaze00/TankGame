import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class DrawObjects extends JPanel{
	private ArrayList<Tank> tanks;
	private ArrayList<Bullet> bullets;
	
	public DrawObjects(ArrayList<Tank> tanks, ArrayList<Bullet> bullets) {
		this.tanks = tanks;
		this.bullets = bullets;
		
		Timer timer = new Timer(50, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	if(bullets != null) {
                	for (Bullet bullet : bullets) {
                		repaint();
                	}
                }
            	for (Tank tank : tanks) {
                	repaint();
                }
            }
        });
        timer.start();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Tank tank : tanks) {
			tank.draw(g);
		}
		if(bullets != null) {
			for (Bullet bullet : bullets) {
				bullet.draw(g);
			}
		}
		
	}
}
