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

        Collections.reverse(children);
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
        return new Measurement(children.get(0));
    }

    public boolean isEmpty() {
        return children.isEmpty();
    }

    public Measurement getLast() {
        final int lastIndex = children.size() - 1;
        final DataSnapshot snapshot = children.get(lastIndex);
        final Measurement measurement = new Measurement(snapshot);

        return measurement;
    }

    private final class MeasurementIterator implements Iterator<Measurement> {

        private final Iterator<DataSnapshot> it;

        public MeasurementIterator() {
            it = children.iterator();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Measurement next() {
            final DataSnapshot next = it.next();
            final Measurement measurement = new Measurement(next);

            return measurement;
        }
    }
}
