(function() {
   "use strict";
   var fc = angular.module('foodControllers', []);

   fc.controller('PageController', function ($scope, $location) {
      $scope.pageUrl = $location.path();
      $scope.location = $location;

      this.changePage = function(newUrl) {
         $scope.pageUrl = newUrl;
         window.location.hash = newUrl;
      }

      this.addPage = function () {
         this.changePage('/page-add');
      }

      this.mainPage = function () {
         this.changePage('/main');
      }

      this.getURL = function () {
         return $scope.pageUrl.slice(1);
      }

      $scope.$watch('location.path()', function () {
         $scope.pageUrl = $location.path();
      });

   });

   /**
    * AddingController
    * this is in charge of the interface for adding new
    * logs
    */
   fc.controller('AddingController', function($scope, $http) {

      $scope.log = {
         title : 'Snack',
         desc : '',
         ingredients : [''],
         symptoms : ['']
      }

      // ingredient stuff
      this.addIngredient = function () {
         $scope.log.ingredients.push('');
      };

      this.removeIngredient = function (index) {
         $scope.log.ingredients.splice(index, 1);
      };
      
      // symptoms stuff
      this.addSymptom = function () {
         $scope.log.symptoms.push('');
      };

      this.removeSymptom = function (index) {
         $scope.log.symptoms.splice(index, 1);
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
