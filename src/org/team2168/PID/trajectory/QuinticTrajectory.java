package org.team2168.PID.trajectory;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.team2168.PID.trajectory.io.TextFileSerializer;


/**
 * The purpose of this project is to generate functions which provide smooth 
 * paths between global waypoints. The approach this project takes is to use
 * quintic (5th order splines) hermite splines to create a continue path
 * between governing waypoints. 
 * 
 * The objective is to have a solution which interpolates the control points
 * provided by the user, and to also have C2 continuity (continuous 1st and 2nd
 * order derivatives).
 * 
 * Since this project is to be used for the mobile navigation of a differential
 * drive mobile ground robot. This algorithm also provides the position, velocity,
 * acceleration, and jerk motion profiles, for the left and right wheels, while
 * trying to maintain max velocity, max acceleration, and max jerk constraints.
 * 
 * @author Kevin Harrilal
 * Reference https://www.rose-hulman.edu/~finn/CCLI/Notes/day09.pdf
 * 
 */



public class QuinticTrajectory
{
	//Path Variables
	public double[][] origPath;
	public double[][] leftPath;
	public double[][] rightPath;
	public double[][] rightVelocity;
	public double[][] leftVelocity;
	public double[][] rightAccel;
	public double[][] leftAccel;
	public double[][] rightJerk;
	public double[][] leftJerk;

	//trajectory variables
	private Spline[] splines = null;
	Trajectory traj;
	TrajectoryGenerator.Config config;
	Trajectory.Pair leftRightTraj; 
	
	String path_name = "path";
	
	//Robot parameters
	double totalSplineLength = 0;

