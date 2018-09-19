
public class Field{

	private int[] size;
	public Field(int width, int height) {
		
		size = new int[]{width, height};
		
	}
	
	public int width() {
		return size[0];
	}
	public int height() {
		return size[1];
	}

}
