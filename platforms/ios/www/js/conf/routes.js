'use strict';
define(['../ngRoutes'], function(routes){
	return function($stateProvider, $urlRouterProvider) {
		  // Ionic uses AngularUI Router which uses the concept of states
		  // Learn more here: https://github.com/angular-ui/ui-router
		  // Set up the various states which the app can be in.
		  // Each state's controller can be found in controllers.js
			// register state for all routes
			var defaultPath = '/index';
			var route = null;
			for (var k in routes) {
				route = routes[k];
				$stateProvider.state(k, route);
				if (route.isDefault) {
					defaultPath = route.url;
				}
			}
			// default state
			$urlRouterProvider.otherwise(defaultPath||'/index');
		};
});

