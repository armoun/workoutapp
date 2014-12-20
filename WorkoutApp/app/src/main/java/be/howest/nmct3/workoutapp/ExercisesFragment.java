package be.howest.nmct3.workoutapp;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;

import be.howest.nmct3.workoutapp.data.Exercise;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class ExercisesFragment extends Fragment {

    private ListAdapter myListAdapter;


    public ExercisesFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Context context) {
        ExercisesFragment frag = new ExercisesFragment();
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.exercises_fragment_layout, null);

        myListAdapter = new MuscleGroupAdapter();
        GridView gridView = (GridView) root.findViewById(R.id.gridview_musclegroups);
        gridView.setAdapter(myListAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Toast.makeText(getActivity().getBaseContext(), "Musclegroup " + MainActivity.MuscleGroups[position] + " selected", Toast.LENGTH_SHORT).show();
                MainActivity.EXTRA_SELECTED_MUSCLEGROUP = MainActivity.MuscleGroups[position];

                Fragment newFragment = Fragment.instantiate(getActivity().getApplicationContext(), "be.howest.nmct3.workoutapp.Exercises_Musclegroup_Fragment");
                // consider using Java coding conventions (upper first char class names!!!)
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                MainActivity.activeFragment = newFragment;
                //Toast.makeText(getActivity().getBaseContext(), "Active fragment: " + MainActivity.activeFragment.getClass().getSimpleName(),Toast.LENGTH_SHORT).show();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back1 stack
                transaction.replace(R.id.main, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });


        getActivity().getActionBar().setTitle("Choose a muscle group");

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.exercises, menu);
    }

    class MuscleGroupAdapter extends ArrayAdapter<String>
    {
        private String[] MuscleGroupTitles;

        public MuscleGroupAdapter()
        {
            super(getActivity(), R.layout.exercises_grid_musclegroup_item_layout, R.id.grid_item_text);
            MuscleGroupTitles = MainActivity.MuscleGroups;
            this.addAll(MuscleGroupTitles);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View row = super.getView(position, convertView, parent);

            String MuscleGroupTitle = MuscleGroupTitles[position];

            TextView txtMuscleGroupTitle = (TextView) row.findViewById(R.id.grid_item_text);
            txtMuscleGroupTitle.setText(MuscleGroupTitle);

                if(position == 0)
                {
                    ImageView imgMusclegroupIcon = (ImageView) row.findViewById(R.id.musclegroup_icon);
                    imgMusclegroupIcon.setImageResource(R.drawable.arms_red);
                }

                if(position == 1)
                {
                    ImageView imgMusclegroupIcon = (ImageView) row.findViewById(R.id.musclegroup_icon);
                    imgMusclegroupIcon.setImageResource(R.drawable.back1_red);
                }

                if(position == 2)
                {
                    ImageView imgMusclegroupIcon = (ImageView) row.findViewById(R.id.musclegroup_icon);
                    imgMusclegroupIcon.setImageResource(R.drawable.chest_red);
                }

                if(position == 3)
                {
                    ImageView imgMusclegroupIcon = (ImageView) row.findViewById(R.id.musclegroup_icon);
                    imgMusclegroupIcon.setImageResource(R.drawable.shoulders_red);
                }

                if(position == 4)
                {
                    ImageView imgMusclegroupIcon = (ImageView) row.findViewById(R.id.musclegroup_icon);
                    imgMusclegroupIcon.setImageResource(R.drawable.abs_red);
                }

                if(position == 5)
                {
                    ImageView imgMusclegroupIcon = (ImageView) row.findViewById(R.id.musclegroup_icon);
                    imgMusclegroupIcon.setImageResource(R.drawable.legs_red);
                }


            return row;
        }

        @Override
        public int getCount() {
            return MuscleGroupTitles.length;
        }
    }




}
