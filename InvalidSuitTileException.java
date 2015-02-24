
public class InvalidSuitTileException extends Exception {
	private static final long serialVersionUID = -7236908209929058014L;
	private Suit suit_;
	private int rank_;
	
	public InvalidSuitTileException(Suit s, int r) {
		suit_ = s;
		rank_ = r;
	}
	
	public String toString() {
		return "Exception creating SuitTile of suit " + suit_ + " and rank " + rank_ + ".";
	}
}
