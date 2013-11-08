package uk.ac.cam.cimr.autophagy.ws;

import java.util.*;

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
public class TermQueryScreenRetrieval extends AbstractScreenRetrieval implements ScreenRetrieval {



    private static final Logger LOGGER = Logger.getLogger(TermQueryScreenRetrieval.class);

    public TermQueryScreenRetrieval(String query) {
        this.setQuery(query);
    }

    /**
     * Sets the query to be used for retrieving AIDs
     *
     * @param termQuery
     */
    private void setQuery(String termQuery) {
        EUtilsWebServiceConnection eutils = new EUtilsWebServiceConnection();
        AIDs = eutils.getPubChemBioAssaysForTermSearch(termQuery);
    }

}
