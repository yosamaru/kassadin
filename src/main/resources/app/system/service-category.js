'use strict';
(function()
{
	var serviceApp = angular.module('sample.service');
	serviceApp.factory('categoryService', ['$log', '$http', CategoryService]);
	
	function CategoryService($log, $http)
	{
		var ret = {};
		
		ret.findAll = function()
 	   	{
 	   	   	return $http({
 				method : 'GET',
 				url: _contextPath + '/category/find-all',
 			});
 	   	};
 	   	
 	   	ret.add = function(category)
 	   	{
 	   	   	return $http({
 				method : 'POST',
 				url: _contextPath + '/category/create',
 				data:category
 			});
 	   	};
 	   	
 	   	ret.deleteById = function(id)
 	   	{
 	   	   	return $http({
 				method : 'GET',
 				url: _contextPath + '/category/delete/',
 				params: {id: id},
 			});
 	   	};
		return ret;
	}
})();