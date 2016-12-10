package nl.dannylamarti.airq;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by danny.lamarti on 10/12/16.
 */
public class Measurement {

    private final DataSnapshot snapshot;

    public Measurement(DataSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    public float getLatitude() {
        return getFloat("latitude");
    }

    public float getLongitude() {
        return getFloat("longitude");
    }

    public float getAirQuality() {
        return getFloat("airQuality");
    }

    private float getFloat(String key) {
        final Number number = (Number) snapshot.child(key).getValue();
        final float value = number.floatValue();

        return value;
    }
}
