package propensi.proyek.properly.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import propensi.proyek.properly.repository.UserDb;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDb userDb;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var users = userDb.findByUsername(username);

        if (users.size() > 1) {
            throw new ResourceAccessException("More than 1 user found when only 1 is expected.");
        }

        if (users.size() == 0) {
            throw new UsernameNotFoundException("Username not found.");
        }

        var user = users.get(0);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_user"));
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getDecriminatorValue()));

        var userDetails = new User(user.getUsername(), user.getPassword(), true, true, true, true,authorities);
        

        return userDetails;
    }
    
}
