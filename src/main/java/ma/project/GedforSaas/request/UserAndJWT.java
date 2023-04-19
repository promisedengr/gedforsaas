package ma.project.GedforSaas.request;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.project.GedforSaas.model.User;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAndJWT {

	private String JWT;

	private User user;

}
