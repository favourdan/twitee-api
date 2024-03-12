package org.tm30.security.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.tm30.security.domain.AppUser;

import java.util.Collection;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDetails implements UserDetails {

	private AppUser appUser;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		 return appUser.getRole().getGrantedAuthorities();
	}

	@Override
	public String getPassword() {
		return appUser.getPassword();
	}

	@Override
	public String getUsername() {
		return appUser.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		Boolean isActive = appUser.getIsActive();
		return isActive != null && isActive;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		Boolean isVerified = appUser.getIsVerified();
		return isVerified != null && isVerified;
	}
}
