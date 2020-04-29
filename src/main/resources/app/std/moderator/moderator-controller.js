'use strict';
(function() {
	var app = angular.module('std.app');
	app.controller('ModeratorController', ['$scope', '$state', '$rootScope', 'moderatorService', ModeratorController]);

	function ModeratorController($scope, $state, $rootScope, moderatorService)
	{
		$scope.refresh = function() {
			moderatorService.findAll().then(function(result)
			{
				$scope.moderators = result.data.content;
            });
		};

		$scope.register = function() {
			moderatorService.register($scope.moderatorBean).then(function(result)
			{
                $state.go("moderator-login");
			});
		};

		$scope.loginUser = function ()
		{
		    console.log(1);
			moderatorService.loginUser($scope.gusername, $scope.gpassword).then(function(result)
			{
			    if(result.data.errCode != 0000) {
               	   alert(result.data.errMessage);
			       $state.go("moderator-login");
			    }else{
			       sessionStorage.setItem('userType', 1);
			       alert(result.data.errMessage);
				   $state.go("moderator");
			    }
			});
        };

        $scope.loginOut = function () {
            console.log('Moderator User loginOut');
            moderatorService.loginOut().then(function(result)
            {
			    sessionStorage.setItem('userType', null);
                $state.go("home", {cache:true},{reload: true});
            });
        };

        $scope.goRegister = function ()
        {
            $state.go("moderator-register");
        }

        moderatorService.findAll().then(function(result)
        {
            console.log(result.data.content);
            $scope.moderators = result.data.content;
        });
	}
})();