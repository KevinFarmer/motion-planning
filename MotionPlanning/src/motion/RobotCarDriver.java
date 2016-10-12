package motion;

import java.awt.geom.Line2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import motion.RRTProblem.RRTNode;

public class RobotCarDriver {
	
	
	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		
		List<Line2D.Double> obstacles = new ArrayList<Line2D.Double>(); //Add points in pairs
		
		//Simple problem 
		
		int strX = 0;
		int strY = 0;
		double startTheta = 0;
		int glX = 90;
		int glY = 90;
		int dim = 400;
		
		
		//Problem with two walls
		/*
		int strX = -150;
		int strY = -100;
		double startTheta = 0;
		int glX = 90;
		int glY = 90;
		int dim = 400;
		obstacles.add(new Line2D.Double(-50, 50, 200, 50));
		obstacles.add(new Line2D.Double(-200, -50, 100, -50));
		*/
		
		
		//Complex problem
		/*
		int strX = -300;
		int strY = 350;
		double startTheta = Math.PI/2;
		int glX = 337;
		int glY = -296;
		int dim = 800;
		obstacles.add(new Line2D.Double(-260, 400, -260, -200));
		obstacles.add(new Line2D.Double(-200, -400, -200, 350));
		obstacles.add(new Line2D.Double(-200, 350, 350, 350));
		obstacles.add(new Line2D.Double(0, 250, 380, 250));
		obstacles.add(new Line2D.Double(-200, 0, 300, 0));
		obstacles.add(new Line2D.Double(-100, -200, 390, -200));
		*/

		
		RRTProblem car = new RobotCarProblem(strX, strY, startTheta, glX, glY, obstacles, dim);
		
		
		
        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                car.createAndShowGUI();
            }
        }); 
		
        
        List<RRTNode> sol = car.rrtExploration();
        for (RRTNode node : sol) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	System.out.println(node);
        	car.updatePrintNode(node);
        }
		
	}
	
	
	
	
	

}
