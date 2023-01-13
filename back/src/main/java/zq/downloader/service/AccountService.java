package zq.downloader.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zq.downloader.bean.request.UserRegisterRequest;
import zq.downloader.mapper.UserMapper;

@Service
public class AccountService {
    private UserMapper mapper;
    private PasswordEncoder encoder;

    @Autowired
    public void setMapper(UserMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Transactional
    public boolean register(UserRegisterRequest register) {
        return mapper.register(register.getUsername(), encoder.encode(register.getPassword())) == 1;
    }

    @Transactional
    public void changePassword(String oldRawPassword, String newRawPassword, String username) {
        if (encoder.matches(oldRawPassword, mapper.selectPassword(username)))
        if (mapper.updatePassword(encoder.encode(newRawPassword), username) != 1)
            throw new RuntimeException("Wrong Password!");
    }

}
