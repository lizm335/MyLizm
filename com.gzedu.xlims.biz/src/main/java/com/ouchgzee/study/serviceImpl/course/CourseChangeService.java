package com.ouchgzee.study.serviceImpl.course;

import com.ouchgzee.study.dao.course.CourseLearningDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class CourseChangeService {

    @Autowired
    private CourseLearningDao courseLearningDao;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void changeRecResultScore(Map map){
        courseLearningDao.updateRecResultScore(map);
    }

}
