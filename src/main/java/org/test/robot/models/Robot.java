package org.test.robot.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Robot {
  private final int positionX;
  private final int positionY;
  private final Direction direction;
}
