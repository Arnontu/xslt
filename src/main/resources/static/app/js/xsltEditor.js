

// JS Trees: input, template, output

var xmlTree = jstreeBinder.bind(xmlData);
var xsltTree = jstreeBinder.bind(xsltData);
var outputTree = jstreeBinder.bind(outputData);
var editorServices = xsltServices();

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
	
var fragmentsTree = [
	{"id": "rootNode", "text":"Fragments", "type":"root", "state": {"opened":true}, "children": [

		// XSL value-of
		{"id": "valueof", "text":"xsl:value-of", "type":"xsl","children": [
			{"id": "value-select", "text":"xsl:select", "type":"element","children": [
				{"id": "value-select-text", "text":"xquery expression", "type":"text"}
			]}
		]},
		
		// XSL copy-of
		{"id": "copyof", "text":"xsl:copy-of", "type":"xsl","children": [
			{"id": "copy-select", "text":"xsl:select", "type":"element","children": [
				{"id": "copy-select-text", "text":"xquery expression", "type":"text"}
			]}
		]},

		// XSL for each 
		{"id": "foreach", "text":"xsl:for-each", "type":"xsl","children": [
			{"id": "for-select", "text":"xsl:select", "type":"element","children": [
				{"id": "for-select-text", "text":"xquery expression", "type":"text"}
			]}
		]},
		
		// XSL if 
		{"id": "xslif", "text":"xsl:if", "type":"xsl","children": [
			{"id": "xslif", "text":"xsl:test", "type":"element","children": [
				{"id": "xslif-test-text", "text":"xquery expression", "type":"text"}
			]}
		]},
		
		// XSL choose when* 
		{"id": "choose", "text":"xsl:choose", "type":"xsl","children": [
			{"id": "choose-when", "text":"xsl:when", "type":"xsl","children": [
				{"id": "when-test", "text":"xsl:test", "type":"element","children": [
					{"id": "when-test-text", "text":"xquery expression", "type":"text"}
				]}
			]}
		]},

		// XSL attribute 
		{"id": "attribute", "text":"xsl:attribute", "type":"xsl","children": [
			{"id": "name", "text":"xsl:name", "type":"xsl"},
			{"id": "attrvalueof", "text":"xsl:value-of", "type":"xsl","children": [
				{"id": "attr-value-select", "text":"xsl:select", "type":"element","children": [
					{"id": "attr-value-select-text", "text":"xquery expression", "type":"text"}
				]}
			]},
		]},
	]}
];

$('#jstree-xmldata').jstree({
  	  "core" : {
  	    "animation" : 0,
  	    "check_callback" : false,
  	    "themes" : { "stripes" : true },
  	    'data' : xmlTree
  	  },
  	  "types" : types,
  	  "plugins" : ["search", "wholerow", "types"]
});

$('#jstree-xsltdata').jstree({
	  "core" : {
	  	    "animation" : 0,
	  	    "check_callback" : true,
	  	    "themes" : { "stripes" : true },
	  	    'data' : xsltTree
	  },
  	  "types" : types,
  	  "plugins" : ["search", "wholerow", "types"]
});


$('#jstree-outdata').jstree({
	  "core" : {
	  	    "animation" : 0,
	  	    "check_callback" : false,
	  	    "themes" : { "stripes" : true },
	  	    'data' : outputTree
	  	  },
	  	  "types" : types,
	  	  "plugins" : ["search", "wholerow", "types"]
});


$('#jstree-fragments').jstree({
	  "core" : {
	  	    "animation" : 0,
	  	    "check_callback" : function(op, node, parent, pos, mor) {return op === 'create_node' ? true : false;},
	  	    "themes" : { "stripes" : true },
	  	    'data' : fragmentsTree
	  	  },
	  	  "types" : types,
	  	  "plugins" : ["wholerow", "types"],
	  	  "checkbox" : {"three_state":false}
	});



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



//---------------------------------------------------------
//rebind and redraw a tree with a POST response 
//
// parameters:
// @jstree   JSTree instance,such as $('#jstree-outdata').jstree(true)
//

function bindAndRedrawTemplate(jstree) {
	return function(data) {
		jstree.settings.core.data = jstreeBinder.bind(data);
		jstree.refresh(true); 	// TODO optimize: redraw only changed nodes
	}
};


// ---------------------------------------------------------
// Test button

$('#xslRun').click(function() {
	var outputTree = $('#jstree-outdata').jstree(true);
	
	var rq = editorServices.requestPrototype();
	rq.action = "RUN";
	rq.fromTree = $('#xmlFileName').val();
	rq.targetTree = $('#outFileName').val();
	rq.templateTree = $('#xslFileName').val();
	editorServices.run(rq, bindAndRedrawTemplate(outputTree));
});


// ------------------------------------------------------
// hide/show in/out/frag tree 

$('#showInput').click(function() {
	$('[inputColumn]').removeAttr('hidden');
	$('[outputColumn]').attr('hidden', true);
	$('[fragmentsColumn]').attr('hidden', true);
	
	$('#showInput').removeClass("btn-warning").addClass("btn-outline-warning");
	$('#showOutput').removeClass("btn-outline-warning").addClass("btn-warning");
	$('#showFragments').removeClass("btn-outline-warning").addClass("btn-warning");
});

$('#showOutput').click(function() {
	$('[inputColumn]').attr('hidden', true);
	$('[outputColumn]').removeAttr('hidden');
	$('[fragmentsColumn]').attr('hidden', true);

	$('#showInput').removeClass("btn-outline-warning").addClass("btn-warning");
	$('#showOutput').removeClass("btn-warning").addClass("btn-outline-warning")
	$('#showFragments').removeClass("btn-outline-warning").addClass("btn-warning");
});

$('#showFragments').click(function() {
	$('[inputColumn]').attr('hidden', true);
	$('[outputColumn]').attr('hidden', true);
	$('[fragmentsColumn]').removeAttr('hidden');

	$('#showInput').removeClass("btn-outline-warning").addClass("btn-warning");
	$('#showOutput').removeClass("btn-outline-warning").addClass("btn-warning");
	$('#showFragments').removeClass("btn-warning").addClass("btn-outline-warning");
});


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
	editorServices.map(rq, bindAndRedrawTemplate(templateTree)); // a-sync
});



// ------------------------------------------------------
// edit expression / rename 
// 
// open expression builder dialog 
$('#editTextModal').on('show.bs.modal', function (event) {
	
	// target template node
	t = $('#jstree-xsltdata').jstree(true);
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
	editorServices.setvalue(rq, bindAndRedrawTemplate(t)); // a-sync
});

//---------------------------------------------------------
// bind and redraw 
function bindAndRedrawTemplate(jstree) {
	return function(data) {
		jstree.settings.core.data = jstreeBinder.bind(data);
		jstree.refresh(true); 	// TODO optimize: redraw only changed nodes
	}
};


