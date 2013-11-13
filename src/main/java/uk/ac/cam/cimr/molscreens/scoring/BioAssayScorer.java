package uk.ac.cam.cimr.molscreens.scoring;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 25/10/13
 * Time: 13:28
 * To change this template use File | Settings | File Templates.
 */
public interface BioAssayScorer {

    /**
     * Scores a text according to implementation details.
     *
     * @param txt the text to score
     * @return the score as an Integer.
     */
    public Integer getScore(String txt);
}
