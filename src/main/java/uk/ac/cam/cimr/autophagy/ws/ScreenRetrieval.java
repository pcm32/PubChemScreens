package uk.ac.cam.cimr.autophagy.ws;

import java.util.*;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.apache.log4j.Logger;
import uk.ac.ebi.metabolomes.webservices.EUtilsWebServiceConnection;
import uk.ac.ebi.metabolomes.webservices.PubChemPUGRESTWebService;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTable;

/**
 * Created with IntelliJ IDEA.
 * User: pcm32
 * Date: 20/09/13
 * Time: 11:54
 * To change this template use File | Settings | File Templates.
 */
public class ScreenRetrieval {

    private static final Logger LOGGER = Logger.getLogger(ScreenRetrieval.class);

    private Integer testingLimit=0;
    private List<BioAssayFilter> bioAssayFilters = new LinkedList<BioAssayFilter>();

    /**
     * This method is intended only for debugging purposes, to run only on a small number of
     * assays.
     *
     * @param limitOfAssaysToUse
     */
    public void setTestingLimit(Integer limitOfAssaysToUse) {
        testingLimit = limitOfAssaysToUse;
    }

    /**
     * Sets the BioAssayFilters to be used to filter the resulting list of AIDs that will be produced by the query.
     * This operation removes any previous set filters added, and needs to be called before
     * {@link #getAssaysForQuery(String)}.
     *
     * @param filters the filters to add.
     */
    public void setBioAssayFilters(BioAssayFilter... filters) {
        this.bioAssayFilters.clear();
        this.bioAssayFilters.addAll(Arrays.asList(filters));
    }

    /**
     * Obtains a BioAssayBag that contains the BioAssays produced by this term query.
     *
     * @param termQuery use to search the Assay data set.
     * @return a BioAssayBag holding all the Assays resulting from the search.
     */
    public BioAssayBag getAssaysForQuery(String termQuery) {
        EUtilsWebServiceConnection eutils = new EUtilsWebServiceConnection();
        Set<String> AIDs = eutils.getPubChemBioAssaysForTermSearch(termQuery);

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
