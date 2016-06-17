'use strict';
define(function($http){
	return function(){
		console.log('ClientConnector');
		return {
			rootPath: 'http://192.168.100.144:3000/vnapps',
			actions : {
				featuredApps: '/featured',
				latestApps: '/latest',
				mostDownloadApps: '/most-download',
				allApps: '/apps'
			},
			getFeaturedApps: function() {
				var uri = this.actions.featuredApps;
				return this.query(uri);
			},
			getLatestApps: function() {
				var uri = this.actions.latestApps;
				return this.query(uri);
			},
			getMostDownloadApps: function() {
				var uri = this.actions.mostDownloadApps;
				return this.query(uri);
			},
			getApps: function(pageId) {
				var uri = this.actions.allApps;
				return this.query(uri, {'pageId': pageId});
			},
			query: function(uri, params) {
				var noCached = (new Date().getTime());
				var url = this.rootPath + uri;
				if (url.lastIndexOf('?') == -1) {
					url += '?noCached='+noCached;
				} else {
					url += '&noCached='+noCached;
				}
				if (params) {
					for (var k in params) {
						url += '&' + k + '=' + encodeURIComponent(params[k]);
					}
				}
				console.log('query: GET', url);
				return $http({'method': 'GET', 'url':url});
			}
		};
	};
});

