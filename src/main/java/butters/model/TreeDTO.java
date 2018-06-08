package butters.model;

import java.util.ArrayList;
import java.util.List;

public class TreeDTO {
	
	private TreeNodeDTO node;
	private List<TreeDTO> children = new ArrayList<TreeDTO>();
	
	// -----------------------------------------------------------
	
	public String toString() {
		return (node.getValue() + "[" + children.size() + "]");
	}
	
	public List<TreeDTO> getChildren() {
		return children;
	}
	public void setChildren(List<TreeDTO> children) {
		this.children = children;
	}
	public void addChild(TreeDTO child) {
		this.children.add(child);
	}
	public TreeNodeDTO getNode() {
		return node;
	}
	public void setNode(TreeNodeDTO node) {
		this.node = node;
	}

}
