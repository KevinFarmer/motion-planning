package motion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;



public class RobotArmProblem {
	
	private final int CIRC_DIM = 30;
	private final double RESOLUTION = Math.PI/180; //1 degree increments
	private final int TOTAL_SAMPLES = 1000;

	private int[] length;
	private int numSegments;
	private Point[] obstacles;
	
	private RobotArmNode startNode;
	private RobotArmNode goalNode;
	
	//Used for graphics printing
	private JPanel jp;
	private RobotArmNode printNode;
	

	
	public RobotArmProblem(double[] strAngles, double[] goalAngles, int[] ln, int k, Point[] obs) {
		startNode = new RobotArmNode(strAngles);
		goalNode = new RobotArmNode(goalAngles);
		length = ln;
		numSegments = k;
		obstacles = obs;
		printNode = startNode;
	}
	
	

	public void printStart() {
		Point[] points = startNode.getPoints();
		for (int i = 0; i < numSegments; i++) 
			System.out.println("("+points[i].x+", "+points[i].y+")");
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
        
        jp = new RobotArmPanel();
        jp.setPreferredSize(new Dimension(dim, dim));
        jp.setMinimumSize(new Dimension(dim, dim));
        jp.setBackground(Color.white);
        
        jf.getContentPane().add(jp, BorderLayout.CENTER);
        
        
        //Display the window.
        jf.pack();
        jf.setVisible(true);
    }
    
    
    public void updatePrintNode(double[] theta) {
	
    	printNode = new RobotArmNode(theta);
    	//System.out.println(jp);
    	jp.repaint();
    }	
    
    
    

    
    //Arg k: the number of vertices to link to
    public List<RobotArmNode> PRM(int k) {
    	List<RobotArmNode> sol = new ArrayList<RobotArmNode>();
    	RobotArmGraph graph = genRobotGraph();
    	

    	for (RobotArmNode node : graph.getKeySet()) {
    		getNeighbors(node, graph, k);
    		//for (int i = 0; i < node.theta.length; i++)
    		//	System.out.print(node.theta[i]+"  ");
    		//break;
    	}	
    			
    	//Test print
    	for (RobotArmNode node : graph.getKeySet()) {
    		List<RobotArmNode> successors = graph.getAdj(node);
    		for (int i = 0; i < k; i++) {
		
    			for (int j = 0; j < successors.get(i).theta.length; j++)
    				System.out.print(successors.get(i).theta[j]+" ");
    			System.out.println(successors.get(i).getPoints()[numSegments-1]);
			
    		}
    		System.out.println(); 
    	}
    	
    	
		double[] theta = {Math.PI/16, Math.PI/3};
    	boolean b = startNode.connectNode(new RobotArmNode(theta));
    	
    	System.out.println("\n"+b);
    	
    	return sol;
    }
    
    
    //Generates a graph
    private RobotArmGraph genRobotGraph() {
    	RobotArmGraph graph = new RobotArmGraph();
    	
    	//Randomly generate TOTAL_SAMPLES points in configuration space
    	int samp = 0; 
    	while (samp < TOTAL_SAMPLES) {
    		double[] randTheta = new double[numSegments];
    		double x;
    		Random r = new Random();
    	
    		for (int i = 0; i < numSegments; i++) {
    			x = (2*Math.PI) * r.nextDouble();
    			randTheta[i] = x;
    		}
    	
    		RobotArmNode randNode = new RobotArmNode(randTheta);
    	
    		if (!graph.containsNode(randNode) && randNode.isIntersect()){
    			graph.addNode(randNode);
    			samp++;
    		}
    	}
    	
    	
    	return graph;
    }
    
    
	private void getNeighbors(RobotArmNode node, RobotArmGraph graph, int k) {
		List<RobotArmNode> successors = new ArrayList<RobotArmNode>();
		
    	for (RobotArmNode nd : graph.getKeySet()) {
    		if (node.equals(nd)) //Don't link to self
    			continue;
    		
    		//double dist = node.getDist(nd);
    		successors.add(nd);
    		//Attempt to connect
    	}	
    	
    	// Sorting
    	Collections.sort(successors, new Comparator<RobotArmNode>() {
    	        @Override
    	        public int compare(RobotArmNode node1, RobotArmNode node2) {

    	            return  node.getDist(node1).compareTo(node.getDist(node2));
    	        }
    	    });
		
    	
    	//Test printing 
		//for (int j = 0; j < node.theta.length; j++)
		//	System.out.print(node.theta[j]+" ");
		//System.out.println(node.getPoints()[numSegments-1]+"\n");
		
		for (int i = 0; i < k; i++) {
			//Add edge
			RobotArmNode adjNode = successors.get(i);
			if(!graph.containsNode(adjNode))
				graph.addNode(adjNode);
			graph.addEdge(node, adjNode);
			
			//for (int j = 0; j < successors.get(i).theta.length; j++)
			//	System.out.print(successors.get(i).theta[j]+" ");
			//System.out.println(successors.get(i).getPoints()[numSegments-1]);
			
		}
		//System.out.println(); 
		
	}
    
    

    
    
    
	
	public class RobotArmNode {
		
		private double[] theta;
		
