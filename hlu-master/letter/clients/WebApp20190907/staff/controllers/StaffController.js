/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.Staff').controller('StaffController', StaffController);

    StaffController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'StaffService',
        'Upload',
        'PositionTitleService',
        'DepartmentService',
        'SocialClassService',
        'AgreementService',
        'AgreementTypeService',
        'EducationService',
        'FamilyRelationshipService'
    ];

    function StaffController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service, Upload, positionTitleService, departmentService, SocialClassService, agreementService, AgreementTypeService, EducationService, FamilyRelationshipService) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });
        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        $scope.ac = function () {
            return true;
        };

        var vm = this;
        vm.roles = [];
        vm.countries = [];
        vm.provinces = [];
        vm.cities = [];
        vm.subjects = [];
        vm.ethnics = [];
        vm.ethnic = null;
        vm.religions = [];
        vm.religion = null;

        vm.socialClasss = [];
        vm.socialClass = null;
        vm.roles = [];
        vm.role = null;
        $scope.search = '';
        //TASK OWNER//
        vm.taskOwner = {};
        vm.taskRoles = [];
        vm.person = {};
        vm.userTaskOwners = {};
        vm.userTaskOwner = {};

        vm.addNew = true;
        vm.objects = [];
        vm.object = null;

        vm.treeData = [];
        //staff position
        vm.positionTitles = [];
        vm.positionTitle = null;

        vm.positions = [];

        vm.departments = [];
        vm.department = null;

        vm.staffPosition = {};
        vm.staffPositions = [];

        vm.staffSearchDto = {};
        vm.baseUrl = settings.api.baseUrl + settings.api.apiV1Url;


        //agreement
        vm.agreement = {}; // 1 biến
        vm.agreements = []; // mảng

        //labourAgreementType // loại hợp đồng
        vm.labourAgreementType = {};
        vm.labourAgreementTypes = [];

        //educationHistory // quá trình công tác
        vm.education = {};
        vm.educations = [];

        //familyRelationship // tiểu sử gia đình
        vm.familyRelationship = {},
            vm.familyRelationships = [];
        vm.seletedFamilys = [];

        vm.validate = validate;

        $scope.treeInstance = null;
        $scope.treeConfig = {
            core: {
                error: function (error) {
                    $log.error('treeCtrl: error from js tree - ' + angular.toJson(error));
                },
                check_callback: true
            },
            plugins: ['types', 'state', 'search']
        };

        vm.isChangePassword = false;
        vm.staff = {};
        vm.staffs = [];
        vm.selectedStaffs = [];
        vm.gender = null;
        vm.genders = [
            { value: 'M', name: 'Nam' },
            { value: 'F', name: 'Nữ' },
            { value: 'U', name: 'Khác' }
        ];
        vm.maritalStatuss = [
            { value: 0, name: 'Độc thân' },
            { value: 1, name: 'Đã kết hôn' },
            { value: 2, name: 'Khác' }
        ];

        //agreement
        vm.selectedAgreements = [];
        vm.status = [
            { value: 1, name: 'Hiện thời' },
            { value: -1, name: 'Đã kết thúc' },
            { value: -2, name: 'Chắm dứt hợp đồng' }
        ]

        //educationHistory
        vm.selectedEducations = [];
        vm.educationStatus = [
            { value: 1, name: 'Hiện thời' },
            { value: 2, name: 'Kết thúc' }
        ]

        vm.pageIndex = 1;
        vm.pageSize = 25;
        $scope.selectNode = function (node, selected, event) {
            if (vm.staffSearchDto.department == null) {
                vm.staffSearchDto.department = {};
            }
            vm.staffSearchDto.department.id = selected.node.id;
            vm.searchByDto();
        }
        $scope.applySearch = function () {
            var to = false;
            if (to) {
                clearTimeout(to);
            }
            to = setTimeout(function () {
                if ($scope.treeInstance) {
                    //console.log('here');
                    $scope.treeInstance.jstree(true).search($scope.search);
                }
            }, 250);
        };

        function getDepartment() {
            //vm.treeData = [];
            service.getDepartmentTree().then(function (data) {
                vm.treeData = data;
                $scope.treeConfig.version++;
            });

            // vm.treeData=[{ id : "1", parent : "#", text : 'Main'},{ id : "2", parent : "#", text : 'Level 1'}];

            //        vm.treeData.push({ id : "2", parent : "1", text : 'Level 1'});
            //        vm.treeData.push({ id : "7", parent : "3", text : 'Missing Level 3'});
            //        vm.treeData.push({ id : "3", parent : "2", text : 'Level 2'});
        }

        function getEthnics() {
            service.getEthnics().then(function (data) {
                vm.ethnics = data.content;
                //console.log(data);
            });
        }

        function getReligions() {
            service.getReligions().then(function (data) {
                vm.religions = data.content;
                //console.log(data);
            });
        }

        function getSocialClasss() {
            SocialClassService.getSocialClasss(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.socialClasss = data.content;
            })
        }

        function getAgreementTypes() {
            AgreementTypeService.getAgreementTypes(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.getAgreementTypes = data.content;
            })
        }


        vm.labourAgreementTypes = function () {
            AgreementTypeService.getAgreementTypes(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.labourAgreementTypes = data.content;

            })
        }



        function getRoles() {
            service.getRoles().then(function (data) {
                vm.roles = data.content;
                //console.log(data);
            });
        }

        vm.getStaffs = function () {
            service.getStaffs(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.staffs = data.content;
                vm.bsTableControl.options.data = vm.staffs;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        };

        vm.nameChange = function () {
            if (vm.staff != null) {
                if (!angular.isUndefined(vm.staff.lastName)) {
                    vm.staff.displayName = vm.staff.lastName;
                }
                if (!angular.isUndefined(vm.staff.firstName)) {
                    vm.staff.displayName = vm.staff.lastName + ' ' + vm.staff.firstName;
                }
                if (vm.staff.lastName == '') {
                    vm.staff.displayName = vm.staff.displayName.trim();
                }
                if (vm.staff.firstName == '') {
                    vm.staff.displayName = vm.staff.displayName.trim();
                }
                /*if(!angular.isUndefined(vm.staff.studentCode)){
                 if(vm.staff.user == null){
                 vm.staff.user = {};
                 }
                 vm.staff.user.username = vm.staff.studentCode;
                 }*/
            }
        }
        getDepartment();
        getEthnics();
        getReligions();
        getSocialClasss();
        getAgreementTypes();
        vm.labourAgreementTypes();
        getRoles();



        vm.getStaffs();

        vm.bsTableControl = {
            options: {
                data: vm.staffs,
                idField: 'id',
                sortable: true,
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
                        vm.selectedStaffs.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedStaffs = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedStaffs);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedStaffs.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedStaffs = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;

                    vm.searchByDtoPageChane();
                }
            }
        };
        vm.searchByDtoPageChane = function () {
            service.searchDto(vm.staffSearchDto, vm.pageIndex, vm.pageSize).then(function (data) {
                vm.staffs = data.content;
                vm.bsTableControl.options.data = vm.staffs;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        };
        /**
         * New event account
         */
        vm.isAdd = false;
        vm.isEdit = false;
        vm.isView = false;
        vm.checkViewUserTab = function (isAdd, isEdit, isView) {
            vm.isAdd = isAdd;
            vm.isEdit = isEdit;
            vm.isView = isView;
        }
        vm.newStaff = function () {
            vm.checkViewUserTab(true, false, false);
            vm.staff.isNew = true;
            vm.agreement = {};
            vm.agreements = [];
            vm.education = {};
            vm.educations = [];
            //            vm.isChangePassword = true;
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_staff_modal.html',
                scope: $scope,
                backdrop: 'static', // khóa ko cho đóng 
                size: 'lg'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (!vm.staff.staffCode || vm.staff.staffCode.trim() == '') {
                        toastr.error('Vui lòng nhập mã công chức.', 'Lỗi');
                        return;
                    }
                    if(vm.validate()){
                        return;
                    }
                    // if (!vm.staff.displayName || vm.staff.displayName.trim() == '') {
                    //     toastr.error('Vui lòng nhập tên chức vụ.', 'Lỗi');
                    //     return;
                    // }
                    vm.staff.agreements = vm.agreements;
                    vm.staff.educations = vm.educations;
                    vm.staff.familyRelationships = vm.familyRelationships;
                    service.saveStaff(vm.staff, function success() {
                        // Refresh list
                        //alert('abc  ')
                        vm.searchByDto();
                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một tài khoản.', 'Thông báo');
                        // clear object
                        vm.staff = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một tài khoản.', 'Thông báo');
                    });
                }
            }, function () {
                vm.staff = {};
                console.log('Modal dismissed at: ' + new Date());
            });
            //            vm.isChangePassword = false;
        };

        /**
         * Edit a account
         * @param accountId
         */
        vm.staffId = 0;
        $scope.editStaff = function (staffId) {
            vm.checkViewUserTab(false, true, false);
            vm.staff.isNew = false;

            vm.agreement = {};
            vm.agreements = [];
            vm.education = {};
            vm.educations = [];
            vm.familyRelationship = {};
            vm.familyRelationships = [];

            service.getStaff(staffId).then(function (data) {
                vm.staff = data;
                //console.log(data);
                vm.staff.isNew = false;

                vm.staffId = staffId;

                getTaskOwnerFromPerson(staffId);
                //agreementService.getAgreementById();
                //EducationService.getEducationHistoryById();
                //vm.getPages(vm.pageIndex, vm.pageSize);
                vm.getAll(staffId);
                service.getPersonByStaff(staffId).then(function (data) {
                    vm.person = data;
                });

                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_staff_modal.html',
                    scope: $scope,
                    backdrop: 'static',
                    size: 'lg'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (!vm.staff.staffCode || vm.staff.staffCode.trim() == '') {
                            toastr.error('Vui lòng nhập mã chức vụ.', 'Lỗi');
                            return;
                        }
                        if(vm.validate()){
                            return;
                        };
                        service.updateStaff(staffId, vm.staff, function success() {
                            // Refresh list
                            vm.searchByDto();
                            // Notify
                            vm.agreementListDelete.forEach(element => {

                                agreementService.removeAgreement(element).then(function (data) {
                                    toastr.info('Bạn đã xóa thành công ' + vm.selectedAgreements.length + ' bản ghi.', 'Thông báo');
                                })
                            })

                            vm.educationHistoryListDelete.forEach(element => {

                                EducationService.removeEducationHistory(element).then(function (data) {
                                    toastr.info('Bạn đã xóa thành công ' + vm.selectedEducations.length + ' bản ghi.', 'Thông báo');
                                })
                            })


                            vm.familyRelationshipListDelete.forEach(element => {
                                FamilyRelationshipService.removeFamilyRelationship(element).then(function (data) {
                                    toastr.info('Bạn đã xóa thành công' + vm.seletedFamilys.length + 'bản ghi', 'Thông báo');
                                })
                            })
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');
                            // clear object
                            vm.staff = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin tài khoản.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.staff = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };
        vm.saveStaff = function () {
            alert('Save Staff');
        }
        /**
         * Delete accounts
         */
        vm.deleteStaffs = function () {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    service.deleteStaffs(vm.selectedStaffs, function success() {

                        // Refresh list
                        vm.getStaffs();

                        // Notify
                        toastr.info('Bạn đã xóa thành công ' + vm.selectedStaffs.length + ' bản ghi.', 'Thông báo');

                        // Clear selected accounts
                        vm.selectedStaffs = [];
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi xóa bản ghi.', 'Lỗi');
                    });
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        //// Upload file
        $scope.MAX_FILE_SIZE = '2MB';
        $scope.f = null;
        $scope.errFile = null;

        $scope.uploadFiles = function (file, errFiles) {
            $scope.f = file;
            $scope.errFile = errFiles && errFiles[0];
        };



        vm.startUploadFile = function (file) {
            if (file) {
                file.upload = Upload.upload({
                    url: vm.baseUrl + 'hr/file/importStaff',
                    data: { uploadfile: file }
                });

                file.upload.then(function (response) {
                    file.result = response.data;
                    vm.getStaffs();
                    toastr.info('Import thành công.', 'Thông báo');
                }, function errorCallback(response) {
                    toastr.error('Import lỗi.', 'Lỗi');
                });
            }
        };

        vm.importStaff = function () {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'import_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'md'
            });

            vm.student = {};
            $scope.f = null;
            $scope.errFile = null;

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    vm.startUploadFile($scope.f);
                }
            }, function () {
                vm.educationProgram = null;
                vm.address = {};
            });
        }

        //search staff
        function getStaffByCode(textSearch, pageIndex, pageSize) {
            service.getStaffByCode(textSearch, pageIndex, pageSize).then(function (data) {
                vm.staffs = data.content;
                vm.bsTableControl.options.data = vm.staffs;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        }

        vm.textSearch = '';
        vm.searchByCode = function () {
            vm.textSearch = String(vm.textSearch).trim();
            if (vm.textSearch != '') {
                getStaffByCode(vm.textSearch, vm.pageIndex, vm.pageSize);
            }
            if (vm.textSearch == '') {
                vm.getStaffs();
            }
        };

        vm.enterSearchCode = function () {
            vm.pageIndexUser = 0;
            if (event.keyCode == 13) {//Phím Enter
                vm.searchByCode();
            }
        };



        //        vm.positionTitleService = function () {
        //            positionTitleService.getpositiontitles(0, 10000).then(function (data) {
        //                vm.positionTitles = data.content;
        //                // console.log('here: ' + vm.positionTitles.length);
        //            });
        //        };
        //vm.positionTitleService();
        vm.getPositionTitle = function () {
            service.getPositionTitles().then(function (data) {
                vm.positionTitles = data.content;
//                 console.log('here: ' + vm.positionTitles.length);
            });
        }
        vm.getPositionTitle();
        vm.getPositions = function () {
            service.getPositions().then(function (data) {
                vm.positions = data.content;
                // console.log('here: ' + vm.positionTitles.length);
            });
        };
        vm.getPositions();

        vm.listAllDepartment = function () {
            departmentService.listAllDepartment().then(function (data) {
                vm.departments = data;
            });
        };
        vm.listAllDepartment();




        vm.addStaffPosition = function () {
            if (vm.staff.positions == null) {
                vm.staff.positions = [];
            }
            vm.staff.positions.push(vm.staffPosition);
            console.log(vm.staff.positions);
            vm.staffPosition = {};
        };

        vm.removeStaffPosition = function (index) {	
            vm.staff.positions.splice(index, 1);
        };

        vm.setMainPosition = function (index) {
            if (vm.staff.positions != null) {
                for (var i = 0; i < vm.staff.positions.length; i++) {
                    if (i != index) {
                        if (vm.staff.positions[index].mainPosition == true) {
                            vm.staff.positions[i].mainPosition = false;
                        }
                    }
                }
            }
        };

        vm.setCurrent = function (index) {
            if (vm.staff.positions != null) {
                for (var i = 0; i < vm.staff.positions.length; i++) {
                    if (i != index) {
                        if (vm.staff.positions[index].current == true) {
                            vm.staff.positions[i].current = false;
                        }
                    }
                }
            }
        };

        vm.searchByDto = function () {
            vm.pageIndex = 1;
            vm.bsTableControl.state.pageNumber = 1;
            service.searchDto(vm.staffSearchDto, vm.pageIndex, vm.pageSize).then(function (data) {
                vm.staffs = data.content;
                vm.bsTableControl.options.data = vm.staffs;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        };



        function getTaskRoles() {
            service.getTaskRoles().then(function (data) {
                vm.taskRoles = data.content;
            });
        }
        getTaskRoles();

        function getTaskOwnerFromPerson(personId) {
            service.getTaskOwnerFromPerson(personId).then(function (data) {
                vm.taskOwner = data;
                vm.userTaskOwner = {};
                vm.userTaskOwner.taskOwner = data;
            });
        }

        vm.createTaskOwner = function () {
            service.createTaskOwner(vm.person, function success() {
                getTaskOwnerFromPerson(vm.staffId);
                toastr.info('Bạn đã tạo thành công.', 'Thông báo');
            }, function failure() {
                toastr.error('Có lỗi xảy ra.', 'Lỗi');
            });
        };

        vm.saveUserTaskOwner = function (addNew) {
        	if (vm.userTaskOwner.user == null && vm.staff.user != null) {
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
                                toastr.warning('Đã tồn tại role ứng với username rồi');
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
                                toastr.warning('Đã tồn tại role ứng với username rồi');
                            }
                        }
                    }
                }
                if (!dup) {
                    //gán giá trị

                    //Thêm mới
                    service.saveUserTaskOwner(vm.userTaskOwner, function success() {
                        getTaskOwnerFromPerson(vm.staffId);
                        toastr.info('Bạn đã tạo thành công.', 'Thông báo');
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra.', 'Lỗi');
                    });
                }
            }
        };

        vm.editUserTaskOwner = function (userTaskOwner) {
            vm.addNew = false;
            vm.userTaskOwner = {};
            //gán giá trị
            vm.userTaskOwner.id = userTaskOwner.id;
            vm.userTaskOwner.user = userTaskOwner.user;
            vm.userTaskOwner.role = userTaskOwner.role;

        };

        vm.deleteUserTaskOwner = function (id) {
            service.deleteUserTaskOwner(id, function success() {
                getTaskOwnerFromPerson(vm.staffId);
                toastr.info('Bạn đã xóa thành công.', 'Thông báo');
            }, function failure() {
                toastr.error('Có lỗi xảy ra.', 'Lỗi');
            });
        };

        vm.cancelProcessUserTaskOwner = function () {
            vm.addNew = true;
            getTaskOwnerFromPerson(vm.staffId);
        };
        //---------//

        //SELECT USER//
        vm.users = [];
        vm.user = null;
        vm.textSearchUser = '';
        vm.pageIndexUser = 0;
        vm.tempIndex = 0;
        vm.pageSizeUser = 20;
        vm.totalItemsUser = 0;
        var modalInstanceUser;
        vm.selectUser = function () {
            modalInstanceUser = modal.open({
                animation: true,
                templateUrl: 'select_user_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'md'
            });

            vm.textSearchUser = '';
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


        vm.searchByUsername = function () {
            vm.textSearchUser = String(vm.textSearchUser);
            if (vm.textSearchUser != '' && vm.textSearchUser != null) {
                if (vm.pageIndexUser != 0) {
                    vm.tempIndex = vm.pageIndexUser - 1;
                }
                service.findUserByUserName(vm.textSearchUser, vm.tempIndex, vm.pageSizeUser).then(function (data) {
                    vm.users = data.content;
                    vm.totalItemsUser = data.totalElements;
                });
            }
            if (vm.textSearchUser == '' || vm.textSearchUser == null) {
                service.getUsers(vm.pageIndexUser, vm.pageSizeUser).then(function (data) {
                    vm.users = data.content;
                    vm.totalItemsUser = data.totalElements;
                });
            }
        };

        vm.enterUserSearchCode = function () {
            vm.pageIndexUser = 0;
            if (event.keyCode == 13) {//Phím Enter
                vm.searchByUsername();
            }
        };

        vm.userSelected = function (user) {
            vm.userTaskOwner.user = user;
            modalInstanceUser.close();
        };

        $scope.pageChanged = function () {
            vm.textSearchUser = String(vm.textSearchUser);
            if (vm.textSearchUser != '' && vm.textSearchUser != null) {
                if (vm.pageIndexUser != 0) {
                    vm.tempIndex = vm.pageIndexUser - 1;
                }
                service.findUserByUserName(vm.textSearchUser, vm.tempIndex, vm.pageSizeUser).then(function (data) {
                    vm.users = data.content;
                    vm.totalItemsUser = data.totalElements;
                });
            }
            if (vm.textSearchUser == '' || vm.textSearchUser == null) {
                service.getUsers(vm.pageIndexUser, vm.pageSizeUser).then(function (data) {
                    vm.users = data.content;
                    vm.totalItemsUser = data.totalElements;
                });
            }
        };
        //-----------//
        function getSocialPrioritys() {
            service.getSocialPrioritys().then(function (data) {
                vm.socialPrioritys = data.content;
            });
        }
        getSocialPrioritys();

        //--------Addres--------//
        vm.types = [
            { value: 1, name: 'Hộ khẩu thường trú' },
            { value: 2, name: 'Địa chỉ hiện tại' },
            { value: 3, name: 'Nguyên quán' }
        ];
        vm.address = {};
        vm.tempAddress = {};
        function getCountry() {
            service.getCountry().then(function (data) {
                vm.countries = data.content;
            });
        }
        getCountry();

        function getProvince() {
            service.getProvince().then(function (data) {
                vm.provinces = data.content;
            });
        }
        getProvince();

        function getListCity(provinceId) {
            service.getListCity(provinceId).then(function (data) {
                vm.cities = data.content;
            });
        }
        function getListVillage(citiesId) {
            service.getListCity(citiesId).then(function (data) {
                vm.villages = data.content;
            });
        }
        vm.onProvinceChange = function () {
            vm.cities = null;
            vm.city = null;
            if (!angular.isUndefined(vm.address.province)) {
                if (vm.address.province.id != null) {
                    getListCity(vm.address.province.id);
                }
            }
        }
        vm.onCityChange = function () {
            vm.villages = null;
            vm.village = null;
            if (!angular.isUndefined(vm.address.city)) {
                if (vm.address.city.id != null) {
                    getListVillage(vm.address.city.id);
                }
            }
        }
        vm.validateAddress = function () {
            if (angular.isUndefined(vm.address)) {
                toastr.warning('Bạn chưa nhập thông tin địa chỉ', 'Cảnh báo');
                return false;
            }
            if (angular.isUndefined(vm.address.country)) {
                toastr.warning('Chưa nhập  quốc gia', 'Cảnh báo');
                return false;
            }
            if (angular.isUndefined(vm.address.province)) {
                toastr.warning('Chưa nhập  tỉnh thành', 'Cảnh báo');
                return false;
            }
            if (angular.isUndefined(vm.address.city)) {
                toastr.warning('Chưa nhập  quận huyện', 'Cảnh báo');
                return false;
            }
            if (angular.isUndefined(vm.address.address1)) {
                toastr.warning('Chưa nhập  phường xã', 'Cảnh báo');
                return false;
            }

            return true;
        }
        vm.addAddress = function () {
            if (vm.validateAddress()) {
                if (vm.staff.address == null || angular.isUndefined(vm.staff.address)) {
                    vm.staff.address = [];
                }
                if (vm.address.country != null)
                    vm.tempAddress.country = vm.address.country.name;
                if (vm.address.province != null)
                    vm.tempAddress.province = vm.address.province.name;
                if (vm.address.city != null)
                    vm.tempAddress.city = vm.address.city.name;
                if (vm.address.address1 != null)
                    vm.tempAddress.address1 = vm.address.address1.name;
                if (vm.address.address1 != null && vm.address.address1.code != null) {
                    vm.tempAddress.postalCode = vm.address.address1.code;
                } else if (vm.address.city != null && vm.address.city.code != null) {
                    vm.tempAddress.postalCode = vm.address.city.code;
                }
                vm.tempAddress.type = vm.address.type;
                vm.staff.address.push(vm.tempAddress);
                vm.tempAddress = {};
                vm.address = {};
            }
        };
        vm.removeAddress = function (index) {
            vm.staff.address.splice(index, 1);
        }
        //---------------------//
        //----------Thông tin hợp đồng-----------//       
        vm.getPages = function (pageIndex, pageSize) {
            agreementService.getPages(pageIndex, pageSize).then(function (data) {

                vm.bsTableControl.options.data = vm.agreements;
                vm.agreements = data.content;
            });
        }

        vm.getAll = function (staffId) {
            agreementService.getAll(staffId).then(function (data) {
                // debugger;
                vm.agreements = data; // Hợp đồng
            })
            // debugger;
            EducationService.getEducationHistoryAll(staffId).then(function (data) {
                vm.educations = data;
            })
            //debugger;
            FamilyRelationshipService.getFamilyRelationshipAll(staffId).then(function (data) {
                vm.familyRelationships = data;
            })
        }

        vm.validateAgreement = function () {

            if (!vm.agreement.signedDate) {
                toastr.error('Vui lòng nhập ngày kí hợp đồng.', 'Lỗi');
                return false;
            }
            if (!vm.agreement.agreementStatus) {
                toastr.error('Vui lòng nhập trạng thái hợp đồng.', 'Lỗi');
                return false;
            }
            if (!vm.agreement.labourAgreementType) {
                toastr.error('Vui lòng nhập trạng loại hợp đồng.', 'Lỗi');
                return false;
            }
            if (!vm.agreement.labourAgreementType) {
                toastr.error('Vui lòng nhập trạng thái hợp đồng.', 'Lỗi');
                return false;
            }
            if (!vm.agreement.startDate) {
                toastr.error('Vui lòng nhập ngày bắt đầu.', 'Lỗi');
                return false;
            }
            return true;
        }


        $scope.editAgreement = function (agreement) {
            vm.agreement = agreement;
            getAll(agreement.id);
        };
        vm.agreementListDelete = [];
        $scope.removeAgreement = function (id) {

            for (let index = 0; index < vm.agreements.length; index++) {
                if (vm.agreements[index].id == id) {

                    vm.agreementListDelete.push(vm.agreements[index].id);
                    vm.agreements.splice(index, 1);
                }
            }


        }

        vm.agreement.labourAgreementType = {};
        vm.saveAgreement = function () {

            if (vm.agreement != null) {
                //vm.agreement.staff = {};


                if (vm.staff.isNew) {

                } else {
                    vm.agreement.staff = vm.staff;
                }
                if (vm.validateAgreement()) {
                    agreementService.saveAgreement(vm.agreement, function success() {

                        vm.agreements.push(vm.agreement);
                        vm.agreement = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới hợp đồng.', 'Thông báo');
                    });
                }

            }
        }

        // Education History

        vm.getEducationHistoryPages = function (pageIndex, pageSize) {
            EducationService.getEducationHistoryPages(pageIndex, pageSize).then(function (data) {

                vm.bsTableControl.options.data = vm.educations;
                vm.educations = data.content;
            });
        }

        vm.getEducationHistoryAll = function (staffId) {
            EducationService.getEducationHistoryAll(staffId).then(function (data) {
                vm.educations = data;
            })
        }

        $scope.editEducationHistory = function (education) {
            vm.education = education;
            getEducationHistoryAll(education.id);
        };
        vm.educationHistoryListDelete = [];
        $scope.removeEducationHistory = function (id) {

            for (let index = 0; index < vm.educations.length; index++) {
                if (vm.educations[index].id == id) {

                    vm.educationHistoryListDelete.push(vm.educations[index].id);
                    vm.educations.splice(index, 1);
                }
            }
        }
        vm.saveEducationHistory = function () {

            if (vm.education != null) {
                //vm.education.staff = {};

                if (vm.staff.isNew) {

                } else {
                    vm.education.staff = vm.staff;
                }

                EducationService.saveEducationHistory(vm.education, function success() {

                    vm.educations.push(vm.education);
                    vm.education = {};
                }, function failure() {
                    toastr.error('Có lỗi xảy ra khi thêm mới.', 'Thông báo');
                });

            }
        }

        // FamilyRelationship
        vm.getFamilyRelationshipPages = function (pageIndex, pageSize) {
            FamilyRelationshipService.getFamilyRelationshipPages(pageIndex, pageSize).then(function (data) {

                vm.bsTableControl.options.data = vm.familyRelationships;
                vm.familyRelationships = data.content;
            });
        }

        vm.getFamilyRelationshipAll = function (staffId) {
            FamilyRelationshipService.getFamilyRelationshipAll(staffId).then(function (data) {
                // debugger;
                vm.familyRelationships = data;
            })
        }

        $scope.editFamilyRelationship = function (familyRelationship) {
            vm.familyRelationship = familyRelationship;
            getFamilyRelationshipAll(familyRelationship.id);
        };
        vm.familyRelationshipListDelete = [];
        $scope.removeFamilyRelationship = function (id) {

            for (let index = 0; index < vm.familyRelationships.length; index++) {
                if (vm.familyRelationships[index].id == id) {

                    vm.familyRelationshipListDelete.push(vm.familyRelationships[index].id);
                    vm.familyRelationships.splice(index, 1);
                }
            }
        }
        vm.saveFamilyRelationship = function () {

            if (vm.familyRelationship != null) {
                //vm.education.staff = {};

                if (vm.staff.isNew) {

                } else {
                    vm.familyRelationship.staff = vm.staff;
                }

                FamilyRelationshipService.saveFamilyRelationship(vm.familyRelationship, function success() {

                    vm.familyRelationships.push(vm.familyRelationship);
                    vm.familyRelationship = {};
                }, function failure() {
                    toastr.error('Có lỗi xảy ra khi thêm mới.', 'Thông báo');
                });

            }
        };

        function validate(){
            if(vm.staff.staffCode && vm.staff.staffCode.trim() != ""){
                if(!vm.staff.id){
                    vm.staff.id = 0;
                }
                
                service.validateStaffCode(vm.staff.staffCode, vm.staff.id, function successCallback(data){
                    
                }, function errorCallback(error){
                    return false;
                }).then(function (data){
                    if(!data){
                        toastr.warning("Mã công chức này đã tồn tại", "Thông báo");
                        return true;
                    }else {
                        return false;
                    }
                });
            }
            if(vm.staff.user && vm.staff.user.username && vm.staff.user.username.trim() != ""){
                if(!vm.staff.user){
                    vm.staff.user = {};
                }
                if(!vm.staff.user.id){
                    vm.staff.user.id = 0;
                }
                service.validateUserName(vm.staff.user.username,vm.staff.user.id, function successCallback(data){

                }, function errorCallback(error){
                    return false;
                }).then(function(data){
                    if(!data){
                        toastr.warning("Tên đăng nhập đã tồn tại", "Thông báo");
                        return true;
                    }else{
                        return false;
                    }
                });
            }
        }

    }
})();