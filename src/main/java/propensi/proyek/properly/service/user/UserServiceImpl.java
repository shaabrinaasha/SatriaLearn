package propensi.proyek.properly.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import propensi.proyek.properly.repository.UserDb;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDb userDb;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
    
}
