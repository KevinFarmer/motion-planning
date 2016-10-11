package motion;

import java.util.ArrayList;
import java.util.List;

public abstract class RRTProblem {

	protected RRTNode startNode;
	protected RRTNode goalNode;
	
	protected interface RRTNode {
		public List<RRTNode> getSuccessors();
		public int getX();
		public int getY();
		public boolean isIntersect();
		public boolean goalTest();
		public Tree<RRTNode> getNearestLeaf(List<Tree<RRTNode>> leaves);
	}
	
	
	
	public List<RRTNode> rrtExploration() {
		Tree<RRTNode> rootTree = new Tree<RRTNode>(startNode);
		List<Tree<RRTNode>> leaves = new ArrayList<Tree<RRTNode>>();
		
		//System.out.println(startNode);
		
		for (RRTNode n : startNode.getSuccessors()) {
			Tree<RRTNode> child = rootTree.addLeaf(n);
			leaves.add(child);
			//System.out.println(" "+n);
		}
		
		//for (int i = 0; i < 5; i++)
		//	getRandomSample();
		
		//if (true)
		//	return null;
		
		//Continually loop
		int i = 0;
		while (leaves.size() > 0) {
			
			//Every 100 loops, attempt to connect to the goal
			RRTNode rand;
			if (i == 100) {
				rand = goalNode;
				i = 0;
			} else {
				rand = getRandomSample();
			}
			
			
			//Loop and find nearest leaf
			Tree<RRTNode> leafTree = rand.getNearestLeaf(leaves);
			
			
			//System.out.println("\n-"+leafTree);
			
			//Expand that leaf
			for (RRTNode n : (leafTree.getHead()).getSuccessors()) {
				//System.out.println("  "+n);
				
				if (n.goalTest()) {
					//System.out.println(n);
					Tree<RRTNode> child = leafTree.addLeaf(n);
					leaves.add(child);
					return backtrack(rootTree, child);
				}
				
				Tree<RRTNode> child = leafTree.addLeaf(n);
				leaves.add(child);
			}
			
			leaves.remove(leafTree);
			i++;
		}

		
		return null;
	}


	
	public List<RRTNode> backtrack(Tree<RRTNode> rootTree, Tree<RRTNode> leaf) {
		List<RRTNode> sol = new ArrayList<RRTNode>();
		Tree<RRTNode> curr = leaf;

		
		while (curr != rootTree) {
			sol.add(0, curr.getHead());
			curr = curr.getParent();
		}
		
		
		
		return sol;
	}
	
	
	protected abstract RRTNode getRandomSample();
	public abstract void createAndShowGUI();
	public abstract void updatePrintNode(RRTNode node);
	
}
