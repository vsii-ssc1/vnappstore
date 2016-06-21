'use strict';
define(function(){
	return function($scope, $rootScope) {
		console.log('updatesCtrl');
		$scope.apps = [{name: 'Workday', scheme: 'hrms://'}, {name: 'Calendar', scheme: 'calshow://'}]
		console.log('$rootScope.timer', $rootScope.timer);
	};
});
      
