package propensi.proyek.properly.service.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import propensi.proyek.properly.model.User;

public interface UserService {
    Boolean doesUserExist(String username, String password);
    List<User> getAllUser();
    String generateUsername(String name);
    String generatePassword();
    List<User> getByUsername(String Username);
    User getUserByUsername(String Username);

    void addCurrentUserToModel(String username, Collection<? extends GrantedAuthority> authorities, Model model);
}
