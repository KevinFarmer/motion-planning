package motion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.*;

public class RobotArm {

	private double[] theta;
	private int[] length;
	private int numSegments;
	
	public RobotArm(double[] angles, int[] ln, int k){
	
		theta = angles;
		length = ln;
		numSegments = k;
	
	
	}
	
	public class Point {
		public int x, y;
		
		public Point(int xCoord, int yCoord) {
			x = xCoord;
			y = yCoord;
		}
	}
	
	//Returns the current set of points, excluding the origin
	public Point[] getPoints() {
		Point[] points = new Point[numSegments];
		
		double sumTheta = 0;
		for (int i = 0; i < numSegments; i++) {
			int x, y;
			sumTheta += theta[i];
			
			x = (int) (length[i] * Math.cos(sumTheta));
			y = (int) (length[i] * Math.sin(sumTheta));	
			
			if (i != 0) {
				x += points[i-1].x;
				y += points[i-1].y;
			}
			
			points[i] = new Point(x, y);
		}
		
		return points;
	}
	
	//Sum the total length of the robot arm
	public int sumLength() {
		int sum = 0;
		for (int i = 0; i < numSegments; i++)
			sum += length[i];
		
		return sum;
	}
	
	
    public void createAndShowGUI() {
    	int dim = 100 + 2*sumLength();
    	
        //Create and set up the window.
        JFrame jf = new JFrame("RobotArm");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        jf.getContentPane().setLayout(new BorderLayout());
        
        JPanel jp = new RobotArmPanel();
        jp.setPreferredSize(new Dimension(dim, dim));
        jp.setMinimumSize(new Dimension(dim, dim));
        jp.setBackground(Color.white);
        
        jf.getContentPane().add(jp, BorderLayout.CENTER);
        
        //Display the window.
        jf.pack();
        jf.setVisible(true);
    }
	
    //Used to display the current configuration of the RobotArm
    public class RobotArmPanel extends JPanel {
    	
    	private final int RECT_DIM = 30;
    	
    	//Does the actual drawing for the class
        private void doDrawing(Graphics g) {

            Graphics2D g2d = (Graphics2D) g;

            //This centers (0, 0)
            AffineTransform tform = AffineTransform.getTranslateInstance( getWidth()/2, getHeight()/2);
            tform.scale( 1, -1);
            g2d.setTransform(tform);
            
            //x,y,width,height
            g2d.fillRect(30, 30, RECT_DIM, RECT_DIM);
            
            Point[] points = getPoints();
            g2d.drawLine(0, 0, points[0].x, points[0].y);
            
            for (int i = 0; i < numSegments-1; i++) {
            	//System.out.println("("+points[i].x+", "+points[i].y+")");
            	g2d.drawLine(points[i].x, points[i].y, points[i+1].x, points[i+1].y);
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            doDrawing(g);          
        }
    	
    	
    	
    }
	
	
	
	
}
