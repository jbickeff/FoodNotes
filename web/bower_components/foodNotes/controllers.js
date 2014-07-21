(function() {
    "use strict";
    var fc = angular.module('foodControllers', []);
    fc.controller('PageController', function($scope, $rootScope, foodAPI, $location) {
        $scope.pageUrl = $location.path();
        $scope.location = $location;
        var that = this;

        this.changePage = function(newUrl) {
            $scope.pageUrl = newUrl;
            window.location.hash = newUrl;
        };

        $rootScope.$on('saved', function() {
            that.mainPage();
        });

        this.addPage = function() {
            this.changePage('/page-add');
        };

        this.mainPage = function() {
            this.changePage('/main');
        };

        this.getURL = function() {
            return $scope.pageUrl.slice(1);
        };

        $scope.$watch('location.path()', function() {
            $scope.pageUrl = $location.path();
        });
    });
    /**
     * AddingController
     * this is in charge of the interface for adding new
     * logs
     */
    fc.controller('AddingController', function($rootScope, $scope, foodAPI) {

        this.saving = false;

        $scope.log = {
            title: 'Snack',
            desc: '',
            ingredients: [
                {name: ''}
            ],
            suj: [
                'cheetos white',
                'hot cheetos',
                'hotdogs'
            ],
            symptoms: [
                {name: ''}
            ],
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
        };

        // symptoms stuff
        this.addSymptom = function() {
            $scope.log.symptoms.push({name: ''});
        };
        this.removeSymptom = function(index) {
            $scope.log.symptoms.splice(index, 1);
        };

        this.save = function() {
            this.saving = true;
            var that = this;
            var promise = foodAPI.newEntry($scope.log);
            promise.success(function() {
                that.saving = false;
                $rootScope.$emit('saved', $scope.log);
            });
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


    /*
     * A controller that controlls the history tab
     */
    fc.controller('HistoryController', function($scope, $rootScope, foodAPI, history) {
        $scope.days = [];
        $scope.hist = [];
        $scope.offset = 0;

        this.update = function() {
            var promise = foodAPI.getHistory();

            promise.success(function(data, status, headers, config) {
                $scope.hist = data.history.reverse();

            });
        };

        this.update();

        var that = this;

        $rootScope.$on('saved', function() {
            that.update();
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
            return $scope.hist;
        };
    });


    fc.factory('foodAPI', function($http, error) {
        return {
            getHistory: function() {
                //return $http.get("/bower_components/foodNotes/hist.json");
                var promise = $http.get("api/getHistory");

                promise.error(function(data) {
                    error.history = true;
                    console.log(data);
                    if (data.relogin === true) {
                        window.location.replace("/#relogin");
                    }
                });
                return promise;
            },
            getUsername: function() {
                //return $http.get("/bower_components/foodNotes/name.json");
                return $http.get("api/getUsername").error(function() {
                    error.username = true;
                    window.location.replace("/#relogin");
                });
            },
            newEntry: function(log) {
                return $http.post('api/newEntry', log).error(function(data) {
                    error.newEntry = true;
                    console.log(data);
                });
            },
            updateEntry: function(log) {
                return $http.post('api/updateEntry', log).error(function() {
                    error.newEntry = true;
                });
            }
        };
    });

    fc.factory('history', function() {
        return  {
            entries: null,
            convertAPI: function(json) {
                console.log(json);
            }
        };
    });

    fc.factory('error', function() {
        return {
            history: false,
            username: false,
            newEntry: false,
            updatedEntry: false
        };
    });

    fc.controller('ErrorController', function($scope, error) {
        $scope.error = error;
        console.log(error);
    });
})();
