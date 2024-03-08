package propensi.proyek.properly.service.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.passay.CharacterRule;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.client.ResourceAccessException;

import propensi.proyek.properly.model.User;
import propensi.proyek.properly.repository.UserDb;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDb userDb;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CharacterRule myCharacterRule;

    @Override
    public Boolean doesUserExist(String username, String password) {
        // TODO Auto-generated method stub
        var users = userDb.findByUsername(username);

        if (users.size() > 1) {
            throw new ResourceAccessException("Username found more than 1 user");
        }

        if (users.size() == 0) {
            return false;
        }

        var user = users.get(0);

        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public List<User> getAllUser() {
        return userDb.findAll();
    }

    //TODO: Need Testing
    @Override
    public String generateUsername(String name) {
        var names = name.split(" ");    
        String generatedUsername;

        if (names.length > 1) {
            generatedUsername = String.format("%s.%s", names[0], names[1]);
            generatedUsername = generatedUsername.toLowerCase();
        } else if (names.length == 1) {
            generatedUsername = String.format("%s", names[0]);
            generatedUsername = generatedUsername.toLowerCase();
        } else {
            throw new IllegalArgumentException("Username format incorrect");
        }

        var usersWithSimilarUsername = userDb.findAllByUsernameLikeOrderByUsernameDesc(generatedUsername+"%");

        if (usersWithSimilarUsername.size() == 0) {
            return generatedUsername + "01";
        }
        var highestPrefix = getHighestPrefix(generatedUsername, usersWithSimilarUsername);

        return String.format("%s%2d", generatedUsername, highestPrefix+1).replace(" ", "0");

        
    }

    private Integer getHighestPrefix(String username, List<User> users) {
        for (User user: users) {
            try {
                var prefix = Integer.parseInt(user.getUsername().split(username)[1]);
                return prefix;
            }
            catch (Exception e) {
                continue;
            }
        }
        return 0;
    }

    @Override
    public String generatePassword() {
        return new PasswordGenerator().generatePassword(8, myCharacterRule);
    }
    public List<User> getByUsername(String Username) {
        return userDb.findByUsername(Username);
    }

    @Override
    public void addCurrentUserToModel(String username, Collection<? extends GrantedAuthority> authorities, Model model) {
        var users = userDb.findByUsername(username);
        if (users.size() != 1) {
            var user = new User();
            model.addAttribute("currentUser", user);
        } else {
            model.addAttribute("currentUser", users.get(0));
        }

        var roles = new ArrayList<String>();

        for (GrantedAuthority authority : authorities) {
            try {
                roles.add(authority.getAuthority().split("ROLE_")[1]);
            } catch (Exception e) {
                continue;
            }
        }

        model.addAttribute("roles", roles);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDb.findUserByUsername(username);
    }

}
