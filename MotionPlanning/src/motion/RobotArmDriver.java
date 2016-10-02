package motion;

import javax.swing.*;

import motion.Point;
import motion.RobotArmNode;

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
		double[] goal = {Math.PI*3/2, Math.PI/2};
		
		int[] ln = new int[k];
		for (int i = 0; i < k; i++)
			ln[i] = 100;
		
		RobotArmProblem arm = new RobotArmProblem(theta, goal, ln, k, obstacles);
		//RobotArmNode start = arm.startNode;
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                arm.createAndShowGUI();
            }
        });
		

		
		double[] theta2 = {Math.PI, Math.PI*3/2};
		//arm.updateNode(theta2);
		
	}
	
	
	
	
	
}
