package motion;

import java.lang.reflect.InvocationTargetException;

public class RobotCarDriver {
	
	
	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		
		int strX = 30;
		int strY = 50;
		double startTheta = Math.PI/2;
		int glX = 90;
		int glY = 90;
		
		
		RRTProblem car = new RobotCarProblem(strX, strY, startTheta, glX, glY);
		
		
		/*
        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                car.createAndShowGUI();
            }
        }); */
		
        
        //car.rrtExploration();
		
	}
	
	
	
	
	

}
