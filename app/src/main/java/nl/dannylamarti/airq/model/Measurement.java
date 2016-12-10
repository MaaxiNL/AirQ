package nl.dannylamarti.airq.model;

import com.google.firebase.database.DataSnapshot;

import java.util.Date;

/**
 * Created by danny.lamarti on 10/12/16.
 */
public class Measurement {

    private final DataSnapshot snapshot;

    public Measurement(DataSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    public float getLatitude() {
        return 51.9234838f;
    }

    public float getLongitude() {
        return 4.4681005f;
    }

    public float getAirQuality() {
        return getFloat("airQuality");
    }

    public Date getDateTime() {
        return (Date) snapshot.child("datetime").getValue();
    }

    private float getFloat(String key) {
        final Number number = (Number) snapshot.child(key).getValue();
        final float value = number.floatValue();

        return value;
    }
}
