package uk.ac.cam.cimr.molscreens.io;

import uk.ac.ebi.mdk.domain.identifier.Identifier;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 13/11/13
 * Time: 16:30
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractLinkMaker implements LinkMaker {
    String urlPrefix;
    String urlPostfix;

    @Override
    public String getLink(Identifier identifier) {
        return urlPrefix+identifier.getAccession()+urlPostfix;
    }
}
