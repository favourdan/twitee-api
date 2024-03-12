package org.tm30.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tm30.data.TwitData;
import org.tm30.data.TwitResponseData;
import org.tm30.service.LikeService;
import org.tm30.service.TwitService;
import org.tm30.util.data.CommandResult;

import java.util.List;

@Tag(name = "Comments")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/twits")
@CrossOrigin
public class TwitController {
    private final TwitService twitService;
    private final LikeService likeService;


    @PostMapping
    public ResponseEntity<CommandResult> postATwit(@RequestBody TwitData twitData) {
        return this.twitService.postATwit(twitData);
    }

    @GetMapping
    //can be paginated
    public List<TwitResponseData> retrieveTwits() {
        return twitService.retrieveTwits();
    }

    @DeleteMapping("/{twitId}")
    public ResponseEntity<CommandResult> deleteTwit(@PathVariable Long twitId) {
        return twitService.deleteTwit(twitId);
    }


    @PostMapping("/{twitId}/like")
    public ResponseEntity<CommandResult> likeOrUnlikeComment(@PathVariable Long twitId) {
        return this.likeService.likeOrUnlikeTwit(twitId);
    }
}
