package com.olympicweightlifting.features.programs.data;

import java.util.List;

/**
 * Created by vangor on 14/02/2018.
 */

public class ProgramWeek {
    private List<ProgramDay> days;

    public ProgramWeek() {
    }

    public ProgramWeek(List<ProgramDay> days) {
        this.days = days;
    }

    public List<ProgramDay> getDays() {
        return days;
    }
}
