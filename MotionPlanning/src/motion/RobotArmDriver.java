package motion;

import javax.swing.*;

import motion.RobotArm.Point;

public class RobotArmDriver {

	
	public static void main(String[] args) {
		int k;
		
		
		k = 4;
		double[] theta = {Math.PI*6/4, 0, Math.PI*4, 0};
		//k = 2;
		//double[] theta = {Math.PI, 0};
		
		int[] ln = new int[k];
		for (int i = 0; i < k; i++)
			ln[i] = 100;
		
		RobotArm arm = new RobotArm(theta, ln, k);
		
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
