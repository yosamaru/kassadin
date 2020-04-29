'use strict';
(function()
{
	var app = angular.module('std.app');
	app.controller('TransactionController', ['$scope', '$state', 'transactionService', 'checkService', TransactionController]);

	function TransactionController($scope, $state, transactionService, checkService)
	{
        checkService.check().then(function(result){
            if(result.data.errCode != 0000) {
               alert(result.data.errMessage);
               sessionStorage.setItem('userType', null);
               $state.go('home', {cache:true},{reload: true});
            }else if(result.data.content==0) {
                sessionStorage.setItem('userType', 0);
            }else {
                sessionStorage.setItem('userType', 1);
            }
        });

		$scope.refresh = function()
		{
			transactionService.findAll().then(function(result)
			{
			    console.log(result.data.content);
				$scope.transactions = result.data.content;
			});
		};


         transactionService.findAll().then(function(result)
        {
            console.log(result.data.content);
            $scope.transactions = result.data.content;
        });

	}


})();