package cs.dartmouth.edu.cs165.vm.stressmeter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

public class StressMeter extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_IMAGE_DETAIL = 1;
    private MediaPlayer mMediaPlayer = null;
    private Vibrator mVibrator = null;
    private boolean isMediaPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stress_meter);
        initViews();
        if(!isMediaPlaying){
            startMediaAndVibrate();
        }
    }
    @Override
    protected void onDestroy(){
        //Every time main activity (launcher) will destroy
        PSMScheduler.setSchedule(this); // we will set the alarm
        super.onDestroy();
    }

    /*
    Play alarm sound and vibrate phone
     */
    private void startMediaAndVibrate(){
        /*Media source = http://soundbible.com/2070-Railroad-Crossing-Bell.html*/
        isMediaPlaying = true;
        mMediaPlayer  = MediaPlayer.create(getBaseContext(), R.raw.rail_sound);
        mMediaPlayer.start();
        mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 100, 400, 300, 200, 100, 500, 200, 100}; //random pattern for validation
        mVibrator.vibrate(pattern, 0);
    }

    /*
    Stop playing alarm sound and vibration
     */
    private void stopMediaAndVibration(){
        if(isMediaPlaying) {
            isMediaPlaying = false;
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
            if (mVibrator != null) {
                mVibrator.cancel();
            }
        }
    }

    /*
    Main setup
     */
    private void initViews(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_stress_meter);
        addStressFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*
    Add stress fragment
     */
    private void addStressFragment(){
        StressGridFragment fragment = StressGridFragment.newInstance(mOnImageSelectedListener);
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.replace(R.id.flFragmentContainer,fragment);
        fragTrans.commit();
    }

    /*
    Add results fragment
     */
    private void addResultsFragment(){
        ResultsFragment fragment = ResultsFragment.newInstance();
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.replace(R.id.flFragmentContainer,fragment);
        fragTrans.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_IMAGE_DETAIL:{
                onActivityResultImageDetail(resultCode, data);
            }
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onActivityResultImageDetail(int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            finish();
        }else {

        }

    }

    /*
    When image selected, inflate it for the user to confirm/cancel, and stop alarm/vibration
     */
    private StressGridFragment.OnImageSelectedListener mOnImageSelectedListener = new StressGridFragment.OnImageSelectedListener() {
        @Override
        public void onImageSelected(int gridPosition, int resId) {
            stopMediaAndVibration();
            Log.e("VVV","Grid position = " + gridPosition + " resId = " + resId );
            Intent intent = new Intent(StressMeter.this,ImageDetailActivity.class);
            intent.putExtra(ImageDetailActivity.EXTRA_IMAGE_POSITION,gridPosition);
            intent.putExtra(ImageDetailActivity.EXTRA_IMAGE_ID,resId);
            startActivityForResult(intent,REQUEST_IMAGE_DETAIL);
        }

        @Override
        public void onMoreClicked() {
            stopMediaAndVibration();
        }
    };
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        stopMediaAndVibration();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_stress_meter) {
            // Handle the camera action
            addStressFragment();

        } else if (id == R.id.nav_results) {
            addResultsFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
