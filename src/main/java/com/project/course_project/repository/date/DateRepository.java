package com.project.course_project.repository.date;

import com.project.course_project.entity.date.DateTime;
import org.springframework.data.repository.CrudRepository;
/*
 *
 * Интерфейс для работы в БД с сущностью DateTime
 *
 */
public interface DateRepository extends CrudRepository<DateTime, Long> {
}
