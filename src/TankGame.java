import java.awt.Color;
import java.util.ArrayList;

public class TankGame {

	public static void main(String[] args) {
		//Game variables
		//Board
		int height = 500;
		int width = 500;
		
		//Tank stats
		int tankSize = 50;
		int reloadTime = 5;
		int lives = 3;
		
		//Players
		int players = 2;
		
		//Colors
		ArrayList<Color> colors = new ArrayList<Color>();
		colors.add(Color.RED);
		colors.add(Color.BLUE);
		colors.add(Color.GREEN);
		
		
		new GameManager(width, height, players, lives, tankSize, reloadTime, colors);
	}
}
