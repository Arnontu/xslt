

var editorServices = xsltServices();


var editorModel = {
	xmlFileName: "na",
	xslFileName: "na" ,
	outFileName: "na",
	fragmentPrototypeFileName: "fragmentsPrototype.xsl"
	// trees data?  
};

var bind = {
	xmlTree: "na",
	xslTree: "na",
	outputTree: "na",
	
	inputFileName: "na",
	xslFileName: "na",
	fragmentsFileName: "na",
	
	bind: function(jq) {
		this.xmlTree = jq('#jstree-xmldata').jstree(true);
		this.xstTree = jq('#jstree-xsltdata').jstree(true);
		this.outputTree = jq('#jstree-outdata').jstree(true);
		this.fragmentsTree = jq('#jstree-fragments').jstree(true);
	}
	
};

// types of nodes in XML tree 
var types = {
    "#" : {
//      "max_children" : 1,
//      "max_depth" : 4,
//      "valid_children" : ["root"]
    },
    "root" : {
      "icon" : "oi oi-document",
    },
    "default" : {
    	"icon" : false,
    },
    "element" : {
        "icon" : "oi oi-code",
    },
    "text" : {
        "icon" : "oi oi-british-pound",
    },
    "xsl" : {
        "icon" : "oi oi-text",
    }
};
	



// -----------------------------------------------
// READY 
// load default view trees

$(document).ready(function() { 
	initTrees();
	bind.bind($);
	initModel();
	
	loadInputTree();
	loadTemplateTree();

});

function initModel() {
	// file names -- TODO return in context object
	this.editorModel.outFileName = $('#outFileName').val();
	this.editorModel.xslFileName = $('#xslFileName').val();
	this.editorModel.xmlFileName = $('#xmlFileName').val();
};

function initTrees() {

	$('#jstree-xmldata').jstree({
		  "core" : {
		    "animation" : 0,
		    "check_callback" : false,
		    "themes" : { "stripes" : true },
		    'data' : {"id": "rootNode", "text":"Input", "type":"root"}
		  },
		  "types" : types,
		  "plugins" : ["search", "wholerow", "types", "dnd"]
	});
	
	$('#jstree-xsltdata').jstree({
		  "core" : {
		  	    "animation" : 0,
		  	    "check_callback" : true,
		  	    "themes" : { "stripes" : true },
		  	    'data' : {"id": "rootNode", "text":"Template", "type":"root"}
		  },
		  "dnd": {"always_copy": true},
		  "types" : types,
		  "plugins" : ["search", "wholerow", "types", "dnd"]
	});
	
	
	$('#jstree-outdata').jstree({
		  "core" : {
		  	    "animation" : 0,
		  	    "check_callback" : false,
		  	    "themes" : { "stripes" : true },
		  	    'data' : {"id": "rootNode", "text":"Output", "type":"root"}
		  	  },
		  	  "types" : types,
		  	  "plugins" : ["search", "wholerow", "types"]
	});


	$('#jstree-fragments').jstree({
		  "core" : {
		  	    "animation" : 0,
		  	    "check_callback" : function(op, node, parent, pos, mor) {return op === 'create_node' ? true : false;},
		  	    "themes" : { "stripes" : true },
		  	    'data' : {"id": "rootNode", "text":"Fragments", "type":"root"}
		  	  },
		  	  "types" : types,
		  	  "plugins" : ["wholerow", "types"],
		  	  "checkbox" : {"three_state":false}
		});

};

// ------------------------------------------------
// search box -- 
$('#xml-search').keyup(function () {
    var v = $('#xml-search').val();
    $('#jstree-xmldata').jstree('search', v, true);
});
$('#xslt-search').keyup(function () {
    var v = $('#xslt-search').val();
    $('#jstree-xsltdata').jstree('search', v, true);
});
$('#output-search').keyup(function () {
    var v = $('#output-search').val();
    $('#jstree-outdata').jstree('search', v, true);
});



