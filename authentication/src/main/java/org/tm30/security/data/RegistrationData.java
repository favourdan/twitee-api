/* RICHARDS AND FAVOUR (C)2024 */
package org.tm30.security.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationData {
	@NotBlank(message = "Field for lastname must not be empty.")
	@NotNull(message = "Lastname required.") private String name;

	@Email(message = "Please enter a valid email.")
	@NotNull(message = "Email required.") private String email;

	@NotNull @Pattern(regexp = "(?=^.{8,}$)(?=.*\\d)(?=.*[!@#$%^&*]+)(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "Password must be greater than or equal to 8, "
			+ "must contain one or more uppercase characters, "
			+ "lowercase characters, numeric values and special characters ")
	private String password;

}
