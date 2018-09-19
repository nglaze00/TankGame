import java.awt.Color;
import java.util.ArrayList;

public class TankGame {

	public static void main(String[] args) {
		//Game variables
		//Board
		int height = 500;
		int width = 500;
		//Tank size
		int tankSize = 50;
		//Players
		int players = 2;
		//Lives
		int lives = 3;
		//Colors
		ArrayList<Color> colors = new ArrayList<Color>();
		colors.add(Color.RED);
		colors.add(Color.BLUE);
		colors.add(Color.GREEN);
		
		
		GameManager game = new GameManager(width, height, players, lives, tankSize, colors);
	}
}
