package be.howest.nmct3.workoutapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import be.howest.nmct3.workoutapp.data.Contract;
import be.howest.nmct3.workoutapp.data.SettingsAdmin;
import be.howest.nmct3.workoutapp.data.WorkoutsLoader;


public class AddNewWorkoutFragment extends android.support.v4.app.Fragment {

    EditText nameEditText;

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

        nameEditText = (EditText) root.findViewById(R.id.name_new_workout_editText);

        //GO TO NEXT
        final Button logoutButton = (Button) root.findViewById(R.id.workoutNameIsChosenNextButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                insertWorkoutName();
                goNextButton(v);
            }
        });

        getActivity().getActionBar().setTitle("Add workouts");

        return root;
    }

    private void insertWorkoutName(){

        String name = nameEditText.getText().toString();

        ContentValues values = new ContentValues();
        values.put(Contract.Workouts.NAME, name);
        values.put(Contract.Workouts.ISPAID, 0);
        values.put(Contract.Workouts.USERNAME, SettingsAdmin.getInstance(getActivity().getApplicationContext()).getUsername());
        getActivity().getContentResolver().insert(Contract.Workouts.CONTENT_URI, values);
    }

    public void goNextButton(View v)
    {
        //OPEN NEXT FRAGMENT
        android.support.v4.app.Fragment newFragment = android.support.v4.app.Fragment.instantiate(getActivity().getApplicationContext(), "be.howest.nmct3.workoutapp.WorkoutsFragment");

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main, newFragment);
        //transaction.addToBackStack(null);

        MainActivity.activeFragment = newFragment;
        //Toast.makeText(getActivity().getBaseContext(), newFragment.getClass().getSimpleName(),Toast.LENGTH_SHORT).show();

        // Commit the transaction
        transaction.commit();
    }

}