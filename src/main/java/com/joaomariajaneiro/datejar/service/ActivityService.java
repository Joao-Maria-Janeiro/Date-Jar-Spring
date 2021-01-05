package com.joaomariajaneiro.datejar.service;

import com.joaomariajaneiro.datejar.model.Activity;
import com.joaomariajaneiro.datejar.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    @Autowired
    ActivityRepository activityRepository;

    public List<Activity> getActivitiesOfCategory(int categoryId) {
        return activityRepository.getActivitiesOfCategory(categoryId);
    }

    public int save(Activity activity, int categoryId) {
        return activityRepository.save(activity, categoryId);
    }

    public int update(String newActivityName, String oldActivityName, int categoryId,
                      String username) {
        return activityRepository.update(newActivityName, oldActivityName, categoryId, username);
    }

    public int delete(Long activityId, Long categoryId,
                      String username, int categoryType) {
        return activityRepository.delete(activityId, categoryId,
                username, categoryType);
    }

}
