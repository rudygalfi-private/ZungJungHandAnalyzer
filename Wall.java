import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;


public class Wall {

	public static final int NUMBER_OF_EACH_UNIQUE_TILE = 4;

	private Stack<Tile> wall_;
	
	public Wall() {
		// Create one of each tile
		ArrayList<Tile> setOfEachTileOnce = new ArrayList<Tile>();
		
		// Create dragons
		setOfEachTileOnce.add(new DragonTile(Dragon.RED));
		setOfEachTileOnce.add(new DragonTile(Dragon.GREEN));
		setOfEachTileOnce.add(new DragonTile(Dragon.WHITE));
		
		// Create winds
		setOfEachTileOnce.add(new WindTile(Wind.EAST));
		setOfEachTileOnce.add(new WindTile(Wind.SOUTH));
		setOfEachTileOnce.add(new WindTile(Wind.WEST));
		setOfEachTileOnce.add(new WindTile(Wind.NORTH));
		
		// Create dots
		for (int rank = SuitTile.LOW_TERMINAL; rank <= SuitTile.HIGH_TERMINAL; ++rank) {
			try {
				setOfEachTileOnce.add(new SuitTile(Suit.DOT, rank));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Create bamboos
		for (int rank = SuitTile.LOW_TERMINAL; rank <= SuitTile.HIGH_TERMINAL; ++rank) {
			try {
				setOfEachTileOnce.add(new SuitTile(Suit.BAMBOO, rank));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Create characters
		for (int rank = SuitTile.LOW_TERMINAL; rank <= SuitTile.HIGH_TERMINAL; ++rank) {
			try {
				setOfEachTileOnce.add(new SuitTile(Suit.CHARACTER, rank));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}		

		// Add each unique tile to the wall four times
		ArrayList<Tile> wallTiles = new ArrayList<Tile>();
		
		for (int multiple = 0; multiple < NUMBER_OF_EACH_UNIQUE_TILE; ++multiple) {
			for (int uniqueTileIndex = 0; uniqueTileIndex < setOfEachTileOnce.size(); ++uniqueTileIndex) {
				wallTiles.add(setOfEachTileOnce.get(uniqueTileIndex));
			}
		}
		
		Collections.shuffle(wallTiles);
		
		wall_ = new Stack<Tile>();
		
		for (int i = 0; i < wallTiles.size(); ++i) {
			wall_.push(wallTiles.get(i));
		}
	}
	
	public Tile drawNext() {
		return wall_.pop();
	}
	
	public int getRemainingTileCount() {
		return wall_.size();
	}
	
	public List<Tile> getRemainingTiles() {
		return wall_;
	}
	
	public String toString() {
		String result = "";
		
		// Print out each tile in the wall
		for (int wallIndex = 0; wallIndex < wall_.size(); ++wallIndex) {
			if (wallIndex > 0) {
				result += ",";
			}
			
			result += wall_.get(wallIndex).toString();
		}
		
		return result;
	}
}
