package motion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import motion.RRTProblem.RRTNode;


public class RobotCarProblem extends RRTProblem {
	
	//private final int VELOCITY = 16;
	private final int CIRC_DIM = 30;
	private final int ACCUR = 4;
	
	private List<Line2D.Double> obstacles;
	private int panelDim;
	private int goalX;
	private int goalY;
	
	
	RobotCarPanel jp;
	private RRTNode printNode;
	private List<Path2D.Double> paths;
	private List<Arc2D.Double> arcs; 

	public RobotCarProblem (int strX, int strY, double startTheta, int glX, int glY, 
			List<Line2D.Double> obs, int dim) {
		
		startNode = new RobotCarNode(strX, strY, startTheta);
		goalNode = new RobotCarNode(glX, glY, startTheta);
		goalX = glX;
		goalY = glY;
		
		obstacles = obs;
		obstacles.add(new Line2D.Double(-dim/2, -dim/2, -dim/2, dim/2)); //Left wall
		obstacles.add(new Line2D.Double(-dim/2, dim/2, dim/2, dim/2));	//Top wall
		obstacles.add(new Line2D.Double(dim/2, dim/2, dim/2, -dim/2));	//Right wall
		obstacles.add(new Line2D.Double(dim/2, -dim/2, -dim/2, -dim/2));	//Top wall
		
		panelDim = dim;
		printNode = startNode;
		paths = new ArrayList<Path2D.Double>();
		arcs = new ArrayList<Arc2D.Double>();
		
	}
	
	
	private class RobotCarNode implements RRTNode {
		
		private int x, y;
		private double theta;
		
		public RobotCarNode(int currX, int currY, double currTheta) {
			x = currX;
			y = currY;
			theta = currTheta;	
			
			//this.rotatePos(20, -5);
		}
		
		public int getX() { return x; }
		public int getY() { return y; }
		
		public boolean goalTest() { 
			//return (x == goalX) && (y == goalY); 
			return Math.abs(x - goalX) < ACCUR && Math.abs(y - goalY) < ACCUR;
		}
		
		
		//Checks collision between the car and the walls
		public boolean isIntersect() {
			if (y == 40) {
				//System.out.println("here");
			}
			Ellipse2D pos = new Ellipse2D.Double(x-CIRC_DIM/2, y-CIRC_DIM/2, 
												CIRC_DIM, CIRC_DIM);
            for (int i = 0; i < obstacles.size(); i++) {
            	if (pos.getBounds().intersectsLine(obstacles.get(i)))
            		return true;
            }
			return false;
		}
		
		
		//Returns the six successors positions, unless intersecting with a wall
		public List<RRTNode> getSuccessors() {
			List<RRTNode> successors = new ArrayList<RRTNode>();
			
			RRTNode newNode;
			
			newNode = rotatePos(20, 1);
			if (newNode != null) {
				successors.add(newNode);
				//return successors;
			} 

			
			newNode = rotatePos(20, -1);
			if (newNode != null) {
				successors.add(newNode);
				//return successors;
			} 
			
			newNode = rotatePos(-20, 1);
			if (newNode != null)
				successors.add(newNode);
			
			newNode = rotatePos(-20, -1);
			if (newNode != null)
				successors.add(newNode);
			
			newNode = rotatePos(20, 0);
			if (newNode != null)
				successors.add(newNode);
			
			newNode = rotatePos(-20, 0);
			if (newNode != null)
				successors.add(newNode);
			
			
			/*
			System.out.println("\n\n\n"+successors.size()+"\n\n\n");
			for (RRTNode node : successors) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				printNode = node;
				jp.repaint();
			} */
			
			return successors;
		}
		
		
		//Change return to RobotCarNode
		private RobotCarNode rotatePos(int v, int w) {
			
			if (w == 0) {
				int newX = (int) (x+(v* Math.cos(theta)));
				int newY = (int) (y+(v* Math.sin(theta)));
				
				
				RobotCarNode newNode = new RobotCarNode(newX, newY, theta);
				if (!newNode.isIntersect()) {
					Path2D.Double path = new Path2D.Double();
					path.moveTo(x, y);
					path.lineTo(newX, newY);
					paths.add(path);
					
					return newNode;
				} else {
					return null;
				}			
			}
			
			double rotateAngle = Math.PI/4; //temp  -----------------------
			double rotateDir;   //The direction that the point of rotation is in
			double rotateTheta; //The angle to rotate around the point by
			
			//rotateDir = theta + Math.signum(w)*Math.PI/2; //Perpendicular to heading of robot
			
			//determine direction of the rotation point
			if (Math.signum(w) == 1) { //To the left of current heading
				rotateDir = (theta + Math.PI/2) % (2*Math.PI);
			} else {				//To the right of current heading
				rotateDir = (theta + Math.PI*3/2) % (2*Math.PI);
			} 
			
			//Determine angle to rotate by 
			if (Math.signum(w) != Math.signum(v)) {
				rotateTheta = (2*Math.PI - rotateAngle) % (2*Math.PI);
			} else {
				rotateTheta = rotateAngle;
			}
		
			//double rotateDir = (theta + rotateAngle) % (2*Math.PI);
			
			double newTheta = (theta + rotateTheta) % (2*Math.PI);
			
			double l = v/w;
			int rotateX = (int) (x+ (Math.abs(l)*Math.cos(rotateDir)));
			int rotateY = (int) (y+ (Math.abs(l)*Math.sin(rotateDir)));
			
			//System.out.println("rotateAngle:"+rotateAngle);
			//System.out.println("rotateDir:"+rotateDir);
			//System.out.println("rotateTheta:"+rotateTheta);
			//System.out.println(x+","+y + " : "+theta);
			//System.out.println("."+rotateX+","+rotateY);
			
			int relX = x - rotateX;
			int relY = y - rotateY;
			
			//System.out.println(":"+relX+","+relY);
			
			int newRelX = (int) (relX*Math.cos(rotateTheta) - relY*Math.sin(rotateTheta));
			int newRelY = (int) (relY*Math.cos(rotateTheta) + relX*Math.sin(rotateTheta));
			
			//System.out.println(newRelX+ " _ " +newRelY);
			
			int newX = rotateX + newRelX;
			int newY = rotateY + newRelY;
			
			//System.out.println(newX+" "+newY+" : "+newTheta+"\n");
			RobotCarNode newNode = new RobotCarNode(newX, newY, newTheta);
			if (!newNode.isIntersect()) {

				Path2D.Double path = new Path2D.Double();
				path.moveTo(x, y);
				path.lineTo(newX, newY);
				paths.add(path);
				/*
				int wid = Math.abs(rotateX - x);
				double start, rot;
				double curveTheta = Math.signum(w)*rotateAngle;
				//start = Math.toDegrees((2*Math.PI - theta)% (2*Math.PI));
				start = 360 - (180+Math.toDegrees(rotateDir))%180;
				//start = Math.toDegrees(rotateDir);
				rot = Math.toDegrees(curveTheta);

				System.out.println(Math.toDegrees(rotateDir));
				System.out.println("s:"+start+" e:"+rot);
				System.out.println("rot: "+rotateX+","+rotateY);
				Arc2D.Double arc = new Arc2D.Double(rotateX-wid, rotateY-wid, 2*wid, 2*wid, 
						start, -1*Math.signum(w)*Math.signum(l)*rot, Arc2D.OPEN);
				arcs.add(arc); */
				
				return newNode;
			} else {
				return null;
			}

		}
		
		
		//Loops through the leaf nodes, and finds the closest
		public Tree<RRTNode> getNearestLeaf(List<Tree<RRTNode>> leaves) {
			double minDist = Double.MAX_VALUE;
			Tree<RRTNode> minNode = null;
			
			//loop through list
			for (Tree<RRTNode> t : leaves) {
				RRTNode node = t.getHead();
				double dist = this.getDist(node);
				if (dist < minDist) {
					minDist = dist;
					minNode = t;
				}	
			}
			
			
			return minNode;
		}
		
		
		//Gets euclidean distance between two nodes
		private double getDist(RRTNode other) {
			int deltaX = Math.abs(x - other.getX());
			int deltaY = Math.abs(y - other.getY());
			
			return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
		}
		
