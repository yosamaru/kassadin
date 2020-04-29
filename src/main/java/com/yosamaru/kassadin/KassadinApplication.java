package com.yosamaru.kassadin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.yosamaru.kassadin.dao")
@EntityScan("com.yosamaru.kassadin.entity.PO")
@SpringBootApplication(exclude = {
		GroovyTemplateAutoConfiguration.class,
		GsonAutoConfiguration.class,
		WebSocketServletAutoConfiguration.class,
		JmxAutoConfiguration.class,

})
public class KassadinApplication {

	public static void main(String[] args) {
		SpringApplication.run(KassadinApplication.class, args);
	}

}
