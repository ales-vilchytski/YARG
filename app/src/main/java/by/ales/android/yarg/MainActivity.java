package by.ales.android.yarg;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import by.ales.android.yarg.data.GenerationParameters;
import by.ales.android.yarg.data.GenerationResult;
import by.ales.android.yarg.data.NumbersGenerationParameters;
import by.ales.android.yarg.data.NumbersGenerationResult;
import by.ales.android.yarg.data.ResultCallback;
import by.ales.android.yarg.fragments.BlankFragment;
import by.ales.android.yarg.fragments.NubmersGenerationFragment;
import by.ales.android.yarg.fragments.listeners.GenerationParametersReadyListener;
import by.ales.android.yarg.service.GenerationService;
import by.ales.android.yarg.utils.ResultCallbackWrapper;

public class MainActivity extends AppCompatActivity implements GenerationParametersReadyListener {
    private static final String TAG = "MainActivity";

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private GenerationService generationService;
    private ServiceConnection generationServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            generationService = ((GenerationService.LocalBinder)service).getService();
        }
        public void onServiceDisconnected(ComponentName className) {
            generationService = null;
        }
    };
    private boolean isGenerationServiceBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.main_pager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.main_pager_tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        doBindService();
    }


    private void setupViewPager(ViewPager pager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(NubmersGenerationFragment.newInstance(), getText(R.string.fragment_numbers_generation_title).toString());
        adapter.addFragment(new BlankFragment(), getText(R.string.fragment_title_lists).toString());
        adapter.addFragment(new BlankFragment(), getText(R.string.fragment_title_dices).toString());
        pager.setAdapter(adapter);
    }

    @Override
    public void onGenerationParametersReady(GenerationParameters data, ResultCallback callback) {
        Log.d(TAG, "MainActivity.onFragmentInteraction - " + data.toString());
        if (generationService == null) {
            callback.call(null);
            return;
        }
        if (data.getType() == GenerationParameters.Type.NUMBERS) {
            generationService.generateRandomNumbers((NumbersGenerationParameters) data, new ResultCallbackWrapper(callback, this));
        }
    }

    private void doBindService() {
        bindService(new Intent(this, GenerationService.class), generationServiceConnection, Context.BIND_AUTO_CREATE);
        isGenerationServiceBound = true;
    }

    void doUnbindService() {
        if (isGenerationServiceBound) {
            unbindService(generationServiceConnection);
            isGenerationServiceBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

}
