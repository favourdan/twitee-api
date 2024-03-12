package org.tm30.service;

import org.springframework.http.ResponseEntity;
import org.tm30.util.data.CommandResult;

public interface LikeService {
    ResponseEntity<CommandResult> likeOrUnlikeTwit(Long twitId);
}
