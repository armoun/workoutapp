package be.howest.nmct3.workoutapp;



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import be.howest.nmct3.workoutapp.data.Contract;
import be.howest.nmct3.workoutapp.data.DatabaseHelper;
import be.howest.nmct3.workoutapp.data.ExercisesLoader;
import be.howest.nmct3.workoutapp.data.SpecificWorkoutLoader;
import be.howest.nmct3.workoutapp.data.WorkoutDatasoure;
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

    public static boolean backPressedRepList = false;

    int selectedWorkoutId;
    int selectedExerciseId;
    int workoutExercisesId;

    String Owner;

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

        Toast.makeText(getActivity(), "Active fragment: " + this.getClass().getSimpleName(),Toast.LENGTH_SHORT).show();

        Bundle args = getArguments();
        if (args  != null && args.containsKey("selected_exercise")) {
            String selectedExercise = args.getString("selected_exercise");
            Toast.makeText(getActivity().getBaseContext(), "Selected exercise: " + selectedExercise, Toast.LENGTH_SHORT).show();
        }

        if(backPressedRepList) {
            Toast.makeText(getActivity(), "RELOAD data",Toast.LENGTH_SHORT).show();
            reOpenFragment();
            backPressedRepList=false;
        }

        list = (ListView) root.findViewById(R.id.workoutselected_list);

        mWorkoutId = MainActivity.WORKOUT_ID;
        selectedWorkoutId = mWorkoutId;

        String[] columns = new String[] {Contract.Exercises.EXERCISE_NAME, Contract.WorkoutExercises.REPS };
        int[] viewIds = new int[] { R.id.workoutselected_item_title, R.id.workoutselected_item_reps };

        Owner = MainActivity.workoutDatasource.getOwnerOfWorkout(getActivity().getApplicationContext(), mWorkoutId);
        Toast.makeText(getActivity().getBaseContext(), "OWNER: " + Owner, Toast.LENGTH_SHORT).show();


        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.workouts_workoutselected_list_item_layout, null,
                columns, viewIds, 0)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View row = super.getView(position, convertView, parent);
                Cursor filteredCursor = ((SimpleCursorAdapter)list.getAdapter()).getCursor();
                filteredCursor.moveToPosition(position);

                filteredCursor.moveToPosition(position);



                String type = filteredCursor.getString(filteredCursor.getColumnIndex(Contract.Exercises.MUSCLE_GROUP));

                Toast.makeText(getActivity().getBaseContext(), "type: " + type, Toast.LENGTH_SHORT).show();

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

        //ENABLE SEARCH FILTERING
        list.setTextFilterEnabled(true);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Cursor filteredCursor = ((SimpleCursorAdapter)list.getAdapter()).getCursor();
                filteredCursor.moveToPosition(position);

                //Log.d("workoutApp.Workouts_selected...", DatabaseUtils.dumpCursorToString(filteredCursor));
                Log.d("workoutApp.Workouts_selected...", "WorkoutExercises ID on position " + position + " is " + filteredCursor.getInt(filteredCursor.getColumnIndex(Contract.WorkoutExercises._ID)));

                //String exerciseId   = "" + filteredCursor.getInt(mCursor.getColumnIndex(Contract.WorkoutExercises.EXERCISE_ID));
                //String workoutId    = "" + filteredCursor.getInt(mCursor.getColumnIndex(Contract.WorkoutExercises.WORKOUT_ID));
                //Toast.makeText(getActivity().getBaseContext(), "ex_id " + exerciseId + " wo_id " + workoutId, Toast.LENGTH_SHORT).show();

                //MainActivity.EXERCICE_ID = filteredCursor.getInt(mCursor.getColumnIndex(Contract.WorkoutExercises.EXERCISE_ID));
                //MainActivity.WORKOUT_ID = filteredCursor.getInt(filteredCursor.getColumnIndex(Contract.WorkoutExercises.WORKOUT_ID));
                MainActivity.WORKOUT_EXERCICE_ID = filteredCursor.getInt(filteredCursor.getColumnIndex(Contract.WorkoutExercises._ID));

                Fragment newFragment = Fragment.instantiate(getActivity().getApplicationContext(), "be.howest.nmct3.workoutapp.RepList");
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

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int pos, long id) {
                // TODO Auto-generated method stub

                Toast.makeText(getActivity().getBaseContext(), "Long Clicked on" + pos , Toast.LENGTH_SHORT).show();

                Cursor filteredCursor = ((SimpleCursorAdapter)list.getAdapter()).getCursor();
                filteredCursor.moveToPosition(pos);

                selectedExerciseId = filteredCursor.getInt(filteredCursor.getColumnIndex(Contract.WorkoutExercises.EXERCISE_ID));
                selectedWorkoutId = filteredCursor.getInt(filteredCursor.getColumnIndex(Contract.WorkoutExercises.WORKOUT_ID));
                workoutExercisesId = filteredCursor.getInt(filteredCursor.getColumnIndex(Contract.WorkoutExercises._ID));


                if(Owner.equals("ALL"))
                {
                    AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity())
                            .setTitle("Error")
                            .setMessage("You can't delete exercises from this workout.")
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
                else {
                    //String selectedFromList = listView.getItemAtPosition(pos).toString();
                    openDialogEditDelete(v);
                }

                return true;
            }
        });

        getActivity().getActionBar().setTitle("Choose an exercise");

        return root;
    }


    public void openDialogEditDelete(View v)
    {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        final View promptView = layoutInflater.inflate(R.layout.input_dialog_delete, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(promptView);

        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
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
                deleteRowMethod(v);
                alert.cancel();
            }
        });
    }

    public void deleteRowMethod(View v)
    {
        MainActivity.workoutDatasource.deleteExerciseForWorkout(getActivity(),workoutExercisesId);
        Toast.makeText(getActivity().getBaseContext(), "Row deleted with Workout ID: " + selectedWorkoutId + " and Exercise ID: " + selectedExerciseId , Toast.LENGTH_SHORT).show();
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

        if(Owner.equals("ALL"))
        {
            inflater.inflate(R.menu.workouts_selectedworkoutlist_all, menu);
        }
        else
        {
            inflater.inflate(R.menu.workouts_selectedworkoutlist, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            //Geklikt op een workout onder workouts
            case R.id.action_add_exercise_to_exercises_selected_workout:
                //Toast.makeText(this, item.getTitle()+" clicked!", Toast.LENGTH_SHORT).show();
                OpenNewWorkoutExercisesList();
                break;

            //Geklikt op search bij workouts
            case R.id.action_search_exercises_selected_workout:
                Toast.makeText(getActivity(), item.getTitle()+" clicked!", Toast.LENGTH_SHORT).show();
                SearchView searchView = (SearchView)item.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        if(s.equals("")) {
                            restartLoader();
                        } else {
                            Log.d("", "--------- QUERY: " + s);
                            mAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                                @Override
                                public Cursor runQuery(CharSequence charSequence) {
                                    Log.d("", "" + charSequence);
                                    mAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                                        @Override
                                        public Cursor runQuery(CharSequence charSequence) {
                                            return getExercisesOfWorkoutByExerciseName(charSequence.toString());
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
        Toast.makeText(getActivity(), "Active fragment: " + frag.getClass().getSimpleName(),Toast.LENGTH_SHORT).show();

        transaction.replace(R.id.main, frag);
        transaction.disallowAddToBackStack();

        // Commit the transaction
        transaction.commit();
    }

    private void reOpenFragment() {
        if(WorkoutsFragment.listView!=null) {
            Cursor filteredCursor = ((SimpleCursorAdapter)WorkoutsFragment.listView.getAdapter()).getCursor();

            Log.d("Current Workout Position: ", "------- "+MainActivity.currentWorkoutPosition);

            filteredCursor.moveToPosition(MainActivity.currentWorkoutPosition);

            String workoutId = filteredCursor.getString(filteredCursor.getColumnIndex(Contract.WorkoutColumns._ID));

            Toast.makeText(getActivity().getBaseContext(), "" + workoutId, Toast.LENGTH_SHORT).show();

            MainActivity.WORKOUT_ID = Integer.parseInt(workoutId);

            Fragment newFragment = Fragment.instantiate(getActivity().getApplicationContext(), "be.howest.nmct3.workoutapp.Workouts_SelectedWorkoutList_Fragment");
            // consider using Java coding conventions (upper first char class names!!!)
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            MainActivity.activeFragment = newFragment;
            Toast.makeText(getActivity(), "Active fragment: " + newFragment.getClass().getSimpleName(),Toast.LENGTH_SHORT).show();
            Log.d("", "Active fragment: " + newFragment.getClass().getSimpleName());

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back1 stack
            transaction.replace(R.id.main, newFragment);

            // Commit the transaction
            transaction.commit();
        }
    }

    private Cursor getExercisesOfWorkoutByExerciseName(String searchTerm) {
        DatabaseHelper helper = DatabaseHelper.getInstance(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();

        String selectedWorkoutIdString = "" + selectedWorkoutId;

        Log.d("","SELECT * " +
                " FROM " + Contract.Exercises.CONTENT_DIRECTORY +
                " INNER JOIN " + Contract.WorkoutExercises.CONTENT_DIRECTORY +
                " ON " + Contract.Exercises.CONTENT_DIRECTORY + "." + Contract.ExerciseColumns._ID
                + " = " + Contract.WorkoutExercises.CONTENT_DIRECTORY + "." + Contract.WorkoutExerciseColumns.EXERCISE_ID +
                " WHERE (" + Contract.Exercises.CONTENT_DIRECTORY + "." + Contract.ExerciseColumns.EXERCISE_NAME + " like " + "%" + searchTerm + "%" + ")"
                + " AND " + Contract.WorkoutExercises.CONTENT_DIRECTORY + "." + Contract.WorkoutExerciseColumns.WORKOUT_ID + " = " + selectedWorkoutId);

        Cursor mData = db.rawQuery("SELECT * " +
                        " FROM " + Contract.Exercises.CONTENT_DIRECTORY +
                        " INNER JOIN " + Contract.WorkoutExercises.CONTENT_DIRECTORY +
                        " ON " + Contract.Exercises.CONTENT_DIRECTORY + "." + Contract.ExerciseColumns._ID
                        + " = " + Contract.WorkoutExercises.CONTENT_DIRECTORY + "." + Contract.WorkoutExerciseColumns.EXERCISE_ID +
                        " WHERE (" + Contract.Exercises.CONTENT_DIRECTORY + "." + Contract.ExerciseColumns.EXERCISE_NAME + " like ? )"
                        + " AND (" + Contract.WorkoutExercises.CONTENT_DIRECTORY + "." + Contract.WorkoutExerciseColumns.WORKOUT_ID + " = ?) "
                        + " AND (" + Contract.WorkoutExercises.CONTENT_DIRECTORY + "." + Contract.WorkoutExerciseColumns.DELETE + " = ?)",
                new String[]{"%" + searchTerm + "%", selectedWorkoutIdString, "0"});

        return mData;
    }

    public void restartLoader(){
        getLoaderManager().restartLoader(0, null, this);
    }
}
