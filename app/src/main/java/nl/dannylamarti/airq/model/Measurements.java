package nl.dannylamarti.airq.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by danny.lamarti on 10/12/16.
 */
public class Measurements implements Iterable<Measurement>, ValueEventListener {

    private final List<DataSnapshot> children = new ArrayList<>();
    private Runnable observer;

    public Measurements() {
    }

    public Measurements(DataSnapshot snapshot) {
        invalidateChildren(snapshot);
        notifyObserver();
    }

    @Override
    public Iterator<Measurement> iterator() {
        return new MeasurementIterator();
    }

    @Override
    public void onDataChange(DataSnapshot snapshot) {
        invalidateChildren(snapshot);
        notifyObserver();
    }

    private void invalidateChildren(DataSnapshot snapshot) {
        children.clear();

        for (DataSnapshot item : snapshot.getChildren()) {
            children.add(item);
        }
    }

    @Override
    public void onCancelled(DatabaseError error) {
        children.clear();
    }

    public void setObserver(Runnable observer) {
        this.observer = observer;
    }

    private void notifyObserver() {
        if (observer != null)
            observer.run();
    }

    public Measurement getFirst() {
        return new Measurement(snapshot.child("0"));
    }

    public boolean isEmpty() {
        return snapshot == null || snapshot.getChildrenCount() == 0;
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
            final DataSnapshot next = wrapped.next();
            final Measurement measurement = new Measurement(next);

            return measurement;
        }
    }
}
