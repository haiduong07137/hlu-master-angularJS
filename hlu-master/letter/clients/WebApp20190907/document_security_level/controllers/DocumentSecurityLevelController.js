/**
 * Created by nguyen the dat on 23/4/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.DocumentSecurityLevel').controller('DocumentSecurityLevelController', DocumentSecurityLevelController);

    DocumentSecurityLevelController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'DocumentSecurityLevelService'
    ];

    function DocumentSecurityLevelController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        var vm = this;

        vm.documentSecurityLevel = {};
        vm.documentSecurityLevels = [];
        vm.selectedDocumentSecurityLevels = [];

        vm.pageIndex = 1;
        vm.pageSize = 25;

        vm.getDocumentSecurityLevels = function () {
            service.getDocumentSecurityLevels(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.documentSecurityLevels = data.content;
                console.log(vm.documentSecurityLevels);
                vm.bsTableControlDocSecurity.options.data = vm.documentSecurityLevels;
                vm.bsTableControlDocSecurity.options.totalRows = data.totalElements;
            });
        };

        vm.getDocumentSecurityLevels();

        vm.bsTableControlDocSecurity = {
            options: {
                data: vm.documentSecurityLevels,
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
                        vm.selectedDocumentSecurityLevels.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentSecurityLevels = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedDocumentSecurityLevels.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentSecurityLevels = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    vm.getDocumentSecurityLevels();
                }
            }
        };

        /**
         * New event account
         */
        vm.newDocumentSecurityLevel = function () {

            vm.documentSecurityLevel.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_modal.html',
                scope: $scope,
                backdrop:'static',
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (!vm.documentSecurityLevel.code || vm.documentSecurityLevel.code.trim() == '') {
                        toastr.error('Vui lòng nhập mã.', 'Lỗi');
                        return;
                    }

                    if (!vm.documentSecurityLevel.name || vm.documentSecurityLevel.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên.', 'Lỗi');
                        return;
                    }
                    
                    service.saveDocumentSecurityLevel(vm.documentSecurityLevel, function success() {

                        // Refresh list
                        vm.getDocumentSecurityLevels();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một tài khoản.', 'Thông báo');

                        // clear object
                        vm.documentSecurityLevel = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một tài khoản.', 'Thông báo');
                    });
                }
            }, function () {
                vm.documentSecurityLevel = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editDocumentSecurityLevel = function (id) {
            service.getDocumentSecurityLevel(id).then(function (data) {

                vm.documentSecurityLevel = data;
                vm.documentSecurityLevel.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_modal.html',
                    scope: $scope,
                    backdrop:'static',
                    size: 'md'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (!vm.documentSecurityLevel.code || vm.documentSecurityLevel.code.trim() == '') {
                            toastr.error('Vui lòng nhập mã.', 'Lỗi');
                            return;
                        }

                        if (!vm.documentSecurityLevel.name || vm.documentSecurityLevel.name.trim() == '') {
                            toastr.error('Vui lòng nhập tên.', 'Lỗi');
                            return;
                        }

                        service.saveDocumentSecurityLevel(vm.documentSecurityLevel, function success() {

                            // Refresh list
                            vm.getDocumentSecurityLevels();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object
                            vm.documentSecurityLevel = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin tài khoản.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.documentSecurityLevel = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        /**
         * Delete accounts
         */
        $scope.deleteDocumentSecurityLevel = function (id) {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    service.deleteDocumentSecurityLevel(id, function success() {

                        vm.getDocumentSecurityLevels();

                        toastr.info('Bạn đã xóa thành công bản ghi.', 'Thông báo');

                        vm.selectedDocumentSecurityLevels = [];
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