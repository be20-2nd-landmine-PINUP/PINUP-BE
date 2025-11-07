package pinup.backend.pinupnotice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("pinup.backend.pinupnotice.notice.query.mapper")
@EnableFeignClients
public class PinupNoticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PinupNoticeApplication.class, args);
    }

}
