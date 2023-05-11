package com.wilddiary.monitoring;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/** Spring Boot Admin application. */
@EnableAdminServer
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class SpringBootAdminApplication {

  /**
   * Main application entrypoint.
   *
   * @param args command-line application arguments
   */
  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(SpringBootAdminApplication.class);
    app.setApplicationStartup(new BufferingApplicationStartup(1500));
    app.run(args);
  }
}
