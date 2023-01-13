package zq.downloader.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import zq.downloader.mapper.UserMapper;
import zq.downloader.wrapper.UserDetailsWrapper;

import java.util.Collection;
import java.util.List;

public class MyUserDetails extends UserDetailsWrapper {
    private final UserMapper userMapper;
    private final String username;

    public MyUserDetails(String username, UserMapper mapper) {
        this.userMapper = mapper;
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_LOGIN"));
    }

    @Override
    public String getPassword() {
        return userMapper.selectPassword(username);
    }

    @Override
    public String getUsername() {
        return username;
    }


}
