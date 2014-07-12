(function() {
   "use strict";
   var fc = angular.module('foodControllers', []);

   fc.controller('PageController', function ($scope) {
      var url = 'main';

      this.addPage = function () {
         url = 'page-add';
         console.log(url);
      }

      this.mainPage = function () {
         url = 'main';
         console.log(url);
      }

      this.getURL = function () {
         return url;
      }

   });

   fc.controller('AddingController', function($scope, $http) {

      this.title = 'Snack';
      this.desc = '';
      this.ingredients = [''];
      this.symptoms = [''];

      this.addIngredient = function () {
         this.ingredients.push('');
      };

      this.removeIngredient = function (index) {
         this.ingredients.splice(index, 1);
      };

      this.addSymptom = function () {
         this.Symptoms.push('');
      };

   });

   /**
    * A Controller for handling user information
    */
   fc.controller('UserController', function($scope, $http) {
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
   fc.controller('HistoryController', function($scope, $http) {
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
