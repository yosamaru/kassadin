'use strict';
(function() {
	var app = angular.module('std.app');
	app.controller('UserController', ['$scope', '$state', '$rootScope', 'userService', UserController]);

	function UserController($scope, $state, $rootScope, userService)
	{
		$scope.refresh = function() {
			userService.findAll().then(function(result) {
				$scope.users = result.data.content;
            });
		};

		$scope.register = function() {
			userService.register($scope.userBean).then(function(result) {
                $state.go("user-login");
			});
		};

		$scope.loginUser = function () {
			userService.loginUser($scope.gusername, $scope.gpassword).then(function(result) {
			    if(result.data.errCode != 0000) {
			        alert(result.data.errMessage);
			        $state.go("user-login");
			    }else{
			        sessionStorage.setItem('userType', 0);
			        $state.go("home");
			    }
			});
        };


        $scope.loginOut = function () {
            console.log('Generic User loginOut');
            userService.loginOut().then(function(result) {
			    sessionStorage.setItem('userType', null);
                $state.go("home",{cache:true},{reload: true});
            });
        };

        $scope.goRegister = function () {
            $state.go("user-register");
        }

        userService.findAll().then(function(result)
        {
            console.log(result.data.content);
            $scope.users = result.data.content;
        });
	}
})();