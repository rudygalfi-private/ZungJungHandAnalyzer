
public interface Tile extends Comparable<Tile> {
	public Suit getSuit();
	public boolean isTerminal();
	public boolean isHonor();
	public String toString();
}
