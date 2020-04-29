'use strict';
(function()
{
	var app = angular.module('std.app');
	app.controller('HeaderController', ['$scope', '$state', '$rootScope', 'checkService', HeaderController]);

	function HeaderController($scope, $state, $rootScope, checkService)
	{
	    var userType = sessionStorage.getItem('userType');

        // 普通用户
	    if(userType==0 || userType==1){
	    	 $scope.ifUser = true;
	    	 $scope.ifBook = true;
	    	 $scope.ifCheque = true;
	    	 $scope.ifTransaction = true;
	    	 $scope.ifContract = true;
	    	 if(userType == 1){
	    	     $scope.ifAuthor = true;
                 $scope.ifCategory = true;
                 $scope.ifPublisher = true;
	    	 	 $scope.ifModerator = true;
	    	 	 $scope.ifStatistics = true;
	    	 	 $scope.ifShelf = false;
	    	 }else{
	    	     $scope.ifAuthor = false;
                 $scope.ifCategory = false;
                 $scope.ifPublisher = false;
	    	 	 $scope.ifModerator = false;
	    	 	 $scope.ifShelf = true;
	    	 }
	    }

	    if(userType==null || typeof(userType) == "undefined" || isNaN(userType) || userType==3){
             $scope.ifUser = false;
             $scope.ifAuthor = false;
             $scope.ifCategory = false;
             $scope.ifPublisher = false;
             $scope.ifBook = true;
             $scope.ifShelf = false;
             $scope.ifCheque = false;
             $scope.ifTransaction = false;
             $scope.ifContract = false;
             $scope.ifModerator = false;
        }
	}
})();