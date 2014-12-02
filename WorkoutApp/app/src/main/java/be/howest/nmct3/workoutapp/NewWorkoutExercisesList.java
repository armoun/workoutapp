package be.howest.nmct3.workoutapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import be.howest.nmct3.workoutapp.data.Contract;
import be.howest.nmct3.workoutapp.data.ExercisesLoader;

/**
 * Created by nielslammens on 2/12/14.
 */
public class NewWorkoutExercisesList extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private CursorAdapter mAdapter;
    private ListView list;

    private Cursor mCursor;

    public NewWorkoutExercisesList() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Context context) {
        NewWorkoutExercisesList frag = new NewWorkoutExercisesList();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("","NEW WORKOUT EXERCISES LIST");

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.new_workout_exercises_list, null);

        list = (ListView) root.findViewById(R.id.new_workout_exercises_liste);

        String[] columns = new String[] { Contract.Exercises.EXERCISE_NAME };
        int[] viewIds = new int[] { R.id.new_workout_exercise_textview};

        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.new_workout_exercise_item_layout, null,
                columns, viewIds, 0);

        list.setAdapter(mAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                mCursor.moveToPosition(position);
                String exerciseId = ""+ mCursor.getInt(mCursor.getColumnIndex(Contract.Exercises._ID));
                Toast.makeText(getActivity().getBaseContext(), "" + exerciseId, Toast.LENGTH_SHORT).show();

                MainActivity.EXERCICE_ID = mCursor.getInt(mCursor.getColumnIndex(Contract.Exercises._ID));

                Fragment newFragment = Fragment.instantiate(getActivity().getApplicationContext(), "be.howest.nmct3.workoutapp.FRAGMENTNAAM");
                // consider using Java coding conventions (upper first char class names!!!)
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.main, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("","NEW WORKOUT EXERCISES LIST _ INITLOADER");
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.d("","NEW WORKOUT EXERCISES LIST _ ONCREATELOADER");
        return  new ExercisesLoader(getActivity(), "");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.d("","NEW WORKOUT EXERCISES LIST _ ONLOADFINISHED");
        mAdapter.swapCursor(cursor);
        mCursor = cursor;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        Log.d("","NEW WORKOUT EXERCISES LIST _ ONLOADERRESET");
        mAdapter.swapCursor(null);
    }
}
