package org.tm30.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tm30.data.CommentData;
import org.tm30.service.CommentService;
import org.tm30.util.data.CommandResult;

@Tag(name = "Comments")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/comments")
@CrossOrigin
public class CommentsController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommandResult> postAComment(@RequestBody CommentData commentData) {
        return this.commentService.postComment(commentData);
    }
}
