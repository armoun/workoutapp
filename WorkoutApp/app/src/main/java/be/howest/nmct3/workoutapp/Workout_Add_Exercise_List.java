package be.howest.nmct3.workoutapp;

import android.content.ContentValues;
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

import be.howest.nmct3.workoutapp.data.Contract;
import be.howest.nmct3.workoutapp.data.ExercisesLoader;

/**
 * Created by nielslammens on 2/12/14.
 */
public class Workout_Add_Exercise_List extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private CursorAdapter mAdapter;
    private ListView list;

    private Cursor mCursor;

    public Workout_Add_Exercise_List() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Context context) {
        Workout_Add_Exercise_List frag = new Workout_Add_Exercise_List();
        return frag;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.workout_add_exercise, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //activity melden dat er een eigen menu moet worden geladen
        setHasOptionsMenu(true);

        Log.d("", "NEW WORKOUT EXERCISES LIST");

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.new_workout_exercises_list, null);

        list = (ListView) root.findViewById(R.id.new_workout_exercises_liste);

        String[] columns = new String[] { Contract.Exercises.EXERCISE_NAME };
        int[] viewIds = new int[] { R.id.new_workout_list_exercises_item_text};

        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.new_workout_exercise_item_layout, null,
                columns, viewIds, 0)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View row = super.getView(position, convertView, parent);

                Cursor filteredCursor = ((SimpleCursorAdapter)list.getAdapter()).getCursor();
                filteredCursor.moveToPosition(position);

                String type = filteredCursor.getString(filteredCursor.getColumnIndex(Contract.Exercises.MUSCLE_GROUP));


                //Toast.makeText(getActivity().getBaseContext(), "type: " + type, Toast.LENGTH_SHORT).show();

                if(type.equals("chest"))
                {
                    ImageView imgExercise = (ImageView) row.findViewById(R.id.selectedworkout_image_exercises);
                    imgExercise.setImageResource(R.drawable.chest_red);
                }

                if(type.equals("back"))
                {
                    ImageView imgExercise = (ImageView) row.findViewById(R.id.selectedworkout_image_exercises);
                    imgExercise.setImageResource(R.drawable.back1_red);
                }

                if(type.equals("legs"))
                {
                    ImageView imgExercise = (ImageView) row.findViewById(R.id.selectedworkout_image_exercises);
                    imgExercise.setImageResource(R.drawable.legs_red);
                }

                if(type.equals("arms"))
                {
                    ImageView imgExercise = (ImageView) row.findViewById(R.id.selectedworkout_image_exercises);
                    imgExercise.setImageResource(R.drawable.arms_red);
                }

                if(type.equals("abs"))
                {
                    ImageView imgExercise = (ImageView) row.findViewById(R.id.selectedworkout_image_exercises);
                    imgExercise.setImageResource(R.drawable.abs_red);
                }

                if(type.equals("shoulders"))
                {
                    ImageView imgExercise = (ImageView) row.findViewById(R.id.selectedworkout_image_exercises);
                    imgExercise.setImageResource(R.drawable.shoulders_red);
                }

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
                String exerciseName = ""+filteredCursor.getString(filteredCursor.getColumnIndex(Contract.Exercises.EXERCISE_NAME));

                MainActivity.EXERCICE_ID = filteredCursor.getInt(filteredCursor.getColumnIndex(Contract.Exercises._ID));

                Fragment newFragment = Fragment.instantiate(getActivity().getApplicationContext(), "be.howest.nmct3.workoutapp.Workouts_SelectedWorkoutList_Fragment");
                // consider using Java coding conventions (upper first char class names!!!)
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                MainActivity.activeFragment = newFragment;

                //geef de geselecteerde exercise door aan Workouts_SelectedWorkoutList_Fragment
                final Bundle bundle = new Bundle();
                bundle.putString("selected_exercise", exerciseName);
                newFragment.setArguments(bundle);

                int ex_id = filteredCursor.getInt(filteredCursor.getColumnIndex(Contract.Exercises._ID));
                int wo_id = MainActivity.WORKOUT_ID;

                ContentValues values = new ContentValues();
                values.put(Contract.WorkoutExercises.WORKOUT_ID, wo_id);
                values.put(Contract.WorkoutExercises.EXERCISE_ID, ex_id);
                values.put(Contract.WorkoutExercises.REPS, "12 12 12");
                getActivity().getContentResolver().insert(Contract.WorkoutExercises.CONTENT_URI, values);

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back1 stack
                transaction.replace(R.id.main, newFragment);
                //transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

            }
        });

        getActivity().getActionBar().setTitle("Add exercise to workouts");

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            //Geklikt op search bij add exercise to workout
            case R.id.action_search_workout_add_exercise:
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

    public void restartLoader(){
        getLoaderManager().restartLoader(0, null, this);
    }

    private Cursor getExercisesByExerciseName(String searchTerm) {
        String[] projection = new String[]{
                Contract.Exercises._ID,
                Contract.Exercises.EXERCISE_NAME,
                Contract.Exercises.DESCRIPTION,
                Contract.Exercises.MUSCLE_GROUP
        };

        Cursor c = getActivity().getContentResolver().query(
                Contract.Exercises.CONTENT_URI,
                projection,
                "(" + Contract.Exercises.EXERCISE_NAME + " like ?)",
                new String[]{"%" + searchTerm + "%"},
                Contract.ExerciseColumns.EXERCISE_NAME + " ASC");

        return c;
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
