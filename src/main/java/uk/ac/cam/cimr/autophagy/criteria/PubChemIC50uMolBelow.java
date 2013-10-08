package uk.ac.cam.cimr.autophagy.criteria;

import org.apache.log4j.Logger;
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
public class PubChemIC50uMolBelow extends AbstractCountableCriterion implements MoleculeInAssayCriterion {

    private static final Logger LOGGER = Logger.getLogger(PubChemIC50uMolBelow.class);

    private Float microMolarThreshold;
    private Integer currentIndex;


    public PubChemIC50uMolBelow(Float microMolarThreshold) {
        super();
        this.microMolarThreshold = microMolarThreshold;
    }

    @Override
    public String getName() {
        return "IC50<"+microMolarThreshold;
    }

    @Override
    public String getCriterionResult(AbstractChemicalIdentifier cid) {
        if(countPositives.containsKey(cid))
            return countPositives.get(cid)+"";
        else
            return null;
    }

    @Override
    void registerPositiveCase(PChemBioAssayTableEntry entry) {
        Integer currentCount =
                countPositives.containsKey(entry.getPChemCompoundIdentifier())
        ? countPositives.get(entry.getPChemCompoundIdentifier())+1 : 1;
        countPositives.put(entry.getPChemCompoundIdentifier(), currentCount);
    }

    @Override
    boolean criteriaAvailable(BioAssayBag bag, PChemBioAssayTable assay) {
        currentIndex = assay.getAdditionalFieldIndex("IC50");
        return currentIndex > -1;
    }

    @Override
    boolean criteriaApproved(BioAssayBag bag, PChemBioAssayTable assay, PChemBioAssayTableEntry entry) {
        Float ic50 = getIC50Value(entry, currentIndex);
        return ic50 != null && !ic50.isNaN() && !ic50.isInfinite() && ic50 <= microMolarThreshold;
    }

    private Float getIC50Value(PChemBioAssayTableEntry entry, Integer index) {
        try {
            Float ic50 = Float.parseFloat(entry.getAdditionalField(index));
            return ic50;
        } catch (NumberFormatException e) {
            LOGGER.error("Problems transforming "+entry.getAdditionalField(index)+" into a float for IC50 for "+entry.getCID(),e);
        }
        return Float.NaN;
    }
}
