/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.LetterManagement').controller('WaitLetterInController', WaitLetterInController);

    WaitLetterInController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'LetterManagementService',
        'Upload',
        '$state',
        'letterIndocumentFactory'
    ];

    function WaitLetterInController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service, Upload,$state,letterIndocumentFactory) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        /*
         * Khai báo biến
         */
        var vm = this;
        vm.isShowLetterIn=true;
        vm.isShowLetterOut=true;
        vm.letterIns=[];
        vm.pageSize = 25;
        vm.pageIndex = 1;
        vm.searchObject ={};
        /*
         * 
         */
        getPageLetterInByIndex(2,-1,vm.pageIndex,vm.pageSize);
        function getPageLetterInByIndex(stepIndex, currentParticipateStates, pageIndex, pageSize) {
            service.getPageLetterInByIndex(stepIndex, currentParticipateStates, pageIndex, pageSize).then(function (data) {
                vm.letterIns = data.content;
                console.log(data.content);
                vm.bsTableControl.options.data = vm.letterIns;
                vm.bsTableControl.options.totalRows = data.totalElements;
                console.log(vm.letterIns);
            });
        }
        
        vm.bsTableControl = {
            options: {
                data: vm.letterIns,
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
                        vm.selectedletterIns.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedletterIns = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedletterIns.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedletterIns = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    getPageLetterInByIndex(2,-1,vm.pageIndex,vm.pageSize);
                }
            }
        };

        $scope.assignLetter = function(letterId){
            $state.go('application.assigner_letter_in',{letter_id:letterId});
        }

        $scope.forwardLetter = function(letterId){
            $state.go('application.forward_letter_in',{letter_id:letterId,state:'forward'});
        }
        
        $scope.editDocument = function(letterId){
            $state.go('application.edit_letter_in',{letter_id:letterId});
        }

        $scope.forwardLetterStepTwo = function(letterId){
            $state.go('application.transfer_process_letter_in',{letter_id:letterId})
        }

        $scope.viewDocument = function(letterId){
            letterIndocumentFactory.letterFunc(letterId,'view');
        }
        vm.hasClerkRole = true;
        vm.checkClerkRole = function(){
            service.checkUserHasTaskRoleByUserNameAndRoleCode($scope.currentUser.username, "ClerkRole").then(function(data){
                vm.hasClerkRole = data;
            }).catch(function (err){
                vm.hasClerkRole = true;
            });
        }
        vm.checkClerkRole();

        $scope.checkViewDocument = function (id){
            vm.letterId = id;
            vm.searchObject.fullname = null;
            service.checkViewDocument(vm.letterId, vm.searchObject, vm.searchUserIndex, vm.searchUserSize).then(function (data) {
                vm.users = data.content;
                vm.totalItemsUser = data.totalElements;
                vm.modalInstanceListUser = modal.open({
                    animation: true,
                    templateUrl: 'checkViewLetter.html',
                    scope: $scope,
                    backdrop: 'static',
                    size: 'lg'
                });
            });
        }
        
        vm.searchUser = function(){
            service.checkViewDocument(vm.letterId, vm.searchObject, vm.searchUserIndex, vm.searchUserSize).then(function (data) {
                vm.users = data.content;
                vm.totalItemsUser = data.totalElements;
            });
        }
        
        vm.enterSearchUser = function () {
            console.log(event.keyCode);
            vm.pageIndexUser = 0;
            if (event.keyCode == 13) {//Phím Enter
                service.checkViewDocument(vm.letterId, vm.searchObject, vm.searchUserIndex, vm.searchUserSize).then(function (data) {
                    vm.users = data.content;
                    vm.totalItemsUser = data.totalElements;
                });
            }
        };
        vm.searchUserIndex = 1;
        vm.searchUserSize = 10;
        vm.pageChange = function() {
            service.checkViewDocument(vm.letterId, vm.searchObject, vm.searchUserIndex, vm.searchUserSize).then(function (data) {
                vm.users = data.content;
                vm.totalItemsUser = data.totalElements;
            });
        }
    }
})();