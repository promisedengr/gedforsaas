package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.project.GedforSaas.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

     Optional<User> findByEmail(String email);
     User findByEmailAndPassword(String email, String password);
     User findUserById(Long id );
}
