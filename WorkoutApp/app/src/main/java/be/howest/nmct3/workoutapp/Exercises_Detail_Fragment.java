package be.howest.nmct3.workoutapp;



import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import be.howest.nmct3.workoutapp.data.Contract;
import be.howest.nmct3.workoutapp.data.DatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class Exercises_Detail_Fragment extends Fragment {

    TextView exercise_title;
    TextView exercise_description;
    ImageView exercise_image;

    public Exercises_Detail_Fragment() {
        // Required empty public constructor
    }

    public static android.support.v4.app.Fragment newInstance(Context context) {
        Exercises_Detail_Fragment frag = new Exercises_Detail_Fragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.exercises_detail_fragment_layout, null);
        String ex_id = ""+ MainActivity.EXERCICE_ID;
        String[] projection = new String[]{Contract.Exercises._ID, Contract.Exercises.EXERCISE_NAME, Contract.Exercises.MUSCLE_GROUP, Contract.Exercises.TARGET, Contract.Exercises.DESCRIPTION, Contract.Exercises.IMAGE_NAME};

        Cursor c = getActivity().getContentResolver().query(
                Contract.Exercises.CONTENT_URI,
                projection,
                "(" + Contract.Exercises._ID + "=?)",
                new String[]{ex_id},
                null);

        c.moveToFirst();

        Log.d("", "_-_-_-_-_-_ ID : " + MainActivity.EXERCICE_ID);
        Log.d("","_-_-_-_-_-_ rows: " + c.getCount() + " " + DatabaseUtils.dumpCursorToString(c));


        exercise_title = (TextView) root.findViewById(R.id.exercise_title);
        exercise_title.setText(c.getString(c.getColumnIndex(Contract.Exercises.EXERCISE_NAME)));

        exercise_description = (TextView) root.findViewById(R.id.exercise_description);
        exercise_description.setText(c.getString(c.getColumnIndex(Contract.Exercises.DESCRIPTION)));

        try {
            exercise_image = (ImageView) root.findViewById(R.id.exercise_detail_photo);
            exercise_image.setImageURI(Uri.parse((c.getString(c.getColumnIndex(Contract.Exercises.IMAGE_NAME)))));
        }catch (Exception e){

        }


        getActivity().getActionBar().setTitle("Exercise description");

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
        inflater.inflate(R.menu.exercises_detail, menu);
    }



}
