/**
 * Created by nguyen the dat on 23/4/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.DocumentType').controller('DocumentTypeController', DocumentTypeController);

    DocumentTypeController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'DocumentTypeService'
    ];

    function DocumentTypeController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        var vm = this;

        vm.documentType = {};
        vm.documentTypes = [];
        vm.selectedDocumentTypes = [];

        vm.pageIndex = 1;
        vm.pageSize = 25;

        vm.getDocumentTypes = function () {
            service.getDocumentTypes(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.documentTypes = data.content;
                console.log(vm.documentTypes);
                vm.bsTableControlDocType.options.data = vm.documentTypes;
                vm.bsTableControlDocType.options.totalRows = data.totalElements;
            });
        };

        vm.getDocumentTypes();

        vm.bsTableControlDocType = {
            options: {
                data: vm.documentTypes,
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
                        vm.selectedDocumentTypes.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentTypes = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedDocumentTypes.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentTypes = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    vm.getDocumentTypes();
                }
            }
        };

        /**
         * New event account
         */
        vm.newDocumentType = function () {

            vm.documentType.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (!vm.documentType.code || vm.documentType.code.trim() == '') {
                        toastr.error('Vui lòng nhập mã.', 'Lỗi');
                        return;
                    }

                    if (!vm.documentType.name || vm.documentType.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên.', 'Lỗi');
                        return;
                    }
                    
                    service.saveDocumentType(vm.documentType, function success() {

                        // Refresh list
                        vm.getDocumentTypes();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một tài khoản.', 'Thông báo');

                        // clear object
                        vm.documentType = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một tài khoản.', 'Thông báo');
                    });
                }
            }, function () {
                vm.documentType = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editDocumentType = function (id) {
            service.getDocumentType(id).then(function (data) {

                vm.documentType = data;
                vm.documentType.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_modal.html',
                    scope: $scope,
                    size: 'md'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (!vm.documentType.code || vm.documentType.code.trim() == '') {
                            toastr.error('Vui lòng nhập mã.', 'Lỗi');
                            return;
                        }

                        if (!vm.documentType.name || vm.documentType.name.trim() == '') {
                            toastr.error('Vui lòng nhập tên.', 'Lỗi');
                            return;
                        }

                        service.saveDocumentType(vm.documentType, function success() {

                            // Refresh list
                            vm.getDocumentTypes();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object
                            vm.documentType = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin tài khoản.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.documentType = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        /**
         * Delete accounts
         */
        $scope.deleteDocumentType = function (id) {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    service.deleteDocumentType(id, function success() {

                        vm.getDocumentTypes();

                        toastr.info('Bạn đã xóa thành công bản ghi.', 'Thông báo');

                        vm.selectedDocumentTypes = [];
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