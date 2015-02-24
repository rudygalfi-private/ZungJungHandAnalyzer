
public class DragonTile implements Tile, Comparable<Tile> {
	private Dragon kind_;
	
	public DragonTile(Dragon d) {
		kind_ = d;
	}
	
	public String toString() {
		String result = "";
		
		switch (kind_) {
		case RED:
			result += "R";
			break;
		case GREEN:
			result += "G";
			break;
		case WHITE:
			result += "#";
			break;
		default:
			result += "?";
		}
		
		return result;
	}
	
	public Dragon getKind() {
		return kind_;
	}
	
	public Suit getSuit() {
		return Suit.DRAGON;
	}
	
	public boolean isTerminal() {
		return false;
	}
	
	public boolean isHonor() {
		return true;
	}
	
	public boolean equals(Object other) {
		if (other instanceof DragonTile) {
			return kind_ == ((DragonTile)other).kind_;
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return kind_.hashCode();
	}
	
	public int compareTo(Tile other) {
		if (equals(other)) {
			return 0;
		}
		
		if (other instanceof DragonTile) {
			return ((DragonTile)other).kind_.ordinal() - kind_.ordinal();
		} else if (other instanceof Tile) {
			return ((Tile)other).getSuit().ordinal() - this.getSuit().ordinal();
		} else {
			return 0;
		}
	}
}
