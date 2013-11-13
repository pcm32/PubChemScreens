package uk.ac.cam.cimr.molscreens.ws;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 8/11/13
 * Time: 10:37
 * To change this template use File | Settings | File Templates.
 */
public interface ScreenRetrieval {
    void setTestingLimit(Integer limitOfAssaysToUse);

    void setBioAssayFilters(BioAssayFilter... filters);

    BioAssayBag getAssays();
}
