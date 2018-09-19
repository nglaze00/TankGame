import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class DrawTanks extends JPanel{
	private ArrayList<Tank> tanks;
	
	public DrawTanks(ArrayList<Tank> tanks) {
		this.tanks = tanks;
		
		Timer timer = new Timer(50, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                for (Tank tank : tanks) {
                	repaint();
                }
            }
        });
        timer.start();
	}
	public void updateTanks(ArrayList<Tank> tanks) {
		this.tanks = tanks;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Tank tank : tanks) {
			tank.drawTank(g);
		}
	}
}
