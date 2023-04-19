package ma.project.GedforSaas.utils;

import java.util.Calendar;
import java.util.TimeZone;

import org.hibernate.envers.RevisionListener;
import org.springframework.beans.factory.annotation.Autowired;

import ma.project.GedforSaas.model.UserRevEntity;
import ma.project.GedforSaas.security.SpringSecurityAuditorAware;

public class UserRevisionListener implements RevisionListener {

	@Autowired
	private SpringSecurityAuditorAware springSecurityAuditorAware;

	/**
	 * @see org.hibernate.envers.RevisionListener#newRevision(java.lang.Object)
	 */
	@Override
	public void newRevision(Object userRevision) {

		String authenticatedUser = null;

		if (springSecurityAuditorAware.getCurrentAuditor().isPresent()) {
			authenticatedUser = springSecurityAuditorAware.getCurrentAuditor().get();
		}

		UserRevEntity userRevEntity = (UserRevEntity) userRevision;

		userRevEntity.setUserName(authenticatedUser);
		userRevEntity.setDate(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime());

	}
}