(function() {

	var drcmon_app = angular.module("drcmon_app");

	var overview_service = function($log, $http) {
		return {

		};
	}

	drcmon_app.factory("overview_service", overview_service);

	var overview_controller = function($scope, overview_service, $http, $window) {

		$scope.status = "Initializing..."

		var onError = function(error) {
			$scope.error = error;
			alert(error);
			refreshStatus();
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
			}, onError);
		}
		$scope.start = function() {
			$http.get("/start").then(function(data) {
				refreshStatus()
			}, onError)
		}

		$scope.stop = function() {
			$http.get("/stop").then(function(data) {
				refreshStatus()
			}, onError)
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
			}, onError)
		}
		refreshStatus();
		refreshData();
	}

	drcmon_app.controller("overview_controller", overview_controller);

}());