package com.myagent;

import org.springframework.boot.SpringApplication;

public class TestMyAgentJavaLangchainApplication {

	public static void main(String[] args) {
		SpringApplication.from(MyAgentJavaLangchainApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
