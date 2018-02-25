package com.olympicweightlifting.features.lifts;


import android.content.Context;

import com.olympicweightlifting.R;

import java.util.ArrayList;
import java.util.List;

public class LiftsContentDataBuilder {
    private final Context context;

    public LiftsContentDataBuilder(Context context) {
        this.context = context;
    }

    public List<LiftsFragmentData> getContentDataSnatch() {
        List<LiftsFragmentData> activityConstructionData = new ArrayList<>();
        activityConstructionData.add(new LiftsFragmentData(getSnatchTechniqueDataFromResources(), context.getString(R.string.floating_button_video_url_snatch)));
        activityConstructionData.add(new LiftsFragmentData(getSnatchMistakesDataFromResources()));
        activityConstructionData.add(new LiftsFragmentData(getSnatchExercisesDataFromResources()));
        return activityConstructionData;
    }

    private List<LiftsContentData> getSnatchTechniqueDataFromResources() {
        List<LiftsContentData> techniqueDataList = new ArrayList<>();
        techniqueDataList.add(new LiftsContentData(context.getString(R.string.starting_position_title), context.getString(R.string.snatch_starting_position)));
        techniqueDataList.add(new LiftsContentData(context.getString(R.string.first_step_title), context.getString(R.string.snatch_first_step)));
        techniqueDataList.add(new LiftsContentData(context.getString(R.string.second_step_title), context.getString(R.string.snatch_second_step)));
        techniqueDataList.add(new LiftsContentData(context.getString(R.string.third_step_title), context.getString(R.string.snatch_third_step)));
        return techniqueDataList;
    }

    private List<LiftsContentData> getSnatchMistakesDataFromResources() {
        List<LiftsContentData> mistakesDataList = new ArrayList<>();
        mistakesDataList.add(new LiftsContentData(context.getString(R.string.butt_high_title_snatch), context.getString(R.string.butt_high_description_snatch)));
        mistakesDataList.add(new LiftsContentData(context.getString(R.string.bar_too_far_title_snatch), context.getString(R.string.bar_too_far_description_snatch)));
        mistakesDataList.add(new LiftsContentData(context.getString(R.string.back_angle_title_snatch), context.getString(R.string.back_angle_description_snatch)));
        mistakesDataList.add(new LiftsContentData(context.getString(R.string.bending_early_title_snatch), context.getString(R.string.bending_early_description_snatch)));
        mistakesDataList.add(new LiftsContentData(context.getString(R.string.jumping_forward_title_snatch), context.getString(R.string.jumping_forward_description_snatch)));
        mistakesDataList.add(new LiftsContentData(context.getString(R.string.swinging_bar_title_snatch), context.getString(R.string.swinging_bar_description_snatch)));
        return mistakesDataList;
    }

    private List<LiftsContentData> getSnatchExercisesDataFromResources() {
        List<LiftsContentData> exercisesDataList = new ArrayList<>();
        exercisesDataList.add(new LiftsContentData(context.getString(R.string.power_snatch_title), context.getString(R.string.power_snatch_description), context.getString(R.string.power_snatch_video_url)));
        exercisesDataList.add(new LiftsContentData(context.getString(R.string.hang_snatch_title), context.getString(R.string.hang_snatch_description), context.getString(R.string.hang_snatch_video_url)));
        exercisesDataList.add(new LiftsContentData(context.getString(R.string.snatch_press_title), context.getString(R.string.snatch_press_description), context.getString(R.string.snatch_press_video_url)));
        exercisesDataList.add(new LiftsContentData(context.getString(R.string.overhead_squat_title), context.getString(R.string.overhead_squat_description), context.getString(R.string.overhead_squat_video_url)));
        exercisesDataList.add(new LiftsContentData(context.getString(R.string.snatch_deadlift_title), context.getString(R.string.snatch_deadlift_description), context.getString(R.string.snatch_deadlift_video_url)));
        exercisesDataList.add(new LiftsContentData(context.getString(R.string.snatch_pull_title), context.getString(R.string.snatch_pull_description), context.getString(R.string.snatch_pull_video_url)));
        return exercisesDataList;
    }

