package propensi.proyek.properly.service.user;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import propensi.proyek.properly.Dto.akuns.UserDto;
import propensi.proyek.properly.model.User;

public interface UserService {
    Boolean doesUserExist(String username, String password);
    List<User> getAllUser();
    String generateUsername(String name);
    String generatePassword();
    List<User> getByUsername(String Username);
    User getUserByUsername(String Username);
    User getUserById(UUID id);

    void addCurrentUserToModel(String username, Collection<? extends GrantedAuthority> authorities, Model model);

    User updateUser(User user);
}
