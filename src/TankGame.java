import java.awt.Color;
import java.util.ArrayList;

public class TankGame {

	public static void main(String[] args) {
		//Game variables
		//Board
		int height = 1000;
		int width = 1000;
		
		//Tank stats
		int tankSize = 50;
		int reloadTime = 10;
		int startShields = 10;
		
		//Players
		int players = 1;
		boolean hasBot = true;
		
		//Colors
		ArrayList<Color> colors = new ArrayList<Color>();
		colors.add(Color.RED);
		colors.add(Color.BLUE);
		colors.add(Color.GREEN);
		colors.add(Color.YELLOW);
		colors.add(Color.CYAN);
		colors.add(Color.LIGHT_GRAY);
		
		GameManager game = new GameManager(width, height, players, startShields, tankSize, reloadTime, colors, hasBot);
	}
}


//TODO

//Bot:
//Make bullet avoiding better - always choose vector away from bullet OR towards middle
//Rewrite without axes
//Don't spawn outside borders
//Fix player mvmt