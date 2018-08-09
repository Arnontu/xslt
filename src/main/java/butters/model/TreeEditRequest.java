package butters.model;

public class TreeEditRequest {

		public static enum ACT {
			NEWTEMPLATE, 
			INSERT, DELETE, COPYPASTE, SETVALUE,
			DEEPCOPY, MAP,
			RUN,
			LOAD
		};
		
		public static enum FRAGMENT {VALUEOF, COPYOF, FOREACH, IF} 

		// --------------------------------

		protected ACT action;
		
		protected String fromTree;
		protected TreeNodeDTO fromNode;

		protected String targetTree;
		protected TreeNodeDTO targetNode;

		protected FRAGMENT fromFragment;
		protected String newValue;
		protected String templateTree;
		
		// --------------------------------

		public String toString() {
			return (new StringBuffer("edit request: {")
					.append("fromTree: " + fromTree + "; ")
					.append("fromNode: " + fromNode + "; ")
					.append("targetTree: " + targetTree + "; ")
					.append("targetTree: " + targetNode + "; ")
					.append("templateTree: " + templateTree + "; ")
					.append("fragment: " + fromFragment + "; ")
					.append("newvalue: " + newValue + "; ")
					).toString();
		}
		
		public String getFromTree() {
			return fromTree;
		}

		public void setFromTree(String fromTree) {
			this.fromTree = fromTree;
		}

		public TreeNodeDTO getFromNode() {
			return fromNode;
		}

		public void setFromNode(TreeNodeDTO fromNode) {
			this.fromNode = fromNode;
		}

		public FRAGMENT getFromFragment() {
			return fromFragment;
		}

		public void setFromFragment(FRAGMENT fromFragment) {
			this.fromFragment = fromFragment;
		}

		public ACT getAction() {
			return action;
		}

		public void setAction(ACT action) {
			this.action = action;
		}

		public String getTargetTree() {
			return targetTree;
		}

		public void setTargetTree(String targetTree) {
			this.targetTree = targetTree;
		}

		public TreeNodeDTO getTargetNode() {
			return targetNode;
		}

		public void setTargetNode(TreeNodeDTO targetNode) {
			this.targetNode = targetNode;
		}

		public String getNewValue() {
			return newValue;
		}

		public void setNewValue(String newValue) {
			this.newValue = newValue;
		}

		public String getTemplateTree() {
			return templateTree;
		}

		public void setTemplateTree(String templateTree) {
			this.templateTree = templateTree;
		}
}
