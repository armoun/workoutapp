package be.howest.nmct3.workoutapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nielslammens on 22/10/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    public static DatabaseHelper INSTANCE;
    public static Object lock = new Object();

    public static final String DB_NAME = "workoutapp.db";
    public static final int DB_VERSION = 1;

    public DatabaseHelper(Context context){super(context, DB_NAME, null, DB_VERSION);}

    public static DatabaseHelper getInstance(Context context) {
        if(INSTANCE == null){
            synchronized (lock){
                if(INSTANCE == null){
                    INSTANCE = new DatabaseHelper(context.getApplicationContext());
                }
            }
        }

        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createWorkoutsTableV1(sqLiteDatabase);
        createWorkoutExercisesTableV1(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + Contract.Workouts.CONTENT_DIRECTORY);
        db.execSQL("DROP TABLE " + Contract.WorkoutExercises.CONTENT_DIRECTORY);

        createWorkoutsTableV1(db);
        createWorkoutExercisesTableV1(db);
    }

    private void createWorkoutsTableV1(SQLiteDatabase db){
        String SQL = "CREATE TABLE " + Contract.Workouts.CONTENT_DIRECTORY + " ("
                + Contract.WorkoutColumns._ID       + " INTEGER PRIMARY KEY, "
                + Contract.WorkoutColumns.NAME      + " TEXT,"
                + Contract.WorkoutColumns.ISPAID    + " INTEGER"
                + ");";

        Log.d(getClass().getCanonicalName(), "CREATE TABLE VOOR WORKOUTS");


        db.execSQL(SQL);
    }

    private void createWorkoutExercisesTableV1(SQLiteDatabase db){
        String SQL = "CREATE TABLE " + Contract.WorkoutExercises.CONTENT_DIRECTORY + " ("
                + Contract.WorkoutExerciseColumns._ID           + " INTEGER PRIMARY KEY, "
                + Contract.WorkoutExerciseColumns.WORKOUT_ID    + " INTEGER, "
                + Contract.WorkoutExerciseColumns.EXERCISE_ID   + " INTEGER, "
                + Contract.WorkoutExerciseColumns.REPS          + " TEXT"
                + ");";
        db.execSQL(SQL);

        SQL = "INSERT INTO " + Contract.WorkoutExercises.CONTENT_DIRECTORY;
    }
}
