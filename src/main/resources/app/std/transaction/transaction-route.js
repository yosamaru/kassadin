'use strict';
(function()
{
	var stdRouteApp = angular.module('std.route.app');
	
	stdRouteApp.config(['$stateProvider','$urlRouterProvider', config]);
	
	function config($stateProvider, $scope, $urlRouterProvider)
	{
		$stateProvider.state('transaction',
		{
			url: '/page/transaction',
            views:
            {
         		'header': { templateUrl: _applicationPath + '/template/header.htm' },
				'footer': { templateUrl: _applicationPath + '/template/footer.htm' },
				'content' : 
				{ 
					templateUrl: _applicationPath + '/transaction/transaction-content.htm',
				}
			},
			resolve:
			{
				transactions:function(transactionService){
					return transactionService.findAll();
				}
			}
		});
	}
	                    

	
})();
