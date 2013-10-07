package uk.ac.cam.cimr.autophagy.ws;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import uk.ac.ebi.metabolomes.webservices.EUtilsWebServiceConnection;
import uk.ac.ebi.metabolomes.webservices.eutils.PubChemBioAssayESummaryResult;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTable;

import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 7/10/13
 * Time: 13:06
 * To change this template use File | Settings | File Templates.
 */
public class BioAssayAnnotator {


    /**
     * Annotates bio assays elements in BioAssayBag, currently with name, description, and sources.
     * @param bag
     */
    public void annotate(BioAssayBag bag) {
        List<PChemBioAssayTable> assays = bag.getAssays();
            for (int from = 0; from < assays.size(); from+=5000) {
                int to = from + 200 - 1 > assays.size() ? assays.size() - 1 : from + 200 - 1;

                List<String> aids = new ArrayList<String>(to - from + 1);

                for (int i = from ; i <= to ; i++)
                    aids.add(assays.get(i).getID());

                Map<String, PubChemBioAssayESummaryResult> resultsMapped = getPubChemBioAssayESummaryResultMap(aids);

                for (int i = from; i <= to; i++) {
                    PChemBioAssayTable assay = assays.get(i);
                    PubChemBioAssayESummaryResult result = resultsMapped.get(assay.getID());
                    assay.setDescription(result.getDescription());
                    assay.setName(result.getName());
                    assay.setSources(result.getSources());
                }

            }

    }

    private Map<String, PubChemBioAssayESummaryResult> getPubChemBioAssayESummaryResultMap(List<String> aids) {
        EUtilsWebServiceConnection connection = new EUtilsWebServiceConnection();
        List<PubChemBioAssayESummaryResult> results = connection.getPubChemBioassaysSummaries(aids);

        Map<String,PubChemBioAssayESummaryResult> resultsMapped = new HashMap<String, PubChemBioAssayESummaryResult>(results.size());
        for (PubChemBioAssayESummaryResult result : results) {
            resultsMapped.put(result.getId(),result);
        }
        return resultsMapped;
    }
}
