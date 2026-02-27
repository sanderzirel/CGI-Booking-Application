package com.example.backend.repository;

import com.example.backend.model.TableReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableReservationRepository extends JpaRepository<TableReservation, Long> {
    List<TableReservation> findByDateAndTime(String date, String time);
    boolean existsByDateAndTime(String date, String time);
    void deleteByDateAndTime(String date, String time);
}
