package org.test.robot.reports;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.test.robot.models.Robot;

@Service
public class RobotReport implements Report {

  private OutputStream outputStream;

  @Autowired
  public RobotReport(OutputStream outputStream) {
    this.outputStream = outputStream;
  }

  @Override
  public void printReport(Robot robot) {
    String content = new StringBuilder().append("Output: ")
        .append(robot.getPositionX())
        .append(", ")
        .append(robot.getPositionY())
        .append(", ")
        .append(robot.getDirection())
        .append(" ")
        .append(System.getProperty("line.separator"))
        .toString();
    try {
      outputStream.write(content.getBytes());
    } catch (IOException ex) {
      System.out.println("Failed to write to OutputStream " + content);
    }
  }
}
