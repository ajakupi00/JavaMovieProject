/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.models;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 *
 * @author arjan
 */
public class ArtistTransferable implements Transferable{
    public static final DataFlavor ARTIST_FLAVOR = new DataFlavor(Artist.class, "Artist");
    private static final DataFlavor[] SUPPORTED_FLAVOR = {ARTIST_FLAVOR};
    private final Artist artist;
    
    public ArtistTransferable(Artist artist){
    this.artist = artist;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return SUPPORTED_FLAVOR;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return ARTIST_FLAVOR.equals(flavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if(isDataFlavorSupported(flavor))
            return artist;
        throw new UnsupportedFlavorException(flavor);
    }
}
