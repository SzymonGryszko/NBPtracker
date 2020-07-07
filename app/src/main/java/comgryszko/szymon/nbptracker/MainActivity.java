package comgryszko.szymon.nbptracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import comgryszko.szymon.nbptracker.utils.FragmentPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String STATE_POSITION = "STATE_POSITION";

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int viewPagerPosition = savedInstanceState == null ? 0 : savedInstanceState.getInt(STATE_POSITION);
        setViewPager(viewPagerPosition);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_POSITION, viewPager.getCurrentItem());
    }

    private void setViewPager(int viewPagerPosition) {
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        viewPager.setCurrentItem(viewPagerPosition, true);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager, true);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_money);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.ic_gold);
    }
}
