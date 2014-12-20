package be.howest.nmct3.workoutapp;



import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.lang.Override;

import be.howest.nmct3.workoutapp.data.Contract;
import be.howest.nmct3.workoutapp.data.WorkoutsLoader;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class WorkoutsFragment extends Fragment {

    //public final String[] Workouts = {"Workout 1", "Workout 2", "Workout 3"};
    private CursorAdapter myWorkoutCursorAdapter;

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

        String[] columns = new String[]{"name"};
        int[] viewIds = new int[]{R.id.workout_item_title};

        WorkoutsLoader wl = new WorkoutsLoader(getActivity());
        final Cursor cursor = wl.loadInBackground();

        final ListView listView = (ListView) root.findViewById(R.id.workout_list);
        myWorkoutCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.workouts_list_workout_item_rowlayout, cursor, columns, viewIds, 0);
        listView.setAdapter(myWorkoutCursorAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                cursor.moveToPosition(position);
                String workoutId = cursor.getString(cursor.getColumnIndex(Contract.WorkoutColumns._ID));

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
        });

        getActivity().getActionBar().setTitle("Choose a workout");

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void OpenAddNewWorkoutFragment() {
        // Create and set the start fragment
        Fragment frag = Fragment.instantiate(getActivity(), "be.howest.nmct3.workoutapp.AddNewWorkoutFragment");
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        MainActivity.activeFragment = frag;

        transaction.replace(R.id.main, frag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}


