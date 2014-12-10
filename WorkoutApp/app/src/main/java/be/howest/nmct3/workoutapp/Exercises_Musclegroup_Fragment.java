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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import be.howest.nmct3.workoutapp.data.Contract;
import be.howest.nmct3.workoutapp.data.ExercisesLoader;
import be.howest.nmct3.workoutapp.json.ExercisesLoaderJson;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class Exercises_Musclegroup_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    String mMuscleGroup = "";
    private CursorAdapter mAdapter;
    private ListView list;

    private Cursor mCursor;

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

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                mCursor.moveToPosition(position);
                String exerciseId = ""+ mCursor.getInt(mCursor.getColumnIndex(Contract.Exercises._ID));
                Toast.makeText(getActivity().getBaseContext(), "" + exerciseId, Toast.LENGTH_SHORT).show();

                MainActivity.EXERCICE_ID = mCursor.getInt(mCursor.getColumnIndex(Contract.Exercises._ID));

                Fragment newFragment = Fragment.instantiate(getActivity().getApplicationContext(), "be.howest.nmct3.workoutapp.Exercises_Detail_Fragment");
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

        getActivity().getActionBar().setTitle("Choose an exercise");

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
        //return new ExercisesLoaderJson(getActivity(), mMuscleGroup, null);
        return  new ExercisesLoader(getActivity(), mMuscleGroup);
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
}
