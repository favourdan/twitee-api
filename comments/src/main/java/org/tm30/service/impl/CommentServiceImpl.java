package org.tm30.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.tm30.data.CommentData;
import org.tm30.domain.Comment;
import org.tm30.domain.Twit;
import org.tm30.exception.AbstractPlatformException;
import org.tm30.repository.CommentRepository;
import org.tm30.repository.TwitRepository;
import org.tm30.domain.AppUser;
import org.tm30.repository.AppUserRepository;
import org.tm30.service.CommentService;
import org.tm30.util.data.CommandResult;
import org.tm30.util.data.CommandResultBuilder;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final AppUserRepository appUserRepository;
    private final TwitRepository twitRepository;
    private final HttpServletRequest request;

    @Override
    public ResponseEntity<CommandResult> postComment(CommentData commentData) {
        String email = request.getHeader("user-token");
        AppUser appUser = appUserRepository.findByEmail(email).orElse(null);
        Long appUserId = appUser == null ? 1L : appUser.getId();

        if(appUser == null) {
            throw new AbstractPlatformException("error.user.not.found",
                    "User is not logged in or does not exist", 404);
        }

        Twit twit = twitRepository.findById(commentData.getTwitId())
                .orElseThrow(() -> new AbstractPlatformException("error.twit.service.twit.does.not.exist", "Twit does not exist", 404));

        Comment comment = Comment.builder()
                .commenterId(appUserId)
                .twitId(twit.getId())
                .body(commentData.getComment())
                .build();

        commentRepository.saveAndFlush(comment);

        return ResponseEntity.ok().body(new CommandResultBuilder()
                        .response("Comment saved")
                        .resourceId(String.valueOf(comment.getId()))
                        .entityId(comment.getCommenterId())
                .build());
    }
}
