package com.sportive.court_booking_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sportive.court_booking_server.models.Profile;

public interface ProfileRepo extends JpaRepository<Profile, Long>{

}
