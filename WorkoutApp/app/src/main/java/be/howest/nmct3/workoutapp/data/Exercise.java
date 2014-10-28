package be.howest.nmct3.workoutapp.data;

import java.util.List;

/**
 * Created by nielslammens on 22/10/14.
 */
public class Exercise {
    public Integer Id;
    public String Name;
    public String Description;
    public String MuscleGroup;
    public List<Integer> Reps;

    //                                      0       1       2           3         4       5
    public final String[] MuscleGroups = {"ARMS", "BACK", "CHEST", "SHOULDERS", "ABS", "LEGS"};
    public enum MuscleGroup{
        ARMS, BACK, CHEST, SHOULDERS, ABS, LEGS;
    }
}
