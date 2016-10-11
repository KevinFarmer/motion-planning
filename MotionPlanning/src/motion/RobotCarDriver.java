package motion;

import java.awt.geom.Line2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import motion.RRTProblem.RRTNode;

public class RobotCarDriver {
	
	
	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		
		int strX = 0;
		int strY = 0;
		double startTheta = 0;// Math.PI/2;
		int glX = 90;
		int glY = 90;
		
		List<Line2D.Double> obstacles = new ArrayList<Line2D.Double>(); //Add points in pairs
		
		obstacles.add(new Line2D.Double(-50, 50, 200, 50));
		obstacles.add(new Line2D.Double(-100, -50, 100, -50));
		//obstacles.add(new Line2D.Double(-200, 0, -200, 400
		
		int dim = 400;
		
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
