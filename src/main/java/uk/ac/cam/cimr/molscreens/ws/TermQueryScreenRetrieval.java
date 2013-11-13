package uk.ac.cam.cimr.molscreens.ws;

import org.apache.log4j.Logger;
import uk.ac.ebi.metabolomes.webservices.EUtilsWebServiceConnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: pcm32
 * Date: 20/09/13
 * Time: 11:54
 * To change this template use File | Settings | File Templates.
 */
public class TermQueryScreenRetrieval extends AbstractScreenRetrieval implements ScreenRetrieval {



    private static final Logger LOGGER = Logger.getLogger(TermQueryScreenRetrieval.class);

    /**
     * Inits the retriever with an explicit query to be sent to PubChem.
     *
     * @param query
     */
    public TermQueryScreenRetrieval(String query) {
        this.setQuery(query);
    }


    /**
     * Inits the retriever with a file that contains a query for pubchem (multiple lines).
     *
     * @param queryFile
     */
    public TermQueryScreenRetrieval(File queryFile) {
        StringBuilder builder = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(queryFile));
            String line;
            builder = new StringBuilder();
            while ((line = reader.readLine())!=null) {
                builder.append(line);
            }
            this.setQuery(builder.toString());
        } catch (IOException e) {
            LOGGER.error("Could not read query file");
            throw  new RuntimeException(e);
        }
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
