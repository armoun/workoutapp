package be.howest.nmct3.workoutapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import be.howest.nmct3.workoutapp.data.WorkoutsLoader;


public class AddNewWorkoutFragment extends android.support.v4.app.Fragment {

    private SimpleCursorAdapter myExercisesAdapter;

    public AddNewWorkoutFragment() {
        // Required empty public constructor
    }

    public static android.support.v4.app.Fragment newInstance(Context context) {
        AddNewWorkoutFragment frag = new AddNewWorkoutFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_add_new_workout, null);

        String[] columns = new String[] { "name" };
        int[] viewIds = new int[] { R.id.new_workout_list_exercises_item_text };

        WorkoutsLoader wl = new WorkoutsLoader(getActivity());
        final Cursor cursor = wl.loadInBackground();

        final ListView listView = (ListView) root.findViewById(R.id.lst_exercises_new_workout);
        myExercisesAdapter = new SimpleCursorAdapter(getActivity(),R.layout.new_workout_exercises_row_layout, cursor, columns, viewIds, 0);
        listView.setAdapter(myExercisesAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                listView.setItemChecked(position, true);
            }
        });

        return root;
    }


}