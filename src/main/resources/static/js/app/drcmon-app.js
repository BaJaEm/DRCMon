(function() {

	var drcmon_app = angular.module("drcmon_app", [ 'ngResource', 'ngCookies',
			'ngRoute', 'datatables', 'datatables.bootstrap' ]);

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
		}).when("/category/", {
			'templateUrl' : "manage_categories.html",
			'controller' : "manage_categories_controller"
		}).otherwise({
			'templateUrl' : "overview.html",
			'controller' : "overview_controller"
		})
	});

	var helper_service = function($log, $http) {
		var s = {};

		s.onError = function(error, $scope) {
			$scope.error = error;
			alert(error);
			refreshStatus();
		};

		s.getProbeTypes = function(scope) {
			$http.get("/api/probeTypes").then(function(data) {
				scope.probeTypes = data.data._embedded.probeTypes
			});
		};

		s.getProbeKeys = function(scope) {
			$http.get("/api/probeKeys").then(function(data) {
				scope.probeKeys = data.data._embedded.probeKeys

			});
		};

		s.getProbeCategories = function(scope) {
			$http.get("/api/probeCategories").then(function(data) {
				scope.probeCategories = data.data._embedded.probeCategories
			});
		}

		return s;
	};

	drcmon_app.factory("helper_service", helper_service);

}());
