'use strict';
(function()
{
	var serviceApp = angular.module('sample.service');
	serviceApp.factory('shelfService', ['$log', '$http', ShelfService]);
	
	function ShelfService($log, $http)
	{
		var ret = {};

		ret.findAll = function()
 	   	{
 	   	   	return $http({
 				method : 'GET',
 				url: _contextPath + '/shelf/find-all',
 			});
 	   	};

 	   	ret.deleteById = function(id)
 	   	{
 	   	   	return $http({
 				method : 'GET',
 				url: _contextPath + '/shelf/delete/',
 				params: {id: id},
 			});
 	   	};

 	   	ret.findById = function(id)
        {
            return $http({
                method : 'GET',
                url: _contextPath + '/shelf/read-book/',
                params: {id: id},
            });
        };

        ret.download = function(fileName)
        {
            window.open(_contextPath + '/shelf/download-file/?fileName=' + fileName);
//            return $http({
//                method : 'GET',
//                url: _contextPath + '/shelf/downloadFile/',
//                params: {fileName: fileName},
//                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
//            });
        };

		return ret;
	}
})();