package be.howest.nmct3.workoutapp;


import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import be.howest.nmct3.workoutapp.data.Exercise;


public class MainActivity extends FragmentActivity {

    public static String EXTRA_SELECTED_MUSCLEGROUP = "";
    public static String[] MuscleGroups = Exercise.MuscleGroups;
    public static int WORKOUT_ID = 0;
    public static int EXERCICE_ID = 0;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private CharSequence mTitle;

    public Fragment activeFragment;

    ActionBarDrawerToggle icon;

    final String[] listTitles ={"Dashboard","Exercises","Workouts","Planner","Settings"};
    final String[] fragments ={
            "be.howest.nmct3.workoutapp.DashboardFragment",
            "be.howest.nmct3.workoutapp.ExercisesFragment",
            "be.howest.nmct3.workoutapp.WorkoutsFragment",
            "be.howest.nmct3.workoutapp.PlannerFragment",
            "be.howest.nmct3.workoutapp.SettingsFragment",
            "be.howest.nmct3.workoutapp.AddNewWorkoutFragment"};

    private CustomDrawerlayoutAdapter customDrawerLayoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        //Toast.makeText(getBaseContext(), frag.getClass().getSimpleName(),Toast.LENGTH_SHORT).show();

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

        int id = item.getItemId();
        Log.d("", "id: " + id);


            OpenNewWorkoutFragment();

        return super.onOptionsItemSelected(item);
    }

    private void OpenNewWorkoutFragment() {
        // Create and set the start fragment
        Fragment frag = Fragment.instantiate(MainActivity.this, fragments[5]);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        activeFragment = frag;
        Log.d("","" + activeFragment.getClass().getName());
        transaction.replace(R.id.main, frag).commit();
        getActionBar().setTitle("Add new workout");
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
}
