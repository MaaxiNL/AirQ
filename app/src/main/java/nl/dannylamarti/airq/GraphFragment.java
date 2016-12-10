package nl.dannylamarti.airq;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import nl.dannylamarti.airq.model.Measurement;
import nl.dannylamarti.airq.model.Measurements;

/**
 * Created by danny.lamarti on 10/12/16.
 */

public class GraphFragment extends Fragment implements Runnable {

    @BindView(R.id.place) TextView place;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.remark) TextView remark;
    @BindView(R.id.quote) TextView quote;
    @BindView(R.id.chart) LineChart chart;

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("mm-dd");
    private final Measurements measurements = new Measurements();
    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabase.getInstance()
                .getReference("data")
                .addValueEventListener(measurements);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.graph, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);
        measurements.setObserver(this);
        run();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        measurements.setObserver(null);
        unbinder.unbind();
    }

    @Override
    public void run() {
        if (!measurements.isEmpty()) {
            final float airQuality = measurements.getFirst().getAirQuality();
            final AirState state = AirState.fromAirQuality(airQuality);
            final Activity context = getActivity();
            final LineData data = getLineData();

            rating.setText("" + ((int) airQuality));
            remark.setText(state.getRemark(context));
            quote.setText(state.getQuote(context));
            chart.setData(data);
        } else {
            rating.setText(":-(");
            remark.setText("No data available yet");
            quote.setText("");
            chart.clear();
        }
    }

    @NonNull
    private LineData getLineData() {
        final LineData data = new LineData();

        for (Measurement measurement : measurements) {
            final Date date = measurement.getDateTime();
            final Entry entry = new Entry(date.getTime(), measurement.getAirQuality());
            final LineDataSet dataSet = new LineDataSet(
                    Collections.singletonList(entry),
                    dateFormatter.format(date)
            );

            data.addDataSet(dataSet);
        }

        return data;
    }

}
