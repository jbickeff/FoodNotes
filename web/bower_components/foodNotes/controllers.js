(function() {
    "use strict";
    var fc = angular.module('foodControllers', []);
    fc.controller('PageController', function($scope, foodAPI, $location) {
        $scope.pageUrl = $location.path();
        $scope.location = $location;
        this.changePage = function(newUrl) {
            $scope.pageUrl = newUrl;
            window.location.hash = newUrl;
        }

        this.addPage = function() {
            this.changePage('/page-add');
        }

        this.mainPage = function() {
            this.changePage('/main');
        }

        this.getURL = function() {
            return $scope.pageUrl.slice(1);
        }

        $scope.$watch('location.path()', function() {
            $scope.pageUrl = $location.path();
        });
    });
    /**
     * AddingController
     * this is in charge of the interface for adding new
     * logs
     */
    fc.controller('AddingController', function($scope, foodAPI) {

        $scope.log = {
            title: 'Snack',
            desc: '',
            ingredients: [{name: ''}],
            suj: [
                'cheetos white',
                'hot cheetos',
                'hotdogs'
            ],
            symptoms: [{name: 'asdf'}],
            symSuj: [
                'headache',
                'stomach ache',
                'hives',
                'sneezeing',
                'swelling',
                'hyperactivity'
            ]
        }

        // ingredient stuff
        this.addIngredient = function() {
            $scope.log.ingredients.push({name: ''});
        };
        this.removeIngredient = function(index) {
            $scope.log.ingredients.splice(index, 1);
        };
        this.selectIng = function(index, suj) {
            $scope.log.ingredients[index].name = suj;
            console.log('hello');
        }

        // symptoms stuff
        this.addSymptom = function() {
            $scope.log.symptoms.push({name: ''});
        };
        this.removeSymptom = function(index) {
            $scope.log.symptoms.splice(index, 1);
        };
    });
    /**
     * A Controller for handling user information
     */
    fc.controller('UserController', function($scope, foodAPI) {
        var promise = foodAPI.getUsername();

        $scope.name = "...";
        promise.success(function(data, status, headers, config) {
            $scope.name = data.name;
        });
        promise.error(function(data, status, headers, config) {
            console.error(data, status, headers, config);
        });
    });
    /**
     * A controller that controlls the history tab
     */
    fc.controller('HistoryController', function($scope, $http, foodAPI) {
        $scope.days = [];
        $scope.hist = [];
        $scope.offset = 0;
        var promise = foodAPI.getHistory();

        promise.success(function(data, status, headers, config) {
            $scope.hist = data;
        });
        promise.error(function(data, status, headers, config) {
            console.error(data, status, headers, config);
        });
        this.newest = function() {
            $scope.offset = 0;
        };
        this.next = function() {
            if ($scope.offset < $scope.hist.length - 3) {
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
    
    
    fc.factory('foodAPI', function($http) {
        return {
            getHistory: function() {
                //return $http.get("/bower_components/foodNotes/hist.json");
                return $http.get("/api/getHistory");
            },
            getUsername: function() {
                //return $http.get("/bower_components/foodNotes/name.json");
                return $http.get("/api/getUsername");
            },
            newEntry: function(log) {
                return $http.post('/api/newEntry', log);
            },
            updateEntry: function(log) {
                return $http.post('/api/updateEntry', log);
            }
        };
    });
})();
