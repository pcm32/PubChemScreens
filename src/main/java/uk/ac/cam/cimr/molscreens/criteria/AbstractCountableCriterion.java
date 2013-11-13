package uk.ac.cam.cimr.molscreens.criteria;

import uk.ac.cam.cimr.molscreens.ws.BioAssayBag;
import uk.ac.ebi.mdk.domain.identifier.AbstractChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTable;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTableEntry;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 7/10/13
 * Time: 16:37
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCountableCriterion {
    Map<PubChemCompoundIdentifier,Integer> countPositives;
    Map<CompoundAssayPair,Boolean> criteriaMeetByCompInAssay;

    public AbstractCountableCriterion() {
        this.criteriaMeetByCompInAssay = new HashMap<CompoundAssayPair, Boolean>();
        this.countPositives = new HashMap<PubChemCompoundIdentifier, Integer>();
    }

    public void compute(BioAssayBag bioAssayBag) {
        for (PChemBioAssayTable assay : bioAssayBag.getAssays()) {
            if(criteriaAvailable(bioAssayBag, assay)) {
                for (PChemBioAssayTableEntry entry : assay.getEntries()) {
                    if(criteriaApproved(bioAssayBag,assay,entry)) {
                        registerPositiveCase(entry);
                        criteriaMeetByCompInAssay.put(new CompoundAssayPair(entry.getPChemCompoundIdentifier(),assay),true);
                    } else {
                        registerNegativeCase(entry);
                        criteriaMeetByCompInAssay.put(new CompoundAssayPair(entry.getPChemCompoundIdentifier(),assay),false);
                    }
                }
            }
        }
    }

    /**
     * Registers the case where the criteria can be decided in the assay, but where the outcome for this entry is
     * negative.
     *
     * @param entry
     */
    protected void registerNegativeCase(PChemBioAssayTableEntry entry) {
        if(!countPositives.containsKey(entry.getPChemCompoundIdentifier())) {
            countPositives.put(entry.getPChemCompoundIdentifier(),0);
        }
    }

    public Boolean getCriterionResult(PChemBioAssayTable assay, AbstractChemicalIdentifier chemicalIdentifier) {
        CompoundAssayPair pair = new CompoundAssayPair(chemicalIdentifier,assay);
        if(criteriaMeetByCompInAssay.containsKey(pair)) {
            return criteriaMeetByCompInAssay.get(pair);
        }
        return null;
    }

    abstract void registerPositiveCase(PChemBioAssayTableEntry entry);

    abstract boolean criteriaAvailable(BioAssayBag bag, PChemBioAssayTable assay);

    abstract boolean criteriaApproved(BioAssayBag bag, PChemBioAssayTable assay, PChemBioAssayTableEntry entry);
}
