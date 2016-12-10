package nl.dannylamarti.airq;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import nl.dannylamarti.airq.model.Measurement;
import nl.dannylamarti.airq.model.Measurements;

/**
 * Created by danny.lamarti on 10/12/16.
 */

public class GraphFragment extends Fragment implements Runnable {

    @BindView(R.id.content) View content;
    @BindView(R.id.place) TextView place;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.remark) TextView remark;
    @BindView(R.id.quote) TextView quote;
    @BindView(R.id.chart) GraphView chart;

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
        final Activity context = getActivity();

        if (!measurements.isEmpty()) {
            final float airQuality = measurements.getLast().getAirQuality();
            final AirState state = AirState.fromAirQuality(airQuality);
            final LineGraphSeries data = getLineData();

            content.setBackgroundColor(state.getColor(context));
            rating.setText("" + ((int) airQuality));
            remark.setText(state.getRemark(context));
            quote.setText(state.getQuote(context));
            chart.removeAllSeries();
            chart.addSeries(data);
        } else {
            final int color = ContextCompat.getColor(context, R.color.colorPrimary);

            content.setBackgroundColor(color);
            rating.setText(":-(");
            remark.setText("No data available yet");
            quote.setText("");
            chart.removeAllSeries();
        }
    }

    @NonNull
    private LineGraphSeries getLineData() {
        final List<DataPoint> points = new ArrayList();

        for (int i = 0; i < measurements.size(); i++) {

        }
        for (Measurement measurement : measurements) {
            final Date date = measurement.getDateTime();
            final DataPoint dataPoint = new DataPoint(
                    date.getTime(),
                    measurement.getAirQuality()
            );

            points.add(dataPoint);
        }

        final LineGraphSeries series = new LineGraphSeries(points.toArray(new DataPoint[points.size()]));

        series.setTitle("Random Curve 1");
        series.setColor(Color.RED);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);

        return series;
    }

}
