package com.sportive.court_booking_server.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.sportive.court_booking_server.common.enums.BookingStatus;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(
    name = "bookings",
    check = @CheckConstraint(
        name = "chk_booking_total_amount",
        constraint = "total_amount >= 0"
    ),
    indexes = {
        @Index(name = "idx_booking_user_history", columnList = "user_id"),
        @Index(name = "idx_booking_facility_history", columnList = "facility_id, booking_date"),
        @Index(name = "idx_booking_date_status", columnList = "booking_date, booking_status")
    }
)
public class Booking {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_code", nullable = false, unique = true, length = 50)
    private String bookingCode;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Column(name = "customer_phone", nullable = false, length = 20)
    private String customerPhone;

    @Column(name = "booking_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus = BookingStatus.PENDING;

    @Column(name = "total_amount", nullable = false)
    private Long totalAmount = 0L;

    @Column(name = "note")
    private String note;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp()
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp()
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facility_id", referencedColumnName = "id", nullable = false)
    private Facility facility;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingDetail> details = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        details.forEach(detail -> validateDetailFacility(detail, facility));
        this.facility = facility;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<BookingDetail> getDetails() {
        return details;
    }

    public void setDetails(List<BookingDetail> details) {
        List<BookingDetail> newDetails = details == null
            ? List.of()
            : new ArrayList<>(details);
        this.details.clear();
        newDetails.forEach(this::addDetail);
    }

    public void addDetail(BookingDetail detail) {
        if (detail == null) {
            throw new IllegalArgumentException("Booking detail must not be null");
        }
        validateDetailFacility(detail, facility);
        details.add(detail);
        detail.setBooking(this);
    }

    public void removeDetail(BookingDetail detail) {
        if (details.remove(detail)) {
            detail.setBooking(null);
        }
    }

    private void validateDetailFacility(BookingDetail detail, Facility bookingFacility) {
        if (bookingFacility == null || detail.getCourt() == null || detail.getCourt().getFacility() == null) {
            return;
        }

        Facility courtFacility = detail.getCourt().getFacility();
        boolean sameFacility = bookingFacility == courtFacility
            || (bookingFacility.getId() != null && bookingFacility.getId().equals(courtFacility.getId()));

        if (!sameFacility) {
            throw new IllegalArgumentException("All courts in a booking must belong to its facility");
        }
    }
}
