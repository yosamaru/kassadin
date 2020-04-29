'use strict';
(function()
{
	var stdRouteApp = angular.module('std.route.app');
	
	stdRouteApp.config(['$stateProvider','$urlRouterProvider', config]);
	
	function config($stateProvider, $scope, $urlRouterProvider)
	{
		$stateProvider.state('author',
		{
			url: '/page/author',
            views:
            {
         		'header': { templateUrl: _applicationPath + '/template/header.htm' },
				'footer': { templateUrl: _applicationPath + '/template/footer.htm' },
				'content' : 
				{ 
					templateUrl: _applicationPath + '/author/author-content.htm',
				}
			},
			resolve:
			{
				authors:function(authorService){
					return authorService.findAll();
				}
			}
		});

		$stateProvider.state('author-add',
        {
        	url: '/page/author/add',
            views:
            {
			    'footer': { templateUrl: _applicationPath + '/template/footer.htm' },
                'content' :
                {
                    templateUrl: _applicationPath + '/author/author-add.htm',
                }
            },
        });

	}
	                    

	
})();
