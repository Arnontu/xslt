/**
 * 
 */


function readTextFile(file)
{
	var reader = new FileReader();
	reader.readAsText("C:/src/spring/gs-serving-web-content/complete/src/test/resources/jstree/butters.xml");
	reader.addEventListener("loadend", function() {
		   // reader.result contains the contents of blob as a typed array
		});
	reader.readAsText(blob);
}


// var text = readTextFile("file:///C:/your/path/to/file.txt");
//var text = readTextFile("C:/src/spring/gs-serving-web-content/complete/src/test/resources/jstree/butters.xml");
var text = "<butters>world</butters>"

const binder = require('../../../main/resources/static/app/js/jstreeBinder');
var tree = binder.bind(text);    
	
console.log(tree);


