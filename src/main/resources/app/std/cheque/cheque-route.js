'use strict';
(function()
{
	var stdRouteApp = angular.module('std.route.app');
	
	stdRouteApp.config(['$stateProvider','$urlRouterProvider', config]);
	
	function config($stateProvider, $scope, $urlRouterProvider)
	{
		$stateProvider.state('cheque',
		{
			url: '/page/cheque',
            views:
            {
         		'header': { templateUrl: _applicationPath + '/template/header.htm' },
				'footer': { templateUrl: _applicationPath + '/template/footer.htm' },
				'content' : 
				{ 
					templateUrl: _applicationPath + '/cheque/cheque-content.htm',
				}
			},
			resolve:
			{
				cheques:function(chequeService){
					return chequeService.findAll();
				}
			}
		});

		$stateProvider.state('cheque-add',
        {
        	url: '/page/cheque/add',
            views:
            {
			    'footer': { templateUrl: _applicationPath + '/template/footer.htm' },
                'content' :
                {
                    templateUrl: _applicationPath + '/cheque/cheque-add.htm',
                }
            },
        });

	}
	                    

	
})();
