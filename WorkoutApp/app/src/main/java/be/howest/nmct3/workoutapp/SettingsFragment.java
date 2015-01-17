package be.howest.nmct3.workoutapp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Set;

import be.howest.nmct3.workoutapp.data.SettingsAdmin;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class SettingsFragment extends Fragment {

    private ListAdapter myListAdapter;
    public ImageView imageViewProfilePicture;
    public static ListView listview;

    public static final String[] Settings = {"Name", "Gender", "Date of Birth", "E-mail", "Picture"};
    //public static final String[] Settings = {"Name", "Gender", "Date of Birth", "E-mail", "Picture", "Units"};

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Context context) {
        SettingsFragment frag = new SettingsFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.settings_fragment_layout, null);

        imageViewProfilePicture = (ImageView) root.findViewById(R.id.imageViewProfile);

        if(SettingsAdmin.getInstance(getActivity().getApplicationContext()).getPicture() != null)
        {
            String pic = SettingsAdmin.getInstance(getActivity().getApplicationContext()).getPicture();
            imageViewProfilePicture.setImageBitmap(BitmapFactory.decodeFile(pic));
        }

        myListAdapter = new CustomSettingsAdapter();
        listview = (ListView) root.findViewById(R.id.settings_list);
        listview.setAdapter(myListAdapter);

        final Button logoutButton = (Button) root.findViewById(R.id.LogOutID);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                logOut(v);
            }
        });

        return root;
    }

    class CustomSettingsAdapter extends ArrayAdapter<String> {

        public CustomSettingsAdapter() {
            super(getActivity(), R.layout.settings_list_item_layout, R.id.list_settings_item_text);
            this.addAll(Settings);
        }

        public void updateImage(Uri myProfilePictureURI)
        {
            this.notifyDataSetChanged();

            imageViewProfilePicture.setImageURI(myProfilePictureURI);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View row = super.getView(position, convertView, parent);

            TextView textSettingsTitle = (TextView) row.findViewById(R.id.list_settings_item_text);
            textSettingsTitle.setText(Settings[position]);

            TextView textSettingsPreview = (TextView) row.findViewById(R.id.list_settings_item_text_preview);
            String value = SettingsAdmin.getInstance(getContext()).getValueForSetting(position);
            textSettingsPreview.setText(value);

            ImageView imagePreview = (ImageView) row.findViewById(R.id.list_settings_item_image_preview);

            //Settings wijzigen
            if(position == 0)
            {
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        changeFirstnameLastname(v);
                    }
                });
            }

            if(position == 1)
            {
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        changeGender(v);
                    }
                });
            }

            if(position == 2)
            {
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        changeDateOfBirth(v);
                    }
                });
            }

            if(position == 3)
            {
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        changeEmail(v);
                    }
                });
            }

            if(position == 4) {
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((MainActivity)getActivity()).startProfilePicturePicker();
                    }
                });

                textSettingsPreview.setVisibility(View.INVISIBLE);

                String pic = SettingsAdmin.getInstance(getActivity().getApplicationContext()).getPicture();
                imagePreview.setImageBitmap(BitmapFactory.decodeFile(pic));

                imagePreview.setVisibility(View.VISIBLE);

            }

            return row;
        }

        @Override
        public int getCount() {
            return Settings.length;
        }
    }

    public void logOut(View v)
    {
        SettingsAdmin.getInstance(getActivity().getApplicationContext()).setUsername("");

        // DOORSTUREN NAAR LOGIN
        Intent myIntent = new Intent(getActivity(), LoginActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        getActivity().startActivity(myIntent);
        getActivity().finish();

    }

    public void changeFirstnameLastname(View v)
    {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.settings_dialog_firstname_lastname, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(promptView);

        final EditText input_firstname = (EditText) promptView.findViewById(R.id.settings_input_firstname);
        final EditText input_lastname = (EditText) promptView.findViewById(R.id.settings_input_lastname);
        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //resultText.setText("Hello, " + editText.getText());

                        SettingsAdmin.getInstance(getActivity().getApplicationContext()).setFirstname(input_firstname.getText().toString());
                        SettingsAdmin.getInstance(getActivity().getApplicationContext()).setLastname(input_lastname.getText().toString());

                        CustomSettingsAdapter adapter = (CustomSettingsAdapter) listview.getAdapter();
                        adapter.notifyDataSetChanged();
                        listview.setAdapter(adapter);

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

    public void changeGender(View v)
    {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.settings_dialog_gender, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(promptView);

        final Spinner input_gender = (Spinner) promptView.findViewById(R.id.spinner_gender);
        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        final String gender_chosen = input_gender.getSelectedItem().toString();
                        Log.d("gender", gender_chosen);
                        SettingsAdmin.getInstance(getActivity().getApplicationContext()).setGender(gender_chosen);

                        //update list
                        CustomSettingsAdapter adapter = (CustomSettingsAdapter) listview.getAdapter();
                        adapter.notifyDataSetChanged();
                        listview.setAdapter(adapter);

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

    public void changeDateOfBirth(View v)
    {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.settings_dialog_dateofbirth, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(promptView);

        final DatePicker input_picker = (DatePicker) promptView.findViewById(R.id.datePicker_dateofbirth);
        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        int day = input_picker.getDayOfMonth();
                        int month = input_picker.getMonth() + 1;
                        int year = input_picker.getYear();

                        String dayStr = Integer.toString(day);
                        String monthStr = Integer.toString(month);
                        String yearStr = Integer.toString(year);

                        final String dateofbirth_chosen = day + "-" + month + "-" + year;
                        Log.d("gender", dateofbirth_chosen);
                        SettingsAdmin.getInstance(getActivity().getApplicationContext()).setDateOfBirth(dateofbirth_chosen);

                        //update list
                        CustomSettingsAdapter adapter = (CustomSettingsAdapter) listview.getAdapter();
                        adapter.notifyDataSetChanged();
                        listview.setAdapter(adapter);

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

    public void changeEmail(View v)
    {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.settings_dialog_email, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(promptView);

        final EditText input_email = (EditText) promptView.findViewById(R.id.settings_input_email);

        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //resultText.setText("Hello, " + editText.getText());

                        SettingsAdmin.getInstance(getActivity().getApplicationContext()).setEmail(input_email.getText().toString());

                        CustomSettingsAdapter adapter = (CustomSettingsAdapter) listview.getAdapter();
                        adapter.notifyDataSetChanged();
                        listview.setAdapter(adapter);

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
}

