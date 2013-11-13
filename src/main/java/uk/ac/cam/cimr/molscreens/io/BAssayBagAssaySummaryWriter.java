package uk.ac.cam.cimr.molscreens.io;

import org.apache.log4j.Logger;
import uk.ac.cam.cimr.molscreens.criteria.BioAssayCriterion;
import uk.ac.cam.cimr.molscreens.criteria.Criterion;
import uk.ac.cam.cimr.molscreens.ws.BioAssayBag;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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
            Collection<BioAssayCriterion> criteria = filter(bag.getCriteria());
            writer.write("ID\tTitle\tDescription\tActiveComp\tTotalComp");
            for (BioAssayCriterion criterion : criteria) {
                writer.write("\t"+criterion.getName());
            }
            writer.write("\n");
            for (PChemBioAssayTable table : assays) {
                StringBuilder builder = new StringBuilder(table.getID());
                builder.append("\t").append(table.getName())
                        .append("\t'").append(table.getDescription())
                        .append("\t").append(table.getActiveCompounds().size())
                        .append("\t").append(table.getEntryCount());
                for (BioAssayCriterion criterion : criteria) {
                    builder.append("\t").append(bag.getCriteriaOutput(criterion,table));
                }
                builder.append("\n");
                writer.write(builder.toString());
            }
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Could not write compounds file", e);
        }
    }

    /**
     * Filters the set of criteria given to those that this writer can handle.
     * TODO We need a better place to have this method.
     *
     * @param criteria
     * @return
     */
    private Collection<BioAssayCriterion> filter(Collection<Criterion> criteria) {
        LinkedList<BioAssayCriterion> res = new LinkedList<BioAssayCriterion>();
        for (Criterion criterion : criteria) {
            if (criterion instanceof BioAssayCriterion)
                res.add((BioAssayCriterion)criterion);
        }
        return res;
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
