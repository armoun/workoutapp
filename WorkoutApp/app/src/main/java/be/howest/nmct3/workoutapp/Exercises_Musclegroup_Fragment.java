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

import be.howest.nmct3.workoutapp.json.ExercisesLoader;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class Exercises_Musclegroup_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    String mMuscleGroup = "";
    private CursorAdapter mAdapter;
    private ListView list;

    public Exercises_Musclegroup_Fragment() {
        // Required empty public constructor
    }

    public static android.support.v4.app.Fragment newInstance(Context context) {
        Exercises_Musclegroup_Fragment frag = new Exercises_Musclegroup_Fragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.exercises_musclegroup_list_fragment_layout, null);

        list = (ListView) root.findViewById(R.id.exercises_musclegroup_list);

        mMuscleGroup = MainActivity.EXTRA_SELECTED_MUSCLEGROUP;

        String[] columns = new String[] { "name" };
        int[] viewIds = new int[] { R.id.list_musclegroup_item_text};

        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.exercises_musclegroup_item_layout, null,
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
        return new ExercisesLoader(getActivity(), mMuscleGroup, null);
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
