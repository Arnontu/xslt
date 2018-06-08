package butters.model;

public class TreeNodeDTO {
	
	public static enum NODETYPE {ROOT, ELEMENT, TEXT, XSL, DEFAULT};
	
	protected long id;
	protected String type;		// 
	protected String value;		// primary value to bind  
	protected boolean isAttribute = false;
	protected String nodePath;  // xpath to original node
	
	// ----------------------------------
	
	public String toString() {
		return (new StringBuffer("node: {"))
				.append(id).append(", ")
				.append(value).append(", ")
				.append(nodePath)
				.append("}")
				.toString();
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type.toLowerCase();
	}
	public boolean isAttribute() {
		return isAttribute;
	}
	public void setAttribute(boolean isAttribute) {
		this.isAttribute = isAttribute;
	}
	public String getNodePath() {
		return nodePath;
	}
	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

}

