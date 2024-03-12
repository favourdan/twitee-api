package org.tm30.security.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tm30.security.enums.Role;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "m_appuser")
public class AppUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@NotNull(message = "Name cannot be missing or empty") @Column(nullable = false, length = 50)
	private String name;
	@Column(name = "username")
	private String username;
	@Email(message = "Email must be valid!")
	@Column(unique = true, nullable = false)
	private String email;
	@Column(nullable = false)
	private String password;
	private Boolean isVerified = false;
	private Boolean isActive = false;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate = new Date();

	@Column(name = "lastmodified_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate = new Date();


	@PrePersist
	public void onCreationTimestamp() {
		createdDate = new Date();
		lastModifiedDate = new Date();
	}

	@PreUpdate
	public void onUpdateTimestamp() {
		lastModifiedDate = new Date();
	}
}
