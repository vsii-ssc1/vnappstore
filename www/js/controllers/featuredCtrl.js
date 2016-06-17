'use strict';
define(function(){
	return function($scope, $rootScope, $timeout, Service2) {
		console.log('featuredCtrl');
		$scope.timer = 0;
		$rootScope.timer = $scope.timer;
		
		$timeout(function(){
			myTimer();
		}, 1000);
		
		var timerInterval = function() {
			$timeout(function(){
				myTimer();
			}, 1000);
		};

		var myTimer = function() {
			$scope.$apply(function(){
				$scope.timer = new Date();
				$rootScope.timer = $scope.timer;
				timerInterval();
			});
		};
		
		$scope.goSettings = function(){
			Service2.go('app.settings');
		};
	};
});
