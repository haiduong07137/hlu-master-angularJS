(function () {
    'use strict';

    angular.module('Hrm.LetterManagement').controller('FinishLetterOutController', FinishLetterOutController);

    FinishLetterOutController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'LetterManagementService',
        'Upload',
        '$state'
    ];

    function FinishLetterOutController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service, Upload, $state) {
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
        vm.isShowLetterIn = true;
        vm.isShowLetterOut = true;
        vm.letterOuts = [];
        vm.pageSize = 25;
        vm.pageIndex = 1;
        /*
         * 
         */
        getAllLetterOutDocumentBy(1, -1, vm.pageIndex, vm.pageSize);
        function getAllLetterOutDocumentBy(stepIndex, currentParticipateStates, pageIndex, pageSize) {
            service.getAllLetterOutDocumentBy(stepIndex, currentParticipateStates, pageIndex, pageSize).then(function (data) {
                vm.letterOuts = data.content;
                console.log(data.content);
                vm.bsTableControl.options.data = vm.letterOuts;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        }

        vm.bsTableControl = {
            options: {
                data: vm.letterOuts,
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
                columns: service.getTableDefinitionFinishLetterOut(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedletterOuts.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedletterOuts = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedletterOuts.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedletterOuts = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    getAllLetterOutDocumentBy(1, -1, vm.pageIndex, vm.pageSize);
                }
            }
        };

        $scope.viewDocument = function (id) {
            $state.go('application.letter_out_view', { letter_id: id, state: 'view' });
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

    }
})();