package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

import com.example.backend.model.TableReservation;
import com.example.backend.repository.TableReservationRepository;


@RestController
@RequestMapping("/api/reservations")
public class TableReservationController {

    @Autowired
    private TableReservationRepository reservationRepository;

    @PutMapping
    public ResponseEntity<TableReservation> reserveTable(@RequestBody TableReservation request) {
        Integer tableId = request.getTableId();
        String date = request.getDate();
        String time = request.getTime();
        
        Optional<TableReservation> existingReservation = reservationRepository.findByTableIdAndDateAndTime(tableId, date, time);

        if (existingReservation.isPresent()) {
            TableReservation reservation = existingReservation.get();

            if (reservation.getReserved()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(reservation);
            }

            reservation.setReserved(true);
            reservation.setGuestName(request.getGuestName());
            reservation.setGuestEmail(request.getGuestEmail());
            reservation.setGuestPhone(request.getGuestPhone());
            reservationRepository.save(reservation);
            return ResponseEntity.ok(reservation);

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
