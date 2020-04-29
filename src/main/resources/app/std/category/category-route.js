'use strict';
(function()
{
	var stdRouteApp = angular.module('std.route.app');
	
	stdRouteApp.config(['$stateProvider','$urlRouterProvider', config]);
	
	function config($stateProvider, $scope, $urlRouterProvider)
	{
		$stateProvider.state('category',
		{
			url: '/page/category',
            views:
            {
         		'header': { templateUrl: _applicationPath + '/template/header.htm' },
				'footer': { templateUrl: _applicationPath + '/template/footer.htm' },
				'content' : 
				{ 
					templateUrl: _applicationPath + '/category/category-content.htm',
				}
			},
			resolve:
			{
                categorys:function(categoryService){
					return categoryService.findAll();
				}
			}
		});

		$stateProvider.state('category-add',
        {
        	url: '/page/category/add',
            views:
            {
			    'footer': { templateUrl: _applicationPath + '/template/footer.htm' },
                'content' :
                {
                    templateUrl: _applicationPath + '/category/category-add.htm',
                }
            },
        });

	}
})();
