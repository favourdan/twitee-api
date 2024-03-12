package org.tm30.data;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TwitResponseData {
	private Long twitId;
	private Long appUserId;
	private String appUsername;
	private String body;
	private String title;
	private Set<LikeData> likeData;
}
