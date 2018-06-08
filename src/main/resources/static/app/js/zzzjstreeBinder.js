/**
 * 
 */


var jstreeBinder = {

	// (PRIVATE) recursively traverse the tree, creating and binding jstree nodes
		
	traverse: function(xmlnode, path) {
		var path = path + "/" + xmlnode.nodeName; console.log(path);
		var newnode = {
				text: xmlnode.nodeName, 
				bound: xmlnode,   // -- does not work ...
				nodeType: xmlnode.nodeType,
				isAttribute: false,
				state: {opened: false},
				thispath: path
				}; 

		switch(xmlnode.nodeType) {
		case Node.ELEMENT_NODE:
			var jsonChildren = [];

			if (xmlnode.hasAttributes()) {
				var attrs = xmlnode.attributes;
		       for(var j = 0; j < attrs.length; j++) {
					var attrvalue = {
							text: attrs[j].value,
							bound: attrs[j],
							isAttribute: true,
							type: "attrvalue"
							}
					var attrname = {
							text: attrs[j].name,
							bound: attrs[j],
							isAttribute: true,
							children: [attrvalue],
							type: "attrname"
							}
					jsonChildren.push(attrname);
		         }
			}
			
			if (xmlnode.hasChildNodes()) {
				var children = xmlnode.childNodes;
				for (var i = 0; i < children.length; i++) {
					var ch = this.traverse(children[i], path);
					if (ch != null) jsonChildren.push(ch);
				}
			}
			
			if (jsonChildren.length > 0) {
				newnode.children = jsonChildren;
				if (jsonChildren.length == 1 && jsonChildren[0].bound.nodeType == Node.TEXT_NODE) { // one TEXT node child
					newnode.type = "leaf";
				} else {
					newnode.type = "element";
				}
			}
			break;
			
		case Node.TEXT_NODE:
			if (xmlnode.nodeValue.trim() == '') {newnode = null;}
			else {
				newnode.text = xmlnode.nodeValue;
				newnode.type = "text";
				}
			break;
			
		default:
			newnode.text = xmlnode.nodeValue;
			newnode.type = "other";
			break;
		}
		
		return newnode;
	},
	
	// (PUBLIC) recursively traverse the tree, creating and binding jstree nodes
	
	bind: function(xmlDoc) {
		var root = xmlDoc.documentElement;
		var tree = this.traverse(root, "");
		tree.state.opened = true;
		tree.id = "rootNode";
		tree.type = "root";
		return tree;
	},

	load: function(xmlString) {
		var parser = new DOMParser();
		var clean = xmlString.replace(/\n/g,''); 
		var xmlDoc = parser.parseFromString(clean, "text/xml");
		return xmlDoc;
	},
	
	// set XPath expression in text or attribute 
	
	setExpression: function(jsNode, expr) {
		if (jsNode.original.nodeType == Node.TEXT_NODE) {
			let xmlnode = jsNode.original.bound;
			xmlnode.nodeValue = expr;
			jsNode.text = expr;
		} else if (jsNode.original.isAttribute) {
			let attr = jsNode.original.bound;
			attr.value = expr;
			jsNode.text = expr;
		} else { // else do nothing
			alert("rename is not supported");
		}
	},
	
	
	// get bounded node (not attribute) pointer (including position) 
	// TODO position in case of recurring elements
	
	getNodeAddress: function(jsNode) {
		let node = jsNode.original.bound;
		let k = new Array();
		while (node != null) {
			k.push(node);
			let node = node.parentNode; 
		}
		
		let path = "";
		while (k.length > 0) {
			let node = k.pop(); 
			let path = path + "/" + node.nodeName + "[1]"; 
		}
		return path;
	}
	
}