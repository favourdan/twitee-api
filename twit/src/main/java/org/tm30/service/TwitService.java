package org.tm30.service;

import org.springframework.http.ResponseEntity;
import org.tm30.data.TwitData;
import org.tm30.data.TwitResponseData;
import org.tm30.util.data.CommandResult;

import java.util.List;

public interface TwitService {
    ResponseEntity<CommandResult> postATwit(TwitData twitData);

    List<TwitResponseData> retrieveTwits();

    ResponseEntity<CommandResult> deleteTwit(Long twitId);
}
