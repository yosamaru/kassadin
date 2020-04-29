'use strict';
(function()
{
	var app = angular.module('std.app');
	app.controller('ShelfController', ['$scope', '$state', '$stateParams', '$rootScope', 'shelfService', 'checkService', ShelfController]);
	app.controller('ShelfDetailController', ['$scope', '$state', '$stateParams', '$rootScope', 'shelfService', 'checkService', ShelfDetailController]);

	function ShelfController($scope, $state, $stateParams, $rootScope, shelfService, checkService)
	{

        checkService.check().then(function(result){
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
        if(userType==null || typeof(userType) == "undefined" || isNaN(userType)){
            $state.go('home',{cache:true},{reload: true});
        }


		$scope.refresh = function()
		{
			shelfService.findAll().then(function(result)
			{
				$scope.shelfs = result.data.content;
			});
		};


		$scope.remove = function(id)
		{
			shelfService.deleteById(id).then(function(result)
			{
                $scope.refresh();
			});
		};


		$scope.reader = function(id)
        {
            $state.go("shelf-reader", {id:id});
        };


         shelfService.findAll().then(function(result)
        {
            $scope.shelfs = result.data.content;
        });
	}

	function ShelfDetailController($scope, $state, $stateParams, $rootScope, shelfService, checkService)
    {
        checkService.check().then(function(result){
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
        if(userType==null || typeof(userType) == "undefined" || isNaN(userType)){
            $state.go('home',{cache:true},{reload: true});
        }

        var id = $stateParams.id;
        shelfService.findById(id).then(function(result)
        {
            if(id == null){
             return;
            }
            $scope.book = result.data.content;
        });

		$scope.download = function(fileName)
        {
           shelfService.download(fileName);
        };

    }

})();