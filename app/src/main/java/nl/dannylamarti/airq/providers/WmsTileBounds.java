package nl.dannylamarti.airq.providers;

import lombok.Getter;

/**
 * Created by danny.lamarti on 10/12/16.
 */
@Getter
final class WmsTileBounds {

    public final int x;
    public final int y;
    public final int zoom;
    public final double n;
    public final double lonMin;
    public final double latMin;
    public final double lonMax;
    public final double latMax;

    public WmsTileBounds(int x, int y, int zoom) {
        this.x = x;
        this.y = y;
        this.zoom = zoom;
        this.n = Math.pow(2, zoom);
        this.lonMin = getLon(x, n);
        this.latMin = getLat(y, n);
        this.lonMax = getLon(x + 1, n);
        this.latMax = getLat(y + 1, n);
    }

    private double getLat(int y, double n) {
        final double rad = Math.atan(Math.sinh(Math.PI * (1 - 2 * y / n)));
        final double lat = rad * 180 / Math.PI;

        return lat;
    }

    private double getLon(int x, double n) {
        return x / n * 360 - 180;
    }

}
