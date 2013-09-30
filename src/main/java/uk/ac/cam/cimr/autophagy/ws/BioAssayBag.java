package uk.ac.cam.cimr.autophagy.ws;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import uk.ac.cam.cimr.autophagy.criteria.MoleculeInAssayCriterion;
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTable;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTableEntry;

import java.util.*;

/**
 * This class brings together a number of pubchem BioAssays tables. Currently it provides ordering capabilities, however
 * probably the ordering and comparator class should be separated from this class. This would allow to inject different
 * criteria of order, and different ordered objects to be retrieved (not only CIDs).
 *
 * Created with IntelliJ IDEA.
 * User: pcm32
 * Date: 20/09/13
 * Time: 13:26
 *
 */
public class BioAssayBag {

    private List<PChemBioAssayTable> tables;
    private Multiset<PubChemCompoundIdentifier> activeCIDs = HashMultiset.create();
    private List<PubChemCompoundIdentifier> orderedCIDs;
    private List<MoleculeInAssayCriterion> criteria;
    private MoleculeInAssayCriterion orderingCriterion;


    public BioAssayBag() {
        this.tables = new LinkedList<PChemBioAssayTable>();
        this.criteria = new LinkedList<MoleculeInAssayCriterion>();
    }

    /**
     * Adds criteria to the bioassay bag. Maybe criteria should external to the bioassaybag.
     *
     * @param criteria
     */
    public void addCriteria(MoleculeInAssayCriterion... criteria) {
        this.criteria.addAll(Arrays.asList(criteria));
    }

    /**
     * Adds a table to the bag. The bags keeps a sequential list of the tables, and an ordered list of active compounds,
     * based on the number of times that the compounds has been seen active in tables so far.
     *
     * @param table to be added to the bag.
     */
    public void addBioAssayTable(PChemBioAssayTable table) {
        tables.add(table);
        activeCIDs.addAll(table.getActiveCompounds());
    }

    public List<PChemBioAssayTable> getAssays() {
        return tables;
    }

    /**
     * Gets an ordered list of {@link PubChemCompoundIdentifier} ordered by the defined criteria. Initially, the only criteria
     * is the count as active in the bioassays of the bag.
     *
     * @return
     */
    public List<PubChemCompoundIdentifier> getOrderedCIDs() {
        if(orderedCIDs==null) {
            orderedCIDs = new ArrayList<PubChemCompoundIdentifier>(activeCIDs.elementSet().size());
            orderedCIDs.addAll(activeCIDs.elementSet());
            Collections.sort(orderedCIDs,new CompoundRanker(activeCIDs));
        }
        return orderedCIDs;
    }

    public Integer getCIDActiveCount(PubChemCompoundIdentifier cid) {
        return activeCIDs.count(cid);
    }

    public String getSIDForCID(PubChemCompoundIdentifier cid) {
        for (PChemBioAssayTable table : tables) {
            if(table.getActiveCompounds().contains(cid)) {
                for (PChemBioAssayTableEntry entry : table.getEntries()) {
                    if (entry.getPChemCompoundIdentifier().equals(cid))
                        return entry.getSID();
                }
            }
        }
        return null;
    }

    public List<MoleculeInAssayCriterion> getCriteria() {
        return criteria;
    }

    public String getCriteriaOutput(MoleculeInAssayCriterion criterion, PubChemCompoundIdentifier cid) {
        return criterion.getCriterionResult(cid);
    }

    /**
     * Within this method, any criterion which requires computation with the complete table can undertake it.
     */
    public void compute() {
        for (MoleculeInAssayCriterion criterion : criteria) {
            criterion.compute(this);
        }
    }

    private class CompoundRanker implements Comparator<PubChemCompoundIdentifier> {

        Multiset<PubChemCompoundIdentifier> cids;

        public CompoundRanker(Multiset<PubChemCompoundIdentifier> cids) {
            this.cids = cids;
        }

        @Override
        public int compare(PubChemCompoundIdentifier o1, PubChemCompoundIdentifier o2) {
            return cids.count(o2) - cids.count(o1);
        }
    }
}
