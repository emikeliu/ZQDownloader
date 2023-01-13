package zq.downloader.runner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class CreateFolderRunner implements ApplicationRunner {
    @Value("${zq.filepath}")
    String path;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        deleteDirectory(new File(path));
        if (!new File(path).mkdir()) {
            throw new RuntimeException("Cannot create folder!");
        }
    }

    private void deleteDirectory(File file) {
        if (file.isDirectory()) {
            File[] fs = file.listFiles();
            if (fs != null) {
                for (File s : fs) {
                    deleteDirectory(s);
                }
            }
        }
        file.delete();
    }
}