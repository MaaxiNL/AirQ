package nl.dannylamarti.airq;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import timber.log.Timber;

/**
 * Created by danny.lamarti on 10/12/16.
 */

public class MoonTileProvider implements TileProvider {


    private static final String MOON_MAP_URL_FORMAT =
            "http://mw1.google.com/mw-planetary/lunar/lunarmaps_v1/clem_bw/%d/%d/%d.jpg";
    private final int width;
    private final int height;

    public MoonTileProvider() {
        this(256, 256);
    }

    public MoonTileProvider(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public Tile getTile(int x, int y, int zoom) {
        final int reversedY = (1 << zoom) - y - 1;
        final String text = String.format(Locale.US, MOON_MAP_URL_FORMAT, zoom, x, reversedY);
        final URL url = getUrl(text);
        final Tile tile = getTile(width, height, url);

        return tile;
    }

    @Nullable
    private Tile getTile(int x, int y, URL url) {
        InputStream input = null;
        Tile tile = null;

        try {
            input = url.openStream();
            tile = new Tile(x, y, getBytes(input));
        } catch (IOException e) {
            Timber.e(e, "error in fetching tile");
        } finally {
            closeQuietly(input);
        }

        return tile;
    }

    private void closeQuietly(InputStream input) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
            }
        }
    }

    @NonNull
    private URL getUrl(String text) {
        try {
            return new URL(text);
        } catch (MalformedURLException e) {
            throw new AssertionError(e);
        }
    }

    private byte[] getBytes(InputStream input) throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        convertBytes(input, output);
        return output.toByteArray();
    }

    private long convertBytes(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[4096];
        long current = 0L;

        while (true) {
            int in = input.read(buffer);

            if (in == -1)
                return current;

            output.write(buffer, 0, in);
            current += (long) in;
        }
    }
}

