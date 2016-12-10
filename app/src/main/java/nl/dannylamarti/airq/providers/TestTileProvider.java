package nl.dannylamarti.airq.providers;

import android.content.Context;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by danny.lamarti on 10/12/16.
 */

public class TestTileProvider implements TileProvider {

    private final int width;
    private final int height;
    private final byte[] image;

    public TestTileProvider(Context context) {
        this(256, 256, context);
    }

    public TestTileProvider(int width, int height, Context context) {
        this.width = width;
        this.height = height;
        this.image = getImage(context);
    }

    private byte[] getImage(Context context) {
        InputStream input = null;
        byte[] bytes = null;

        try {
            input = context.getResources().getAssets().open("smiley.png");
            bytes = IOUtils.toByteArray(input);
        } catch (IOException e) {
            IOUtils.closeQuietly(input);
        }

        return bytes;
    }

    @Override
    public Tile getTile(int x, int y, int zoomLevel) {
        return new Tile(width, height, image);
    }
}
