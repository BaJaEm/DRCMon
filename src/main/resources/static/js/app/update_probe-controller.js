(function() {

	var drcmon_app = angular.module("drcmon_app");

	var update_probe_controller = function($scope, $http, $window, $routeParams) {
		var onError = function(error) {
			$scope.error = error;
			alert(error);
		}

		var getProbeTypes = function(next) {
			$http.get("/api/probeTypes").then(function(data) {
				$scope.probeTypes = data.data._embedded.probeTypes;
				next();
			});
		}
		var id = $routeParams.id

		$scope.action = "update";
		var getConfig = function() {
			alert("/api/probeConfigs/" + id + "?inlineProbeConfig");
			$http.get("/api/probeConfigs/" + id + "?projection=inlineProbeConfig").then(
					function(data) {
						$scope.np = data.data;
						for (var x = 0; x < $scope.probeTypes.length; x++) {
							if ($scope.probeTypes[x].href = "") {
								//
							}
						}
					}, onError);
		}

		getProbeTypes(getConfig);

		$scope.doAction = function(item) {

			if (item.pt.name == 'Ping' || item.pt.name == 'PortMon') {
				item.customConfiguration.HOST = item.customConfigurationTemp.HOST;
			}
			if (item.pt.name == 'PortMon') {
				item.customConfiguration.PORT = item.customConfigurationTemp.PORT;
			}
			if (item.pt.name == 'SQLQuery' || item.pt.name == 'RESTGet') {
				item.customConfiguration.URL = item.customConfigurationTemp.URL;
				item.customConfiguration.PATH = item.customConfigurationTemp.PATH;
				item.customConfiguration.EXPECTED = item.customConfigurationTemp.EXPECTED;
				item.customConfiguration.KEYFILE = item.customConfigurationTemp.KEYFILE;

			}
			if (item.pt.name == 'SQLQuery') {
				item.customConfiguration.QUERY = item.customConfigurationTemp.QUERY;
			}
			item.probeType = item.pt._links.self.href;
			$http.put(item._links.self.href, item).then($scope.foo = item
			// $window.location.href = '/#!/add'
			, function(response) {
				alert(response)
			});

		}
	}

	drcmon_app.controller("update_probe_controller", update_probe_controller);

}());