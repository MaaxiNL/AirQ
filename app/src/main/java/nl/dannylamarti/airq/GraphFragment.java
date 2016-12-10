package nl.dannylamarti.airq;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
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
    @BindView(R.id.message) TextView message;
    @BindView(R.id.chart) GraphView chart;

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
        initGraph();
        run();
    }

    private void initGraph() {
        final int white = ContextCompat.getColor(getActivity(), android.R.color.white);
        final int transparent = ContextCompat.getColor(getActivity(), android.R.color.transparent);
        final GridLabelRenderer renderer = chart.getGridLabelRenderer();

        renderer.setGridColor(transparent);
        renderer.setVerticalLabelsColor(white);
        renderer.setHorizontalLabelsColor(white);
        renderer.setHorizontalAxisTitle("minutes");
        renderer.setHorizontalAxisTitleColor(white);
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
            final float airQuality = measurements.getFirst().getAirQuality();
            final AirState state = AirState.fromAirQuality(airQuality);
            final LineGraphSeries data = getLineData();

            content.setBackgroundColor(state.getColor(context));
            rating.setText("" + ((int) airQuality));
            remark.setText(state.getRemark(context));
            message.setText(state.getMessage(context));
            chart.removeAllSeries();
            chart.addSeries(data);
        } else {
            final int color = ContextCompat.getColor(context, R.color.colorPrimary);

            content.setBackgroundColor(color);
            rating.setText(":-(");
            remark.setText("No data available yet");
            message.setText("");
            chart.removeAllSeries();
        }
    }

    @NonNull
    private LineGraphSeries getLineData() {
        final List<DataPoint> points = new ArrayList();
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        final int thickness = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics);

        for (int i = 0; i < 120 && i < measurements.size(); i++) {
            final Measurement measurement = measurements.get(i);
            final DataPoint dataPoint = new DataPoint(
                    i,
                    measurement.getAirQuality()
            );

            points.add(dataPoint);
        }

        final DataPoint[] array = points.toArray(new DataPoint[points.size()]);
        final LineGraphSeries series = new LineGraphSeries(array);

        series.setColor(Color.WHITE);
        series.setThickness(thickness);

        return series;
    }

}
