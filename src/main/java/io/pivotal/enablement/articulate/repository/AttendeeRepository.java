package io.pivotal.enablement.articulate.repository;

import io.pivotal.enablement.articulate.model.Attendee;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
	

}
