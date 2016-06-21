'use strict';
define(function(){
	return function(){
		return {
			restrict: 'E',
 	        scope: {
 	        	label: '=',
 	        	scheme: '=',
 	        	disable: '='
 	        },
			template: '<button ng-disable="disable" ng-click="open();">{{label}}</button>',
			link: function (scope, element, attrs) {
				window.plugins.appPlugin.check(scope.scheme, function(data){
					data = JSON.parse(data);
					if(data.success == true){
						scope.label = 'Open'
					} else {
						scope.label = 'Install'
					}
					scope.disable = false;
					scope.$evalAsync();
				});
				
				scope.open = function(){
					if(scope.label == 'Open'){
						window.plugins.appPlugin.open(scope.scheme, function(data){});
					} else {
						scope.install();
					}
				}
				scope.install = function(){
					alert('Hello');
				}
			}
		};
	};
});
