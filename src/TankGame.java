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
		int startShields = 3;
		
		//Players
		int players = 2;
		
		//Colors
		ArrayList<Color> colors = new ArrayList<Color>();
		colors.add(Color.RED);
		colors.add(Color.BLUE);
		colors.add(Color.GREEN);
		
		
		GameManager game = new GameManager(width, height, players, startShields, tankSize, reloadTime, colors);
	}
}


//TODO

//Bot:
<<<<<<< HEAD
//Add bullet dodging / shooting
//Prioritize boundary distance > target distance **
=======
//Make bullet avoiding better - always choose vector away from bullet OR towards middle
//stop getting caught in corners
>>>>>>> cc235cb... successfully implemented bot firing bullets
