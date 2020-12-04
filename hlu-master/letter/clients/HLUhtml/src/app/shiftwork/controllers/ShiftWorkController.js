/**
 * Author Giang 21/04/2018
 */
(function () {
    'use strict';

    angular.module('Hrm.ShiftWork').controller('ShiftWorkController', ShiftWorkController);

    ShiftWorkController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'ShiftWorkService'
    ];
    angular.module('Hrm.ShiftWork').directive('myDatePicker', function () {
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

    angular.module('Hrm.ShiftWork').directive("gdatepicker", function () {
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

    function ShiftWorkController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service) {
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
        vm.shiftWork = {};
        vm.shiftWork.timePeriods = [];
        vm.shiftWorks = [];
        vm.selectedShiftWorks = [];
        vm.time = {};
        vm.pageIndex = 1;
        vm.pageSize = 25;
        vm.typeOption = [{
                id: 1,
                name: 'Chính Quyền'
            },
            {
                id: 2,
                name: 'Đoàn thể'
            }
        ]

        vm.getShiftWorks = function () {
            service.getShiftWorks(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.shiftWorks = data.content;
                vm.bsTableControl.options.data = vm.shiftWorks;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        };

        vm.getShiftWorks();


        vm.bsTableControl = {
            options: {
                data: vm.shiftWorks,
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
                        vm.selectedShiftWorks.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedShiftWorks = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedShiftWorks.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedShiftWorks = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    vm.getShiftWorks();
                }
            }
        };
        vm.bsTableControlListTime = {
            options: {
                data: vm.shiftWork.timePeriods,
                idField: 'id',
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: false,
                showToggle: false,
                pageSize: vm.pageSize,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: service.getTableDefinitiontionTimePeriods()
            }
        };
        /**
         * New event account
         */


        vm.newShiftWork = function () {
            vm.shiftWork.totalHours=0;

            vm.shiftWork.isNew = true;
            console.log(vm.shiftWork.timePeriods);
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_shift_work_modal.html',
                scope: $scope,
                size: 'lg'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (!vm.shiftWork.code || vm.shiftWork.code.trim() == '') {
                        toastr.error('Vui lòng nhập mã ca làm việc.', 'Lỗi');
                        return;
                    }

                    if (!vm.shiftWork.name || vm.shiftWork.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên ca làm việc.', 'Lỗi');
                        return;
                    }

                    service.saveShiftWork(vm.shiftWork, function success() {

                        // Refresh list
                        vm.getShiftWorks();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một ca làm việc.', 'Thông báo');

                        // clear object
                        vm.shiftWork = {};
                        vm.shiftWork.timePeriods = [];
                        vm.time.end = '';
                        vm.time.start = '';
                        vm.bsTableControlListTime.options.data = vm.shiftWork.timePeriods;
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một ca làm việc.', 'Thông báo');
                    });
                }
            }, function () {
                vm.shiftWork = {};
                vm.shiftWork.timePeriods = [];
                vm.time.end = '';
                vm.time.start = '';
                vm.bsTableControlListTime.options.data = vm.shiftWork.timePeriods;
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        
        vm.addTime = function () {
            
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_time_period.html',
                scope: $scope,
                size: 'md'
            });
            vm.time.start='';
            vm.time.end='';
            modalInstance.result.then(function (confirm) {
                if (confirm =='yes') {
                    if (vm.time.start != null && vm.time.end != null && vm.time.start != '' && vm.time.end != '') {
                        
                        vm.stime=vm.time.start.getHours()*60+vm.time.start.getMinutes();
                        vm.etime=vm.time.end.getHours()*60+vm.time.end.getMinutes();
                        if (vm.stime<vm.etime){
                            if(vm.shiftWork.timePeriods.length>=1){
                                
                                for(var i=0;i<vm.shiftWork.timePeriods.length;i++){
                                    if(typeof vm.shiftWork.timePeriods[i].startTime ==="number"){
                                        var date=new Date(vm.shiftWork.timePeriods[0].startTime);
                                        var date1=new Date(vm.shiftWork.timePeriods[0].endTime);
                                        vm.stime1=date.getHours()*60+date.getMinutes();
                                        vm.etime1=date1.getHours()*60+date1.getMinutes();
                                    }else{
                                        vm.stime1=vm.shiftWork.timePeriods[i].startTime.getHours()*60+vm.shiftWork.timePeriods[i].startTime.getMinutes();
                                        vm.etime1=vm.shiftWork.timePeriods[i].endTime.getHours()*60+vm.shiftWork.timePeriods[i].endTime.getMinutes();
                                    }   
                                    if((vm.stime>vm.stime1&&vm.stime<vm.etime1)||(vm.etime>vm.stime1&&vm.etime<vm.etime1)||vm.stime==vm.stime1||vm.etime==vm.etime1){
                                        toastr.error('Trùng lịch với ca thời gian khác', 'Lỗi');
                                        return;
                                    }
                            }

                            }
                            vm.timep = {};
                            vm.timep.startTime = vm.time.start;
                            vm.timep.endTime = vm.time.end;
                            vm.shiftWork.totalHours += parseFloat(((vm.time.end  - vm.time.start) / (60 * 60 * 1000)).toFixed(2));
                            vm.time.end = '';
                            vm.time.start = '';
                            vm.shiftWork.timePeriods.push(vm.timep);
                            vm.bsTableControlListTime.options.data = vm.shiftWork.timePeriods;
                        
                        }else toastr.error('Lỗi thời gian, vui lòng nhập lại', 'Lỗi');
                    } else {
                        toastr.error('Vui lòng nhập thông tin thời gian', 'Lỗi');
                    }
                }
            });

        }
        $scope.deleteTimePeriod = function (index) {
            if (index == null || index == '') {
                toastr.error('Có lỗi xảy ra vui lòng thử lại.', 'Lỗi');
                return;
            }


            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                   
                    vm.bsTableControlListTime.options.data = vm.shiftWork.timePeriods;
                   
                    vm.shiftWork.totalHours -= parseFloat(((vm.shiftWork.timePeriods[index].endTime - vm.shiftWork.timePeriods[index].startTime) / (60 * 60 * 1000)).toFixed(2));
                    vm.shiftWork.timePeriods.splice(index, 1);
                    toastr.info('Bạn đã xóa thành công bản ghi','Thông báo');
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };
        $scope.editTimePeriod = function (index) {

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_time_period.html',
                scope: $scope,
                size: 'md'
            });
            vm.time.start = vm.shiftWork.timePeriods[index].startTime;
            vm.time.end = vm.shiftWork.timePeriods[index].endTime;
            modalInstance.result.then(function (confirm) {
                if (confirm =='yes') {
                    if (vm.time.start != null && vm.time.end != null && vm.time.start != '' && vm.time.end != '') {
                        
                        vm.stime=vm.time.start.getHours()*60+vm.time.start.getMinutes();
                        vm.etime=vm.time.end.getHours()*60+vm.time.end.getMinutes();
                        if (vm.stime<vm.etime){
                            if(vm.shiftWork.timePeriods.length>=1){
                                
                                for(var i=0;i<vm.shiftWork.timePeriods.length;i++){
                                    if(i==index){
                                        continue;
                                    }
                                    if(typeof vm.shiftWork.timePeriods[i].startTime ==="number"){
                                        var date=new Date(vm.shiftWork.timePeriods[0].startTime);
                                        var date1=new Date(vm.shiftWork.timePeriods[0].endTime);
                                        vm.stime1=date.getHours()*60+date.getMinutes();
                                        vm.etime1=date1.getHours()*60+date1.getMinutes();
                                    }else{
                                        vm.stime1=vm.shiftWork.timePeriods[i].startTime.getHours()*60+vm.shiftWork.timePeriods[i].startTime.getMinutes();
                                        vm.etime1=vm.shiftWork.timePeriods[i].endTime.getHours()*60+vm.shiftWork.timePeriods[i].endTime.getMinutes();
                                    }   
                                    if((vm.stime>vm.stime1&&vm.stime<vm.etime1)||(vm.etime>vm.stime1&&vm.etime<vm.etime1)||vm.stime==vm.stime1||vm.etime==vm.etime1){
                                        toastr.error('Trùng lịch với ca thời gian khác', 'Lỗi');
                                        return;
                                    }
                            }

                            }
                            vm.shiftWork.totalHours -= parseFloat(((vm.shiftWork.timePeriods[index].endTime - vm.shiftWork.timePeriods[index].startTime) / (60 * 60 * 1000)).toFixed(2));
                            vm.shiftWork.timePeriods[index].startTime =vm.time.start;
                            vm.shiftWork.timePeriods[index].endTime =vm.time.end;
                            vm.shiftWork.totalHours += parseFloat(((vm.shiftWork.timePeriods[index].endTime - vm.shiftWork.timePeriods[index].startTime) / (60 * 60 * 1000)).toFixed(2));
                            
                            toastr.info('Đã sửa thành công bản ghi', 'Thông báo');
                            
                            vm.bsTableControlListTime.options.data = vm.shiftWork.timePeriods;
                        }else  toastr.error('Lỗi thời gian, vui lòng nhập lại', 'Lỗi');
                        
        
                    } else {
                        toastr.error('Vui lòng nhập thông tin thời gian', 'Lỗi');
                    }
                }
            });
        }
        $scope.editShiftWork = function (shiftWorkId) {
            service.getShiftWork(shiftWorkId).then(function (data) {

                vm.shiftWork = data;
                vm.shiftWork.isNew = false;
                vm.bsTableControlListTime.options.data = vm.shiftWork.timePeriods;
                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_shift_work_modal.html',
                    scope: $scope,
                    size: 'lg'
                });


                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (!vm.shiftWork.code || vm.shiftWork.code.trim() == '') {
                            toastr.error('Vui lòng nhập mã ca làm việc.', 'Lỗi');
                            return;
                        }

                        if (!vm.shiftWork.name || vm.shiftWork.name.trim() == '') {
                            toastr.error('Vui lòng nhập tên ca làm việc.', 'Lỗi');
                            return;
                        }

                        service.saveShiftWork(vm.shiftWork, function success() {

                            // Refresh list
                            vm.getShiftWorks();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object

                            vm.shiftWork = {};
                            vm.shiftWork.timePeriods = [];
                            vm.bsTableControlListTime.options.data = vm.shiftWork.timePeriods;
                            vm.time.end = '';
                            vm.time.start = '';
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin bản ghi.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.shiftWork = {};
                    vm.shiftWork.timePeriods = [];
                    vm.time.end = '';
                    vm.time.start = '';
                    vm.bsTableControlListTime.options.data = vm.shiftWork.timePeriods;
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        /**
         * Delete accounts
         */
        vm.deleteShiftWorks = function () {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    console.log(vm.selectedShiftWorks);
                    service.deleteShiftWorks(vm.selectedShiftWorks, function success() {

                        // Refresh list
                        vm.getShiftWorks();

                        // Notify
                        toastr.info('Bạn đã xóa thành công ' + vm.selectedShiftWorks.length + ' bản ghi.', 'Thông báo');

                        // Clear selected accounts
                        vm.selectedShiftWorks = [];
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