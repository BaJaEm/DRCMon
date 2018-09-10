(function() {

	var drcmon_app = angular.module("drcmon_app");

	var update_probe_controller = function($scope, $http, $window, $routeParams) {
		var onError = function(error) {
			$scope.error = error;
			alert(error);
		}
		var getProbeKeys = function() {
			$http.get("/api/probeKeys").then(function(data) {
				$scope.probeKeys = data.data._embedded.probeKeys
			});
		}
		getProbeKeys();
		var id = $routeParams.id

		$scope.action = "update";
		var getConfig = function() {
			$http.get("/api/probeConfigs/" + id + "?projection=full").then(
					function(data) {
						$scope.np = data.data;
						$scope.probeTypes = [ data.data.probeType ];
					}, onError);
		}

		getConfig();
		$scope.doAction = function(np) {
			
			if (np.probeKey && np.probeKey._links ){
				var probeKey = np.probeKey._links.probeKey.href;
				np.probeKey = probeKey;
		    }
			else
			{
				delete np.probeKey;
			}
			delete np.probeType;
			$http.patch(np._links.self.href, np).then(function() {
				$scope.foo = np;
				$window.location.href = '/';
			}, function(response) {
				$scope.error = response;
			});

		}
	}

	drcmon_app.controller("update_probe_controller", update_probe_controller);

}());