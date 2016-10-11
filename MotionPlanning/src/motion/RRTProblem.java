package motion;

import java.util.ArrayList;
import java.util.List;

public abstract class RRTProblem {

	protected RRTNode startNode;
	
	protected interface RRTNode {
		public List<RRTNode> getSuccessors();
		public int getX();
		public int getY();
		public boolean isIntersect();
		public boolean goalTest();
		public Tree<RRTNode> getNearestLeaf(List<Tree<RRTNode>> leaves);
	}
	
	
	
	public List<RRTNode> rrtExploration() {
		List<RRTNode> sol = new ArrayList<RRTNode>();
		
		Tree<RRTNode> rootTree = new Tree<RRTNode>(startNode);
		List<Tree<RRTNode>> leaves = new ArrayList<Tree<RRTNode>>();
		
		System.out.println(startNode);
		
		for (RRTNode n : startNode.getSuccessors()) {
			Tree<RRTNode> child = rootTree.addLeaf(n);
			leaves.add(child);
			System.out.println(" "+n);
		}
		
		//for (int i = 0; i < 5; i++)
		//	getRandomSample();
		
		
		//Continually loop
		//while (leaves.size() > 0) {
			RRTNode rand = getRandomSample();
			
			
			//Loop and find nearest leaf
			Tree<RRTNode> leafTree = rand.getNearestLeaf(leaves);
			
			System.out.println("\n-"+leafTree);
			
			//Expand that leaf
			for (RRTNode n : (leafTree.getHead()).getSuccessors()) {
				System.out.println("  "+n);
				if (rootTree.getTree(n) == null) {
					Tree<RRTNode> child = leafTree.addLeaf(n);
					leaves.add(child);
				}
			}
			
			leaves.remove(leafTree);
			
			
			
			
		//} 
		
		
		
		
		return sol;
	}


	
	
	

	protected abstract RRTNode getRandomSample();
	public abstract void createAndShowGUI();
	
}
