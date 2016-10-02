package motion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;

import javax.swing.*;

public class RobotArm {
	
	private final int RECT_DIM = 30;

	private double[] theta;
	private int[] length;
	private int numSegments;
	private Point[] obstacles;
	
	public RobotArm(double[] angles, int[] ln, int k, Point[] obs){
		theta = angles;
		length = ln;
		numSegments = k;
		obstacles = obs;	
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
	
	//Returns true if there is an intersection
	public boolean isIntersect() {
		Rectangle[] rect = new Rectangle[obstacles.length];
		Point[] points = getPoints();
		Line2D[] lines = new Line2D[numSegments];
		
		for (int i = 0; i < obstacles.length; i++){
			rect[i] = new Rectangle(obstacles[i].x, obstacles[i].y, RECT_DIM, RECT_DIM);
		}
		
		lines[0] = new Line2D.Double(0,0, points[0].x, points[0].y);
		for (int i = 0; i < numSegments-1; i++) {
			double x1 = (double) (points[i].x);
			double y1 = (double) (points[i].y);
			double x2 = (double) (points[i+1].x);
			double y2 = (double) (points[i+1].y);
			lines[i+1] = new Line2D.Double(x1, y1, x2, y2);
		}
		
		for (int i = 0; i < rect.length; i++) {
			for (int j = 0; j < numSegments; j++) {
				if (rect[i].intersectsLine(lines[j]))
					return true;
			}
		}
		//Rectangle r1 = new Rectangle(100, 100, 100, 100);
		//Line2D l1 = new Line2D.Float(0, 200, 200, 0);
		//System.out.println("l1.intsects(r1) = " + l1.intersects(r1));
		
		return false;
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
    	
    	
    	//Does the actual drawing for the class
        private void doDrawing(Graphics g) {

            Graphics2D g2d = (Graphics2D) g;
            
            if(isIntersect()) {
            	setBackground(Color.red);
            } else {
            	setBackground(Color.white);
            }

            //This centers (0, 0)
            AffineTransform tform = AffineTransform.getTranslateInstance( getWidth()/2, getHeight()/2);
            tform.scale( 1, -1);
            g2d.setTransform(tform);
            
            //Draw obstacles
            for (int i = 0; i < obstacles.length; i++)
            	g2d.fillRect(obstacles[i].x, obstacles[i].y, RECT_DIM, RECT_DIM);
            
            Point[] points = getPoints();
            g2d.drawLine(0, 0, points[0].x, points[0].y);
            
            //Draw robot arm
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
