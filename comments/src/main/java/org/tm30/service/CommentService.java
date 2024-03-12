package org.tm30.service;

import org.springframework.http.ResponseEntity;
import org.tm30.data.CommentData;
import org.tm30.util.data.CommandResult;

public interface CommentService {
    ResponseEntity<CommandResult> postComment(CommentData commentData);
}
