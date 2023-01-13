package zq.downloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ZqDownloaderSpringApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ZqDownloaderSpringApplication.class, args);
    }


}
