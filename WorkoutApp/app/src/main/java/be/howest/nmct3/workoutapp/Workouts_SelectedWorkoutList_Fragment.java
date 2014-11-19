package be.howest.nmct3.workoutapp;



import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

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
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.workouts_workoutselected_list_fragment_layout, null);

        list = (ListView) root.findViewById(R.id.workoutselected_list);

        mWorkoutId = MainActivity.WORKOUT_ID;

        String[] columns = new String[] {Contract.Exercises.EXERCISE_NAME, Contract.WorkoutExercises.REPS };
        int[] viewIds = new int[] { R.id.workoutselected_item_title, R.id.workoutselected_item_reps };

        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.workouts_workoutselected_list_item_layout, null,
                columns, viewIds, 0);

        list.setAdapter(mAdapter);

        return root;
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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }
}
