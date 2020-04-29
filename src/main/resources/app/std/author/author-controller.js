'use strict';
(function()
{
	var app = angular.module('std.app');
	app.controller('AuthorController', ['$scope', '$state', 'authorService', 'checkService', AuthorController]);

	function AuthorController($scope, $state, authorService, checkService)
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
        if(userType==null || typeof(userType) == "undefined" || isNaN(userType)){
            $state.go('home', {cache:true},{reload: true});
        }

		$scope.refresh = function()
		{
			authorService.findAll().then(function(result)
			{
				$scope.authors = result.data.content;
			});
		};

		$scope.add = function()
		{
			authorService.add().then(function(result)
			{
				$scope.refresh();
			});
		};

		$scope.remove = function(id)
		{
			authorService.deleteById(id).then(function(result)
			{
                $scope.refresh();
			});
		};

		$scope.submitForm = function ()
		{
          authorService.add($scope.authorBean).then(function(result)
          {
            $state.go('author');
          });
        }

         authorService.findAll().then(function(result)
        {
            $scope.authors = result.data.content;
            console.log('authors:'+result.data.content);
        });

	}


})();