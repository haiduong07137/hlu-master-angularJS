/**
 * Created by bizic on 28/8/2016.
 */
(function () {
    'use strict';

    angular.module('Hrm.DocumentBook').controller('DocumentBookController', DocumentBookController);

    DocumentBookController.$inject = [
        '$rootScope',
        '$scope',
        '$http',
        '$timeout',
        'settings',
        'DocumentBookService',
        '$uibModal',
        'toastr',
        '$state'
    ];

    function DocumentBookController ($rootScope, $scope, $http, $timeout,settings,service,modal,toastr,$state) {
        $scope.$on('$viewContentLoaded', function() {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        var vm = this;
        vm.documentBook = null;
        vm.documentBooks = [];
        vm.selectedDocumentBooks = [];

        // pagination
        vm.pageIndex = 1;
        vm.pageSize = 10;

        //check duplicate code
        vm.viewCheckDuplicateCodeBook = {};
        vm.tempCodeBook = '';
        // vm.bsTableControlDocBook.state.pageNumber = 1;
        // vm.bsTableControlDocBook.options.data = 0;
        // vm.bsTableControlDocBook.options.totalRows = 0;

        function getListDocumentBookGroup() {
            service.getListDocumentBookGroup(1, 1000).then(function (data) {
                vm.documentBookGroups = data.content;
            });
        }
        getListDocumentBookGroup();
        function getListDocumentBook(pageIndex, pageSize){
            service.getListDocumentBook(pageIndex,pageSize).then(function(data) {
                vm.documentBooks = data.content;
                console.log(data.content);
                console.log(vm.bsTableControlDocBook);
                if(vm.documentBooks.length <= 0 && data.totalElements != 0){
                    $state.reload();
                }
                // if(pageIndex<=1){
                //     if( vm.bsTableControlDocBook.state=='undefined'){
                //         vm.bsTableControlDocBook.state={}
                //     }
                //     vm.bsTableControlDocBook.state.pageNumber = 1;
                // }
                // vm.bsTableControlDocBook.options.data = 0;
                // vm.bsTableControlDocBook.options.totalRows = 0;
                vm.bsTableControlDocBook.options.data = vm.documentBooks;
                vm.bsTableControlDocBook.options.totalRows = data.totalElements;
                console.log(data);
            });
        }

        function newDocumentBook(examSkill) {
            service.newDocumentBook(examSkill).then(function(data) {
                getListDocumentBook(vm.pageIndex,vm.pageSize);
                modalInstance.close();
                toastr.info('Thêm mới thành công','Thông báo');
            });
        }

        function getDocumentBookById(documentBookId){
            service.getDocumentBookById(documentBookId).then(function(data) {
                vm.documentBook = data;
                vm.tempCodeBook = vm.documentBook.code;
                console.log(vm.documentBook);
            });
        }

        function editDocumentBook(documentBook) {
            service.editDocumentBook(documentBook).then(function (data) {
                getListDocumentBook(vm.pageIndex,vm.pageSize);
                modalInstance.close();
                toastr.info('Lưu thành công','Thông báo');
            });
        }

        function deleteDocumentBook(documentBookId) {
            service.deleteDocumentBook(documentBookId).then(function (data) {
                getListDocumentBook(vm.pageIndex,vm.pageSize);
                toastr.info('Xóa thành công.', 'Thông báo');
            });
        }

        //check duplicate code
        function validateEducationLevelBook() {
            console.log(vm.documentBook);
            if(vm.documentBook == null){
                toastr.warning("Chưa nhập dữ liệu");
                return false;
            }
            if(angular.isUndefined(vm.documentBook.code) || vm.documentBook.code == null || vm.documentBook.code.length <= 0){
                toastr.warning("Chưa nhập mã");
                return false;
            }
            return true;
        }

        function checkDuplicateCodeBook(code,type,action){ //type: 1 -> save; 2 -> edit;   action: 1 -> just check code; 2 -> save or edit
            service.checkDuplicateCode(code).then(function(data) {
                vm.viewCheckDuplicateCodeBook = data;
                if(action == 1){
                    if(type == 1){
                        if(vm.viewCheckDuplicateCodeBook != null && vm.viewCheckDuplicateCodeBook.duplicate == true){
                            toastr.warning("Mã bị trùng");
                        }
                        if(vm.viewCheckDuplicateCodeBook != null && vm.viewCheckDuplicateCodeBook.duplicate == false){
                            toastr.success("Mã không bị trùng");
                        }
                    }
                    if(type == 2){
                        if(vm.tempCodeBook.toLowerCase().trim() != code.toLowerCase().trim()){
                            checkDuplicateCode(vm.tempCodeBook,1,1);
                        }else{
                            toastr.info("Mã chưa thay đổi");
                        }
                    }
                }
                if(action == 2){
                    if(type == 1){
                        if(vm.viewCheckDuplicateCodeBook != null && vm.viewCheckDuplicateCodeBook.duplicate == true){
                            toastr.warning("Mã bị trùng");
                        }
                        if(vm.viewCheckDuplicateCodeBook != null && vm.viewCheckDuplicateCodeBook.duplicate == false){
                            newDocumentBook(vm.documentBook);
                        }
                    }
                    if(type == 2){
                        if(vm.tempCodeBook.toLowerCase().trim() != code.toLowerCase().trim()){
                            service.checkDuplicateCode(vm.tempCodeBook).then(function(data) {
                                vm.viewCheckDuplicateCodeBook = data;
                                if(vm.viewCheckDuplicateCodeBook != null && vm.viewCheckDuplicateCodeBook.duplicate == true){
                                    toastr.warning("Mã bị trùng");
                                }
                                if(vm.viewCheckDuplicateCodeBook != null && vm.viewCheckDuplicateCodeBook.duplicate == false){
                                    vm.documentBook.code = vm.tempCodeBook.trim();
                                    editDocumentBook(vm.documentBook);
                                }
                            });
                        }else{
                            vm.documentBook.code = vm.tempCodeBook.trim();
                            editDocumentBook(vm.documentBook);
                        }
                    }
                }
                console.log(data);

            });
        }

        vm.checkDuplicateCodeBook = function (type,action) {
            if(validateEducationLevelBook()){
                checkDuplicateCodeBook(vm.documentBook.code,type,action);
            }
        }

        var modalInstance;

        vm.newDocumentBook = function () {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'new_modal.html',
                scope: $scope,
                size: 'md'
            });

            vm.documentBook = {};

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    newDocumentBook(vm.documentBook);
                }
            }, function () {
                console.log("cancel");
            });
        }

        $scope.editDocumentBook = function (documentBookId) {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_modal.html',
                scope: $scope,
                size: 'md'
            });

            getDocumentBookById(documentBookId);

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    editDocumentBook(vm.documentBook);
                }
            }, function () {
                console.log("cancel");
            });
        }

        $scope.viewDocumentBook = function (documentBookId) {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'view_modal.html',
                scope: $scope,
                size: 'md'
            });

            getDocumentBookById(documentBookId);

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                }
            }, function () {
                console.log("cancel");
            });
        }

        $scope.deleteDocumentBook = function (documentBookId) {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            getDocumentBookById(documentBookId);

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    if(vm.documentBook != null){
                        if(!angular.isUndefined(vm.documentBook.id)){
                            deleteDocumentBook(vm.documentBook.id);
                        }
                    }
                }
            }, function () {
                console.log("cancel");
            });
        }

        getListDocumentBook(vm.pageIndex,vm.pageSize);

        vm.bsTableControlDocBook = {
            options: {
                data: vm.documentBooks,
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
                        vm.selectedDocumentBooks.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentBooks = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedDocumentBooks);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedDocumentBooks.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentBooks = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    getListDocumentBook(vm.pageIndex,vm.pageSize);
                }
            }
        };
    }
})();
