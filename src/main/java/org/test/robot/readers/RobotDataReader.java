package org.test.robot.readers;


import java.io.InputStream;
import java.util.Scanner;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.test.robot.navigation.Navigation;
import org.test.robot.reports.Report;
import org.test.robot.models.Direction;
import org.test.robot.models.Instruction;
import org.test.robot.models.Robot;

@Service
public class RobotDataReader implements Reader {

  @Getter
  private Robot robot;
  private final Navigation navigation;
  private final Report report;

  @Autowired
  public RobotDataReader(final Navigation navigation, final Report report) {
    this.navigation = navigation;
    this.report = report;
  }

  @Override
  public void read(InputStream inputStream) {
    Scanner scanner = new Scanner(inputStream);
    String line = "";
    while(!"EXIT".equalsIgnoreCase(line)) {
      if(scanner.hasNextLine()) {
        line = scanner.nextLine();
        parse(line.split(","));
      }
    }
  }

  protected void parse(final String[] instructions) {
    try {
      Instruction instruct = Instruction.valueOf(instructions[0].trim().toUpperCase());
      int positionX;
      int positionY;
      Direction direction;

      if (!Instruction.PLACE.equals(instruct) && robot == null) {
        System.out.println("Please place Robot on board");
        return;
      }

      switch (instruct) {
        case PLACE:
          positionX = Integer.parseInt(instructions[1].trim());
          positionY = Integer.parseInt(instructions[2].trim());
          direction = Direction.valueOf(instructions[3].trim());
          robot = navigation.place(positionX, positionY, direction);
          break;
        case LEFT:
        case RIGHT:
          robot = navigation.rotate(robot, instruct);
          break;
        case MOVE:
          robot = navigation.move(robot);
          break;
        case REPORT:
          report.printReport(robot);
          break;
      }


    } catch (NumberFormatException ex) {
      System.out.println("Please enter a valid board position for your Robot");
    } catch (Exception ex) {
      System.out.println("Unexpected error while processing Robot instruction");
    }

  }
}
