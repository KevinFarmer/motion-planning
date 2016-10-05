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
import java.util.Queue;
import java.util.Random;



public class RobotArmProblem {//extends RobotSearch {
	
	private final int CIRC_DIM = 30;
	private final double RESOLUTION = Math.PI/90; //1 degree increments
	private final int TOTAL_SAMPLES = 10000;
	private int totalSamples = 10;

	private int[] length;
	private int numSegments;
	private Point[] obstacles;
	
	RobotArmGraph graph;
	
	
	//Used for graphics printing
	private JPanel jp;
	private RobotArmNode printNode;
	

	
	public RobotArmProblem(double[] strAngles, double[] goalAngles, int[] ln, int k, Point[] obs) {
		//startNode = new RobotArmNode(strAngles);
		//goalNode = new RobotArmNode(goalAngles);
		length = ln;
		numSegments = k;
		obstacles = obs;
		printNode = new RobotArmNode(strAngles);
		graph = null;
		
		for (int i = 0; i < k; i++)
			totalSamples = 10*totalSamples;
	}
	
	
	/*
	public void printStart() {
		Point[] points = startNode.getPoints();
		for (int i = 0; i < numSegments; i++) 
			System.out.println("("+points[i].x+", "+points[i].y+")");
	} */
	
	
	
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
    	
