package nl.dannylamarti.airq;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.Iterator;

/**
 * Created by danny.lamarti on 10/12/16.
 */
public class Measurements implements Iterable<Measurement>, ValueEventListener {

    private DataSnapshot snapshot;
    private Runnable observer;

    public Measurements() {
    }

    public Measurements(DataSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    @Override
    public Iterator<Measurement> iterator() {
        return new MeasurementIterator();
    }

    @Override
    public void onDataChange(DataSnapshot snapshot) {
        this.snapshot = snapshot;
        notifyObserver();
    }

    @Override
    public void onCancelled(DatabaseError error) {
        this.snapshot = null;
    }

    public void setObserver(Runnable observer) {
        this.observer = observer;
    }

    private void notifyObserver() {
        if (observer != null)
            observer.run();
    }

    private final class MeasurementIterator implements Iterator<Measurement> {

        final Iterator<DataSnapshot> wrapped;

        public MeasurementIterator() {
            if (snapshot != null) {
                wrapped = snapshot.getChildren().iterator();
            } else {
                wrapped = (Iterator) Collections.emptyList().iterator();
            }
        }

        @Override
        public boolean hasNext() {
            return wrapped.hasNext();
        }

        @Override
        public Measurement next() {
            return new Measurement(wrapped.next());
        }
    }
}
