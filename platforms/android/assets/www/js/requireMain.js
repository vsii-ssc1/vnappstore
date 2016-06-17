'use strict';
// Require Configuration
require.config({
    baseUrl: "js",
    paths: {
        "controller": "controllers",
        "directive" : "directives",
        "filter"    : "filters",
        "service"   : "services",
        "config"    : "conf",
        "run"       : "conf",
    },
    /**
	 * for libs that either do not support AMD out of the box, or require
	 * some fine tuning to dependency mgt'
	 */
    shim: {
	    'underscore':{
    		exports: '_'
	    },
    	'Q' : {
            exports: 'Q'
        }
    },
    waitSeconds: 15
});

window.ionicLoad = function(){
	// angular/ionic modules load
	require(['ngApp'], function(ng){
		console.log('requirejs loading...');
		ng.init(function(){
			console.log('loading process done');
			ng.start();
		});
	});
};

// onload action
angular.element(document).ready(function(){
	window.ionicLoad();
});