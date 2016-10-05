package motion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import motion.RobotArmProblem.RobotArmNode;

public abstract class RobotSearch {
	

	
	
	//public RobotSearch() {
		
	//}
	
	
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
    			
    	/*
    	for (RobotArmNode node : graph.getKeySet()) {
    		List<RobotArmNode> successors = graph.getAdj(node);
    		//for (int i = 0; i < k; i++) {
		
    			//for (int j = 0; j < successors.get(i).getTheta().length; j++)
    			//	System.out.print(successors.get(i).theta[j]+" ");
    			//System.out.println(successors.get(i).getPoints()[numSegments-1]);
			
    		//}
    		System.out.println(); 
    	} */
    	
    	//System.out.println("\n"+graph.getKeySet().size());
    	
    	
		//double[] theta = {Math.PI/16, Math.PI/3};
    	//boolean b = startNode.connectNode(new RobotArmNode(theta));
    	
    	//System.out.println("\n"+b);
    	
    	return sol;
    }
    
    /*
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
    } */
    
    
	private void getNeighbors(RobotArmNode node, RobotArmGraph graph, int k) {
		List<RobotArmNode> successors = new ArrayList<RobotArmNode>();
		
    	for (RobotArmNode key : graph.getKeySet()) {
    		RobotArmNode nd = (RobotArmNode) key;
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
    
	
	
	protected abstract RobotArmGraph genRobotGraph();
    public abstract void createAndShowGUI();
	
}
