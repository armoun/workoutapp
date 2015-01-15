package be.howest.nmct3.workoutapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import be.howest.nmct3.workoutapp.data.Contract;
import be.howest.nmct3.workoutapp.data.SettingsAdmin;

/**
 * Created by nielslammens on 2/12/14.
 */
public class RepList extends Fragment {

    private ListAdapter myListAdapter;
    ListView list;

    public RepList() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Context context) {
        RepList frag = new RepList();
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.replist, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            //Geklikt op "+" bij sets en reps (replist)
            case R.id.action_add_sets:
                //Toast.makeText(getActivity(), item.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();
                getNewSet();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getNewSet()
    {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.input_dialog_rep, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(promptView);

        final EditText input_reps = (EditText) promptView.findViewById(R.id.input_weight);
        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("Replist","_"+input_reps.getText().toString()+ "_");
                        if(input_reps.getText().toString().length() > 0){
                            Log.d("Replist","input reps not empty");
                            String ex_id = ""+ MainActivity.EXERCICE_ID;
                            String wo_id = ""+ MainActivity.WORKOUT_ID;
                            Cursor c = getActivity().getContentResolver().query(Contract.WorkoutExercises.CONTENT_URI, new String[]{Contract.WorkoutExercises.REPS},
                                    "(" + Contract.WorkoutExercises.EXERCISE_ID + " =? AND " + Contract.WorkoutExercises.WORKOUT_ID + "=?)",
                                    new String[]{ex_id, wo_id},
                                    null);
                            c.moveToFirst();
                            String reps = c.getString(c.getColumnIndex(Contract.WorkoutExercises.REPS));
                            reps += " " + input_reps.getText().toString();

                            ContentValues cv = new ContentValues();
                            cv.put(Contract.WorkoutExercises.REPS, reps);

                            int a = getActivity().getContentResolver().update(Contract.WorkoutExercises.CONTENT_URI, cv,
                                    "(" + Contract.WorkoutExercises.EXERCISE_ID + " =? AND " + Contract.WorkoutExercises.WORKOUT_ID + "=?)",
                                    new String[]{ex_id, wo_id});

                            myListAdapter = new repsAdapter();
                            list.setAdapter(myListAdapter);
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //activity melden dat er een eigen menu moet worden geladen
        setHasOptionsMenu(true);

        Log.d("RepList", "Ex_id" + MainActivity.EXERCICE_ID + " Wo_id" + MainActivity.WORKOUT_ID);

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.rep_list, null);

        myListAdapter = new repsAdapter();
        list = (ListView) root.findViewById(R.id.repslist);
        list.setAdapter(myListAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {

            }
        });

        getActivity().getActionBar().setTitle("Reps for exercise");

        return root;
    }



    class repsAdapter extends ArrayAdapter<String>
    {
        private String[] reps;

        public repsAdapter()
        {
            super(getActivity(), R.layout.rep_list_item, R.id.rep_textview);

            String ex_id = ""+ MainActivity.EXERCICE_ID;
            String wo_id = ""+ MainActivity.WORKOUT_ID;
            String[] projection = new String[]{Contract.WorkoutExercises._ID, Contract.WorkoutExercises.EXERCISE_ID, Contract.WorkoutExercises.WORKOUT_ID, Contract.WorkoutExercises.REPS};

            Cursor c = getActivity().getContentResolver().query(
                    Contract.WorkoutExercises.CONTENT_URI,
                    projection,
                    "(" + Contract.WorkoutExercises.EXERCISE_ID + " =? AND " + Contract.WorkoutExercises.WORKOUT_ID + "=?)",
                    new String[]{ex_id, wo_id},
                    null);

            c.moveToFirst();

            Log.d("RepList", DatabaseUtils.dumpCursorToString(c));

            reps = c.getString(c.getColumnIndex(Contract.WorkoutExercises.REPS)).split(" ");

            this.addAll(reps);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View row = super.getView(position, convertView, parent);

            String rep = reps[position];

            TextView txtRep = (TextView) row.findViewById(R.id.rep_textview);
            txtRep.setText(rep);

            return row;
        }

        @Override
        public int getCount() {
            return reps.length;
        }
    }
}
