package be.howest.nmct3.workoutapp;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class ExercisesFragment extends Fragment {


    public ExercisesFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Context context) {
        ExercisesFragment frag = new ExercisesFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.exercises_fragment_layout, null);
        return root;
    }


}
