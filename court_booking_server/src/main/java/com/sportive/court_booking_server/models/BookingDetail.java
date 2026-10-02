package com.sportive.court_booking_server.models;

import java.time.LocalTime;

import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(
    name = "booking_details",
    check = {
        @CheckConstraint(
            name = "chk_booking_detail_time",
            constraint = "start_time < end_time"
        ),
        @CheckConstraint(
            name = "chk_booking_detail_sub_total",
            constraint = "sub_total >= 0"
        )
    },
    indexes = {
        @Index(name = "idx_booking_detail_booking", columnList = "booking_id"),
        @Index(
            name = "idx_booking_detail_court_time",
            columnList = "court_id, start_time, end_time, booking_id"
        )
    }
)
public class BookingDetail {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", referencedColumnName = "id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "court_id", referencedColumnName = "id", nullable = false)
    private Court court;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "sub_total", nullable = false)
    private Long subTotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;
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

    public Long getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Long subTotal) {
        this.subTotal = subTotal;
    }

    @PrePersist
    @PreUpdate
    private void validateFacilityConsistency() {
        if (booking == null || booking.getFacility() == null || court == null || court.getFacility() == null) {
            return;
        }

        Facility bookingFacility = booking.getFacility();
        Facility courtFacility = court.getFacility();
        boolean sameFacility = bookingFacility == courtFacility
            || (bookingFacility.getId() != null && bookingFacility.getId().equals(courtFacility.getId()));

        if (!sameFacility) {
            throw new IllegalStateException("The booked court must belong to the booking facility");
        }
    }
}
