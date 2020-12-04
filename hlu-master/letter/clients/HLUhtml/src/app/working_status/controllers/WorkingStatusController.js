/**
 * Author Giang 21/04/2018
 */
(function () {
    'use strict';

    angular.module('Hrm.WorkingStatus').controller('WorkingStatusController', WorkingStatusController);

    WorkingStatusController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'WorkingStatusService'
    ];
    angular.module('Hrm.WorkingStatus').directive('myDatePicker', function () {
        return {
            restrict: 'A',
            require: '?ngModel',
            link: function (scope, element, attrs, ngModelController) {

                // Private variables
                var datepickerFormat = 'dd/mm/yyyy',
                    momentFormat = 'DD/MM/YYYY',
                    datepicker,
                    elPicker;

                // Init date picker and get objects http://bootstrap-datepicker.readthedocs.org/en/release/index.html
                datepicker = element.datepicker({
                    autoclose: true,
                    keyboardNavigation: false,
                    todayHighlight: true,
                    format: datepickerFormat
                });
                elPicker = datepicker.data('datepicker').picker;

                // Adjust offset on show
                datepicker.on('show', function (evt) {
                    elPicker.css('left', parseInt(elPicker.css('left')) + +attrs.offsetX);
                    elPicker.css('top', parseInt(elPicker.css('top')) + +attrs.offsetY);
                });

                // Only watch and format if ng-model is present https://docs.angularjs.org/api/ng/type/ngModel.NgModelController
                if (ngModelController) {
                    // So we can maintain time
                    var lastModelValueMoment;

                    ngModelController.$formatters.push(function (modelValue) {
                        //
                        // Date -> String
                        //

                        // Get view value (String) from model value (Date)
                        var viewValue,
                            m = moment(modelValue);
                        if (modelValue && m.isValid()) {
                            // Valid date obj in model
                            lastModelValueMoment = m.clone(); // Save date (so we can restore time later)
                            viewValue = m.format(momentFormat);
                        } else {
                            // Invalid date obj in model
                            lastModelValueMoment = undefined;
                            viewValue = undefined;
                        }

                        // Update picker
                        element.datepicker('update', viewValue);

                        // Update view
                        return viewValue;
                    });

                    ngModelController.$parsers.push(function (viewValue) {
                        //
                        // String -> Date
                        //

                        // Get model value (Date) from view value (String)
                        var modelValue,
                            m = moment(viewValue, momentFormat, true);
                        if (viewValue && m.isValid()) {
                            // Valid date string in view
                            if (lastModelValueMoment) { // Restore time
                                m.hour(lastModelValueMoment.hour());
                                m.minute(lastModelValueMoment.minute());
                                m.second(lastModelValueMoment.second());
                                m.millisecond(lastModelValueMoment.millisecond());
                            }
                            modelValue = m.toDate();
                        } else {
                            // Invalid date string in view
                            modelValue = undefined;
                        }

                        // Update model
                        return modelValue;
                    });

                    datepicker.on('changeDate', function (evt) {
                        // Only update if it's NOT an <input> (if it's an <input> the datepicker plugin trys to cast the val to a Date)
                        if (evt.target.tagName !== 'INPUT') {
                            ngModelController.$setViewValue(moment(evt.date).format(momentFormat)); // $seViewValue basically calls the $parser above so we need to pass a string date value in
                            ngModelController.$render();
                        }
                    });
                }

            }
        };
    });

    angular.module('Hrm.WorkingStatus').directive("gdatepicker", function () {
        return {
            restrict: "E",
            scope: {
                ngModel: "=",
                dateOptions: "=",
                opened: "=",
                id: "="
            },
            link: function ($scope, element, attrs) {
                $scope.open = function (event, id) {
                    event.preventDefault();
                    event.stopPropagation();
                    //alert('test:'+id);
                    $scope.opened = {};
                    $scope.opened[id] = true;
                    //alert($scope.opened[id]);
                };

                $scope.clear = function () {
                    $scope.ngModel = null;
                };
            },
            templateUrl: 'gtemplate/datepicker.html'
        }
    });

    function WorkingStatusController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        var vm = this;
        $scope.hstep = 1;
        $scope.mstep = 15;

        $scope.options = {
            hstep: [1, 2, 3],
            mstep: [1, 5, 10, 15, 25, 30]
        };
        vm.workingstatus = {};
        vm.WorkingStatuses = [];
        vm.selectedWorkingStatuses = [];
        vm.pageIndex = 1;
        vm.pageSize = 25;

        vm.getWorkingStatuses = function () {
            service.getWorkingStatuses(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.WorkingStatuses = data.content;
                vm.bsTableControl.options.data = vm.WorkingStatuses;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        };

        vm.getWorkingStatuses();


        vm.bsTableControl = {
            options: {
                data: vm.WorkingStatuses,
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
                        vm.selectedWorkingStatuses.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedWorkingStatuses = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedWorkingStatuses.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedWorkingStatuses = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    vm.getWorkingStatuses();
                }
            }
        };
       
        /**
         * New event account
         */


        vm.newWorkingStatus = function () {

            vm.workingstatus.isNew = true;
            console.log(vm.workingstatus.timePeriods);
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_working_status_modal.html',
                scope: $scope,
                size: 'lg'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (!vm.workingstatus.code || vm.workingstatus.code.trim() == '') {
                        toastr.error('Vui lòng nhập mã trạng thái.', 'Lỗi');
                        return;
                    }

                    if (!vm.workingstatus.name || vm.workingstatus.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên trạng thái.', 'Lỗi');
                        return;
                    }

                    if (!vm.workingstatus.statusValue ) {
                        toastr.error('Vui lòng nhập giá trị.', 'Lỗi');
                        return;
                    }

                    service.saveWorkingStatus(vm.workingstatus, function success() {

                        // Refresh list
                        vm.getWorkingStatuses();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một trạng thái.', 'Thông báo');

                        // clear object
                        vm.workingstatus = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một trạng thái.', 'Thông báo');
                    });
                }
            }, function () {
                vm.workingstatus = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        
        
        $scope.editWorkingStatus = function (workingstatusId) {
            service.getWorkingStatus(workingstatusId).then(function (data) {

                vm.workingstatus = data;
                vm.workingstatus.isNew = false;
                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_working_status_modal.html',
                    scope: $scope,
                    size: 'lg'
                });


                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (!vm.workingstatus.code || vm.workingstatus.code.trim() == '') {
                            toastr.error('Vui lòng nhập mã ca làm việc.', 'Lỗi');
                            return;
                        }

                        if (!vm.workingstatus.name || vm.workingstatus.name.trim() == '') {
                            toastr.error('Vui lòng nhập tên ca làm việc.', 'Lỗi');
                            return;
                        }
                        if (!vm.workingstatus) {
                            toastr.error('Vui lòng nhập giá trị.', 'Lỗi');
                            return;
                        }

                        service.saveWorkingStatus(vm.workingstatus, function success() {

                            // Refresh list
                            vm.getWorkingStatuses();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object

                            vm.workingstatus = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin bản ghi.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.workingstatus = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        /**
         * Delete accounts
         */
        vm.deleteWorkingStatuses = function () {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    console.log(vm.selectedWorkingStatuses);
                    service.deleteWorkingStatuses(vm.selectedWorkingStatuses, function success() {

                        // Refresh list
                        vm.getWorkingStatuses();

                        // Notify
                        toastr.info('Bạn đã xóa thành công ' + vm.selectedWorkingStatuses.length + ' bản ghi.', 'Thông báo');

                        // Clear selected accounts
                        vm.selectedWorkingStatuses = [];
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