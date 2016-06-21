
function AppPlugin(){
	if (!(this instanceof AppPlugin)) {
        throw new TypeError("AppPlugin constructor cannot be called as a function.");
    }//end if
}
AppPlugin.prototype = {
		
		check: function(scheme, callback){
			if(window.device)
				cordova.exec(callback, null, 'AppPlugin', 'check', [scheme]);
		},
		open: function(scheme, callback){
			console.log(window.device);
			if(window.device)
				cordova.exec(callback, null, 'AppPlugin', 'open', [scheme]);
		}
}
AppPlugin.install = function () {
	if (!window.plugins) {
		window.plugins = {};
	}
	window.plugins.appPlugin = new AppPlugin();
	return window.plugins.appPlugin;
};

cordova.addConstructor(AppPlugin.install);
