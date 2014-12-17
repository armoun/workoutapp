package be.howest.nmct3.workoutapp;

// MAG MISSCHIEN WEG

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class Workouts_AddExercise_Fragment extends Fragment {


    public Workouts_AddExercise_Fragment() {
        // Required empty public constructor
    }

    public static android.support.v4.app.Fragment newInstance(Context context) {
        Workouts_AddExercise_Fragment frag = new Workouts_AddExercise_Fragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.workouts_workoutselected_list_fragment_layout, null);



        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.workouts_selectedworkoutlist, menu);
    }

}
