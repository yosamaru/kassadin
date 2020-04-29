'use strict';
(function()
{
	var serviceApp = angular.module('sample.service');
	serviceApp.factory('moderatorService', ['$log', '$http', ModeratorService]);
	
	function ModeratorService($log, $http)
	{
		var ret = {};
		
		ret.findAll = function()
 	   	{
 	   	   	return $http({
 				method : 'GET',
 				url: _contextPath + '/moderator/find-all',
 			});
 	   	};
 	   	
 	   	ret.register = function(moderatorBean)
 	   	{
 	   	    console.log("[moderatorBean]|"+moderatorBean);
 	   	   	return $http({
 				method : 'POST',
 				url: _contextPath + '/moderator/register',
 				data:moderatorBean
 			});
 	   	};
 	   	
 	   	ret.loginUser = function(gusername, gpassword)
 	   	{
 	   	    		    console.log(2);

            return $http({
 				method : 'GET',
 				url: _contextPath + '/moderator/login',
 				params: {username: gusername, password: gpassword}
 			});
 	   	};

        ret.loginOut = function()
        {
            return $http({
                method : 'GET',
                url: _contextPath + '/moderator/login-out',
            });
        };

		return ret;
	}
})();