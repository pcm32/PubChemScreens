package uk.ac.cam.cimr.autophagy.io;

import uk.ac.cam.cimr.autophagy.ws.BioAssayBag;

/**
 * Created with IntelliJ IDEA.
 * User: pcm32
 * Date: 20/09/13
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
public interface BioAssayWriter {

    public void write(BioAssayBag bag);
    public void close();

}
