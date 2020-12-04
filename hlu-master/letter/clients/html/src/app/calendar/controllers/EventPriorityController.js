/**
 * Created by bizic on 28/8/2016.
 */
(function () {
    'use strict';

    angular.module('Hrm.Calendar').controller('EventPriorityController', EventPriorityController);

    EventPriorityController.$inject = [
        '$rootScope',
        '$scope',
        '$timeout',
        'settings',
        '$uibModal',
        'toastr',
        'blockUI',
        'bsTableAPI',
        'Utilities',
        'focus',
        'EventPriorityService'
    ];

    function EventPriorityController($rootScope, $scope, $timeout, settings, modal, toastr, blockUI, bsTableAPI, utils, focus, service) {
    	$scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        var vm = this;
        vm.isChangePassword = false;
        vm.priority = {};
        vm.priorities = [];
        vm.selectedPriorities = [];
        vm.pageIndex = 1;
        vm.pageSize = 25;
        
        vm.getPriorities = function () {
            service.getPriorities(vm.pageSize,vm.pageIndex).then(function (data) {
                vm.priorities = data.content;
                vm.bsTableControl.options.data = vm.priorities;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        };

        vm.getPriorities();

        vm.bsTableControl = {
            options: {
                data: vm.priorities,
                idField: 'id',
//              sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: false,
                showToggle: false,
                pagination: true,
                pageSize: vm.pageSize,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: service.getTableDefinition(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedPriorities.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedPriorities = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedPriorities);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedPriorities.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedPriorities = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;

                    vm.getPriorities();
                }
            }
        };

        /**
         * New event account
         */
        vm.pressEnterKey = function($event,func,agur=null){
            var keyCode = $event.which || $event.keyCode;
            if (keyCode === 13) {
                // Do that thing you finally wanted to do
            	if(agur==null){
            		func();
            	}
            	else{
            		func(agur); 
            	}
            }
        }
        vm.isAdd = false;
    	vm.isEdit = false;
    	vm.isView = false;
        vm.checkViewUserTab = function(isAdd,isEdit,isView){
        	vm.isAdd = isAdd;
        	vm.isEdit = isEdit;
        	vm.isView = isView;
        }
        
        vm.newPriority = function () {
        	vm.priority={};	
        	vm.checkViewUserTab(true,false,false);
            vm.priority.isNew = true;
//            vm.isChangePassword = true;
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_document_modal.html',
                scope: $scope,
                size: 'md'
            });
            
            vm.Save=function (confirm) {
            	
                if (confirm == 'yes') {

                    if (!vm.priority.name || vm.priority.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên.', 'Lỗi');
                        return;
                    }
                    
                    if (vm.priority.priority ==null) {
                        toastr.error('Vui lòng nhập thứ tự ưu tiên.', 'Lỗi');
                        return;
                    }
                    service.savePriority(vm.priority, function success() {

                        // Refresh list
                        vm.getPriorities();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một sự kiện.', 'Thông báo');

                        // clear object
                        vm.priority = {};
                        modalInstance.close();
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một sự kiện.', 'Thông báo');
                    });
                }
            }, function () {
                vm.priority = {};
                console.log('Modal dismissed at: ' + new Date());
            };
//            vm.isChangePassword = false;
        };

        /**
         * Edit a account
         * @param accountId
         */

        $scope.editPriority = function (PriorityId) {
        	vm.checkViewUserTab(false,true,false);
        	
            service.getPriority(PriorityId).then(function (data) {

                vm.priority = data;
                console.log(data);
                vm.priority.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_document_modal.html',
                    scope: $scope,
                    size: 'md'
                });

               vm.Save=function (confirm) {
                    if (confirm == 'yes') {

                    	if (!vm.priority.name || vm.priority.name.trim() == '') {
                            toastr.error('Vui lòng nhập tên.', 'Lỗi');
                            return;
                        }

                        if (vm.priority.priority ==null) {
                            toastr.error('Vui lòng nhập thứ tự ưu tiên.', 'Lỗi');
                            return;
                        }
                       
                        service.updatePriority(PriorityId,vm.priority, function success() {
                            // Refresh list
                            vm.getPriorities();
                            // Notify
                            toastr.info('Bạn đã sửa thành công.', 'Thông báo');
                            // clear object
                            vm.priority = {};
                            modalInstance.close();
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi sửa thông tin sự kiện.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.priority = {};
                    console.log('Modal dismissed at: ' + new Date());
                };
            });
        };
     
       
        vm.deletePriorities = function () {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    service.deletePriorities(vm.selectedPriorities, function success() {

                        // Refresh list
                        vm.getPriorities();

                        // Notify
                        toastr.info('Bạn đã xóa thành công ' + vm.selectedPriorities.length + ' bản ghi.', 'Thông báo');

                        // Clear selected accounts
                        vm.selectedPriorities = [];
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi xóa bản ghi.', 'Lỗi');
                    });
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };

       

        //search Document
        function searchPriority(textSearch, pageIndex, pageSize) {
            service.searchPriority(textSearch,pageIndex, pageSize).then(function (data) {
                vm.priority = data.content;
                console.log(data);
                vm.bsTableControl.options.data = vm.priority;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        }
        
        vm.textSearch = '';
        vm.searchByCode = function () {
            vm.textSearch = String(vm.textSearch).trim();
            if(vm.textSearch != ''){
                searchPriority(vm.textSearch,vm.pageIndex,vm.pageSize);
            }
            if(vm.textSearch == ''){
                vm.getPriorities();
            }
        }
 }

})();