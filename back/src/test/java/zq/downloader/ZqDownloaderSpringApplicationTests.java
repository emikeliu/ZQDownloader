package zq.downloader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import zq.downloader.mapper.UserMapper;
@SpringBootTest()
@RunWith(SpringRunner.class)
public class ZqDownloaderSpringApplicationTests {
    UserMapper mapper;
    @Autowired
    public void setMapper(UserMapper mapper) {
        this.mapper = mapper;
    }
    @Test
    public void contextLoads() {
        mapper.register("123", new BCryptPasswordEncoder().encode("123"));
    }

}