		public String toString() {
			return "("+x+","+y+") "+theta;
		}
		
		@Override
		public boolean equals(Object other) {
			RobotCarNode o = (RobotCarNode) other;
			//return x == o.x && y == o.y && Math.abs(theta - o.theta) < (Math.PI/90);
			return Math.abs(x - o.x) < ACCUR && Math.abs(y - o.y) < ACCUR && Math.abs(theta - o.theta) < (Math.PI/45);
		}
		
		@Override
		public int hashCode() {
			return (int) (100*x + 10*y + theta);
		}
		
	}
	
	
	
	
	public void createAndShowGUI() {
    	
        //Create and set up the window.
        JFrame jf = new JFrame("RobotCar");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        jf.getContentPane().setLayout(new BorderLayout());
        
        jp = new RobotCarPanel();
        jp.setPreferredSize(new Dimension(panelDim+10, panelDim+10));
        jp.setMinimumSize(new Dimension(panelDim+10, panelDim+10));
        jp.setBackground(Color.white);
        
        jf.getContentPane().add(jp, BorderLayout.CENTER);
        
        
        //Display the window.
        jf.pack();
        jf.setVisible(true);
		
	}
	
	public void updatePrintNode(RRTNode node) {
		printNode = node;
    	jp.repaint();
	}
	
	
	protected RRTNode getRandomSample() {
		Random r = new Random();
		int x = r.nextInt(panelDim/2);
		int y = r.nextInt(panelDim/2);
		
		if (0 == r.nextInt(2))
			x *= -1;
		
		if (0 == r.nextInt(2))
			y*= -1;
		
		//System.out.println("rand "+x+" "+y);
		return new RobotCarNode(x, y, 0);
	}
	
	
	//Used to display the current configuration of the RobotCar
    private class RobotCarPanel extends JPanel {
		private static final long serialVersionUID = 1L; //Default

		//Does the actual drawing for the class
        private void doDrawing(Graphics g) {

            Graphics2D g2d = (Graphics2D) g;
        	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        	
        	g.setColor(Color.BLACK);
            
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
            for (int i = 0; i < obstacles.size(); i++) {
            	g2d.draw(obstacles.get(i));
            }
            
            //Draw straight paths
            for (int i = 0; i < paths.size(); i++)
            	g2d.draw(paths.get(i));
            
            //Draw arcs
            for (int i = 0; i < arcs.size(); i++)
            	g2d.draw(arcs.get(i));
            
            Color myColor = new Color(0.2f, 0.8f, 0.2f, 0.7f);
            g.setColor(myColor);
            //g2d.drawLine(0,0,0,0);

            g2d.fillOval(printNode.getX()-CIRC_DIM/2, printNode.getY()-CIRC_DIM/2, CIRC_DIM, CIRC_DIM);
            
            myColor = new Color(0.8f, 0.2f, 0.2f, 0.7f);
            g.setColor(myColor);
            g2d.fillOval(goalX-CIRC_DIM/2, goalY-CIRC_DIM/2, CIRC_DIM, CIRC_DIM);
            
            /*
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
