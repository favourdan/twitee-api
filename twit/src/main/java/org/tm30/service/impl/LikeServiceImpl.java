package org.tm30.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.tm30.domain.Like;
import org.tm30.domain.Twit;
import org.tm30.exception.AbstractPlatformException;
import org.tm30.repository.AppUserRepository;
import org.tm30.repository.LikeRepository;
import org.tm30.repository.TwitRepository;
import org.tm30.domain.AppUser;
import org.tm30.service.LikeService;
import org.tm30.util.data.CommandResult;
import org.tm30.util.data.CommandResultBuilder;


@Slf4j
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final TwitRepository twitRepository;
    private final LikeRepository likeRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public ResponseEntity<CommandResult> likeOrUnlikeTwit(Long twitId) {
        //get current user
        AppUser appUser = appUserRepository.findById(1L).orElse(null);
        Long appUserId = appUser == null ? 1L : appUser.getId();;

        Twit twit = twitRepository.findById(twitId)
                .orElseThrow(() -> new AbstractPlatformException("error.twit.service.twit.does.not.exist", "Twit does not exist", 404));

        Like savedLike = likeRepository.findByTwitIdAndAppUserId(twit, appUserId).orElse(null);

        if(savedLike == null) {
            savedLike = Like.builder()
                    .appUserId(appUserId)
                    .twitId(twit)
                    .isLiked(true)
                    .build();
        } else {
            savedLike.setIsLiked(!savedLike.getIsLiked());
        }
        likeRepository.saveAndFlush(savedLike);

        return ResponseEntity.ok().body(new CommandResultBuilder()
                        .resourceId(String.valueOf(savedLike.getId()))
                .entityId(twitId)
                        .message(savedLike.getIsLiked() ? "Liked" : "Unliked")
                .build());
    }
}
