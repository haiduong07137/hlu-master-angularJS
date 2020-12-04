/**
 * Created by nguyen the dat on 23/4/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.TaskPriority').controller('TaskPriorityController', TaskPriorityController);

    TaskPriorityController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'TaskPriorityService'
    ];

    function TaskPriorityController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        var vm = this;

        vm.documentPriority = {};
        vm.documentPriorities = [];
        vm.selectedTaskPriorities = [];

        vm.pageIndex = 1;
        vm.pageSize = 25;

        vm.getTaskPriorities = function () {
            service.getTaskPriorities(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.documentPriorities = data.content;
                console.log(vm.documentPriorities);
                vm.bsTableControl.options.data = vm.documentPriorities;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        };

        vm.getTaskPriorities();

        vm.bsTableControl = {
            options: {
                data: vm.documentPriorities,
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
                        vm.selectedTaskPriorities.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedTaskPriorities = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedTaskPriorities.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedTaskPriorities = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    vm.getTaskPriorities();
                }
            }
        };

        /**
         * New event account
         */
        vm.newTaskPriority = function () {

            vm.documentPriority.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                backdrop: 'static',
                templateUrl: 'edit_modal.html',
                scope: $scope,
                size: 'md'
            });

            vm.CheckResult = function () {

                if (!vm.documentPriority.code || vm.documentPriority.code.trim() == '') {
                    toastr.error('Vui lòng nhập mã mức độ ưu tiên.', 'Lỗi');
                    return;
                }
                if (!vm.documentPriority.name || vm.documentPriority.name.trim() == '') {
                    toastr.error('Vui lòng nhập tên mức độ ưu tiên.', 'Lỗi');
                    return;
                }
                service.saveTaskPriority(vm.documentPriority, function success() {

                    // Refresh list
                    vm.getTaskPriorities();

                    // Notify
                    toastr.info('Bạn đã tạo mới thành công một mức độ ưu tiên.', 'Thông báo');
                    // clear object
                    vm.documentPriority = {};
                    modalInstance.close();
                }, function failure() {
                    toastr.error('Có lỗi xảy ra khi thêm mới một mức độ ưu tiên.', 'Thông báo');
                });
            }
        }, function () {
            vm.documentPriority = {};
            console.log('Modal dismissed at: ' + new Date());
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editTaskPriority = function (id) {
            service.getTaskPriority(id).then(function (data) {

                vm.documentPriority = data;
                vm.documentPriority.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    backdrop: 'static',
                    templateUrl: 'edit_modal.html',
                    scope: $scope,
                    size: 'md'
                });

                vm.CheckResult = function () {
                    if (!vm.documentPriority.code || vm.documentPriority.code.trim() == '') {
                        toastr.error('Vui lòng nhập mã mức độ ưu tiên.', 'Lỗi');
                        return;
                    }
                    if (!vm.documentPriority.name || vm.documentPriority.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên mức độ ưu tiên.', 'Lỗi');
                        return;
                    }

                    service.saveTaskPriority(vm.documentPriority, function success() {

                        // Refresh list
                        vm.getTaskPriorities();

                        // Notify
                        toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                        // clear object
                        vm.documentPriority = {};
                        modalInstance.close();
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi lưu thông tin mức độ ưu tiên.', 'Lỗi');
                    });
                }
            }, function () {
                vm.documentPriority = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Delete accounts
         */
        $scope.deleteTaskPriority = function (id) {
            var modalInstance = modal.open({
                animation: true,
                backdrop:'static',
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            vm.CheckResult = function () {
                    service.deleteTaskPriority(id, function success() {

                        vm.getTaskPriorities();

                        toastr.info('Bạn đã xóa thành công bản ghi.', 'Thông báo');

                        vm.selectedTaskPriorities = [];
                        modalInstance.close();
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi xóa bản ghi.', 'Lỗi');
                    });
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
        };
    }

})();