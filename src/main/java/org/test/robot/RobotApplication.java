package org.test.robot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.test.robot.readers.Reader;

@SpringBootApplication
public class RobotApplication {

  private final Reader robotReader;

  @Autowired
  public RobotApplication(Reader robotReader) {
    this.robotReader = robotReader;
    robotReader.read(System.in);
  }

  public static void main(String[] args) {
    SpringApplication.run(RobotApplication.class);
  }

}
