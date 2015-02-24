
public class WindTile implements Tile, Comparable<Tile> {
	private Wind kind_;
	
	public WindTile(Wind w) {
		kind_ = w;
	}
	
	public String toString() {
		String result = "";
		
		switch (kind_) {
		case EAST:
			result += "E";
			break;
		case SOUTH:
			result += "S";
			break;
		case WEST:
			result += "W";
			break;
		case NORTH:
			result += "N";
			break;
		default:
			result += "?";
		}
		
		return result;
	}
	
	public Wind getKind() {
		return kind_;
	}
	
	public Suit getSuit() {
		return Suit.WIND;
	}
	
	public boolean isTerminal() {
		return false;
	}
	
	public boolean isHonor() {
		return true;
	}
	
	public boolean equals(Object other) {
		if (other instanceof WindTile) {
			return kind_ == ((WindTile)other).kind_;
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
		
		if (other instanceof WindTile) {
			return ((WindTile)other).kind_.ordinal() - kind_.ordinal();
		} else if (other instanceof Tile) {
			return ((Tile)other).getSuit().ordinal() - this.getSuit().ordinal();
		} else {
			return 0;
		}
	}
}
