'use strict';
(function()
{
	var stdRouteApp = angular.module('std.route.app');
	
	stdRouteApp.config(['$stateProvider','$urlRouterProvider', config]);
	
	function config($stateProvider, $scope, $urlRouterProvider)
	{
		$stateProvider.state('publisher',
		{
			url: '/page/publisher',
            views:
            {
         		'header': { templateUrl: _applicationPath + '/template/header.htm' },
				'footer': { templateUrl: _applicationPath + '/template/footer.htm' },
				'content' : 
				{ 
					templateUrl: _applicationPath + '/publisher/publisher-content.htm',
				}
			},
			resolve:
			{
                publishers:function(publisherService){
					return publisherService.findAll();
				}
			}
		});

        $stateProvider.state('publisher-add',
        {
        	url: '/page/publisher/add',
            views:
            {
			    'footer': { templateUrl: _applicationPath + '/template/footer.htm' },
                'content' :
                {
                    templateUrl: _applicationPath + '/publisher/publisher-add.htm',
                }
            },
        });

	}
})();
