package uk.ac.cam.cimr.autophagy.io;

import org.apache.log4j.Logger;
import uk.ac.cam.cimr.autophagy.criteria.Criterion;
import uk.ac.cam.cimr.autophagy.criteria.MoleculeInAssayCriterion;
import uk.ac.cam.cimr.autophagy.ws.BioAssayBag;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTable;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTableEntry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Writes a Compound to BioAssay list, where each row represents a compound to bio assay assigmnet.
 *
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 7/10/13
 * Time: 14:19
 * To change this template use File | Settings | File Templates.
 */
public class Compound2BioAssayWriter implements BioAssayBagWriter {

    private static final Logger LOGGER = Logger.getLogger(Compound2BioAssayWriter.class);

    String pathToFile;

    public Compound2BioAssayWriter(String path) {
        pathToFile = path + File.separator + "comp2Assay.txt";
    }

    @Override
    public void write(BioAssayBag bag) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(pathToFile));
            Collection<MoleculeInAssayCriterion> suitableCriteria = filterCriteria(bag.getCriteria());
            writer.write("CID\tCompoundName\tAID\tAssayTitle\tAssayDescription");
            for (MoleculeInAssayCriterion criterion : suitableCriteria) {
                writer.write("\t"+criterion.getName());
            }
            writer.write("\n");

            for (PChemBioAssayTable assay : bag.getAssays()) {
                for (PChemBioAssayTableEntry entry : assay.getEntries()) {
                    writer.write(entry.getCID()+"\t"+bag.getNameForCID(entry.getPChemCompoundIdentifier())+"\t"+assay.getID()+"\t"+assay.getName()+"\t"+assay.getDescription());

                    for (MoleculeInAssayCriterion criterion : suitableCriteria) {
                        Boolean outcome = bag.getCriteriaOutput(criterion, assay, entry.getPChemCompoundIdentifier());
                        String outcomeStr = outcome != null ? outcome.toString() : "N/A";
                        writer.write("\t"+outcomeStr);
                    }

                    writer.write("\n");
                }
            }
        } catch (IOException e) {
            LOGGER.error("Could not write compounds file", e);
        }

    }

    private Collection<MoleculeInAssayCriterion> filterCriteria(Collection<Criterion> criteria) {
        LinkedList<MoleculeInAssayCriterion> res = new LinkedList<MoleculeInAssayCriterion>();
        for (Criterion criterion : criteria) {
            if(criterion instanceof MoleculeInAssayCriterion)
                res.add((MoleculeInAssayCriterion)criterion);
        }
        return res;
    }

    @Override
    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
