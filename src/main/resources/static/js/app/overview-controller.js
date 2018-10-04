(function() {

	var drcmon_app = angular.module("drcmon_app");

	var overview_controller = function($scope, $http, $window, helper_service) {

		$scope.status = "Initializing...";

		helper_service.getProbeTypes($scope);
		helper_service.getProbeCategories($scope);

		$scope.c_filter_list = [ {
			'name' : '--any--'
		} ].concat($scope.probeCategories);

		$scope.myFilter = function(a, b) {

			if (!b || b == null || b == 'null' || angular.equals(b, {})) {

				return true;
			}
			if (a) {
				return a.match(".*" + b + ".*");
			}
			return false;
		}
		var refreshStatus = function() {
			$http.get("/status").then(function(data) {
				$scope.running = data.data;
				$scope.start_disabled = $scope.running;
				$scope.stop_disabled = !$scope.running;
				$scope.debug = data.data;
				if ($scope.running) {
					$scope.status = "Running";
				} else {
					$scope.status = "Not Running";
				}
			}, function(error) {
				alert(error);
			});
		}
		$scope.start = function() {
			$http.get("/start").then(function(data) {
				refreshStatus()
			}, function(error) {
				alert(error);
			})
		}

		$scope.stop = function() {
			$http.get("/stop").then(function(data) {
				refreshStatus()
			}, function(error) {
				alert(error);
			})
		}

		$scope.update = function(config) {
			var ref = config._links.self.href;
			var n = ref.lastIndexOf("/");
			var id = ref.substring(n + 1);
			$window.location.href = '/#!/update/' + id;
		}

		var refreshData = function() {
			$http.get("/api/probeConfigs").then(function(data) {
				$scope.data = data.data._embedded.probeConfigs;
			}, function(error) {
				alert(error);
			})
		}
		refreshStatus();
		refreshData();
	}

	drcmon_app.controller("overview_controller", overview_controller);

}());