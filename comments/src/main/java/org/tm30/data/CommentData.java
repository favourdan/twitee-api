package org.tm30.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentData {

	private Long appUserId;
	@NotNull
	private Long twitId;
	@NotNull(message = "Please provide a twit message body.")
	@NotBlank(message = "Please provide a twit message body.")
	private String comment;
}
