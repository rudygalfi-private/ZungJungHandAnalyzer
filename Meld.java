import java.util.ArrayList;


public class Meld implements Comparable<Meld> {
	
	public enum Type {
		SEQUENCE,
		TRIPLET,
		KONG,
	}
	
	private boolean isConcealed_ = false;
	private Type type_;
	
	// The key tile is:
	// - In the case of TRIPLET or KONG: the relevant suit tile
	// - In the case of a SEQUENCE: the middle-rank tile (the key tile can never be terminal)
	private Tile keyTile_;
	
	private boolean isValid_ = false;
	
	public Meld(Tile a, Tile b, Tile c) {
		if (!(isValid_ = validateAndCreateMeld(a, b, c))) {
			//System.out.println("Error! Meld was not valid.");
		}
	}
	
	public Meld(Tile a, Tile b, Tile c, Tile d) {
		if (!(isValid_ = validateAndCreateMeld(a, b, c, d))) {
			//System.out.println("Error! Meld was not valid.");
		}
	}
	
	public Suit getSuit() {
		return keyTile_.getSuit();
	}
	
	public Type getType() {
		return type_;
	}
	
	public Tile getKeyTile() {
		return keyTile_;
	}
	
	public boolean isValid() {
		return isValid_;
	}
	
	public boolean isConcealed() {
		return isConcealed_;
	}
	
	public boolean isTerminal() {
		switch (type_) {
		case SEQUENCE:
			// For a sequence, the key tile will be one off the terminal tile.
			if (keyTile_ instanceof SuitTile) {
				return ((SuitTile)keyTile_).getRank() - 1 == SuitTile.LOW_TERMINAL
					   || ((SuitTile)keyTile_).getRank() + 1 == SuitTile.HIGH_TERMINAL;
			} else {
				return false;
			}
		case TRIPLET:
		case KONG:
			// For a triplet or kong, the key tile will have a terminal rank.
			if (keyTile_ instanceof SuitTile) {
				return ((SuitTile)keyTile_).getRank() == SuitTile.LOW_TERMINAL
					   || ((SuitTile)keyTile_).getRank() == SuitTile.HIGH_TERMINAL;
			} else {
				return false;
			}
		default:
			return false;
		}
	}
	
	public boolean isHonor() {
		return keyTile_.getSuit() == Suit.DRAGON || keyTile_.getSuit() == Suit.WIND;
	}
	
	public boolean isSuited() {
		return keyTile_.getSuit() == Suit.DOT || keyTile_.getSuit() == Suit.BAMBOO || keyTile_.getSuit() == Suit.CHARACTER;
	}
	
	public String toString() {
		if (!isValid_) return "???";
		
		String result = "";
		
		switch (type_) {
		case SEQUENCE:
			int keyRank = ((SuitTile)keyTile_).getRank();
			Suit keySuit = ((SuitTile)keyTile_).getSuit();
			try {
				SuitTile lowerTile = new SuitTile(keySuit, keyRank - 1);
				SuitTile keyTile = new SuitTile(keySuit, keyRank);
				SuitTile higherTile = new SuitTile(keySuit, keyRank + 1);
				result += lowerTile + "," + keyTile + "," + higherTile;
			} catch (InvalidSuitTileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case TRIPLET:
			result += keyTile_ + "," + keyTile_ + "," + keyTile_;
			break;
		case KONG:
			result += keyTile_ + "," + keyTile_ + "," + keyTile_ + "," + keyTile_;
			break;
		default:
			result += "???";
		}
		
		return result;
	}
	
	public boolean validateAndCreateMeld(Tile a, Tile b, Tile c) {
		// Check that the tiles are all the same suit.
		if (!(a.getSuit() == b.getSuit() && b.getSuit() == c.getSuit())) {
			return false;
		}
		
		// Get suit we're dealing with. Any tile will do.
		Suit suit = a.getSuit();
		
		if (suit == Suit.DRAGON) {
			if (((DragonTile)a).getKind() == ((DragonTile)b).getKind()
				&& ((DragonTile)b).getKind() == ((DragonTile)c).getKind()) {
				type_ = Type.TRIPLET;
				keyTile_ = a;
				return true;
			} else {
				return false;
			}
		} else if (suit == Suit.WIND) {
			if (((WindTile)a).getKind() == ((WindTile)b).getKind()
					&& ((WindTile)b).getKind() == ((WindTile)c).getKind()) {
					type_ = Type.TRIPLET;
					keyTile_ = a;
					return true;
			} else {
				return false;
			}
		} else if (suit != Suit.DOT && suit != Suit.BAMBOO && suit != Suit.CHARACTER) {
			return false;
		} else {
			SuitTile aSuit = (SuitTile)a;
			int aRank = aSuit.getRank();
			SuitTile bSuit = (SuitTile)b;
			int bRank = bSuit.getRank();
			SuitTile cSuit = (SuitTile)c;
			int cRank = cSuit.getRank();
			
			int minRank = Math.min(Math.min(aRank, bRank), cRank);
			int maxRank = Math.max(Math.max(aRank, bRank), cRank);
			
			if (minRank == maxRank) {
				type_ = Type.TRIPLET;
				keyTile_ = a;
				return true;
			} else if (maxRank - minRank == 2) {
				type_ = Type.SEQUENCE;
				
				if (aRank != minRank && aRank != maxRank) {
					keyTile_ = a;
				} else if (bRank != minRank && bRank != maxRank) {
					keyTile_ = b;
				} else if (cRank != minRank && cRank != maxRank) {
					keyTile_ = c;
				} else {
					// This is the case when two of the three tiles are exactly the same
					// (and the two kinds of tiles are separated by two, e.g. d5 d5 d7).
					return false;
				}
				
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean validateAndCreateMeld(Tile a, Tile b, Tile c, Tile d) {
		// We're dealing with a KONG case. Make sure the first three tiles would meld.
		if (validateAndCreateMeld(a, b, c)) {			
			// Check that the meld suit matches the 4th tile and that the meld type
			// is a triplet (in anticipation of promoting to KONG).
			if (this.getKeyTile().getSuit() == d.getSuit() && this.getType() == Type.TRIPLET) {
				// Check that the 4th tile matches the key tile. If so, promote to KONG.
				if (this.getKeyTile().equals(d)) {
					type_ = Type.KONG;
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean equals(Object other) {
		if (other instanceof Meld) {
			return isConcealed_ == ((Meld)other).isConcealed_
				   && type_ == ((Meld)other).type_
				   && keyTile_.equals(((Meld)other).keyTile_)
				   && isValid_ == ((Meld)other).isValid_;
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return ((Boolean)isConcealed_).hashCode()
			   + type_.hashCode()
			   + keyTile_.hashCode()
			   + ((Boolean)isValid_).hashCode();
	}
	
	public int compareTo(Meld other) {
		if (equals(other)) {
			return 0;
		}
		
		return keyTile_.compareTo(other.keyTile_);
	}
}
