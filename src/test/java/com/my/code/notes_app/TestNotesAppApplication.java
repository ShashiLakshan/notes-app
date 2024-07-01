package com.my.code.notes_app;

import org.springframework.boot.SpringApplication;

public class TestNotesAppApplication {

	public static void main(String[] args) {
		SpringApplication.from(NotesAppApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