		/*try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} */
    	
    }	
    
    
    

    
    //Arg k: the number of vertices to link to
    public List<RobotArmNode> PRM(int k, double[] theta, double[] goal) {
    	List<RobotArmNode> sol = new ArrayList<RobotArmNode>();
    	RobotArmNode startNode = new RobotArmNode(theta);
    	RobotArmNode goalNode = new RobotArmNode(goal);
    	
    	if (startNode.isIntersect() || goalNode.isIntersect()) {
    		System.out.println("Invalid input");
    		return null;
    	}
    	
    	if (graph == null) {
    		System.out.println("Generating nodes...");
    		graph = genRobotGraph();
    		System.out.println("Nodes generated.\nCreating edges...");
    	
    		//System.out.println(graph.getKeySet().size());
    		for (RobotArmNode node : graph.getKeySet()) {
    			getNeighbors(node, graph, k);
    		//updatePrintNode(node.theta); //----------------------------------------------------------
    		//for (int i = 0; i < node.theta.length; i++)
    		//	System.out.print(node.theta[i]+"  ");
    		//System.out.println();
    		}
    	}
    	System.out.println("Edges created.");
    	
    	graph.addNode(startNode);
    	graph.addNode(goalNode);
    	getNeighbors(startNode, graph, k);
    	getNeighbors(goalNode, graph, k);
    	
    	/*
    	System.out.println("\n\n Exit \n\n");
    	List<RobotArmNode> l = graph.getAdj(startNode);
    	for (int i = 0; i < l.size(); i++)
    		System.out.print(l.get(i)+" ");
    	System.out.println();
    	
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
    	
    	for (RobotArmNode node : graph.getKeySet()) {
    		System.out.println(node);
    		for (RobotArmNode adj : graph.getAdj(node))
    			System.out.println("   "+adj);
    	} */
    	
		//if (true)
		//	return null;
    	
    	/*
		List<RobotArmNode> successors = graph.getAdj(startNode);
		for (int i = 0; i < successors.size(); i++) {
			RobotArmNode n = successors.get(i);
			System.out.println("# "+n);
			double[] x = ((successors.get(i)).theta);
			System.out.println("--"+x.toString());
			for (int j = 0; j < numSegments; j++)
				System.out.print(successors.get(i).theta[j]+" ");
			System.out.println(successors.get(i).getPoints()[numSegments-1]);
		
		} */
    	
    	/*
    	//Test print
    	for (RobotArmNode node : graph.getKeySet()) {
    		List<RobotArmNode> successors = graph.getAdj(node);
    		for (int i = 0; i < k; i++) {
		
    			for (int j = 0; j < successors.get(i).theta.length; j++)
    				System.out.print(successors.get(i).theta[j]+" ");
    			System.out.println(successors.get(i).getPoints()[numSegments-1]);
			
    		}
    		System.out.println(); 
    	} */
    	
    	System.out.println("Running BFS");
    	sol = robotBFS(graph, startNode, goalNode);
    	System.out.println("BFS done");
    	
    	//System.out.println("\n"+graph.getKeySet().size());
    	
    	
		//double[] theta = {Math.PI/16, Math.PI/3};
    	//boolean b = startNode.connectNode(new RobotArmNode(theta));
    	
    	//System.out.println("\n"+b);
    	
    	return sol;
    } 
    
    
    //Generates a graph
    private RobotArmGraph genRobotGraph() {
    	RobotArmGraph graph = new RobotArmGraph();
    	
    	//Randomly generate TOTAL_SAMPLES points in configuration space
    	int samp = 0; 
    	
    	System.out.println(totalSamples);
    	while (samp < TOTAL_SAMPLES) {
    	//while (samp < totalSamples) {
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
				if(!graph.containsNode(adjNode))
					graph.addNode(adjNode);
				graph.addEdge(node, adjNode);

			}
			i++;
			
			//for (int j = 0; j < successors.get(i).theta.length; j++)
			//	System.out.print(successors.get(i).theta[j]+" ");
			//System.out.println(successors.get(i).getPoints()[numSegments-1]);
			
		}
		//System.out.println(); 
		
	}
    
    
	private List<RobotArmNode> robotBFS(RobotArmGraph g, RobotArmNode startNode, RobotArmNode goalNode) {
		//List<RobotArmNode> sol = new ArrayList<RobotArmNode>();
		
		Queue<RobotArmNode> frontier = new LinkedList<RobotArmNode>();
		HashMap<RobotArmNode, RobotArmNode> visited = 
				new HashMap<RobotArmNode, RobotArmNode>();
		visited.put(startNode, null);
		
		
		RobotArmNode currNode = startNode;
		List<RobotArmNode> successors;
		boolean goalNotFound = true;
		RobotArmNode goal = null;
		
		
		while (goalNotFound){
			successors = g.getAdj(currNode);
			for (int i = 0; i < successors.size(); i++){
				
				if (successors.get(i).equals(goalNode)){ //if goal found
					goalNotFound = false;
					goal = successors.get(i);
					visited.put(successors.get(i), currNode);
					
					//incrementNodeCount();
					//updateMemory(visited.size());
					break;
				} else if (!visited.containsKey(successors.get(i))){ //If unvisited
					frontier.add(successors.get(i));
					visited.put(successors.get(i), currNode);
					//incrementNodeCount();
				}
				//updateMemory(visited.size());
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
	
	// backchain should only be used by bfs, not the recursive dfs
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

    
    
    
	
	public class RobotArmNode {
		
		private double[] theta;
		
		public RobotArmNode(double[] angles){
			theta = angles;	
		}
		
		public double[] getTheta() { return theta; }
		
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
	    //Returns true if one can get to other from this node
	    public boolean connectNode(RobotArmNode other) {
	    	double[] currTheta = Arrays.copyOf(theta, theta.length);
	    	RobotArmNode currNode;
	    	boolean connect = true;
	    	
	    	//for (int i = 0; i < theta.length; i++) {
	    	//	System.out.println(currTheta[i]+"   "+other.theta[i]);
	    	//}
	    	
			/*updatePrintNode(currTheta);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} */
			
	    	//System.out.println(theta.length);
	    	
	    	//Rotate in closest direction
	    	for (int i = 0; i < theta.length; i++) {
	    		int rotateDir;
	    		double diff = other.theta[i] - theta[i]; //other.theta[i] is goal, theta[i] is start
	    		//double opp = (other.theta[i] + Math.PI) % (2*Math.PI); //Gets angle opposite on circle
	    		
	    		/*if (theta[i] > other.theta[i] || theta[i] < opp)
	    			rotateDir = -1;
	    		else
	    			rotateDir = 1; */
	    		
	    		double absDiff = Math.abs(diff);
	    		
	    		if (absDiff < Math.PI && diff > 0) //If less than halfway around the circle
	    			rotateDir = 1; //Rotate curr counterclockwise
	    		else if (absDiff < Math.PI && diff < 0)
	    			rotateDir = -1; //Rotate curr clockwise
	    		else if (absDiff > Math.PI && diff > 0)
	    			rotateDir = -1; //Rotate counterclockwise
	    		else //If absDiff > Math.PI && diff < 0
	    			rotateDir = 1;
	    		
	    		//System.out.println("\n--"+theta[i]+ "  " + other.theta[i] + "  " + diff + "  " +absDiff);
	    		
	    		//updatePrintNode(currTheta);
	    		while (currTheta[i] != other.theta[i]) {
	    			//updatePrintNode(currTheta);
		    		//System.out.println("here2");
	    			
	    			double temp = (Math.abs(other.theta[i] - currTheta[i]) % (2*Math.PI));
	    			
	    			if (temp > RESOLUTION ) {
			    		//System.out.print("here3  ");
	    				currTheta[i] += rotateDir*RESOLUTION;
	    			} else {
			    		//System.out.println("here4");
	    				currTheta[i] = other.theta[i];
	    			}
	    			
	    			//System.out.println(" - " +currTheta[i] + "  " + other.theta[i] + "  t:"+temp+ " -- " +RESOLUTION);
	    			
	    			currTheta[i] = currTheta[i] % (2*Math.PI);
	    			currNode = new RobotArmNode(currTheta);
	    			if (currNode.isIntersect()) {
	    				//System.out.println("\n\nIntersection" + currNode+ "\n\n");
	    				return false;
	    			}
	    				//connect = false;
	    			
	    			
	    			/*try {
	    				Thread.sleep(100);
	    			} catch (InterruptedException e) {
	    				e.printStackTrace();
	    			} 
	    			updatePrintNode(currTheta); */
	    		}  /*
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
	    				//if (connect == false)
	    					return false;
	    				
	    			}
	    		
	    			/*try {
	    				Thread.sleep(100);
	    			} catch (InterruptedException e) {
	    				e.printStackTrace();
	    			}
	    			updatePrintNode(currTheta); 
	    		} 
	    		connect = true; */
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
	    public Double getDist(RobotArmNode other) {
	    	double[] otherTheta = other.getTheta();
	    	double sum = 0;
	    	for (int i = 0; i < numSegments; i++) {
	    		double diff = Math.abs(other.theta[i] - theta[i]);
	    		if (diff > Math.PI) {
	    			double x = diff - Math.PI;
	    			diff = Math.PI-x;
	    		}
	    		
	    			sum += diff;
	    	}
	    	//System.out.println(other + "   d:"+sum);
	    	return sum;
	    	/*
	    	Point end = getPoints()[numSegments-1];
	    	Point oEnd = other.getPoints()[numSegments-1];
	    	int xDist = Math.abs(end.x - oEnd.x);
	    	int yDist = Math.abs(end.y - oEnd.y);
	    	return (int) Math.sqrt(xDist*xDist + yDist*yDist) + sum; */
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


		//@Override
		//public boolean goalTest() {
		//	return this.equals(goalNode);
		//}
		
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
