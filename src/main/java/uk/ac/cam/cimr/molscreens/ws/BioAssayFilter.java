package uk.ac.cam.cimr.molscreens.ws;

import java.util.Set;

/**
 * This interface sets the contract of functionality for objects that can receive a list of AIDs and filter
 * that list according to some criteria, to produce a new filtered list base on the first one.
 *
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 16/10/13
 * Time: 16:08
 * To change this template use File | Settings | File Templates.
 */
public interface BioAssayFilter {


    /**
     * Receives a set of AIDs (PubChem Assay Identifiers) and produces a filtered set of them based on
     * each implementations criteria.
     *
     * @param aiDs
     * @return
     */
    Set<String> filterAssays(Set<String> aiDs);
}
