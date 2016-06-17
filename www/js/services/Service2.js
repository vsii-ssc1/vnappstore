'use strict';
define(['./Service1'], function(Service1){
	return function($state){
		console.log('Service2');
		return $.extend({}, Service1, {
			go: function(id){
				console.info('go', id);
				$state.go(id, {}, {});
			}
		});
	};
});

