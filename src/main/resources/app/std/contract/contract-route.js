'use strict';
(function()
{
	var stdRouteApp = angular.module('std.route.app');
	
	stdRouteApp.config(['$stateProvider','$urlRouterProvider', config]);
	
	function config($stateProvider, $scope, $urlRouterProvider)
	{
		$stateProvider.state('contract',
		{
			url: '/page/contract',
            views:
            {
         		'header': { templateUrl: _applicationPath + '/template/header.htm' },
				'footer': { templateUrl: _applicationPath + '/template/footer.htm' },
				'content' : 
				{ 
					templateUrl: _applicationPath + '/contract/contract-content.htm',
				}
			},
			resolve:
			{
				contracts:function(contractService){
					return contractService.findAll();
				}
			}
		});
	}
	                    
})();
