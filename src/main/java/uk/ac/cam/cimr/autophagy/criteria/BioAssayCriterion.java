package uk.ac.cam.cimr.autophagy.criteria;

import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTable;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 25/10/13
 * Time: 12:04
 * To change this template use File | Settings | File Templates.
 */
public interface BioAssayCriterion extends Criterion {

    /**
     * The result of the criterion for a particular identifier. This identifier probably needs to part of the bag where
     * the criteria was computed.
     *
     * @param assay
     * @return criterion result for the provided assay.
     */
    public String getCriterionResult(PChemBioAssayTable assay);

}
