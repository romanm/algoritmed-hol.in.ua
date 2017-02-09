function initAll ($http, $scope){
	console.log('----initAll---------------');
	$scope.generalInfo = {};
	$scope.param = parameters;
	$scope.pagePath = window.location.href.split('?')[0].split('/').splice(4);//.reverse();
	if($scope.pagePath.last() && $scope.pagePath.last().length==0) $scope.pagePath.pop();
	$scope.prevousPath = function(){
		if($scope.pagePath.length<=1)
			return '/';
		var pp = $scope.pagePath.slice();
		pp.pop();
		var previousUrl = '/v/'+pp.toString().replace(',','/');
		return previousUrl;
	}

	$scope.prevousPath();

	$scope.menuHomeClicked = function(k){
		return k == $scope.pagePath[0];
	}
	$scope.urlHol2 = function(){
		var url = '/hol2';
		if($scope.pagePath.length){
			if('department' == $scope.pagePath.last()){
				url += '/vid'
			}else
			if('department' == $scope.pagePath.forLast()){
				url += '/v.' + $scope.pagePath.last();
			}else
				if('department' == $scope.pagePath.forForLast()){
					url += '/v.' 
						+ $scope.pagePath.forLast()
						+'/'+ $scope.pagePath.last();
				}
		}
		return url;
	}

	initDepartmentAccordion = function () {

		$scope.departmentTypeAccordion = {
			'chirurgiae':{'name':'Хірургічні відділення','expand':false, 'idx':[1,2]}
			,'therapy':{'name':'Терапевтичні відділення','expand':false, 'idx':[3,4]}
			,'obstetrical-gynecological':{'name':'Акушерсько-гінекологічні','expand':false, 'idx':[5,6]}
		}

		angular.forEach($scope.departmentTypeAccordion, function(o, k){
			o.idx = [];
		});
		angular.forEach($scope.vidsAll.departments, function(d, index){
			var o = $scope.departmentTypeAccordion[d.type];
			if(o)
				o.idx.push(index);
		});
	}

	if('personal' == $scope.pagePath.last()){
		var url = '/f/hol.in.ua/model/personalListHolWeb.json.js';
		$http.get(url).then(
			function(response) {
				$scope.personalListHolWeb = response.data;
				console.log($scope.personalListHolWeb);
			}, function(response) {
				console.error(response);
			}
		);
	}

	if('department' == $scope.pagePath.last()
	|| 'patient' == $scope.pagePath.last()
	){
		var url = '/f/hol.in.ua/model/vidsAll.json.js';
		$http.get(url).then(
			function(response) {
				$scope.vidsAll = response.data;
				initDepartmentAccordion();
			}, function(response) {
				console.error(response);
			}
		);
	}

	if('department' == $scope.pagePath.forLast()
	|| 'department' == $scope.pagePath.forForLast()
	){
		$scope.departmentName = $scope.pagePath.last();
		if('department' == $scope.pagePath.forForLast()){
			$scope.departmentName = $scope.pagePath.forLast();
			$scope.departmentPart = $scope.pagePath.last();
		}
		var url = '/f/hol.in.ua/model/department/v.' + $scope.departmentName + '.json.js';
		console.log(url);
		if(parameters.archive){
			url = '/f/hol.in.ua/model/department/archive/' + parameters.archive;
		}
		console.log(url);
		$http.get(url).then(
			function(response) {
				$scope.departmentInfo = response.data;
			}, function(response) {
				console.error(response);
			}
		);
	}

	$http.get('/f/config/hol1.algoritmed.site.config.json').then(
		function(response) {
			$scope.config = response.data;
			$scope.menuHomeIndex = [];
			angular.forEach($scope.config, function(v, i){
				if(v.parent == 'holhome'){
					$scope.menuHomeIndex.push(i);
				}
			});
		}, function(response) {
			console.error(response);
		}
	);

	$http.get('/f/hol.in.ua/model/generalInfo.json.js').then(
		function(response) {
			$scope.generalInfo = response.data;
		}, function(response) {
			console.error(response);
		}
	);

	
	$http.get('/r/principal').then(
		function(response) {
			$scope.principal = response.data;
			console.log($scope.principal);
		}, function(response) {
			console.error(response);
		}
	);
	
	$scope.thisPageName = function(){
		return $scope.pagePath.last();
	}
}

var dayInMilliseconds = (24*60*60*1000);
var parameters = {};
if(window.location.search){
//	$.each(window.location.search.split("?")[1].split("&"), function(index, value){
	angular.forEach(window.location.search.split("?")[1].split("&"), function(value, index){
		var par = value.split("=");
		parameters[par[0]] = par[1];
	});
	console.log(parameters);
}

//Used to toggle the menu on small screens when clicking on the menu button
function toggleFunction() {
	var x = document.getElementById("navDemo");
	if (x.className.indexOf("w3-show") == -1) {
		x.className += " w3-show";
	} else {
		x.className = x.className.replace(" w3-show", "");
	}
}

if (!Object.prototype.hasChild){
	Object.prototype.hasChild = function(type, parentKey){
		var keys = Object.keys(this);
		for (i = 0; i < keys.length; i++)
			if(this[keys[i]][type] == parentKey)
				return true;
		return false;
	}
}

if (!Array.prototype.last){
	Array.prototype.last = function(){
		return this[this.length - 1];
	}
	Array.prototype.forLast = function(){
		return this[this.length - 2];
	}
	Array.prototype.forForLast = function(){
		return this[this.length - 3];
	}
}
