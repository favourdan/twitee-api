/* RICHARDS AND FAVOUR (C)2024 */
package org.tm30.security.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_token")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "token", length = 500)
	private String token;

	@Column(name = "start_time")
	private Long startTime;

	@Column(name = "expiration_time")
	private Long expirationTime;

	@Column(name = "appuser_id", unique = true)
	// This references appuser id on t_appuser
	private Long appUserId;
}
