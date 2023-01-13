package zq.downloader.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import zq.downloader.mapper.UserMapper;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserMapper mapper;

    @Autowired
    public MyUserDetailsService(UserMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new MyUserDetails(username, mapper);
    }
}
