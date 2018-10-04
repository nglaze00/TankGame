import java.awt.Graphics;

public class GameInfo {
	
	GameManager game;
	
	public GameInfo(GameManager game) {
		this.game = game;
		
	}
	
	
	public void draw(Graphics g) {
		int ycoord = game.boardSize()[1] - 50;
		int xcoord = 50;
		for(Tank tank : game.tanks()) {
			g.setColor(tank.color());
			g.drawString(tank.toString(), xcoord, ycoord);
			xcoord += 100;
		}
	}
}
