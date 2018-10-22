(function() {

	var drcmon_app = angular.module("drcmon_app");

	var update_probe_controller = function($scope, $http, $window,
			$routeParams, helper_service) {

		helper_service.getProbeKeys($scope);
		helper_service.getProbeCategories($scope);

		var id = $routeParams.id

		$scope.action = "update";
		var getConfig = function() {
			$http.get("/api/probeConfigs/" + id + "?projection=full").then(
					function(data) {
						$scope.np = data.data;
						$scope.probeTypes = [ data.data.probeType ];
						$scope.np.pt = data.data.probeType;

					}, function(error) {
						alert(error);
					});
		}

		getConfig();
		$scope.doAction = function(np) {

			if (np.probeKey && np.probeKey._links) {
				var probeKey = np.probeKey._links.probeKey.href;
				np.probeKey = probeKey;
			} else {
				delete np.probeKey;
			}
			
			if (np.probeCategory && np.probeCategory._links) {
				var probeCategory = np.probeCategory._links.probeCategory.href;
				np.probeCategory = probeCategory;
			} else {
				delete np.probeCategory;
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