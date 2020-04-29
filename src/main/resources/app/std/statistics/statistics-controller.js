'use strict';
(function()
{
	var app = angular.module('std.app');
	app.controller('StatisticsController', ['$scope', '$state', '$rootScope',  'checkService', StatisticsController]);

	function StatisticsController ($scope, $state, $rootScope, checkService)
	{

        var userType = sessionStorage.getItem('userType');
        if(userType==null || typeof(userType) == "undefined" || isNaN(userType)){
            $state.go('home',{cache:true},{reload: true});
        }

        checkService.check().then(function(result){
            if(result.data.errCode != 0000) {
               alert(result.data.errMessage);
               sessionStorage.setItem('userType', null);
               $state.go('home', {cache:true},{reload: true});
            }else if(result.data.content==0) {
                sessionStorage.setItem('userType', 0);
            }else {
                sessionStorage.setItem('userType', 1);
            }
        });

        checkService.statistics().then(function(result)
        {
            console.log(result.data.content);
            $scope.statistics = result.data.content;
        });
	}


})();