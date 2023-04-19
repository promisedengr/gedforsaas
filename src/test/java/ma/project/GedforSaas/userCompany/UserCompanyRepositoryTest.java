package ma.project.GedforSaas.userCompany;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import ma.project.GedforSaas.model.User;
import ma.project.GedforSaas.model.UserCompany;
import ma.project.GedforSaas.repository.UserCompanyRepository;

@DataJpaTest	
public class UserCompanyRepositoryTest {
	
	@Autowired
	private UserCompanyRepository underTest ; 
	
	@Test
	void itReturnAlistOfUserWhengetUsersCompany () {
		//given
		Long id = 1L;
		//when
//		User user = new User();
//		underTest.save(user);
		List<UserCompany> result = underTest.findByUserId(id);
		//then 
		assertThat(result).isNotNull();
	}

}
