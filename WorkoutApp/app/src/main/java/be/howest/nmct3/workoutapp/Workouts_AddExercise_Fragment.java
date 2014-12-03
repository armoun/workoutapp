package be.howest.nmct3.workoutapp;

// MAG MISSCHIEN WEG

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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


}
