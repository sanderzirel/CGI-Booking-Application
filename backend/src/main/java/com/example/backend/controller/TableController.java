package com.example.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.backend.model.TableReservation;
import com.example.backend.repository.TableReservationRepository;

@RestController
@RequestMapping("/api")
public class TableController {
    @GetMapping("/tables/suggest")
    public List<Map<String, Object>> suggestTables(
        @RequestParam String date,
        @RequestParam String time,
        @RequestParam int peopleCount,
        @RequestParam(required = false) String preference) {
        
        List<Map<String, Object>> allTables = getTables(date, time);
        return allTables.stream()
        .filter(table -> !((Boolean) table.get("reserved")))
        .filter(table -> {
            Integer size = (Integer) table.get("size");
            return size >= peopleCount; 
        })
        .filter(table -> {
            if (preference == null || preference.isEmpty()) return true;
            String zone = (String) table.get("zone");
            return matchesPreference(zone, preference);
        })
        .sorted((t1, t2) -> {
            Integer size1 = (Integer) t1.get("size");
            Integer size2 = (Integer) t2.get("size");
            return size1.compareTo(size2);
        })
        .collect(Collectors.toList());
    }

    private boolean matchesPreference(String zone, String preference) {
        return switch(preference.toLowerCase()) {
            case "couch area", "quiet area" -> zone.equals("inside") || zone.equals("private");
            case "window seat" -> zone.equals("outside");
            default -> true;
        };
    }



    @Autowired
    private TableReservationRepository reservationRepository;

    private static final List<Map<String, Object>> TABLE_DEFINITIONS = List.of(
        Map.of("id", 1, "size", 2, "zone", "inside"),
        Map.of("id", 2, "size", 2, "zone", "inside"),
        Map.of("id", 3, "size", 4, "zone", "inside"),
        Map.of("id", 4, "size", 4, "zone", "inside"),
        Map.of("id", 5, "size", 6, "zone", "inside"),
        Map.of("id", 6, "size", 2, "zone", "outside"),
        Map.of("id", 7, "size", 4, "zone", "outside"),
        Map.of("id", 8, "size", 2, "zone", "outside"),
        Map.of("id", 9, "size", 4, "zone", "outside"),
        Map.of("id", 10, "size", 6, "zone", "outside"),
        Map.of("id", 11, "size", 20, "zone", "private"),
        Map.of("id", 12, "size", 15, "zone", "private")
    );

    @GetMapping("/tables")
    public List<Map<String, Object>> getTables(
        @RequestParam(required = false) String date,
        @RequestParam(required = false) String time) {
        
        if (date == null) date = "default";
        if (time == null) time = "default";

        
        boolean reservationsExist = reservationRepository.existsByDateAndTime(date, time);

        if (!reservationsExist) {
            generateAndSaveReservations(date, time);
        }

        List<TableReservation> reservations = reservationRepository.findByDateAndTime(date, time);

        Map<Integer, Boolean> reservedMap = new java.util.HashMap<>();
        for (TableReservation res : reservations) {
            reservedMap.put(res.getTableId(), res.getReserved());
        }

        List<Map<String, Object>> tables = new ArrayList<>();
        for (Map<String, Object> tableDef : TABLE_DEFINITIONS) {
            Integer tableId = (Integer) tableDef.get("id");
            Boolean reserved = reservedMap.getOrDefault(tableId, false);
            
            Map<String, Object> tableWithReservation = new java.util.HashMap<>(tableDef);
            tableWithReservation.put("reserved", reserved);
            tables.add(tableWithReservation);
        }

        return tables;
    }

    private void generateAndSaveReservations(String date, String time) {
        for (Map<String, Object> tableDef : TABLE_DEFINITIONS) {
            Integer tableId = (Integer) tableDef.get("id");
            boolean reserved = isTableReserved(tableId, date, time);
            
            TableReservation reservation = new TableReservation(tableId, date, time, reserved);
            reservationRepository.save(reservation);
        }
    }

    private boolean isTableReserved(Integer tableId, String date, String time) {
        int hash = (tableId + "_" + date + "_" + time).hashCode();
        return Math.abs(hash % 100) < 30;
    }
}
