package org.test.robot.navigation;

import org.test.robot.models.Direction;
import org.test.robot.models.Instruction;
import org.test.robot.models.Robot;

public interface Navigation {

  Robot rotate(final Robot robot, Instruction instruction);
  Robot move(final Robot robot);
  Robot place(final int x, final int y, final Direction direction);
}
