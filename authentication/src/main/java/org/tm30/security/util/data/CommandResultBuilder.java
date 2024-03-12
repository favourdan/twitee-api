/* RICHARDS AND FAVOUR (C)2024 */
package org.tm30.security.util.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommandResultBuilder {
	private String resourceId;
	private String message;
	private String token;
	private Long entityId;
	private String response;
	Map<String, Object> changes;

	public CommandResult build() {
		return new CommandResult(this.resourceId, this.message, this.token, this.entityId, this.response, this.changes);
	}

	public CommandResultBuilder resourceId(String resourceId) {
		this.resourceId = resourceId;
		return this;
	}

	public CommandResultBuilder message(String message) {
		this.message = message;
		return this;
	}

	public CommandResultBuilder token(String token) {
		this.token = token;
		return this;
	}

	public CommandResultBuilder entityId(Long entityId) {
		this.entityId = entityId;
		return this;
	}

	public CommandResultBuilder response(String response) {
		this.response = response;
		return this;
	}

	public CommandResultBuilder changes(Map<String, Object> changes) {
		this.changes = changes;
		return this;
	}
}
