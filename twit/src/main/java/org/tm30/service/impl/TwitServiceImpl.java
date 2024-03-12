package org.tm30.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.tm30.data.LikeData;
import org.tm30.data.TwitData;
import org.tm30.data.TwitResponseData;
import org.tm30.domain.AppUser;
import org.tm30.domain.Like;
import org.tm30.domain.Twit;
import org.tm30.exception.AbstractPlatformException;
import org.tm30.repository.AppUserRepository;
import org.tm30.repository.LikeRepository;
import org.tm30.repository.TwitRepository;
import org.tm30.service.TwitService;
import org.tm30.util.data.CommandResult;
import org.tm30.util.data.CommandResultBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwitServiceImpl implements TwitService {
    @Value("${spring.app.gateway.url}")
    private String GATEWAY_URL;
    private final LikeRepository likeRepository;
    private final TwitRepository twitRepository;
    private final AppUserRepository appUserRepository;
    private final HttpServletRequest request;

    @Override
    public ResponseEntity<CommandResult> postATwit(TwitData twitData) {
        String email = request.getHeader("user-token");
        AppUser appUser = appUserRepository.findByEmail(email).orElse(null);

        if(appUser == null) {
            throw new AbstractPlatformException("error.user.not.found",
                    "User is not logged in or does not exist", 404);
        }

        Twit twit = Twit.builder().body(twitData.getBody())
                .title(twitData.getTitle())
                .appUserId(appUser.getId())
                .build();

        twitRepository.saveAndFlush(twit);
        return ResponseEntity.ok().body(CommandResult.builder()
                        .entityId(twit.getId())
                        .message("Twit posted")
                .build());
    }

    @Override
    public List<TwitResponseData> retrieveTwits() {
        Collection<Twit> twits = twitRepository.findAll();
        log.info("Retrieving Twits' Likes");
        twits.forEach(twit -> twit.getLikes().forEach(System.out::println));

        return twits.stream().map(twit -> {
            Set<Like> likes = likeRepository.findByTwitId(twit);
            return TwitResponseData.builder()
                    .twitId(twit.getId())
                    .body(twit.getBody())
                    .title(twit.getTitle())
                    .appUserId(twit.getAppUserId())
                    .appUsername(getAppUsername(twit.getAppUserId()))
                    .likeData(likes.stream().map(this::convertToLikeData).collect(Collectors.toSet()))
                    .build();

        }).collect(Collectors.toList());
    }

    private String getAppUsername(Long appUserId) {
        AppUser appUser = this.appUserRepository.findById(appUserId).orElse(null);
        return (appUser == null) ? "N/A" : appUser.getId() + "_" + appUser.getUsername();
    }

    private LikeData convertToLikeData(Like like) {
        String username = getAppUsername(like.getAppUserId());
        return LikeData.builder()
                .username(username)
                .appUserId(like.getAppUserId())
                .twitId(like.getTwitId().getId())
                .build();
    }

    @Override
    public ResponseEntity<CommandResult> deleteTwit(Long twitId) {
        String email = request.getHeader("user-token");
        AppUser appUser = appUserRepository.findByEmail(email).orElse(null);

        Twit twit = twitRepository.findById(twitId)
                .orElseThrow(() -> new AbstractPlatformException("error.twit.service.twit.does.not.exist", "Twit does not exist", 404));

        if(appUser != null && !appUser.getId().equals(twit.getAppUserId())) {
            throw new AbstractPlatformException("error.you.do.not.have.the.right.to.delete.another's.twit",
                    "Unauthorized to delete twit", 403);
        }

        log.info("Deleting twit");
        return ResponseEntity.ok().body(new CommandResultBuilder()
                        .entityId(twitId)
                        .message("Twit deleted")
                .build());
    }
}
