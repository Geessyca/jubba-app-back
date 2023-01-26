package com.jubba.repository;

import com.jubba.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByIdCliente(Integer idCliente);

    void deleteAllById(Integer id);
}
