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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;



public class RobotArmProblem {
	
	private final double RESOLUTION = Math.PI/90; //2 degree increments
	private int totalSamples = 10;

	private int[] length;
	private int numSegments;
	private Rectangle[] obstacles;
	
	private RobotArmGraph graph;
	private RobotArmNode startNode;
	private RobotArmNode goalNode;
	
	//Used for graphics printing
	private JPanel jp;
	private RobotArmNode printNode;
	

	
	public RobotArmProblem(double[] strAngles, double[] goalAngles, int[] ln, int k, Rectangle[] rect) {
		//startNode = new RobotArmNode(strAngles);
		//goalNode = new RobotArmNode(goalAngles);
		length = ln;
		numSegments = k;
		obstacles = rect;
		printNode = new RobotArmNode(strAngles);
		graph = null;
		
		for (int i = 0; i < k; i++) {
			if (i <= 2)
				totalSamples = 10*totalSamples;
			else
				totalSamples = 4*totalSamples;
		}
		
		System.out.println("Using "+totalSamples+" total samples.");
	}
	
	
	//Sum the total length of the robot arm
	public int sumLength() {
		int sum = 0;
		for (int i = 0; i < numSegments; i++)
			sum += length[i];
		
		return sum;
	}
	
	
	
    public void createAndShowGUI() {
    	int dim = 40 + 2*sumLength();
    	
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
    	jp.repaint();
    }	
    
    
    

    
    //Arg k: the number of vertices to link to
    public List<RobotArmNode> PRM(int k, double[] theta, double[] goal) {
    	List<RobotArmNode> sol = new ArrayList<RobotArmNode>();
    	startNode = new RobotArmNode(theta);
    	goalNode = new RobotArmNode(goal);
    	
    	if (startNode.isIntersect() || goalNode.isIntersect()) {
    		System.out.println("Invalid input");
    		return null;
    	}
    	
    	if (graph == null) {
    		System.out.println("Generating nodes...");
    		graph = genRobotGraph();
    		System.out.println("Nodes generated.\nCreating edges...");
    	
    		int i = 0;
    		for (RobotArmNode node : graph.getKeySet()) {
    			getNeighbors(node, graph, k);
    			
    			//System.out.println(i);
    			i++;
    		}
    	}
    	
    	System.out.println("Edges created.");
    	
    	graph.addNode(startNode);
    	graph.addNode(goalNode);
    	getNeighbors(startNode, graph, k);
    	getNeighbors(goalNode, graph, k);
    	

    	System.out.println("Running BFS");
    	sol = robotBFS();
    	System.out.println("BFS done");
    	
    	
    	return sol;
    } 
    
    
    //Generates a graph with no edges
    private RobotArmGraph genRobotGraph() {
    	RobotArmGraph graph = new RobotArmGraph();
    	
    	//Randomly generate totalSamples points in configuration space
    	int samp = 0; 
    	while (samp < totalSamples) {
    		
    		double[] randTheta = new double[numSegments];
    		double x;
    		Random r = new Random();
    	
    		for (int i = 0; i < numSegments; i++) {
    			x = (2*Math.PI) * r.nextDouble();
    			randTheta[i] = x;
    		}
    	
    		RobotArmNode randNode = new RobotArmNode(randTheta);
    	
    		if (!graph.containsNode(randNode) && !randNode.isIntersect()){
    			graph.addNode(randNode);
    			samp++;
    		}
    	}
    	
    	return graph;
    }
    
    
    //Adds neighbors to graph
	private void getNeighbors(RobotArmNode node, RobotArmGraph graph, int k) {
		List<RobotArmNode> successors = new ArrayList<RobotArmNode>();
		
    	for (RobotArmNode key : graph.getKeySet()) {
    		if (!node.equals(key)) //Don't link to self
    			successors.add(key);
    	} 
    	
    	// Sorting
    	Collections.sort(successors, new Comparator<RobotArmNode>() {
    	        @Override
    	        public int compare(RobotArmNode node1, RobotArmNode node2) {
    	            return  node.getDist(node1).compareTo(node.getDist(node2));
    	        }
    	    });
		
    	int i = 0;
		while (i < k) {
			//Add edge
			RobotArmNode adjNode = successors.get(i);
			if (node.connectNode(adjNode)) {
				//if(!graph.containsNode(adjNode))
				//	graph.addNode(adjNode);
				graph.addEdge(node, adjNode);

			}
			i++;
		}
	}
    
    
	
