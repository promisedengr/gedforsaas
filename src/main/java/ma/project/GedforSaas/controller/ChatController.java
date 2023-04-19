package ma.project.GedforSaas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import ma.project.GedforSaas.model.Chat;
import ma.project.GedforSaas.service.ChatService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/chats")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/ref/{ref}")
    public List<Chat> findByReference(@PathVariable String ref) {
        return chatService.findByReference(ref);
    }


    @GetMapping("/{senderId}/{receiverId}")
    public List<Chat> findBySenderIdAndReceiverId(@PathVariable Long senderId, @PathVariable Long receiverId) {
        return chatService.getChats(senderId, receiverId);
    }

    @GetMapping("/{id}")
    public List<Chat> findAll(@PathVariable Long id) {
        return chatService.getUserChats(id);
    }

    @PostMapping("/all")
    public List<Chat> saveAll(@RequestBody List<Chat> entities) {
        return chatService.saveAll(entities);
    }

    @PostMapping("/")
    public Chat save(@RequestBody Chat entity) {
        return chatService.save(entity);
    }




    @GetMapping("/count/{senderId}/{receiverId}")
    public Long countBySenderIdAndReceiverId(@PathVariable Long senderId, @PathVariable Long receiverId) {
        return chatService.countBySenderIdAndReceiverId(senderId, receiverId);
    }
}
