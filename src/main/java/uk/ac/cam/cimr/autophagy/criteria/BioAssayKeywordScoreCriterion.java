package uk.ac.cam.cimr.autophagy.criteria;

import uk.ac.cam.cimr.autophagy.scoring.BioAssayScorer;
import uk.ac.cam.cimr.autophagy.ws.BioAssayBag;
import uk.ac.ebi.metabolomes.webservices.pubchem.PChemBioAssayTable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 25/10/13
 * Time: 13:20
 * To change this template use File | Settings | File Templates.
 */
public class BioAssayKeywordScoreCriterion implements BioAssayCriterion {

    private Map<PChemBioAssayTable,Integer> assay2score;
    private BioAssayScorer scorer;

    public BioAssayKeywordScoreCriterion(BioAssayScorer scorer) {
        this.scorer = scorer;
        this.assay2score = new HashMap<PChemBioAssayTable,Integer>();
    }

    @Override
    public String getCriterionResult(PChemBioAssayTable assay) {
        return assay2score.get(assay)+"";
    }

    @Override
    public String getName() {
        return "KeywordScore";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void compute(BioAssayBag bioAssayBag) {
        for (PChemBioAssayTable table : bioAssayBag.getAssays()) {
            Integer score = scorer.getScore(table.getName());
            score += scorer.getScore(table.getDescription());

            assay2score.put(table,score);
        }
    }
}