	public static void main(String[] args)
	{
		
		long start = System.currentTimeMillis();
		
		//create control points which govern the path. Format is 
		//{x, y, heading angle}
		//x,and y, are provided in units of distance and are assumed by the user
		//i.e (inches, ft, meters). heading angle is the desired orientation of
		//the robot once it reaches that point and is provided in radians.
		
		System.out.println("Hello World");
		
//		double[][] waypointPath = new double[][]{
//				{4, 3, Math.PI/2},
//				{4, 18, Math.PI/2},
//		};
		
//		curve Path
//		double[][] waypointPath = new double[][]{
//				{0, 0, 1.4217},
//				{5, 8, 0.28363},
//				{9, 8, -0.3363},
//				{15, 5, -0.4363},
//				{25, 3, 0},
//				{30, 3, 0},
//
//				
//		};
		
//		Left Switch Path
		double[][] waypointPath = new double[][]{
			{0, 14.49, 0},
			{8, 18.5,45*Math.PI/180},
		//	{12, 25, 65*Math.PI/180},
			
			{17, 26, 0*Math.PI/180},
			{23, 8, -89*Math.PI/180},


			
	};		
		
		
//		
//		//Square Path
//		double[][] waypointPath = new double[][]{
//				{5, 3, Math.PI/2},
//				{5, 18, Math.PI/2},
//				{10, 24, 0.0001},
//				{20, 24, 0},
//				{25, 18, -Math.PI/2+0.0001},
//				{25, 8, -Math.PI/2},
//				{20, 4, -Math.PI+0.0001},
//				{7, 4, -Math.PI},
//				
//		};
//		
//		//Clockwise Lap path
//		double[][] waypointPath = new double[][]{
//				{5, 8, Math.PI/2},
//				{5, 18, Math.PI/2},
//				{10, 24, 0.0001},
//				{20, 24, 0},
//				{25, 18, -Math.PI/2+0.0001},
//				{25, 8, -Math.PI/2},
//				{20, 4, -Math.PI+0.0001},
//				{10, 4, -Math.PI},
//				{5, 8,  Math.PI/2+0.0001},
//				{28, 29,  Math.PI/4},
//				
//		};

		
		//create new class object and perform the calculations. 
		QuinticTrajectory quinticPath= new QuinticTrajectory(waypointPath);
		quinticPath.calculate();
		
		long end = System.currentTimeMillis();
		
		
		
	    System.out.println("Traj num segments=" + quinticPath.traj.getNumSegments() );
	    System.out.println("Left = " + quinticPath.leftRightTraj.left.getNumSegments());
	    print(quinticPath.leftPath);
	    print(quinticPath.leftVelocity);
		
	    System.out.println("Time to run is " + (end - start) + "ms");
	    
		//Lets create a bank image
		FalconLinePlot fig3 = new FalconLinePlot(waypointPath,null,Color.black);
		fig3.yGridOn();
		fig3.xGridOn();
		fig3.setYLabel("Y (feet)");
		fig3.setXLabel("X (feet)");
		fig3.setTitle("Top Down View of FRC Field (30ft x 27ft) \n shows global position of robot path, along with left and right wheel trajectories");
		fig3.setSize(600,400);


		//force graph to show 1/2 field dimensions of 24.8ft x 27 feet
		double fieldWidth = 27.0;
		fig3.setXTic(0, 54, 1);
		fig3.setYTic(0, fieldWidth, 1);
		fig3.addData(quinticPath.rightPath, Color.magenta);
		fig3.addData(quinticPath.leftPath, Color.cyan);

		
		
		//Velocity
		FalconLinePlot fig4 = new FalconLinePlot(new double[][]{{0.0,0.0}});
		fig4.yGridOn();
		fig4.xGridOn();
		fig4.setYLabel("Velocity (ft/sec)");
		fig4.setXLabel("time (seconds)");
		fig4.setTitle("Velocity Profile for Left and Right Wheels \n Left = Cyan, Right = Magenta");
		fig4.addData(quinticPath.rightVelocity, Color.magenta);
		fig4.addData(quinticPath.leftVelocity, Color.cyan);

		
		//Velocity
		FalconLinePlot fig5 = new FalconLinePlot(new double[][]{{0.0,0.0}});
		fig5.yGridOn();
		fig5.xGridOn();
		fig5.setYLabel("accel (ft^2/sec)");
		fig5.setXLabel("time (seconds)");
		fig5.setTitle("Acceleration Profile for Left and Right Wheels \n Left = Cyan, Right = Magenta");
		fig5.addData(quinticPath.rightAccel, Color.magenta);
		fig5.addData(quinticPath.leftAccel, Color.cyan);
		
	
	
	
		
		//Write to file
		String directory = "../";
		quinticPath.path_name = "MYNEWPATH";
		
		
		 // Outputs to the directory supplied as the first argument.
	      TextFileSerializer js = new TextFileSerializer();
	      String serialized = js.serialize(quinticPath);
	      //System.out.print(serialized);
	      String fullpath = joinPath(directory, quinticPath.path_name + ".txt");
	      if (!writeFile(fullpath, serialized)) {
	        System.err.println(fullpath + " could not be written!!!!1");
	        System.exit(1);
	      } else {
	        System.out.println("Wrote " + fullpath);
	      }
	    
		
	
	
	}


	
	QuinticTrajectory(double[][] path)
	{
		
		this.origPath = doubleArrayCopy(path);
		
		config = new TrajectoryGenerator.Config();
	    config.dt = .02;
	    config.max_acc = 4.5; //ft/s^2
	    config.max_jerk = 10.0; //ft/s^3
	    config.max_vel = 6.0; //ft/s

	}



	public void calculate()
	{
		//Calculate Total Arc Length Using Distance
		quinticSplines(origPath);
		
		//calculate total distance
		for (int i = 0; i < splines.length; ++i)
			this.totalSplineLength += splines[i].arc_length_;
		
		System.out.println("Total Length = " + this.totalSplineLength);
		
		
		
		// Generate a smooth trajectory over the total distance.
	    this.traj = TrajectoryGenerator.generate(config,
	            TrajectoryGenerator.SCurvesStrategy, 0.0, this.origPath[0][2],
	            this.totalSplineLength, 0.0, this.origPath[0][2]);
		
	    
	    
	    
	    fixHeadings();
	   
	    leftRightTraj = makeLeftAndRightTrajectories(traj, 30.0/12.0);
	    
	    
	    
	    copyWheelPaths();

	    
		
	}
	
	public static void print(double[] path)
	{
		System.out.println("X: \t Y:");

		for(double u: path)
			System.out.println(u);
	}



	/**
	 * Prints Cartesian Coordinates to the System Output as Column Vectors in the Form X	Y
	 * @param path
	 */
	public static void print(double[][] path)
	{
		System.out.println("X: \t Y:");

		for(double[] u: path)
			System.out.println(u[0]+ "\t" +u[1]);
	}

