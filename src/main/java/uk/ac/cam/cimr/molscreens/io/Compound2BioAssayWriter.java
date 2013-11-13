package uk.ac.cam.cimr.molscreens.io;

import org.apache.log4j.Logger;
import uk.ac.cam.cimr.molscreens.criteria.Criterion;
import uk.ac.cam.cimr.molscreens.criteria.MoleculeInAssayCriterion;
import uk.ac.cam.cimr.molscreens.ws.BioAssayBag;
import uk.ac.ebi.mdk.domain.identifier.PubChemBioAssayIdentifier;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTable;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTableEntry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

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
    Boolean forExcel = false;

    public Compound2BioAssayWriter(String path) {
        pathToFile = path + File.separator + "comp2Assay.txt";
    }

    public Compound2BioAssayWriter(String path, Boolean forExcel) {
        this(path);
        this.forExcel = forExcel;
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

            ExcelHyperlinkMaker excelHyperlinkMaker = new ExcelHyperlinkMaker();
            LinkMaker cidLinkMaker = new CIDLinkMaker();
            LinkMaker sidLinkMaker = new SIDLinkMaker();
            LinkMaker aidLinkMaker = new AIDLinkMaker();

            for (PChemBioAssayTable assay : bag.getAssays()) {
                for (PChemBioAssayTableEntry entry : assay.getEntries()) {
                    writer.write(getCIDLabel(entry,excelHyperlinkMaker,cidLinkMaker)
                            +"\t"+bag.getNameForCID(entry.getPChemCompoundIdentifier())
                            +"\t"+getAssayLabel(assay,excelHyperlinkMaker,aidLinkMaker)+"\t"+assay.getName()+"\t"+assay.getDescription());

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

    private String getAssayLabel(PChemBioAssayTable assay, ExcelHyperlinkMaker excelHyperlinkMaker, LinkMaker aidLinkMaker) {
        if(forExcel) {
            return excelHyperlinkMaker.getHyperlink(new PubChemBioAssayIdentifier(assay.getID()),aidLinkMaker);
        } else {
            return assay.getID();
        }
    }

    private String getCIDLabel(PChemBioAssayTableEntry entry, ExcelHyperlinkMaker excelHyperlinkMaker, LinkMaker linkMaker) {
        if(forExcel) {
            return excelHyperlinkMaker.getHyperlink(entry.getPChemCompoundIdentifier(),linkMaker);
        } else {
            return entry.getCID();
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
