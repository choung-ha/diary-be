package diary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"diary.diary-be"})
@EnableAsync
public class DiaryApplication {
	public static void main(String[] args) {
		SpringApplication.run(DiaryApplication.class, args);
	}
}
