'use strict';
(function()
{
	var serviceApp = angular.module('sample.service');
	serviceApp.factory('publisherService', ['$log', '$http', PublisherService]);
	
	function PublisherService($log, $http)
	{
		var ret = {};
		
		ret.findAll = function()
 	   	{
 	   	   	return $http({
 				method : 'GET',
 				url: _contextPath + '/publisher/find-all',
 			});
 	   	};
 	   	
 	   	ret.add = function(publisher)
 	   	{
 	   	   	return $http({
 				method : 'POST',
 				url: _contextPath + '/publisher/create',
 				data:publisher
 			});
 	   	};
 	   	
 	   	ret.deleteById = function(id)
 	   	{
 	   	   	return $http({
 				method : 'GET',
 				url: _contextPath + '/publisher/delete/',
 				params: {id: id},
 			});
 	   	};
		return ret;
	}
})();