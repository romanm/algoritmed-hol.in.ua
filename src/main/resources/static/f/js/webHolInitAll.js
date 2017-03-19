function initAll ($http, $scope){
	console.log('----initAll---------------');
	$scope.generalInfo = {};
	$scope.param = parameters;

	$scope.editArticle = function(editMode){
		console.log(editMode);
		if($scope.editMode != editMode)
			$scope.editMode = editMode;
		else
			$scope.editMode = '-';
	}

	$scope.pagePath = window.location.href.split('?')[0].split('/').splice(4);//.reverse();
	if($scope.pagePath.last() && $scope.pagePath.last().length==0) $scope.pagePath.pop();

	$scope.prevousPath = function(){
		if($scope.pagePath.length==1){
			if($scope.config){
				return '/v/' + $scope.config[$scope.pagePath[0]].parent;
			}
		}else
		if($scope.pagePath.length<1){
			return '/';
		}
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
			,'other':{'name':'Інші відділення і підрозділи','expand':false, 'idx':[7,8]}
		}

		angular.forEach($scope.departmentTypeAccordion, function(o, k){
			o.idx = [];
		});
		$scope.vidsAllDepIdx = {};
		angular.forEach($scope.vidsAll.departments, function(d, index){
			var o = $scope.departmentTypeAccordion[d.type];
			console.log(index);
			$scope.vidsAllDepIdx[d.vUrl] = index;
			if(o)
				o.idx.push(index);
		});
	}

	if(
			'pharmarest' == $scope.pagePath.last()
||			'phprint' == $scope.pagePath.last()
			){
		$scope.fileToUpload = {};

		$scope.uploadFile = function(){
			console.log('--------uploadFile-------');
			console.log($scope.fileToUpload);
		}

		var needConstant = 20;
		$scope.medicamentNeed = function(p){
			var rest = p.medicament_rest;
			if(rest==0)
				rest = 1;
			var nc = needConstant;
//			if(rest>100)
//				nc /= 10;
			var n = nc * rest;
			/*
			 * Напрямок закритий. Angular має проблему з random()
			n = window.Math.floor((window.Math.random() * n) + 1); 
			n = n+'-'
			console.log(window.Math.random());
			console.log(nc + '*' + rest + '*' + Math.random() + '=' +n);
			n=(n+'-').subString(0,2);
			n=n.toFixed(0);
			while (n>200) {
				n/=2;
			}
			n = n.toFixed(0);
			return 2.3;
			n = window.Math.floor((window.Math.random() * 6) + 1); 
			 * */
			return n;
		}

		$scope.readMedicamentRest = function(data){
			var url = '/r/readMedicamentRest/' + data;
			console.log(url);
			$http.get(url).then(
				function(response) {
					$scope.pharmarest.db1Test1Medicamenten = response.data.db1Test1Medicamenten;
					console.log($scope.pharmarest);
				}, function(response) {
					console.error(response);
				}
			);
		}

		var url = '/r/testDb1Medicamenten';
		$http.get(url).then(
			function(response) {
				$scope.pharmarest = response.data;
				console.log($scope.pharmarest);
				var lastDate = $scope.pharmarest.allDateDb1MedicamentenList[0].medicament_rest_date
				$scope.readMedicamentRest(lastDate);
			}, function(response) {
				console.error(response);
			}
		);

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

	console.log($scope.pagePath)
	if('department' == $scope.pagePath.last()
	|| 'department' == $scope.pagePath.forLast()
	|| 'patient' == $scope.pagePath.last()
	){
		var url = '/f/hol.in.ua/model/vidsAll2.json.js';
		//var url = '/f/hol.in.ua/model/vidsAll.json.js';
		console.log(url)
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
		var urlActually = '/f/hol.in.ua/model/department/actually/' + $scope.departmentName + '.html';
		console.log(urlActually);
		$http.get(urlActually).then(
				function(response) {
					console.log(response.status == 200);
					if(response.status == 200){
						$scope.departmentActually = true;
					}
				}, function(response) {
					console.error(response);
				}
		);
		var readDepartmentData = function(url){
			$http.get(url).then(
				function(response) {
					$scope.departmentInfo = response.data;
//				console.log($scope.departmentInfo);
				}, function(response) {
					console.error(response);
				}
			);
		}
		var url = '/f/hol.in.ua/model/department/v.' + $scope.departmentName + '.json.js';
		console.log(url);
		if(parameters.archive){
			url = '/f/hol.in.ua/model/department/archive/' + parameters.archive;
			console.log(url);
			console.log(parameters.archive.indexOf('html'));
			if(parameters.archive.indexOf('html')>0){
				readDepartmentData(url);
			}else{
				readDepartmentData(url);
			}
		}else{
			readDepartmentData(url);
		}
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

//	$http.get('/f/hol.in.ua/model/generalInfo.json.js').then(
	$http.get('/f/hol.in.ua/model/generalInfo2.json.js').then(
		function(response) {
			$scope.generalInfo = response.data;
			console.log($scope.generalInfo);
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
