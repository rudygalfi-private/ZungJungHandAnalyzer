
public class SuitTile implements Tile, Comparable<Tile> {
	private Suit suit_;
	private int rank_;
	
	public static final int LOW_TERMINAL = 1;
	public static final int HIGH_TERMINAL = 9;
	public static final int NUMBER_OF_TILES_PER_SUIT = HIGH_TERMINAL - LOW_TERMINAL + 1;
	public static final int LOW_SEQ_KEY_RANK = 2;
	public static final int MIDDLE_SEQ_KEY_RANK = 5;
	public static final int HIGH_SEQ_KEY_RANK = 8;
	
	public SuitTile(Suit suit, int rank) throws InvalidSuitTileException {
		if (rank < LOW_TERMINAL || HIGH_TERMINAL < rank) {
			throw new InvalidSuitTileException(suit, rank);
		} else if (suit != Suit.DOT && suit != Suit.BAMBOO && suit != Suit.CHARACTER) {
			throw new InvalidSuitTileException(suit, rank);
		}
		
		rank_ = rank;
		suit_ = suit;
	}
	
	public String toString() {
		String result = "";
		
		switch (suit_) {
		case DOT:
			result += "d";
			break;
		case BAMBOO:
			result += "b";
			break;
		case CHARACTER:
			result += "c";
			break;
		default:
			result += "_";
		}
		
		if (LOW_TERMINAL <= rank_ && rank_ <= HIGH_TERMINAL) {
			result += rank_;
		} else {
			result += "?";
		}
		
		return result;
	}
	
	public int getRank() {
		if (LOW_TERMINAL <= rank_ && rank_ <= HIGH_TERMINAL) {
			return rank_;
		} else {
			return 0;
		}
	}
	
	public Suit getSuit() {
		return suit_;
	}
	
	public boolean isTerminal() {
		return rank_ == LOW_TERMINAL || rank_ == HIGH_TERMINAL;
	}
	
	public boolean isHonor() {
		return false;
	}
	
	public boolean equals(Object other) {
		if (other instanceof SuitTile) {
			return suit_ == ((SuitTile)other).suit_ && rank_ == ((SuitTile)other).rank_;
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return suit_.hashCode() + ((Integer)rank_).hashCode();
	}
	
	public int compareTo(Tile other) {
		if (equals(other)) {
			return 0;
		}
		
		if (other instanceof SuitTile) {
			if (this.suit_ == ((SuitTile)other).suit_) {
				return ((SuitTile)other).rank_ - this.rank_;
			} else {
				int otherOrdinal = NUMBER_OF_TILES_PER_SUIT*((SuitTile)other).getSuit().ordinal() + ((SuitTile)other).getRank();
				int thisOrdinal = NUMBER_OF_TILES_PER_SUIT*this.getSuit().ordinal() + this.getRank();
				return otherOrdinal - thisOrdinal;
			}
		} else {
			return other.getSuit().ordinal() - this.getSuit().ordinal();
		}
	}
}
