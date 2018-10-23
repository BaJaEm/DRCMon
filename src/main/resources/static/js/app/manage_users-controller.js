(function() {

	var drcmon_app = angular.module("drcmon_app");
	var manage_users_controller = function($scope, $http, $window,
			helper_service) {

		helper_service.getProbeUsers($scope);

		$scope.modify = {};

		$scope.addUser = function(item) {
			if (item.pw1 != item.pw2) {
				alert("passwords must match")
			} else {
				item.secret = item.pw1 ? item.pw1 : "";
				$http.post("/api/probeUsers", item).then(
						$window.location.href = '/#!/users', function(response) {
							alert(response)
						});
			}
		}

		$scope.updateUser = function(item) {
			$scope.modify = {};
			$scope.modify[item.id] = true;
		}

		$scope.cancelUser = function(item) {
			$scope.modify[item.id] = false;
		}

		$scope.saveUser = function(item) {
			if (item.pw1 != item.pw2) {
				alert("passwords must match")
			} else {
				if (item.pw1) {
					item.secret = item.pw1
				}
				$http.patch(item._links.self.href, item).then(
						$window.location.href = '/#!/users', function(response) {
							alert(response)
						});
			}
		}
	}
	drcmon_app.controller("manage_users_controller", manage_users_controller);

}());