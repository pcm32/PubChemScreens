package uk.ac.cam.cimr.molscreens.ws;

import uk.ac.ebi.metabolomes.webservices.EUtilsWebServiceConnection;
import uk.ac.ebi.metabolomes.webservices.eutils.PubChemBioAssayESummaryResult;
import uk.ac.ebi.metabolomes.webservices.eutils.PubChemNamesResult;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTable;

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


    private EUtilsWebServiceConnection connection;

    private final Integer assayStep = 1000;

    public BioAssayAnnotator() {
        this.connection = new EUtilsWebServiceConnection();
    }

    /**
     * Annotates bio assays elements in BioAssayBag, currently with name, description, and sources.
     *
     * @param bag
     */
    public void annotate(BioAssayBag bag) {
        List<PChemBioAssayTable> assays = bag.getAssays();
        for (int from = 0; from < assays.size(); from+=assayStep) {
            int to = from + assayStep - 1 > assays.size() ? assays.size() - 1 : from + assayStep - 1;

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

        PubChemNamesResult names = connection.getNamesForPubChemCompoundIdentifiers(bag.getOrderedCIDs());
        bag.setCompoundNames(names);

    }

    private Map<String, PubChemBioAssayESummaryResult> getPubChemBioAssayESummaryResultMap(List<String> aids) {
        List<PubChemBioAssayESummaryResult> results = connection.getPubChemBioassaysSummaries(aids);
        Map<String,PubChemBioAssayESummaryResult> resultsMapped = new HashMap<String, PubChemBioAssayESummaryResult>(results.size());
        for (PubChemBioAssayESummaryResult result : results) {
            resultsMapped.put(result.getId(),result);
        }
        return resultsMapped;
    }
}
