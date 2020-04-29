'use strict';
(function()
{
	var app = angular.module('std.app');
	app.controller('ContractController', ['$scope', '$state', '$rootScope', 'contractService', 'checkService', ContractController]);

	function ContractController($scope, $state, $rootScope, contractService, checkService)
	{
	    checkService.check().then(function(result)
	    {
            if(result.data.errCode != 0000) {
               alert(result.data.errMessage);
               $state.go('home', {cache:true},{reload: true});
            }else if(result.data.content==0) {
                sessionStorage.setItem('userType', 0);
            }else {
                sessionStorage.setItem('userType', 1);
            }
        });

	    var userType = sessionStorage.getItem('userType');
        if(userType==null || typeof(userType) == "undefined" || isNaN(userType))
        {
            $state.go('home',{cache:true},{reload: true});
        }
        if(userType==1) {
            $scope.auditShow=1;
        }

		$scope.refresh = function()
		{
			contractService.findAll().then(function(result)
			{
				$scope.contracts = result.data.content;
			});
		};

		$scope.add = function()
		{
			contractService.add().then(function(result)
			{
			   alert(result.data.errMessage);
               $scope.refresh();
			});
		};

		$scope.audit = function(id)
		{
			contractService.audit(id).then(function(result)
			{
               alert(result.data.errMessage);
               $scope.refresh();
			});
		};

        contractService.findAll().then(function(result)
        {
            console.log(result.data.content);
            $scope.contracts = result.data.content;
        });
	}

})();