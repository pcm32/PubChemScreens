package uk.ac.cam.cimr.autophagy.ws;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This object combines a query and list based approach to produce a bag of bio assays.
 *
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 13/11/13
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
public class CombinedRetrieval implements ScreenRetrieval {

    List<ScreenRetrieval> retrievals;
    /**
     * Initializes the retriever with a query and path to a list of AIDs. The produced bag will be the union of both
     * approaches.
     *
     *
     * @param retrievals
     */
    public CombinedRetrieval(ScreenRetrieval... retrievals) {
        this.retrievals = new LinkedList<ScreenRetrieval>(Arrays.asList(retrievals));
    }

    @Override
    public void setTestingLimit(Integer limitOfAssaysToUse) {
        for (ScreenRetrieval retrieval : retrievals) {
            retrieval.setTestingLimit(limitOfAssaysToUse);
        }
    }

    @Override
    public void setBioAssayFilters(BioAssayFilter... filters) {
        for (ScreenRetrieval retrieval : retrievals) {
            retrieval.setBioAssayFilters(filters);
        }
    }

    @Override
    public BioAssayBag getAssays() {
        BioAssayBag bagList = new BioAssayBag();
        if(retrievals.size()>0) {
            bagList = retrievals.get(0).getAssays();
            for (int i=1; i<retrievals.size();i++) {
                bagList.addBioAssays(retrievals.get(i).getAssays());
            }
        }

        return bagList;
    }

}
