package zq.downloader.bean;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class SpringMiscBeans {
    @Value("${zq.encoded-method}")
    String encryptMethod;

    @Bean
    public ExecutorService service() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        switch (encryptMethod) {
            case "simple" -> {
                return new SimplePasswordEncoder();
            }
            case "bcrypt" -> {
                return new BCryptPasswordEncoder();
            }

            default -> throw new RuntimeException("No encoder specified!");
        }
    }

    @Bean
    public HashMap<String, Object> keeper() {
        return new HashMap<>();
    }


    static class SimplePasswordEncoder implements PasswordEncoder {
        @Override
        public String encode(CharSequence rawPassword) {
            return rawPassword.toString();
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return rawPassword.equals(encodedPassword);
        }
    }
}
