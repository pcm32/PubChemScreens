package uk.ac.cam.cimr.molscreens.io;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 13/11/13
 * Time: 16:32
 * To change this template use File | Settings | File Templates.
 */
public class SIDLinkMaker extends AbstractLinkMaker implements LinkMaker {

    public SIDLinkMaker() {
        this.urlPrefix = "http://pubchem.ncbi.nlm.nih.gov/summary/summary.cgi?sid=";
        this.urlPostfix = "";
    }
}
