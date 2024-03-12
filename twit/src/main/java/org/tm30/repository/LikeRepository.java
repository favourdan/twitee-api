package org.tm30.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tm30.domain.Like;
import org.tm30.domain.Twit;

import java.util.Optional;
import java.util.Set;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByTwitIdAndAppUserId(Twit twit, Long appUserId);
    Set<Like> findByTwitId(Twit twit);
}
