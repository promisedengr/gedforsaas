package ma.project.GedforSaas.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SpringSecurityAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			 username = ((UserDetails)principal).getUsername();
		} else {
			 username = principal.toString();
		}
		System.out.println(username);
		return Optional.ofNullable(username).filter(s -> !s.isEmpty());
	}

}
