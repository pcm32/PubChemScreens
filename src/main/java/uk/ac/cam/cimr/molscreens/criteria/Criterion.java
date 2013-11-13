package uk.ac.cam.cimr.molscreens.criteria;

import uk.ac.cam.cimr.molscreens.ws.BioAssayBag;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 25/10/13
 * Time: 12:03
 * To change this template use File | Settings | File Templates.
 */
public interface Criterion {
    /**
     * The name of the criterion.
     *
     * @return
     */
    String getName();

    /**
     * Runs the computation for all small molecules in that bag of assays. The compute method implementation shouldn't
     * change anything of the bag.
     *
     * @param bioAssayBag
     */
    void compute(BioAssayBag bioAssayBag);
}
