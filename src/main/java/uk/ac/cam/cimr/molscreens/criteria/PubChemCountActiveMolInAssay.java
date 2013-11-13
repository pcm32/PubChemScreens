package uk.ac.cam.cimr.molscreens.criteria;

import uk.ac.cam.cimr.molscreens.ws.BioAssayBag;
import uk.ac.ebi.mdk.domain.identifier.AbstractChemicalIdentifier;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTable;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTableEntry;

/**
 * This criteria counts how many times a compound is active within a BioAssayBag.
 *
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 27/9/13
 * Time: 15:31
 * To change this template use File | Settings | File Templates.
 */
public class PubChemCountActiveMolInAssay extends AbstractCountableCriterion implements MoleculeInAssayCriterion {

    public PubChemCountActiveMolInAssay() {
        super();
    }

    @Override
    public String getName() {
        return "ActiveAssays";
    }

    @Override
    public String getCriterionResult(AbstractChemicalIdentifier cid) {
        return countPositives.get(cid)+"";
    }

//    @Override
//    public void compute(BioAssayBag bioAssayBag) {
//        Multiset<PubChemCompoundIdentifier> activeCIDs = HashMultiset.create();
//        for (PChemBioAssayTable assay : bioAssayBag.getAssays()) {
//            activeCIDs.addAll(assay.getActiveCompounds());
//        }
//
//        for (PubChemCompoundIdentifier ident : activeCIDs.elementSet()) {
//            countPositives.put(ident,activeCIDs.count(ident));
//        }
//    }

    @Override
    void registerPositiveCase(PChemBioAssayTableEntry entry) {
        Integer times = countPositives.containsKey(entry.getPChemCompoundIdentifier()) ? countPositives.get(entry.getPChemCompoundIdentifier()) + 1 : 1;
        countPositives.put(entry.getPChemCompoundIdentifier(), times);
    }

    @Override
    boolean criteriaAvailable(BioAssayBag bag, PChemBioAssayTable assay) {
        return true;
    }

    /**
     * In this case the criteria is approved if the molecule is active in the assay.
     *
     * @param bag
     * @param assay
     * @param entry
     * @return
     */
    @Override
    boolean criteriaApproved(BioAssayBag bag, PChemBioAssayTable assay, PChemBioAssayTableEntry entry) {
        return assay.getActiveCompounds().contains(entry.getPChemCompoundIdentifier());
    }
}
