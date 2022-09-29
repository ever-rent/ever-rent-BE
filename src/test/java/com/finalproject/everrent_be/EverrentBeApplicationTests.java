package com.finalproject.everrent_be;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class EverrentBeApplicationTests {

	@Test
	void contextLoads() {
		//String -> LocalDate
		String string = "2019-01-10";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(string, formatter);
		System.out.println(date);
		System.out.println(date.getClass().getName());

		//LocalDate -> String
		String sstr=date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		System.out.println(sstr);
		System.out.println(sstr.getClass().getName());
	}

}
