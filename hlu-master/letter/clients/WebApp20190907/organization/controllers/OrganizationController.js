/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.Organization').controller('OrganizationController', OrganizationController);

    OrganizationController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'OrganizationService',
        'Upload',
        'focus'
    ];

    angular.module('Hrm.Organization').directive('fileDownload', function () {
        return {
            restrict: 'A',
            scope: {
                fileDownload: '=',
                fileName: '=',
            },

            link: function (scope, elem, atrs) {


                scope.$watch('fileDownload', function (newValue, oldValue) {

                    if (newValue != undefined && newValue != null) {
                        console.debug('Downloading a new file');
                        var isFirefox = typeof InstallTrigger !== 'undefined';
                        var isSafari = Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0;
                        var isIE = /*@cc_on!@*/false || !!document.documentMode;
                        var isEdge = !isIE && !!window.StyleMedia;
                        var isChrome = !!window.chrome && !!window.chrome.webstore;
                        var isOpera = (!!window.opr && !!opr.addons) || !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;
                        var isBlink = (isChrome || isOpera) && !!window.CSS;

                        if (isFirefox || isIE || isChrome) {
                            if (isChrome) {
                                console.log('Manage Google Chrome download');
                                var url = window.URL || window.webkitURL;
                                var fileURL = url.createObjectURL(scope.fileDownload);
                                var downloadLink = angular.element('<a></a>');//create a new  <a> tag element
                                downloadLink.attr('href', fileURL);
                                downloadLink.attr('download', scope.fileName);
                                downloadLink.attr('target', '_self');
                                downloadLink[0].click();//call click function
                                url.revokeObjectURL(fileURL);//revoke the object from URL
                            }
                            if (isIE) {
                                console.log('Manage IE download>10');
                                window.navigator.msSaveOrOpenBlob(scope.fileDownload, scope.fileName);
                            }
                            if (isFirefox) {
                                console.log('Manage Mozilla Firefox download');
                                var url = window.URL || window.webkitURL;
                                var fileURL = url.createObjectURL(scope.fileDownload);
                                var a = elem[0];//recover the <a> tag from directive
                                a.href = fileURL;
                                a.download = scope.fileName;
                                a.target = '_self';
                                a.click();//we call click function
                            }


                        } else {
                            alert('SORRY YOUR BROWSER IS NOT COMPATIBLE');
                        }
                    }
                });

            }
        }
    });

    function OrganizationController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service, Upload, focus) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;


        var vm = this;

        vm.organization = {};
        vm.organizationDetail = {};
        vm.organizationChild = {};
        vm.organizations = [];
        vm.childrens = [];
        vm.selectedOrganizations = [];
        vm.selectedNode = {};

        //pagination
        vm.pageIndex = 1;
        vm.pageSize = 25;
        vm.totalItems = 0;
        vm.limitedSize = 10000; //for list all

        vm.typeOption = [
            {
                id: 1,
                name: 'Phòng- Ban hành chính'
            },
            {
                id: 2,
                name: 'Khoa- Trung tâm đào tạo'
            }
        ]

        /**
         * New Organization
         * edit Organization
         * update line path
         */

        //check duplicate code
        vm.viewCheckDuplicateCode = {};
        vm.tempCodeDetail = '';
        vm.tempCode = '';
        vm.searchKeyword = '';
        var modalInstance;

        function checkDuplicateCode(code,type,action){ //type: 1 -> save; 2 -> edit;   action: 1 -> just check code; 2 -> save or edit
            service.checkDuplicateCode(code).then(function(data) {
                vm.viewCheckDuplicateCode = data;
                if(action == 1){
                    if(type == 1){
                        if(vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == true){
                            toastr.warning("Mã bị trùng");
                        }
                        if(vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == false){
                            toastr.success("Mã không bị trùng");
                        }
                    }
                    if(type == 2){
                        if(vm.tempCode.toLowerCase().trim() != code.toLowerCase().trim()){
                            checkDuplicateCode(vm.tempCode,1,1);
                        }else{
                            toastr.info("Mã chưa thay đổi");
                        }
                    }
                }
                if(action == 2){
                    if(type == 1){
                        if(vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == true){
                            toastr.warning("Mã bị trùng");
                        }
                        if(vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == false){
                            service.saveOrganization(vm.organization, function success() {
                                getOrganizationTree();
                                vm.reloadTree();
                                toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');
                                vm.organization = {};
                                modalInstance.close();
                            }, function failure() {
                                toastr.error('Có lỗi xảy ra khi lưu thông tin phòng ban.', 'Lỗi');
                            });
                        }
                    }
                    if(type == 2){
                        if(vm.tempCode.toLowerCase().trim() != code.toLowerCase().trim()){
                            service.checkDuplicateCode(vm.tempCode).then(function(data) {
                                vm.viewCheckDuplicateCode = data;
                                if(vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == true){
                                    toastr.warning("Mã bị trùng");
                                }
                                if(vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == false){
                                    vm.organizationChild.code = vm.tempCode.trim();
                                    service.updateOrganization(vm.organizationChild, function success() {
                                        getOrganizationTree();
                                        vm.reloadTree();
                                        toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');
                                        vm.organizationChild = {};
                                        vm.searchKeyword = '';
                                        modalInstance.close();
                                    }, function failure() {
                                        toastr.error('Có lỗi xảy ra khi lưu thông tin phòng ban.', 'Lỗi');
                                    });
                                }
                            });
                        }else{
                            vm.organizationChild.code = vm.tempCode.trim();
                            service.updateOrganization(vm.organizationChild, function success() {
                                getOrganizationTree();
                                vm.reloadTree();
                                toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');
                                vm.organizationChild = {};
                                vm.searchKeyword = '';
                                modalInstance.close();
                            }, function failure() {
                                toastr.error('Có lỗi xảy ra khi lưu thông tin phòng ban.', 'Lỗi');
                            });
                        }
                    }
                }
                console.log(data);

            });
        };

        vm.checkDuplicateCode = function (type,action,organization) {
            if(validateOrganization(organization)){
                checkDuplicateCode(organization.code,type,action);
            }
        };

        vm.newOrganization = function () {
            vm.organization = {};

            modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_organization_modal.html',
                scope: $scope,
                backdrop: false,
                size: 'lg'
            });

            vm.close = function (confirm) {
                if (confirm == 'yes') {
                    //check null
                    if (!vm.organization.code || vm.organization.code.trim() == '') {
                        toastr.warning('Vui lòng nhập mã đơn vị.', 'Lỗi');
                        focus('code');
                        return;
                    }
                    if (!vm.organization.name || vm.organization.name.trim() == '') {
                        toastr.warning('Vui lòng nhập tên đơn vị.', 'Lỗi');
                        focus('name');
                        return;
                    }
                    service.saveOrganization(vm.organization, function success() {
                        getOrganizationTree();
                        vm.reloadTree();
                        toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');
                        vm.organization = {};
                        modalInstance.close();
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi lưu thông tin phòng ban.', 'Lỗi');
                    });
                }
            }, function () {
                vm.organization = {};
                console.log('Modal dismissed at: ' + new Date());
            };
        };

        $scope.editOrganization = function (organizationId) {
            service.getOrganization(organizationId).then(function (data) {
                vm.organizationChild = data;
                vm.tempCode = vm.organizationChild.code;
                //alert(vm.organizationChild.parent.name);
                modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_organization_child_modal.html',
                    scope: $scope,
                    backdrop: false,
                    size: 'lg'
                });

                vm.close = function (confirm) {
                    if (confirm == 'yes') {
                        if (!vm.organization.code || vm.organization.code.trim() == '') {
                            toastr.warning('Vui lòng nhập mã đơn vị.', 'Lỗi');
                            focus('code');
                            return;
                        }
                        if (!vm.organization.name || vm.organization.name.trim() == '') {
                            toastr.warning('Vui lòng nhập tên đơn vị.', 'Lỗi');
                            focus('name');
                            return;
                        }

                        service.updateOrganization(vm.organization, function success() {
                            // getTreeData(vm.pageIndex, vm.limitedSize, vm.selectedNode);
                            getOrganizationTree();
                            vm.reloadTree();
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');
                            vm.organization = {};
                            vm.getOrganizationDetailById(vm.organizationDetail.id);
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin phòng ban.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.organization = {};
                    console.log('Modal dismissed at: ' + new Date());
                };
            });
        };

        vm.getOrganizationDetailById = function (organizationId) {
            service.getOrganization(organizationId).then(function (data) {
                vm.organizationDetail = data;
                vm.childrens = vm.organizationDetail.subDepartments;
                vm.bsTableControl.options.data = vm.childrens;
            });
        };

        //check duplicate code Organization detail
        function checkDuplicateCodeDetail(code,type,action){ //type: 1 -> save; 2 -> edit;   action: 1 -> just check code; 2 -> save or edit
            service.checkDuplicateCode(code).then(function(data) {
                vm.viewCheckDuplicateCode = data;
                if(action == 1){
                    if(type == 1){
                        if(vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == true){
                            toastr.warning("Mã bị trùng");
                        }
                        if(vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == false){
                            toastr.success("Mã không bị trùng");
                        }
                    }
                    if(type == 2){
                        if(vm.tempCodeDetail.toLowerCase().trim() != code.toLowerCase().trim()){
                            checkDuplicateCodeDetail(vm.tempCodeDetail,1,1);
                        }else{
                            toastr.info("Mã chưa thay đổi");
                        }
                    }
                }
                if(action == 2){
                    if(type == 2){
                        if(vm.tempCodeDetail.toLowerCase().trim() != code.toLowerCase().trim()){
                            service.checkDuplicateCode(vm.tempCodeDetail).then(function(data) {
                                vm.viewCheckDuplicateCode = data;
                                if(vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == true){
                                    toastr.warning("Mã bị trùng");
                                }
                                if(vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == false){
                                    vm.organizationDetail.code = vm.tempCodeDetail.trim();
                                    service.updateOrganization(vm.organizationDetail, function success() {
                                        getOrganizationTree();
                                        vm.reloadTree();
                                        toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');
                                        vm.organizationDetail = {};
                                        vm.searchKeyword = '';
                                        modalInstance.close();
                                    }, function failure() {
                                        toastr.error('Có lỗi xảy ra khi lưu thông tin phòng ban.', 'Lỗi');
                                    });
                                }
                            });
                        }else{
                            vm.organizationDetail.code = vm.tempCodeDetail.trim();
                            service.updateOrganization(vm.organizationDetail, function success() {
                                getOrganizationTree();
                                vm.reloadTree();
                                toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');
                                vm.organizationDetail = {};
                                vm.searchKeyword = '';
                                modalInstance.close();
                            }, function failure() {
                                toastr.error('Có lỗi xảy ra khi lưu thông tin phòng ban.', 'Lỗi');
                            });
                        }
                    }
                }
                console.log(data);

            });
        };

        vm.checkDuplicateCodeDetail = function (type,action,organization) {
            if(validateOrganization(organization)){
                checkDuplicateCodeDetail(vm.organizationDetail.code,type,action);
            }
        };

        vm.updateOrganizationDetail = function (organizationId) {
            if (organizationId != null) {
                service.getOrganization(organizationId).then(function (data) {
                    vm.organization = data;
                    // alert(vm.organization.name);
                    // console.log( vm.organizationDetail.parent.name);
                    vm.tempCodeDetail = vm.organizationDetail.code;
                    modalInstance = modal.open({
                        animation: true,
                        templateUrl: 'edit_organization_detail_modal.html',
                        scope: $scope,
                        backdrop:false,
                        size: 'lg'
                    });
                    vm.close = function (confirm) {
                        if (confirm == 'yes') {

                            if (!vm.organizationDetail.code || vm.organizationDetail.code.trim() == '') {
                                toastr.warning('Vui lòng nhập mã đơn vị.', 'Lỗi');
                                focus('code');
                                return;
                            }
                            if (!vm.organizationDetail.name || vm.organizationDetail.name.trim() == '') {
                                toastr.warning('Vui lòng nhập tên đơn vị.', 'Lỗi');
                                focus('name');
                                return;
                            }

                            service.updateOrganization(vm.organizationDetail, function success() {
                                getOrganizationTree();
                                vm.reloadTree();
                                toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');
                                vm.organizationDetail = {};
                                vm.searchKeyword = '';
                                modalInstance.close();
                            }, function failure() {
                                toastr.error('Có lỗi xảy ra khi lưu thông tin phòng ban.', 'Lỗi');
                            });
                        }
                    }, function () {
                        vm.getOrganizationDetailById(vm.organizationDetail.id);
                        vm.searchKeyword = '';
                    };
                });
            }
        };

        vm.updateLinePath = function () {
            service.updateLinePath(function success() {
                getOrganizationTree();
                vm.reloadTree();
                toastr.info('Cập nhật thành công.', 'Thông báo');
            }, function failure() {
                toastr.error('Có lỗi xảy ra khi cập nhật.', 'Thông báo');
            });
        };

        /**
         * Delete
         */
        vm.deleteOrganizations = function () {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    service.deleteOrganizations(vm.selectedOrganizations, function success() {

                        // Refresh list
                        // vm.getOrganizations();
                        getOrganizationTree();
                        vm.reloadTree();

                        // Notify
                        toastr.info('Bạn đã xóa thành công ' + vm.selectedOrganizations.length + ' bản ghi.', 'Thông báo');

                        // Clear selected accounts
                        vm.selectedOrganizations = [];
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi xóa bản ghi.', 'Lỗi');
                    });
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        vm.deleteDetailOrganizations = function () {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_detail_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    service.deleteOrganizationById(vm.organizationDetail.id, function success() {
                        // Refresh list
                        getOrganizationTree();
                        vm.reloadTree();
                        // Notify
                        toastr.info('Bạn đã xóa thành công ' + vm.selectedOrganizations.length + ' bản ghi.', 'Thông báo');
                        // Clear selected accounts
                        vm.selectedOrganizations = [];
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi xóa bản ghi.', 'Lỗi');
                    });
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Upload file
         * import
         * export
         */
        $scope.MAX_FILE_SIZE = '2MB';
        $scope.f = null;
        $scope.errFile = null;

        $scope.uploadFiles = function (file, errFiles) {
            $scope.f = file;
            $scope.errFile = errFiles && errFiles[0];
        };

        vm.baseUrl = settings.api.baseUrl + settings.api.apiV1Url;

        vm.startUploadFile = function (file) {
            console.log(file);
            if (file) {
                file.upload = Upload.upload({
                    url: vm.baseUrl + 'hr/file/importOrganization',
                    data: {uploadfile: file}
                });

                file.upload.then(function (response) {
                    console.log(response);
                    file.result = response.data;
                    vm.getOrganizations();
                    toastr.info('Import thành công.', 'Thông báo');
                }, function errorCallback(response) {
                    toastr.error('Import lỗi.', 'Lỗi');
                });
            }
        };

        vm.importOrganization = function () {
            modalInstance = modal.open({
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

        $scope.myBlobObject = undefined;
        $scope.getFile = function () {
            console.log('download started, you can show a wating animation');
            service.getStream(vm.organizations)
                .then(function (data) {//is important that the data was returned as Aray Buffer
                    console.log('Stream download complete, stop animation!');
                    $scope.myBlobObject = new Blob([data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
                }, function (fail) {
                    console.log('Download Error, stop animation and show error message');
                    $scope.myBlobObject = [];
                });
        };

        // Tree view
        vm.treeData = [];
        $scope.treeInstance = null;
        // $scope.treeConfig.version = 0;
        $scope.treeConfig = {
            core: {
                error: function (error) {
                    $log.error('treeCtrl: error from js tree - ' + angular.toJson(error));
                },
                check_callback: true
            },
            plugins: ['types', 'state', 'search']
            // version: 1
        };

        $scope.readyCB = function () {
            // getOrganizationTree();
        }
        $scope.selectNode = function (node, selected, event) {
            //console.log(selected.node.id);
            //console.log(selected.node.text);
            vm.getOrganizationDetailById(selected.node.id);
        }

        $scope.search = '';

        $scope.applySearch = function () {
            // getOrganizationTree();
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

        function getOrganizationTree() {
            service.getOrganizationTree().then(function (data) {
                vm.treeData = data;
                vm.organizations = data;
                $scope.treeConfig.version = 1;
            });
        }
        vm.reloadTree = function () {
            $scope.treeConfig.version++;
        };
        getOrganizationTree();

        vm.bsTableControl = {
            options: {
                data: vm.childrens,
                idField: 'id',
                height: 347,
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: false,
                showToggle: false,
                pagination: false,
                pageSize: vm.pageSize,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: service.getTableDefinition(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedOrganizations.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedOrganizations = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedOrganizations);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedOrganizations.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedOrganizations = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index - 1;
                    vm.getOrganizations();
                }
            }
        };

        function validateOrganization(organization){
            if (!organization.code || organization.code.trim() == '') {
                toastr.warning('Vui lòng nhập mã phòng ban.');
                return false;
            }

            if (!organization.name || organization.name.trim() == '') {
                toastr.warning('Vui lòng nhập tên phòng ban.');
                return false;
            }
            return true;
        }
    }

})();