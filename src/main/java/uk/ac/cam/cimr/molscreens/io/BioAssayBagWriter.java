package uk.ac.cam.cimr.molscreens.io;

import uk.ac.cam.cimr.molscreens.ws.BioAssayBag;

/**
 * Created with IntelliJ IDEA.
 * User: pcm32
 * Date: 20/09/13
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
public interface BioAssayBagWriter {

    public void write(BioAssayBag bag);
    public void close();

}
