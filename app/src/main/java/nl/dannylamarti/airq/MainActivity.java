package nl.dannylamarti.airq;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, Runnable {

    private GoogleMap map;
    private final Measurements measurements = new Measurements();
    private final Collection<Marker> markers = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        measurements.setObserver(this);

        FirebaseDatabase.getInstance()
                .getReference("data")
                .addValueEventListener(measurements);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        measurements.setObserver(null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        final LatLngBounds bounds = new LatLngBounds(
                new LatLng(50.720f, 3.784f),
                new LatLng(53.492f, 6.838)
        );
        final CameraUpdate update = CameraUpdateFactory.newLatLngBounds(
                bounds,
                metrics.widthPixels,
                metrics.heightPixels,
                0
        );
        final TileOverlayOptions overlayOptions = new TileOverlayOptions()
                .tileProvider(new WmsTileProvider(WmsTileProvider.AIRQ_MAP));

        map = googleMap;
        map.moveCamera(update);
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        map.addTileOverlay(overlayOptions);
    }

    @Override
    public void run() {
        removeMarkers();
//        addMarkers();
    }

    private void removeMarkers() {
        final Iterator<Marker> it = markers.iterator();

        while(it.hasNext()) {
            final Marker marker = it.next();

            marker.remove();
            it.remove();
        }
    }

    private void addMarkers() {
        for (Measurement measurement : measurements) {
            final float quality = measurement.getAirQuality();
            final float hue = (quality * 1.8f + 330f) % 360f;
            final LatLng latLng = new LatLng(
                    measurement.getLatitude(),
                    measurement.getLongitude());
            final MarkerOptions options = new MarkerOptions()
                    .position(latLng);
            final Marker marker = map.addMarker(options);

            markers.add(marker);
        }
    }

}
