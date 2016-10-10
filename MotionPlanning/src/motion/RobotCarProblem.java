package motion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class RobotCarProblem extends RRTProblem {
	
	private final int VELOCITY = 10;
	
	RobotCarPanel jp;

	public RobotCarProblem (int strX, int strY, double startTheta, int glX, int glY) {
		
		startNode = new RobotCarNode(strX, strY, startTheta);
		
		
		
		
	}
	
	
	private class RobotCarNode implements RRTNode {
		
		private int x, y;
		private double theta;
		
		public RobotCarNode(int currX, int currY, double currTheta) {
			x = currX;
			y = currY;
			theta = currTheta;	
			
			this.rotatePos(10, 10);
		}
		
		public List<RRTNode> getSuccessors() {
			List<RRTNode> successors = new ArrayList<RRTNode>();;
			return successors;
		}
		
		
		//Change return to RobotCarNode
		private void rotatePos(int v, int w) {
			
			double rotateAngle = Math.PI/2; //temp  -----------------------
			
			double rotateDir = theta + (Math.signum(w)*Math.PI/2);
			double l = v/w;
			int rotateX = (int) (x+(Math.abs(l)*Math.cos(rotateDir)));
			int rotateY = (int) (y+(Math.abs(l)*Math.sin(rotateDir)));
			
			System.out.println("r:"+rotateDir);
			System.out.println(x+","+y);
			System.out.println("."+rotateX+","+rotateY);
			
			int relX = x - rotateX;
			int relY = y - rotateY;
			
			System.out.println(":"+relX+","+relY);
			
			int newRelX = (int) (relX*Math.cos(rotateAngle) - relY*Math.sin(rotateAngle));
			int newRelY = (int) (relY*Math.cos(rotateAngle) + relX*Math.sin(rotateAngle));
			
			System.out.println(newRelX+ " _ " +newRelY);
			
			int newX = rotateX + newRelX;
			int newY = rotateY + newRelY;
			
			System.out.println(newX+" "+newY);
			
			
			//return new RobotCarNode(0, 0, 0);
		}
		
		
		
		
	}
	
	
	
	
	public void createAndShowGUI() {
    	int dim = 400; //Change for scalability later
    	
        //Create and set up the window.
        JFrame jf = new JFrame("RobotCar");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        jf.getContentPane().setLayout(new BorderLayout());
        
        jp = new RobotCarPanel();
        jp.setPreferredSize(new Dimension(dim, dim));
        jp.setMinimumSize(new Dimension(dim, dim));
        jp.setBackground(Color.white);
        
        jf.getContentPane().add(jp, BorderLayout.CENTER);
        
        
        //Display the window.
        jf.pack();
        jf.setVisible(true);
		
		
	}
	
	
	//Used to display the current configuration of the RobotCar
    private class RobotCarPanel extends JPanel {
		private static final long serialVersionUID = 1L; //Default

		//Does the actual drawing for the class
        private void doDrawing(Graphics g) {

            Graphics2D g2d = (Graphics2D) g;
        	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            /*
            if(printNode.isIntersect()) {
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
            	//g2d.fill(new Ellipse2D.Double(obstacles[i].x, obstacles[i].y, CIRC_DIM, CIRC_DIM));
            	g2d.fillRect(obstacles[i].x, obstacles[i].y, CIRC_DIM, CIRC_DIM);
            
            Point[] points = printNode.getPoints();
            g2d.drawLine(0, 0, points[0].x, points[0].y);
            
            //Draw robot arm
            for (int i = 0; i < numSegments-1; i++) {
            	//System.out.println("("+points[i].x+", "+points[i].y+")");
            	g2d.drawLine(points[i].x, points[i].y, points[i+1].x, points[i+1].y);
            } */
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            doDrawing(g);          
        }
    }
	
}
