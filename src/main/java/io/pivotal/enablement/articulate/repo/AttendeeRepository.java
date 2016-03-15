package io.pivotal.enablement.articulate.repo;

import io.pivotal.enablement.articulate.server.model.Attendee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
	
    //@RestResource(exported = false)
	//Page<Attendee> findAll(Pageable pageable);

    Attendee findById(Long id);

    Page<Attendee> findByFirstNameIgnoreCase(@Param("q") String firstName, Pageable pageable);

    Page<Attendee> findByFirstNameContainsIgnoreCase(@Param("q") String firstName, Pageable pageable);

    Page<Attendee> findByStateIgnoreCase(@Param("q") String state, Pageable pageable);

    Page<Attendee> findByZipCode(@Param("q") String postalCode, Pageable pageable);

}
