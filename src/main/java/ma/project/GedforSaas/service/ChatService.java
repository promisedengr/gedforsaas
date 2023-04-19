package ma.project.GedforSaas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import ma.project.GedforSaas.model.Chat;
import ma.project.GedforSaas.repository.ChatRepository;

import java.util.List;
import java.util.Optional;
@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;

    public List<Chat> findByReference(String ref) {
        return chatRepository.findByReference(ref);
    }

    public List<Chat> findBySenderIdAndReceiverId(Long senderId, Long receiverId) {
        return chatRepository.findBySenderIdAndReceiverId(senderId, receiverId);
    }


    public List<Chat> getChats(Long senderId, Long receiverId) {
        return chatRepository.getChats(senderId, receiverId);
    }


    public List<Chat> getUserChats(Long id) {
        return chatRepository.getUserChats(id);
    }

    public  List<Chat> saveAll(List<Chat> entities) {
        return chatRepository.saveAll(entities);
    }

    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

    public  Chat save(Chat entity) {
        return chatRepository.save(entity);
    }

    public Optional<Chat> findById(Long aLong) {
        return chatRepository.findById(aLong);
    }

    public Long countByReference(String ref) {
        return chatRepository.countByReference(ref);
    }

    public Long countBySenderIdAndReceiverId(Long senderId, Long receiverId) {
        return chatRepository.countBySenderIdAndReceiverId(senderId, receiverId);
    }
}
