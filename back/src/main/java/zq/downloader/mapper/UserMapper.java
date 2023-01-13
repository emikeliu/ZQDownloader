package zq.downloader.mapper;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper {

    int register(String username, String password);

    String selectPassword(String username);

    String tableExists();

    void createTable();

    int updatePassword(String newPassword, String username);
}
