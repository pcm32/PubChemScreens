package uk.ac.cam.cimr.autophagy.criteria;

import uk.ac.cam.cimr.autophagy.ws.BioAssayBag;
import uk.ac.ebi.mdk.domain.identifier.AbstractChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTable;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTableEntry;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 27/9/13
 * Time: 16:37
 * To change this template use File | Settings | File Templates.
 */
public class PubChemIC50uMolBelow implements MoleculeInAssayCriterion {


    private Float microMolarThreshold;
    private Map<PubChemCompoundIdentifier,Integer> countBelowThres;


    public PubChemIC50uMolBelow(Float microMolarThreshold) {
        this.microMolarThreshold = microMolarThreshold;
        this.countBelowThres = new HashMap<PubChemCompoundIdentifier, Integer>();
    }

    @Override
    public String getName() {
        return "IC50<"+microMolarThreshold;
    }

    @Override
    public String getCriterionResult(AbstractChemicalIdentifier cid) {
        return countBelowThres.get(cid)+"";
    }

    @Override
    public void compute(BioAssayBag bioAssayBag) {
        for (PChemBioAssayTable assay : bioAssayBag.getAssays()) {
            Integer index = assay.getAdditionalFieldIndex("IC50");
            if(index > -1) {
                for (PChemBioAssayTableEntry entry : assay.getEntries()) {
                    Float ic50 = getIC50Value(entry, index);
                    if(ic50 != null && !ic50.isNaN() && !ic50.isInfinite() && ic50 <= microMolarThreshold ) {
                        Integer currentCount =
                                countBelowThres.containsKey(entry.getPChemCompoundIdentifier())
                        ? countBelowThres.get(entry.getPChemCompoundIdentifier())+1 : 1;
                        countBelowThres.put(entry.getPChemCompoundIdentifier(),currentCount);
                    }
                }
            }
        }
    }

    private Float getIC50Value(PChemBioAssayTableEntry entry, Integer index) {
        Float ic50 = Float.parseFloat(entry.getAdditionalField(index));
        return ic50;
    }
}
