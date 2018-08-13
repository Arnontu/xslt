/**
 * 
 */

function xsltServices()  {
	return {
		
		urls: {
			save: "/save",
			setvalue: "/api/xslt/setvalue",
			map: "/api/xslt/map",
			insert: "/api/xslt/insert",
			run: "/api/xslt/run", 
			load: "/api/xslt/load"
		},
		
		// prototype object of editor service request
		requestPrototype: function() {
			return {
				action: null,
				fromTree: "source.xml",
				fromNode: {
					id: 0,
					type: "TEXT",
					value: "null",
					isAtribute: false,
					nodePath: "/"
				},
				
				fromFragment: "VALUEOF",
				newValue: "null",
				
				targetTree: "target.xml",
				fromNode: {
					id: 0,
					type: "TEXT",
					value: "null",
					isAtribute: false,
					nodePath: "/"
				},
				
			}  // request object proto
		}, 
		
		// base ajax POST 
		// @param rq	request object
		// @param url
		// @param success callback
		post: function(url, rq, callback) {
			$.ajax({
			    url: url,
			    data: JSON.stringify(rq), 
			    processData: false,
			    type: 'POST',
			    contentType: "application/json",
			    dataType: "json",
			    
			    success : function(data, status) {
			        //alert("Status: " + status);
			        callback(data);   //JSON.parse(data);
			    },

				error: function(data, status) {
				    alert("Data: " + data + "\nStatus: " + status);
				    // TODO exception???
				}
			 }); 
		},

		// set text value or rename element
		setvalue: function(rq, callback) {
			this.post(this.urls.setvalue, rq, callback);
		},
		
		// map input element as xsl:value-of 
		map: function(rq, callback) {
			this.post(this.urls.map, rq, callback);
		},
		
		// insert fragment to template 
		insert: function(rq, callback) {
			this.post(this.urls.insert, rq, callback);
		},
		
		// test: apply the XSLT template on sample input 
		run: function(rq, callback) {
			 this.post(this.urls.run, rq, callback);
		},

		// load: load/refresh the XSLT template from server 
		load: function(rq, callback) {
			 this.post(this.urls.load, rq, callback);
		}

	};  // return func
	
};