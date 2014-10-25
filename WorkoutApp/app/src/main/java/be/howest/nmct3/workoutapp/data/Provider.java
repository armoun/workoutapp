package be.howest.nmct3.workoutapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by nielslammens on 22/10/14.
 */
public class Provider extends ContentProvider{

    private static HashMap<String, String> sWorkoutsProjectionMap;
    private static HashMap<String, String> sWorkoutExercisesProjectionMap;

    private static final int WORKOUTS = 1;
    private static final int WORKOUT_ID = 2;
    private static final int WORKOUTEXERCISES = 3;
    private static final int WORKOUTEXERCISE_ID = 4;

    private DatabaseHelper mOpenHelper;

    private static final UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Contract.AUTHORITY, "workouts", WORKOUTS);
        sUriMatcher.addURI(Contract.AUTHORITY, "workouts/#", WORKOUT_ID);
        sUriMatcher.addURI(Contract.AUTHORITY, "workoutexercises", WORKOUTEXERCISES);
        sUriMatcher.addURI(Contract.AUTHORITY, "workoutexercises/#", WORKOUTEXERCISE_ID);

        sWorkoutsProjectionMap = new HashMap<String, String>();
        sWorkoutsProjectionMap.put(Contract.Workouts._ID, Contract.Workouts._ID);
        sWorkoutsProjectionMap.put(Contract.Workouts.NAME, Contract.Workouts.NAME);
        sWorkoutsProjectionMap.put(Contract.Workouts.ISPAID, Contract.Workouts.ISPAID);

        sWorkoutExercisesProjectionMap = new HashMap<String, String>();
        sWorkoutExercisesProjectionMap.put(Contract.WorkoutExercises._ID, Contract.WorkoutExercises._ID);
        sWorkoutExercisesProjectionMap.put(Contract.WorkoutExercises.WORKOUT_ID, Contract.WorkoutExercises.WORKOUT_ID);
        sWorkoutExercisesProjectionMap.put(Contract.WorkoutExercises.EXERCISE_ID, Contract.WorkoutExercises.EXERCISE_ID);
    }

    public Provider(){
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = DatabaseHelper.getInstance(getContext());

        Log.d(getClass().getCanonicalName(), "GET WRITABLE DATABASE");

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String orderBy = sortOrder;

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)){
            case WORKOUTS:
                qb.setTables(Contract.Workouts.CONTENT_DIRECTORY);
                qb.setProjectionMap(sWorkoutsProjectionMap);
                if (TextUtils.isEmpty(sortOrder)){
                    orderBy = Contract.Workouts.DEFAULT_SORT_ORDER;
                }
                break;
            case WORKOUT_ID:
                qb.setTables(Contract.Workouts.CONTENT_DIRECTORY);
                qb.setProjectionMap(sWorkoutsProjectionMap);
                qb.appendWhere(
                        "(" + Contract.Workouts._ID + "=" + uri.getPathSegments().get(Contract.Workouts.WORKOUT_ID_PATH_POSITION) + ")"
                );
                if (TextUtils.isEmpty(sortOrder)){
                    orderBy = Contract.Workouts.DEFAULT_SORT_ORDER;
                }
                break;
            case WORKOUTEXERCISES:
                qb.setTables(Contract.WorkoutExercises.CONTENT_DIRECTORY);
                qb.setProjectionMap(sWorkoutExercisesProjectionMap);
                if (TextUtils.isEmpty(sortOrder)){
                    orderBy = Contract.WorkoutExercises.DEFAULT_SORT_ORDER;
                }
                break;
            case WORKOUTEXERCISE_ID:
                qb.setTables(Contract.WorkoutExercises.CONTENT_DIRECTORY);
                qb.setProjectionMap(sWorkoutExercisesProjectionMap);
                qb.appendWhere(
                        "(" + Contract.WorkoutExercises._ID + "=" + uri.getPathSegments().get(Contract.WorkoutExercises.WORKOUTEXERCISE_ID_PATH_POSITION) + ")"
                );
                if (TextUtils.isEmpty(sortOrder)){
                    orderBy = Contract.WorkoutExercises.DEFAULT_SORT_ORDER;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        Cursor c = qb.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                orderBy
        );

        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case WORKOUTS:
                return Contract.Workouts.CONTENT_TYPE;
            case WORKOUT_ID:
                return Contract.Workouts.CONTENT_ITEM_TYPE;
            case WORKOUTEXERCISES:
                return Contract.WorkoutExercises.CONTENT_TYPE;
            case WORKOUTEXERCISE_ID:
                return Contract.WorkoutExercises.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        Log.d(getClass().getCanonicalName(), "INSERT VALUES NOW IN PROVIDER");

        long rowId;
        switch (sUriMatcher.match(uri)){
            case WORKOUTS:
                if (!values.containsKey(Contract.Workouts.NAME))
                    throw  new IllegalArgumentException(Contract.Workouts.NAME + " is required for " + Contract.Workouts.CONTENT_DIRECTORY);

                rowId = db.insert(
                        Contract.Workouts.CONTENT_DIRECTORY,
                        Contract.Workouts.NAME,
                        values
                );

                if (rowId > 0){
                    Uri workoutUri = ContentUris.withAppendedId(Contract.Workouts.ITEM_CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(workoutUri, null);
                    return workoutUri;
                }
                break;
            case WORKOUTEXERCISES:
                if (!values.containsKey(Contract.WorkoutExercises.WORKOUT_ID))
                    throw  new IllegalArgumentException(Contract.WorkoutExercises.WORKOUT_ID + " is required for " + Contract.WorkoutExercises.CONTENT_DIRECTORY);

                rowId = db.insert(
                        Contract.WorkoutExercises.CONTENT_DIRECTORY,
                        Contract.WorkoutExercises.WORKOUT_ID,
                        values
                );

                if (rowId > 0){
                    Uri workoutExerciseUri = ContentUris.withAppendedId(Contract.WorkoutExercises.ITEM_CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(workoutExerciseUri, null);
                    return workoutExerciseUri;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        throw new IllegalArgumentException();
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        String finalWhere;
        int count;

        switch (sUriMatcher.match(uri)) {
            case WORKOUTS:
                count = db.delete(
                        Contract.Workouts.CONTENT_DIRECTORY,
                        where,
                        whereArgs
                );
                break;

            case WORKOUT_ID:
                String workoutId = uri.getPathSegments().get(Contract.Workouts.WORKOUT_ID_PATH_POSITION);
                finalWhere = Contract.Workouts._ID + "=" + workoutId;

                if (where != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, where);
                }

                count = db.delete(
                        Contract.Workouts.CONTENT_DIRECTORY,
                        finalWhere,
                        whereArgs
                );
                break;
            case WORKOUTEXERCISES:
                count = db.delete(
                        Contract.WorkoutExercises.CONTENT_DIRECTORY,
                        where,
                        whereArgs
                );
                break;

            case WORKOUTEXERCISE_ID:
                String workoutExerciseId = uri.getPathSegments().get(Contract.WorkoutExercises.WORKOUTEXERCISE_ID_PATH_POSITION);
                finalWhere = Contract.WorkoutExercises._ID + "=" + workoutExerciseId;

                if (where != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, where);
                }

                count = db.delete(
                        Contract.WorkoutExercises.CONTENT_DIRECTORY,
                        finalWhere,
                        whereArgs
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        String finalWhere;

        switch (sUriMatcher.match(uri)) {
            case WORKOUTS:
                count = db.update(
                        Contract.Workouts.CONTENT_DIRECTORY,
                        values,
                        where,
                        whereArgs
                );
                break;

            case WORKOUT_ID:
                String workoutId = uri.getPathSegments().get(Contract.Workouts.WORKOUT_ID_PATH_POSITION);
                finalWhere = Contract.WorkoutExercises._ID + "=" + workoutId;
                if (where != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, where);
                }

                count = db.update(
                        Contract.Workouts.CONTENT_DIRECTORY,
                        values,
                        finalWhere,
                        whereArgs
                );
                break;
            case WORKOUTEXERCISES:
                count = db.update(
                        Contract.WorkoutExercises.CONTENT_DIRECTORY,
                        values,
                        where,
                        whereArgs
                );
                break;

            case WORKOUTEXERCISE_ID:
                String workoutExerciseId = uri.getPathSegments().get(Contract.WorkoutExercises.WORKOUTEXERCISE_ID_PATH_POSITION);
                finalWhere = Contract.WorkoutExercises._ID + "=" + workoutExerciseId;
                if (where != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, where);
                }

                count = db.update(
                        Contract.WorkoutExercises.CONTENT_DIRECTORY,
                        values,
                        finalWhere,
                        whereArgs
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}
