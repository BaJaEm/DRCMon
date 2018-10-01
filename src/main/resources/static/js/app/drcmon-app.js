(function() {

	var drcmon_app = angular.module("drcmon_app", [ 'ngResource', 'ngCookies',
			'ngRoute' ]);

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
	
	drcmon_app.config(function($httpProvider) {
		  var authToken = $('meta[name="csrf-token"]').attr('content');
		  $httpProvider.defaults.headers.common["X-CSRF-TOKEN"] = authToken;
	});

	var navigation = function($rootScope, $http, $location, $route) {

		$http.get('/user').then(function(data) {
			$rootScope.user = data.data;
			if (data.data.authenticated) {
				$rootScope.authenticated = true;
			} else {
				$rootScope.authenticated = false;
			}
		}, function() {
			$rootScope.authenticated = false;
		});

		$rootScope.credentials = {};

		$rootScope.logout = function() {
			$http.post('logout', {}).then(function() {
				$rootScope.authenticated = false;
				$location.path("/");
			},function(data) {
				$rootScope.authenticated = false;
			});
		};
	};
	drcmon_app.controller("navigation", navigation);

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
