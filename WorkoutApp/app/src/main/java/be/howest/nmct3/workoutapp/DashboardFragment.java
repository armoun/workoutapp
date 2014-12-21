package be.howest.nmct3.workoutapp;



import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

import be.howest.nmct3.workoutapp.Account.GenericAccountService;
import be.howest.nmct3.workoutapp.data.Contract;
import be.howest.nmct3.workoutapp.data.DatabaseHelper;
import be.howest.nmct3.workoutapp.data.SettingsAdmin;
import be.howest.nmct3.workoutapp.sync.SyncUtils;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class DashboardFragment extends Fragment {

    TextView Weight;
    TextView Height;
    TextView BMI;
    TextView TodaysWorkout;

    String weight;
    String height;

    private Object mSyncObserverHandle;


    public DashboardFragment() {
        // Required empty public constructor
    }

    public static android.support.v4.app.Fragment newInstance(Context context) {
        DashboardFragment frag = new DashboardFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.dashboard_fragment_layout, null);

        de.hdodenhof.circleimageview.CircleImageView profilePicture = (de.hdodenhof.circleimageview.CircleImageView) root.findViewById(R.id.profile_image);

        if(MainActivity.myProfilePicturePath != "Picture.jpg") {
            profilePicture.setImageBitmap(BitmapFactory.decodeFile(MainActivity.myProfilePicturePath));
        }

        //NAAM INVULLEN
        TextView txtNavDrawerTitle = (TextView) root.findViewById(R.id.dashboardFirstnameLastnameId);
        String firstname = SettingsAdmin.getInstance(getActivity().getApplicationContext()).getFirstname();
        String lastname = SettingsAdmin.getInstance(getActivity().getApplicationContext()).getLastname();
        txtNavDrawerTitle.setText(firstname + " " + lastname);

        Weight  = (TextView) root.findViewById(R.id.dashboardWEIGHT);
        Height = (TextView) root.findViewById(R.id.dashboardHEIGHT);
        BMI = (TextView) root.findViewById(R.id.dashboardBMI);
        TodaysWorkout = (TextView) root.findViewById(R.id.todaysWorkout);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        weight = preferences.getString(SettingsAdmin.getInstance(getActivity().getApplicationContext()).getUsername()+"-WEIGHT","");
        if(!weight.equalsIgnoreCase(""))
        {
            Weight.setText(weight + " kg");
        }
        else
        {
            Weight.setText("0 kg");
        }

        height = preferences.getString(SettingsAdmin.getInstance(getActivity().getApplicationContext()).getUsername()+"-HEIGHT","");
        if(!height.equalsIgnoreCase(""))
        {
            Height.setText(height + " cm");
        }
        else
        {
            Height.setText("0 cm");
        }

        getWorkout();

        calculateBMI(Weight, Height, BMI);

        Weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                changeWeight(v);
            }
        });
        Height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                changeHeight(v);
            }
        });
        TodaysWorkout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Fragment newFragment = Fragment.instantiate(getActivity().getApplicationContext(), "be.howest.nmct3.workoutapp.Workouts_SelectedWorkoutList_Fragment");
                // consider using Java coding conventions (upper first char class names!!!)
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                MainActivity.activeFragment = newFragment;

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back1 stack
                transaction.replace(R.id.main, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

        return root;
    }

    private void getWorkout(){
        Calendar cal = Calendar.getInstance();
        String date = cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR);
        Log.d("DashboardFragment","Date:" + date);
        SQLiteDatabase db = DatabaseHelper.getInstance(getActivity().getApplicationContext()).getReadableDatabase();
        Cursor c = db.rawQuery( "SELECT "       + Contract.Workouts.CONTENT_DIRECTORY + "." + Contract.Workouts._ID + ", " + Contract.Workouts.CONTENT_DIRECTORY + "." + Contract.Workouts.NAME +
                                " FROM "         + Contract.Workouts.CONTENT_DIRECTORY +
                                " INNER JOIN "   + Contract.Planners.CONTENT_DIRECTORY +
                                " ON "           + Contract.Workouts.CONTENT_DIRECTORY + "." + Contract.Workouts._ID + " = " + Contract.Planners.CONTENT_DIRECTORY + "." + Contract.Planners.WORKOUT_ID +
                                " WHERE "        + Contract.Planners.CONTENT_DIRECTORY + "." + Contract.Planners.WO_DATE + " = ?",
                new String[]{date});

        //Cursor c1 = db.rawQuery( "SELECT * FROM " + Contract.Planners.CONTENT_DIRECTORY, null);

        Log.d("DashboardFragment", DatabaseUtils.dumpCursorToString(c));
        c.moveToFirst();
        if(c.getCount() > 0){
            TodaysWorkout.setText(c.getString(c.getColumnIndex(Contract.Workouts.NAME)));
            MainActivity.WORKOUT_ID = c.getInt(c.getColumnIndex(Contract.Workouts._ID));
        }else{
            TodaysWorkout.setText("No workout planned");
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        SyncUtils.CreateSyncAccount(activity);
    }

    @Override
    public void onResume() {
        super.onResume();

        mSyncStatusObserver.onStatusChanged(0);

        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING | ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mSyncObserverHandle != null){
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
        }
    }

    public void setRefreshActionButtonState(boolean refreshing) {
        /*if (mOptionsMenu == null) {
            return;
        }*/

        /*final MenuItem refreshItem = mOptionsMenu.findItem(R.id.menu_refresh);
        if (refreshItem != null) {
            if (refreshing) {
                refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
            } else {
                refreshItem.setActionView(null);
            }
        }*/
    }
    /**
     * Crfate a new anonymous SyncStatusObserver. It's attached to the app's ContentResolver in
     * onResume(), and removed in onPause(). If status changes, it sets the state of the Refresh
     * button. If a sync is active or pending, the Refresh button is replaced by an indeterminate
     * ProgressBar; otherwise, the button itself is displayed.
     */
    private SyncStatusObserver mSyncStatusObserver = new SyncStatusObserver() {
        /** Callback invoked with the sync adapter status changes. */
        @Override
        public void onStatusChanged(int which) {
            getActivity().runOnUiThread(new Runnable() {
                /**
                 * The SyncAdapter runs on a background thread. To update the UI, onStatusChanged()
                 * runs on the UI thread.
                 */
                @Override
                public void run() {
                    // Create a handle to the account that was created by
                    // SyncService.CreateSyncAccount(). This will be used to query the system to
                    // see how the sync status has changed.
                    Account account = GenericAccountService.GetAccount();
                    if (account == null) {
                        // GetAccount() returned an invalid value. This shouldn't happen, but
                        // we'll set the status to "not refreshing".
                        setRefreshActionButtonState(false);
                        return;
                    }

                    // Test the ContentResolver to see if the sync adapter is active or pending.
                    // Set the state of the refresh button accordingly.
                    boolean syncActive = ContentResolver.isSyncActive(
                            account, Contract.AUTHORITY);
                    boolean syncPending = ContentResolver.isSyncPending(
                            account, Contract.AUTHORITY);
                    setRefreshActionButtonState(syncActive || syncPending);
                }
            });
        }
    };

    public void calculateBMI(TextView pWeight, TextView pHeight, TextView pBMI)
    {
        String[] splitWeight = pWeight.getText().toString().split("\\s+");
        String[] splitHeight = pHeight.getText().toString().split("\\s+");

        double iWeight = Double.parseDouble(splitWeight[0]);
        double iHeight = Double.parseDouble(splitHeight[0]);
        iHeight = iHeight/100;

        Log.d("WEIGHT", Double.toString(iWeight));
        Log.d("HEIGHT", Double.toString(iHeight));

        double myBMI = (iWeight / (iHeight * iHeight));
        myBMI *= 100;
        myBMI = Math.round(myBMI);
        myBMI /= 100;

        Log.d("BMI", Double.toString(myBMI));

        pBMI.setText(Double.toString(myBMI) + " BMI");
    }


    public void changeWeight(View v)
    {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(promptView);

        final EditText input_weight = (EditText) promptView.findViewById(R.id.input_weight);
        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //resultText.setText("Hello, " + editText.getText());

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(SettingsAdmin.getInstance(getActivity().getApplicationContext()).getUsername()+"-WEIGHT",input_weight.getText().toString());
                        editor.apply();

                        Weight.setText(input_weight.getText().toString() + " kg");
                        calculateBMI(Weight, Height, BMI);
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

    public void changeHeight(View v)
    {
// get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.input_dialog_height, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(promptView);

        final EditText input_height = (EditText) promptView.findViewById(R.id.input_height);
        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //resultText.setText("Hello, " + editText.getText());

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(SettingsAdmin.getInstance(getActivity().getApplicationContext()).getUsername()+"-HEIGHT",input_height.getText().toString());
                        editor.apply();

                        Height.setText(input_height.getText().toString() + " cm");
                        calculateBMI(Weight, Height, BMI);
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