	//Modified from Hwk1, works but doesn't take into account distance between points
	private List<RobotArmNode> robotBFS() {
		Queue<RobotArmNode> frontier = new LinkedList<RobotArmNode>();
		HashMap<RobotArmNode, RobotArmNode> visited = 
				new HashMap<RobotArmNode, RobotArmNode>();
		visited.put(startNode, null);
		
		
		RobotArmNode currNode = startNode;
		List<RobotArmNode> successors;
		boolean goalNotFound = true;
		RobotArmNode goal = null;
		
		
		while (goalNotFound){
			successors = graph.getAdj(currNode);
			for (int i = 0; i < successors.size(); i++){
				
				if (successors.get(i).equals(goalNode)){ //if goal found
					goalNotFound = false;
					goal = successors.get(i);
					visited.put(successors.get(i), currNode);
					
					break;
				} else if (!visited.containsKey(successors.get(i))){ //If unvisited
					frontier.add(successors.get(i));
					visited.put(successors.get(i), currNode);
				}
			}
			currNode = frontier.poll();
			if (currNode == null)
				break;
		}
		
		if (goalNotFound){
			System.out.println("No valid path found.");
			return null;
		} 

		return backchain(goal, visited);
	}
	
	// backchain used by BFS
	private List<RobotArmNode> backchain(RobotArmNode node,
			HashMap<RobotArmNode, RobotArmNode> visited) {
		
		List<RobotArmNode> solution = new ArrayList<RobotArmNode>();
		RobotArmNode curr = node;
		RobotArmNode prev = curr;
		
		while (prev != null){
			prev = visited.get(curr);
			solution.add(0, curr);
			curr = prev;
		}

		return solution;
	}

	/*
	//A* search on a maze, modified from previous homework assignment
	private List<RobotArmNode> AStarSearch() {
		
		HashMap<RobotArmNode, RobotArmNode> visited = new HashMap<RobotArmNode, RobotArmNode>();
		HashMap<RobotArmNode, Double> distance = new HashMap<RobotArmNode, Double>();
		List<RobotArmNode> frontier = new ArrayList<RobotArmNode>();
		frontier.add(startNode);
		
		visited.put(startNode, null);
		distance.put(startNode, 0.0);
		startNode.updateDist(0);
		
		RobotArmNode currNode = startNode;
		double currDist = currNode.getDistFromStart();
		
    	// Sorting
    	Collections.sort(frontier, new Comparator<RobotArmNode>() {
    	        @Override
    	        public int compare(RobotArmNode node1, RobotArmNode node2) {
    	        	double dist1 = currDist + currNode.getDist(node1);
    	        	double dist2 = currDist + currNode.getDist(node2);
    	            return  Double.valueOf(dist1).compareTo(dist2);
    	        }
    	    });
		
		while (frontier.size() > 0) {
			currNode = frontier.remove(0);

			currDist = distance.get(currNode);

			
			List<RobotArmNode> adj = graph.getAdj(currNode);
			for (RobotArmNode node : adj) {
				
				if ( node.equals(goalNode) ) { 		//Done
					visited.put(node, currNode);
					return backchain(node, visited);
				}

				double dist = currDist + currNode.getDist(node);
				node.updateDist(dist);
				
				if (!visited.containsKey(node)) { //If never seen, add
					visited.put(node, currNode);
					distance.put(node, dist);
					frontier.add(node);
				} else if (dist < distance.get(node)) { //If seen, but new path closer
					visited.put(node, currNode);
					distance.put(node, dist);
					if (!frontier.contains(node))
						frontier.add(node);
				} 
				
			}

		}
		
		return null;
	} */
    
    
    
	
	public class RobotArmNode {
		
