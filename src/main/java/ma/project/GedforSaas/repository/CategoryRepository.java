package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.project.GedforSaas.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
