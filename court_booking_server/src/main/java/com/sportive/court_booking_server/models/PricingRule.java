package com.sportive.court_booking_server.models;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.sportive.court_booking_server.common.enums.DayType;

import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(
    name = "pricing_rules",
    check = {
        @CheckConstraint(
            name = "chk_pricing_rule_time",
            constraint = "start_time < end_time"
        ),
        @CheckConstraint(
            name = "chk_pricing_rule_price",
            constraint = "price_per_slot >= 0"
        ),
        @CheckConstraint(
            name = "chk_pricing_rule_slot_duration",
            constraint = "slot_duration_minutes > 0"
        )
    },
    indexes = @Index(
        name = "idx_pricing_rule_lookup",
        columnList = "facility_id, court_type_id, day_type, start_time, end_time"
    )
)
public class PricingRule {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "price_per_slot", nullable = false)
    private Long pricePerSlot;

    @Column(name = "slot_duration_minutes", nullable = false)
    private Integer slotDurationMinutes = 30;

    @Column(name = "day_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayType dayType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facility_id", referencedColumnName = "id", nullable = false)
    private Facility facility;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "court_type_id", referencedColumnName = "id", nullable = false)
    private CourtType courtType;

    @CreationTimestamp()
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp()
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Long getPricePerSlot() {
        return pricePerSlot;
    }

    public void setPricePerSlot(Long pricePerSlot) {
        this.pricePerSlot = pricePerSlot;
    }

    public Integer getSlotDurationMinutes() {
        return slotDurationMinutes;
    }

    public void setSlotDurationMinutes(Integer slotDurationMinutes) {
        this.slotDurationMinutes = slotDurationMinutes;
    }

    public DayType getDayType() {
        return dayType;
    }

    public void setDayType(DayType dayType) {
        this.dayType = dayType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public CourtType getCourtType() {
        return courtType;
    }

    public void setCourtType(CourtType courtType) {
        this.courtType = courtType;
    }
}
