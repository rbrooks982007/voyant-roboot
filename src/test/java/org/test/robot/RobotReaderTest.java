package org.test.robot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.test.robot.models.Direction;
import org.test.robot.models.Robot;
import org.test.robot.readers.Reader;
import org.test.robot.readers.RobotDataReader;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RobotReaderTestConfig.class)
@ActiveProfiles("test")
public class RobotReaderTest {

  @Autowired
  private Reader robotReader;

  @Autowired
  private OutputStream outputStream;

  private StringBuilder testBuilder = null;

  protected Robot getRobot() {
    return ((RobotDataReader)robotReader).getRobot();
  }

  protected RobotReaderTest append(String testLine) {
    testBuilder = testBuilder == null
        ? new StringBuilder().append(testLine)
        : testBuilder.append(System.getProperty("line.separator")).append(testLine);
    return this;
  }

  @After
  public void cleanUp() {
    testBuilder = null;
  }

  @Test
  public void shouldNotPlaceRobotOnBoard() {
    append("PLACE,-1,0,WEST")
        .append("foo, bar")
        .append("PLACE,4,-1,WEST")
        .append("REPORT,0,0,WEST")
        .append("MOVE,4,4,EAST")
        .append("LEFT,2,4,NORTH")
        .append("RIGHT,2,4,SOUTH")
        .append("exit");
    robotReader.read(new ByteArrayInputStream(testBuilder.toString().getBytes()));
    Assert.assertNull(getRobot());
  }

  @Test
  public void shouldBePlaceRobotOnBoard() {
    append("PLACE,0,0,WEST")
        .append("exit");
    robotReader.read(new ByteArrayInputStream(testBuilder.toString().getBytes()));
    Assert.assertNotNull(getRobot());
    Assert.assertEquals(Robot.builder()
                            .positionX(0)
                            .positionY(0)
                            .direction(Direction.WEST)
                            .build(), getRobot());
    testBuilder = null;
    append("PLACE,1,0,EAST")
        .append("exit");
    robotReader.read(new ByteArrayInputStream(testBuilder.toString().getBytes()));
    Assert.assertNotNull(getRobot());
    Assert.assertEquals(Robot.builder()
                            .positionX(1)
                            .positionY(0)
                            .direction(Direction.EAST)
                            .build(), getRobot());

    testBuilder = null;
    append("PLACE,4,4,NORTH")
        .append("exit");
    robotReader.read(new ByteArrayInputStream(testBuilder.toString().getBytes()));
    Assert.assertNotNull(getRobot());
    Assert.assertEquals(Robot.builder()
                            .positionX(4)
                            .positionY(4)
                            .direction(Direction.NORTH)
                            .build(), getRobot());

    testBuilder = null;
    append("PLACE,0,4,SOUTH")
        .append("exit");
    robotReader.read(new ByteArrayInputStream(testBuilder.toString().getBytes()));
    Assert.assertNotNull(getRobot());
    Assert.assertEquals(Robot.builder()
                            .positionX(0)
                            .positionY(4)
                            .direction(Direction.SOUTH)
                            .build(), getRobot());

    testBuilder = null;
    append("PLACE,0,4,WEST")
        .append("exit");
    robotReader.read(new ByteArrayInputStream(testBuilder.toString().getBytes()));
    Assert.assertNotNull(getRobot());
    Assert.assertEquals(Robot.builder()
                            .positionX(0)
                            .positionY(4)
                            .direction(Direction.WEST)
                            .build(), getRobot());
  }

