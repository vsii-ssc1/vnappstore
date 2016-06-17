'use strict';
define(['ngComps'], function(comps){
	window.ngConf = {
			data: comps,
			init: function(callback, data) {
				var conf = data||this.data;
				var counter = 0;
				var mLen = 0;
				var _onComplete = function() {
					++counter;
					console.log('App-counter', counter, mLen);
					if (counter == mLen ) {
						console.log('angular/ionic modules load done');
						if (callback) {
							callback();
						}
					}
				};
				if (conf.lib && conf.lib.length > 0) {
					++mLen;
					require(conf.lib, function(){
						console.log('AMD module/shim have been loadded');
						_onComplete();
					});
				};
				if (conf.app) {
					++mLen;
					var app = conf.app;
					var ng = angular.module(app.name, app.deps);
					this.load(ng, app.modules, function(){
						console.log('App['+app.name+'] has been loaded');
						_onComplete();
					});
					// save somethings
					this.appName = app.name;
					this.ngApp = ng;
				}
			},
			load: function(ng, ngMods, callback){
				console.log('requirejs analyzing...');
				// make sure 'run' always at the end of line
				var keys = Object.keys(ngMods);
				var counter = 0;
				var mLen = 0;
				// loading things
				for (var k in ngMods) {
					var comps = ngMods[k];
					if (comps.length > 0) {
						++mLen;
						(function(type, mods){
							require(mods, function(){
								for (var i=0; i<arguments.length; i++) {
									var mName = mods[i];
									var mod = arguments[i];
									if(!(type == 'run' || type == 'config')){
										var ix = mName.lastIndexOf("/");
										if (ix != -1) {
											mName = mName.substring(ix + 1);
										}
									}
									
									if (type == 'run' || type == 'config') {
										ng[type](mod);
									} else {
										ng[type](mName, mod);
									}
									console.log('angularjs registered module', type, mName);
								}// for i
								++counter;
								console.log('Module-counter', counter, mLen);
								if (counter == mLen) {
									if (callback) {
										callback();
									};
								}
							});
						})(k, comps);
					}
				}// for n
			},
			start: function(name) {
				console.log('angular/ionic ready');
			    angular.bootstrap(document, [name||this.appName]);
			}
	};
	return window.ngConf;
});