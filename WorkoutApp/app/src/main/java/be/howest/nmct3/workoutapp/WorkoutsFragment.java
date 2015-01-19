package be.howest.nmct3.workoutapp;



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.series.DataPoint;

import java.lang.Override;
import java.util.Calendar;
import java.util.Date;

import be.howest.nmct3.workoutapp.data.Contract;
import be.howest.nmct3.workoutapp.data.DatabaseHelper;
import be.howest.nmct3.workoutapp.data.SettingsAdmin;
import be.howest.nmct3.workoutapp.data.SpecificWorkoutLoader;
import be.howest.nmct3.workoutapp.data.WorkoutDatasoure;
import be.howest.nmct3.workoutapp.data.WorkoutsLoader;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class WorkoutsFragment extends Fragment {

    //public final String[] Workouts = {"Workout 1", "Workout 2", "Workout 3"};
    private CursorAdapter myWorkoutCursorAdapter;

    String Owner;

    public static ListView listView;
    public static int currentWorkoutPosition = -1;

    public WorkoutsFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Context context) {
        WorkoutsFragment frag = new WorkoutsFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //activity melden dat er een eigen menu moet worden geladen
        setHasOptionsMenu(true);

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.workouts_fragment_layout, null);

        MainActivity.workoutDatasource = new WorkoutDatasoure();

        String[] columns = new String[]{"name"};
        int[] viewIds = new int[]{R.id.workout_item_title};

        MainActivity.todaysWorkoutClicked = false;

        // VERKEERDE WERKWIJZE !
        WorkoutsLoader wl = new WorkoutsLoader(getActivity());
        final Cursor cursor = wl.loadInBackground();

        Log.d("WorkoutsFragment", DatabaseUtils.dumpCursorToString(cursor));

        listView = (ListView) root.findViewById(R.id.workout_list);
        myWorkoutCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.workouts_list_workout_item_rowlayout, cursor, columns, viewIds, 0);
        listView.setAdapter(myWorkoutCursorAdapter);

        //ENABLE SEARCH FILTERING
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                currentWorkoutPosition = position;
                openWorkoutExercises(position);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int pos, long id) {
                // TODO Auto-generated method stub

                Toast.makeText(getActivity().getBaseContext(), "Long Clicked on" + pos , Toast.LENGTH_SHORT).show();

                Cursor filteredCursor = ((SimpleCursorAdapter)listView.getAdapter()).getCursor();
                filteredCursor.moveToPosition(pos);
                String selectedFromList = ""+ filteredCursor.getString(filteredCursor.getColumnIndex(Contract.Workouts.NAME));
                int selectedId = filteredCursor.getInt(filteredCursor.getColumnIndex(Contract.Workouts._ID));
                //String selectedFromList = listView.getItemAtPosition(pos).toString();


                Owner = MainActivity.workoutDatasource.getOwnerOfWorkout(getActivity().getApplicationContext(), selectedId);

                if(Owner.equals("ALL"))
                {
                    AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity())
                            .setTitle("Error")
                            .setMessage("You can't edit or delete this workout.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //dialog.cancel();
                                }
                            });

                    //.show();

                    //The tricky part
                    Dialog d = builder.show();
                    int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
                    View divider = d.findViewById(dividerId);
                    divider.setBackgroundColor(getResources().getColor(R.color.headcolor));

                    int textViewId = d.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                    TextView tv = (TextView) d.findViewById(textViewId);
                    tv.setTextColor(getResources().getColor(R.color.headcolor));
                }
                else
                {
                    openDialogEditDelete(v, selectedFromList,selectedId);
                }


                return true;
            }
        });

        getActivity().getActionBar().setTitle("Choose a workout");

        return root;
    }

    public void openDialogEditDelete(View v, String SelectedText, final int selectedID){
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        final View promptView = layoutInflater.inflate(R.layout.input_dialog_edit_delete, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(promptView);

        //EDIT TEXT
        final EditText input_for_row = (EditText) promptView.findViewById(R.id.input_for_row);
        input_for_row.setText(SelectedText);

        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        MainActivity.workoutDatasource.updateWorkoutName(getActivity(),selectedID, input_for_row.getText().toString());
                        Toast.makeText(getActivity(), "Workout name changed to " + input_for_row.getText().toString() + " with ID: " + selectedID,Toast.LENGTH_LONG).show();
                        reOpenFragment();

                    }
                })
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
                deleteRowMethod(v, selectedID);
                alert.cancel();
            }
        });
    }

    public void deleteRowMethod(View v, int selectedItemID){
        MainActivity.workoutDatasource.deleteWorkout(getActivity(),selectedItemID);
        Toast.makeText(getActivity().getBaseContext(), "Row deleted with id: " + selectedItemID , Toast.LENGTH_SHORT).show();
        reOpenFragment();
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
        inflater.inflate(R.menu.workouts, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            //Geklikt op "+" bij workouts
            case R.id.action_add_workout:
                //Toast.makeText(this, item.getTitle()+" clicked!", Toast.LENGTH_SHORT).show();
                OpenAddNewWorkoutFragment();
                break;

            //Geklikt op search bij workouts
            case R.id.action_search_workouts:
                Toast.makeText(getActivity(), item.getTitle()+" clicked!", Toast.LENGTH_SHORT).show();
                SearchView searchView = (SearchView)item.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        Log.d("", "--------- QUERY: " + s);
                        myWorkoutCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                            @Override
                            public Cursor runQuery(CharSequence charSequence) {
                                Log.d("",""+charSequence);
                                myWorkoutCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                                    @Override
                                    public Cursor runQuery(CharSequence charSequence) {
                                        return getWorkoutsByWorkoutName(charSequence.toString());
                                    }
                                });
                                return null;
                            }
                        });
                        myWorkoutCursorAdapter.runQueryOnBackgroundThread(s);
                        myWorkoutCursorAdapter.getFilter().filter(s);
                        return false;
                    }
                });
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void OpenAddNewWorkoutFragment() {
        // Create and set the start fragment
        Fragment frag = Fragment.instantiate(getActivity(), "be.howest.nmct3.workoutapp.AddNewWorkoutFragment");
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        MainActivity.activeFragment = frag;
        Toast.makeText(getActivity(), "Active fragment: " + frag.getClass().getSimpleName(),Toast.LENGTH_SHORT).show();

        transaction.replace(R.id.main, frag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private void reOpenFragment() {
        // Create and set the start fragment
        Fragment frag = Fragment.instantiate(getActivity(), "be.howest.nmct3.workoutapp.WorkoutsFragment");
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        MainActivity.activeFragment = frag;
        Toast.makeText(getActivity(), "Active fragment: " + frag.getClass().getSimpleName(),Toast.LENGTH_SHORT).show();

        transaction.replace(R.id.main, frag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private Cursor getWorkoutsByWorkoutName(String searchTerm) {
        String[] projection = new String[]{
                Contract.Workouts._ID,
                Contract.Workouts.NAME,
                Contract.Workouts.ISPAID
        };

        Cursor c = getActivity().getContentResolver().query(
                Contract.Workouts.CONTENT_URI,
                projection,
                Contract.Workouts.NAME + " like ? AND " + Contract.Workouts.DELETE + " = 0",
                new String[]{"%" + searchTerm + "%"},
                Contract.WorkoutColumns.ISPAID + " ASC");

        return c;
    }

    public void openWorkoutExercises(int position) {
        Cursor filteredCursor = ((SimpleCursorAdapter)listView.getAdapter()).getCursor();
        filteredCursor.moveToPosition(position);

        String workoutId = filteredCursor.getString(filteredCursor.getColumnIndex(Contract.WorkoutColumns._ID));

        Toast.makeText(getActivity().getBaseContext(), "" + workoutId, Toast.LENGTH_SHORT).show();

        MainActivity.WORKOUT_ID = Integer.parseInt(workoutId);

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
}