	/**
	 * Performs a deep copy of a 2 Dimensional Array looping thorough each element in the 2D array
	 * 
	 * BigO: Order N x M
	 * @param arr
	 * @return
	 */
	public static double[][] doubleArrayCopy(double[][] arr)
	{

		//size first dimension of array
		double[][] temp = new double[arr.length][arr[0].length];

		for(int i=0; i<arr.length; i++)
		{
			//Resize second dimension of array
			temp[i] = new double[arr[i].length];

			//Copy Contents
			for(int j=0; j<arr[i].length; j++)
				temp[i][j] = arr[i][j];
		}

		return temp;

	}
	
	  public Spline[] quinticSplines(double[][] path) {
	    if (path.length < 2) {
	      return null;
	    }

	    // Compute the total length of the path by creating splines for each pair
	    // of waypoints.
	    this.splines = new Spline[path.length - 1];
	    double[] spline_lengths = new double[splines.length];
	    
	    double total_distance = 0;
	    
	    for (int i = 0; i < splines.length; ++i) 
	    {
	      splines[i] = new Spline();
	      if (!Spline.reticulateSplines(path[i][0],path[i][1],path[i][2],
	    		  path[i+1][0],path[i+1][1],path[i+1][2], splines[i])) {
	        return null;
	      }
	      spline_lengths[i] = splines[i].calculateLength();
	      total_distance += spline_lengths[i];
	    }
	    
	    return splines;
	

}
	  private void fixHeadings()
	  {
	  // Assign headings based on the splines.
	    int cur_spline = 0;
	    double cur_spline_start_pos = 0;
	    double length_of_splines_finished = 0;
	    for (int i = 0; i < traj.getNumSegments(); ++i) {
	      double cur_pos = traj.getSegment(i).pos;

	      boolean found_spline = false;
	      while (!found_spline) {
	        double cur_pos_relative = cur_pos - cur_spline_start_pos;
	        if (cur_pos_relative <= this.splines[cur_spline].arc_length_) {
	          double percentage = splines[cur_spline].getPercentageForDistance(
	                  cur_pos_relative);
	          traj.getSegment(i).heading = splines[cur_spline].angleAt(percentage);
	          double[] coords = splines[cur_spline].getXandY(percentage);
	          traj.getSegment(i).x = coords[0];
	          traj.getSegment(i).y = coords[1];
	          found_spline = true;
	        } else if (cur_spline < splines.length - 1) {
	          length_of_splines_finished += this.splines[cur_spline].arc_length_;
	          cur_spline_start_pos = length_of_splines_finished;
	          ++cur_spline;
	        } else {
	          traj.getSegment(i).heading = splines[splines.length - 1].angleAt(1.0);
	          double[] coords = splines[splines.length - 1].getXandY(1.0);
	          traj.getSegment(i).x = coords[0];
	          traj.getSegment(i).y = coords[1];
	          found_spline = true;
	        }
	      }
	    }

	  
}
	  
	  /**
	   * Generate left and right wheel trajectories from a reference.
	   *
	   * @param input The reference trajectory.
	   * @param wheelbase_width The center-to-center distance between the left and
	   * right sides.
	   * @return [0] is left, [1] is right
	   */
	  static Trajectory.Pair makeLeftAndRightTrajectories(Trajectory input,
	          double wheelbase_width) {
	    Trajectory[] output = new Trajectory[2];
	    output[0] = input.copy();
	    output[1] = input.copy();
	    Trajectory left = output[0];
	    Trajectory right = output[1];

	    for (int i = 0; i < input.getNumSegments(); ++i) {
	      Trajectory.Segment current = input.getSegment(i);
	      double cos_angle = Math.cos(current.heading);
	      double sin_angle = Math.sin(current.heading);

	      Trajectory.Segment s_left = left.getSegment(i);
	      s_left.x = current.x - wheelbase_width / 2 * sin_angle;
	      s_left.y = current.y + wheelbase_width / 2 * cos_angle;
	      if (i > 0) {
	        // Get distance between current and last segment
	        double dist = Math.sqrt((s_left.x - left.getSegment(i - 1).x)
	                * (s_left.x - left.getSegment(i - 1).x)
	                + (s_left.y - left.getSegment(i - 1).y)
	                * (s_left.y - left.getSegment(i - 1).y));
	        s_left.pos = left.getSegment(i - 1).pos + dist;
	        s_left.vel = dist / s_left.dt;
	        s_left.acc = (s_left.vel - left.getSegment(i - 1).vel) / s_left.dt;
	        s_left.jerk = (s_left.acc - left.getSegment(i - 1).acc) / s_left.dt;
	      }

	      Trajectory.Segment s_right = right.getSegment(i);
	      s_right.x = current.x + wheelbase_width / 2 * sin_angle;
	      s_right.y = current.y - wheelbase_width / 2 * cos_angle;
	      if (i > 0) {
	        // Get distance between current and last segment
	        double dist = Math.sqrt((s_right.x - right.getSegment(i - 1).x)
	                * (s_right.x - right.getSegment(i - 1).x)
	                + (s_right.y - right.getSegment(i - 1).y)
	                * (s_right.y - right.getSegment(i - 1).y));
	        s_right.pos = right.getSegment(i - 1).pos + dist;
	        s_right.vel = dist / s_right.dt;
	        s_right.acc = (s_right.vel - right.getSegment(i - 1).vel) / s_right.dt;
	        s_right.jerk = (s_right.acc - right.getSegment(i - 1).acc) / s_right.dt;
	      }
	    }

	    return new Trajectory.Pair(output[0], output[1]);
	  }
	  
	  
	  
