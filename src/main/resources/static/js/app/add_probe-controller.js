(function() {

	var drcmon_app = angular.module("drcmon_app");

	var add_probe_service = function($log, $http) {

		return {};
	}

	drcmon_app.factory("add_probe_service", add_probe_service);

	var add_probe_controller = function($scope, add_probe_service, $http,
			$window) {
		var getProbeTypes = function() {
			$http.get("/api/probeTypes").then(function(data) {
				$scope.probeTypes = data.data._embedded.probeTypes
			});
		}
		getProbeTypes();
		
		var getProbeKeys = function() {
			$http.get("/api/probeKeys").then(function(data) {
				$scope.probeKeys = data.data._embedded.probeKeys
			});
		}
		getProbeKeys();
		
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
			
			if (item.probeKey._links ){
				var probeKey = item.probeKey._links.probeKey.href;
				item.probeKey = probeKey;
		    }
			else
			{
				delete item.probeKey;
			}
			$http.post("/api/probeConfigs", item).then(
					$window.location.href = '/#!/add', function(response) {
						alert(response)
					});
		}
	}

	drcmon_app.controller("add_probe_controller", add_probe_controller);

}());