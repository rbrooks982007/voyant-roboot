package org.test.robot.readers;

import java.io.OutputStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class ReaderConfig {

  @Bean
  public OutputStream getOutputStream() {
    return System.out;
  }
}
