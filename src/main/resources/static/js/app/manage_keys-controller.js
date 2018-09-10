(function() {

	var drcmon_app = angular.module("drcmon_app");
	var manage_keys_controller = function($scope, add_probe_service, $http,
			$window) {

		var getKeys = function() {
			$http.get("/api/probeKeys").then(function(data) {
				$scope.probeKeys = data.data._embedded.probeKeys
			});
		}
		getKeys();
		$scope.addKey = function(item) {
			if (item.pw1 != item.pw2) {
				alert("passwords must match")
			} else {
				item.secret = item.pw1 ? item.pw1 : "";
				$http.post("/api/probeKeys", item).then(
						$window.location.href = '/#!/keys', function(response) {
							alert(response)
						});
			}
		}
	}
	drcmon_app.controller("manage_keys_controller", manage_keys_controller);

}());