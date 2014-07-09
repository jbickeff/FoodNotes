(function() {
   "use strict";
   var app = angular.module('foodNotes', []);



   /**
    * A Controller for handling user information
    */
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



   /**
    * A controller that controlls the history tab
    */
   app.controller('HistoryController', function($scope, $http) {
      $scope.days = [];
      $scope.hist = [];
      $scope.offset = 0;

      var promise = $http.get("/bower_components/foodNotes/hist.json");

      promise.success(function (data, status, headers, config) {
         $scope.hist = data;
      });

      promise.error(function (data, status, headers, config) {
         console.error(data, status, headers, config);
      });


      this.newest = function() {
         $scope.offset = 0;
      };


      this.next = function() {
         if($scope.offset < $scope.hist.length - 3) {
            $scope.offset += 3;
         }
      };

      this.prev = function() {
         $scope.offset -= 3;

         if ($scope.offset < 0) {
            $scope.offset = 0;
         }
      };

      this.getHist = function() {
         var max = $scope.hist.length - $scope.offset - 3 >= 3 ? 3 : $scope.hist.length - $scope.offset;
         return $scope.hist.slice($scope.offset, $scope.offset + max);
      };


   });

   

})();
