'use strict';
define(function(){
	return function(){
		console.log('Service1');
		return {
			print: function(msg) {
				console.info('Service1:', msg);
			}
		};
	};
});

