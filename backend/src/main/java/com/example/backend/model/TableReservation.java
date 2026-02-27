package com.example.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "table_reservations", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tableId", "date", "time"})
})
public class TableReservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer tableId;
    
    @Column(nullable = false)
    private String date;
    
    @Column(nullable = false)
    private String time;
    
    @Column(nullable = false)
    private Boolean reserved;
    
    public TableReservation() {}
    
    public TableReservation(Integer tableId, String date, String time, Boolean reserved) {
        this.tableId = tableId;
        this.date = date;
        this.time = time;
        this.reserved = reserved;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getTableId() {
        return tableId;
    }
    
    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public Boolean getReserved() {
        return reserved;
    }
    
    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }
}
