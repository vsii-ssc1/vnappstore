'use strict';
define(function(){
	return function($scope, $rootScope, $timeout, Service2) {
		console.log('featuredCtrl');
		$scope.timer = 0;
		$rootScope.timer = $scope.timer;
		$scope.mostDowdloadApps = [{name: 'Workday', scheme: 'hrms://'}, {name: 'Calendar', scheme: 'calshow://'},{name: 'Bluepages+', scheme: 'mbpp://'}];
		$scope.featureApps = [{name: 'AppStore', scheme: 'appstore://'}, {name: 'Vani', scheme: 'vani://'}];
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
