'use strict';
(function()
{
	var serviceApp = angular.module('sample.service');
	serviceApp.factory('checkService', ['$log', '$http', CheckService]);
	
	function CheckService($log, $http)
	{
		var ret = {};
		
		ret.check = function()
 	   	{
 	   	   	return $http({
 				method : 'GET',
 				url: _contextPath + '/check/is-login',
 			});
 	   	};

 	   	ret.statistics = function()
        {
            return $http({
                method : 'GET',
                url: _contextPath + '/check/statistics',
            });
        };
 	   	
		return ret;
	}
})();