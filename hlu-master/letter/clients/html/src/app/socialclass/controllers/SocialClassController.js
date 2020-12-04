/**
 * Created by nguyen the dat on 23/4/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.SocialClass').controller('SocialClassController', SocialClassController);

    SocialClassController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'SocialClassService'
    ];

    function SocialClassController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        var vm = this;

        vm.socialclass = {};
        vm.socialclasss = [];
        vm.selectedSocialClasss = [];

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
        
        vm.getSocialClasss = function () {
            service.getSocialClasss(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.socialclasss = data.content;
                vm.bsTableControl.options.data = vm.socialclasss;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        };

        vm.getSocialClasss();

        vm.bsTableControl = {
            options: {
                data: vm.socialclasss,
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
                        vm.selectedSocialClasss.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedSocialClasss = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedSocialClasss.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedSocialClasss = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    vm.getSocialclasses();
                }
            }
        };

        /**
         * New event account
         */
        vm.newSocialClass = function () {

            vm.socialclass.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_socialclass_modal.html',
                scope: $scope,
                backdrop: 'static', // khóa ko cho đóng 
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (!vm.socialclass.code || vm.socialclass.code.trim() == '') {
                        toastr.error('Vui lòng nhập mã phần tử lương.', 'Lỗi');
                        return;
                    }

                    if (!vm.socialclass.name || vm.socialclass.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên phần tử lương.', 'Lỗi');
                        return;
                    }
                    
                    service.saveSocialClass(vm.socialclass, function success() {

                        // Refresh list
                        vm.getSocialClasss();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một tài khoản.', 'Thông báo');

                        // clear object
                        vm.socialclass = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một tài khoản.', 'Thông báo');
                    });
                }
            }, function () {
                vm.socialclass = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editSocialClass = function (socialclassId) {
            service.getSocialClass(socialclassId).then(function (data) {

                vm.socialclass = data;
                vm.socialclass.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_socialclass_modal.html',
                    scope: $scope,
                    size: 'md'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (!vm.socialclass.code || vm.socialclass.code.trim() == '') {
                            toastr.error('Vui lòng nhập mã thành phần gia đình.', 'Lỗi');
                            return;
                        }

                        if (!vm.socialclass.name || vm.socialclass.name.trim() == '') {
                            toastr.error('Vui lòng nhập tên thành phần gia đình.', 'Lỗi');
                            return;
                        }

                        service.saveSocialClass(vm.socialclass, function success() {

                            // Refresh list
                            vm.getSocialClasss();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object
                            vm.socialclass = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin tài khoản.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.socialclass = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        /**
         * Delete accounts
         */
        vm.deleteSocialClasss = function () {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                	console.log(vm.selectedSocialClasss);
                    service.deleteSocialClasss(vm.selectedSocialClasss, function success() {

                        // Refresh list
                        vm.getSocialClasss();

                        // Notify
                        toastr.info('Bạn đã xóa thành công ' + vm.selectedSocialClasss.length + ' bản ghi.', 'Thông báo');

                        // Clear selected accounts
                        vm.selectedSocialClasss = [];
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