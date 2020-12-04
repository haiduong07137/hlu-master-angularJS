/**
 * Created by nguyen the dat on 23/4/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.AgreementType').controller('AgreementTypeController', AgreementTypeController);

    AgreementTypeController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'AgreementTypeService'
    ];

    function AgreementTypeController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        var vm = this;

        vm.agreementtype= {};
        vm.agreementtypes = [];
        vm.selectedAgreementTypes = [];

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
        
        vm.getAgreementTypes = function () {
            service.getAgreementTypes(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.agreementtypes = data.content;
                vm.bsTableControl.options.data = vm.agreementtypes;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        };

        vm.getAgreementTypes();

        vm.bsTableControl = {
            options: {
                data: vm.agreementtypes,
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
                        vm.selectedAgreementTypes.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedAgreementTypes = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedAgreementTypes.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedAgreementTypes = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    vm.getAgreementTypes();
                }
            }
        };

        /**
         * New event account
         */
        vm.newAgreementType = function () {

            vm.agreementtype.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_agreementtype_modal.html',
                scope: $scope,
                backdrop: 'static', // khóa ko cho đóng 
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (!vm.agreementtype.code || vm.agreementtype.code.trim() == '') {
                        toastr.error('Vui lòng nhập mã phần tử lương.', 'Lỗi');
                        return;
                    }

                    if (!vm.agreementtype.name || vm.agreementtype.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên phần tử lương.', 'Lỗi');
                        return;
                    }
                    
                    service.saveAgreementType(vm.agreementtype, function success() {

                        // Refresh list
                        vm.getAgreementTypes();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một tài khoản.', 'Thông báo');

                        // clear object
                        vm.agreementtype = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một tài khoản.', 'Thông báo');
                    });
                }
            }, function () {
                vm.agreementtype = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editAgreementType = function (agreementtypeId) {
            service.getAgreementType(agreementtypeId).then(function (data) {

                vm.agreementtype = data;
                vm.agreementtype.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_agreementtype_modal.html',
                    scope: $scope,
                    size: 'md'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (!vm.agreementtype.code || vm.agreementtype.code.trim() == '') {
                            toastr.error('Vui lòng nhập mã phần tử lương.', 'Lỗi');
                            return;
                        }

                        if (!vm.agreementtype.name || vm.agreementtype.name.trim() == '') {
                            toastr.error('Vui lòng nhập tên phần tử lương.', 'Lỗi');
                            return;
                        }

                        service.saveAgreementType(vm.agreementtype, function success() {

                            // Refresh list
                            vm.getAgreementTypes();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object
                            vm.agreementtype = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin tài khoản.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.agreementtype = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        /**
         * Delete accounts
         */
        vm.deleteAgreementTypes = function () {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                	console.log(vm.selectedAgreementTypes);
                    service.deleteAgreementTypes(vm.selectedAgreementTypes, function success() {

                        // Refresh list
                        vm.getAgreementTypes();

                        // Notify
                        toastr.info('Bạn đã xóa thành công ' + vm.selectedAgreementTypes.length + ' bản ghi.', 'Thông báo');

                        // Clear selected accounts
                        vm.selectedAgreementTypes = [];
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