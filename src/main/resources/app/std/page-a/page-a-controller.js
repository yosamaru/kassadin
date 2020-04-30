'use strict';
(function()
{
	var app = angular.module('std.app');
	app.controller('PageAController', ['$scope', 'testService', PageAController]);
	
	function PageAController($scope, testService)
	{
		$scope.refresh = function()
		{
			testService.findTestAll().then(function(result)
			{
				$scope.data = result.data;
			});
		};
		
		$scope.add = function()
		{
			testService.addTest().then(function(result)
			{
				$scope.refresh();
			});
		};
		
		$scope.remove = function(testId)
		{
			testService.deleteTestByTestId(testId).then(function(result)
			{
				$scope.refresh();
			});
		};
		
		$scope.refresh();
	}
	
	                                      
})();
