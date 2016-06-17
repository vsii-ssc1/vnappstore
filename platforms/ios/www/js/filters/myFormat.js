'use strict';
define(function(){
	return function(){
		return function(x) {
	        var i, c, txt = "";
	        for (i = 0; i < x.length; i++) {
	            c = x[i];
	            if (i % 2 == 0) {
	                c = c.toUpperCase();
	            }
	            txt += c;
	        }
	        return txt;
	    };
	};
});

