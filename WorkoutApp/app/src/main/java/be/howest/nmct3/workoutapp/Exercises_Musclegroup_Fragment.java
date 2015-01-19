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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
                columns, viewIds, 0)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final View row = super.getView(position, convertView, parent);

                String musclegroup = MainActivity.EXTRA_SELECTED_MUSCLEGROUP;
                ImageView imgMusclegroupIcon = (ImageView) row.findViewById(R.id.musclegroup_list_icon);

                Log.d("MUSCLEGROUP", musclegroup);

                if(musclegroup.equals("ARMS"))
                {
                    imgMusclegroupIcon.setImageResource(R.drawable.arms_red);
                }

                if(musclegroup.equals("BACK"))
                {
                    imgMusclegroupIcon.setImageResource(R.drawable.back1_red);
                }

                if(musclegroup.equals("CHEST"))
                {
                    imgMusclegroupIcon.setImageResource(R.drawable.chest_red);
                }

                if(musclegroup.equals("SHOULDERS"))
                {
                    imgMusclegroupIcon.setImageResource(R.drawable.shoulders_red);
                }

                if(musclegroup.equals("ABS"))
                {
                    imgMusclegroupIcon.setImageResource(R.drawable.abs_red);
                }

                if(musclegroup.equals("LEGS"))
                {
                    imgMusclegroupIcon.setImageResource(R.drawable.legs_red);
                }

                Cursor filteredCursor = ((SimpleCursorAdapter)list.getAdapter()).getCursor();
                filteredCursor.moveToPosition(position);
                String Target = ""+ filteredCursor.getString(filteredCursor.getColumnIndex(Contract.Exercises.TARGET));

                TextView target = (TextView) row.findViewById(R.id.exercises_target);

                target.setText(Target);

                return row;
            }
        };

        list.setAdapter(mAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Cursor filteredCursor = ((SimpleCursorAdapter)list.getAdapter()).getCursor();
                filteredCursor.moveToPosition(position);
                String exerciseId = ""+ filteredCursor.getInt(filteredCursor.getColumnIndex(Contract.Exercises._ID));
                //Toast.makeText(getActivity().getBaseContext(), "" + exerciseId, Toast.LENGTH_SHORT).show();

                MainActivity.EXERCICE_ID = filteredCursor.getInt(filteredCursor.getColumnIndex(Contract.Exercises._ID));

                Fragment newFragment = Fragment.instantiate(getActivity().getApplicationContext(), "be.howest.nmct3.workoutapp.Exercises_Detail_Fragment");
                // consider using Java coding conventions (upper first char class names!!!)
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back1 stack
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
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.exercises_musclegroup, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            //Geklikt op search
            case R.id.action_search_exercises_musclegroup:
                //Toast.makeText(getActivity(), item.getTitle()+" clicked!", Toast.LENGTH_SHORT).show();
                SearchView searchView = (SearchView)item.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        if(s.equals("")){
                            restartLoader();
                        }else{
                            Log.d("", "--------- QUERY: " + s);
                            mAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                                @Override
                                public Cursor runQuery(CharSequence charSequence) {
                                    Log.d("",""+charSequence);

                                    mAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                                        @Override
                                        public Cursor runQuery(CharSequence charSequence) {
                                            return getExercisesByExerciseName(charSequence.toString());
                                        }
                                    });
                                    return null;
                                }
                            });
                            mAdapter.runQueryOnBackgroundThread(s);
                            mAdapter.getFilter().filter(s);
                        }
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

    public void restartLoader(){
        getLoaderManager().restartLoader(0, null, this);
    }

    private Cursor getExercisesByExerciseName(String searchTerm) {
        String[] projection = new String[]{
                Contract.Exercises._ID,
                Contract.Exercises.EXERCISE_NAME,
                Contract.Exercises.TARGET,
                Contract.Exercises.MUSCLE_GROUP
        };

        Cursor c;
        String selection = "(" + Contract.Exercises.EXERCISE_NAME + " like ?) and " + Contract.Exercises.MUSCLE_GROUP + "=?";
        String whereArgs[] = new String[]{"%" + searchTerm + "%", mMuscleGroup.toLowerCase()};

        c = getActivity().getContentResolver().query(
                Contract.Exercises.CONTENT_URI,
                projection,
                selection,
                whereArgs,
                Contract.ExerciseColumns._ID + " ASC");

        return c;
    }
}
