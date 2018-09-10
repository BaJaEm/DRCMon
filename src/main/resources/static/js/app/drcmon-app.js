(function() {

	var drcmon_app = angular.module("drcmon_app", [ 'ngResource', 'ngCookies', 'ngRoute' ]);

	drcmon_app.config(function($routeProvider) {
		$routeProvider.when("/add/", {
			'templateUrl' : "add_probe.html",
			'controller' : "add_probe_controller"
		}).when("/update/:id", {
			'templateUrl' : "update_probe.html",
			'controller' : "update_probe_controller"
		}).when("/key/", {
			'templateUrl' : "manage_keys.html",
			'controller' : "manage_keys_controller"
		}).otherwise({
			'templateUrl' : "overview.html",
			'controller' : "overview_controller"
		})
	});

}());
