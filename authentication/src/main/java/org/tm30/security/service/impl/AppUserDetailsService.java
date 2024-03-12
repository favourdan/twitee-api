package org.tm30.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.tm30.security.data.AppUserDetails;
import org.tm30.security.domain.AppUser;
import org.tm30.security.repository.AppUserRepository;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
	private final AppUserRepository personRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		AppUser dbUser = personRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Not Found"));
		return new AppUserDetails(dbUser);
	}
}
