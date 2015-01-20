package be.howest.nmct3.workoutapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by nielslammens on 17/11/14.
 */
public class ExercisesLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mData;
    private final String mMuscleGroup;

    public ExercisesLoader(Context context, String muscleGroup) {
        super(context);
        mMuscleGroup = muscleGroup;
    }

    @Override
    public Cursor loadInBackground() {
        DatabaseHelper helper = DatabaseHelper.getInstance(getContext());

        SQLiteDatabase db = helper.getReadableDatabase();

        if(mMuscleGroup.equalsIgnoreCase("") || mMuscleGroup == null){
            //  no muscle group given, load all exercises
            mData = db.query(Contract.Exercises.CONTENT_DIRECTORY,
                    new String[]{
                            Contract.ExerciseColumns._ID,
                            Contract.ExerciseColumns.EXERCISE_NAME,
                            Contract.ExerciseColumns.TARGET,
                            Contract.ExerciseColumns.MUSCLE_GROUP},
                    null,
                    null,
                    null,
                    null,
                    Contract.ExerciseColumns.TARGET + " ASC"
            );
        }else{

            mData = db.query(Contract.Exercises.CONTENT_DIRECTORY,
                    new String[]{
                            Contract.ExerciseColumns._ID,
                            Contract.ExerciseColumns.EXERCISE_NAME,
                            Contract.ExerciseColumns.TARGET,
                            Contract.ExerciseColumns.MUSCLE_GROUP},
                    Contract.ExerciseColumns.MUSCLE_GROUP + "= ?",
                    new String[]{mMuscleGroup.toLowerCase()},
                    null,
                    null,
                    Contract.ExerciseColumns.TARGET + " ASC"
            );
        }

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