// ---------------------------------------------------------
// Test button

$('#xslRun').click(function() {
	var outputTree = $('#jstree-outdata').jstree(true);
	
	var rq = editorServices.requestPrototype();
	rq.action = "RUN";
	rq.fromTree = editorModel.xmlFileName;
	rq.targetTree = editorModel.outFileName;
	rq.templateTree = editorModel.xslFileName
	editorServices.run(rq, bindAndRedrawTree(outputTree));
});


// ------------------------------------------------------
// hide/show in/out/frag tree 

$('#showInput').click(function() {
	
	loadInputTree();

	$('[inputColumn]').removeAttr('hidden');
	$('[outputColumn]').attr('hidden', true);
	$('[fragmentsColumn]').attr('hidden', true);
	
	$('#showInput').removeClass("btn-warning").addClass("btn-outline-warning");
	$('#showOutput').removeClass("btn-outline-warning").addClass("btn-warning");
	$('#showFragments').removeClass("btn-outline-warning").addClass("btn-warning");
});

$('#showOutput').click(function() {
	
	loadOutputTree();
	
	$('[inputColumn]').attr('hidden', true);
	$('[outputColumn]').removeAttr('hidden');
	$('[fragmentsColumn]').attr('hidden', true);

	$('#showInput').removeClass("btn-outline-warning").addClass("btn-warning");
	$('#showOutput').removeClass("btn-warning").addClass("btn-outline-warning")
	$('#showFragments').removeClass("btn-outline-warning").addClass("btn-warning");
});

$('#showFragments').click(function() {
	
	loadFragmentsTree();
	
	$('[inputColumn]').attr('hidden', true);
	$('[outputColumn]').attr('hidden', true);
	$('[fragmentsColumn]').removeAttr('hidden');

	$('#showInput').removeClass("btn-outline-warning").addClass("btn-warning");
	$('#showOutput').removeClass("btn-outline-warning").addClass("btn-warning");
	$('#showFragments').removeClass("btn-warning").addClass("btn-outline-warning");
});

// ------------------------------------------------------
// load tree

function loadTree(fileName, treeInstance) {
	var rq = editorServices.requestPrototype();
	rq.action = "LOAD";
	rq.targetTree = fileName; // TODO move to better place 
	editorServices.load(rq, bindAndRedrawTree(treeInstance));
}

function emptyTree(title) {
	return {"id": "rootNode", "text": title, "type":"root"}
};

function loadFragmentsTree() {
	loadTree(editorModel.fragmentPrototypeFileName, $('#jstree-fragments').jstree(true))
};

function loadInputTree() {
	loadTree(editorModel.xmlFileName, $('#jstree-xmldata').jstree(true))
};

function loadTemplateTree() {
	loadTree(editorModel.xslFileName, $('#jstree-xsltdata').jstree(true))
};

function loadOutputTree() {
	loadTree(editorModel.outFileName, $('#jstree-outdata').jstree(true))
};


// ------------------------------------------------------
// Map an input text/element to template.
// User selects a node in each tree.
// Input node can be either a text of element.
// Template node should be a text. 
// "map" operation replaces the text with <xsl:value-of select="path of input node">
//
$('#copyInputPath').click(function() {

	// source and target
	var inputTree = $('#jstree-xmldata').jstree(true);
	var selectedInput = inputTree.get_selected(true)[0];
	var inputNode = selectedInput.original.bound.node;
	
	var templateTree = $('#jstree-xsltdata').jstree(true);
	var selectedTemplateNode = templateTree.get_selected(true)[0];
	var templateNode = selectedTemplateNode.original.bound.node;
	
	// call service
	var rq = editorServices.requestPrototype();
	rq.action = "MAP";
	rq.fromTree = $('#xmlFileName').val();
	rq.fromNode = inputNode;
	rq.targetTree = $('#xslFileName').val();
	rq.targetNode = templateNode;
	editorServices.map(rq, bindAndRedrawTree(templateTree)); // a-sync
});



