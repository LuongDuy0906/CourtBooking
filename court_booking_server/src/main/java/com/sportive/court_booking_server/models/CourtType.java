package com.sportive.court_booking_server.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.sportive.court_booking_server.common.enums.SportType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor 
@NoArgsConstructor 
@Table(name = "court_types")
public class CourtType {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sport_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SportType code;

    @OneToMany(mappedBy = "courtType", fetch = FetchType.LAZY)
    private List<Court> courts = new ArrayList<>();

    @Column(name = "slot_duration_minutes", nullable = false)
    private Integer slotDurationMinute = 30;

    @OneToMany(mappedBy = "courtType", fetch = FetchType.LAZY)
    private List<PricingRule> pricingRules = new ArrayList<>();

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

    public SportType getCode() {
        return code;
    }

    public void setCode(SportType code) {
        this.code = code;
    }

    public List<Court> getCourts() {
        return courts;
    }

    public void setCourts(List<Court> courts) {
        this.courts = courts;
    }

    public Integer getSlotDurationMinute() {
        return slotDurationMinute;
    }

    public void setSlotDurationMinute(Integer slotDurationMinute) {
        this.slotDurationMinute = slotDurationMinute;
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

    public List<PricingRule> getPricingRules() {
        return pricingRules;
    }

    public void setPricingRules(List<PricingRule> pricingRules) {
        this.pricingRules = pricingRules;
    }
}
