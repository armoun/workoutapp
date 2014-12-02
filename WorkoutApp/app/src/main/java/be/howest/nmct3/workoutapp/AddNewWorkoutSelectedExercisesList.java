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
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import be.howest.nmct3.workoutapp.data.Contract;
import be.howest.nmct3.workoutapp.data.ExercisesLoader;

/**
 * Created by timdujardin on 2/12/14.
 */
public class AddNewWorkoutSelectedExercisesList extends Fragment {

    private ListAdapter myListAdapter;

    public AddNewWorkoutSelectedExercisesList() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Context context) {
        AddNewWorkoutSelectedExercisesList frag = new AddNewWorkoutSelectedExercisesList();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //activity melden dat er een eigen menu moet worden geladen
        setHasOptionsMenu(true);

        Log.d("","NEW WORKOUT EXERCISES LIST");

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_add_new_workout_selected_exercises_list, null);

        Bundle args = getArguments();
        if (args  != null && args.containsKey("selected_exercise")) {
            String selectedExercise = args.getString("selected_exercise");
            //Toast.makeText(getActivity().getBaseContext(), "Exercise met id: '" + selectedExercise + "' wordt toegevoegd aan deze nieuwe workout ...", Toast.LENGTH_SHORT).show();

            MainActivity.selectedExercises.add(selectedExercise);
        }

        ListView listView = (ListView) root.findViewById(R.id.new_workout_selected_exercises_list);

        myListAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, MainActivity.selectedExercises);

        listView.setAdapter(myListAdapter);

        return root;
    }
}
