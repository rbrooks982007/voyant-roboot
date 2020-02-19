package org.test.robot.navigation;

import org.springframework.stereotype.Service;
import org.test.robot.models.Direction;
import org.test.robot.models.Instruction;
import org.test.robot.models.Robot;

@Service
public class RobotNavigation implements Navigation {

  private static final int UPPER_BOUND = 4;
  private static final int LOWER_BOUND = 0;

  @Override
  public Robot rotate(Robot robot, Instruction instruction) {
    Robot changed = robot;
    switch(robot.getDirection()) {
      case NORTH:
        switch (instruction) {
          case LEFT:
            changed = robot.toBuilder().direction(Direction.WEST).build();
            break;
          case RIGHT:
            changed = robot.toBuilder().direction(Direction.EAST).build();
        }
        break;
      case SOUTH:
        switch (instruction) {
          case LEFT:
            changed = robot.toBuilder().direction(Direction.EAST).build();
            break;
          case RIGHT:
            changed = robot.toBuilder().direction(Direction.WEST).build();
        }
        break;
      case EAST:
        switch (instruction) {
          case LEFT:
            changed = robot.toBuilder().direction(Direction.NORTH).build();
            break;
          case RIGHT:
            changed = robot.toBuilder().direction(Direction.SOUTH).build();
        }
        break;
      case WEST:
        switch (instruction) {
          case LEFT:
            changed = robot.toBuilder().direction(Direction.SOUTH).build();
            break;
          case RIGHT:
            changed = robot.toBuilder().direction(Direction.NORTH).build();
        }
        break;
    }
    return changed;
  }

  @Override
  public Robot move(Robot robot) {
    Robot changed = robot;
    switch (robot.getDirection()) {
      case NORTH:
        changed = robot.toBuilder()
            .positionY(Math.min(robot.getPositionY() + 1, UPPER_BOUND)).build();
        break;
      case SOUTH:
        changed = robot.toBuilder()
            .positionY(Math.max(robot.getPositionY() - 1, LOWER_BOUND)).build();
        break;
      case EAST:
        changed = robot.toBuilder()
            .positionX(Math.min(robot.getPositionX() + 1, UPPER_BOUND)).build();
        break;
      case WEST:
        changed = robot.toBuilder()
            .positionX(Math.max(robot.getPositionX() - 1, LOWER_BOUND)).build();
        break;
    }
    return changed;
  }

  @Override
  public Robot place(int x, int y, Direction direction) {
    Robot robot = null;
    if(direction != null
        && x >= LOWER_BOUND && x <= UPPER_BOUND
        && y >= LOWER_BOUND && y <= UPPER_BOUND) {

      robot = Robot.builder().positionX(x).positionY(y).direction(direction).build();
    }
    return robot;
  }
}
