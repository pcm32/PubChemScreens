package uk.ac.cam.cimr.molscreens.ws;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 13/11/13
 * Time: 15:10
 * To change this template use File | Settings | File Templates.
 */
public class CombinedRetrievalTest {
    @Test
    public void testGetAssays() throws Exception {
        String path = CombinedRetrievalTest.class.getResource("exampleAIDs.txt").getPath();
        String path2 = CombinedRetrievalTest.class.getResource("exampleAIDs2.txt").getPath();

        ScreenRetrieval retP1 = new ListScreenRetrieval(path);
        ScreenRetrieval retP2 = new ListScreenRetrieval(path2);

        assertEquals(5,retP1.getAssays().getAssays().size());
        assertEquals(7,retP2.getAssays().getAssays().size());

        CombinedRetrieval ret = new CombinedRetrieval(retP1,retP2);

        assertEquals(10,ret.getAssays().getAssays().size());
    }
}
