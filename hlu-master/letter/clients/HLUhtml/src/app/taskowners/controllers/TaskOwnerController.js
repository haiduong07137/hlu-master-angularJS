/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.TaskOwner').controller('TaskOwnerController', TaskOwnerController);

    TaskOwnerController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'TaskOwnerService'
    ];

    function TaskOwnerController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        var vm = this;
        vm.taskOwner = {};
        vm.taskOwners = [];
        vm.userTaskOwner = {};
        vm.selectTaskOwner = {};
        vm.selectedTaskOwners = [];
        vm.pageIndexUser = 0;
        vm.pageSizeUser = 10;
        
        vm.AllDepartments = [];// tất cả phòng ban

        vm.person = {};//thành viên tham gia
        vm.AllPersons = [];// tất cả thành viên
        vm.displayName = '';
        vm.id='';
        
        vm.pageIndex = 1;
        vm.pageSize = 25;

        vm.searchUserIndex = 0;
        vm.searchUserPage = 10;

        /*
         * Viết thêm Vị trí công việc
         */
        vm.typeOption = [
            {
                id: 0,
                name: 'Phòng ban'
            },
            {
                id: 1,
                name: 'Cá nhân'
            },
            {
                id: 2,
                name: 'Khác'
            }
        ]

        vm.getTaskOwners = function () {
            service.getTaskOwners(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.taskOwners = data.content;
                vm.bsTableControl.options.data = vm.taskOwners;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        };

        vm.getTaskOwners();

            //Get list person
        vm.getListPerson = function () {
            service.getListPerson(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.AllPersons = data.content;
                // vm.bsTableControlPersons.options.data = vm.AllPersons;
                // vm.bsTableControlPersons.options.totalRows = data.totalElements;
            });
        };

        vm.getListPerson();
        //get list department
        vm.getDepartments = function () {
            service.getListDepartments(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.AllDepartments = data.content;
                // vm.bsTableControlDepartments.options.data = vm.AllDepartments;
                // vm.bsTableControlDepartments.options.totalRows = data.totalElements;
            });
        };
        vm.getDepartments();

        vm.bsTableControl = {
            options: {
                data: vm.taskOwners,
                idField: 'id',
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: true,
                showToggle: true,
                pagination: true,
                singleSelect: true,
                pageSize: vm.pageSize,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: service.getTableDefinition(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectTaskOwner = row;
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedTaskOwners = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    vm.selectTaskOwner = {};
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedTaskOwners = [];
                    });
                },
                onPageChange: function (pageIndex, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = pageIndex;
                    vm.getTaskOwners();
                }
            }
        };

        /*
        * add New task owner
        */
        vm.newTaskOwner = function () {
            vm.taskOwner={};
            vm.taskOwner.isNew = true;
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_taskowner_item_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'lg'
            });

            vm.CheckResult =function (){
                    if (!vm.taskOwner.displayName || vm.taskOwner.displayName.trim() == '') {
                        toastr.error('Vui lòng nhập tên vị trí công việc.', 'Lỗi');
                        return;
                    }
                    if (vm.taskOwner.ownerType == null) {
                        toastr.error('Vui lòng chọn loại vị trí công việc.', 'Lỗi');
                        return;
                    }
                    
                    if (vm.taskOwner.ownerType == 1) {
                        if (vm.taskOwner.person == null) {
                            toastr.error('Vui lòng chọn thành viên tham gia vị trí công việc.', 'Lỗi');
                            return;
                        }
                    }
                    if (vm.taskOwner.ownerType == 0) {
                        if (vm.taskOwner.department == null) {
                            toastr.error('Vui lòng chọn phòng ban tham gia vị trí công việc.', 'Lỗi');
                            return;
                        }
                    }
                    service.saveTaskOwner(vm.taskOwner, function success() {

                        // Refresh list
                        vm.getTaskOwners();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một vị trí công việc.', 'Thông báo');

                        // clear object
                        vm.taskOwner = {};
                        modalInstance.close();
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một vị trí công việc.', 'Thông báo');
                    });
                }
            }, function () {
                vm.taskOwner = {};
                console.log('Modal dismissed at: ' + new Date());
        };

        /**
         * Edit a Task Owner
         *
         */
        
        function getTaskOwner(id) {
            service.getTaskOwner(id).then(function (data) {
                vm.taskOwner = data;
                vm.userTaskOwner = {};
                vm.userTaskOwner.taskOwner = data;
                console.log(data);
            });
        }
        
        $scope.editTaskOwner = function (id) {
            service.getTaskOwner(id).then(function (data) {
                vm.taskOwner = data;
                vm.person = vm.taskOwner.person;
                vm.staffId = id;
                vm.taskOwner.isNew = false;
                vm.addNew = true;
                var modalInstance = modal.open({
                    animation: true,
                    backdrop: 'static',
                    templateUrl: 'edit_taskowner_item_modal.html',
                    scope: $scope,
                    size: 'lg'
                });

                vm.CheckResult= function () {
                    if (!vm.taskOwner.displayName || vm.taskOwner.displayName.trim() == '') {
                        toastr.error('Vui lòng nhập tên vị trí công việc.', 'Lỗi');
                        return;
                    }
                    if (vm.taskOwner.ownerType == null) {
                        toastr.error('Vui lòng chọn loại vị trí công việc.', 'Lỗi');
                        return;
                    }
                    
                    if (vm.taskOwner.ownerType == 1) {
                        if (vm.taskOwner.person == null) {
                            toastr.error('Vui lòng chọn thành viên tham gia vị trí công việc.', 'Lỗi');
                            return;
                        }
                    }
                    if (vm.taskOwner.ownerType == 0) {
                        if (vm.taskOwner.department == null) {
                            toastr.error('Vui lòng chọn phòng ban tham gia vị trí công việc.', 'Lỗi');
                            return;
                        }
                    }
                    service.saveTaskOwner(vm.taskOwner, function success() {

                        // Refresh list
                        vm.getTaskOwners();

                        // Notify
                        toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                        // clear object
                        vm.taskOwner = {};
                        modalInstance.close();
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi lưu thông tin vị trí công việc.', 'Lỗi');
                    });
                }
            }, function () {
                vm.taskOwner = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };
        vm.deleteUserTaskOwner = function (id) {
            service.deleteUserTaskOwner(id, function success() {
            	getTaskOwner(vm.staffId);
                toastr.info('Bạn đã xóa thành công.', 'Thông báo');
            }, function failure() {
                toastr.error('Có lỗi xảy ra.', 'Lỗi');
            });
        };        
        vm.saveUserTaskOwner = function (addNew) {
        	vm.userTaskOwner.taskOwner = vm.taskOwner;
        	if (vm.userTaskOwner.user == null) {
        		vm.userTaskOwner.user = vm.staff.user;
			}
            if (vm.userTaskOwner.role == null) {
                toastr.warning('Mời chọn role');
                return;
            }
            if (vm.userTaskOwner.user == null) {
                /*toastr.warning('Mời chọn user');*/
            	toastr.warning('Không tìm thấy user của nhân viên cần phân quyền, vui lòng tải lại trang.');
                return;
            }
            if (!addNew) {
                vm.addNew = true;
                //check trùng
                var dup = false;
                if (vm.taskOwner != null && vm.taskOwner.userTaskOwners != null && vm.taskOwner.userTaskOwners.length > 0) {
                    for (var i = 0; i < vm.taskOwner.userTaskOwners.length; i++) {
                        if (vm.taskOwner.userTaskOwners[i].user != null && vm.taskOwner.userTaskOwners[i].role != null) {
                            if (vm.userTaskOwner.user.id == vm.taskOwner.userTaskOwners[i].user.id && vm.userTaskOwner.role.id == vm.taskOwner.userTaskOwners[i].role.id) {
                                dup = true;
                                toastr.warning('Đã tồn tại role ứng với username');
                            }
                        }
                    }
                }
                if (!dup) {
                    //gán giá trị

                    //Sửa
                    service.saveUserTaskOwner(vm.userTaskOwner, function success() {
                        getTaskOwnerFromPerson(vm.staffId);
                        toastr.info('Bạn đã tạo thành công.', 'Thông báo');
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra.', 'Lỗi');
                    });
                }


            }
            if (addNew) {
                //Check trùng
                var dup = false;
                if (vm.taskOwner != null && vm.taskOwner.userTaskOwners != null && vm.taskOwner.userTaskOwners.length > 0) {
                    for (var i = 0; i < vm.taskOwner.userTaskOwners.length; i++) {
                        if (vm.taskOwner.userTaskOwners[i].user != null && vm.taskOwner.userTaskOwners[i].role != null) {
                            if (vm.userTaskOwner.user.id == vm.taskOwner.userTaskOwners[i].user.id && vm.userTaskOwner.role.id == vm.taskOwner.userTaskOwners[i].role.id) {
                                dup = true;
                                toastr.warning('Đã tồn tại role ứng với username');
                            }
                        }
                    }
                }
                if (!dup) {
                    //gán giá trị

                    //Thêm mới
                    service.saveUserTaskOwner(vm.userTaskOwner, function success() {
                    	getTaskOwner(vm.staffId);
                        toastr.info('Bạn đã tạo thành công.', 'Thông báo');
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra.', 'Lỗi');
                    });
                }
            }
        };
        function getTaskRoles() {
            service.getTaskRoles().then(function (data) {
                vm.taskRoles = data.content;
            });
        }
        getTaskRoles();

        vm.enterSearchUser = function () {
			console.log(event.keyCode);
			vm.pageIndexUser = 0;
			if (event.keyCode == 13) {//Phím Enter
				vm.searchByUsername();
			}
		};
        
        vm.searchByUsername = function () {
            if (vm.textSearchUser != "" && vm.textSearchUser != null) {
                service.findUserByUserName(vm.textSearchUser, vm.pageIndexUser, vm.pageSizeUser).then(function (data) {
                    vm.users = data.content;
                    vm.totalItemsUser = data.totalElements;
                });
            }
            if (vm.textSearchUser == "" || vm.textSearchUser == null) {
                service.getUsers(vm.pageIndexUser, vm.pageSizeUser).then(function (data) {
                    vm.users = data.content;
                    vm.totalItemsUser = data.totalElements;
                    console.log(data);
                });
            }
        }; 
        var modalInstanceUser;
        vm.pageChange = function() {
        	if (vm.textSearchUser != "" && vm.textSearchUser != null) {
                service.findUserByUserName(vm.textSearchUser, vm.pageIndexUser, vm.pageSizeUser).then(function (data) {
                    vm.users = data.content;
                    vm.totalItemsUser = data.totalElements;
                });
            }
            if (vm.textSearchUser == "" || vm.textSearchUser == null) {
                service.getUsers(vm.pageIndexUser, vm.pageSizeUser).then(function (data) {
                    vm.users = data.content;
                    vm.totalItemsUser = data.totalElements;
                    console.log(data);
                });
            }
		}
        vm.selectUser = function () {
        	vm.textSearchUser = "";
            modalInstanceUser = modal.open({
                animation: true,
                templateUrl: 'select_user_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'md'
            });

            service.getUsers(vm.pageIndexUser, vm.pageSizeUser).then(function (data) {
                vm.users = data.content;
                vm.totalItemsUser = data.totalElements;
            });

            modalInstanceUser.result.then(function (confirm) {
                if (confirm == 'yes') {
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };
        
        vm.userSelected = function (user) {
            vm.userTaskOwner.user = user;
            modalInstanceUser.close();
        };
        /**
         * delete TaskOwner
         */

        $scope.deleteTaskOwnerById = function (id) {
            var modalInstance = modal.open({
                animation: true,
                backdrop: 'static',
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    // console.log(vm.selectedTaskOwners);
                    service.deleteTaskOwnerById(id, function success() {

                        // Refresh list
                        vm.getTaskOwners();

                        // Notify
                        toastr.info('Bạn đã xóa thành công bản ghi.', 'Thông báo');

                        // Clear selected accounts
                        vm.selectedTaskOwners = [];
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