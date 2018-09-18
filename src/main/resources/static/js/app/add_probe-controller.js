(function() {

	var drcmon_app = angular.module("drcmon_app");

	var add_probe_controller = function($scope, $http, $window, helper_service) {

		helper_service.getProbeTypes($scope);
		helper_service.getProbeKeys($scope);
		helper_service.getProbeCategories($scope);

		$scope.np = {
			"enabled" : true,
			"pollingInterval" : 30,
			"delayTime" : 0,
			"customConfiguration" : {}
		};
		$scope.action = "add";
		$scope.doAction = function(item) {

			if (item.pt.name == 'Ping' || item.pt.name == 'PortMon') {
				item.customConfiguration.HOST = item.customConfigurationTemp.HOST;
			}
			if (item.pt.name == 'PortMon') {
				item.customConfiguration.PORT = item.customConfigurationTemp.PORT;
			}
			if (item.pt.name == 'RESTGet') {
				item.customConfiguration.PATH = item.customConfigurationTemp.PATH;
			}
			if (item.pt.name == 'SQLQuery' || item.pt.name == 'RESTGet') {
				item.customConfiguration.URL = item.customConfigurationTemp.URL;
				item.customConfiguration.EXPECTED = item.customConfigurationTemp.EXPECTED;
				item.customConfiguration.KEYFILE = item.customConfigurationTemp.KEYFILE;
			}
			if (item.pt.name == 'SQLQuery') {
				item.customConfiguration.QUERY = item.customConfigurationTemp.QUERY;
			}
			item.probeType = item.pt._links.self.href;

			if (item.probeKey && item.probeKey._links) {
				var probeKey = item.probeKey._links.probeKey.href;
				item.probeKey = probeKey;
			} else {
				delete item.probeKey;
			}

			if (item.probeCategory && item.probeCategory._links) {
				var probeCategory = item.probeCategory._links.probeCategory.href;
				item.probeCategory = probeCategory;
			} else {
				delete item.probeCategory;
			}
			$http.post("/api/probeConfigs", item).then(
					$window.location.href = '/#!/add', function(response) {
						alert(response)
					});
		}
	}

	drcmon_app.controller("add_probe_controller", add_probe_controller);

}());