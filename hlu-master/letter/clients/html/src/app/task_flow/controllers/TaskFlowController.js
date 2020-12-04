/**
 * Created by nguyen the dat on 23/4/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.TaskFlow').controller('TaskFlowController', TaskFlowController);

    TaskFlowController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'TaskFlowService'
    ];

    function TaskFlowController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        var vm = this;

        //Initial Page//
        vm.taskFlow = {};
        vm.taskFlows = [];
        vm.selectedTaskFlows = [];
        vm.steps = [];
        vm.step = null;
        vm.taskFlowSteps = [];
        vm.pageIndex = 1;
        vm.pageSize = 25;
        vm.autoCreate = false;

        vm.getSteps = function () {
            service.getSteps(1, 100000).then(function (data) {
                vm.steps = data.content;
            });
        };
        vm.getSteps();

        vm.getTaskFlows = function () {
            service.getTaskFlows(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.taskFlows = data.content;
                vm.bsTableControlTaskFlow.options.data = vm.taskFlows;
                vm.bsTableControlTaskFlow.options.totalRows = data.totalElements;
                console.log(vm.taskFlows);
            });
        };
        vm.getTaskFlows();
        //---------------------//


        //Step process//
        vm.autoCreateTaskFlowStep = function () {
            if(vm.taskFlow == null){
                vm.taskFlow = {};
            }
            if(vm.steps != null && vm.steps.length > 0){
                if(angular.isUndefined(vm.taskFlow.steps) ||vm.taskFlow.steps == null){
                    vm.taskFlow.steps = [];
                }
                if(vm.autoCreate){
                    for(var j = 0; j < vm.steps.length; j++){
                        var dup = false;
                        for(var i = 0; i < vm.taskFlow.steps.length; i++){
                            if(vm.steps[j].id == vm.taskFlow.steps[i].step.id){
                                dup = true;
                            }
                        }
                        if(!dup){
                            var flowStep = {};
                            flowStep.step = vm.steps[j]
                            vm.taskFlow.steps.push(flowStep);
                        }
                    }
                    console.log(vm.taskFlow);
                }else{
                    vm.taskFlow.steps = [];
                }
            }
        };
        
        vm.addStep = function () {
            if(vm.taskFlow == null){
                vm.taskFlow = {};
            }
            if(angular.isUndefined(vm.taskFlow.steps) ||vm.taskFlow.steps == null){
                vm.taskFlow.steps = [];
            }
            var dup = false;
            for(var i = 0; i < vm.taskFlow.steps.length; i++){
                if(vm.step.id == vm.taskFlow.steps[i].step.id){
                    dup = true;
                    toastr.warning('Bị trùng');
                }
            }
            if(!dup){
                var flowStep = {};
                flowStep.step = vm.step;
                vm.taskFlow.steps.push(flowStep);
            }
        };
        //----------//
        vm.bsTableControlTaskFlow = {
            options: {
                data: vm.taskFlows,
                idField: 'id',
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: true,
                showToggle: true,
                pagination: true,
                pageSize: vm.pageSize,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: service.getTableDefinition(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedTaskFlows.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedTaskFlows = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedTaskFlows.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedTaskFlows = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    vm.getTaskFlows();
                }
            }
        };

        /**
         * New event account
         */
        vm.newTaskFlow = function () {

            vm.taskFlow.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (!vm.taskFlow.code || vm.taskFlow.code.trim() == '') {
                        toastr.error('Vui lòng nhập mã.', 'Lỗi');
                        return;
                    }

                    if (!vm.taskFlow.name || vm.taskFlow.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên.', 'Lỗi');
                        return;
                    }
                    
                    service.saveTaskFlow(vm.taskFlow, function success() {

                        // Refresh list
                        vm.getTaskFlows();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một tài khoản.', 'Thông báo');

                        // clear object
                        vm.taskFlow = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một tài khoản.', 'Thông báo');
                    });
                }
            }, function () {
                vm.taskFlow = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editTaskFlow = function (id) {
            service.getTaskFlow(id).then(function (data) {

                vm.taskFlow = data;
                vm.taskFlow.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_modal.html',
                    scope: $scope,
                    size: 'md'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (!vm.taskFlow.code || vm.taskFlow.code.trim() == '') {
                            toastr.error('Vui lòng nhập mã.', 'Lỗi');
                            return;
                        }

                        if (!vm.taskFlow.name || vm.taskFlow.name.trim() == '') {
                            toastr.error('Vui lòng nhập tên.', 'Lỗi');
                            return;
                        }

                        service.saveTaskFlow(vm.taskFlow, function success() {

                            // Refresh list
                            vm.getTaskFlows();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object
                            vm.taskFlow = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin tài khoản.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.taskFlow = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        /**
         * Delete accounts
         */
        $scope.deleteTaskFlow = function (id) {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    service.deleteTaskFlow(id, function success() {

                        vm.getTaskFlows();

                        toastr.info('Bạn đã xóa thành công bản ghi.', 'Thông báo');

                        vm.selectedTaskFlows = [];
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi xóa bản ghi.', 'Lỗi');
                    });
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };
    }

})();