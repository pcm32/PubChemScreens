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

    public BioAssayBag getAssaysForQuery(String termQuery) {
        EUtilsWebServiceConnection eutils = new EUtilsWebServiceConnection();
        Set<String> AIDs = eutils.getPubChemBioAssaysForTermSearch(termQuery);

        PubChemPUGRESTWebService pug = new PubChemPUGRESTWebService();

        BioAssayBag bag = new BioAssayBag();
        int visited = 0;
        int active = 0;
        for (String aid : AIDs) {

            try {
                PChemBioAssayTable table = pug.getBioAssayTable(aid);
                bag.addBioAssayTable(table);

                if(table.getActiveCompounds().size()>0) {
                    System.out.println("AID has "+table.getActiveCompounds().size()+" active compounds");
                    active += table.getActiveCompounds().size();
                }
            } catch (RuntimeException e) {
                LOGGER.error("Problems for AID "+aid,e);
            }

            if(++visited % 500 == 0) {
                System.out.println("Done "+visited+" AIDs\n");
            }
        }

        return bag;
    }



}
