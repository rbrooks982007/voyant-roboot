package org.test.robot;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
    "org.test.robot.models",
    "org.test.robot.navigation",
    "org.test.robot.reports",
    "org.test.robot.readers"
})
public class RobotReaderTestConfig {

  @Bean
  public OutputStream getOutputStream() {
    return new ByteArrayOutputStream();
  }

}
