import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;


public class MeldTest {

	@Test
	public void test() {
		try {
			// Create test tiles
			SuitTile c4 = new SuitTile(Suit.CHARACTER, 4);
			SuitTile c5 = new SuitTile(Suit.CHARACTER, 5);
			SuitTile c6 = new SuitTile(Suit.CHARACTER, 6);
			DragonTile greenDragon = new DragonTile(Dragon.GREEN);
			
			// Create test melds
			Meld suitPung = new Meld(c4, c4, c4);
			Meld suitKong = new Meld(c4, c4, c4, c4);
			Meld suitSeq1 = new Meld (c4, c5, c6);
			Meld suitSeq2 = new Meld (c6, c5, c4);
			Meld suitSeq3 = new Meld (c6, c4, c5);
			Meld dragonPung = new Meld(greenDragon, greenDragon, greenDragon);
			Meld badMeld1 = new Meld(c4, c4, greenDragon);
			Meld badMeld2 = new Meld(c4, c4, c5);
			
			// Assertions
			Assert.assertEquals(suitPung.getKeyTile(), c4);
			Assert.assertEquals(suitPung.isValid(), true);
			Assert.assertEquals(suitPung.getType(), Meld.Type.TRIPLET);
			
			Assert.assertEquals(suitKong.getKeyTile(), c4);
			Assert.assertEquals(suitKong.isValid(), true);
			Assert.assertEquals(suitKong.getType(), Meld.Type.KONG);
			
			Assert.assertEquals(suitSeq1.getKeyTile(), c5);
			Assert.assertEquals(suitSeq1.isValid(), true);
			Assert.assertEquals(suitSeq1.getType(), Meld.Type.SEQUENCE);
			
			Assert.assertEquals(suitSeq2.getKeyTile(), c5);
			Assert.assertEquals(suitSeq2.isValid(), true);
			Assert.assertEquals(suitSeq2.getType(), Meld.Type.SEQUENCE);
			
			Assert.assertEquals(suitSeq3.getKeyTile(), c5);
			Assert.assertEquals(suitSeq3.isValid(), true);
			Assert.assertEquals(suitSeq3.getType(), Meld.Type.SEQUENCE);
			
			Assert.assertEquals(dragonPung.getKeyTile(), greenDragon);
			Assert.assertEquals(dragonPung.isValid(), true);
			Assert.assertEquals(dragonPung.getType(), Meld.Type.TRIPLET);
			
			Assert.assertEquals(badMeld1.isValid(), false);
			Assert.assertEquals(badMeld2.isValid(), false);
		} catch (InvalidSuitTileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
