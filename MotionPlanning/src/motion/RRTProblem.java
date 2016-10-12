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
	
	
	//Explores a search space using a Rapidly Exploring Random Tree
	public List<RRTNode> rrtExploration() {
		Tree<RRTNode> rootTree = new Tree<RRTNode>(startNode);
		List<Tree<RRTNode>> leaves = new ArrayList<Tree<RRTNode>>();
		
		for (RRTNode n : startNode.getSuccessors()) {
			Tree<RRTNode> child = rootTree.addLeaf(n);
			leaves.add(child);
		}
		
		//Continually loop
		while (leaves.size() > 0) {
			RRTNode rand = getRandomSample();
			
			//Find nearest leaf to rand
			Tree<RRTNode> leafTree = rand.getNearestLeaf(leaves);
			
			//Expand that leaf
			for (RRTNode n : (leafTree.getHead()).getSuccessors()) {
				if (n.goalTest()) {
					Tree<RRTNode> child = leafTree.addLeaf(n);
					leaves.add(child);
					return backtrack(rootTree, child);
				}
				
				Tree<RRTNode> child = leafTree.addLeaf(n);
				leaves.add(child);
			}
			leaves.remove(leafTree);
		}

		return null;
	}

	

	//Return path from the root to the specified leaf node
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
