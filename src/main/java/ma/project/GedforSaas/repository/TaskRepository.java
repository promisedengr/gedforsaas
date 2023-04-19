package ma.project.GedforSaas.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.project.GedforSaas.model.Resource;
import ma.project.GedforSaas.model.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findByName(String name);
    List<Task> findByCompanyId(Long id);
    List<Task> findByResourceId(Long resourceId);
}
