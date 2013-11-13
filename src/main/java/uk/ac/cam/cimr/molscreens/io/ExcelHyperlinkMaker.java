package uk.ac.cam.cimr.molscreens.io;

import uk.ac.ebi.mdk.domain.identifier.Identifier;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 13/11/13
 * Time: 16:35
 * To change this template use File | Settings | File Templates.
 */
public class ExcelHyperlinkMaker {

    public String getHyperlink(Identifier identifier,LinkMaker maker) {
        return "=HYPERLINK(\""+maker.getLink(identifier)+"\","+identifier.getAccession()+")";
    }

}
