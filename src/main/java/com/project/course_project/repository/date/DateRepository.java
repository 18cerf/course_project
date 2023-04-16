package com.project.course_project.repository.date;

import com.project.course_project.entity.user.DateTime;
import org.springframework.data.repository.CrudRepository;

public interface DateRepository extends CrudRepository<DateTime, Long> {
}