    public List<LiftsFragmentData> getContentDataCaj() {
        List<LiftsFragmentData> activityConstructionData = new ArrayList<>();
        activityConstructionData.add(new LiftsFragmentData(getCajTechniqueDataFromResources(), context.getString(R.string.floating_button_video_url_caj)));
        activityConstructionData.add(new LiftsFragmentData(getCajMistakesDataFromResources()));
        activityConstructionData.add(new LiftsFragmentData(getCajExercisesDataFromResources()));
        return activityConstructionData;
    }

    private List<LiftsContentData> getCajTechniqueDataFromResources() {
        List<LiftsContentData> techniqueDataList = new ArrayList<>();
        techniqueDataList.add(new LiftsContentData(context.getString(R.string.starting_position_title), context.getString(R.string.starting_position_description_caj)));
        techniqueDataList.add(new LiftsContentData(context.getString(R.string.first_step_title), context.getString(R.string.first_step_description_caj)));
        techniqueDataList.add(new LiftsContentData(context.getString(R.string.second_step_title), context.getString(R.string.second_step_description_caj)));
        techniqueDataList.add(new LiftsContentData(context.getString(R.string.third_step_title), context.getString(R.string.third_step_description_caj)));
        techniqueDataList.add(new LiftsContentData(context.getString(R.string.fourth_step_title), context.getString(R.string.fourth_step_description_caj)));
        techniqueDataList.add(new LiftsContentData(context.getString(R.string.fifth_step_title), context.getString(R.string.fifth_step_description_caj)));
        return techniqueDataList;
    }

    private List<LiftsContentData> getCajMistakesDataFromResources() {
        List<LiftsContentData> mistakesDataList = new ArrayList<>();
        mistakesDataList.add(new LiftsContentData(context.getString(R.string.bar_too_far_title_caj), context.getString(R.string.bar_too_far_description_caj)));
        mistakesDataList.add(new LiftsContentData(context.getString(R.string.swinging_bar_title_caj), context.getString(R.string.swinging_bar_description_caj)));
        mistakesDataList.add(new LiftsContentData(context.getString(R.string.pushing_away_title_caj), context.getString(R.string.pushing_away_description_caj)));
        mistakesDataList.add(new LiftsContentData(context.getString(R.string.going_forward_title_caj), context.getString(R.string.going_forward_description_caj)));
        return mistakesDataList;
    }

    private List<LiftsContentData> getCajExercisesDataFromResources() {
        List<LiftsContentData> exercisesDataList = new ArrayList<>();
        exercisesDataList.add(new LiftsContentData(context.getString(R.string.power_clean_title), context.getString(R.string.power_clean_description), context.getString(R.string.power_clean_video_url)));
        exercisesDataList.add(new LiftsContentData(context.getString(R.string.hang_clean_title), context.getString(R.string.hang_clean_description), context.getString(R.string.hang_clean_video_url)));
        exercisesDataList.add(new LiftsContentData(context.getString(R.string.overhead_press_title), context.getString(R.string.overhead_press_description), context.getString(R.string.overhead_press_video_url)));
        exercisesDataList.add(new LiftsContentData(context.getString(R.string.behind_the_neck_press_title), context.getString(R.string.behind_the_neck_press_description), context.getString(R.string.behind_the_neck_press_video_url)));
        exercisesDataList.add(new LiftsContentData(context.getString(R.string.push_press_title), context.getString(R.string.push_press_description), context.getString(R.string.push_press_video_url)));
        exercisesDataList.add(new LiftsContentData(context.getString(R.string.clean_deadlift_title), context.getString(R.string.clean_deadlift_description), context.getString(R.string.clean_deadlift_video_url)));
        exercisesDataList.add(new LiftsContentData(context.getString(R.string.clean_pull_title), context.getString(R.string.clean_pull_description), context.getString(R.string.clean_pull_video_url)));
        return exercisesDataList;
    }
}
