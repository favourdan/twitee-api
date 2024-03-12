package org.tm30.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tm30.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
