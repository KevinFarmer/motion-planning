package motion;

import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import motion.RobotArmProblem.RobotArmNode;


public class RobotArmDriver {

	private static int RECT_DIM = 30;
	
	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		
		
		int k = 10; //k param in PRM
		int numSegments = 2; //Set numSegments to 2, 3 or 4
		int numObstacles = 15; //Set to be 5, or 15

		
		Rectangle[] rect = new Rectangle[numObstacles];
		if (numObstacles == 5) {
			rect[0] = new Rectangle(60, -50, RECT_DIM, RECT_DIM);
			rect[1] = new Rectangle(163, 94, RECT_DIM, RECT_DIM);
			rect[2] = new Rectangle(-130, -70, RECT_DIM, RECT_DIM);
			rect[3] = new Rectangle(2, 100, RECT_DIM, RECT_DIM);
			rect[4] = new Rectangle(2, -90, RECT_DIM, RECT_DIM);
		}
		if (numObstacles == 15) {
			rect[0] = new Rectangle(80, -150, RECT_DIM, RECT_DIM);
			rect[1] = new Rectangle(163, 94, RECT_DIM, RECT_DIM);
			rect[2] = new Rectangle(-130, -70, RECT_DIM, RECT_DIM);
			rect[3] = new Rectangle(2, 110, RECT_DIM, RECT_DIM);
			rect[4] = new Rectangle(2, -90, RECT_DIM, RECT_DIM);
			rect[5] = new Rectangle(102, 30, RECT_DIM, RECT_DIM);
			rect[6] = new Rectangle(10, -190, RECT_DIM, RECT_DIM);
			rect[7] = new Rectangle(-130, 40, RECT_DIM, RECT_DIM);
			rect[8] = new Rectangle(-90, -150, RECT_DIM, RECT_DIM);
			rect[9] = new Rectangle(-30, 190, RECT_DIM, RECT_DIM);
			rect[10] = new Rectangle(140, -40, RECT_DIM, RECT_DIM);
			rect[11] = new Rectangle(-40, -300, RECT_DIM, RECT_DIM);
			rect[12] = new Rectangle(100, -250, RECT_DIM, RECT_DIM);
			rect[13] = new Rectangle(-100, 160, RECT_DIM, RECT_DIM);
			rect[14] = new Rectangle(140, 200, RECT_DIM, RECT_DIM);
		}
		
		double[] theta = new double[numSegments];
		double[] goal = new double[numSegments];
		if (numSegments == 2) {
			theta[0] = Math.PI*3/2;
			theta[1] = Math.PI*3/2;
			goal[0] = 0;
			goal[1] = Math.PI/2;
		}
		
		if (numSegments == 3) {
			theta[0] = 0;
			theta[1] = Math.PI*3/2;
			theta[2] = Math.PI*3/2;
			
			goal[0] = Math.PI*3/2;
			goal[1] = 0;
			goal[2] = Math.PI/2;
		}
		
		
		if (numSegments == 4) {
			theta[0] = Math.PI*13/12;
			theta[1] = Math.PI*23/12;
			theta[2] = Math.PI/4;
			theta[3] = Math.PI*3/2;
			
			goal[0] = 0;
			goal[1] = Math.PI/2;
			goal[2] = 0;
			goal[3] = 0;
		}
		 

		
		int[] ln = new int[numSegments];
		for (int i = 0; i < numSegments; i++)
			ln[i] = 100;
		
		RobotArmProblem arm = new RobotArmProblem(theta, goal, ln, numSegments, rect);
		
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
		
		//If using 4 segments, do a second query
		if (numSegments == 4) {
			System.out.println("Starting next query...");
			theta[0] = Math.PI *3/2;
			theta[1] = 0;
			theta[2] = Math.PI/4;
			theta[3] = 0;
			
			goal[0] = 0;
			goal[1] = Math.PI*3/2;
			goal[2] = Math.PI/2;
			goal[3] = Math.PI*3/2;
			
			sol = arm.PRM(k, theta, goal);
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
		}
 

		
		
		
		
	}
	
	
		
}
