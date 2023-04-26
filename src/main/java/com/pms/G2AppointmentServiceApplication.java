package com.pms;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
public class G2AppointmentServiceApplication {

		@Bean
		public ModelMapper modelmapper() {
			return new ModelMapper();
		}
		
	public static void main(String[] args) {
		SpringApplication.run(G2AppointmentServiceApplication.class, args);
	}

}
