package be.howest.nmct3.workoutapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

/**
 * Created by nielslammens on 17/11/14.
 */
public class SpecificWorkoutLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mData;
    private final String mWorkoutID;

    public SpecificWorkoutLoader(Context context, String workoutId) {
        super(context);
        mWorkoutID = workoutId;
    }

    @Override
    public Cursor loadInBackground() {
        DatabaseHelper helper = DatabaseHelper.getInstance(getContext());

        SQLiteDatabase db = helper.getReadableDatabase();

        if (!mWorkoutID.equalsIgnoreCase("") || mWorkoutID != null) {
            //  no muscle group given, load all exercises
            mData = db.rawQuery("SELECT "   + Contract.Exercises.CONTENT_DIRECTORY + "."        + Contract.ExerciseColumns._ID              + ", "
                                            + Contract.Exercises.CONTENT_DIRECTORY + "."        + Contract.ExerciseColumns.EXERCISE_NAME    + ", "
                                            + Contract.Exercises.CONTENT_DIRECTORY + "."        + Contract.ExerciseColumns.MUSCLE_GROUP    + ", "
                                            + Contract.WorkoutExercises.CONTENT_DIRECTORY + "." + Contract.WorkoutExercises._ID             + ", "
                                            + Contract.WorkoutExercises.EXERCISE_ID     + ", "
                                            + Contract.WorkoutExercises.WORKOUT_ID      + ", "
                                            + Contract.WorkoutExercises.REPS            +

                                " FROM " + Contract.Exercises.CONTENT_DIRECTORY +
                                " INNER JOIN " + Contract.WorkoutExercises.CONTENT_DIRECTORY +
                                " ON " + Contract.Exercises.CONTENT_DIRECTORY + "." + Contract.ExerciseColumns._ID + " = " + Contract.WorkoutExercises.CONTENT_DIRECTORY + "." + Contract.WorkoutExerciseColumns.EXERCISE_ID +
                                " INNER JOIN " + Contract.Workouts.CONTENT_DIRECTORY +
                                " ON " + Contract.WorkoutExercises.CONTENT_DIRECTORY + "." + Contract.WorkoutExerciseColumns.WORKOUT_ID + " = " + Contract.Workouts.CONTENT_DIRECTORY + "." + Contract.WorkoutColumns._ID +
                                " WHERE (" + Contract.Workouts.CONTENT_DIRECTORY + "." + Contract.WorkoutColumns._ID + " = ?) AND (" + Contract.WorkoutExercises.CONTENT_DIRECTORY + "." + Contract.WorkoutExercises.DELETE + " = \"0\")",
                                new String[]{mWorkoutID});
            //mData = db.rawQuery("SELECT * FROM workouts WHERE _id = ?", new String[]{mWorkoutID});
        }

        mData.getCount();

        Log.d("", "_-_-_-_-_-_" + DatabaseUtils.dumpCursorToString(mData));

        return mData;
    }

    @Override
    public void deliverResult(Cursor data) {
        if (isReset()) {
            if (data != null) {
                data.close();
            }
            return;
        }

        Cursor oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if ((oldData != null && oldData != data && !oldData.isClosed())) {
            oldData.close();
        }

    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }

        if (takeContentChanged() || mData == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor data) {
        if (data != null && !data.isClosed()) {
            data.close();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();

        if (mData != null && !mData.isClosed()) {
            mData.close();
        }

        mData = null;
    }

    private void releaseResources(Cursor data) {
        data.close();
    }

}