package be.howest.nmct3.workoutapp;



import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import be.howest.nmct3.workoutapp.data.Contract;
import be.howest.nmct3.workoutapp.data.DatabaseHelper;
import be.howest.nmct3.workoutapp.data.SettingsAdmin;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class PlannerFragment extends Fragment {

    private CursorAdapter myPlannerAdapter;
    private Cursor mCursor;
    private ListView listView;
    private int plannerId;

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
        menu.clear();
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

        String[] columns = new String[] { Contract.Workouts.NAME};
        int[] viewIds = new int[] { R.id.dayWorkoutTitle };

        //SET CURRENT DATE TO TODAY
        Date now = new Date();
        MainActivity.phoneCalendar.setTime(now);
        Log.d("", " -------- JAAR => " + MainActivity.phoneCalendar.get(Calendar.YEAR));
        Log.d("", " -------- MAAND => " + MainActivity.phoneCalendar.get(Calendar.MONTH));
        Log.d("", " -------- DAG => " + MainActivity.phoneCalendar.get(Calendar.DAY_OF_MONTH));

        listView = (ListView) root.findViewById(R.id.plannerList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCursor.moveToPosition(i);
                int id = mCursor.getInt(mCursor.getColumnIndex(Contract.Workouts._ID));
                Log.d("PlannerFragment",DatabaseUtils.dumpCursorToString(mCursor));
                Log.d("PlannerFragment","Workout ID: " + id);
                MainActivity.WORKOUT_ID = id;

                Fragment newFragment = Fragment.instantiate(getActivity().getApplicationContext(), "be.howest.nmct3.workoutapp.Workouts_SelectedWorkoutList_Fragment");
                // consider using Java coding conventions (upper first char class names!!!)
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                MainActivity.activeFragment = newFragment;

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back1 stack
                transaction.replace(R.id.main, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int pos, long id) {
                // TODO Auto-generated method stub

                mCursor.moveToPosition(pos);
                plannerId = mCursor.getInt(mCursor.getColumnIndex(Contract.WorkoutExercises._ID));

                openDialogEditDelete(v);

                return true;
            }
        });

        myPlannerAdapter = new SimpleCursorAdapter(getActivity(), R.layout.planner_list_row_layout, null, columns, viewIds, 0);

        listView.setAdapter(myPlannerAdapter);

        final CalendarView planner = (CalendarView) root.findViewById(R.id.planner);

        if(MainActivity.plannerSelectedWorkoutId!=-1)
        {
            String wo_id = ""+MainActivity.plannerSelectedWorkoutId;
            Cursor c = getActivity().getContentResolver().query(Contract.Workouts.CONTENT_URI, new String[]{Contract.Workouts._ID, Contract.Workouts.NAME, Contract.Workouts.ISPAID}, "(" + Contract.Workouts._ID + "=?)", new String[]{wo_id}, null);
            c.moveToFirst();
            //Toast.makeText(getActivity(),"Plan this workout: " + c.getString(c.getColumnIndex(Contract.Workouts.NAME)) + " for date: " + MainActivity.plannerSelectedDate, Toast.LENGTH_SHORT).show();

            ContentValues cv = new ContentValues();
            cv.put(Contract.Planners.WORKOUT_ID, wo_id);
            cv.put(Contract.Planners.WO_DATE, MainActivity.plannerSelectedDate);
            cv.put(Contract.Planners.USERNAME, SettingsAdmin.getInstance(getActivity().getApplicationContext()).getUsername());

            Uri uri = getActivity().getContentResolver().insert(Contract.Planners.CONTENT_URI, cv);
            Log.d("","Inserted " + uri.toString());
            MainActivity.syncPlannerUp();
            MainActivity.plannerSelectedWorkoutId = -1;
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

    private void getworkouts(){

        SQLiteDatabase db = DatabaseHelper.getInstance(getActivity().getApplicationContext()).getReadableDatabase();
        mCursor = db.rawQuery(  "SELECT "        + Contract.Workouts.CONTENT_DIRECTORY + "." + Contract.Workouts._ID + ", " + Contract.Workouts.CONTENT_DIRECTORY + "." + Contract.Workouts.NAME +
                                " FROM "         + Contract.Workouts.CONTENT_DIRECTORY +
                                " INNER JOIN "   + Contract.Planners.CONTENT_DIRECTORY +
                                " ON "           + Contract.Workouts.CONTENT_DIRECTORY + "." + Contract.Workouts._ID + " = " + Contract.Planners.CONTENT_DIRECTORY + "." + Contract.Planners.WORKOUT_ID +
                                " WHERE "  + "(" + Contract.Planners.CONTENT_DIRECTORY + "." + Contract.Planners.WO_DATE    + " = ?) AND "
                                           + "(" + Contract.Planners.CONTENT_DIRECTORY + "." + Contract.Planners.USERNAME   + " = ? OR " + Contract.Planners.CONTENT_DIRECTORY + "." + Contract.Planners.USERNAME + " = \"ALL\") AND "
                                           + "(" + Contract.Planners.CONTENT_DIRECTORY + "." + Contract.Planners.DELETE     + " = 0) AND "
                                           + "(" + Contract.Workouts.CONTENT_DIRECTORY + "." + Contract.Workouts.DELETE     + " = 0)",
                new String[]{MainActivity.plannerSelectedDate, SettingsAdmin.getInstance(getActivity().getApplicationContext()).getUsername()});

        Log.d("",DatabaseUtils.dumpCursorToString(mCursor));

        myPlannerAdapter.changeCursor(mCursor);
    }

    private void setPlannerSelectedDate(CalendarView planner) {
        MainActivity.plannerSelectedDate = MainActivity.phoneCalendar.get(Calendar.DAY_OF_MONTH)+"-"+ (MainActivity.phoneCalendar.get(Calendar.MONTH)+1) +"-"+MainActivity.phoneCalendar.get(Calendar.YEAR);
        planner.setDate(MainActivity.phoneCalendar.getTimeInMillis(),true,true);
        getworkouts();
    }

    public void openDialogEditDelete(View v)
    {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        final View promptView = layoutInflater.inflate(R.layout.input_dialog_delete, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(promptView);

        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        //DELETE BUTTON
        Button deleteRow;
        deleteRow = (Button) promptView.findViewById(R.id.delete_row_button_dialog_id);
        deleteRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                deleteRowMethod(v);
                alert.cancel();
            }
        });
    }

    public void deleteRowMethod(View v)
    {
        MainActivity.workoutDatasource.deleteWorkoutFromPlanner(getActivity().getApplicationContext(), plannerId);
        reOpenFragment();
        MainActivity.WORKOUT_ID = 0;
    }

    private void reOpenFragment() {
/*        Cursor filteredCursor = ((SimpleCursorAdapter)WorkoutsFragment.listView.getAdapter()).getCursor();
        filteredCursor.moveToPosition(WorkoutsFragment.currentWorkoutPosition);

        String workoutId = filteredCursor.getString(filteredCursor.getColumnIndex(Contract.WorkoutColumns._ID));

        Toast.makeText(getActivity().getBaseContext(), "" + workoutId, Toast.LENGTH_SHORT).show();

        MainActivity.WORKOUT_ID = Integer.parseInt(workoutId);*/

        Fragment newFragment = Fragment.instantiate(getActivity().getApplicationContext(), "be.howest.nmct3.workoutapp.PlannerFragment");
        // consider using Java coding conventions (upper first char class names!!!)
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        MainActivity.activeFragment = newFragment;
        //Toast.makeText(getActivity(), "Active fragment: " + newFragment.getClass().getSimpleName(),Toast.LENGTH_SHORT).show();
        Log.d("", "Active fragment: " + newFragment.getClass().getSimpleName());

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back1 stack
        transaction.replace(R.id.main, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
