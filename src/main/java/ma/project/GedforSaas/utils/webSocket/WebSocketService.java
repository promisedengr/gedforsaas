package ma.project.GedforSaas.utils.webSocket;


import org.springframework.stereotype.Service;

import ma.project.GedforSaas.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebSocketService {

    List<User> connectedUsers = new ArrayList<>();
    Map<Long, List<User>> mapUsers= new HashMap<>();

    List<User> addUserToList(User user, Long id)   {
        if (mapUsers.containsKey(id)){
            if (!this.mapUsers.get(id).contains(user)){
                this.mapUsers.get(id).add(user);
            }
        } else {
            connectedUsers.add(user);
            mapUsers.put(id, connectedUsers);
        }

       return mapUsers.get(id);
    }


    List<User> removeUserToList(User user, Long id)   {
        this.mapUsers.get(id).remove(user);
        return mapUsers.get(id);
    }

    List<User> getList(Long id){
        return mapUsers.get(id);
    }


}
