package uk.ac.cam.cimr.autophagy.io;

import org.apache.log4j.Logger;
import uk.ac.cam.cimr.autophagy.ws.BioAssayBag;
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.metabolomes.webservices.EUtilsWebServiceConnection;
import uk.ac.ebi.metabolomes.webservices.eutils.PubChemNamesResult;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: pcm32
 * Date: 20/09/13
 * Time: 13:40
 * To change this template use File | Settings | File Templates.
 */
public class HighlyActiveCompoundsBioAssayWriter implements BioAssayWriter {

    private static final Logger LOGGER = Logger.getLogger(HighlyActiveCompoundsBioAssayWriter.class);

    String pathToFile;

    public HighlyActiveCompoundsBioAssayWriter(String path) throws IOException {
        pathToFile = path + File.separator + "compounds.txt";
    }

    @Override
    public void write(BioAssayBag bag) {
        EUtilsWebServiceConnection con = new EUtilsWebServiceConnection();
        PubChemNamesResult names = con.getNamesForPubChemCompoundIdentifiers(bag.getOrderedCIDs());
        try {
            BufferedWriter compoundListWriter = new BufferedWriter(new FileWriter(pathToFile));
            compoundListWriter.write("CID\tSID\tName\tTimesActive\n");
            for(PubChemCompoundIdentifier cid : bag.getOrderedCIDs()) {
                compoundListWriter.write(cid.getAccession()+"\t"
                        +bag.getSIDForCID(cid)+"\t"
                        +names.getPreferredName(cid.getAccession())+"\t"
                        +bag.getCIDActiveCount(cid)+"\n");
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
