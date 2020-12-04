/**
 * Created by bizic on 28/8/2016.
 */
(function () {
    'use strict';

    angular.module('Hrm.Building').controller('BuildingController', BuildingController);

    BuildingController.$inject = [
        '$rootScope',
        '$scope',
        '$http',
        '$timeout',
        'settings',
        'BuildingService',
        '$uibModal',
        'toastr'
    ];

    function BuildingController ($rootScope, $scope, $http, $timeout,settings,service,modal,toastr) {
        $scope.$on('$viewContentLoaded', function() {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        var vm = this;
        vm.building = null;
        vm.buildings = [];
        vm.selectedBuildings = [];

        // pagination
        vm.pageIndex = 1;
        vm.pageSize = 10;

        //check duplicate code
        vm.viewCheckDuplicateCode = {};
        vm.tempCode = '';

        //dateTime
        // vm.datepickerOptions = {
        //     format: 'dd/mm/yyyy',
        //     language: 'en',
        //     startDate: '1/10/1900',
        //     autoclose: true,
        //     weekStart: 0,
        //     todayHighlight: true
        // };
        // vm.optionsDate = {
        //     dropdownSelector: '#dropdown1',
        //     minView: 'day'
        // }

        function getListBuilding(pageIndex, pageSize){
            service.getListBuilding(pageIndex,pageSize).then(function(data) {
                vm.buildings = data.content;
                vm.bsTableControl.options.data = vm.buildings;
                vm.bsTableControl.options.totalRows = data.totalElements;
                console.log(data);
            });
        }

        function newBuilding(Building) {
            service.newBuilding(Building).then(function(data) {
                getListBuilding(vm.pageIndex,vm.pageSize);
                modalInstance.close();
                toastr.info('Thêm mới thành công','Thông báo');
            });
        }

        function getBuildingById(BuildingById){
            service.getBuildingById(BuildingById).then(function(data) {
                vm.building = data;
                vm.tempCode = vm.building.code;
                console.log(vm.building);
            });
        }

        function editBuilding(Building) {
            service.editBuilding(Building).then(function (data) {
                getListBuilding(vm.pageIndex,vm.pageSize);
                modalInstance.close();
                toastr.info('Lưu thành công','Thông báo');
            });
        }

        function deleteBuilding(BuildingId) {
            service.deleteBuilding(BuildingId).then(function (data) {
                getListBuilding(vm.pageIndex,vm.pageSize);
                toastr.info('Xóa thành công.', 'Thông báo');
            });
        }

        //check duplicate code
        function validateEducationLevel() {
            console.log(vm.building);
            if(vm.building == null){
                toastr.warning("Chưa nhập dữ liệu");
                return false;
            }
            if(angular.isUndefined(vm.building.code) || vm.building.code == null || vm.building.code.length <= 0){
                toastr.warning("Chưa nhập mã");
                return false;
            }
            return true;
        }

        function checkDuplicateCode(code,type,action){ //type: 1 -> save; 2 -> edit;   action: 1 -> just check code; 2 -> save or edit
            service.checkDuplicateCode(code).then(function(data) {
                vm.viewCheckDuplicateCode = data;
                if(action == 1){
                    if(type == 1){
                        if(vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == true){
                            toastr.warning("Mã bị trùng");
                        }
                        if(vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == false){
                            toastr.success("Mã không bị trùng");
                        }
                    }
                    if(type == 2){
                        if(vm.tempCode.toLowerCase().trim() != code.toLowerCase().trim()){
                            checkDuplicateCode(vm.tempCode,1,1);
                        }else{
                            toastr.info("Mã chưa thay đổi");
                        }
                    }
                }
                if(action == 2){
                    if(type == 1){
                        if(vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == true){
                            toastr.warning("Mã bị trùng");
                        }
                        if(vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == false){
                            newBuilding(vm.building);
                        }
                    }
                    if(type == 2){
                        if(vm.tempCode.toLowerCase().trim() != code.toLowerCase().trim()){
                            service.checkDuplicateCode(vm.tempCode).then(function(data) {
                                vm.viewCheckDuplicateCode = data;
                                if(vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == true){
                                    toastr.warning("Mã bị trùng");
                                }
                                if(vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == false){
                                    vm.building.code = vm.tempCode.trim();
                                    editBuilding(vm.building);
                                }
                            });
                        }else{
                            vm.building.code = vm.tempCode.trim();
                            editBuilding(vm.building);
                        }
                    }
                }
                console.log(data);

            });
        }

        vm.checkDuplicateCode = function (type,action) {
            if(validateEducationLevel()){
                checkDuplicateCode(vm.building.code,type,action);
            }
        }

        var modalInstance;

        vm.newBuilding = function () {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'new_modal.html',
                backdrop: 'static',
                scope: $scope,
                size: 'md'
            });

            vm.building = {};

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    newBuilding(vm.building);
                }
            }, function () {
                console.log("cancel");
            });
        }

        $scope.editBuilding = function (BuildingId) {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_modal.html',
                backdrop: 'static',
                scope: $scope,
                size: 'md'
            });

            getBuildingById(BuildingId);

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    editBuilding(vm.building);
                }
            }, function () {
                console.log("cancel");
            });
        }

        $scope.viewBuilding = function (BuildingId) {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'view_modal.html',
                backdrop: 'static',
                scope: $scope,
                size: 'md'
            });

            getBuildingById(BuildingId);

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                }
            }, function () {
                console.log("cancel");
            });
        }

        $scope.deleteBuilding = function (BuildingId) {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            getBuildingById(BuildingId);

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    if(vm.building != null){
                        if(!angular.isUndefined(vm.building.id)){
                            deleteBuilding(vm.building.id);
                        }
                    }
                }
            }, function () {
                console.log("cancel");
            });
        }

        getListBuilding(vm.pageIndex,vm.pageSize);

        vm.bsTableControl = {
            options: {
                data: vm.buildings,
                idField: 'id',
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: false,
                singleSelect: false,
                showToggle: false,
                pagination: true,
                pageSize: vm.pageSize,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: service.getTableDefinition(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedBuildings.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedBuildings = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedBuildings);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedBuildings.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedBuildings = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    getListBuilding(vm.pageIndex,vm.pageSize);
                }
            }
        };
    }
})();
