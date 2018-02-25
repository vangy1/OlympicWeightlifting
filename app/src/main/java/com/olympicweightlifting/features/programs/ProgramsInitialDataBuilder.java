package com.olympicweightlifting.features.programs;

import com.olympicweightlifting.features.programs.data.Program;
import com.olympicweightlifting.features.programs.data.ProgramDay;
import com.olympicweightlifting.features.programs.data.ProgramExercise;
import com.olympicweightlifting.features.programs.data.ProgramWeek;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vangor on 18/02/2018.
 */

public class ProgramsInitialDataBuilder {
    public static List<Program> getInitialPrograms() {
        List<Program> programs = new ArrayList<>();

        Program olympicLifts1 = new Program(
                new ArrayList<>(Arrays.asList(
                        new ProgramWeek(new ArrayList<>(Arrays.asList(
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Clean & Jerk", 4, 4),
                                        new ProgramExercise("Clean & Jerk", 2, 2),
                                        new ProgramExercise("Snatch", 4, 4),
                                        new ProgramExercise("Snatch", 2, 2),
                                        new ProgramExercise("Pull-ups", 15, 3)
                                ))),
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Snatch Deadlift", 2, 5),
                                        new ProgramExercise("Squat", 3, 5),
                                        new ProgramExercise("Push Press", 5, 5)
                                ))),
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Snatch", 3, 3),
                                        new ProgramExercise("Snatch", 1, 1),
                                        new ProgramExercise("Clean & Jerk", 3, 3),
                                        new ProgramExercise("Clean & Jerk", 1, 2),
                                        new ProgramExercise("Pull-ups", 15, 3)
                                ))),
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Overhead Squat", 6, 4),
                                        new ProgramExercise("Front Squat", 6, 3),
                                        new ProgramExercise("Clean Pull", 3, 5)
                                )))
                        ))),
                        new ProgramWeek(new ArrayList<>(Arrays.asList(
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Snatch", 4, 4),
                                        new ProgramExercise("Snatch", 2, 2),
                                        new ProgramExercise("Clean & Jerk", 4, 4),
                                        new ProgramExercise("Clean & Jerk", 2, 2),
                                        new ProgramExercise("Pull-ups", 15, 3)
                                ))),
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Clean Deadlift", 2, 5),
                                        new ProgramExercise("Front Squat", 3, 5),
                                        new ProgramExercise("Snatch Push Press", 5, 5)
                                ))),
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Clean & Jerk", 3, 3),
                                        new ProgramExercise("Clean & Jerk", 1, 2),
                                        new ProgramExercise("Snatch", 3, 3),
                                        new ProgramExercise("Snatch", 1, 1),
                                        new ProgramExercise("Pull-ups", 15, 3)
                                ))),
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Snatch Balance", 6, 4),
                                        new ProgramExercise("Back Squat", 6, 3),
                                        new ProgramExercise("Snatch Pull", 3, 5)
                                )))
                        )))
                ))
        );

        Program olympicLifts2 = new Program(
                new ArrayList<>(Arrays.asList(
                        new ProgramWeek(new ArrayList<>(Arrays.asList(
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Snatch", 2, 3),
                                        new ProgramExercise("Snatch", 1, 2),
                                        new ProgramExercise("Clean Pull", 2, 5),
                                        new ProgramExercise("Push Press", 4, 4),
                                        new ProgramExercise("Squat", 3, 5)
                                ))),
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Snatch Pull", 2, 5),
                                        new ProgramExercise("Clean & Jerk", 2, 3),
                                        new ProgramExercise("Clean & Jerk", 1, 2),
                                        new ProgramExercise("Front Squat", 3, 3)
                                ))),
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Snatch", 2, 5),
                                        new ProgramExercise("Clean Pull", 2, 5),
                                        new ProgramExercise("Behind the Neck Jerk", 3, 5),
                                        new ProgramExercise("Squat", 3, 3)
                                )))
                        ))),
                        new ProgramWeek(new ArrayList<>(Arrays.asList(
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Snatch Pull", 2, 5),
                                        new ProgramExercise("Clean & Jerk", 2, 5),
                                        new ProgramExercise("Overhead Press", 5, 5),
                                        new ProgramExercise("Squat", 3, 3)
                                ))),
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Snatch", 2, 3),
                                        new ProgramExercise("Snatch", 1, 2),
                                        new ProgramExercise("Clean Pull", 2, 5),
                                        new ProgramExercise("Behind the Neck Jerk", 3, 5),
                                        new ProgramExercise("Front Squat", 5, 5)
                                ))),
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Snatch Pull", 3, 5),
                                        new ProgramExercise("Clean & Jerk", 2, 3),
                                        new ProgramExercise("Clean & Jerk", 1, 2),
                                        new ProgramExercise("Squat", 3, 3)
                                )))
                        )))
                ))
        );
        Program olympicLifts3 = new Program(
                new ArrayList<>(Arrays.asList(
                        new ProgramWeek(new ArrayList<>(Arrays.asList(
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Squat", 5, 3),
                                        new ProgramExercise("Clean Pull", 4, 3),
                                        new ProgramExercise("Snatch Push Press", 5, 5)
                                ))),
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Snatch", 3, 3),
                                        new ProgramExercise("Snatch", 2, 2),
                                        new ProgramExercise("Power Clean + Jerk", 3, 5)
                                ))),
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Front Squat", 3, 3),
                                        new ProgramExercise("Snatch Pull", 3, 3),
                                        new ProgramExercise("Overhead Squat", 5, 3),
                                        new ProgramExercise("Push Press", 4, 4)
                                ))),
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Clean & Jerk", 2, 5),
                                        new ProgramExercise("Clean & Jerk", 1, 3),
                                        new ProgramExercise("Power Snatch", 3, 3)
                                )))
                        ))),
                        new ProgramWeek(new ArrayList<>(Arrays.asList(
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Front Squat", 5, 3),
                                        new ProgramExercise("Snatch Pull", 3, 5),
                                        new ProgramExercise("Overhead Squat", 5, 3),
                                        new ProgramExercise("Push Press", 3, 3)
                                ))),
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Clean & Jerk", 3, 3),
                                        new ProgramExercise("Clean & Jerk", 2, 2),
                                        new ProgramExercise("Power Snatch", 3, 5)
                                ))),
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Squat", 3, 3),
                                        new ProgramExercise("Clean Pull", 2, 5),
                                        new ProgramExercise("Snatch Push Press", 4, 4)
                                ))),
                                new ProgramDay(new ArrayList<>(Arrays.asList(
                                        new ProgramExercise("Snatch", 2, 5),
                                        new ProgramExercise("Snatch", 1, 3),
                                        new ProgramExercise("Power Clean + Jerk", 3, 5)

                                )))
                        )))
                ))
        );

        olympicLifts1.setProgramTitle("Olympic Lifts 1");
        olympicLifts2.setProgramTitle("Olympic Lifts 2");
        olympicLifts3.setProgramTitle("Olympic Lifts 3");

        programs.add(olympicLifts1);
        programs.add(olympicLifts2);
        programs.add(olympicLifts3);

        return programs;

    }
}
