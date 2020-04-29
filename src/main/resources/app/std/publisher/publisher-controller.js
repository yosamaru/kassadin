'use strict';
(function()
{
	var app = angular.module('std.app');
	app.controller('PublisherController', ['$scope', '$state', '$rootScope', 'publisherService', 'checkService', PublisherController]);

	function PublisherController($scope, $state, $rootScope, publisherService, checkService)
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
            publisherService.findAll().then(function(result) {
				$scope.publishers = result.data.content;
			});
		};

		$scope.add = function()
		{
            publisherService.add().then(function(result) {
				$scope.refresh();
			});
		};

		$scope.remove = function(id)
		{
            publisherService.deleteById(id).then(function(result) {
                $scope.refresh();
			});
		};

		$scope.submitForm = function () {
            publisherService.add($scope.publisherBean).then(function(result) {
            $state.go('publisher');
          });
        };

        publisherService.findAll().then(function(result)
        {
            $scope.publishers = result.data.content;
        });
	}

})();