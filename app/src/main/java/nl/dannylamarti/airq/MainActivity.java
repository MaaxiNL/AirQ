package nl.dannylamarti.airq;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {

    @BindView(R.id.tabs) TabLayout tabs;
    @BindView(R.id.pager) ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        final int white = ContextCompat.getColor(this, android.R.color.white);

        setContentView(R.layout.main);
        ButterKnife.bind(this);

        tabs.setTabTextColors(white, white);
        tabs.setupWithViewPager(pager);
        pager.setAdapter(adapter);
    }

}
