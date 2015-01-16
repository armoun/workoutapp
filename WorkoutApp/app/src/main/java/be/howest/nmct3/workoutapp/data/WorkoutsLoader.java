package be.howest.nmct3.workoutapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by nielslammens on 28/10/14.
 */
public class WorkoutsLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mData;

    public WorkoutsLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        DatabaseHelper helper = DatabaseHelper.getInstance(getContext());

        SQLiteDatabase db = helper.getReadableDatabase();

        mData = db.query(Contract.Workouts.CONTENT_DIRECTORY,
                new String[]{
                        Contract.WorkoutColumns._ID,
                        Contract.WorkoutColumns.NAME,
                        Contract.WorkoutColumns.ISPAID},
                Contract.Workouts.USERNAME + "=? OR " + Contract.Workouts.USERNAME + "= \"ALL\"",
                new String[]{SettingsAdmin.getInstance(getContext()).getUsername()},
                null,
                null,
                Contract.WorkoutColumns.ISPAID + " ASC"
        );

        //mData = db.rawQuery("SELECT * FROM " + Contract.Workouts.CONTENT_DIRECTORY ,null);

        mData.getCount();

        return mData;
    }

    @Override
    public void deliverResult(Cursor data) {
        if(isReset()){
            if(data != null){
                data.close();
            }
            return;
        }

        Cursor oldData = mData;
        mData = data;

        if(isStarted()){
            super.deliverResult(data);
        }

        if ((oldData != null && oldData != data && !oldData.isClosed())){
            oldData.close();
        }

    }

    @Override
    protected void onStartLoading() {
        if(mData != null){
            deliverResult(mData);
        }

        if(takeContentChanged() || mData == null){
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor data) {
        if(data != null && !data.isClosed()){
            data.close();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();

        if(mData != null && !mData.isClosed()){
            mData.close();
        }

        mData = null;
    }

    private void releaseResources(Cursor data){
        data.close();
    }
}
