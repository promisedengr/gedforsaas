package ma.project.GedforSaas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ma.project.GedforSaas.model.User;
import ma.project.GedforSaas.model.UserInfo;

@Service
public class UserInfoService {

    public UserInfo getUserInfoFromUser(User user) {

        return new UserInfo(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getBirthDate(), user.getWorkspace(), user.isTwoFactorAuth(), user.getSignature(),
                user.getCertificat(), user.isActive(), user.isConfirmed(), user.getPhoneNumber(), user.getPermissions(),
                user.getPlan(), user.getRole());
    }

    public List<UserInfo> getUserInfoFromUsers(List<User> users) {

        List<UserInfo> usersInfoList = new ArrayList<>();

        for (User user : users) {
            usersInfoList.add(getUserInfoFromUser(user));
        }

        return usersInfoList;
    }

}
