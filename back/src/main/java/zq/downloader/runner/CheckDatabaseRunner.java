package zq.downloader.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import zq.downloader.mapper.UserMapper;

@Component
public class CheckDatabaseRunner implements ApplicationRunner {
    private UserMapper mapper;

    @Autowired
    public void setMapper(UserMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (mapper.tableExists() == null) mapper.createTable();
        if (mapper.tableExists() == null) throw new RuntimeException("Database initialized failed!");
    }
}
