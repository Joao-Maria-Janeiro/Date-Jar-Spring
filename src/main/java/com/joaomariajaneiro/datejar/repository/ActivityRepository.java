package com.joaomariajaneiro.datejar.repository;

import com.joaomariajaneiro.datejar.model.Activity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {
}
