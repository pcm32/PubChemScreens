package uk.ac.cam.cimr.autophagy.io;

import org.apache.log4j.Logger;
import uk.ac.cam.cimr.autophagy.ws.BioAssayBag;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 7/10/13
 * Time: 10:01
 * To change this template use File | Settings | File Templates.
 */
public class BAssayBagAssaySummaryWriter implements BioAssayBagWriter {

    private static final Logger LOGGER = Logger.getLogger(HighlyActiveCompoundsBioAssayBagWriter.class);

    private final String pathToFile;

    public BAssayBagAssaySummaryWriter(String path) {
        pathToFile = path + File.separator + "assays.txt";
    }

    @Override
    public void write(BioAssayBag bag) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(pathToFile));
            List<PChemBioAssayTable> assays = bag.getAssays();
            Collections.sort(assays,new AssayTableSorterByActiveCount());
            writer.write("ID\tTitle\tDescription\tActiveComp\tTotalComp\n");
            for (PChemBioAssayTable table : assays) {
                StringBuilder builder = new StringBuilder(table.getID());
                builder.append("\t").append(table.getName())
                        .append("\t'").append(table.getDescription())
                        .append("\t").append(table.getActiveCompounds().size())
                        .append("\t").append(table.getEntryCount()).append("\n");
                writer.write(builder.toString());
            }
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Could not write compounds file", e);
        }
    }

    @Override
    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private class AssayTableSorterByActiveCount implements Comparator<PChemBioAssayTable> {
        @Override
        public int compare(PChemBioAssayTable o1, PChemBioAssayTable o2) {
            if(o2 == null && o1 != null) {
                return -1;
            } else if(o1 == null && o2 != null) {
                return -1;
            } else if(o1 == null && o2 == null) {
                return 0;
            }

            Integer size1 = o1.getActiveCompounds().size();
            Integer size2 = o2.getActiveCompounds().size();

            return -1*size1.compareTo(size2);
        }
    }
}
