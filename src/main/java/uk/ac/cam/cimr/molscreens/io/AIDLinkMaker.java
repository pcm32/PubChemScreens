package uk.ac.cam.cimr.molscreens.io;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 13/11/13
 * Time: 16:31
 * To change this template use File | Settings | File Templates.
 */
public class AIDLinkMaker extends AbstractLinkMaker implements LinkMaker {

    public AIDLinkMaker() {
        this.urlPrefix = "http://pubchem.ncbi.nlm.nih.gov/summary/summary.cgi?aid=";
        this.urlPostfix = "";
    }

}
