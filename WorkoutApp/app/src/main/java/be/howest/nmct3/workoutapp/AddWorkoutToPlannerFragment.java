package be.howest.nmct3.workoutapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import be.howest.nmct3.workoutapp.data.Contract;
import be.howest.nmct3.workoutapp.data.ExercisesLoader;
import be.howest.nmct3.workoutapp.data.WorkoutsLoader;


public class AddWorkoutToPlannerFragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter myWorkoutsAdapter;

    private CursorAdapter mAdapter;
    private Cursor mCursor;
    public static String actionBarTitle = "Add workout to planner";

    public AddWorkoutToPlannerFragment() {
        // Required empty public constructor
    }

    public static android.support.v4.app.Fragment newInstance(Context context) {
        AddWorkoutToPlannerFragment frag = new AddWorkoutToPlannerFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //activity melden dat er een eigen menu moet worden geladen
        setHasOptionsMenu(true);

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_add_workout_to_planner, null);

        String[] columns = new String[] { "name" };
        int[] viewIds = new int[] { R.id.planner_add_workout_item_title };

        final ListView listView = (ListView) root.findViewById(R.id.lst_workouts_add_planner);
        mAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.fragment_add_workout_to_planner_item_layout,
                null,
                columns,
                viewIds,
                0
        );

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor filteredCursor = ((SimpleCursorAdapter)listView.getAdapter()).getCursor();
                filteredCursor.moveToPosition(position);

                MainActivity.plannerSelectedWorkoutId = filteredCursor.getInt(filteredCursor.getColumnIndex(Contract.Workouts._ID));

                android.support.v4.app.Fragment frag = android.support.v4.app.Fragment.instantiate(getActivity(), MainActivity.fragments[3]);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main, frag).commit();

                MainActivity.activeFragment = frag;
            }
        });

        getActivity().getActionBar().setTitle("Add workouts to planner");

        return root;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.workouts_planner, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //Geklikt op search bij workouts
            case R.id.action_search_workouts:
                Toast.makeText(getActivity(), item.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();
                SearchView searchView = (SearchView)item.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        Log.d("", "--------- QUERY: " + s);
                        mAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                            @Override
                            public Cursor runQuery(CharSequence charSequence) {
                                Log.d("",""+charSequence);
                                mAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                                    @Override
                                    public Cursor runQuery(CharSequence charSequence) {
                                        return getWorkoutsByWorkoutName(charSequence.toString());
                                    }
                                });
                                return null;
                            }
                        });
                        mAdapter.runQueryOnBackgroundThread(s);
                        mAdapter.getFilter().filter(s);
                        return false;
                    }
                });
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
        //return new ExercisesLoaderJson(getActivity(), mMuscleGroup, null);
        return  new WorkoutsLoader(getActivity());
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

    private Cursor getWorkoutsByWorkoutName(String searchTerm) {
        String[] projection = new String[]{
                Contract.Workouts._ID,
                Contract.Workouts.NAME,
                Contract.Workouts.ISPAID
        };

        Cursor c = getActivity().getContentResolver().query(
                Contract.Workouts.CONTENT_URI,
                projection,
                "(" + Contract.Workouts.NAME + " like ?)",
                new String[]{"%" + searchTerm + "%"},
                Contract.WorkoutColumns.ISPAID + " ASC");

        return c;
    }
}