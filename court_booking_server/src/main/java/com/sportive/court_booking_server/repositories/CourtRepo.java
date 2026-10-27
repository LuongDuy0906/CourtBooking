package com.sportive.court_booking_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sportive.court_booking_server.models.Court;

public interface CourtRepo extends JpaRepository<Court, Long>{

}
