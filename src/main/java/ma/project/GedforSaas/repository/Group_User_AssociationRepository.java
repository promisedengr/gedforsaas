package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.project.GedforSaas.model.GroupUserAssociations;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface Group_User_AssociationRepository extends JpaRepository<GroupUserAssociations, Long> {
    List<GroupUserAssociations> findByUserId(Long id);
    List<GroupUserAssociations> deleteByUserId(Long userId);
    GroupUserAssociations findByGroupIdAndUserId(Long groupId, Long userId);
    @Modifying
    @Query("DELETE  FROM GroupUserAssociations  o  WHERE o.id= :id")
    int deleteGroupUserAssociationById(@Param("id") Long id);
}
