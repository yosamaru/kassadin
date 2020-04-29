'use strict';
(function()
{
	var serviceApp = angular.module('sample.service');
	serviceApp.factory('authorService', ['$log', '$http', AuthorService]);
	
	function AuthorService($log, $http)
	{
		var ret = {};
		
		ret.findAll = function()
 	   	{
 	   	   	return $http({
 				method : 'GET',
 				url: _contextPath + '/author/find-all',
 			});
 	   	};
 	   	
 	   	ret.add = function(author)
 	   	{
 	   	   	return $http({
 				method : 'POST',
 				url: _contextPath + '/author/create',
 				data:author
 			});
 	   	};
 	   	
 	   	ret.deleteById = function(id)
 	   	{
 	   	   	return $http({
 				method : 'GET',
 				url: _contextPath + '/author/delete/',
 				params: {id: id},
 			});
 	   	};
		return ret;
	}
})();