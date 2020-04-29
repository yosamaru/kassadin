'use strict';
(function()
{
	var serviceApp = angular.module('sample.service');
	serviceApp.factory('transactionService', ['$log', '$http', TransactionService]);
	
	function TransactionService($log, $http)
	{
		var ret = {};

		ret.findAll = function()
 	   	{
 	   	   	return $http({
 				method : 'GET',
 				url: _contextPath + '/transaction/find-all',
 			});
 	   	};
 	   	
		return ret;
	}
})();