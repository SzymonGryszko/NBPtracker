package comgryszko.szymon.nbptracker.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import comgryszko.szymon.nbptracker.ExchangeRateFragment;
import comgryszko.szymon.nbptracker.GoldRateFragment;

public class FragmentPagerAdapter extends androidx.fragment.app.FragmentPagerAdapter {

    private Fragment exchangeRateFragment;
    private Fragment goldRateFragment;

    public FragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        exchangeRateFragment = new ExchangeRateFragment();
        goldRateFragment = new GoldRateFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return exchangeRateFragment;
        }
        else {
            return goldRateFragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
