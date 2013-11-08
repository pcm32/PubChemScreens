package uk.ac.cam.cimr.autophagy.ws;

import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTable;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 16/10/13
 * Time: 13:07
 * To change this template use File | Settings | File Templates.
 */
public class TermQueryScreenRetrievalTest {


    /**
     * This test aims to reconstruct and correct the case where certain bioassays
     * don't show a correct name and description.
     *
     * @throws Exception
     */
    @Test
    public void testGetAssaysForQuery1811() throws Exception {
        TermQueryScreenRetrieval retrieval = new TermQueryScreenRetrieval("1811");
        BioAssayBag bag = retrieval.getAssays();
        BioAssayAnnotator annotator = new BioAssayAnnotator();
        annotator.annotate(bag);

        for (PChemBioAssayTable assay : bag.getAssays()) {
            String name = assay.getName();
            assertNotNull(name);
            System.out.println("Name : "+name);
            String desc = assay.getDescription();
            assertNotNull(desc);
            System.out.println("Desc : "+desc);
        }
    }
}
