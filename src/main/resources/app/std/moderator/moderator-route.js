'use strict';
(function()
{
	var stdRouteApp = angular.module('std.route.app');

	stdRouteApp.config(['$stateProvider','$urlRouterProvider', config]);

	function config($stateProvider, $scope, $urlRouterProvider)
	{
		$stateProvider.state('moderator', {
		    url: '/page/moderator',
            controller: 'moderatorController',
            views: {
         		'header': { templateUrl: _applicationPath + '/template/header.htm' },
				'footer': { templateUrl: _applicationPath + '/template/footer.htm' },
				'content': {
         		    templateUrl: _applicationPath + '/moderator/moderator-content.htm',
				}
			},
            resolve: {
                moderator: function (moderatorService) {
                    moderatorService.findAll();
                }
            }
		});

		$stateProvider.state('moderator-register', {
            url: '/page/moderator/register',
            views: {
                'header': { templateUrl: _applicationPath + '/template/header.htm' },
              	'footer': { templateUrl: _applicationPath + '/template/footer.htm' },
                'content': {
                    templateUrl: _applicationPath + '/moderator/moderator-register.htm',
                }
            }
        });

        $stateProvider.state('moderator-login', {
            url: '/page/moderator/login',
            controller: 'ModeratorController',
            views: {
                    'header': { templateUrl: _applicationPath + '/template/header.htm' },
                    'footer': { templateUrl: _applicationPath + '/template/footer.htm' },
                    'content': {
                        templateUrl: _applicationPath + '/moderator/moderator-login.htm',
                    }
            }
        });
	}
})();