	  void copyWheelPaths()
	  {
		  this.leftPath = new double[this.leftRightTraj.left.getNumSegments()][2];
		  this.rightPath = new double[this.leftRightTraj.right.getNumSegments()][2];
		  this.leftVelocity = new double[this.leftRightTraj.left.getNumSegments()][2];
		  this.rightVelocity = new double[this.leftRightTraj.right.getNumSegments()][2];
		  this.leftAccel = new double[this.leftRightTraj.left.getNumSegments()][2];
		  this.rightAccel = new double[this.leftRightTraj.right.getNumSegments()][2];
		  this.leftJerk = new double[this.leftRightTraj.left.getNumSegments()][2];
		  this.rightJerk = new double[this.leftRightTraj.right.getNumSegments()][2];
		  
		  //copy left
		  for( int i =0; i < this.leftRightTraj.left.getNumSegments(); i++)
		  {
			  this.leftPath[i][0] = this.leftRightTraj.left.getSegment(i).x;
			  this.leftPath[i][1] = this.leftRightTraj.left.getSegment(i).y;
			  this.rightPath[i][0] = this.leftRightTraj.right.getSegment(i).x;
			  this.rightPath[i][1] = this.leftRightTraj.right.getSegment(i).y;
			  
			  this.leftVelocity[i][0] = this.leftRightTraj.left.getSegment(i).dt*i;
			  this.leftVelocity[i][1] = this.leftRightTraj.left.getSegment(i).vel;
			  this.rightVelocity[i][0] = this.leftRightTraj.right.getSegment(i).dt*i;
			  this.rightVelocity[i][1] = this.leftRightTraj.right.getSegment(i).vel;
			  
			  this.leftAccel[i][0] = this.leftRightTraj.left.getSegment(i).dt*i;
			  this.leftAccel[i][1] = this.leftRightTraj.left.getSegment(i).acc;
			  this.rightAccel[i][0] = this.leftRightTraj.right.getSegment(i).dt*i;
			  this.rightAccel[i][1] = this.leftRightTraj.right.getSegment(i).acc;
			  
			  this.leftJerk[i][0] = this.leftRightTraj.left.getSegment(i).dt*i;
			  this.leftJerk[i][1] = this.leftRightTraj.left.getSegment(i).jerk;
			  this.rightJerk[i][0] = this.leftRightTraj.right.getSegment(i).dt*i;
			  this.rightJerk[i][1] = this.leftRightTraj.right.getSegment(i).jerk;
			  
		  }
	  }
	  

	  private static boolean writeFile(String path, String data) {
		    try {
		      File file = new File(path);

		      // if file doesnt exists, then create it
		      if (!file.exists()) {
		          file.createNewFile();
		          
		      }

		      FileWriter fw = new FileWriter(file.getAbsoluteFile());
		      BufferedWriter bw = new BufferedWriter(fw);
		      bw.write(data);
		      bw.close();
		    } catch (IOException e) {
		      return false;
		    }
		    
		    return true;
		  }
	  
	  public static String joinPath(String path1, String path2)
	  {
	      File file1 = new File(path1);
	      File file2 = new File(file1, path2);
	      return file2.getPath();
	  }
	  
	  public Trajectory getTrajectory()
	  {
		  return traj;
	  }
	  
	  public String getName()
	  {
		  return path_name;
	  }
	  
	  public Trajectory getLeftTrajectory()
	  {
		  return this.leftRightTraj.left;
	  }
	  
	  public Trajectory getRightTrajectory()
	  {
		  return this.leftRightTraj.right;
	  }
	  
	}
	  
