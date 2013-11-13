package uk.ac.cam.cimr.molscreens.ws;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 13/11/13
 * Time: 15:01
 * To change this template use File | Settings | File Templates.
 */
public class BlackListBAFilterTest {
    @Test
    public void testFilterAssays() throws Exception {
        BlackListBAFilter filter = new BlackListBAFilter(BlackListBAFilterTest.class.getResourceAsStream("blackList.txt"));
        Set<String> onGoingAIDs = new HashSet<String>(Arrays.asList("5555","6767676","454324","720536",
                "602124",
                "434954",
                "624305"));
        Set<String> resultingIDs = filter.filterAssays(onGoingAIDs);
        assertEquals(3,resultingIDs.size());
    }
}
