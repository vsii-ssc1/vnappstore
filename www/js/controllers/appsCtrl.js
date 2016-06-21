'use strict';
define(function(){
	return function($scope, $rootScope) {
		console.log('appsCtrl');
		console.log('$rootScope.timer', $rootScope.timer);
		$scope.apps = [{name: 'IBM Softphone', scheme: 'ibmel://'}, {name: 'App Catalog', scheme: 'maas360://'}, {name: 'Secure Browser', scheme: 'maas360browser://'}]
	};
});

 