// ------------------------------------------------------
// edit expression / rename 
// 
// open expression builder dialog 
$('#editTextModal').on('show.bs.modal', function (event) {
	
	// target template node
	var t = $('#jstree-xsltdata').jstree(true);
	if (t.get_selected(true).size > 0) {
		alert("select only single node in the template tree");
		return false;
	}
	var selectedTemplateNode = t.get_selected(true)[0];
	var tnode = selectedTemplateNode.original.bound;

	var button = $(event.relatedTarget); // Button that triggered the modal
	$('#xqueryTextArea').val(selectedTemplateNode.text);
	//modal.find('.modal-body input').val(selectedTemplateNode.text);
});

// close dialog and apply new expression 
$('#editTextModal').on('hide.bs.modal', function (event) {
	
	// TODO CANCEL button 
	
	// target template node: first selected 
	t = $('#jstree-xsltdata').jstree(true);
	var selectedTemplateNode = t.get_selected(true)[0];
	var tnode = selectedTemplateNode.original.bound;
		
	// call service
	var rq = editorServices.requestPrototype();
	rq.action = "SETVALUE";
	rq.newValue =  $('#xqueryTextArea').val();
	rq.targetTree = $('#xslFileName').val();
	rq.targetNode = tnode.node;
	editorServices.setvalue(rq, bindAndRedrawTree(t)); // a-sync
});

//---------------------------------------------------------
//rebind and redraw a tree with a POST response 
//
//parameters:
//@jstree   JSTree instance,such as $('#jstree-outdata').jstree(true)
//

function bindAndRedrawTree(jstree) {
	return function(data) {
		jstree.settings.core.data = jstreeBinder.bind(data);
		jstree.refresh(true); 	// TODO optimize: redraw only changed nodes
	}
};

// --------------------------------------------------------
// copy node event
// 1. copy from INPUT to XSLT is treated as a "(flat) map" gesture: instead of 
// copying the actual source node, we copy only the reference to the node ("value-of").
// 2. copy between two nodes of XSLT will do nothing -- we simply refresh the tree.
// 3. copy of "fragment" is treated as "create" gesture. the actual content of the source fragment 
// is ignored. the server decides what elements to create. 

$('#jstree-xsltdata').on('copy_node.jstree', function (event, data) {
	var inputTree = data.old_instance;
	var selectedInput = inputTree.get_node(data.original.id);
	var inputNode = selectedInput.original.bound.node;

	var templateTree = data.new_instance;
	var selectedTemplateNode = templateTree.get_node(data.parent); 
	var templateNode = selectedTemplateNode.original.bound.node;
	
	var x = $('#jstree-xmldata').jstree(true);
	var f = $('#jstree-fragments').jstree(true);
	
	if (inputTree == templateTree) { // same tree copy -- ignore, and refresh
		var rq = editorServices.requestPrototype();
		rq.action = "LOAD";
		rq.targetTree = $('#xslFileName').val();
		editorServices.load(rq, bindAndRedrawTree(templateTree));

	} else if (inputTree == x) {  // input xml to template: map
		var rq = editorServices.requestPrototype();
		rq.action = "MAP";
		rq.fromTree = $('#xmlFileName').val();
		rq.fromNode = inputNode;
		rq.targetTree = $('#xslFileName').val();
		rq.targetNode = templateNode;
		editorServices.map(rq, bindAndRedrawTree(templateTree)); // a-sync
	} else if (inputTree == f) {  // fragments to template: map
		var rq = editorServices.requestPrototype();
		rq.action = "INSERT";
		rq.fromTree = editorModel.fragmentPrototypeFileName;
		rq.fromNode = inputNode;
		rq.targetTree = $('#xslFileName').val();
		rq.targetNode = templateNode;
		editorServices.insert(rq, bindAndRedrawTree(templateTree)); // a-sync
	} 
	
});



