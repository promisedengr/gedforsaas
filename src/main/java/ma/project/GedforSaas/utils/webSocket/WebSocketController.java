package ma.project.GedforSaas.utils.webSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ma.project.GedforSaas.model.User;

import java.util.List;

@RestController
@RequestMapping("chat/")
public class WebSocketController {
    @Autowired
    private  WebSocketService webSocketService;


    @PostMapping("add/{id}/")
    public List<User> addUserToList(@RequestBody User user,@PathVariable Long id) {
        // return webSocketService.
        // return socketService.a
        return webSocketService.addUserToList(user, id);
    }

    @PostMapping("remove/{id}/")
    public List<User> removeUserToList(@RequestBody User user,@PathVariable Long id) {
        return webSocketService.removeUserToList(user, id);
    }

    @GetMapping("get/{id}")
    public List<User> getList(@PathVariable Long id) {
        return webSocketService.getList(id);
    }
}
