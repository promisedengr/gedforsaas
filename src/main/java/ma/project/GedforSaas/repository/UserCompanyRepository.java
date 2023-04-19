package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.User;
import ma.project.GedforSaas.model.UserCompany;

import java.util.List;

@Repository
public interface UserCompanyRepository extends JpaRepository<UserCompany, Long> {
    List<UserCompany> findByUserId(Long id);
    List<UserCompany> findByCompanyId(Long id);
    List<UserCompany> findByRoleName(String name);
    List<UserCompany> deleteAllByUserId(Long userId);

    @Query("SELECT u.user FROM UserCompany u WHERE u.company.id = :id")
    List<User> getUsersCompany(Long id);

    @Query("SELECT u.company FROM UserCompany u WHERE u.user.email = :email")
    List<Company> getCompanyOfUser(String email);
}
