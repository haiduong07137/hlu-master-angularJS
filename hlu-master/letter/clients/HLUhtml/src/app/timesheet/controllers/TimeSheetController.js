/**
 * Author Giang 21/04/2018
 */
(function () {
    'use strict';

    angular.module('Hrm.TimeSheet').controller('TimeSheetController', TimeSheetController);

    TimeSheetController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'TimeSheetService'
    ];
    angular.module('Hrm.TimeSheet').directive('myDatePicker', function () {
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

    angular.module('Hrm.TimeSheet').directive("gdatepicker", function () {
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

    function TimeSheetController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        var vm = this;
        //$scope.mytime = new Date();

        $scope.hstep = 1;
        $scope.mstep = 15;
        vm.statuses = ['', ''];
        $scope.options = {
            hstep: [1, 2, 3],
            mstep: [1, 5, 10, 15, 25, 30]
        };
        vm.timeSheet = {};
        vm.timeSheets = [];
        vm.selectedTimeSheets = [];
        vm.selectedDetails=[];
        vm.staff = {};
        vm.staffs = [];
        vm.selectedStaff = null;
        var name = null;
        $scope.ID = '';
        vm.timeSheetDetail = {};
        vm.pageIndexStaff = 1;
        vm.pageSizeStaff = 10;
        vm.pageIndexDetail = 1;
        vm.PageSizeDetail = 15;
        vm.pageIndex = 1;
        vm.pageSize = 25;

        vm.staffShiftwork={};
        vm.staffShiftwork.staffs=[]
        vm.selectedStaffs=[];
        vm.selectedSt=[];
        vm.pageSizeSt=10;
        vm.pageIndexSt=1;
        vm.textSearch = '';


        vm.searchByDto = function() {
        	var searchDto = {};

            if (vm.datefilter != null || vm.datefilter != '') {
            	searchDto.workingDate = vm.datefilter;
			}
            vm.textSearch = String(vm.textSearch).trim();
            if (vm.textSearch != '') {
            	searchDto.codeAndName = vm.textSearch;
            }
            service.searchListByDto(searchDto, vm.pageIndex, vm.pageSize).then(function (data) {
                vm.timeSheets = data.content;
                vm.bsTableControl.options.data = vm.timeSheets;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
		}
        
        vm.getTimeSheets = function () {
            service.getTimeSheets(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.timeSheets = data.content;
                vm.bsTableControl.options.data = vm.timeSheets;
                vm.bsTableControl.options.totalRows = data.totalElements;
                vm.datefilter = '';
            });
        };

        vm.getStaffs = function () {
            service.getStaffs(vm.pageIndexSt, vm.pageSizeSt).then(function (data) {
                vm.staffs = data.content;
                vm.bsTableStaffControl.options.data = vm.staffs;
                vm.bsTableStaffControl.options.totalRows = data.totalElements;
            });
        };

        vm.getShiftWork = function () {
            service.getShiftWork(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.ShiftWorks = data.content;
            });
        };
 
        vm.getWorkingStatus= function () {
            service.getWorkingStatus(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.WorkingStatuses = data.content;
            });
        };
        vm.getWorkingStatus();
        vm.getShiftWork();
        vm.searchByDto();

        vm.bsTableControl = {
            options: {
                data: vm.timeSheets,
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
                        vm.selectedTimeSheets.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedTimeSheets = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedTimeSheets);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedTimeSheets.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedTimeSheets = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    vm.searchByDto();
                }
            }
        };
        vm.bsTableSelectedStaffControl = {
            options: {
                data: vm.staffShiftwork.staffs,
                idField: 'id',
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: false,
                showToggle: false,
                pagination: true,
                pageSize: 10,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                columns: service.getTableSelectedStaffDefinition(),
                // onCheck: function (row, $element) {
                //     $scope.$apply(function () {
                //         vm.selectedTimeSheets.push(row);
                //     });
                // },
                // onCheckAll: function (rows) {
                //     $scope.$apply(function () {
                //         vm.selectedTimeSheets = rows;
                //     });
                // },
                // onUncheck: function (row, $element) {
                //     var index = utils.indexOf(row, vm.selectedTimeSheets);
                //     if (index >= 0) {
                //         $scope.$apply(function () {
                //             vm.selectedTimeSheets.splice(index, 1);
                //         });
                //     }
                // },
                // onUncheckAll: function (rows) {
                //     $scope.$apply(function () {
                //         vm.selectedTimeSheets = [];
                //     });
                // },
                // onPageChange: function (index, pageSize) {
                //     vm.pageSize = pageSize;
                //     vm.pageIndex = index;
                //     vm.getTimeSheets();
                // }
            }
        };

        vm.bsTableStaffControl={
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
                pageSize: vm.pageSizeSt,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: service.getTableStaffDefinition(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedStaffs.push(row);
                    });
                    console.log(vm.selectedStaffs);
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        for(var i=0; i<rows.length; i++){
                            vm.selectedStaffs.push(rows[i]);
                        }
                    });
                    console.log(vm.selectedStaffs);
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedStaffs);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedStaffs.splice(index, 1);
                        });
                    }
                    console.log(vm.selectedStaffs);
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedStaffs.splice(vm.selectedStaffs.length-rows.length,rows.length);
                    });
                    console.log(vm.selectedStaffs);
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSizeSt = pageSize;
                    vm.pageIndexSt = index;
                    vm.getStaffs();
                }
            }

        }
        vm.bsTableDetailControl = {
            options: {
                data: vm.timeSheets,
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
                columns: service.getTableDefinitionDetail(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedDetails.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDetails = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedDetails);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedDetails.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDetails = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    vm.getTimeSheetDetails();
                }
            }
        };
        vm.bsTableControlSearchStaff = {
            options: {
                data: vm.staffs,
                idField: 'id',
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                singleSelect: true,
                showColumns: false,
                showToggle: false,
                pagination: true,
                pageSize: vm.pageSizeStaff,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: service.getTableDefinitionSearchStaff(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedStaff = row;
                        console.log(vm.selectedStaff);
                    });
                },
                onUncheck: function (row, $element) {
                    vm.selectedStaff = null;
                    console.log(vm.selectedStaff);
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSizeStaff = pageSize;
                    vm.pageIndexStaff = index;
                    vm.selectedStaff = null;
                    vm.searchStaffByName();
                }
            }
        };

        vm.getUserInfo = function () {
            service.getCurrentUser().then(function (data) {
                $rootScope.currentUser = data;
                vm.timeSheet.employee = $rootScope.currentUser.person;
            })
        }

        /**
         * New event account
         */
        vm.newTimeSheet = function () {

            vm.timeSheet.isNew = true;
            vm.getShiftWork();
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_time_sheet_modal.html',
                scope: $scope,
                size: 'md'
            });
            vm.getUserInfo();
            vm.timeSheet.startTime = new Date();
            vm.timeSheet.startTime.setSeconds(0);
            vm.timeSheet.endTime = new Date();
            vm.timeSheet.endTime.setSeconds(0);
            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (vm.timeSheet.startTime == null) {
                        toastr.error('Vui lòng nhập thời gian bắt đầu.', 'Lỗi');
                        return;
                    }

                    if (vm.timeSheet.endTime == null) {
                        toastr.error('Vui lòng nhập thời gian kết thúc.', 'Lỗi');
                        return;
                    }

                    if (vm.timeSheet.employee== null) {
                        toastr.error('Vui lòng nhập tên nhân viên.', 'Lỗi');
                        return;
                    }

                    if (vm.timeSheet.shiftWork== null || vm.timeSheet.shiftWork=='') {
                        toastr.error('Vui lòng nhập ca làm việc.', 'Lỗi');
                        return;
                    }
                    if (vm.timeSheet.workingDate== null || vm.timeSheet.workingDate=='') {
                        toastr.error('Vui lòng nhập ngày làm việc.', 'Lỗi');
                        return;
                    }
                    // if ( vm.timeSheet.workingStatus== null || vm.timeSheet.workingStatus=='') {
                    //     toastr.error('Vui lòng nhập trạng thái công việc.', 'Lỗi');
                    //     return;
                    // }
                   
                    
                    service.saveTimeSheet(vm.timeSheet, function success() {
                        console.log(vm.timeSheet);
                        // Refresh list
                        vm.searchByDto();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công.', 'Thông báo');

                        // clear object
                        vm.timeSheet = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới.', 'Thông báo');
                    });
                }
            }, function () {
                vm.timeSheet = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editTimeSheet = function (timeSheetId) {
            service.getTimeSheet(timeSheetId).then(function (data) {
                vm.timeSheet = {};
                vm.timeSheet = data;
                vm.timeSheet.isNew = false;


                console.log(vm.timeSheet.workingStatus);
                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_time_sheet_modal.html',
                    scope: $scope,
                    size: 'md'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (vm.timeSheet.startTime == null|| vm.timeSheet.startTime=='') {
                            toastr.error('Vui lòng nhập thời gian bắt đầu.', 'Lỗi');
                            return;
                        }
    
                        if (vm.timeSheet.endTime == null || vm.timeSheet.endTime=='') {
                            toastr.error('Vui lòng nhập thời gian kết thúc.', 'Lỗi');
                            return;
                        }
    
                        if (vm.timeSheet.employee== null) {
                            toastr.error('Vui lòng nhập tên nhân viên.', 'Lỗi');
                            return;
                        }
    
                        if (vm.timeSheet.shiftWork== null || vm.timeSheet.shiftWork=='') {
                            toastr.error('Vui lòng nhập ca làm việc.', 'Lỗi');
                            return;
                        }
                        if (vm.timeSheet.workingDate== null || vm.timeSheet.workingDate=='') {
                            toastr.error('Vui lòng nhập ngày làm việc.', 'Lỗi');
                            return;
                        }
                        if ( vm.timeSheet.workingStatus== null || vm.timeSheet.workingStatus=='') {
                            toastr.error('Vui lòng nhập trạng thái công việc.', 'Lỗi');
                            return;
                        }
                        service.saveTimeSheet(vm.timeSheet, function success() {

                            // Refresh list
                            vm.searchByDto();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object
                            vm.timeSheet = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin bản ghi.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.timeSheet = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };
         /**
         * Delete accounts
         */
       
        vm.deleteTimeSheets = function () {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    console.log(vm.selectedTimeSheets);
                    service.deleteTimeSheets(vm.selectedTimeSheets, function success() {

                        // Refresh list
                        vm.searchByDto();

                        // Notify
                        toastr.info('Bạn đã xóa thành công ' + vm.selectedTimeSheets.length + ' bản ghi.', 'Thông báo');

                        // Clear selected accounts
                        vm.selectedTimeSheets = [];
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi xóa bản ghi.', 'Lỗi');
                    });
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        // phê Duyệt timeSheet
        vm.confirmTimeSheets = function () {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_timesheets_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    console.log(vm.selectedTimeSheets);
                    service.confirmTimeSheets(vm.selectedTimeSheets, function success() {

                        // Refresh list
                        vm.searchByDto();

                        // Notify
                        toastr.info('Bạn đã phê duyệt thành công ' + vm.selectedTimeSheets.length + ' bản ghi.', 'Thông báo');

                        // Clear selected accounts
                        vm.selectedTimeSheets = [];
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi phê duyệt.', 'Lỗi');
                    });
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };
        $scope.remoteUrlRequestFn = function (str) {
            return {
                textSearch: str
            };
        };

        vm.createTimeSheets=function(){
            console.log(vm.staffShiftwork.staffs);
           
            vm.bsTableSelectedStaffControl.options.data=vm.staffShiftwork.staffs;
            vm.bsTableSelectedStaffControl.options.totalRows=vm.staffShiftwork.staffs.length;
            vm.getShiftWork();
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'create_time_sheet_modal.html',
                scope: $scope,
                size: 'md'
            });
            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (vm.staffShiftwork.fromDate == null) {
                        toastr.error('Vui lòng nhập ngày bắt đầu.', 'Lỗi');
                        return;
                    }

                    if (vm.staffShiftwork.toDate == null) {
                        toastr.error('Vui lòng nhập ngày kết thúc.', 'Lỗi');
                        return;
                    }

                    if (vm.staffShiftwork.shiftWork== null || vm.timeSheet.shiftWork=='') {
                        toastr.error('Vui lòng nhập ca làm việc.', 'Lỗi');
                        return;
                    }
                    if (vm.staffShiftwork.staffs== null || vm.staffShiftwork.staffs.length==0) {
                        toastr.error('Vui lòng thêm nhân viên.', 'Lỗi');
                        return;
                    }
                    if(!vm.staffShiftwork.workingOnSunday || vm.staffShiftwork.workingOnSunday == false) vm.staffShiftwork.workingOnSunday=0;
                    if(vm.staffShiftwork.workingOnSunday == true ) vm.staffShiftwork.workingOnSunday = 1;
                    service.createTimeSheets(vm.staffShiftwork, function success() {
                        console.log(vm.staffShiftwork);
                        // Refresh list
                        vm.searchByDto();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công.', 'Thông báo');

                        // clear object
                        vm.staffShiftwork = {};
                        vm.staffShiftwork.staffs = [];
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới.', 'Thông báo');
                    });
                }
            }, function () {
                vm.staffShiftwork = {};
                vm.staffShiftwork.staffs = [];
                console.log('Modal dismissed at: ' + new Date());
            });
        }
        //  timesheetdetail
        vm.getTimeSheetDetailbyTimesheetID = function (ID) {
            service.getTimeSheetDetailbyTimeSheetID(ID, vm.pageIndexDetail, vm.PageSizeDetail).then(function (data) {
                vm.timeSheetDetails = data.content;
                vm.bsTableDetailControl.options.data = vm.timeSheetDetails;
                vm.bsTableDetailControl.options.totalRows = data.totalElements;
            });
        };

        $scope.TimeSheetDetail = function (timeSheetId, displayname, workingdate) {
            service.getTimeSheetDetailbyTimeSheetID(timeSheetId, vm.pageIndexDetail, vm.PageSizeDetail).then(function (data) {
                $scope.ID = timeSheetId;
                vm.timeSheetDetails = data.content;
                vm.bsTableDetailControl.options.data = vm.timeSheetDetails;
                vm.bsTableDetailControl.options.totalRows = data.totalElements;
                vm.displayname = displayname;
                vm.workdate = workingdate;
                console.log(vm.workdate);
                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'timesheet_detail.html',
                    scope: $scope,
                    size: 'lg'
                });
                modalInstance.result.then(function (confirm) {}, );
            });
        };

        vm.newTimeSheetDetail = function () {

            vm.timeSheetDetail.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_time_sheet_detail_modal.html',
                scope: $scope,
                size: 'sm'
            });
            vm.timeSheetDetail.startTime = new Date();
            vm.timeSheetDetail.startTime.setSeconds(0);
            vm.timeSheetDetail.endTime = new Date();
            vm.timeSheetDetail.endTime.setSeconds(0);
            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (vm.timeSheetDetail.startTime == null ||vm.timeSheetDetail.startTime=='') {
                        toastr.error('Vui lòng nhập thời gian bắt đầu.', 'Lỗi');
                        return;
                    }

                    if (vm.timeSheetDetail.endTime == null || vm.timeSheetDetail.endTime=='' ) {
                        toastr.error('Vui lòng nhập thời gian kết thúc.', 'Lỗi');
                        return;
                    }
                    if (vm.timeSheetDetail.workingItemTitle == null || vm.timeSheetDetail.workingItemTitle=='' ) {
                        toastr.error('Vui lòng nhập thời gian kết thúc.', 'Lỗi');
                        return;
                    }




                    service.getTimeSheet($scope.ID).then(function (data) {
                        vm.timeSheet = {};
                        vm.timeSheet.details = [];

                        vm.timeSheet = data;
                        if(vm.timeSheet.details==null){
                            vm.timeSheet.details=[];
                        }

                        vm.timeSheet.details.push(vm.timeSheetDetail);


                        service.saveTimeSheet(vm.timeSheet, function success() {

                            // Refresh list
                            vm.getTimeSheetDetailbyTimesheetID($scope.ID);

                            // Notify
                            toastr.info('Bạn đã tạo mới thành công một công việc.', 'Thông báo');

                            // clear object
                            vm.timeSheetDetail = {};

                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi thêm mới mộtcông việc.', 'Thông báo');
                        });
                    });



                }
            }, function () {
                vm.timeSheetDetail = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editTimeSheetDetail = function (timeSheetDetailId) {
            service.getTimeSheetDetail(timeSheetDetailId).then(function (data) {

                vm.timeSheetDetail = data;
                vm.timeSheetDetail.isNew = false;
                vm.timeSheetDetail.timeSheet=null;
                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_time_sheet_detail_modal.html',
                    scope: $scope,
                    size: 'md'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (vm.timeSheetDetail.startTime == null ||vm.timeSheetDetail.startTime=='') {
                            toastr.error('Vui lòng nhập thời gian bắt đầu.', 'Lỗi');
                            return;
                        }
    
                        if (vm.timeSheetDetail.endTime == null || vm.timeSheetDetail.endTime=='' ) {
                            toastr.error('Vui lòng nhập thời gian kết thúc.', 'Lỗi');
                            return;
                        }
                        if (vm.timeSheetDetail.workingItemTitle == null || vm.timeSheetDetail.workingItemTitle=='' ) {
                            toastr.error('Vui lòng nhập thời gian kết thúc.', 'Lỗi');
                            return;
                        }
                        console.log(vm.timeSheetDetail);
                        service.saveTimeSheetDetail(vm.timeSheetDetail, function success() {

                            // Refresh list
                            vm.getTimeSheetDetailbyTimesheetID($scope.ID);

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object
                            vm.timeSheetDetail = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin bản ghi.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.timeSheetDetail = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        vm.deleteTimeSheetDetails = function () {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_detail_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    
                    service.deleteTimeSheetDetails(vm.selectedDetails, function success() {

                        // Refresh list
                        vm.getTimeSheetDetailbyTimesheetID($scope.ID);

                        // Notify
                        toastr.info('Bạn đã xóa thành công ' + vm.selectedDetails.length + ' bản ghi.', 'Thông báo');

                        // Clear selected accounts
                        vm.selectedDetails = [];
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi xóa bản ghi.', 'Lỗi');
                    });
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };
       
        vm.searchByCode = function () {
            vm.textSearch = String(vm.textSearch).trim();
            if (vm.textSearch != '') {
                service.getStaffByCode(vm.textSearch, 1,25).then(function (data) {
                    vm.timeSheets = data.content;
                    vm.bsTableControl.options.data = vm.timeSheets;
                    vm.bsTableControl.options.totalRows = data.totalElements;
                });
            }
            if (vm.textSearch == '') {
                vm.searchByDto();
            }
        };

        vm.enterSearchCode = function(){
            console.log(event.keyCode);
            vm.pageIndexUser = 0;
            if(event.keyCode == 13){//Phím Enter
                vm.searchByCode();
            }
        };


        $scope.localSearch = function (str, people) {
            //	      	service.searchStaffs(str).then(function (data) {
            //	            vm.staffs = data.content;
            //	        }); 
            //          return vm.staffs;
            var matches = [];
            people.forEach(function (person) {
                var fullName = person.firstName + ' ' + person.surname;
                if ((person.firstName.toLowerCase().indexOf(str.toString().toLowerCase()) >= 0) ||
                    (person.surname.toLowerCase().indexOf(str.toString().toLowerCase()) >= 0) ||
                    (fullName.toLowerCase().indexOf(str.toString().toLowerCase()) >= 0)) {
                    matches.push(person);
                }
            });
            return matches;
        };

        $scope.people = [{
                firstName: "Daryl",
                surname: "Rowland",
                twitter: "@darylrowland",
                pic: "img/daryl.jpeg"
            },
            {
                firstName: "Alan",
                surname: "Partridge",
                twitter: "@alangpartridge",
                pic: "img/alanp.jpg"
            },
            {
                firstName: "Annie",
                surname: "Rowland",
                twitter: "@anklesannie",
                pic: "img/annie.jpg"
            }
        ];
        $scope.search = function (str) {
            return service.searchStaffs(str);
        };


        vm.openSearchStaffModal = function () {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'search_staff_modal.html',
                scope: $scope,
                size: 'sm'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    if (vm.selectedStaff != null) {
                        vm.timeSheet.employee = vm.selectedStaff;
                    }
                    vm.bsTableControlSearchStaff.options.data = [];
                    vm.bsTableControlSearchStaff.options.totalRows = 0;
                    vm.staff = {};
                    vm.selectedStaff = null;
                    vm.pageIndexStaff = 1;
                    vm.pageSizeStaff = 10;
                }
            }, function () {
                vm.bsTableControlSearchStaff.options.data = [];
                vm.bsTableControlSearchStaff.options.totalRows = 0;
                vm.staff = {};
                vm.selectedStaff = null;
                vm.pageIndexStaff = 1;
                vm.pageSizeStaff = 10;
            });
        }

        vm.openStaffModal = function () {
            vm.getStaffs();
            vm.selectedStaffs=[];

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'staff_modal.html',
                scope: $scope,
                size: 'sm'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    if (vm.selectedStaffs.length > 0) {
                      for(var i=0; i<vm.selectedStaffs.length;i++){
                          var check=true;
                        for(var j=0; j<vm.staffShiftwork.staffs.length;j++)
                        {
                            if(vm.selectedStaffs[i].staffCode==vm.staffShiftwork.staffs[j].staffCode){
                                check=false;
                            } 
                        }
                        if(check==true) 
                        {
                            vm.staffShiftwork.staffs.push(vm.selectedStaffs[i]);
                        }
                      }
                    }
                    vm.bsTableStaffControl.options.data = [];
                    vm.bsTableStaffControl.options.totalRows = 0;
                    vm.bsTableSelectedStaffControl.options.data=vm.staffShiftwork.staffs;
                    vm.bsTableSelectedStaffControl.options.totalRows=vm.staffShiftwork.staffs.length;
                    vm.pageIndexSt = 1;
                    vm.pageSizeSt = 10;
                }
            }, function () {
                console.log("abcabc");
                vm.bsTableStaffControl.options.data = [];
                vm.bsTableStaffControl.options.totalRows = 0;
                vm.pageIndexSt = 1;
                vm.pageSizeSt = 10;
            });
        }


        vm.findStaff = function () {
            vm.pageIndexStaff = 1;
            vm.bsTableControlSearchStaff.state.pageNumber = 1;
            name = vm.staff.displayName;
            if (name != null && name != '')
                vm.searchStaffByName();
        }

        vm.findStaffs = function () {
            vm.pageIndexSt = 1;
            vm.bsTableStaffControl.state.pageNumber = 1;
            if (vm.staffname != null && vm.staffname != '')
                {
                    service.searchStaffByName(vm.staffname, vm.pageIndexSt, vm.pageSizeSt).then(function (data) {
                        vm.staffs = data.content;
                        vm.bsTableStaffControl.options.data = vm.staffs;
                        vm.bsTableStaffControl.options.totalRows = data.totalElements;
                        vm.staffname='';
                    });
                }
            else vm.getStaffs();
        }

        vm.addStaff= function(){
            vm.pageIndexSt = 1;
            vm.bsTableControlStaff.state.pageNumber = 1;
            vm.getStaffs();
        }
        vm.searchStaffByName = function () {
            if (vm.staff.displayName!=null && vm.staff.displayName!=''){
                service.searchStaffByName(name, vm.pageIndexStaff, vm.pageSizeStaff).then(function (data) {
                    vm.staffs = data.content;
                    vm.bsTableControlSearchStaff.options.data = vm.staffs;
                    vm.bsTableControlSearchStaff.options.totalRows = data.totalElements;
                });
            }
            
        }
       

       
        vm.changed = function () {
            if (vm.timeSheet.startTime != null && vm.timeSheet.endTime != null) {

                if (vm.timeSheet.startTime <= vm.timeSheet.endTime) {
                    vm.timeSheet.totalHours = ((vm.timeSheet.endTime - vm.timeSheet.startTime) / (60 * 60 * 1000)).toFixed(2);
                }
                
                //        		else{
                //        			vm.timeSheet.totalHours=null;
                //        			toastr.warning('Vui lòng chọn thời gian kết thúc lớn hơn thời gian bắt đầu', 'Cảnh báo');
                //        		}
            }
        }

        vm.changedDetail = function () {
            if (vm.timeSheetDetail.startTime != null && vm.timeSheetDetail.endTime != null) {

                if (vm.timeSheetDetail.startTime <= vm.timeSheetDetail.endTime) {
                    vm.timeSheetDetail.duration = ((vm.timeSheetDetail.endTime - vm.timeSheetDetail.startTime) / (60 * 60 * 1000)).toFixed(2);
                }
                console.log(vm.timeSheetDetail.duration);
                //        		else{
                //        			vm.timeSheet.totalHours=null;
                //        			toastr.warning('Vui lòng chọn thời gian kết thúc lớn hơn thời gian bắt đầu', 'Cảnh báo');
                //        		}
            }
        }
        /*vm.filter = function () {
            if (vm.datefilter == null || vm.datefilter == '') {
                vm.searchByDto();
            } else {
                var formattedDate = vm.datefilter.getFullYear() + "-" + (vm.datefilter.getMonth() + 1) + "-" + vm.datefilter.getDate()
                service.getTimeSheetbyWorkingdate(formattedDate, 1, 25).then(function (data) {
                    vm.datefilters = data.content;
                    vm.bsTableControl.options.data = vm.datefilters;
                    vm.bsTableControl.options.totalRows = data.totalElements;
                });
            }

        }*/
        
        
        
        
    }

})();