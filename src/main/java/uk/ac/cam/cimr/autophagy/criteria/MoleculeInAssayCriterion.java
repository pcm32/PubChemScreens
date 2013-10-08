package uk.ac.cam.cimr.autophagy.criteria;

import uk.ac.cam.cimr.autophagy.ws.BioAssayBag;
import uk.ac.ebi.mdk.domain.identifier.AbstractChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTable;

/**
 * Ths interface describes the idea of a criteria relevant to a Molecule participating in an assay. A criteria should be
 * able to describe itself (so that it can be written to a file), be able to say whether it is sortable or not, and be able
 * to sort molecules in a bag (as it is a MoleculeInAssay criteria).
 *
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 27/9/13
 * Time: 14:14
 * To change this template use File | Settings | File Templates.
 */
public interface MoleculeInAssayCriterion {

    /**
     * The name of the criterion.
     *
     * @return
     */
    public String getName();

    /**
     * The result of the criterion for a particular identifier. This identifier probably needs to part of the bag where
     * the criteria was computed.
     *
     * @param cid
     * @return
     */
    public String getCriterionResult(AbstractChemicalIdentifier cid);

    /**
     * Runs the computation for all small molecules in that bag of assays. The compute method implementation shouldn't
     * change anything of the bag.
     *
     * @param bioAssayBag
     */
    public void compute(BioAssayBag bioAssayBag);


    /**
     * Indicates whether a particular small molecule (represented by a chemical identifier) approves this criterion within
     * a particular assay.
     *
     * @param assay
     * @param chemicalIdentifier
     * @return true or false if the criterions is approved; null if not applicable.
     */
    public Boolean getCriterionResult(PChemBioAssayTable assay, AbstractChemicalIdentifier chemicalIdentifier);
}
