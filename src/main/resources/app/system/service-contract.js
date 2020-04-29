'use strict';
(function()
{
	var serviceApp = angular.module('sample.service');
	serviceApp.factory('contractService', ['$log', '$http', ContractService]);
	
	function ContractService($log, $http)
	{
		var ret = {};

		ret.findAll = function()
 	   	{
 	   	   	return $http({
 				method : 'GET',
 				url: _contextPath + '/contract/find-all',
 			});
 	   	};
 	   	
 	   	ret.add = function(contract)
 	   	{
 	   	   	return $http({
 				method : 'POST',
 				url: _contextPath + '/contract/create',
 				data:contract
 			});
 	   	};
 	   	
 	   	ret.audit = function(id)
 	   	{
 	   	   	return $http({
 				method : 'GET',
 				url: _contextPath + '/contract/audit/',
 				params: {id: id},
 			});
 	   	};
		return ret;
	}
})();