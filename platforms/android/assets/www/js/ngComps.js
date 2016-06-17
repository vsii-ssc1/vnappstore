'use strict';
define({
	lib: [],
	app: {
		name:'vnappstore',
		deps:['ionic'],
		modules: {
			'controller' : [ 'controllers/aboutCtrl', 
			                 'controllers/appsCtrl', 
			                 'controllers/featuredCtrl', 
			                 'controllers/settingsCtrl',
			                 'controllers/startCtrl', 
			                 'controllers/updatesCtrl' ],
			'factory'   : [ ],
			'service'   : [ 'services/ClientConnector', 
			              'services/Service1', 
			              'services/Service2' ],
			'provider'  : [ ],
			'directive' : [ 'directives/w3Hello', 
			                'directives/w3TestDirective' ],
			'filter'    : [ 'filters/myFormat' ],
			'config'    : [ 'conf/routes' ],
			'run'       : [ 'conf/init' ]
		}
	}
});
