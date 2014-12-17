package be.howest.nmct3.workoutapp;



import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

import be.howest.nmct3.workoutapp.data.Contract;
import be.howest.nmct3.workoutapp.data.ExercisesLoader;
import be.howest.nmct3.workoutapp.data.SpecificWorkoutLoader;
import be.howest.nmct3.workoutapp.json.ExercisesLoaderJson;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class Workouts_SelectedWorkoutList_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    int mWorkoutId = 0;
    private CursorAdapter mAdapter;
    private Cursor mCursor;
    private ListView list;

    public Workouts_SelectedWorkoutList_Fragment() {
        // Required empty public constructor
    }

    public static android.support.v4.app.Fragment newInstance(Context context) {
        Workouts_SelectedWorkoutList_Fragment frag = new Workouts_SelectedWorkoutList_Fragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //activity melden dat er een eigen menu moet worden geladen
        setHasOptionsMenu(true);

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.workouts_workoutselected_list_fragment_layout, null);



        Bundle args = getArguments();
        if (args  != null && args.containsKey("selected_exercise")) {
            String selectedExercise = args.getString("selected_exercise");
            Toast.makeText(getActivity().getBaseContext(), "Selected exercise: " + selectedExercise, Toast.LENGTH_SHORT).show();
        }

        list = (ListView) root.findViewById(R.id.workoutselected_list);

        mWorkoutId = MainActivity.WORKOUT_ID;

        String[] columns = new String[] {Contract.Exercises.EXERCISE_NAME, Contract.WorkoutExercises.REPS };
        int[] viewIds = new int[] { R.id.workoutselected_item_title, R.id.workoutselected_item_reps };

        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.workouts_workoutselected_list_item_layout, null,
                columns, viewIds, 0);

        list.setAdapter(mAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                mCursor.moveToPosition(position);
                String exerciseId   = "" + mCursor.getInt(0);
                String workoutId    = "" + mCursor.getInt(3);
                Toast.makeText(getActivity().getBaseContext(), "ex_id " + exerciseId + " wo_id " + workoutId, Toast.LENGTH_SHORT).show();

                MainActivity.EXERCICE_ID = mCursor.getInt(0);
                MainActivity.WORKOUT_ID = mCursor.getInt(3);

                Fragment newFragment = Fragment.instantiate(getActivity().getApplicationContext(), "be.howest.nmct3.workoutapp.RepList");
                // consider using Java coding conventions (upper first char class names!!!)
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                MainActivity.activeFragment = newFragment;

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.main, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

            }
        });

        getActivity().getActionBar().setTitle("Choose an exercise");

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
        inflater.inflate(R.menu.workouts_selectedworkoutlist, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            //Geklikt op een workout onder workouts
            case R.id.action_add_exercise_to_exercises_selected_workout:
                //Toast.makeText(this, item.getTitle()+" clicked!", Toast.LENGTH_SHORT).show();
                OpenNewWorkoutExercisesList();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new SpecificWorkoutLoader(getActivity(), ""+ mWorkoutId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
        mCursor = cursor;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    private void OpenNewWorkoutExercisesList() {
        // Create and set the start fragment
        Fragment frag = Fragment.instantiate(getActivity(), "be.howest.nmct3.workoutapp.Workout_Add_Exercise_List");
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        MainActivity.activeFragment = frag;

        transaction.replace(R.id.main, frag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
