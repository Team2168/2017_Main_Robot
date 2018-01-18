package org.team2168.PID.trajectory.io;

import org.team2168.PID.trajectory.QuinticTrajectory;
import org.team2168.PID.trajectory.Trajectory;
import org.team2168.PID.trajectory.Trajectory.Segment;

/**
 * Serializes a Path to a simple space and CR separated text file.
 * 
 * @author Jared341
 */
public class TextFileSerializer implements IPathSerializer {

  /**
   * Format:
   *   PathName
   *   NumSegments
   *   LeftSegment1
   *   ...
   *   LeftSegmentN
   *   RightSegment1
   *   ...
   *   RightSegmentN
   * 
   * Each segment is in the format:
   *   pos vel acc jerk heading dt x y
   * 
   * @param quinticPath The path to serialize.
   * @return A string representation.
   */
  public String serialize(QuinticTrajectory quinticPath) {
    String content = quinticPath.getName() + "\n";
    content += quinticPath.getTrajectory().getNumSegments() + "\n";
    content += serializeTrajectory(quinticPath.getLeftTrajectory());
    content += serializeTrajectory(quinticPath.getRightTrajectory());
    return content;
  }
  
  private String serializeTrajectory(Trajectory trajectory) {
    String content = "";
    for (int i = 0; i < trajectory.getNumSegments(); ++i) {
      Segment segment = trajectory.getSegment(i);
      content += String.format(
              "%.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f\n", 
              segment.pos, segment.vel, segment.acc, segment.jerk,
              segment.heading, segment.dt, segment.x, segment.y);
    }
    return content;
  }

@Override
public String serialize(Path path)
{
	// TODO Auto-generated method stub
	return null;
}
  
}
