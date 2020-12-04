/**
 * Created by bizic on 28/8/2016.
 */
(function () {
    'use strict';

    angular.module('Hrm.Room').controller('RoomController', RoomController);

    RoomController.$inject = [
        '$rootScope',
        '$scope',
        '$http',
        '$timeout',
        'settings',
        'RoomService',
        '$uibModal',
        'toastr'
    ];

    function RoomController ($rootScope, $scope, $http, $timeout,settings,service,modal,toastr) {
        $scope.$on('$viewContentLoaded', function() {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        var vm = this;
        vm.room = null;
        vm.rooms = [];
        vm.selectedRooms = [];

        // pagination
        vm.pageIndex = 1;
        vm.pageSize = 10;

        vm.buildings = null;
        vm.building = null;

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

        function getListRoom(pageIndex, pageSize){
            service.getListRoom(pageIndex,pageSize).then(function(data) {
                vm.rooms = data.content;
                if(vm.rooms.length <= 0 && data.totalElements != 0){
                    $state.reload();
                }
                vm.bsTableControl.options.data = vm.rooms;
                vm.bsTableControl.options.totalRows = data.totalElements;
                console.log(data);
            });
        }

        function newRoom(Room) {
            service.newRoom(Room).then(function(data) {
                getListRoom(vm.pageIndex,vm.pageSize);
                modalInstance.close();
                toastr.info('Thêm mới thành công','Thông báo');
            });
        }

        function getRoomById(RoomById){
            service.getRoomById(RoomById).then(function(data) {
                vm.room = data;
                vm.tempCode = vm.room.code;
                console.log(vm.room);
            });
        }

        function editRoom(Room) {
            service.editRoom(Room).then(function (data) {
                getListRoom(vm.pageIndex,vm.pageSize);
                modalInstance.close();
                toastr.info('Lưu thành công','Thông báo');
            });
        }

        function deleteRoom(RoomId) {
            service.deleteRoom(RoomId).then(function (data) {
                getListRoom(vm.pageIndex,vm.pageSize);
                toastr.info('Xóa thành công.', 'Thông báo');
            });
        }

        function getBuilding() {
            service.getBuilding().then(function (data) {
                vm.buildings = data.content;
            });
        }

        //check duplicate code
        function validateEducationLevel() {
            console.log(vm.room);
            if(vm.room == null){
                toastr.warning("Chưa nhập dữ liệu");
                return false;
            }
            if(angular.isUndefined(vm.room.code) || vm.room.code == null || vm.room.code.length <= 0){
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
                            newRoom(vm.room);
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
                                    vm.room.code = vm.tempCode.trim();
                                    editRoom(vm.room);
                                }
                            });
                        }else{
                            vm.room.code = vm.tempCode.trim();
                            editRoom(vm.room);
                        }
                    }
                }
                console.log(data);

            });
        }

        vm.checkDuplicateCode = function (type,action) {
            if(validateEducationLevel()){
                checkDuplicateCode(vm.room.code,type,action);
            }
        }

        var modalInstance;

        vm.newRoom = function () {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'new_modal.html',
                backdrop: 'static',
                scope: $scope,
                size: 'md'
            });

            vm.room = {};

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    newRoom(vm.room);
                }
            }, function () {
                console.log("cancel");
            });
        }

        $scope.editRoom = function (RoomId) {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_modal.html',
                backdrop: 'static',
                scope: $scope,
                size: 'md'
            });

            getRoomById(RoomId);

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    editRoom(vm.room);
                }
            }, function () {
                console.log("cancel");
            });
        }

        $scope.viewRoom = function (RoomId) {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'view_modal.html',
                backdrop: 'static',
                scope: $scope,
                size: 'md'
            });

            getRoomById(RoomId);

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                }
            }, function () {
                console.log("cancel");
            });
        }

        $scope.deleteRoom = function (RoomId) {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            getRoomById(RoomId);

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    if(vm.room != null){
                        if(!angular.isUndefined(vm.room.id)){
                            deleteRoom(vm.room.id);
                        }
                    }
                }
            }, function () {
                console.log("cancel");
            });
        }

        getListRoom(vm.pageIndex,vm.pageSize);
        getBuilding();

        vm.bsTableControl = {
            options: {
                data: vm.rooms,
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
                        vm.selectedRooms.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedRooms = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedRooms);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedRooms.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedRooms = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    getListRoom(vm.pageIndex,vm.pageSize);
                },
                onChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    getListRoom(vm.pageIndex,vm.pageSize);
                }
            }
        };
    }
})();
