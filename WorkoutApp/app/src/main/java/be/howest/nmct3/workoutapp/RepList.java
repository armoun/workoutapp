package be.howest.nmct3.workoutapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import be.howest.nmct3.workoutapp.data.Contract;

/**
 * Created by nielslammens on 2/12/14.
 */
public class RepList extends Fragment {

    private ListAdapter myListAdapter;
    ListView list;
    private String[] reps;
    String Owner;

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
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);

        if(Owner.equals("ALL"))
        {
            inflater.inflate(R.menu.replist_all, menu);
        }
        else
        {
            if(MainActivity.todaysWorkoutClicked) {

                Log.d("","onCreateOptionsMenu todaysworkout");
                inflater.inflate(R.menu.replist_todaysworkout, menu);

            } else
            {


                inflater.inflate(R.menu.replist, menu);
            }
        }
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
                            String we_id = ""+ MainActivity.WORKOUT_EXERCICE_ID;
                            Cursor c = getActivity().getContentResolver().query(Contract.WorkoutExercises.CONTENT_URI, new String[]{Contract.WorkoutExercises.REPS},
                                    Contract.WorkoutExercises._ID + " =?",
                                    new String[]{we_id},
                                    null);
                            c.moveToFirst();
                            String reps = c.getString(c.getColumnIndex(Contract.WorkoutExercises.REPS));
                            reps += " " + input_reps.getText().toString();

                            ContentValues cv = new ContentValues();
                            cv.put(Contract.WorkoutExercises.REPS, reps);

                            int a = getActivity().getContentResolver().update(Contract.WorkoutExercises.CONTENT_URI, cv,
                                    Contract.WorkoutExercises._ID+ " =?",
                                    new String[]{we_id});

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

        Log.d("RepList", "we_id" + MainActivity.WORKOUT_EXERCICE_ID);

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.rep_list, null);

        myListAdapter = new repsAdapter();
        list = (ListView) root.findViewById(R.id.repslist);
        list.setAdapter(myListAdapter);

        Owner = MainActivity.workoutDatasource.getOwnerOfWorkout(getActivity().getApplicationContext(), MainActivity.WORKOUT_ID);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {

            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int pos, long id) {
                //

                //Toast.makeText(getActivity().getBaseContext(), "Long Clicked on" + pos, Toast.LENGTH_SHORT).show();

                //Cursor filteredCursor = ((SimpleCursorAdapter)list.getAdapter()).getCursor();

                //filteredCursor.moveToPosition(pos);

                String selectedFromList = "";

                //HIER MOET DE REP OPGEHAALD WORDEN

                //selectedFromList = ""+ filteredCursor.getString(filteredCursor.getColumnIndex(Contract.WorkoutExercises.REPS));
                selectedFromList = reps[pos];

                if(Owner.equals("ALL"))
                {
                    AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity())
                            .setTitle("Error")
                            .setMessage("You can't edit or delete reps in this workout.")
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
                else
                {
                    //String selectedFromList = listView.getItemAtPosition(pos).toString();
                    openDialogEditDelete(v, selectedFromList, pos);
                }

                return true;
            }
        });

        getActivity().getActionBar().setTitle("Reps for exercise");

        return root;
    }


    public void openDialogEditDelete(View v, String SelectedText, int pos)
    {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        final View promptView = layoutInflater.inflate(R.layout.input_dialog_edit_delete, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(promptView);

        //EDIT TEXT
        final EditText input_for_row = (EditText) promptView.findViewById(R.id.input_for_row);
        input_for_row.setText(SelectedText);

        final int posi = pos;

        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String newRep = input_for_row.getText().toString();
                        Log.d("Edit rep", newRep);
                        reps[posi] = newRep;
                        Log.d("Edit rep", newRep + " " + posi + " " + reps[posi]);
                        String allreps = "";
                        if(reps.length > 1){
                            for(String str: reps) {
                                if(!str.equals("")){
                                    allreps = allreps + " " + str;
                                }
                            }
                            allreps = allreps.substring(1);
                        }else{
                            allreps = newRep;
                        }

                        Log.d("Edit rep", newRep + " " + posi + " " + reps[posi] + " " + allreps);

                        ContentValues c = new ContentValues();
                        c.put(Contract.WorkoutExercises.REPS, allreps);
                        String w = "" + MainActivity.WORKOUT_EXERCICE_ID;
                        getActivity().getContentResolver().update(Contract.WorkoutExercises.CONTENT_URI, c, Contract.WorkoutExercises._ID + "=?", new String[]{w});
                        reOpenFragment();

                    }
                })
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
                deleteRowMethod(v, posi);
                alert.cancel();
            }
        });
    }

    public void deleteRowMethod(View v, int pos)
    {
        //TODO

        reps[pos] = "";
        String allreps = "";
        if(reps.length > 1){
            for(String str: reps) {
                if(!str.equals("")){
                    allreps = allreps + " " + str;
                }
            }
            allreps = allreps.substring(1);
        }else{
            //TODO DIALOG MUST HAVE AT LEAST 1 REPETITION
        }

        ContentValues c = new ContentValues();
        c.put(Contract.WorkoutExercises.REPS, allreps);
        String w = "" + MainActivity.WORKOUT_EXERCICE_ID;
        getActivity().getContentResolver().update(Contract.WorkoutExercises.CONTENT_URI, c, Contract.WorkoutExercises._ID + "=?", new String[]{w});

        //Toast.makeText(getActivity().getBaseContext(), "Row deleted" , Toast.LENGTH_SHORT).show();
        reOpenFragment();
    }

    class repsAdapter extends ArrayAdapter<String>
    {
        //private String[] reps;

        public repsAdapter()
        {
            super(getActivity(), R.layout.rep_list_item, R.id.rep_textview);

            String we_id = ""+ MainActivity.WORKOUT_EXERCICE_ID;
            String[] projection = new String[]{Contract.WorkoutExercises._ID, Contract.WorkoutExercises.EXERCISE_ID, Contract.WorkoutExercises.WORKOUT_ID, Contract.WorkoutExercises.REPS};

            Cursor c = getActivity().getContentResolver().query(
                    Contract.WorkoutExercises.CONTENT_URI,
                    projection,
                    Contract.WorkoutExercises._ID + " =?",
                    new String[]{we_id},
                    null);

            c.moveToFirst();

            //Toast.makeText(getActivity(), "ID: " + we_id,Toast.LENGTH_SHORT).show();

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

    private void reOpenFragment() {
        // Create and set the start fragment
        Fragment frag = Fragment.instantiate(getActivity(), "be.howest.nmct3.workoutapp.RepList");
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        MainActivity.activeFragment = frag;
        //Toast.makeText(getActivity(), "Active fragment: " + frag.getClass().getSimpleName(),Toast.LENGTH_SHORT).show();

        transaction.replace(R.id.main, frag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onDestroy() {
        Workouts_SelectedWorkoutList_Fragment.backPressedRepList = true;
        //Toast.makeText(getActivity(), "Destroyed",Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}
