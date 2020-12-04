/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.Department').controller('DepartmentController', DepartmentController);

    DepartmentController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'DepartmentService',
        'Upload'
    ];
    
    angular.module('Hrm.Department').directive('fileDownload',function(){
        return{
            restrict:'A',
            scope:{
                fileDownload:'=',
                fileName:'=',
            },

            link:function(scope,elem,atrs){


                scope.$watch('fileDownload',function(newValue, oldValue){

                    if(newValue!=undefined && newValue!=null){
                        console.debug('Downloading a new file');
                        var isFirefox = typeof InstallTrigger !== 'undefined';
                        var isSafari = Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0;
                        var isIE = /*@cc_on!@*/false || !!document.documentMode;
                        var isEdge = !isIE && !!window.StyleMedia;
                        var isChrome = !!window.chrome && !!window.chrome.webstore;
                        var isOpera = (!!window.opr && !!opr.addons) || !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;
                        var isBlink = (isChrome || isOpera) && !!window.CSS;

                        if(isFirefox || isIE || isChrome){
                            if(isChrome){
                                console.log('Manage Google Chrome download');
                                var url = window.URL || window.webkitURL;
                                var fileURL = url.createObjectURL(scope.fileDownload);
                                var downloadLink = angular.element('<a></a>');//create a new  <a> tag element
                                downloadLink.attr('href',fileURL);
                                downloadLink.attr('download',scope.fileName);
                                downloadLink.attr('target','_self');
                                downloadLink[0].click();//call click function
                                url.revokeObjectURL(fileURL);//revoke the object from URL
                            }
                            if(isIE){
                                console.log('Manage IE download>10');
                                window.navigator.msSaveOrOpenBlob(scope.fileDownload,scope.fileName);
                            }
                            if(isFirefox){
                                console.log('Manage Mozilla Firefox download');
                                var url = window.URL || window.webkitURL;
                                var fileURL = url.createObjectURL(scope.fileDownload);
                                var a=elem[0];//recover the <a> tag from directive
                                a.href=fileURL;
                                a.download=scope.fileName;
                                a.target='_self';
                                a.click();//we call click function
                            }


                        }else{
                            alert('SORRY YOUR BROWSER IS NOT COMPATIBLE');
                        }
                    }
                });

            }
        }
    });

    function DepartmentController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service,Upload) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        
        var vm = this;

        vm.department = {};
        vm.departments = [];
        vm.selecteddepartments = [];

        vm.pageIndex = 0;
        vm.pageSize = 25;

        vm.typeOption=[
        	{
        		id:1,
        		name:'Phòng- Ban hành chính'
        	},
        	{
        		id:2,
        		name:'Khoa- Trung tâm đào tạo'
        	}
        ]
        
        vm.getdepartments = function () {
            service.getdepartments(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.departments = data.content;
                console.log( vm.departments );
                vm.bsTableControl.options.data = vm.departments;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        };
        
        vm.getdepartments();
        vm.bsTableControl = {
            options: {
                data: vm.departments,
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
                        vm.selecteddepartments.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selecteddepartments = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selecteddepartments);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selecteddepartments.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selecteddepartments = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index - 1;
                    vm.getdepartments();
                }
            }
        };

        /**
         * New event account
         */
        vm.newdepartment = function () {

            vm.department.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_department_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (!vm.department.code || vm.department.code.trim() == '') {
                        toastr.error('Vui lòng nhập mã phòng ban.', 'Lỗi');
                        return;
                    }

                    if (!vm.department.name || vm.department.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên phòng ban.', 'Lỗi');
                        return;
                    }
             /*
              *Đoạn này Giang thêm kiểu departmentType
              */
//                    if (!vm.department.departmentType || vm.department.departmentType.trim() == '') {
//                        toastr.error('Vui lòng nhập cấp phòng ban.', 'Lỗi');
                    
                    service.savedepartment(vm.department, function success() {

                        // Refresh list
                        vm.getdepartments();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một phòng ban.', 'Thông báo');

                        // clear object
                        vm.department = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một phòng ban.', 'Thông báo');
                    });
                }
            }, function () {
                vm.department = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };
        
        $scope.MAX_FILE_SIZE = '2MB';
        $scope.f = null;
        $scope.errFile = null;

        $scope.uploadFiles = function(file, errFiles) {
            $scope.f = file;
            $scope.errFile = errFiles && errFiles[0];
        };

        vm.baseUrl = settings.api.baseUrl + settings.api.apiV1Url;

        vm.startUploadFile = function(file) {
            console.log(file);
            if (file) {
                file.upload = Upload.upload({
                    url: vm.baseUrl + 'hr/file/importDepartment',
                    data: {uploadfile: file}
                });

                file.upload.then(function (response) {
                    console.log(response);
                    file.result = response.data;
                    vm.getdepartments();
                    toastr.info('Import thành công.', 'Thông báo');
                },function errorCallback(response) {
                    toastr.error('Import lỗi.', 'Lỗi');
                });
            }
        };

        vm.importDepartment = function () {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'import_modal.html',
                scope: $scope,
                size: 'md'
            });

            vm.student = {};
            $scope.f = null;
            $scope.errFile = null;

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    vm.startUploadFile($scope.f);
                    console.log($scope.f);
                }
            }, function () {
                vm.educationProgram = null;
                vm.address = {};
                console.log("cancel");
            });
        }

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editdepartment = function (departmentId) {
            service.getdepartment(departmentId).then(function (data) {

                vm.department = data;
                vm.department.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_department_modal.html',
                    scope: $scope,
                    size: 'md'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (!vm.department.code || vm.department.code.trim() == '') {
                            toastr.error('Vui lòng nhập mã phòng ban.', 'Lỗi');
                            return;
                        }

                        if (!vm.department.name || vm.department.name.trim() == '') {
                            toastr.error('Vui lòng nhập tên phòng ban.', 'Lỗi');
                            return;
                        }
                     /*
                      *Đoạn này Giang thêm kiểu departmentType
                      */
                        console.log(vm.department);
                        if ((vm.department.departmentType==null) || vm.department.departmentType == '') {
                            toastr.error('Vui lòng nhập cấp phòng ban.', 'Lỗi');
                            return;
                        }

                        service.updateDepartment(vm.department, function success() {

                            // Refresh list
                            vm.getdepartments();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object
                            vm.department = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin phòng ban.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.department = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        /**
         * Delete accounts
         */
        vm.deleteDepartments = function () {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    service.deleteDepartments(vm.selecteddepartments, function success() {

                        // Refresh list
                        vm.getdepartments();

                        // Notify
                        toastr.info('Bạn đã xóa thành công ' + vm.selecteddepartments.length + ' bản ghi.', 'Thông báo');

                        // Clear selected accounts
                        vm.selecteddepartments = [];
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi xóa bản ghi.', 'Lỗi');
                    });
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };
        $scope.myBlobObject=undefined;
        $scope.getFile=function(){
            console.log('download started, you can show a wating animation');
            service.getStream(vm.departments)
            .then(function(data){//is important that the data was returned as Aray Buffer
                console.log('Stream download complete, stop animation!');
                $scope.myBlobObject=new Blob([data],{ type:'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
            },function(fail){
                console.log('Download Error, stop animation and show error message');
                $scope.myBlobObject=[];
            });
        };
    }

})();