'use strict';
define({
	'start' : {
		isDefault: true,
		url : '/pageStart',
		templateUrl : 'templates/start.html',
		controller : 'startCtrl'
	},
	'app' : {
		abstract : true,
		url : '/app',
		templateUrl : 'templates/mainMenu.html'
	},
	'app.tabsController' : {
		abstract : true,
		url : '/tap',
		views : {
			'pageView' : {
				templateUrl : 'templates/tabsController.html',
			}
		}
	},
	'app.tabsController.featured' : {
		url : '/pageFeatured',
		views : {
			'tab1' : {
				templateUrl : 'templates/featured.html',
				controller : 'featuredCtrl'
			}
		}
	},
	'app.tabsController.apps' : {
		cache: true,
		url : '/pageApps',
		views : {
			'tab2' : {
				templateUrl : 'templates/apps.html',
				controller : 'appsCtrl'
			}
		}
	},
	'app.tabsController.updates' : {
		url : '/pageUpdates',
		views : {
			'tab3' : {
				templateUrl : 'templates/updates.html',
				controller : 'updatesCtrl'
			}
		}
	},
	'app.about' : {
		url : '/pageAbout',
		views : {
			'pageView' : {
				templateUrl : 'templates/about.html',
				controller : 'aboutCtrl'
			}
		}
	},
	'app.settings' : {
		url : '/pageSettings',
		views : {
			'pageView' : {
				templateUrl : 'templates/settings.html',
				controller : 'settingsCtrl'
			}
		}
	}
});