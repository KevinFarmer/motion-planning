package motion;

import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import motion.RobotArmProblem.RobotArmNode;


public class RobotArmDriver {

	private static int RECT_DIM = 30;
	
	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		int k = 10; //k param in PRM
		int numSegments;
		int numObstacles = 5;
		
		Rectangle[] rect = new Rectangle[numObstacles];
		rect[0] = new Rectangle(60, -50, RECT_DIM, RECT_DIM);
		rect[1] = new Rectangle(163, 94, RECT_DIM, RECT_DIM);
		rect[2] = new Rectangle(-130, -70, RECT_DIM, RECT_DIM);
		rect[3] = new Rectangle(2, 100, RECT_DIM, RECT_DIM);
		rect[4] = new Rectangle(2, -90, RECT_DIM, RECT_DIM);


		//numSegments = 2;
		//double[] theta = {Math.PI*3/2, Math.PI*3/2};
		//double[] goal = {0, Math.PI/2};
		
		numSegments = 4;
		double[] theta = {Math.PI*6/4, 0, Math.PI/4, 0};
		double[] goal = {0, Math.PI*3/2, Math.PI/2, Math.PI*3/2};
		
		
		//numSegments = 3;
		//double[] theta = {0, Math.PI*3/2, Math.PI*3/2};
		//double[] goal = {Math.PI*3/2, 0, Math.PI/2};
		 

		
		int[] ln = new int[numSegments];
		for (int i = 0; i < numSegments; i++)
			ln[i] = 100;
		
		RobotArmProblem arm = new RobotArmProblem(theta, goal, ln, numSegments, rect);
		
        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                arm.createAndShowGUI();
            }
        }); 
        
		
		List<RobotArmNode> sol = arm.PRM(k);
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
		double[] theta2 = {Math.PI*3/2, Math.PI*5/4 , Math.PI*3/2};
		double[] goal2 = {Math.PI/2, 0, Math.PI*3/2}; 
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
