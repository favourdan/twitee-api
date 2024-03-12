/* RICHARDS AND FAVOUR (C)2024 */
package org.tm30.security.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WelcomeRequestData extends EmailRequestData {
	private String displayName;
	private String activateUrl;
}
