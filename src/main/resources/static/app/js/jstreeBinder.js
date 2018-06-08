/**
 * 
 */


var jstreeBinder = {

	// (PRIVATE) recursively traverse the tree, creating and binding jstree nodes
		
	traverse: function(elm) {
		console.log(elm.node.nodePath);
		var newnode = {
				text: elm.node.value, 
				bound: elm,
				type: elm.node.type,
				state: {opened: false},
				}; 

		var children = elm.children;
		newnode.children = [];
		if (children != null) for (var i=0; i<children.length; i++) {
			var ch = this.traverse(children[i]);
			newnode.children.push(ch);
		}

		return newnode;
	},
	
	// (PUBLIC) recursively traverse the tree, creating and binding jstree nodes
	
	bind: function(treedto) {
		var tree = this.traverse(treedto);
		tree.state.opened = true;
		tree.type = "root";
		return tree;
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