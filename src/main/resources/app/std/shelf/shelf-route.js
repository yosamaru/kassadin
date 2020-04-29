'use strict';
(function()
{
	var stdRouteApp = angular.module('std.route.app');
	
	stdRouteApp.config(['$stateProvider','$urlRouterProvider', config]);
	
	function config($stateProvider, $scope, $urlRouterProvider)
	{
		$stateProvider.state('shelf',
		{
			url: '/page/shelf',
            views:
            {
         		'header': { templateUrl: _applicationPath + '/template/header.htm' },
				'footer': { templateUrl: _applicationPath + '/template/footer.htm' },
				'content' : 
				{ 
					templateUrl: _applicationPath + '/shelf/shelf-content.htm',
				}
			},
			resolve:
			{
				shelfs:function(shelfService){
					return shelfService.findAll();
				}
			}
		});

		$stateProvider.state('shelf-reader',
        {
            url: '/page/shelf-reader/:id',
            controller: 'ShelfDetailController',
            views:
            {
                'header': { templateUrl: _applicationPath + '/template/header.htm' },
                'footer': { templateUrl: _applicationPath + '/template/footer.htm' },
                'content' :
                {
                    templateUrl: _applicationPath + '/shelf/shelf-reader.htm',
                }
            }
        });

	}
})();
