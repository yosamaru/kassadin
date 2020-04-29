'use strict';
(function()
{
	var app = angular.module('std.app');
	app.controller('ChequeController', ['$scope', '$state', '$rootScope', 'chequeService', 'checkService', ChequeController]);

	function ChequeController($scope, $state, $rootScope, chequeService, checkService)
	{
	    checkService.check().then(function(result)
	    {
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
			chequeService.findAll().then(function(result)
			{
			    console.log(result.data.content);
				$scope.cheques = result.data.content;
			});
		};

		$scope.add = function()
		{
			chequeService.add().then(function(result)
			{
				$scope.refresh();
			});
		};

		$scope.remove = function(id)
		{
			chequeService.deleteById(id).then(function(result)
			{
                $scope.refresh();
			});
		};

		$scope.submitForm = function () {
            console.log($scope.chequeBean);
            chequeService.add($scope.chequeBean).then(function(result)
            {
                if(result.data.errCode != 0000) {
                     alert(result.data.errMessage);
                     $scope.refresh();
                }else{
                     alert(result.data.errMessage);
                     $state.go("cheque");
                }
            });
        }

        chequeService.findAll().then(function(result)
        {
            console.log(result.data.content);
            $scope.cheques = result.data.content;
        });

	}


})();