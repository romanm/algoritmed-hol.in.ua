(function(angular) {
	'use strict';

	var holWebApp = angular.module('holWebApp', ['ngSanitize']);

	holWebApp.controller('AccordionCtrl', function ($scope) {
		this.expandItem = function (o){
			o.expand = !o.expand;
		}
	});

	holWebApp.controller('ProbeCtrl', function ($scope) {
		var i = 0;
		this.title = 'Some title ' + i + $scope.myHTML;
	});

	holWebApp.controller('HolWebCtrl', function($scope, $http) {
		this.title = 'ХОЛ в інтернеті';
		console.log('---HolWebApp-----HolWebCtrl--------');
		initAll($http, $scope);
		$scope.myHTML =
			'I am an <code>HTML</code>string with ' +
			'<a href="#">links!</a> and other <em>stuff</em>';
		$scope.tmpList = [];
		for (var i = 0; i < 99; i++) {
			$scope.tmpList.push(i);
		}
	});

})(window.angular);