/* RICHARDS AND FAVOUR (C)2024 */
package org.tm30.util.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Getter
@Setter
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommandResult {
	private String resourceId;
	private String message;
	private String token;
	private Long entityId;
	private String response;
	private Map<String, Object> changes;

	public CommandResult(String resourceId, String message, String token, Long entityId, String response,
			Map<String, Object> changes) {
		this.resourceId = resourceId;
		this.message = message;
		this.token = token;
		this.entityId = entityId;
		this.response = response;
		this.changes = changes;
	}
}
