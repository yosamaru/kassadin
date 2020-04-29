'use strict';
(function()
{
	var serviceApp = angular.module('sample.service');
	serviceApp.factory('chequeService', ['$log', '$http', ChequeService]);
	
	function ChequeService($log, $http)
	{
		var ret = {};
		
		ret.findAll = function()
 	   	{
 	   	   	return $http({
 				method : 'GET',
 				url: _contextPath + '/cheque/find-all',
 			});
 	   	};
 	   	
 	   	ret.add = function(cheque)
 	   	{
 	   	   	return $http({
 				method : 'POST',
 				url: _contextPath + '/cheque/create',
 				data:cheque
 			});
 	   	};

 	   	ret.deleteById = function(id)
 	   	{
 	   	   	return $http({
 				method : 'GET',
 				url: _contextPath + '/cheque/delete/',
 				params: {id: id},
 			});
 	   	};
		return ret;
	}
})();