		private double[] theta;
		double dist;
		
		public RobotArmNode(double[] angles){
			theta = angles;	
		}
		
		public double[] getTheta() { return theta; }
		
		//public void updateDist(double d) { dist = d; }
		//public double getDistFromStart() { return dist; }
		
		
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
			Point[] points = getPoints();
			Line2D[] lines = new Line2D[numSegments];
			
			lines[0] = new Line2D.Double(0,0, points[0].x, points[0].y);
			for (int i = 0; i < numSegments-1; i++) {
				double x1 = (double) (points[i].x);
				double y1 = (double) (points[i].y);
				double x2 = (double) (points[i+1].x);
				double y2 = (double) (points[i+1].y);
				lines[i+1] = new Line2D.Double(x1, y1, x2, y2);
			}
			
			for (int i = 0; i < obstacles.length; i++) {
				for (int j = 0; j < numSegments; j++) {
					if (obstacles[i].intersectsLine(lines[j]))
						return true;
				}
			}

			return false;
		}
		
		
		//Local planner
	    //Returns true if one can get to other from this node
	    public boolean connectNode(RobotArmNode other) {
	    	double[] currTheta = Arrays.copyOf(theta, theta.length);
	    	RobotArmNode currNode;	    	
	    	
	    	//Rotate in closest direction
	    	for (int i = 0; i < theta.length; i++) {
	    		int rotateDir;
	    		double diff = other.theta[i] - theta[i];
	    		
	    		double absDiff = Math.abs(diff);
	    		
	    		if (absDiff < Math.PI && diff > 0) //If less than halfway around the circle
	    			rotateDir = 1; //Rotate curr counterclockwise
	    		else if (absDiff < Math.PI && diff < 0)
	    			rotateDir = -1; //Rotate curr clockwise
	    		else if (absDiff > Math.PI && diff > 0)
	    			rotateDir = -1; //Rotate counterclockwise
	    		else //If absDiff > Math.PI && diff < 0
	    			rotateDir = 1;
	    		
	    		while (currTheta[i] != other.theta[i]) {

	    			double temp = (Math.abs(other.theta[i] - currTheta[i]) % (2*Math.PI));
	    			
	    			if (temp > RESOLUTION ) {
	    				currTheta[i] += rotateDir*RESOLUTION;
	    			} else {
	    				currTheta[i] = other.theta[i];
	    			}
	    			
	    			currTheta[i] = currTheta[i] % (2*Math.PI);
	    			currNode = new RobotArmNode(currTheta);
	    			//updatePrintNode(currTheta);
	    			if (currNode.isIntersect()) {
	    				return false;
	    			}
	    		}  
	    	}

	    	return true;
	    }
	    
	    
	    //Gets the distance between the angles of two arms
	    public Double getDist(RobotArmNode other) {
	    	double sum = 0;
	    	for (int i = 0; i < numSegments; i++) {
	    		double diff = Math.abs(other.theta[i] - theta[i]);
	    		if (diff > Math.PI) {
	    			double x = diff - Math.PI;
	    			diff = Math.PI-x;
	    		}
	    		
	    			sum += diff;
	    	}
	    	return sum;
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
		
		public String toString() {
			String s = "";
			s += "("+theta[0];
			for (int i = 1; i < theta.length; i++) {
				s += ", "+theta[i];
			}
			return s+")";
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

            //This centers (0, 0) in the GUI
            AffineTransform tform = AffineTransform.getTranslateInstance( getWidth()/2, getHeight()/2);
            tform.scale( 1, -1);
            g2d.setTransform(tform);
            
            //Draw obstacles
            for (int i = 0; i < obstacles.length; i++)
            	g2d.fill(obstacles[i]);
            
            Point[] points = printNode.getPoints();
            g2d.drawLine(0, 0, points[0].x, points[0].y);
            
            //Draw robot arm
            for (int i = 0; i < numSegments-1; i++)
            	g2d.drawLine(points[i].x, points[i].y, points[i+1].x, points[i+1].y);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            doDrawing(g);          
        }
    }
	
	
	
}
