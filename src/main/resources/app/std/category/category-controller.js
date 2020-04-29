'use strict';
(function()
{
	var app = angular.module('std.app');
	app.controller('CategoryController', ['$scope', '$state', '$rootScope', 'categoryService', 'checkService', CategoryController]);

	function CategoryController($scope, $state, $rootScope, categoryService, checkService)
	{

	    var userType = sessionStorage.getItem('userType');
        if(userType==null || typeof(userType) == "undefined" || isNaN(userType))
        {
            $state.go('home',{cache:true},{reload: true});
        }

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

		$scope.refresh = function()
		{
            categoryService.findAll().then(function(result) {
				$scope.categorys = result.data.content;
			});
		};

		$scope.add = function()
		{
            categoryService.add().then(function(result) {
				$scope.refresh();
			});
		};

		$scope.remove = function(id)
		{
            categoryService.deleteById(id).then(function(result) {
                $scope.refresh();
			});
		};

		$scope.submitForm = function () {
            categoryService.add($scope.categoryBean).then(function(result)
            {
                $state.go('category');
            });
        };

        categoryService.findAll().then(function(result)
        {
            $scope.categorys = result.data.content;
        });
	}

})();