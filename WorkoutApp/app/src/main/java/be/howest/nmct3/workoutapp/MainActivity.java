package be.howest.nmct3.workoutapp;


import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import be.howest.nmct3.workoutapp.data.Exercise;
import be.howest.nmct3.workoutapp.data.SettingsAdmin;
import be.howest.nmct3.workoutapp.data.WorkoutDatasoure;
import be.howest.nmct3.workoutapp.data.WorkoutsLoader;
import be.howest.nmct3.workoutapp.sync.SyncAdmin;


public class MainActivity extends FragmentActivity {

    public static String EXTRA_SELECTED_MUSCLEGROUP = "";
    public static String[] MuscleGroups = Exercise.MuscleGroups;
    public static int WORKOUT_ID = 0;
    public static int EXERCICE_ID = 0;
    public static int WORKOUT_EXERCICE_ID = 0;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private CharSequence mTitle;

    public static Fragment activeFragment;

    public static String plannerSelectedDate = "";
    public static Calendar phoneCalendar = Calendar.getInstance();
    public static int plannerSelectedWorkoutId = -1;

    public static WorkoutDatasoure workoutDatasource;

    public static Context baseContext;

    public static boolean todaysWorkoutClicked = false;
    public static int currentWorkoutPosition = -1;


    ActionBarDrawerToggle icon;

    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "be.howest.nmct3.workoutapp";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "be.howest.nmct3.workoutapp";
    // The account name
    public static final String ACCOUNT = "sync";
    // Instance fields
    public static Account mAccount;

    final String[] listTitles ={"Dashboard","Exercises","Workouts","Planner","Settings"};
    static final String[] fragments = {
            "be.howest.nmct3.workoutapp.DashboardFragment",
            "be.howest.nmct3.workoutapp.ExercisesFragment",
            "be.howest.nmct3.workoutapp.WorkoutsFragment",
            "be.howest.nmct3.workoutapp.PlannerFragment",
            "be.howest.nmct3.workoutapp.SettingsFragment",
            "be.howest.nmct3.workoutapp.AddNewWorkoutFragment",
            "be.howest.nmct3.workoutapp.Workout_Add_Exercise_List",
            "be.howest.nmct3.workoutapp.Workouts_SelectedWorkoutList_Fragment",
            "be.howest.nmct3.workoutapp.RepList",
            "be.howest.nmct3.workoutapp.AddWorkoutToPlannerFragment",
            "be.howest.nmct3.workoutapp.Exercises_Musclegroup_Fragment",
            "be.howest.nmct3.workoutapp.ExercisesFragment"
    };

    private CustomDrawerlayoutAdapter customDrawerLayoutAdapter;

    //Profile Picture Picker
    private static int RESULT_LOAD_IMAGE = 1;
    public static String myProfilePicturePath = "Picture.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseContext = getBaseContext();

        mAccount = CreateSyncAccount(this);
        workoutDatasource = new WorkoutDatasoure();

        runSyncAdapter();

        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer);

        //set adapter
        customDrawerLayoutAdapter = new CustomDrawerlayoutAdapter();
        mDrawerList.setAdapter(customDrawerLayoutAdapter);

        //set list click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        //set background color
        mDrawerList.setBackgroundColor(Color.parseColor("#FFFFFF"));

        // Create and set the start fragment (Dashboard)
        Fragment frag = Fragment.instantiate(MainActivity.this, fragments[0]);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, frag).commit();
        getActionBar().setTitle(listTitles[0]);

        activeFragment = frag;

        //remove ic_launcher icon from actionbar
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.app_name,  /* "open drawer" description */
                R.string.app_name  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        String name = SettingsAdmin.getInstance(getBaseContext()).getUsername();

        Toast.makeText(getApplicationContext(), "Logged in as: " + name,
                Toast.LENGTH_LONG).show();

    }

    private void runSyncAdapter(){

        SyncAdmin.getInstance(getApplicationContext()).setAllowExercisesDownload(true);
        SyncAdmin.getInstance(getApplicationContext()).setAllowWorkoutsDownload(true);

        Log.d("MainActivity","runSyncAdapter() all set to true" );

        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);
    }

    public static void syncWorkoutsUp(){

        SyncAdmin.getInstance(baseContext).setAllowWorkoutsUpload(true);

        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);
    }

    public static void syncPlannerUp(){

        SyncAdmin.getInstance(baseContext).setAllowPlannersUpload(true);

        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {

        // Create and set a new fragment
        Fragment frag = Fragment.instantiate(MainActivity.this, fragments[position]);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, frag).commit();

        activeFragment = frag;
        Toast.makeText(getBaseContext(), "Active fragment: " + frag.getClass().getSimpleName(),Toast.LENGTH_SHORT).show();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(listTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    //creates the items on action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //hides the action menu icons when drawer is open
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        //menu.findItem(R.id.action_add).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    class CustomDrawerlayoutAdapter extends ArrayAdapter<String> {

        public CustomDrawerlayoutAdapter() {
            super(getBaseContext(), R.layout.navdrawer_row_layout, R.id.navdrawer_title);
            this.addAll(listTitles);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);

            String NavDrawerTitle = listTitles[position];

            TextView txtNavDrawerTitle = (TextView) row.findViewById(R.id.navdrawer_title);
            txtNavDrawerTitle.setText(NavDrawerTitle);

            ImageView imgNavDrawerIcon = (ImageView) row.findViewById(R.id.navdrawer_icon);
            imgNavDrawerIcon.setImageResource(getResources().getIdentifier(listTitles[position].toLowerCase(),"drawable",getPackageName()));

            return row;
        }
    }


    public static Account CreateSyncAccount(Context context){
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager= (AccountManager) context.getSystemService(ACCOUNT_SERVICE);

        if(accountManager.addAccountExplicitly(newAccount, null, null)){
            return newAccount;
        }else{
            Log.d("","_______________ Error Creating sync account");
            return newAccount;
        }
    }

    public void startProfilePicturePicker() {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            myProfilePicturePath = cursor.getString(columnIndex);
            cursor.close();

            // String picturePath contains the path of selected Image
            Toast.makeText(getBaseContext(), "Selected Profile Picture Path: " + BitmapFactory.decodeFile(myProfilePicturePath), Toast.LENGTH_SHORT).show();
            SettingsAdmin.getInstance(getApplicationContext()).setPicture(myProfilePicturePath);

            ((SettingsFragment.CustomSettingsAdapter)SettingsFragment.listview.getAdapter()).updateImage(selectedImage);
        }
    }
}
