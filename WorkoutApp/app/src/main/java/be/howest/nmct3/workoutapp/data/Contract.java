package be.howest.nmct3.workoutapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by nielslammens on 22/10/14.
 */
public class Contract {

    public static final String AUTHORITY = "be.howest.nmct3.workoutapp";
    public static final String ACCOUNT_TYPE = "be.howest.nmct3.workoutapp.account";

    public interface WorkoutColumns extends BaseColumns{
        public static final String NAME = "name";
        public static final String ISPAID = "ispaid";
    }

    public static final class Workouts implements WorkoutColumns{
        public static final String CONTENT_DIRECTORY = "workouts";
        public static final String DEFAULT_SORT_ORDER = NAME + " ASC";
        public static final int WORKOUT_ID_PATH_POSITION = 1;
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.howest.workout";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.howest.workout";
        public static final String ITEM_CONTENT_PATH = "/" + CONTENT_DIRECTORY +"/";
        public static final Uri ITEM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + ITEM_CONTENT_PATH);
        public static final String CONTENT_PATH = "/" + CONTENT_DIRECTORY;
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + CONTENT_PATH);
    }

    public interface WorkoutExerciseColumns extends BaseColumns{
        public static final String WORKOUT_ID = "workout_id";
        public static final String EXERCISE_ID = "exercise_id";
        public static final String REPS = "reps";

    }

    public static final class WorkoutExercises implements WorkoutExerciseColumns{
        public static final String CONTENT_DIRECTORY = "workoutexercises";
        public static final String DEFAULT_SORT_ORDER = WORKOUT_ID + " ASC";
        public static final int WORKOUTEXERCISE_ID_PATH_POSITION = 1;
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.howest.workoutexercise";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.howest.workoutexercise";
        public static final String ITEM_CONTENT_PATH = "/" + CONTENT_DIRECTORY +"/";
        public static final Uri ITEM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + ITEM_CONTENT_PATH);
        public static final String CONTENT_PATH = "/" + CONTENT_DIRECTORY;
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + CONTENT_PATH);
    }


    public interface ExerciseColumns extends BaseColumns{
        public static final String EXERCISE_NAME = "name";
        public static final String MUSCLE_GROUP = "muscle_group";
        public static final String TARGET = "target";
        public static final String DESCRIPTION = "description";
        public static final String IMAGE_NAME = "image_name";
    }

    public static final class Exercises implements ExerciseColumns{
        public static final String CONTENT_DIRECTORY = "exercises";
        public static final String DEFAULT_SORT_ORDER = EXERCISE_NAME + " ASC";
        public static final int EXERCISE_ID_PATH_POSITION = 1;
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.howest.exercise";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.howest.exercise";
        public static final String ITEM_CONTENT_PATH = "/" + CONTENT_DIRECTORY +"/";
        public static final Uri ITEM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + ITEM_CONTENT_PATH);
        public static final String CONTENT_PATH = "/" + CONTENT_DIRECTORY;
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + CONTENT_PATH);
    }

    public interface PlannersColumns extends BaseColumns {
        public static final String WORKOUT_ID = "workout_id";
        public static final String WO_DATE = "wo_date";
    }

    public static final class Planners implements PlannersColumns {
        public static final String CONTENT_DIRECTORY = "planners";
        public static final String DEFAULT_SORT_ORDER = WO_DATE + " ASC";
        public static final int PLANNER_ID_PATH_POSITION = 1;
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.howest.planner";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.howest.planners";
        public static final String ITEM_CONTENT_PATH = "/" + CONTENT_DIRECTORY +"/";
        public static final Uri ITEM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + ITEM_CONTENT_PATH);
        public static final String CONTENT_PATH = "/" + CONTENT_DIRECTORY;
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + CONTENT_PATH);
    }

}
