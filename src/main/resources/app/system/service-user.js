'use strict';
(function()
{
	var serviceApp = angular.module('sample.service');
	serviceApp.factory('userService', ['$log', '$http', UserService]);
	
	function UserService($log, $http)
	{
		var ret = {};
		
		ret.findAll = function()
 	   	{
 	   	   	return $http({
 				method : 'GET',
 				url: _contextPath + '/user/find-all',
 			});
 	   	};
 	   	
 	   	ret.register = function(userBean)
 	   	{
 	   	   	return $http({
 				method : 'POST',
 				url: _contextPath + '/user/register',
 				data:userBean
 			});
 	   	};
 	   	
 	   	ret.loginUser = function(gusername, gpassword)
 	   	{
            console.log("1");

            return $http({
 				method : 'GET',
 				url: _contextPath + '/user/login',
 				params: {username: gusername, password: gpassword}
 			});
 	   	};

 	   	ret.loginOut = function()
        {
            return $http({
                method : 'GET',
                url: _contextPath + '/user/login-out',
            });
        };
		return ret;
	}
})();