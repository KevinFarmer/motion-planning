package motion;

import javax.swing.*;

import motion.Point;

public class RobotArmDriver {

	
	public static void main(String[] args) {
		int k;
		int numObstacles = 3;
		Point[] obstacles = new Point[numObstacles];
		obstacles[0] = new Point(20, 120);
		obstacles[1] = new Point(80, 70);
		obstacles[2] = new Point(-40, -160);
		
		//k = 4;
		//double[] theta = {Math.PI*6/4, 0, Math.PI*4, 0};
		k = 2;
		double[] theta = {Math.PI*3/8, Math.PI*1/4};
		
		int[] ln = new int[k];
		for (int i = 0; i < k; i++)
			ln[i] = 100;
		
		RobotArm arm = new RobotArm(theta, ln, k, obstacles);
		
		Point[] points = arm.getPoints();
		
		for (int i = 0; i < k; i++) {
			
			System.out.println("("+points[i].x+", "+points[i].y+")");
		}
		
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                arm.createAndShowGUI();
            }
        });
		
		
	}
	
	
	
	
	
}
