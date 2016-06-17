'use strict';
define(function(){
	return function($scope, $rootScope, Service1, Service2) {
		console.log('startCtrl');
		
		$scope.goFeatured = function(){
			Service1.print('goFeatured');
			Service2.go('app.tabsController.featured');
		};
		
		$scope.names = ['Jani', 'Carl', 'Margareth', 'Hege', 'Joe', 'Gustav', 'Birgit', 'Mary', 'Kai'];
	};
});
 