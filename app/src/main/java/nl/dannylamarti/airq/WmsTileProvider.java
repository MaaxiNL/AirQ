package nl.dannylamarti.airq;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

/**
 * Created by danny.lamarti on 10/12/16.
 */
public class WmsTileProvider extends UrlTileProvider {

    public final static String KADASTRAL_WMS_URL = "http://geodata.nationaalgeoregister.nl/kadastralekaartv2/wms";
    public final static String TEST_WMS_URL = "https://sampleserver1.arcgisonline.com/ArcGIS/rest/services/Specialty/ESRI_StateCityHighway_USA/MapServer/export";
    // "https://sampleserver1.arcgisonline.com/ArcGIS/rest/services/Specialty/ESRI_StateCityHighway_USA/MapServer/export?bbox=%s&bboxSRS=3857&layers=&layerdefs=&size=256&imageSR=3857&format=png&transparent=true&dpi=90&time=&layerTimeOptions=&f=image";
    public final static String SNOWY_MAP = "http://ows.terrestris.de/osm-gray/service?LAYERS=OSM-WMS&EXCEPTIONS=application%2Fvnd.ogc.se_inimage&UID=268c5d67&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&STYLES=&FORMAT=image%2Fjpeg&SRS=EPSG%3A4326&BBOX=--1--,--2--,--3--,--4--&WIDTH=256&HEIGHT=256";
    public final static String BRIEVENBUSSEN_MAP = "https://services.geodan.nl/public/data/my/gws/POST4277VOOR/wms?LAYERS=brievenbussen_d027e429-2dcc-4171-97f8-f6ab7fa8e861&TRANSPARENT=TRUE&EXCEPTIONS=application%2Fvnd.ogc.se_inimage&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&STYLES=&FORMAT=image%2Fpng&SRS=EPSG%3A4326&SLD=https%3A%2F%2Fservices.geodan.nl%2Fpublic%2Fdocument%2FPOST4277VOOR%2Fapi%2Fdata%2FPOST4277VOOR%2Fstyles%2FPOST4277VOOR_public%3Abrievenbussen_d027e429-2dcc-4171-97f8-f6ab7fa8e861%3Abrievenbussen&PUP=1480942279829&BBOX=--1--,--2--,--3--,--4--&WIDTH=256&HEIGHT=256&servicekey=bb5cd4d3-b180-11e6-84a1-005056805b87";
    public final static String AIRQ_MAP = "https://services.geodan.nl/public/data/my/gws/GEOD2219HACK/wms" +
            "?request=getmap" +
            "&featuretype=test_423a776e-a626-404c-bcdd-4e403a548a65" +
            "&WIDTH=256" +
            "&HEIGHT=256" +
            "&TRANSPARENT=TRUE" +
            "&LAYERS=" +
            "&EXCEPTIONS=application%2Fvnd.ogc.se_inimage" +
            "&SERVICE=WMS" +
            "&VERSION=1.1.1" +
            "&FORMAT=image%2Fpng" +
            "&SRS=EPSG%3A4326" +
            "&BBOX=--1--,--2--,--3--,--4--";
    public final static String AIRQ_MAP_PRETTY = "https://services.geodan.nl/public/data/my/gws/GEOD2219HACK/wms\n" +
            "?request=getmap\n" +
            "\t&featuretype=test_423a776e-a626-404c-bcdd-4e403a548a65\n" +
            "\t&WIDTH=256\n" +
            "\t&HEIGHT=256\n" +
            "\t&TRANSPARENT=TRUE\n" +
            "\t&LAYERS=\n" +
            "\t&EXCEPTIONS=application%2Fvnd.ogc.se_inimage\n" +
            "\t&SERVICE=WMS\n" +
            "\t&VERSION=1.1.1\n" +
            "\t&FORMAT=image%2Fpng\n" +
            "\t&SRS=EPSG%3A4326\n" +
            "\t&BBOX=--1--,--2--,--3--,--4--";

    private final String source;

    public WmsTileProvider(String source) {
        this(256, 256, source);
    }

    public WmsTileProvider(int width, int height, String source) {
        super(width, height);
        this.source = source;
    }

    @Override
    public URL getTileUrl(int x, int y, int zoom) {
        final WmsTileBounds bounds = new WmsTileBounds(x, y, zoom);
        final String text = source
                .replace("--1--", String.format(Locale.US, "%.5f", bounds.lonMin))
                .replace("--2--", String.format(Locale.US, "%.5f", bounds.latMax))
                .replace("--3--", String.format(Locale.US, "%.5f", bounds.lonMax))
                .replace("--4--", String.format(Locale.US, "%.5f", bounds.latMin));
        final URL url = getUrl(text);

        return url;
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
