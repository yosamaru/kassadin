'use strict';
(function()
{
	var stdRouteApp = angular.module('std.route.app');

	stdRouteApp.config(['$stateProvider','$urlRouterProvider', config]);

	function config($stateProvider, $scope, $urlRouterProvider)
	{
		$stateProvider.state('user', {
		    url: '/page/user',
            controller: 'userController',
            views: {
         		'header': { templateUrl: _applicationPath + '/template/header.htm' },
				'footer': { templateUrl: _applicationPath + '/template/footer.htm' },
				'content': {
         		    templateUrl: _applicationPath + '/user/user-content.htm',
				}
			},
            resolve: {
                users: function (userService) {
                    userService.findAll();
                }
            }
		});

		$stateProvider.state('user-register', {
            url: '/page/user/register',
            views: {
                'header': { templateUrl: _applicationPath + '/template/header.htm' },
              	'footer': { templateUrl: _applicationPath + '/template/footer.htm' },
                'content': {
                    templateUrl: _applicationPath + '/user/user-register.htm',
                }
            }
        });

        $stateProvider.state('user-login', {
            url: '/page/user/login',
            controller: 'UserController',
            views: {
                    'header': { templateUrl: _applicationPath + '/template/header.htm' },
                    'footer': { templateUrl: _applicationPath + '/template/footer.htm' },
                    'content': {
                        templateUrl: _applicationPath + '/user/user-login.htm',
                    }
            }
        });
	}
})();
