(function () {
    'use strict';

    angular.module('Hrm.DocumentBookGroup').controller('DocumentBookGroupController', DocumentBookGroupController);

    DocumentBookGroupController.$inject = [
        '$rootScope',
        '$scope',
        '$http',
        '$timeout',
        'settings',
        'DocumentBookGroupService',
        '$uibModal',
        'toastr',
        '$state'
    ];

    function DocumentBookGroupController ($rootScope, $scope, $http, $timeout,settings,service,modal,toastr,$state) {
        $scope.$on('$viewContentLoaded', function() {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        var vm = this;

        vm.documentBookGroup = null;
        vm.documentBookGroups = [];
        vm.selectedDocumentBookGroups = [];
        console.log("group");
        // pagination
        vm.pageIndex = 1;
        vm.pageSize = 10;

        //check duplicate code
        vm.viewCheckDuplicateCode = {};
        vm.tempCode = '';

        function getListDocumentBookGroup(pageIndex, pageSize) {
            service.getListDocumentBookGroup(pageIndex, pageSize).then(function (data) {
                vm.documentBookGroups = data.content;
                vm.bsTableControlDocBookGroup.options.data = vm.documentBookGroups;
                vm.bsTableControlDocBookGroup.options.totalRows = data.totalElements;
            });
        }

        function newDocumentBookGroup(examSkill) {
            service.newDocumentBookGroup(examSkill).then(function (data) {
                getListDocumentBookGroup(vm.pageIndex, vm.pageSize);
                modalInstance.close();
                toastr.info('Thêm mới thành công', 'Thông báo');
            });
        }

        function getDocumentBookGroupById(documentBookGroupId) {
            service.getDocumentBookGroupById(documentBookGroupId).then(function (data) {
                vm.documentBookGroup = data;
                vm.tempCode = vm.documentBookGroup.code;
                console.log(vm.documentBookGroup);
            });
        }

        function editDocumentBookGroup(documentBookGroup) {
            service.editDocumentBookGroup(documentBookGroup).then(function (data) {
                getListDocumentBookGroup(vm.pageIndex, vm.pageSize);
                modalInstance.close();
                toastr.info('Lưu thành công', 'Thông báo');
            });
        }

        function deleteDocumentBookGroup(documentBookGroupId) {
            service.deleteDocumentBookGroup(documentBookGroupId).then(function (data) {
                getListDocumentBookGroup(vm.pageIndex, vm.pageSize);
                toastr.info('Xóa thành công.', 'Thông báo');
            });
        }

        //check duplicate code
        function validateEducationLevel() {
            console.log(vm.documentBookGroup);
            if (vm.documentBookGroup == null) {
                toastr.warning("Chưa nhập dữ liệu");
                return false;
            }
            if (angular.isUndefined(vm.documentBookGroup.code) || vm.documentBookGroup.code == null || vm.documentBookGroup.code.length <= 0) {
                toastr.warning("Chưa nhập mã");
                return false;
            }
            return true;
        }

        function checkDuplicateCode(code, type, action) { //type: 1 -> save; 2 -> edit;   action: 1 -> just check code; 2 -> save or edit
            service.checkDuplicateCode(code).then(function (data) {
                vm.viewCheckDuplicateCode = data;
                if (action == 1) {
                    if (type == 1) {
                        if (vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == true) {
                            toastr.warning("Mã bị trùng");
                        }
                        if (vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == false) {
                            toastr.success("Mã không bị trùng");
                        }
                    }
                    if (type == 2) {
                        if (vm.tempCode.toLowerCase().trim() != code.toLowerCase().trim()) {
                            checkDuplicateCode(vm.tempCode, 1, 1);
                        } else {
                            toastr.info("Mã chưa thay đổi");
                        }
                    }
                }
                if (action == 2) {
                    if (type == 1) {
                        if (vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == true) {
                            toastr.warning("Mã bị trùng");
                        }
                        if (vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == false) {
                            newDocumentBookGroup(vm.documentBookGroup);
                        }
                    }
                    if (type == 2) {
                        if (vm.tempCode.toLowerCase().trim() != code.toLowerCase().trim()) {
                            service.checkDuplicateCode(vm.tempCode).then(function (data) {
                                vm.viewCheckDuplicateCode = data;
                                if (vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == true) {
                                    toastr.warning("Mã bị trùng");
                                }
                                if (vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == false) {
                                    vm.documentBookGroup.code = vm.tempCode.trim();
                                    editDocumentBookGroup(vm.documentBookGroup);
                                }
                            });
                        } else {
                            vm.documentBookGroup.code = vm.tempCode.trim();
                            editDocumentBookGroup(vm.documentBookGroup);
                        }
                    }
                }
                console.log(data);

            });
        }

        vm.checkDuplicateCode = function (type, action) {
            if (validateEducationLevel()) {
                checkDuplicateCode(vm.documentBookGroup.code, type, action);
            }
        }

        var modalInstance;

        vm.newDocumentBookGroup = function () {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'new_modal.html',
                scope: $scope,
                backdrop:'static',
                size: 'md'
            });

            vm.documentBookGroup = {};

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    newDocumentBookGroup(vm.documentBookGroup);
                }
            }, function () {
                console.log("cancel");
            });
        }

        $scope.editDocumentBookGroup = function (documentBookGroupId) {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_modal.html',
                scope: $scope,
                size: 'md'
            });

            getDocumentBookGroupById(documentBookGroupId);

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    editDocumentBookGroup(vm.documentBookGroup);
                }
            }, function () {
                console.log("cancel");
            });
        }

        $scope.viewDocumentBook = function (documentBookGroupId) {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'view_modal.html',
                scope: $scope,
                size: 'md'
            });

            getDocumentBookGroupById(documentBookGroupId);

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                }
            }, function () {
                console.log("cancel");
            });
        }

        $scope.deleteDocumentBookGroup = function (documentBookGroupId) {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            getDocumentBookGroupById(documentBookGroupId);

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    if (vm.documentBookGroup != null) {
                        if (!angular.isUndefined(vm.documentBookGroup.id)) {
                            deleteDocumentBookGroup(vm.documentBookGroup.id);
                        }
                    }
                }
            }, function () {
                console.log("cancel");
            });
        }

        getListDocumentBookGroup(vm.pageIndex, vm.pageSize);

        vm.bsTableControlDocBookGroup = {
            options: {
                data: vm.documentBookGroups,
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
                        vm.selectedDocumentBookGroups.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentBookGroups = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedDocumentBookGroups);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedDocumentBookGroups.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentBookGroups = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    getListDocumentBookGroup(vm.pageIndex, vm.pageSize);
                }
            }
        };

        
    }
})();
