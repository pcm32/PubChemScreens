package uk.ac.cam.cimr.autophagy.criteria;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import uk.ac.cam.cimr.autophagy.ws.BioAssayBag;
import uk.ac.ebi.mdk.domain.identifier.AbstractChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTable;

import java.util.HashMap;
import java.util.Map;

/**
 * This criteria counts how many times a compound is active within a BioAssayBag.
 *
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 27/9/13
 * Time: 15:31
 * To change this template use File | Settings | File Templates.
 */
public class PubChemCountActiveMolInAssay implements MoleculeInAssayCriterion {

    private Map<PubChemCompoundIdentifier,Integer> timesActive;

    public PubChemCountActiveMolInAssay() {
        this.timesActive = new HashMap<PubChemCompoundIdentifier, Integer>();
    }

    @Override
    public String getName() {
        return "ActiveAssays";
    }

    @Override
    public String getCriterionResult(AbstractChemicalIdentifier cid) {
        return timesActive.get(cid)+"";
    }

    @Override
    public void compute(BioAssayBag bioAssayBag) {
        Multiset<PubChemCompoundIdentifier> activeCIDs = HashMultiset.create();
        for (PChemBioAssayTable assay : bioAssayBag.getAssays()) {
            activeCIDs.addAll(assay.getActiveCompounds());
        }

        for (PubChemCompoundIdentifier ident : activeCIDs.elementSet()) {
            timesActive.put(ident,activeCIDs.count(ident));
        }
    }
}
