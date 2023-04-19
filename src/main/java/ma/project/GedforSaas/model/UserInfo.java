package ma.project.GedforSaas.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo extends User {

	@JsonIgnore
	private String password;

	public UserInfo(Long id, String firstName, String lastName, String email, String password, Date birthDate,
			String workspace, boolean twoFactorAuth, String signature, String certificat, boolean active,
			boolean confirmed, Timestamp dateCreated, Timestamp dateModified, String phoneNumber,
			List<ConfirmationToken> confirmationTokens, List<Company> companys, List<Permission> permissions, Plan plan,
			Role role) {
		super(id, firstName, lastName, email, birthDate, workspace, twoFactorAuth, signature, certificat, active,
				confirmed, phoneNumber, permissions, plan, role);
		// TODO Auto-generated constructor stub
	}

	public UserInfo(Long id, String firstName, String lastName, String email, Date birthDate, String workspace,
			boolean twoFactorAuth, String signature, String certificat, boolean active, boolean confirmed,
			String phoneNumber, List<Permission> permissions, Plan plan, Role role) {

		super(id, firstName, lastName, email, birthDate, workspace, twoFactorAuth, signature, certificat, active,
				confirmed, phoneNumber, permissions, plan, role);

	}
}
