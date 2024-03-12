/* RICHARDS AND FAVOUR (C)2024 */
package org.tm30.security.data;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginData {
	private final String email;
	private final String password;
}
