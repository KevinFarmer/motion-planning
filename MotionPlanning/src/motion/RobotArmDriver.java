package motion;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import motion.Point;
import motion.RobotArmProblem.RobotArmNode;


public class RobotArmDriver {

	
	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		int k;
		int numObstacles = 3;
		
		Point[] obstacles = new Point[numObstacles];
		obstacles[0] = new Point(20, 120);
		obstacles[1] = new Point(163, 94);
		obstacles[2] = new Point(-100, -70);
		
		//k = 4;
		//double[] theta = {Math.PI*6/4, 0, Math.PI/4, 0};
		//double[] goal = {0, Math.PI*3/2, Math.PI/2, 0};
		k = 1;
		double[] theta = {0};
		double[] goal = {Math.PI*3/2};
		
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
		
		List<RobotArmNode> sol = arm.PRM(10);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("here");
		
		//if (true)
		//	return;
		
		for (RobotArmNode n : sol) {
			for (int i = 0; i < theta.length; i++)
				System.out.print(n.getTheta()[i]+"  ");
			System.out.println();
			
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			arm.updatePrintNode(n.getTheta());
			
		}
		
		//arm.updatePrintNode(theta2);
		
	}
	
	
	
	
	
}
