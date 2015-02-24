import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class Hand {
	public static final int NUMBER_OF_STARTING_TILES = 13;
	public static final int NUMBER_OF_MELDS_IN_REGULAR_HAND = 4;
	
	private ArrayList<Tile> tiles_;
	private ArrayList<Meld> melds_;
	
	public Hand() {
		tiles_ = new ArrayList<Tile>();
		melds_ = new ArrayList<Meld>();
	}
	
	public Hand(Hand h) {
		tiles_ = new ArrayList<Tile>(h.tiles_);
		melds_ = new ArrayList<Meld>(h.melds_);
	}
	
	public Hand(Wall wall) {
		this();
		dealHand(wall);
	}
	
	public ArrayList<Tile> getTiles() {
		return tiles_;
	}
	
	public ArrayList<Meld> getMelds() {
		return melds_;
	}
	
	public void dealHand(Wall wall) {
		for (int i = 0; i < NUMBER_OF_STARTING_TILES; ++i) {
			tiles_.add(wall.drawNext());
		}
	}
	
	// Returns true if the hand is 14 tiles.
	public boolean drawTile(Wall wall) {
		tiles_.add(wall.drawNext());
		return isFullHand();
	}
	
	// Returns true if the hand is 14 tiles.
	public boolean addTile(Tile t) {
		tiles_.add(t);
		return isFullHand();
	}
	
	public boolean isFullHand() {
		return 3*melds_.size() + tiles_.size() == NUMBER_OF_STARTING_TILES + 1;
	}
	
	public boolean isProperlyMeldedHand() {		
		for (int i = 0; i < melds_.size(); ++i) {
			if (!melds_.get(i).isValid()) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean isRegularHand() {
		if (!isProperlyMeldedHand()) {
			return false;
		}
		
		if (melds_.size() == NUMBER_OF_MELDS_IN_REGULAR_HAND
			&& findPairingTiles().size() == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public String toString() {
		String result = "";
		
		for (int i = 0; i < melds_.size(); ++i) {
			result += "(" + melds_.get(i) + ")";
		}
		
		List<Tile> sortedTiles = new ArrayList<Tile>(tiles_);
		Collections.sort(sortedTiles, Collections.reverseOrder());
		
		for (int i = 0; i < sortedTiles.size(); ++i) {
			if (i > 0) {
				result += ", ";
			}
			
			result += sortedTiles.get(i);
		}
		
		return result;
	}
	
	public boolean makeMeld(boolean ensureValid, int tileIndex1, int tileIndex2, int tileIndex3) {
		int minTileIndex = Math.min(Math.min(tileIndex1, tileIndex2), tileIndex3);
		int maxTileIndex = Math.max(Math.max(tileIndex1, tileIndex2), tileIndex3);
		
		HashSet<Tile> tileSet = new HashSet<Tile>(tiles_);
		
		// Ensure that the indices aren't out of bounds and that there are three unique indices.
		if (minTileIndex < 0 || maxTileIndex >= tiles_.size() || tileSet.size() != 3) {
			return false;
		}
		
		return makeMeld(ensureValid, tiles_.get(tileIndex1), tiles_.get(tileIndex2), tiles_.get(tileIndex3));
	}
	
	public boolean makeMeld(boolean ensureValid, int tileIndex1, int tileIndex2, int tileIndex3, int tileIndex4) {
		int minTileIndex = Math.min(Math.min(Math.min(tileIndex1, tileIndex2), tileIndex3), tileIndex4);
		int maxTileIndex = Math.max(Math.max(Math.max(tileIndex1, tileIndex2), tileIndex3), tileIndex4);
		
		HashSet<Tile> tileSet = new HashSet<Tile>(tiles_);
		
		// Ensure that the indices aren't out of bounds and that there are three unique indices.
		if (minTileIndex < 0 || maxTileIndex >= tiles_.size() || tileSet.size() != 4) {
			return false;
		}
		
		return makeMeld(ensureValid, tiles_.get(tileIndex1), tiles_.get(tileIndex2), tiles_.get(tileIndex3), tiles_.get(tileIndex4));
	}
	
	public boolean makeMeld(boolean ensureValid, Tile tile1, Tile tile2, Tile tile3) {
		// Build a list of the tiles that need to get melded.
		ArrayList<Tile> tilesToMeld = new ArrayList<Tile>();
		tilesToMeld.add(tile1);
		tilesToMeld.add(tile2);
		tilesToMeld.add(tile3);		
		
		// Confirm the tiles we need to meld are all available within the hand (unmelded).
		if (!tiles_.containsAll(tilesToMeld)) {
			return false;
		}
		
		// Make the meld as instructed out of the given tiles.
		Meld meld = new Meld(tile1, tile2, tile3);
		
		// If we need to ensure the meld is valid and it is not, abort before making changes to the hand.
		if (ensureValid && !meld.isValid()) {
			return false;
		}
		
		// Make the changes to the hand: create the meld and remove the corresponding tiles from the
		// unmelded part of the hand.
		melds_.add(meld);
		tiles_.remove(tile1);
		tiles_.remove(tile2);
		tiles_.remove(tile3);
		
		return meld.isValid();
	}
	
	public boolean makeMeld(boolean ensureValid, Tile tile1, Tile tile2, Tile tile3, Tile tile4) {
		// Build a list of the tiles that need to get melded.
		ArrayList<Tile> tilesToMeld = new ArrayList<Tile>();
		tilesToMeld.add(tile1);
		tilesToMeld.add(tile2);
		tilesToMeld.add(tile3);		
		tilesToMeld.add(tile4);
		
		// Confirm the tiles we need to meld are all available within the hand (unmelded).
		if (!tiles_.containsAll(tilesToMeld)) {
			return false;
		}
		
		// Make the meld as instructed out of the given tiles.
		Meld meld = new Meld(tile1, tile2, tile3, tile4);
		
		// If we need to ensure the meld is valid and it is not, abort before making changes to the hand.
		if (ensureValid && !meld.isValid()) {
			return false;
		}
		
		// Make the changes to the hand: create the meld and remove the corresponding tiles from the
		// unmelded part of the hand.
		melds_.add(meld);
		tiles_.remove(tile1);
		tiles_.remove(tile2);
		tiles_.remove(tile3);
		tiles_.remove(tile4);
		
		return meld.isValid();
	}
	
	public static ArrayList<Hand> generateMeldedHands(Hand h) {
		
		//System.out.println("Generating melded hand with: " + h);
		
		ArrayList<Hand> results = new ArrayList<Hand>();
		
		// If the hand is not full or properly melded, then it cannot be fully melded.
		if (!h.isFullHand() || !h.isProperlyMeldedHand()) {
			return results;
		}
		
		// If there are four melds and there is a single pair,
		// then the hand is fully melded.
		if (h.isRegularHand()) {
			results.add(h);
			return results;
		}
		
		// Iterate through every possible new meld.
		//System.out.println("h.getTiles().size():" + h.getTiles().size());
		for (int i = 0; i < h.getTiles().size() - 2; ++i) {
			//System.out.println(">i="+i);
			for (int j = i + 1; j < h.getTiles().size() - 1; ++j) {
				//System.out.println(">>j="+j);
				for (int k = j + 1; k < h.getTiles().size(); ++k) {
					//System.out.println(">>>k="+k);
					
					// Create a clone hand to meld further.
					Hand newHand = new Hand(h);
					
					//System.out.println("Making meld with: " + newHand.getTiles().get(i) + "[" + i + "]," + newHand.getTiles().get(j) + "[" + j + "]," + newHand.getTiles().get(k) + "[" + k + "]");
					newHand.makeMeld(false, newHand.getTiles().get(i), newHand.getTiles().get(j), newHand.getTiles().get(k));
					results.addAll(generateMeldedHands(newHand));
				}
			}
		}
		
		//System.out.println("Got here!");
		
		return results;
	}
	
	public ArrayList<Tile> findPairingTiles() {
		ArrayList<Tile> results = new ArrayList<Tile>();
		
		List<Tile> sortedTiles = new ArrayList<Tile>(tiles_);
		Collections.sort(sortedTiles, Collections.reverseOrder());
		
		// Start an examination through the sorted list.
		// Stop at the penultimate entry.
		int checkIndex = 0;
		while (checkIndex < sortedTiles.size() - 1) {
			Tile tileAtCheckIndex = sortedTiles.get(checkIndex);
			Tile tileAfterCheckIndex = sortedTiles.get(checkIndex + 1);
			
			// If the this and the next tile are the same,
			// add to result list and remove from this array.
			// Don't bump the check index if items get removed.
			if (tileAtCheckIndex.equals(tileAfterCheckIndex)) {
				results.add(tileAtCheckIndex);
				sortedTiles.remove(checkIndex);
				sortedTiles.remove(checkIndex);
			} else {
				++checkIndex;
			}
		}
		
		return results;
	}
	
	public int calculateScore(Wind seatWind) {
		ArrayList<ScoringPattern> scoringPatterns = new ArrayList<ScoringPattern>();
		
		scoringPatterns.add(getScoreForSevenPairs());
		scoringPatterns.add(getScoreForThirteenOrphans());
		scoringPatterns.add(getScoreForConcealedHand());
		scoringPatterns.add(getScoreForAllMiddleTiles());
		scoringPatterns.add(getScoreForOneSuit());
		scoringPatterns.add(getScoreForNineGates());
		scoringPatterns.add(getScoreForTerminals());
		scoringPatterns.addAll(getScoreForValueHonors(seatWind));
		scoringPatterns.add(getScoreForDragons());
		scoringPatterns.add(getScoreForWinds());
		scoringPatterns.add(getScoreForAllHonors());
		scoringPatterns.add(getScoreForSequences());
		scoringPatterns.add(getScoreForIdenticalSequences());
		scoringPatterns.add(getScoreForSimilarSequences());
		scoringPatterns.add(getScoreForNineTileStraight());
		scoringPatterns.add(getScoreForConcealedTriplets());
		scoringPatterns.add(getScoreForTriplets());
		scoringPatterns.add(getScoreForSimilarTriplets());
		scoringPatterns.add(getScoreForConsecutiveTriplets());
		scoringPatterns.add(getScoreForKong());
		
		return calculateScoreFromScoringPatterns(scoringPatterns);
	}
	
	public ScoringPattern getScoreForSevenPairs() {
		if (findPairingTiles().size() == 7) {
			return ScoringPattern.SEVEN_PAIRS;
		} else {
			return ScoringPattern.NONE;
		}
	}
	
	public ScoringPattern getScoreForThirteenOrphans() {
		// Ensure no melds.
		if (melds_.size() > 0) {
			return ScoringPattern.NONE;
		}
		
		// Check for each of the needed tiles.
		HashSet<Tile> tileSet = new HashSet<Tile>(tiles_);
		if (!tileSet.contains(new DragonTile(Dragon.RED))) return ScoringPattern.NONE;
		if (!tileSet.contains(new DragonTile(Dragon.GREEN))) return ScoringPattern.NONE;
		if (!tileSet.contains(new DragonTile(Dragon.WHITE))) return ScoringPattern.NONE;
		if (!tileSet.contains(new WindTile(Wind.EAST))) return ScoringPattern.NONE;
		if (!tileSet.contains(new WindTile(Wind.SOUTH))) return ScoringPattern.NONE;
		if (!tileSet.contains(new WindTile(Wind.WEST))) return ScoringPattern.NONE;
		if (!tileSet.contains(new WindTile(Wind.NORTH))) return ScoringPattern.NONE;
		try {
			if (!tileSet.contains(new SuitTile(Suit.DOT, SuitTile.LOW_TERMINAL))) return ScoringPattern.NONE;
			if (!tileSet.contains(new SuitTile(Suit.DOT, SuitTile.HIGH_TERMINAL))) return ScoringPattern.NONE;
			if (!tileSet.contains(new SuitTile(Suit.BAMBOO, SuitTile.LOW_TERMINAL))) return ScoringPattern.NONE;
			if (!tileSet.contains(new SuitTile(Suit.BAMBOO, SuitTile.HIGH_TERMINAL))) return ScoringPattern.NONE;
			if (!tileSet.contains(new SuitTile(Suit.CHARACTER, SuitTile.LOW_TERMINAL))) return ScoringPattern.NONE;
			if (!tileSet.contains(new SuitTile(Suit.CHARACTER, SuitTile.HIGH_TERMINAL))) return ScoringPattern.NONE;
		} catch (InvalidSuitTileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// There must also be a pair to explain the 14th tile.
		if (findPairingTiles().size() == 1) {
			return ScoringPattern.THIRTEEN_ORPHANS;
		} else {
			return ScoringPattern.NONE;
		}
	}
	
	public ScoringPattern getScoreForConcealedHand() {
		// Hand must be regular.
		if (!isRegularHand()) {
			return ScoringPattern.NONE;
		}
		
		// Check for any non-concealed meld, which would invalidate.
		for (int i = 0; i < melds_.size(); ++i) {
			if (!melds_.get(i).isConcealed()) {
				return ScoringPattern.NONE;
			}
		}
		
		return ScoringPattern.CONCEALED_HAND;
	}
	
	public ScoringPattern getScoreForAllMiddleTiles() {
		// Hand must be regular.
		if (!isRegularHand()) {
			return ScoringPattern.NONE;
		}
		
		// Check for any terminal meld, which would invalidate.
		for (int i = 0; i < melds_.size(); ++i) {
			if (melds_.get(i).isTerminal() || melds_.get(i).isHonor()) {
				return ScoringPattern.NONE;
			}
		}
		
		for (int i = 0; i < tiles_.size(); ++i) {
			if (tiles_.get(i).isTerminal() || tiles_.get(i).isHonor()) {
				return ScoringPattern.NONE;
			}
		}
		
		return ScoringPattern.ALL_MIDDLE_TILES;
	}
	
	public ScoringPattern getScoreForOneSuit() {
		// Hand must be regular.
		if (!isRegularHand()) {
			return ScoringPattern.NONE;
		}
		
		// Start by assuming a PURE ONE SUIT pattern.
		// Downgrade this pattern to MIXED ONE SUIT upon seeing an honor or
		// return NO PATTERN upon finding two mismatched suits.
		ScoringPattern result = ScoringPattern.PURE_ONE_SUIT;
		
		boolean foundKeySuit = false;
		Suit s = Suit.DRAGON;
		for (int i = 0; i < melds_.size(); ++i) {
			if (!foundKeySuit && melds_.get(i).isSuited()) {
				// If the key suit hasn't been found and we see a suit that's a DOT, BAMBOO, or CHARACTER,
				// then remember that suit and mark that we've found the key suit.
				foundKeySuit = true;
				s = melds_.get(i).getSuit();
			} else if (melds_.get(i).isHonor()) {
				// If we see a meld of the DRAGON or WIND suit, then downgrade the pattern to MIXED ONE SUIT.
				result = ScoringPattern.MIXED_ONE_SUIT;
			} else if (foundKeySuit && s != melds_.get(i).getSuit()) {
				return ScoringPattern.NONE;
			}
		}
		
		// If we get here, then we've validated that all of the melds are either honors
		// or a single suit. Now we check the pair.
		ArrayList<Tile> pairs = findPairingTiles();
		if (pairs.size() == 1) {
			if (pairs.get(0).isHonor()) {
				return ScoringPattern.MIXED_ONE_SUIT;
			} else if (foundKeySuit && s == pairs.get(0).getSuit()) {
				return result;
			} else {
				return ScoringPattern.NONE;
			}
		} else {
			return ScoringPattern.NONE;
		}
	}
	
	public ScoringPattern getScoreForNineGates() {
		// NEEDS TO BE IMPLEMENTED!
		return ScoringPattern.NONE;
	}
	
	public ScoringPattern getScoreForTerminals() {
		// Hand must be regular.
		if (!isRegularHand()) {
			return ScoringPattern.NONE;
		}
		
		// Start by assuming a PURE GREATER TERMINALS pattern.
		// Downgrade this pattern to MIXED upon seeing an honor,
		// downgrade this pattern to LESSER upon seeing a sequence, or
		// return NO PATTERN upon finding a non-terminal.
		ScoringPattern result = ScoringPattern.PURE_GREATER_TERMINALS;
		
		for (int i = 0; i < melds_.size(); ++i) {			
			if (melds_.get(i).getType() == Meld.Type.SEQUENCE) {
				// If the meld is a sequence, downgrade the current result to LESSER.
				if (result == ScoringPattern.PURE_GREATER_TERMINALS) {
					result = ScoringPattern.PURE_LESSER_TERMINALS;
				} else if (result == ScoringPattern.MIXED_GREATER_TERMINALS) {
					result = ScoringPattern.MIXED_LESSER_TERMINALS;
				}
			}
			
			if (melds_.get(i).isHonor()) {
				// If the meld is an honor, downgrade the current result to a MIXED pattern.
				if (result == ScoringPattern.PURE_GREATER_TERMINALS) {
					result = ScoringPattern.MIXED_GREATER_TERMINALS;
				} else if (result == ScoringPattern.PURE_LESSER_TERMINALS) {
					result = ScoringPattern.MIXED_LESSER_TERMINALS;
				}
			} else if (!melds_.get(i).isTerminal()) {
				// If the meld is not a terminal or honor, no pattern will score.
				return ScoringPattern.NONE;
			}
		}
		
		// If we get here, then we've validated that all of the melds are terminals or
		// honors. Now we check the pair.
		ArrayList<Tile> pairs = findPairingTiles();
		if (pairs.size() == 1) {
			if (pairs.get(0).isHonor()) {
				// If the meld is an honor, downgrade the current result to a MIXED pattern.
				if (result == ScoringPattern.PURE_GREATER_TERMINALS) {
					result = ScoringPattern.MIXED_GREATER_TERMINALS;
				} else if (result == ScoringPattern.PURE_LESSER_TERMINALS) {
					result = ScoringPattern.MIXED_LESSER_TERMINALS;
				}
				return result;
			} else if (pairs.get(0).isTerminal()) {
				return result;
			} else {
				return ScoringPattern.NONE;
			}
		} else {
			return ScoringPattern.NONE;
		}
	}
	
	public ArrayList<ScoringPattern> getScoreForValueHonors(Wind seatWind) {
		ArrayList<ScoringPattern> results = new ArrayList<ScoringPattern>();
		
		for (int i = 0; i < melds_.size(); ++i) {			
			if (melds_.get(i).getSuit() == Suit.DRAGON
				|| (melds_.get(i).getSuit() == Suit.WIND && ((WindTile)melds_.get(i).getKeyTile()).getKind() == seatWind)) {
				results.add(ScoringPattern.VALUE_HONOR);
			}
		}
		
		return results;
	}
	
	public ScoringPattern getScoreForDragons() {
		int numDragonTriplets = 0;
		for (int i = 0; i < melds_.size(); ++i) {			
			if (melds_.get(i).getSuit() == Suit.DRAGON
				&& (melds_.get(i).getType() == Meld.Type.TRIPLET || melds_.get(i).getType() == Meld.Type.KONG)) {
				++numDragonTriplets;
			}
		}
		
		int numDragonPairs = 0;
		ArrayList<Tile> pairs = findPairingTiles();
		if (pairs.size() == 1) {
			if (pairs.get(0).getSuit() == Suit.DRAGON) {
				++numDragonPairs;
			}
		}
		
		if (numDragonTriplets == 3 && numDragonPairs == 0) {
			return ScoringPattern.BIG_THREE_DRAGONS;
		} else if (numDragonTriplets == 2 && numDragonPairs == 1) {
			return ScoringPattern.SMALL_THREE_DRAGONS;
		} else {
			return ScoringPattern.NONE;
		}
	}
	
	public ScoringPattern getScoreForWinds() {
		int numWindTriplets = 0;
		for (int i = 0; i < melds_.size(); ++i) {			
			if (melds_.get(i).getSuit() == Suit.WIND
				&& (melds_.get(i).getType() == Meld.Type.TRIPLET || melds_.get(i).getType() == Meld.Type.KONG)) {
				++numWindTriplets;
			}
		}
		
		int numWindPairs = 0;
		ArrayList<Tile> pairs = findPairingTiles();
		if (pairs.size() == 1) {
			if (pairs.get(0).getSuit() == Suit.WIND) {
				++numWindPairs;
			}
		}
		
		if (numWindTriplets == 4 && numWindPairs == 0) {
			return ScoringPattern.BIG_FOUR_WINDS;
		} else if (numWindTriplets == 3 && numWindPairs == 1) {
			return ScoringPattern.SMALL_FOUR_WINDS;
		} else if (numWindTriplets == 3 && numWindPairs == 0) {
			return ScoringPattern.BIG_THREE_WINDS;
		} else if (numWindTriplets == 2 && numWindPairs == 1) {
			return ScoringPattern.SMALL_THREE_WINDS;
		} else {
			return ScoringPattern.NONE;
		}
	}
	
	public ScoringPattern getScoreForAllHonors() {
		// Hand must be regular.
		if (!isRegularHand()) {
			return ScoringPattern.NONE;
		}
		
		for (int i = 0; i < melds_.size(); ++i) {			
			if (!(melds_.get(i).isHonor())) {
				return ScoringPattern.NONE;
			}
		}
		
		// If we get here, then we've validated that all of the melds are honors. Now we check the pair.
		ArrayList<Tile> pairs = findPairingTiles();
		if (pairs.size() == 1) {
			if (pairs.get(0).isHonor()) {
				return ScoringPattern.ALL_HONORS;
			} else {
				return ScoringPattern.NONE;
			}
		} else {
			return ScoringPattern.NONE;
		}
	}
	
	public ScoringPattern getScoreForSequences() {		
		int numSequences = 0;
		for (int i = 0; i < melds_.size(); ++i) {
			if (melds_.get(i).getType() == Meld.Type.SEQUENCE) {
				++numSequences;
			}
		}
		
		if (numSequences == 4) {
			return ScoringPattern.FOUR_SEQUENCES;
		} else {
			return ScoringPattern.NONE;
		}
	}
	
	public ScoringPattern getScoreForIdenticalSequences() {
		// Collect the ranks of all of the sequence key tiles by suit.
		ArrayList<Integer> dotSeqKeyTileRanks = new ArrayList<Integer>();
		ArrayList<Integer> bambooSeqKeyTileRanks = new ArrayList<Integer>();
		ArrayList<Integer> characterSeqKeyTileRanks = new ArrayList<Integer>();
		
		for (int i = 0; i < melds_.size(); ++i) {			
			if (melds_.get(i).getType() == Meld.Type.SEQUENCE) {
				if (melds_.get(i).getSuit() == Suit.DOT) {
					dotSeqKeyTileRanks.add(((SuitTile)melds_.get(i).getKeyTile()).getRank());
				} else if (melds_.get(i).getSuit() == Suit.BAMBOO) {
					bambooSeqKeyTileRanks.add(((SuitTile)melds_.get(i).getKeyTile()).getRank());
				} else if (melds_.get(i).getSuit() == Suit.CHARACTER) {
					characterSeqKeyTileRanks.add(((SuitTile)melds_.get(i).getKeyTile()).getRank());
				}
			}
		}
		
		// Create hashsets to unique-ify and store into new lists that get sorted.
		ArrayList<Integer> uniqueDotSeqKeyTileRanksSorted = new ArrayList<Integer>(new HashSet<Integer>(dotSeqKeyTileRanks));
		Collections.sort(uniqueDotSeqKeyTileRanksSorted);
		ArrayList<Integer> uniqueBambooSeqKeyTileRanksSorted = new ArrayList<Integer>(new HashSet<Integer>(bambooSeqKeyTileRanks));
		Collections.sort(uniqueBambooSeqKeyTileRanksSorted);
		ArrayList<Integer> uniqueCharacterSeqKeyTileRanksSorted = new ArrayList<Integer>(new HashSet<Integer>(characterSeqKeyTileRanks));
		Collections.sort(uniqueCharacterSeqKeyTileRanksSorted);
		
		ArrayList<Integer> uniqueDotSeqKeyTileRanksSortedMultiples = new ArrayList<Integer>();
		ArrayList<Integer> uniqueBambooSeqKeyTileRanksSortedMultiples = new ArrayList<Integer>();
		ArrayList<Integer> uniqueCharacterSeqKeyTileRanksSortedMultiples = new ArrayList<Integer>();
		
		// Go through each unique sorted list and find the number of occurrences in the original list.
		// For dots:
		for (int i = 0; i < uniqueDotSeqKeyTileRanksSorted.size(); ++i) {
			uniqueDotSeqKeyTileRanksSortedMultiples.add(countOccurrences(dotSeqKeyTileRanks, uniqueDotSeqKeyTileRanksSorted.get(i)));
		}
		
		// For bamboos:
		for (int i = 0; i < uniqueBambooSeqKeyTileRanksSorted.size(); ++i) {
			uniqueBambooSeqKeyTileRanksSortedMultiples.add(countOccurrences(bambooSeqKeyTileRanks, uniqueBambooSeqKeyTileRanksSorted.get(i)));
		}
		
		// For characters:
		for (int i = 0; i < uniqueCharacterSeqKeyTileRanksSorted.size(); ++i) {
			uniqueCharacterSeqKeyTileRanksSortedMultiples.add(countOccurrences(characterSeqKeyTileRanks, uniqueCharacterSeqKeyTileRanksSorted.get(i)));
		}
		
		// Now the unique* lists have the multiples data.
		// Find instances of 4's, 3's, 2's, or 2's twice to get the scoring pattern.
		if (uniqueDotSeqKeyTileRanksSortedMultiples.contains(4)
			|| uniqueBambooSeqKeyTileRanksSortedMultiples.contains(4)
			|| uniqueCharacterSeqKeyTileRanksSortedMultiples.contains(4)) {
			return ScoringPattern.FOUR_IDENTICAL_SEQUENCES;
		} else if (uniqueDotSeqKeyTileRanksSortedMultiples.contains(3)
			|| uniqueBambooSeqKeyTileRanksSortedMultiples.contains(3)
			|| uniqueCharacterSeqKeyTileRanksSortedMultiples.contains(3)) {
			return ScoringPattern.THREE_IDENTICAL_SEQUENCES;
		} if (countOccurrences(uniqueDotSeqKeyTileRanksSortedMultiples, 2) == 2
			|| countOccurrences(uniqueBambooSeqKeyTileRanksSortedMultiples, 2) == 2
			|| countOccurrences(uniqueCharacterSeqKeyTileRanksSortedMultiples, 2) == 2) {
			return ScoringPattern.TWO_IDENTICAL_SEQUENCES_TWICE;
		} else if (uniqueDotSeqKeyTileRanksSortedMultiples.contains(2)
			|| uniqueBambooSeqKeyTileRanksSortedMultiples.contains(2)
			|| uniqueCharacterSeqKeyTileRanksSortedMultiples.contains(2)) {
			return ScoringPattern.TWO_IDENTICAL_SEQUENCES;
		} else {
			return ScoringPattern.NONE;
		}
	}
	
	private static int countOccurrences(ArrayList<Integer> a, int v) {
		int result = 0;
		for (int i = 0; i < a.size(); ++i) {
			if (a.get(i) == v) {
				++result;
			}
		}
		return result;
	}
	
	public ScoringPattern getScoreForSimilarSequences() {
		ArrayList<Integer> dotSeqKeyTileRanks = new ArrayList<Integer>();
		ArrayList<Integer> bambooSeqKeyTileRanks = new ArrayList<Integer>();
		ArrayList<Integer> characterSeqKeyTileRanks = new ArrayList<Integer>();
		
		for (int i = 0; i < melds_.size(); ++i) {			
			if (melds_.get(i).getType() == Meld.Type.SEQUENCE) {
				if (melds_.get(i).getSuit() == Suit.DOT) {
					dotSeqKeyTileRanks.add(((SuitTile)melds_.get(i).getKeyTile()).getRank());
				} else if (melds_.get(i).getSuit() == Suit.BAMBOO) {
					bambooSeqKeyTileRanks.add(((SuitTile)melds_.get(i).getKeyTile()).getRank());
				} else if (melds_.get(i).getSuit() == Suit.CHARACTER) {
					characterSeqKeyTileRanks.add(((SuitTile)melds_.get(i).getKeyTile()).getRank());
				}
			}
		}
		
		// Check for the same rank in every suit.
		for (int i = SuitTile.LOW_TERMINAL; i <= SuitTile.HIGH_TERMINAL; ++i) {
			if (dotSeqKeyTileRanks.contains(i)
				&& bambooSeqKeyTileRanks.contains(i)
				&& characterSeqKeyTileRanks.contains(i)) {
				return ScoringPattern.THREE_SIMILAR_SEQUENCES;
			}
		}
		
		return ScoringPattern.NONE;
	}
	
	public ScoringPattern getScoreForNineTileStraight() {
		ArrayList<Integer> dotSeqKeyTileRanks = new ArrayList<Integer>();
		ArrayList<Integer> bambooSeqKeyTileRanks = new ArrayList<Integer>();
		ArrayList<Integer> characterSeqKeyTileRanks = new ArrayList<Integer>();
		
		for (int i = 0; i < melds_.size(); ++i) {			
			if (melds_.get(i).getType() == Meld.Type.SEQUENCE) {
				if (melds_.get(i).getSuit() == Suit.DOT) {
					dotSeqKeyTileRanks.add(((SuitTile)melds_.get(i).getKeyTile()).getRank());
				} else if (melds_.get(i).getSuit() == Suit.BAMBOO) {
					bambooSeqKeyTileRanks.add(((SuitTile)melds_.get(i).getKeyTile()).getRank());
				} else if (melds_.get(i).getSuit() == Suit.CHARACTER) {
					characterSeqKeyTileRanks.add(((SuitTile)melds_.get(i).getKeyTile()).getRank());
				}
			}
		}
		
		
		boolean dotStraight = dotSeqKeyTileRanks.contains(SuitTile.LOW_SEQ_KEY_RANK)
				              && dotSeqKeyTileRanks.contains(SuitTile.MIDDLE_SEQ_KEY_RANK)
				              && dotSeqKeyTileRanks.contains(SuitTile.HIGH_SEQ_KEY_RANK);
		
		boolean bambooStraight = bambooSeqKeyTileRanks.contains(SuitTile.LOW_SEQ_KEY_RANK)
					             && bambooSeqKeyTileRanks.contains(SuitTile.MIDDLE_SEQ_KEY_RANK)
					             && bambooSeqKeyTileRanks.contains(SuitTile.HIGH_SEQ_KEY_RANK);
		
		boolean characterStraight = characterSeqKeyTileRanks.contains(SuitTile.LOW_SEQ_KEY_RANK)
						            && characterSeqKeyTileRanks.contains(SuitTile.MIDDLE_SEQ_KEY_RANK)
						            && characterSeqKeyTileRanks.contains(SuitTile.HIGH_SEQ_KEY_RANK);
		
		return dotStraight || bambooStraight || characterStraight
			   ? ScoringPattern.NINE_TILE_STRAIGHT
			   : ScoringPattern.NONE;
	}
	
	public ScoringPattern getScoreForConcealedTriplets() {		
		int numConcealedTriplets = 0;
		for (int i = 0; i < melds_.size(); ++i) {
			if ((melds_.get(i).getType() == Meld.Type.TRIPLET || melds_.get(i).getType() == Meld.Type.KONG)
				&& melds_.get(i).isConcealed()) {
				++numConcealedTriplets;
			}
		}
		
		if (numConcealedTriplets == 4) {
			return ScoringPattern.FOUR_CONCEALED_TRIPLETS;
		} else if (numConcealedTriplets == 4) {
			return ScoringPattern.THREE_CONCEALED_TRIPLETS;
		} else if (numConcealedTriplets == 2) {
			return ScoringPattern.TWO_CONCEALED_TRIPLETS;
		} else {
			return ScoringPattern.NONE;
		}
	}
	
	public ScoringPattern getScoreForTriplets() {	
		int numTriplets = 0;
		for (int i = 0; i < melds_.size(); ++i) {
			if (melds_.get(i).getType() == Meld.Type.TRIPLET || melds_.get(i).getType() == Meld.Type.KONG) {
				++numTriplets;
			}
		}
		
		if (numTriplets == 4) {
			return ScoringPattern.FOUR_TRIPLETS;
		} else {
			return ScoringPattern.NONE;
		}
	}
	
	public ScoringPattern getScoreForSimilarTriplets() {
		ArrayList<Integer> dotTripletKeyTileRanks = new ArrayList<Integer>();
		ArrayList<Integer> bambooTripletKeyTileRanks = new ArrayList<Integer>();
		ArrayList<Integer> characterTripletKeyTileRanks = new ArrayList<Integer>();
		
		for (int i = 0; i < melds_.size(); ++i) {			
			if (melds_.get(i).getType() == Meld.Type.TRIPLET || melds_.get(i).getType() == Meld.Type.KONG) {
				if (melds_.get(i).getSuit() == Suit.DOT) {
					dotTripletKeyTileRanks.add(((SuitTile)melds_.get(i).getKeyTile()).getRank());
				} else if (melds_.get(i).getSuit() == Suit.BAMBOO) {
					bambooTripletKeyTileRanks.add(((SuitTile)melds_.get(i).getKeyTile()).getRank());
				} else if (melds_.get(i).getSuit() == Suit.CHARACTER) {
					characterTripletKeyTileRanks.add(((SuitTile)melds_.get(i).getKeyTile()).getRank());
				}
			}
		}
		
		ArrayList<Tile> pairs = findPairingTiles();
		int pairRank = 0;
		Suit pairSuit = Suit.DRAGON;
		boolean validPair = false;
		if (pairs.size() == 1) {
			pairSuit = pairs.get(0).getSuit();
			if (pairSuit == Suit.DOT
				|| pairSuit == Suit.BAMBOO
				|| pairSuit == Suit.CHARACTER) {
				pairRank = ((SuitTile)pairs.get(0)).getRank();
				validPair = true;
			}
		}
		
		// Check for the same rank in every suit.
		for (int i = SuitTile.LOW_TERMINAL; i <= SuitTile.HIGH_TERMINAL; ++i) {
			boolean dotHasTriplet = dotTripletKeyTileRanks.contains(i);
			boolean bambooHasTriplet = bambooTripletKeyTileRanks.contains(i);
			boolean characterHasTriplet = characterTripletKeyTileRanks.contains(i);
			boolean dotHasPair = validPair && pairSuit == Suit.DOT && pairRank == i;
			boolean bambooHasPair = validPair && pairSuit == Suit.BAMBOO && pairRank == i;
			boolean characterHasPair = validPair && pairSuit == Suit.CHARACTER && pairRank == i;
			
			if (dotHasTriplet && bambooHasTriplet && characterHasTriplet) {
				return ScoringPattern.THREE_SIMILAR_TRIPLETS;
			} else if (dotHasTriplet && bambooHasTriplet && characterHasPair) {
				return ScoringPattern.SMALL_THREE_SIMILAR_TRIPLETS;
			} else if (dotHasTriplet && bambooHasPair && characterHasTriplet) {
				return ScoringPattern.SMALL_THREE_SIMILAR_TRIPLETS;
			} else if (dotHasPair && bambooHasTriplet && characterHasTriplet) {
				return ScoringPattern.SMALL_THREE_SIMILAR_TRIPLETS;
			}
		}
		
		return ScoringPattern.NONE;
	}
	
	public ScoringPattern getScoreForConsecutiveTriplets() {
		// Go through the hand to find triplets and store the ranks found for each suit.
		ArrayList<Integer> dotTripletKeyTileRanks = new ArrayList<Integer>();
		ArrayList<Integer> bambooTripletKeyTileRanks = new ArrayList<Integer>();
		ArrayList<Integer> characterTripletKeyTileRanks = new ArrayList<Integer>();
		
		for (int i = 0; i < melds_.size(); ++i) {			
			if (melds_.get(i).getType() == Meld.Type.TRIPLET || melds_.get(i).getType() == Meld.Type.KONG) {
				if (melds_.get(i).getSuit() == Suit.DOT) {
					dotTripletKeyTileRanks.add(((SuitTile)melds_.get(i).getKeyTile()).getRank());
				} else if (melds_.get(i).getSuit() == Suit.BAMBOO) {
					bambooTripletKeyTileRanks.add(((SuitTile)melds_.get(i).getKeyTile()).getRank());
				} else if (melds_.get(i).getSuit() == Suit.CHARACTER) {
					characterTripletKeyTileRanks.add(((SuitTile)melds_.get(i).getKeyTile()).getRank());
				}
			}
		}
		
		// Sort the suit lists to be checked for consecutive elements.
		Collections.sort(dotTripletKeyTileRanks);
		Collections.sort(bambooTripletKeyTileRanks);
		Collections.sort(characterTripletKeyTileRanks);
		
		int longestConsecutiveDot = longestConsecutiveRun(dotTripletKeyTileRanks);
		int longestConsecutiveBamboo = longestConsecutiveRun(bambooTripletKeyTileRanks);
		int longestConsecutiveCharacter = longestConsecutiveRun(characterTripletKeyTileRanks);
		
		if (longestConsecutiveDot == 4 || longestConsecutiveBamboo == 4 || longestConsecutiveCharacter == 4) {
			return ScoringPattern.FOUR_CONSECUTIVE_TRIPLETS;
		} else if (longestConsecutiveDot == 3 || longestConsecutiveBamboo == 3 || longestConsecutiveCharacter == 3) {
			return ScoringPattern.THREE_CONSECUTIVE_TRIPLETS;
		} else {
			return ScoringPattern.NONE;
		}
	}
	
	private static int longestConsecutiveRun(ArrayList<Integer> list) {
		// Assumes a sorted list.
		
		if (list.size() <= 1) {
			return list.size();
		}
		
		boolean isIncreasing = true;
		int result = 1;
		int tempResult = 1;
		for (int i = 1; i < list.size(); ++i) {
			int consecutiveElementDiff = list.get(i) - list.get(i - 1);			
			if (i == 1 && consecutiveElementDiff < 0) {
				isIncreasing = false;
			}
			if ((isIncreasing && consecutiveElementDiff == 1)
				|| (!isIncreasing && consecutiveElementDiff == -1)) {
				++tempResult;
			} else {
				result = Math.max(result, tempResult);
				tempResult = 1;
			}
		}
		
		result = Math.max(result, tempResult);
		return result;
	}
	
	public ScoringPattern getScoreForKong() {
		// Hand must be regular.
		if (!isRegularHand()) {
			return ScoringPattern.NONE;
		}
		
		int numKong = 0;
		for (int i = 0; i < melds_.size(); ++i) {
			if (melds_.get(i).getType() == Meld.Type.KONG) {
				++numKong;
			}
		}
		
		if (numKong == 4) {
			return ScoringPattern.FOUR_KONG;
		} else if (numKong == 3) {
			return ScoringPattern.THREE_KONG;
		} else if (numKong == 2) {
			return ScoringPattern.TWO_KONG;
		} else if (numKong == 1) {
			return ScoringPattern.ONE_KONG;
		} else {
			return ScoringPattern.NONE;
		}
	}
		
	public static int calculateScoreFromScoringPatterns(ArrayList<ScoringPattern> scoringPatterns) {
		int result = 0;
		
		// Does NOT enforce a single pattern per series.
		// Does enforce the limit of 320.
		
		boolean reachedLimit = false;
		for (int i = 0; i < scoringPatterns.size() && !reachedLimit; ++i) {
			int score = calculateScoreFromScoringPattern(scoringPatterns.get(i));
			reachedLimit = score >= 320;
			result += score;
		}
		
		// If we know we reached a limit, result the non-limit bound result.
		// Otherwise, cap the maximum additive total to 320 and give 1 for
		// a score of 0.
		return reachedLimit ? result : Math.max(Math.min(result, 320), 1);
	}
	
	public static int calculateScoreFromScoringPattern(ScoringPattern scoringPattern) {
		switch (scoringPattern) {
		case SEVEN_PAIRS:
			return 30;			
		case THIRTEEN_ORPHANS:
			return 160;
		case CONCEALED_HAND:
			return 5;
		case ALL_MIDDLE_TILES:
			return 5;
		case MIXED_ONE_SUIT:
			return 40;
		case PURE_ONE_SUIT:
			return 80;
		case NINE_GATES:
			return 480;
		case MIXED_LESSER_TERMINALS:
			return 40;
		case PURE_LESSER_TERMINALS:
			return 50;
		case MIXED_GREATER_TERMINALS:
			return 100;
		case PURE_GREATER_TERMINALS:
			return 400;
		case VALUE_HONOR:
			return 10;
		case SMALL_THREE_DRAGONS:
			return 40;
		case BIG_THREE_DRAGONS:
			return 130;
		case SMALL_THREE_WINDS:
			return 30;
		case SMALL_FOUR_WINDS:
			return 320;
		case BIG_THREE_WINDS:
			return 120;
		case BIG_FOUR_WINDS:
			return 400;
		case ALL_HONORS:
			return 320;
		case FOUR_SEQUENCES:
			return 5;
		case TWO_IDENTICAL_SEQUENCES:
			return 10;
		case THREE_IDENTICAL_SEQUENCES:
			return 120;
		case FOUR_IDENTICAL_SEQUENCES:
			return 480;
		case TWO_IDENTICAL_SEQUENCES_TWICE:
			return 60;
		case THREE_SIMILAR_SEQUENCES:
			return 35;
		case NINE_TILE_STRAIGHT:
			return 40;
		case TWO_CONCEALED_TRIPLETS:
			return 5;
		case THREE_CONCEALED_TRIPLETS:
			return 30;
		case FOUR_CONCEALED_TRIPLETS:
			return 125;
		case FOUR_TRIPLETS:
			return 30;
		case SMALL_THREE_SIMILAR_TRIPLETS:
			return 30;
		case THREE_SIMILAR_TRIPLETS:
			return 120;
		case THREE_CONSECUTIVE_TRIPLETS:
			return 100;
		case FOUR_CONSECUTIVE_TRIPLETS:
			return 200;
		case ONE_KONG:
			return 5;
		case TWO_KONG:
			return 20;
		case THREE_KONG:
			return 120;
		case FOUR_KONG:
			return 480;
		case NONE:
		default:
			return 0;
		}
	}
	
	public boolean equals(Object other) {
		//System.out.println("Comparing: " + this + " to " + other);
		if (other instanceof Hand) {
			List<Tile> thisSortedTiles = new ArrayList<Tile>(tiles_);
			Collections.sort(thisSortedTiles, Collections.reverseOrder());
			List<Tile> otherSortedTiles = new ArrayList<Tile>(((Hand)other).tiles_);
			Collections.sort(otherSortedTiles, Collections.reverseOrder());
			List<Meld> thisSortedMelds = new ArrayList<Meld>(melds_);
			Collections.sort(thisSortedMelds, Collections.reverseOrder());
			List<Meld> otherSortedMelds = new ArrayList<Meld>(((Hand)other).melds_);
			Collections.sort(otherSortedMelds, Collections.reverseOrder());
			return thisSortedTiles.equals(otherSortedTiles) && thisSortedMelds.equals(otherSortedMelds);
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		List<Tile> sortedTiles = new ArrayList<Tile>(tiles_);
		Collections.sort(sortedTiles, Collections.reverseOrder());
		List<Meld> sortedMelds = new ArrayList<Meld>(melds_);
		Collections.sort(sortedMelds, Collections.reverseOrder());
		
		int result = 0;
		for (int i = 0; i < sortedTiles.size(); ++i) {
			result += sortedTiles.get(i).hashCode();
		}
		for (int i = 0; i < sortedMelds.size(); ++i) {
			result += sortedMelds.get(i).hashCode();
		}
		return result;
	}
}
