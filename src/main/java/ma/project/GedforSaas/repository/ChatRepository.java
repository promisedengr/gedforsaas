package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.project.GedforSaas.model.Chat;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByReference(String ref);

    List<Chat> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    Long countByReference(String ref);

    Long countBySenderIdAndReceiverId(Long senderId, Long receiverId);

    @Query(value = "SELECT * FROM Chat WHERE " +
            "(receiver_user_id = :receiverId AND sender_user_id= :senderId) OR " +
            "(receiver_user_id = :senderId AND sender_user_id= :receiverId)", nativeQuery = true)
    List<Chat> getChats(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);


    @Query(value = "SELECT * FROM Chat WHERE " +
            "(receiver_user_id = :id) OR " +
            "(sender_user_id= :id)", nativeQuery = true)
    List<Chat> getUserChats(@Param("id") Long id);
}
