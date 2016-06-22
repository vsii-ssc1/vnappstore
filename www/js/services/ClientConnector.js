'use strict';
define(function($http){
	return function(){
		console.log('ClientConnector');
		return {
			rootPath: 'http:/vnappstore.mybluemix.net/vnas/api/v1/apps',
			actions : {
				featuredApps: '/featured',
				latestApps: '/latest',
				mostDownloadApps: '/most-download',
				allApps: '/'
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

