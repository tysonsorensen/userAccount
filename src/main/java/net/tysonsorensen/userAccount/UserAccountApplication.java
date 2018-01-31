package net.tysonsorensen.userAccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class UserAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAccountApplication.class, args);
	}
}
