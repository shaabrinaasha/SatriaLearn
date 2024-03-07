package propensi.proyek.properly.service.user;

import java.util.List;

import propensi.proyek.properly.model.User;

public interface UserService {
    Boolean doesUserExist(String username, String password);
    List<User> getAllUser();
    String generateUsername(String name);
    String generatePassword();
    List<User> getByUsername(String Username);
}
