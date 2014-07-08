(function() {
   var app = angular.module('foodNotes', []);



   app.controller('UserController', function($scope, $http) {
      var promise = $http.get("/bower_components/foodNotes/name.json");

      $scope.name = "...";

      promise.success(function (data, status, headers, config) {
         $scope.name = data.name;
      });

      promise.error(function (data, status, headers, config) {
         console.error(data, status, headers, config);
      });

   });



   app.controller('HistoryController', function($scope, $http) {
      $scope.days = [];

      var promise = $http.get("/bower_components/foodNotes/hist.json");

      $scope.hist = [];

      promise.success(function (data, status, headers, config) {
         $scope.hist = data;
      });

      promise.error(function (data, status, headers, config) {
         console.error(data, status, headers, config);
      });

   });

   

})();
