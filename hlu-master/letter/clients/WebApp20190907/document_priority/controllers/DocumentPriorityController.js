/**
 * Created by nguyen the dat on 23/4/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.DocumentPriority').controller('DocumentPriorityController', DocumentPriorityController);

    DocumentPriorityController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'DocumentPriorityService'
    ];

    function DocumentPriorityController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service) {
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
        vm.selectedDocumentPriorities = [];

        vm.pageIndex = 1;
        vm.pageSize = 25;

        vm.getDocumentPriorities = function () {
            service.getDocumentPriorities(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.documentPriorities = data.content;
                console.log(vm.documentPriorities);
                vm.bsTableControlDocPriorities.options.data = vm.documentPriorities;
                vm.bsTableControlDocPriorities.options.totalRows = data.totalElements;
            });
        };

        vm.getDocumentPriorities();

        vm.bsTableControlDocPriorities = {
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
                        vm.selectedDocumentPriorities.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentPriorities = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedDocumentPriorities.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentPriorities = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    vm.getDocumentPriorities();
                }
            }
        };

        /**
         * New event account
         */
        vm.newDocumentPriority = function () {

            vm.documentPriority.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_modal.html',
                scope: $scope,
                backdrop:'static',
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (!vm.documentPriority.code || vm.documentPriority.code.trim() == '') {
                        toastr.error('Vui lòng nhập mã.', 'Lỗi');
                        return;
                    }

                    if (!vm.documentPriority.name || vm.documentPriority.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên.', 'Lỗi');
                        return;
                    }
                    
                    service.saveDocumentPriority(vm.documentPriority, function success() {

                        // Refresh list
                        vm.getDocumentPriorities();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một tài khoản.', 'Thông báo');

                        // clear object
                        vm.documentPriority = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một tài khoản.', 'Thông báo');
                    });
                }
            }, function () {
                vm.documentPriority = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editDocumentPriority = function (id) {
            service.getDocumentPriority(id).then(function (data) {

                vm.documentPriority = data;
                vm.documentPriority.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_modal.html',
                    scope: $scope,
                    backdrop:'static',
                    size: 'md'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (!vm.documentPriority.code || vm.documentPriority.code.trim() == '') {
                            toastr.error('Vui lòng nhập mã.', 'Lỗi');
                            return;
                        }

                        if (!vm.documentPriority.name || vm.documentPriority.name.trim() == '') {
                            toastr.error('Vui lòng nhập tên.', 'Lỗi');
                            return;
                        }

                        service.saveDocumentPriority(vm.documentPriority, function success() {

                            // Refresh list
                            vm.getDocumentPriorities();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object
                            vm.documentPriority = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin tài khoản.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.documentPriority = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        /**
         * Delete accounts
         */
        $scope.deleteDocumentPriority = function (id) {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    service.deleteDocumentPriority(id, function success() {

                        vm.getDocumentPriorities();

                        toastr.info('Bạn đã xóa thành công bản ghi.', 'Thông báo');

                        vm.selectedDocumentPriorities = [];
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