  @Test
  public void shouldMoveRobotOnlyOnBoard() {
    append("PLACE,0,0,WEST")
        .append("MOVE")
        .append("exit");
    robotReader.read(new ByteArrayInputStream(testBuilder.toString().getBytes()));
    Assert.assertNotNull(getRobot());
    Assert.assertEquals(Robot.builder()
                            .positionX(0)
                            .positionY(0)
                            .direction(Direction.WEST)
                            .build(), getRobot());

    testBuilder = null;
    append("PLACE,0,0,EAST")
        .append("MOVE")
        .append("MOVE")
        .append("LEFT")
        .append("MOVE")
        .append("exit");
    robotReader.read(new ByteArrayInputStream(testBuilder.toString().getBytes()));
    Assert.assertNotNull(getRobot());
    Assert.assertEquals(Robot.builder()
                            .positionX(2)
                            .positionY(1)
                            .direction(Direction.NORTH)
                            .build(), getRobot());

    testBuilder = null;
    append("PLACE,3,2,EAST")
        .append("MOVE")
        .append("MOVE")
        .append("LEFT")
        .append("MOVE")
        .append("MOVE")
        .append("MOVE")
        .append("MOVE")
        .append("LEFT")
        .append("exit");
    robotReader.read(new ByteArrayInputStream(testBuilder.toString().getBytes()));
    Assert.assertNotNull(getRobot());
    Assert.assertEquals(Robot.builder()
                            .positionX(4)
                            .positionY(4)
                            .direction(Direction.WEST)
                            .build(), getRobot());

    testBuilder = null;
    append("PLACE,0,0,SOUTH")
        .append("LEFT")
        .append("MOVE")
        .append("MOVE")
        .append("LEFT")
        .append("MOVE")
        .append("exit");
    robotReader.read(new ByteArrayInputStream(testBuilder.toString().getBytes()));
    Assert.assertNotNull(getRobot());
    Assert.assertEquals(Robot.builder()
                            .positionX(2)
                            .positionY(1)
                            .direction(Direction.NORTH)
                            .build(), getRobot());

    testBuilder = null;
    append("PLACE,0,0,SOUTH")
        .append("MOVE")
        .append("MOVE")
        .append("MOVE")
        .append("RIGHT")
        .append("MOVE")
        .append("exit");
    robotReader.read(new ByteArrayInputStream(testBuilder.toString().getBytes()));
    Assert.assertNotNull(getRobot());
    Assert.assertEquals(Robot.builder()
                            .positionX(0)
                            .positionY(0)
                            .direction(Direction.WEST)
                            .build(), getRobot());
  }

  @Test
  public void shouldReportRobotMoves() {
    append("PLACE,0,0,WEST")
        .append("MOVE")
        .append("REPORT")
        .append("exit");
    robotReader.read(new ByteArrayInputStream(testBuilder.toString().getBytes()));
    Assert.assertNotNull(getRobot());
    Assert.assertEquals("Output: 0, 0, WEST \n", outputStream.toString());


    ((ByteArrayOutputStream)outputStream).reset();
    testBuilder = null;
    append("PLACE,0,0,EAST")
        .append("MOVE")
        .append("MOVE")
        .append("LEFT")
        .append("MOVE")
        .append("REPORT")
        .append("exit");
    robotReader.read(new ByteArrayInputStream(testBuilder.toString().getBytes()));
    Assert.assertNotNull(getRobot());
    Assert.assertEquals("Output: 2, 1, NORTH \n", outputStream.toString());

    ((ByteArrayOutputStream)outputStream).reset();
    testBuilder = null;
    append("PLACE,3,2,EAST")
        .append("MOVE")
        .append("MOVE")
        .append("LEFT")
        .append("MOVE")
        .append("MOVE")
        .append("MOVE")
        .append("MOVE")
        .append("LEFT")
        .append("REPORT")
        .append("exit");
    robotReader.read(new ByteArrayInputStream(testBuilder.toString().getBytes()));
    Assert.assertNotNull(getRobot());
    Assert.assertEquals("Output: 4, 4, WEST \n", outputStream.toString());

    ((ByteArrayOutputStream)outputStream).reset();
    testBuilder = null;
    append("PLACE,0,0,SOUTH")
        .append("LEFT")
        .append("MOVE")
        .append("MOVE")
        .append("LEFT")
        .append("MOVE")
        .append("REPORT")
        .append("exit");
    robotReader.read(new ByteArrayInputStream(testBuilder.toString().getBytes()));
    Assert.assertNotNull(getRobot());
    Assert.assertEquals("Output: 2, 1, NORTH \n", outputStream.toString());

  }

}
