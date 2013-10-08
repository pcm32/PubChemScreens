package uk.ac.cam.cimr.autophagy.criteria;

import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTable;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 7/10/13
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */
public class CompoundAssayPair {
    private Identifier chemicalIdentifer;
    private PChemBioAssayTable assay;

    public CompoundAssayPair(Identifier chemicalIdentifier,PChemBioAssayTable assay) {
        this.chemicalIdentifer = chemicalIdentifier;
        this.assay = assay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompoundAssayPair that = (CompoundAssayPair) o;

        if (!assay.equals(that.assay)) return false;
        if (!chemicalIdentifer.equals(that.chemicalIdentifer)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = chemicalIdentifer.hashCode();
        result = 31 * result + assay.hashCode();
        return result;
    }
}
