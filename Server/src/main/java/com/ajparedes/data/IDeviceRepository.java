package com.ajparedes.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ajparedes.model.Device;


public interface IDeviceRepository extends JpaRepository<Device, String> {

	//@Query("SELECT d FROM device WHERE d.username = :username")
	//public Optional<Device> findByUsername(@Param("username") String username);
}
