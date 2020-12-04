/**
 * Created by nguyen the dat on 23/4/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.Ethnics').controller('EthnicsController', EthnicsController);

    EthnicsController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'EthnicsService'
    ];

    function EthnicsController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        var vm = this;

        vm.ethnics = {};
        vm.ethnicses = [];
        vm.selectedEthnicses = [];

        vm.pageIndex = 1;
        vm.pageSize = 25;

        vm.typeOption=[
        	{
        		id:1,
        		name:'Chính Quyền'
        	},
        	{
        		id:2,
        		name:'Đoàn thể'
        	}
        ]
        
        vm.getEthnicses = function () {
            service.getEthnicses(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.ethnicses = data.content;
                vm.bsTableControl.options.data = vm.ethnicses;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        };

        vm.getEthnicses();

        vm.bsTableControl = {
            options: {
                data: vm.ethnicses,
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
                        vm.selectedEthnicses.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedEthnicses = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedEthnicses.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedEthnicses = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    vm.getEthnicses();
                }
            }
        };

        /**
         * New event account
         */
        vm.newEthnics = function () {

            vm.ethnics.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_ethnics_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (!vm.ethnics.code || vm.ethnics.code.trim() == '') {
                        toastr.error('Vui lòng nhập mã phần tử lương.', 'Lỗi');
                        return;
                    }

                    if (!vm.ethnics.name || vm.ethnics.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên phần tử lương.', 'Lỗi');
                        return;
                    }
                    
                    service.saveEthnics(vm.ethnics, function success() {

                        // Refresh list
                        vm.getEthnicses();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một tài khoản.', 'Thông báo');

                        // clear object
                        vm.ethnics = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một tài khoản.', 'Thông báo');
                    });
                }
            }, function () {
                vm.ethnics = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editEthnics = function (ethnicsId) {
            service.getEthnics(ethnicsId).then(function (data) {

                vm.ethnics = data;
                vm.ethnics.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_ethnics_modal.html',
                    scope: $scope,
                    backdrop: 'static', // khóa ko cho đóng 
                    size: 'md'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (!vm.ethnics.code || vm.ethnics.code.trim() == '') {
                            toastr.error('Vui lòng nhập mã phần tử lương.', 'Lỗi');
                            return;
                        }

                        if (!vm.ethnics.name || vm.ethnics.name.trim() == '') {
                            toastr.error('Vui lòng nhập tên phần tử lương.', 'Lỗi');
                            return;
                        }

                        service.saveEthnics(vm.ethnics, function success() {

                            // Refresh list
                            vm.getEthnicses();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object
                            vm.ethnics = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin tài khoản.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.ethnics = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        /**
         * Delete accounts
         */
        vm.deleteEthnicses = function () {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                	console.log(vm.selectedEthnicses);
                    service.deleteEthnicses(vm.selectedEthnicses, function success() {

                        // Refresh list
                        vm.getEthnicses();

                        // Notify
                        toastr.info('Bạn đã xóa thành công ' + vm.selectedEthnicses.length + ' bản ghi.', 'Thông báo');

                        // Clear selected accounts
                        vm.selectedEthnicses = [];
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