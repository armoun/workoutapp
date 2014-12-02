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
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import be.howest.nmct3.workoutapp.data.Contract;
import be.howest.nmct3.workoutapp.data.WorkoutsLoader;


public class AddNewWorkoutFragment extends android.support.v4.app.Fragment {


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

        //GO TO NEXT
        final Button logoutButton = (Button) root.findViewById(R.id.workoutNameIsChosenNextButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                goNextButton(v);
            }
        });


        return root;
    }

    public void goNextButton(View v)
    {

        //OPEN NEXT FRAGMENT
        android.support.v4.app.Fragment newFragment = android.support.v4.app.Fragment.instantiate(getActivity().getApplicationContext(), "be.howest.nmct3.workoutapp.NewWorkoutExercisesList");

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }


}