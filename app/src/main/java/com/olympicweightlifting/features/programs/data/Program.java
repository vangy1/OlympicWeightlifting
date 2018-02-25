package com.olympicweightlifting.features.programs.data;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.Date;
import java.util.List;

@IgnoreExtraProperties
public class Program {
    @Exclude
    private String documentId;
    private Date dateAdded;

    private String programTitle;
    private List<ProgramWeek> weeks;


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
}
