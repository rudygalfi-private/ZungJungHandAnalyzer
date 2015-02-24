import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


public class ZungJungHandAnalyzer {
	
	public static final int NUM_TRIALS = 1;
	
	public static void main(String[] args) {
		for (int i = 0; i < NUM_TRIALS; ++i) {
			System.out.println("Trial " + i + " in progress...");
			Wall wall = new Wall();
			
			Hand eastHand = new Hand(wall);
			Hand southHand = new Hand(wall);
			Hand westHand = new Hand(wall);
			Hand northHand = new Hand(wall);
			
			eastHand.drawTile(wall);
			
			//System.out.println("EAST HAND: " + eastHand);
			//System.out.println("SOUTH HAND: " + southHand);
			//System.out.println("WEST HAND: " + westHand);
			//System.out.println("NORTH HAND: " + northHand);
			
			//System.out.println("WALL: " + wall + " (" + wall.getRemainingTileCount() + ")");
			
			Hand sampleHand = new Hand();
			try {
				sampleHand.addTile(new SuitTile(Suit.BAMBOO, 3));
				sampleHand.addTile(new SuitTile(Suit.BAMBOO, 3));
				sampleHand.addTile(new SuitTile(Suit.BAMBOO, 3));
				sampleHand.addTile(new SuitTile(Suit.DOT, 3));
				sampleHand.addTile(new SuitTile(Suit.DOT, 3));
				sampleHand.addTile(new SuitTile(Suit.DOT, 3));
				sampleHand.addTile(new SuitTile(Suit.CHARACTER, 6));
				sampleHand.addTile(new SuitTile(Suit.CHARACTER, 6));
				sampleHand.addTile(new SuitTile(Suit.CHARACTER, 6));
				sampleHand.addTile(new SuitTile(Suit.BAMBOO, 7));
				sampleHand.addTile(new SuitTile(Suit.BAMBOO, 8));
				sampleHand.addTile(new SuitTile(Suit.BAMBOO, 9));
				sampleHand.addTile(new SuitTile(Suit.CHARACTER, 3));
				sampleHand.addTile(new SuitTile(Suit.CHARACTER, 3));
			} catch (InvalidSuitTileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ArrayList<Hand> meldedHands = new ArrayList<Hand>();
			meldedHands = Hand.generateMeldedHands(sampleHand);
			//System.out.println("Melded hands = " + meldedHands.size());
			for (int hand = 0; hand < meldedHands.size(); ++hand) {
				//System.out.println("Hand: " + meldedHands.get(hand));
				//System.out.println(meldedHands.get(0).equals(meldedHands.get(hand)));
			}
			
			HashSet<Hand> dedupedMeldedHands = new HashSet<Hand>(meldedHands);
			System.out.println("Deduped melded hands = " + dedupedMeldedHands.size());
			
			for (int hand = 0; hand < dedupedMeldedHands.toArray().length; ++hand) {
				System.out.println("Hand: " + dedupedMeldedHands.toArray()[hand]
						           + " (" + ((Hand)dedupedMeldedHands.toArray()[hand]).calculateScore(Wind.EAST) + ")");
			}
			
			System.out.println("Unmelded Hand: " + sampleHand
			           + " (" + sampleHand.calculateScore(Wind.EAST) + ")");
		}
		System.out.println("Done!");
	}
}
