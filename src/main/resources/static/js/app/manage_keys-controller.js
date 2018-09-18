(function() {

	var drcmon_app = angular.module("drcmon_app");
	var manage_keys_controller = function($scope, $http, $window,
			helper_service) {

		helper_service.getProbeKeys($scope);

		$scope.modify = {};

		$scope.addKey = function(item) {
			if (item.pw1 != item.pw2) {
				alert("passwords must match")
			} else {
				item.secret = item.pw1 ? item.pw1 : "";
				$http.post("/api/probeKeys", item).then(
						$window.location.href = '/#!/key', function(response) {
							alert(response)
						});
			}
		}

		$scope.updateKey = function(item) {
			$scope.modify = {};
			$scope.modify[item.id] = true;
		}

		$scope.cancelKey = function(item) {
			$scope.modify[item.id] = false;
		}

		$scope.saveKey = function(item) {
			if (item.pw1 != item.pw2) {
				alert("passwords must match")
			} else {
				if (item.pw1) {
					item.secret = item.pw1
				}
				$http.patch(item._links.self.href, item).then(
						$window.location.href = '/#!/key', function(response) {
							alert(response)
						});
			}
		}
	}
	drcmon_app.controller("manage_keys_controller", manage_keys_controller);

}());