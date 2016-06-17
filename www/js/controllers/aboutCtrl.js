'use strict';
define(function(){
	return function($scope, $rootScope, Service1, Service2) {
		console.log('aboutCtrl');
		console.log('$rootScope.timer', $rootScope.timer);
		
		$scope.goFeatured = function(){
			Service1.print('goFeatured');
			Service2.go('app.tabsController.featured');
		};
		
		$scope.names = ['Jani', 'Carl', 'Margareth', 'Hege', 'Joe', 'Gustav', 'Birgit', 'Mary', 'Kai'];
	};
});
