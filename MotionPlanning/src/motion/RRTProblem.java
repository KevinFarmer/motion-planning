package motion;

import java.util.ArrayList;
import java.util.List;

public abstract class RRTProblem {

	protected RRTNode startNode;
	
	protected interface RRTNode {
		public List<RRTNode> getSuccessors();
		
	}
	
	
	
	public List<RRTNode> rrtExploration() {
		List<RRTNode> sol = new ArrayList<RRTNode>();
		
		
		
		
		return sol;
	}


	
	
	

	public abstract void createAndShowGUI();
	
	
}
