package be.howest.nmct3.workoutapp;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import be.howest.nmct3.workoutapp.data.Contract;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class PlannerFragment extends Fragment {

    private CursorAdapter myPlannerAdapter;
    private ListView listView;

    public PlannerFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Context context) {
        PlannerFragment frag = new PlannerFragment();
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.planner, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            //Geklikt op "+" bij planner
            case R.id.action_add_workout_to_planner:
                //Toast.makeText(this, item.getTitle()+" clicked!", Toast.LENGTH_SHORT).show();
                OpenAddNewWorkoutInPlannerFragment();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void OpenAddNewWorkoutInPlannerFragment()
    {
        Fragment frag = Fragment.instantiate(getActivity(), "be.howest.nmct3.workoutapp.AddWorkoutToPlannerFragment");
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        MainActivity.activeFragment = frag;

        transaction.replace(R.id.main, frag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.planner_fragment_layout, null);

        String[] columns = new String[] { "name" };
        int[] viewIds = new int[] { R.id.dayWorkoutTitle };

        listView = (ListView) root.findViewById(R.id.plannerList);
        
        listView.setAdapter(myPlannerAdapter);

        final CalendarView planner = (CalendarView) root.findViewById(R.id.planner);

        if(MainActivity.plannerSelectedWorkoutId!=-1)
        {
<<<<<<< HEAD
            String wo_id = ""+MainActivity.plannerSelectedWorkoutId;
            Cursor c = getActivity().getContentResolver().query(Contract.Workouts.CONTENT_URI, new String[]{Contract.Workouts._ID, Contract.Workouts.NAME, Contract.Workouts.ISPAID}, "(" + Contract.Workouts._ID + "=?)", new String[]{wo_id}, null);
            c.moveToFirst();
            Toast.makeText(getActivity(),"Plan this workout: " + c.getString(c.getColumnIndex(Contract.Workouts.NAME)) + " for date: " + MainActivity.plannerSelectedDate, Toast.LENGTH_SHORT).show();

            ContentValues cv = new ContentValues();
            cv.put(Contract.Planners.WORKOUT_ID, wo_id);
            cv.put(Contract.Planners.WORKOUT_ID, MainActivity.plannerSelectedDate);

            Uri uri = getActivity().getContentResolver().insert(Contract.Planners.CONTENT_URI, cv);
            Log.d("","Inserted " + uri.toString());

=======
            MainActivity.plannerWorkoutCursor.moveToPosition(MainActivity.plannerSelectedWorkoutId);
            Toast.makeText(getActivity(),"Plan this workouts: " + MainActivity.plannerWorkoutCursor.getString(1) + " for date: " + MainActivity.plannerSelectedDate, Toast.LENGTH_SHORT).show();
>>>>>>> FETCH_HEAD
        }
        final TextView plannerCurrentDate = (TextView) root.findViewById(R.id.plannerCurrentDate);

        setPlannerSelectedDate(planner);
        plannerCurrentDate.setText("Workout for " + MainActivity.plannerSelectedDate);

        planner.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {
                MainActivity.phoneCalendar.set(Calendar.YEAR, year);
                MainActivity.phoneCalendar.set(Calendar.MONTH, month);
                MainActivity.phoneCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setPlannerSelectedDate(planner);
                plannerCurrentDate.setText("Workout for " + MainActivity.plannerSelectedDate);
            }
        });

        getActivity().getActionBar().setTitle("Planner Workouts");

        return root;
    }

    private void setPlannerSelectedDate(CalendarView planner) {
        MainActivity.plannerSelectedDate = MainActivity.phoneCalendar.get(Calendar.DAY_OF_MONTH)+"/"+ (MainActivity.phoneCalendar.get(Calendar.MONTH)+1) +"/"+MainActivity.phoneCalendar.get(Calendar.YEAR);
        planner.setDate(MainActivity.phoneCalendar.getTimeInMillis(),true,true);
    }
}
