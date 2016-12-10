package nl.dannylamarti.airq;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.Iterator;

/**
 * Created by danny.lamarti on 10/12/16.
 */
public class MeasurementList implements Iterable<Measurement>, ValueEventListener {

    private DataSnapshot snapshot;
    private Runnable observer;

    public MeasurementList() {
    }

    public MeasurementList(DataSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    @Override
    public Iterator<Measurement> iterator() {
        if (snapshot == null) {
            return (Iterator) Collections.emptyList().iterator();
        } else {
            return new MeasurementIterator();
        }
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

    private class MeasurementIterator implements Iterator<Measurement> {

        final Iterator<DataSnapshot> wrapped = snapshot.getChildren().iterator();

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
