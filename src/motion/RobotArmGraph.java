package motion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import motion.RobotArmProblem.RobotArmNode;



/*
 * Adjacency list implementation of a Graph
 * 
 * Used the following for reference:
 * http://algs4.cs.princeton.edu/41graph/Graph.java.html
 */
public class RobotArmGraph {

    private int V;
    private int E;
    private HashMap<RobotArmNode, ArrayList<RobotArmNode>> adj;
	
	
    public RobotArmGraph() {
        this.V = 0;
        this.E = 0;
        adj = new HashMap<RobotArmNode, ArrayList<RobotArmNode>>();
        //for (int i = 0; i < V; i++)
        //	adj[i] = new ArrayList<RobotArmNode>();
        
    }
	
	
	public int getV() { return V; }
	public int getE() { return E; }
	
	
	//Check if vertex is in Graph
    //private void validateVertex(int v) {
    //    if (v < 0 || v >= V)
    //        throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
    //}
	
	
    public void addNode(RobotArmNode v) {
        //validateVertex(v);
        //validateVertex(w);
    	ArrayList<RobotArmNode> list = new ArrayList<RobotArmNode>();
        V++;
        adj.put(v, list);
    }
	
    public void addEdge(RobotArmNode v, RobotArmNode w) {
        //validateVertex(v);
        //validateVertex(w);
        E++;
        adj.get(v).add(w);
        adj.get(w).add(v);
    }
    
    
    /**
     * Returns the vertices adjacent to vertex v.
     */
    public List<RobotArmNode> getAdj(RobotArmNode v) {
        //validateVertex(v);
        return adj.get(v);
    }
    

    /**
     * Returns the degree of vertex v.
     */
    public int degree(RobotArmNode v) {
        //validateVertex(v);
        return adj.get(v).size();
    }
    
    /**
     * Checks if the graph contains vertex v.
     */
    public boolean containsNode(RobotArmNode v) {
        return adj.containsKey(v);
    }
    
    public Set<RobotArmNode> getKeySet() {
    	return adj.keySet();
    }

	
}
