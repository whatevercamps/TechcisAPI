(function (ng) {
    var app = angular.module('app', [
        'ui.router'
        ]);

    app.config(function($stateProvider, $urlRouterProvider) {

        $urlRouterProvider.otherwise('/home');

        $stateProvider.state('verClientes', {
            url: '/clientes/list',
            params: {
                filtro: null,
                parametro: null
            },
            views: {
                'listaView': {
                    templateUrl: 'src/clientes.html',
                    controller: 'clientesCtrl',
                    controllerAs: 'ctrl'
                }
            }
        });

        $stateProvider.state('crearNodo', {
            url: '/nodos/crearNodo',
            params: {
                filtro: null,
                parametro: null
            },
            views: {
                'mainView': {
                    templateUrl: 'src/crearNodo.html',
                    controller: 'crearNodoCtrl',
                    controllerAs: 'ctrl'
                }
            }
        });

        $stateProvider.state('crearCliente', {
            url: '/nodos/crearCliente',
            params: {
                filtro: null,
                parametro: null
            },
            views: {
                'mainView': {
                    templateUrl: 'src/crearCliente.html',
                    controller: 'crearClienteCtrl',
                    controllerAs: 'ctrl'
                }
            }
        });

        $stateProvider.state('modificarCliente', {
            url: '/clientes/modificarCliente/{idCliente:int}',
            param: {
                idCliente: null
            },
            views: {
                'mainView': {
                    templateUrl: 'src/modificarCliente.html',
                    controller: 'modificarClienteCtrl',
                    controllerAs: 'ctrl'
                }
            }
        });   

        $stateProvider.state('borrarCliente', {
            url: '/clientes/borrarCliente/{idCliente:int}',
            param: {
                idCliente: null
            },
            views: {
                'mainView': {
                    templateUrl: 'src/borrarCliente.html',
                    controller: 'borrarClienteCtrl',
                    controllerAs: 'ctrl'
                }
            }
        });         

        $stateProvider.state('verNodos', {
            url: '/nodos/list',
            params: {
                filtro: null,
                parametro: null
            },
            views: {
                'listaView': {
                    templateUrl: 'src/nodos.html',
                    controller: 'nodosCtrl',
                    controllerAs: 'ctrl'
                }
            }
        });



    });

    app.controller('clientesCtrl', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {

        var ruta = "http://186.155.119.177:8080/RedetekAPIRest/rest/clientes";
        $http.get(ruta).then(function (response) {
            $scope.clientes = response.data;
        }, function(response){
            console.log(response.data);
            $state.go('verNodos', {}, {reload: true});
            document.getElementById("errorText").innerHTML = response.data.ERROR;
            $('#errorModal').modal("show");
        });
    }]);




    app.controller('nodosCtrl', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {

        var ruta = "http://186.155.119.177:8080/RedetekAPIRest/rest/nodos";
        $http.get(ruta).then(function (response) {
            $scope.nodos = response.data;
        }, function(response){
            console.log(response.data);
            $state.go('verNodos', {}, {reload: true});
            document.getElementById("errorText").innerHTML = response.data.ERROR;
            $('#errorModal').modal("show");
        });
    }]);


    app.controller('crearNodoCtrl', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {


     $scope.data = {
        "id": 1,
        "clientes": []
    };

    var config = {
        headers : {
            'Content-Type': 'application/json'
        }
    };



    $scope.crearNodo = function () {
        $http.post('http://186.155.119.177:8080/RedetekAPIRest/rest/nodos', $scope.data, config).then(function (response) {
            $state.go('verNodos', {}, {reload: true});
        }, function(response){
            console.log(response.data);
            $state.go('verNodos', {}, {reload: true});
            document.getElementById("errorText").innerHTML = response.data.ERROR;
            $('#errorModal').modal("show");
        });
    };
}]);



    app.controller('crearClienteCtrl', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {

        var ruta = "http://186.155.119.177:8080/RedetekAPIRest/rest/nodos";
        $http.get(ruta).then(function (response) {
            $scope.nodos = response.data;
        });      

        ruta = "http://186.155.119.177:8080/RedetekAPIRest/rest/planes";
        $http.get(ruta).then(function (response) {
            $scope.planes = response.data;
        });

        $scope.nodoX = {};

        $scope.data = {
            "octeto4": 1,
            "dispositivos": [],
            "octeto1": 1,
            "octeto2": 1,
            "octeto3": 1,
            "plan": {
                "nombre": "duo",
                "descripcion": "tv e internet",
                "velocidadInternet": 5.5,
                "cantidadCanales": 120
            }    
        };

        var config = {
            headers : {
                'Content-Type': 'application/json'
            }
        };





        $scope.crearCliente = function () {
            console.log($scope.data);
            $http.post('http://186.155.119.177:8080/RedetekAPIRest/rest/clientes?idNodo=' + $scope.nodoX.idNodo, $scope.data, config).then(function (response) {
                $state.go('verClientes', {}, {reload: true});
            },
            function(response){
                console.log(response.data);
                $state.go('verNodos', {}, {reload: true});
                document.getElementById("errorText").innerHTML = response.data.ERROR;
                $('#errorModal').modal("show");
            }
            );
        };
    }]);


    app.controller('modificarClienteCtrl', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {
        var ruta = "http://186.155.119.177:8080/RedetekAPIRest/rest/nodos";
        $http.get(ruta).then(function (response) {
            $scope.nodos = response.data;
        });      

        ruta = "http://186.155.119.177:8080/RedetekAPIRest/rest/planes";
        $http.get(ruta).then(function (response) {
            $scope.planes = response.data;
        });


        if ($state.params.idCliente !== undefined) {
            if( $state.params.idCliente !== null ){
                console.log('entró');
                var ruta = "http://186.155.119.177:8080/RedetekAPIRest/rest/clientes/" + $state.params.idCliente;
                $http.get(ruta).then(function (response) {
                    $scope.cliente = response.data;

                    $http.get("http://186.155.119.177:8080/RedetekAPIRest/rest/nodos/busq?filtro=3&parametro='" + $scope.cliente.nombreNodo + "'").then(function (response) {
                        $scope.nodoX = response.data;
                    });    


                    var config = {
                        headers : {
                            'Content-Type': 'application/json'
                        }
                    };

                    $scope.modificarCliente = function () {
                        console.log($scope.data);
                        $http.put('http://186.155.119.177:8080/RedetekAPIRest/rest/clientes/' + $state.params.idCliente + '?idNodo=' + $scope.nodoX.id, $scope.cliente, config).then(function (response) {
                            $state.go('verClientes', {}, {reload: true});
                        },
                        function(response){
                            console.log(response.data);
                            $state.go('verClientes', {}, {reload: true});
                            document.getElementById("errorText").innerHTML = response.data.ERROR;
                            $('#errorModal').modal("show");
                        }
                        );
                    };


                }, function(response){
                    console.log(response.data);
                    $state.go('verNodos', {}, {reload: true});
                    document.getElementById("errorText").innerHTML = response.data.ERROR;
                    $('#errorModal').modal("show");
                });
            }else {
                console.log('paila interno');
            }
        } else {
            console.log('paila paila');
        }
    }]);


    app.controller('borrarClienteCtrl', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {
        var ruta = "http://186.155.119.177:8080/RedetekAPIRest/rest/nodos";


        if ($state.params.idCliente !== undefined) {
            if( $state.params.idCliente !== null ){
                console.log('entró');
                var ruta = "http://186.155.119.177:8080/RedetekAPIRest/rest/clientes/" + $state.params.idCliente;

                $scope.borrarCliente = function () {
                    console.log($scope.data);
                    $http.delete(ruta, {}).then(function (response) {
                        $state.go('verClientes', {}, {reload: true});
                    }, function(response){
                        console.log(response.data);
                        $state.go('verNodos', {}, {reload: true});
                        document.getElementById("errorText").innerHTML = response.data.ERROR;
                        $('#errorModal').modal("show");
                    }
                    );
                };

            }else {
                console.log('paila interno');
            }
        } else {
            console.log('paila paila');
        }
    }]);    

})(window.angular);
