package be.howest.nmct3.workoutapp;



import android.content.Intent;
import android.content.SharedPreferences;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import be.howest.nmct3.workoutapp.data.Exercise;
import be.howest.nmct3.workoutapp.data.WorkoutsLoader;


public class MainActivity extends FragmentActivity {

    public static String EXTRA_SELECTED_MUSCLEGROUP = "";
    public static String[] MuscleGroups = Exercise.MuscleGroups;
    public static int WORKOUT_ID = 0;
    public static int EXERCICE_ID = 0;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private CharSequence mTitle;

    public static Fragment activeFragment;

    public static Cursor plannerWorkoutCursor;
    public static int plannerSelectedWorkoutId = -1;

    ActionBarDrawerToggle icon;

    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "be.howest.nmct3.workoutapp";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "be.howest.nmct3.workoutapp.account";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    // Instance fields
    Account mAccount;

    //add new workout (lijst opvullen met exercises)
    public static ArrayList<String> selectedExercises = new ArrayList<String>();

    final String[] listTitles ={"Dashboard","Exercises","Workouts","Planner","Settings"};
    static final String[] fragments = {
            "be.howest.nmct3.workoutapp.DashboardFragment",
            "be.howest.nmct3.workoutapp.ExercisesFragment",
            "be.howest.nmct3.workoutapp.WorkoutsFragment",
            "be.howest.nmct3.workoutapp.PlannerFragment",
            "be.howest.nmct3.workoutapp.SettingsFragment",
            "be.howest.nmct3.workoutapp.AddNewWorkoutFragment",
            "be.howest.nmct3.workoutapp.AddNewWorkoutSelectedExercisesList",
            "be.howest.nmct3.workoutapp.Workout_Add_Exercise_List",
            "be.howest.nmct3.workoutapp.Workouts_SelectedWorkoutList_Fragment",
            "be.howest.nmct3.workoutapp.AddWorkoutToPlannerFragment"
    };

    private CustomDrawerlayoutAdapter customDrawerLayoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mAccount = CreateSyncAccount(this);

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

        loadWorkouts();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("USERNAME","");

        Toast.makeText(getApplicationContext(), "Logged in as: " + name,
                Toast.LENGTH_LONG).show();

    }

    private void runSyncAdapter(){

        Log.d("","_________________ runSyncAdapter()");
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);

    }

    private void loadWorkouts() {
        WorkoutsLoader wl = new WorkoutsLoader(MainActivity.this);
        MainActivity.plannerWorkoutCursor = wl.loadInBackground();
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
        //Toast.makeText(getBaseContext(), "Active fragment: " + frag.getClass().getSimpleName(),Toast.LENGTH_SHORT).show();

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
        for(int i=0;i<fragments.length;i++)
        {
            if(fragments[i].equals(activeFragment.getClass().getName()))
            {
                switch (i)
                {
                    //Dashboard
                    case 0:
                        getMenuInflater().inflate(R.menu.my, menu);
                        break;
                    //Exercises
                    case 1:
                        getMenuInflater().inflate(R.menu.exercises, menu);
                        break;
                    //Workouts
                    case 2:
                        getMenuInflater().inflate(R.menu.workout, menu);
                        break;
                    //Planner
                    case 3:
                        getMenuInflater().inflate(R.menu.planner, menu);
                        break;
                    //Settings
                    case 4:
                        getMenuInflater().inflate(R.menu.settings, menu);
                        break;
                    //Add New Workout
                    case 5:
                        getMenuInflater().inflate(R.menu.my, menu);
                        break;
                    //New Workout Selected Exercises List
                    case 6:
                        getMenuInflater().inflate(R.menu.new_workout_selected_exercises, menu);
                        break;
                    //New Workout Exercises List
                    case 7:
                        getMenuInflater().inflate(R.menu.new_workout_all_exercises, menu);
                        break;
                    //Add Exercise To Selected Workout
                    case 8:
                        getMenuInflater().inflate(R.menu.workouts_workoutselected_list, menu);
                        break;
                    //Add Workout To Planner
                    case 9:
                        getMenuInflater().inflate(R.menu.my, menu);
                        break;
                    default:
                        getMenuInflater().inflate(R.menu.my, menu);
                        break;
                }
            }
        }

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

        switch (item.getItemId()) {

            //Geklikt op "+" bij workouts
            case R.id.action_add_workout:
                //Toast.makeText(this, item.getTitle()+" clicked!", Toast.LENGTH_SHORT).show();
                OpenAddNewWorkoutFragment();
                break;
            //Geklikt op een workout onder workouts
            case R.id.action_add_exercise_to_exercises_selected_workout:
                Toast.makeText(this, item.getTitle()+" clicked!", Toast.LENGTH_SHORT).show();
                break;
            //Geklikt op "+" bij new workout selected exercises list
            case R.id.action_add_exercise_new_workout_selected_exercises:
                //Toast.makeText(this, item.getTitle()+" clicked!", Toast.LENGTH_SHORT).show();
                OpenNewWorkoutExercisesList();
                break;
            //Geklikt op "search" bij new workout selected exercises list
            case R.id.action_search_new_workout_selected_exercises:
                //Toast.makeText(this, item.getTitle()+" clicked!", Toast.LENGTH_SHORT).show();
                break;
            //Geklikt op "search" bij exercises
            case R.id.action_search:
                //Toast.makeText(this, item.getTitle()+" clicked!", Toast.LENGTH_SHORT).show();
                break;
            //Geklikt op "+" bij planner
            case R.id.action_add_workout_to_planner:
                //Toast.makeText(this, item.getTitle()+" clicked!", Toast.LENGTH_SHORT).show();
                break;
            default:
                //Toast.makeText(this, item.getTitle()+" clicked!", Toast.LENGTH_SHORT).show();
                break;
        }

        //Log.d("", "ACTIVE FRAGMENT: " + activeFragment.getClass().getName());

        return super.onOptionsItemSelected(item);
    }

    private void OpenAddNewWorkoutFragment() {
        // Create and set the start fragment
        Fragment frag = Fragment.instantiate(MainActivity.this, "be.howest.nmct3.workoutapp.AddNewWorkoutFragment");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        activeFragment = frag;
        selectedExercises = new ArrayList<String>();

        transaction.replace(R.id.main, frag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private void OpenNewWorkoutExercisesList() {
        // Create and set the start fragment
        Fragment frag = Fragment.instantiate(MainActivity.this, "be.howest.nmct3.workoutapp.Workout_Add_Exercise_List");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        activeFragment = frag;

        transaction.replace(R.id.main, frag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private void OpenAddNewWorkoutInPlannerFragment()
    {
        Fragment frag = Fragment.instantiate(MainActivity.this, "be.howest.nmct3.workoutapp.AddWorkoutToPlannerFragment");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        activeFragment = frag;

        transaction.replace(R.id.main, frag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
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

}
