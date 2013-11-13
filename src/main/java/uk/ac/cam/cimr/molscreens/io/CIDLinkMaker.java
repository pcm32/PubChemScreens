package uk.ac.cam.cimr.molscreens.io;

import uk.ac.ebi.mdk.domain.identifier.Identifier;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 13/11/13
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
public class CIDLinkMaker extends AbstractLinkMaker implements LinkMaker {

    public CIDLinkMaker() {
        urlPrefix = "http://pubchem.ncbi.nlm.nih.gov/summary/summary.cgi?cid=";
        urlPostfix = "";
    }

}
