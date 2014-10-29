package be.howest.nmct3.workoutapp;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class Exercises_Musclegroup_Fragment extends Fragment {


    public Exercises_Musclegroup_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.exercises_musclegroup_fragment_layout, null);

        return root;
    }


}
