/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.TaskRole').controller('TaskRoleController', TaskRoleController);

    TaskRoleController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'TaskRoleService'
    ];

    function TaskRoleController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        var vm = this;

        vm.taskRole = {};
        vm.taskRoles = [];
        vm.selectedTaskRoles = [];

        vm.pageIndex = 1;
        vm.pageSize = 25;
        /*
         * Viết thêm loại phần tử lương
         */
        vm.typeOption = [
            {
                id: 0,
                name: 'Hằng số'
            },
            {
                id: 1,
                name: 'Nhập bằng tay'
            }, ,
            {
                id: 2,
                name: 'Tính theo công thức'
            }
        ]

        vm.getTaskRoles = function () {
            service.getTaskRoles(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.taskRoles = data.content;
                vm.bsTableControl.options.data = vm.taskRoles;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        };

        vm.getTaskRoles();

        vm.bsTableControl = {
            options: {
                data: vm.taskRoles,
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
                        vm.selectedTaskRoles.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedTaskRoles = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedTaskRoles.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedTaskRoles = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    vm.getTaskRoles();
                }
            }
        };

        /**
         * New event account
         */
        vm.newTaskRole = function () {

            vm.taskRole.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                backdrop: 'static',
                templateUrl: 'edit_task_role_modal.html',
                scope: $scope,
                size: 'md'
            });

            vm.CheckResult = function () {
                if (!vm.taskRole.code || vm.taskRole.code.trim() == '') {
                    toastr.error('Vui lòng nhập mã vai trò công việc.', 'Lỗi');
                    return;
                }

                if (!vm.taskRole.name || vm.taskRole.name.trim() == '') {
                    toastr.error('Vui lòng nhập tên  vai trò công việc.', 'Lỗi');
                    return;
                }

                service.saveTaskRole(vm.taskRole, function success() {

                    // Refresh list
                    vm.getTaskRoles();

                    // Notify
                    toastr.info('Bạn đã tạo mới thành công một  vai trò công việc.', 'Thông báo');

                    // clear object
                    vm.taskRole = {};
                    modalInstance.close();
                }, function failure() {
                    toastr.error('Có lỗi xảy ra khi thêm mới một  vai trò công việc.', 'Thông báo');
                });
            }
        }, function () {
            vm.taskRole = {};
            console.log('Modal dismissed at: ' + new Date());
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editTaskRole = function (taskRoleId) {
            service.getTaskRole(taskRoleId).then(function (data) {

                vm.taskRole = data;
                vm.taskRole.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    backdrop: 'static',
                    templateUrl: 'edit_task_role_modal.html',
                    scope: $scope,
                    size: 'md'
                });

                vm.CheckResult = function () {
                    if (!vm.taskRole.code || vm.taskRole.code.trim() == '') {
                        toastr.error('Vui lòng nhập mã vai trò công việc.', 'Lỗi');
                        return;
                    }

                    if (!vm.taskRole.name || vm.taskRole.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên vai trò công việc.', 'Lỗi');
                        return;
                    }

                    service.saveTaskRole(vm.taskRole, function success() {

                        // Refresh list
                        vm.getTaskRoles();

                        // Notify
                        toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                        // clear object
                        vm.taskRole = {};
                        modalInstance.close();
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi lưu thông tin vai trò công việc.', 'Lỗi');
                    });
                }
            }, function () {
                vm.taskRole = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Delete task role
         */
        $scope.deleteTaskRoleById = function (id) {
            var modalInstance = modal.open({
                animation: true,
                backdrop: 'static',
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    service.deleteTaskRoleById(id, function success() {

                        // Refresh list
                        vm.getTaskRoles();

                        // Notify
                        toastr.info('Bạn đã xóa thành công bản ghi.', 'Thông báo');

                        // Clear selected
                        vm.selectedTaskRoles = [];
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