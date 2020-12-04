/**
 * Created by nguyen the dat on 23/4/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.DocumentField').controller('DocumentFieldController', DocumentFieldController);

    DocumentFieldController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'DocumentFieldService'
    ];

    function DocumentFieldController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        var vm = this;

        vm.documentField = {};
        vm.documentFields = [];
        vm.selectedDocumentFields = [];

        vm.pageIndex = 1;
        vm.pageSize = 25;

        vm.getDocumentFields = function () {
            service.getDocumentFields(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.documentFields = data.content;
                vm.bsTableControlDocFiled.options.data = vm.documentFields;
                vm.bsTableControlDocFiled.options.totalRows = data.totalElements;
            });
        };

        vm.getDocumentFields();

        vm.bsTableControlDocFiled = {
            options: {
                data: vm.documentFields,
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
                        vm.selectedDocumentFields.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentFields = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedDocumentFields.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentFields = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    vm.getDocumentFields();
                }
            }
        };

        /**
         * New event account
         */
        vm.newDocumentField = function () {

            vm.documentField.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (!vm.documentField.code || vm.documentField.code.trim() == '') {
                        toastr.error('Vui lòng nhập mã.', 'Lỗi');
                        return;
                    }

                    if (!vm.documentField.name || vm.documentField.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên.', 'Lỗi');
                        return;
                    }
                    
                    service.saveDocumentField(vm.documentField, function success() {

                        // Refresh list
                        vm.getDocumentFields();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một tài khoản.', 'Thông báo');

                        // clear object
                        vm.documentField = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một tài khoản.', 'Thông báo');
                    });
                }
            }, function () {
                vm.documentField = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editDocumentField = function (id) {
            service.getDocumentField(id).then(function (data) {

                vm.documentField = data;
                vm.documentField.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_modal.html',
                    scope: $scope,
                    size: 'md'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (!vm.documentField.code || vm.documentField.code.trim() == '') {
                            toastr.error('Vui lòng nhập mã.', 'Lỗi');
                            return;
                        }

                        if (!vm.documentField.name || vm.documentField.name.trim() == '') {
                            toastr.error('Vui lòng nhập tên.', 'Lỗi');
                            return;
                        }

                        service.saveDocumentField(vm.documentField, function success() {

                            // Refresh list
                            vm.getDocumentFields();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object
                            vm.documentField = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin tài khoản.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.documentField = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        /**
         * Delete accounts
         */
        $scope.deleteDocumentField = function (id) {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    service.deleteDocumentField(id, function success() {

                        vm.getDocumentFields();

                        toastr.info('Bạn đã xóa thành công bản ghi.', 'Thông báo');

                        vm.selectedDocumentFields = [];
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