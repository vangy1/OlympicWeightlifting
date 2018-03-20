package com.olympicweightlifting.features.programs.data;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@IgnoreExtraProperties
public class Program {
    @Exclude
    private String documentId;
    private Date dateAdded;

    private String programTitle;
    private List<ProgramWeek> weeks = new ArrayList<>();


    public Program() {
    }

    public Program(List<ProgramWeek> weeks) {
        this.weeks = weeks;
    }

    public <T extends Program> T withId(String documentId) {
        this.documentId = documentId;
        return (T) this;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public boolean validateObject() {
        return getProgramTitle() != null &&
                getWeeks() != null;
    }

    public String getProgramTitle() {
        return programTitle;
    }

    public List<ProgramWeek> getWeeks() {
        return weeks;
    }

    public void setProgramTitle(String programTitle) {
        this.programTitle = programTitle;
    }

    public void setDateAdded() {
        this.dateAdded = new Date();
    }

    public void cleanOfEmptyDaysAndWeeks() {
        List<ProgramWeek> weeks = getWeeks();
        for (int i = 0; i < weeks.size(); i++) {
            List<ProgramDay> days = weeks.get(i).getDays();
            for (int j = 0; j < days.size(); j++) {
                if (days.get(j).getExercises().size() == 0) {
                    days.remove(j);
                    j--;
                }
            }
            if (days.size() == 0) {
                weeks.remove(i);
                i--;
            }
        }
        System.out.println(weeks.size());
    }

    public boolean hasAtLeastOneExercise() {
        for (ProgramWeek programWeek : getWeeks()) {
            for (ProgramDay programDay : programWeek.getDays()) {
                for (ProgramExercise userExercise : programDay.getExercises()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addInitialWeek() {
        weeks.add(new ProgramWeek(new ArrayList<>(Collections.singletonList(new ProgramDay()))));
    }
}
