(function() {

	var drcmon_app = angular.module("drcmon_app");
	var manage_categories_controller = function($scope, $http, $window,
			helper_service) {

		helper_service.getProbeCategories($scope);
		
		$scope.modify = {};

		$scope.updateCategory = function(item) {
			$scope.modify = {};
			$scope.modify[item.id] = true;
		}

		$scope.cancelCategory = function(item) {
			$scope.modify[item.id] = false;
		}

		$scope.addCategory = function(item) {
			$http.post("/api/probeCategories", item).then(
					$window.location.href = '/#!/category', function(response) {
						alert(response)
					});
		}

		$scope.saveCategory = function(item) {
			$http.patch(item._links.self.href, item).then(
					$window.location.href = '/#!/category', function(response) {
						alert(response)
					});
		}
	}
	drcmon_app.controller("manage_categories_controller",
			manage_categories_controller);

}());