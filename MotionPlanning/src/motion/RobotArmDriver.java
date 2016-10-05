package motion;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import motion.Point;
import motion.RobotArmProblem.RobotArmNode;


public class RobotArmDriver {

	
	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		int k = 10; //k param in PRM
		int numSegments;
		int numObstacles = 5;
		
		Point[] obstacles = new Point[numObstacles];
		obstacles[0] = new Point(60, -50);
		obstacles[1] = new Point(163, 94);
		obstacles[2] = new Point(-130, -70);
		obstacles[3] = new Point(2, 100);
		obstacles[4] = new Point(2, -90);
		
		
		
		//numSegments = 2;
		//double[] theta = {Math.PI*3/2, Math.PI*3/2};
		//double[] goal = {0, Math.PI/2};
		
		numSegments = 4;
		double[] theta = {Math.PI*6/4, 0, Math.PI/4, 0};
		double[] goal = {0, Math.PI*3/2, Math.PI/2, Math.PI*3/2};
		
		/*numSegments = 3;
		double[] theta = {0, Math.PI*3/2, Math.PI*3/2};
		double[] goal = {Math.PI*3/2, 0, Math.PI/2};
		double[] theta2 = {Math.PI*3/2, Math.PI*5/4 , Math.PI*3/2};
		double[] goal2 = {Math.PI/2, 0, Math.PI*3/2}; */

		
		int[] ln = new int[numSegments];
		for (int i = 0; i < numSegments; i++)
			ln[i] = 100;
		

		
		RobotArmProblem arm = new RobotArmProblem(theta, goal, ln, numSegments, obstacles);
		//RobotArmNode start = arm.startNode;
		

		/* 
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                arm.createAndShowGUI();
            }
        }); */
        
        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                arm.createAndShowGUI();
            }
        }); 
        
		
		List<RobotArmNode> sol = arm.PRM(k, theta, goal);
		if (sol != null){
			for (RobotArmNode n : sol) {
				arm.updatePrintNode(n.getTheta());
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("\nDone!");
		
		/*
		sol = arm.PRM(k, theta2, goal2);
		if (sol != null){
			for (RobotArmNode n : sol) {
				arm.updatePrintNode(n.getTheta());
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} */
		
		
		
		
	}
	
	
		
}
