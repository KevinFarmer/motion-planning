package motion;

import java.lang.reflect.InvocationTargetException;


import motion.Point;


public class RobotArmDriver {

	
	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		int k;
		int numObstacles = 3;
		
		Point[] obstacles = new Point[numObstacles];
		obstacles[0] = new Point(20, 120);
		obstacles[1] = new Point(163, 94);
		obstacles[2] = new Point(-10, -70);
		
		//k = 4;
		//double[] theta = {Math.PI*6/4, 0, Math.PI/4, 0};
		//double[] goal = {0, Math.PI*3/2, Math.PI/2, 0};
		k = 2;
		double[] theta = {0, Math.PI/8};
		double[] goal = {Math.PI*3/2, Math.PI/2};
		
		int[] ln = new int[k];
		for (int i = 0; i < k; i++)
			ln[i] = 100;
		
		RobotArmProblem arm = new RobotArmProblem(theta, goal, ln, k, obstacles);
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
        
        

        
		//try {
		//	Thread.sleep(2000);
		//} catch (InterruptedException e) {
		//	e.printStackTrace();
		//}
		
		arm.PRM(10);
		

		
		//arm.updatePrintNode(theta2);
		
	}
	
	
	
	
	
}
