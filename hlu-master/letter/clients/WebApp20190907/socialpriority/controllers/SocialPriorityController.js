/**
 * Created by nguyen the dat on 23/4/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.SocialPriority').controller('SocialPriorityController', SocialPriorityController);

    SocialPriorityController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'SocialPriorityService'
    ];

    function SocialPriorityController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        var vm = this;

        vm.socialpriority = {};
        vm.socialpriorityes = [];
        vm.selectedSocialpriorityes = [];

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
        
        vm.getSocialpriorityes = function () {
            service.getSocialpriorityes(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.socialpriorityes = data.content;
                vm.bsTableControl.options.data = vm.socialpriorityes;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        };

        vm.getSocialpriorityes();

        vm.bsTableControl = {
            options: {
                data: vm.socialpriorityes,
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
                        vm.selectedSocialpriorityes.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedSocialpriorityes = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedSocialpriorityes.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedSocialpriorityes = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    vm.getSocialpriorityes();
                }
            }
        };

        /**
         * New event account
         */
        vm.newSocialpriority = function () {

            vm.socialpriority.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_socialpriority_modal.html',
                scope: $scope,
                backdrop: 'static', // khóa ko cho đóng 
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (!vm.socialpriority.code || vm.socialpriority.code.trim() == '') {
                        toastr.error('Vui lòng nhập mã phần tử lương.', 'Lỗi');
                        return;
                    }

                    if (!vm.socialpriority.name || vm.socialpriority.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên phần tử lương.', 'Lỗi');
                        return;
                    }
                    
                    service.saveSocialpriority(vm.socialpriority, function success() {

                        // Refresh list
                        vm.getSocialpriorityes();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một tài khoản.', 'Thông báo');

                        // clear object
                        vm.socialpriority = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một tài khoản.', 'Thông báo');
                    });
                }
            }, function () {
                vm.socialpriority = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editSocialpriority = function (socialpriorityId) {
            service.getSocialpriority(socialpriorityId).then(function (data) {

                vm.socialpriority = data;
                vm.socialpriority.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_socialpriority_modal.html',
                    scope: $scope,
                    size: 'md'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (!vm.socialpriority.code || vm.socialpriority.code.trim() == '') {
                            toastr.error('Vui lòng nhập mã phần tử lương.', 'Lỗi');
                            return;
                        }

                        if (!vm.socialpriority.name || vm.socialpriority.name.trim() == '') {
                            toastr.error('Vui lòng nhập tên phần tử lương.', 'Lỗi');
                            return;
                        }

                        service.saveSocialpriority(vm.socialpriority, function success() {

                            // Refresh list
                            vm.getSocialpriorityes();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object
                            vm.socialpriority = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin tài khoản.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.socialpriority = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        /**
         * Delete accounts
         */
        vm.deleteSocialpriorityes = function () {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                	console.log(vm.selectedSocialpriorityes);
                    service.deleteSocialpriorityes(vm.selectedSocialpriorityes, function success() {

                        // Refresh list
                        vm.getSocialpriorityes();

                        // Notify
                        toastr.info('Bạn đã xóa thành công ' + vm.selectedSocialpriorityes.length + ' bản ghi.', 'Thông báo');

                        // Clear selected accounts
                        vm.selectedSocialpriorityes = [];
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