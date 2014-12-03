package be.howest.nmct3.workoutapp;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
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


    public RepList() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Context context) {
        RepList frag = new RepList();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("RepList", "Ex_id" + MainActivity.EXERCICE_ID + " Wo_id" + MainActivity.WORKOUT_ID);

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.rep_list, null);

        myListAdapter = new repsAdapter();
        ListView list = (ListView) root.findViewById(R.id.repslist);
        list.setAdapter(myListAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {

            }
        });



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
            //"(" + Contract.WorkoutExercises.EXERCISE_ID + "=? AND " + Contract.WorkoutExercises.WORKOUT_ID + "=?)", new String[]{ex_id, wo_id},

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
