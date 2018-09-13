(function (ng) {
    var app = angular.module('app', [
        'ui.router', 'ui.bootstrap'
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
                'mainView': {
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
                'ViewCrearNodo': {
                    templateUrl: 'src/crearNodo.html',
                    controller: 'crearNodoCtrl',
                    controllerAs: 'ctrl'
                }
            }
        });



        $stateProvider.state('crearCliente', {
            url: '/clientes/crearCliente',
            params: {
                filtro: null,
                parametro: null
            },
            views: {
                'ViewCrearCliente': {
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
                'ViewModificarCliente': {
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
                'ViewBorrarCliente': {
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
                'mainView': {
                    templateUrl: 'src/nodos.html',
                    controller: 'nodosCtrl',
                    controllerAs: 'ctrl'
                }
            }
        });

        $stateProvider.state('clienteDetail', {
            url: '/clientes/detalle/{clienteId:int}',
            params: {
                clienteId: null
            },
            views: {
                'mainView': {
                    templateUrl: 'src/detalleCliente.html',
                    controller: 'detalleClienteCtrl',
                    controllerAs: 'ctrl'
                }
            }
        });

        $stateProvider.state('agregarOrdenCliente', {
            url: '/orden/crearOrden?idCliente={clienteId : int}',
            params: {
                clienteId: null, 
                clienteRef: null
            },
            views: {
                'ViewAgregarOrden':{
                    templateUrl: 'src/agregarOrden.html',
                    controller: 'agregarOrdenCtrl'               
                },
                'mainView': {
                    templateUrl: 'src/detalleCliente.html',
                    controller: 'detalleClienteCtrl'
                }
            }
        });



        $stateProvider.state('verFotosOrden', {
            url: '/orden/{idOrden:int}/fotos',
            params: {
                idOrden: null, 
                clienteRef: null
            },
            views: {
                'ViewVerFotos':{
                    templateUrl: 'src/verFotosOrden.html',
                    controller: 'verFotosOrdenCtrl'               
                },
                'mainView': {
                    templateUrl: 'src/detalleCliente.html',
                    controller: 'detalleClienteCtrl'
                }
            }
        });

        $stateProvider.state('agregarDispositivoCliente', {
            url: '/clientes/detalle/{clienteId:int}/agregarDispositivo',
            params: {
                clienteId: null
            },
            views: {
                'ViewAgregarDispositivo': {
                    templateUrl: 'src/agregarDispositivoCliente.html',
                    controller: 'agregarDispositivoClienteCtrl',
                    controllerAs: 'ctrl'
                }
            }
        });

    });

app.controller('clientesCtrl', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {

    var ruta = "http://localhost:8080/RedetekAPIRest/rest/clientes";
    $http.get(ruta).then(function (response) {
        $scope.clientes = response.data;
    }, function(response){
        console.log(response.data);
        $state.go('verNodos', {}, {reload: true});
        document.getElementById("errorText").innerHTML = response.data.ERROR;
        $('#errorModal').modal("show");
    });
}]);


app.controller('agregarDispositivoClienteCtrl', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {
    var ruta = "http://localhost:8080/RedetekAPIRest/rest/dispositivos/tiposDispositivo";

    $http.get(ruta).then(function (response) {
        $scope.tipos = response.data;
    });

    $scope.data = {
        "tipo": {
            "id": 1
        }

    };

    var config = {
        headers : {
            'Content-Type': 'application/json'
        }
    };

    $scope.validar = function(valor, id) {
        console.log(valor);
        if(valor === null || valor === undefined){
            return true;
        }else{
            if(valor == 'A' ||
                valor == 'B' ||
                valor == 'C' ||
                valor == 'D' || 
                valor == 'E' || 
                valor == 'F' || 
                valor == '1' || 
                valor == '2' || 
                valor == '3' ||
                valor == '4' || 
                valor == '5' ||
                valor == '6' ||
                valor == '7' || 
                valor == '8' ||
                valor == '9'){
                return true;
        }else{
            var x = document.getElementById(id);
            x.style.color = '#AF2E13';
            return false;
        }
    }
};

$scope.agregarDispositivoClienteFuncion = function () {
    console.log($scope.data);
    $http.post('http://localhost:8080/RedetekAPIRest/rest/clientes/'+$state.params.clienteId+'/agregarDispositivo', $scope.data, config).then(function (response) {
        $state.go('clienteDetail', {clienteId: $state.params.clienteId}, {reload: true});
    },
    function(response){
        console.log(response.data);
        $state.go('clienteDetail', {clienteId: $state.params.clienteId}, {reload: true});
        document.getElementById("errorText").innerHTML = response.data.ERROR;
        $('#errorModal').modal("show");
    }
    );
};
}]);



app.controller('nodosCtrl', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {

    var ruta = "http://localhost:8080/RedetekAPIRest/rest/nodos";
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
    $http.post('http://localhost:8080/RedetekAPIRest/rest/nodos', $scope.data, config).then(function (response) {
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

    var ruta = "http://localhost:8080/RedetekAPIRest/rest/nodos";
    $http.get(ruta).then(function (response) {
        $scope.nodos = response.data;
    });      

    ruta = "http://localhost:8080/RedetekAPIRest/rest/planes";
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
        $http.post('http://localhost:8080/RedetekAPIRest/rest/clientes?idNodo=' + $scope.nodoX.idNodo, $scope.data, config).then(function (response) {
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



app.controller('agregarOrdenCtrl', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {


    $scope.example1model = []; 
    $scope.example1data = [ {id: 1, label: "David"}, {id: 2, label: "Jhon"}, {id: 3, label: "Danny"} ];

    $scope.tipos = [{
        "id": 1,
        "nombre": "Instalación"
    }, {
        "id": 2,
        "nombre": "Revisión"
    }, {
        "id": 3,
        "nombre": "Reparación"
    }, {
        "id": 4,
        "nombre": "Traslado"
    }, {
        "id": 5,
        "nombre": "Retiro"
    }];

    

    var ruta = "http://localhost:8080/RedetekAPIRest/rest/planes";
    $http.get(ruta).then(function (response) {
        $scope.planes = response.data;
    });



    $scope.data = {};

    var config = {
        headers : {
            'Content-Type': 'application/json'
        }
    };

    $scope.crearOrden = function () {
        console.log($state.params.clienteId);
        $http.post('http://localhost:8080/RedetekAPIRest/rest/ordenes/crearOrden?idCliente=' + $state.params.clienteId, $scope.data, config).then(function (response) {
            $state.go('clienteDetail', {clienteId: $state.params.clienteId}, {reload: true});
        },
        function(response){
            console.log(response.data);
            $state.go('clienteDetail', {clienteId: $state.params.clienteId}, {reload: true});
            document.getElementById("errorText").innerHTML = response.data.ERROR;
            $('#errorModal').modal("show");
        }
        );
    };

}]);


app.controller('detalleClienteCtrl', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {
    if(($state.params.clienteId !== undefined) && ($state.params.clienteId !== null)){
        console.log("entro al detalle del Cliente");
        var ruta = "http://localhost:8080/RedetekAPIRest/rest/clientes/" + $state.params.clienteId;
        $http.get(ruta).then(function (response) {
            $scope.cliente = response.data;
            var i = 0;
            while( i < $scope.cliente.ordenes.length) {
                if($scope.cliente.ordenes[i].tipo == 1){
                    $scope.cliente.ordenes[i].tipo = 'Instalación';
                } 
                i++;  
            }
        });
    }else if(($state.params.clienteRef !== undefined) && ($state.params.clienteRef !== null)){
        $scope.cliente = $state.params.clienteRef;
    }
}]);

app.controller('verFotosOrdenCtrl', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {
    if(($state.params.idOrden !== undefined) && ($state.params.idOrden !== null)){
        console.log("entro pues al ver fotos");
        if($scope.fotos == null || $scope.fotos == undefined){
            console.log("paila, sigue...");
            $scope.listasLasFotos = false;
            var ruta2 = "http://localhost:8080/RedetekAPIRest/rest/ordenes/"+$state.params.idOrden+"/descargarfotosftp";
            $http.get(ruta2).then(function (response) {
                $scope.fotos = response.data;
                $scope.listasLasFotos = true;
            });

        }else{
            console.log("paró");
            $scope.listasLasFotos = true;
        }

    }


}]);



app.controller('modificarClienteCtrl', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {
    var ruta = "http://localhost:8080/RedetekAPIRest/rest/nodos";
    $http.get(ruta).then(function (response) {
        $scope.nodos = response.data;
    });      

    ruta = "http://localhost:8080/RedetekAPIRest/rest/planes";
    $http.get(ruta).then(function (response) {
        $scope.planes = response.data;
    });


    if ($state.params.idCliente !== undefined) {
        if( $state.params.idCliente !== null ){
            console.log('entró a modificar cliente');
            var ruta = "http://localhost:8080/RedetekAPIRest/rest/clientes/" + $state.params.idCliente;
            $http.get(ruta).then(function (response) {
                $scope.cliente = response.data;

                $http.get("http://localhost:8080/RedetekAPIRest/rest/nodos/busq?filtro=3&parametro='" + $scope.cliente.nombreNodo + "'").then(function (response) {
                    $scope.nodoX = response.data;
                });    


                var config = {
                    headers : {
                        'Content-Type': 'application/json'
                    }
                };

                $scope.modificarCliente = function () {
                    console.log($scope.data);
                    $http.put('http://localhost:8080/RedetekAPIRest/rest/clientes/' + $state.params.idCliente + '?idNodo=' + $scope.nodoX.id, $scope.cliente, config).then(function (response) {
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
    var ruta = "http://localhost:8080/RedetekAPIRest/rest/nodos";

    if ($state.params.idCliente !== undefined) {
        if( $state.params.idCliente !== null ){
            console.log('entró');
            var ruta = "http://localhost:8080/RedetekAPIRest/rest/clientes/" + $state.params.idCliente;

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

angular.module('ui.bootstrap.carousel', ['ui.bootstrap.transition'])
.controller('CarouselController', ['$scope', '$timeout', '$transition', '$q', function        ($scope, $timeout, $transition, $q) {
}]).directive('carousel', [function() {
    return {

    }
}]);



})(window.angular);
