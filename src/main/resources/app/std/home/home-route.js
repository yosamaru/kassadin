'use strict';
(function()
{
	var stdRouteApp = angular.module('std.route.app');
	
	stdRouteApp.config(['$stateProvider','$urlRouterProvider', config]);
	
	function config($stateProvider, $urlRouterProvider)
	{
//		$urlRouterProvider.otherwise('/home');
		$stateProvider.state('home',
		{
			url: '/home',
			views:
			{
				'header': { templateUrl: _applicationPath + '/template/header.htm' },
				'footer': { templateUrl: _applicationPath + '/template/footer.htm' },
				'content' : 
				{ 
					templateUrl: _applicationPath + '/home/home-content.htm',
				}
			},
			reload:true,
		});
	}
	                    

	
})();
