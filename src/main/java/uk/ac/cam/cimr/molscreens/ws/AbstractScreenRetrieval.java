package uk.ac.cam.cimr.molscreens.ws;

import org.apache.log4j.Logger;
import uk.ac.ebi.metabolomes.webservices.PubChemPUGRESTWebService;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 8/11/13
 * Time: 12:31
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractScreenRetrieval implements ScreenRetrieval {

    private static final Logger LOGGER = Logger.getLogger(AbstractScreenRetrieval.class);

    protected Set<String> AIDs;
    protected Integer testingLimit=0;
    protected List<BioAssayFilter> bioAssayFilters = new LinkedList<BioAssayFilter>();

    /**
     * This method is intended only for debugging purposes, to run only on a small number of
     * assays.
     *
     * @param limitOfAssaysToUse
     */
    @Override
    public void setTestingLimit(Integer limitOfAssaysToUse) {
        testingLimit = limitOfAssaysToUse;
    }

    /**
     * Sets the BioAssayFilters to be used to filter the resulting list of AIDs that will be produced by the query.
     * This operation removes any previous set filters added.
     *
     * @param filters the filters to add.
     */
    @Override
    public void setBioAssayFilters(BioAssayFilter... filters) {
        this.bioAssayFilters.clear();
        this.bioAssayFilters.addAll(Arrays.asList(filters));
    }

    /**
     * Obtains a BioAssayBag that contains the BioAssays produced by this implementation.
     *
     * @return a BioAssayBag holding all the Assays resulting from the search.
     */
    @Override
    public BioAssayBag getAssays() {
        for (BioAssayFilter filter : bioAssayFilters) {
            AIDs = filter.filterAssays(AIDs);
        }
        PubChemPUGRESTWebService pug = new PubChemPUGRESTWebService();

        BioAssayBag bag = new BioAssayBag();
        int visited = 0;
        for (String aid : AIDs) {

            try {
                PChemBioAssayTable table = pug.getBioAssayTable(aid);
                bag.addBioAssayTable(table);

                if(table.getActiveCompounds().size()>0) {
                    System.out.println("AID has "+table.getActiveCompounds().size()+" active compounds");
                }
            } catch (RuntimeException e) {
                LOGGER.error("Problems for AID "+aid,e);
            }

            if(++visited % 500 == 0) {
                System.out.println("Done "+visited+" AIDs\n");
            }
            if(testingLimit > 0 && visited > testingLimit) {
                break;
            }
        }

        return bag;
    }
}
