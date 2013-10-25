package uk.ac.cam.cimr.autophagy.io;

import org.apache.log4j.Logger;
import uk.ac.cam.cimr.autophagy.criteria.MoleculeInAssayCriterion;
import uk.ac.cam.cimr.autophagy.ws.BioAssayBag;
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.metabolomes.webservices.EUtilsWebServiceConnection;
import uk.ac.ebi.metabolomes.webservices.eutils.PubChemNamesResult;

import java.io.*;

/**
 * Writer for a BioAssayBag. A writer should be more about the format, type of information (list of small molecules,
 * list of bioassays, etc), or destiny (database, etc). Probably not about a particular criteria fullfilled by small
 * molecules in a bag.
 *
 * Created with IntelliJ IDEA.
 * User: pcm32
 * Date: 20/09/13
 * Time: 13:40
 * To change this template use File | Settings | File Templates.
 */
public class HighlyActiveCompoundsBioAssayBagWriter implements BioAssayBagWriter {

    private static final Logger LOGGER = Logger.getLogger(HighlyActiveCompoundsBioAssayBagWriter.class);

    String pathToFile;

    public HighlyActiveCompoundsBioAssayBagWriter(String path) throws IOException {
        pathToFile = path + File.separator + "compounds.txt";
    }

    @Override
    public void write(BioAssayBag bag) {
        EUtilsWebServiceConnection con = new EUtilsWebServiceConnection();
        PubChemNamesResult names = con.getNamesForPubChemCompoundIdentifiers(bag.getOrderedCIDs());
        try {
            BufferedWriter compoundListWriter = new BufferedWriter(new FileWriter(pathToFile));
            compoundListWriter.write("CID\tSID\tName");
            for (MoleculeInAssayCriterion criterion : bag.getCriteria()) {
                compoundListWriter.write("\t"+criterion.getName());
            }
            compoundListWriter.write("\n");
            for(PubChemCompoundIdentifier cid : bag.getOrderedCIDs()) {
                compoundListWriter.write(cid.getAccession()+"\t"
                        +bag.getSIDForCID(cid)+"\t"
                        +names.getPreferredName(cid.getAccession()));
                for (MoleculeInAssayCriterion criterion : bag.getCriteria()) {
                    String outcome =
                            bag.getCriteriaOutput(criterion,cid) != null ? bag.getCriteriaOutput(criterion,cid) : "N/A";
                    compoundListWriter.write("\t"+outcome);
                }
                compoundListWriter.write("\n");
            }
            compoundListWriter.close();
        } catch (IOException e) {
            LOGGER.error("Could not write compounds file",e);
        }
    }

    @Override
    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