		public RobotArmNode(double[] angles){
			theta = angles;	
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
		
		
		
		//Returns true if there is an intersection
		public boolean isIntersect() {
			//Rectangle[] circ = new Rectangle[obstacles.length];
			Rectangle[] rect = new Rectangle[obstacles.length];
			Point[] points = getPoints();
			Line2D[] lines = new Line2D[numSegments];
			
			//Ellipse2D.Double circle = new Ellipse2D.Double(x, y, diameter, diameter);
			
			for (int i = 0; i < obstacles.length; i++){
				rect[i] = new Rectangle(obstacles[i].x, obstacles[i].y, CIRC_DIM, CIRC_DIM);
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

			return false;
		}
		
		
		//Local planner
	    //Returns true if the two nodes can be connected
	    public boolean connectNode(RobotArmNode other) {
	    	double[] currTheta = Arrays.copyOf(theta, theta.length);
	    	RobotArmNode currNode;
	    	boolean connect = true;
	    	
	    	//for (int i = 0; i < theta.length; i++) {
	    	//	System.out.println(currTheta[i]+"   "+other.theta[i]);
	    	//}
	    	
			updatePrintNode(currTheta);
	    	
	    	//Rotate counterclockwise
	    	for (int i = 0; i < theta.length; i++) {
	    		while (currTheta[i] != other.theta[i]) {
		    		//System.out.println("here2");
	    			currNode = new RobotArmNode(currTheta);
	    			double temp = (Math.abs(currTheta[i] - other.theta[i]) % (2*Math.PI));
	    			if (temp > RESOLUTION ) {
			    		//System.out.println("here3");
	    				currTheta[i] += RESOLUTION;
	    			} else {
			    		//System.out.println("here4");
	    				currTheta[i] = other.theta[i];
	    			}
	    			
	    			currTheta[i] = currTheta[i] % (2*Math.PI);
	    			
	    			if (currNode.isIntersect())
	    				connect = false;
	    			
	    			/*try {
	    				Thread.sleep(100);
	    			} catch (InterruptedException e) {
	    				e.printStackTrace();
	    			} 
	    			updatePrintNode(currTheta); */
	    		}
	    		if (connect == false)
	    			currTheta[i] = theta[i];
	    		while (currTheta[i] != other.theta[i] && connect == false) {
	    			currNode = new RobotArmNode(currTheta);
	    			double temp = (Math.abs(currTheta[i] - other.theta[i]) % (2*Math.PI));
	    			if (temp > RESOLUTION ) {
	    				currTheta[i] -= RESOLUTION;
	    			} else {
	    				currTheta[i] = other.theta[i];
	    			}
	    			
	    			currTheta[i] = currTheta[i] % (2*Math.PI);
	    			
	    			if (currNode.isIntersect()) {
	    				if (connect == false)
	    					return false;
	    				
	    			}
	    		
	    			/*try {
	    				Thread.sleep(100);
	    			} catch (InterruptedException e) {
	    				e.printStackTrace();
	    			}
	    			updatePrintNode(currTheta); */
	    		}
	    		connect = true;
	    	}
	    	/*
	    	currTheta = Arrays.copyOf(theta, theta.length);
	    	//Rotate clockwise
	    	for (int i = 0; i < theta.length; i++) {
	    		while (currTheta[i] != other.theta[i]) {
	    			currNode = new RobotArmNode(currTheta);
	    			double temp = (Math.abs(currTheta[i] - other.theta[i]) % (2*Math.PI));
	    			if (temp > RESOLUTION ) {
	    				currTheta[i] -= RESOLUTION;
	    			} else {
	    				currTheta[i] = other.theta[i];
	    			}
	    			
	    			currTheta[i] = currTheta[i] % (2*Math.PI);
	    			
	    			if (currNode.isIntersect())
	    				connect = false;
	    		
	    			try {
	    				Thread.sleep(100);
	    			} catch (InterruptedException e) {
	    				e.printStackTrace();
	    			}
	    			updatePrintNode(currTheta);
	    		}
	    	}
	    	*/
	    	return connect;
	    }
	    
	    
	    //Gets the Euclidean distance to the end of the other arm
	    public Integer getDist(RobotArmNode other) {
	    	Point end = getPoints()[numSegments-1];
	    	Point oEnd = other.getPoints()[numSegments-1];
	    	int xDist = Math.abs(end.x - oEnd.x);
	    	int yDist = Math.abs(end.y - oEnd.y);
	    	
	    	return (int) Math.sqrt(xDist*xDist + yDist*yDist);
	    }
	    
	    
	    @Override
	    public boolean equals(Object o) {
	    	RobotArmNode other = (RobotArmNode) o;
	    	for (int i = 0; i < numSegments; i++) {
	    		if (theta[i] != other.theta[i])
	    			return false;
	    	}
	    	return true;
	    }
	    
	    @Override
	    public int hashCode() {
	    	int hash = 0;
	    	int mult = 10;
	    	for (int i = 0; i < numSegments; i++) {
	    		hash += theta[i]*mult;
	    		mult = 10*mult;
	    	}
	    	return hash;
	    }

		
	}

	
    
	
	
	//Used to display the current configuration of the RobotArm
    private class RobotArmPanel extends JPanel {
		private static final long serialVersionUID = 1L; //Default

		//Does the actual drawing for the class
        private void doDrawing(Graphics g) {

            Graphics2D g2d = (Graphics2D) g;
        	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            
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
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            doDrawing(g);          
        }
    }
	
	
	
}
