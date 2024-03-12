package org.tm30.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tm30.domain.Twit;

@Repository
public interface TwitRepository extends JpaRepository<Twit, Long> {
}
