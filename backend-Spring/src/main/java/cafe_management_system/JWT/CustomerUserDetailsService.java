package cafe_management_system.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cafe_management_system.dao.UserDao;
import org.springframework.security.core.userdetails.User;
import java.util.Objects;
import java.util.ArrayList;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    private cafe_management_system.model.User userDetail;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        userDetail = userDao.findByEmail(email);
        if (!Objects.isNull(userDetail)) {
            return new User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found");
        }

    }

    public cafe_management_system.model.User getUserDetails() {
        return userDetail;
    }
}
