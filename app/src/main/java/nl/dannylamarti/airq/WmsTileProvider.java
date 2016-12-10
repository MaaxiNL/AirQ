package nl.dannylamarti.airq;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.UrlTileProvider;

import org.apache.commons.io.IOUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

/**
 * Created by danny.lamarti on 10/12/16.
 */
public class WmsTileProvider extends UrlTileProvider {

    public final static String KADASTRAL_WMS_URL = "http://geodata.nationaalgeoregister.nl/kadastralekaartv2/wms";
    public final static String TEST_WMS_URL = "https://sampleserver1.arcgisonline.com/ArcGIS/rest/services/Specialty/ESRI_StateCityHighway_USA/MapServer/export";


    private static final String WMS_FORMAT = "%s" +
            "?bbox=%s" +
            "&bboxSR=%s" +
            "&layers=%s" +
            "&layerdefs=%s" +
            "&size=%d" +
            "&imageSR=%s" +
            "&format=%s" + //png,png8,png24,jpg,pdf,bmp,gif, svg
            "&transparent=%b" +
            "&dpi=%d" +
            "&time=%s" +
            "&layerTimeOptions=%s" +
            "&format=%s"; // html,image,kmz,json

    private static final String GEOSERVER_FORMAT = "%s" +
            "?service=WMS" +
            "&version=1.1.1" +
            "&request=GetMap" +
            "&layers=yourLayer" +
            "&bbox=%f,%f,%f,%f" +
            "&width=256" +
            "&height=256" +
            "&srs=EPSG:900913" +
            "&format=image/png" +
            "&transparent=true";


    private static final double[] TILE_ORIGIN = {-20037508.34789244, 20037508.34789244};
    private static final int ORIG_X = 0;
    private static final int ORIG_Y = 1;
    private static final double MAP_SIZE = 20037508.34789244 * 2;

    protected static final int MINX = 0;
    protected static final int MAXX = 1;
    protected static final int MINY = 2;
    protected static final int MAXY = 3;

    public WmsTileProvider() {
        super(256, 256);
    }

    public WmsTileProvider(int width, int height) {
        super(width, height);
    }

    protected double[] getBoundingBox(int x, int y, int zoom) {
        double tileSize = MAP_SIZE / Math.pow(2, zoom);
        double minx = TILE_ORIGIN[ORIG_X] + x * tileSize;
        double maxx = TILE_ORIGIN[ORIG_X] + (x + 1) * tileSize;
        double miny = TILE_ORIGIN[ORIG_Y] - (y + 1) * tileSize;
        double maxy = TILE_ORIGIN[ORIG_Y] - y * tileSize;

        double[] bbox = new double[4];

        bbox[MINX] = minx;
        bbox[MINY] = miny;
        bbox[MAXX] = maxx;
        bbox[MAXY] = maxy;

        return bbox;
    }

    @Override
    public URL getTileUrl(int x, int y, int zoom) {
        return getUrl("https://sampleserver1.arcgisonline.com/ArcGIS/rest/services/Specialty/ESRI_StateCityHighway_USA/MapServer/export?bbox=-10018754.171394622%2C5009377.085697312%2C-7514065.628545966%2C7514065.628545968&bboxSR=3857&layers=&layerdefs=&size=256&imageSR=3857&format=png&transparent=true&dpi=90&time=&layerTimeOptions=&f=image");
    }

    @NonNull
    private URL getUrl(String text) {
        final URL url;

        try {
            url = new URL(text);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return url;
    }

}
