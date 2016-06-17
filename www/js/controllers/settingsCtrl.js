'use strict';
define(function(){
	return function($scope, $rootScope, Service2) {
		console.log('settingsCtrl');
		console.log('$rootScope.timer', $rootScope.timer);
		$scope.goAbout = function(){
			Service2.go('app.about');
		};
	};
});
 