/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.CmsCategory').controller('CmsCategoryController', CmsCategoryController);

    CmsCategoryController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'CmsCategoryService',
        'Upload'
    ];
    
    angular.module('Hrm.CmsCategory').directive('fileDownload',function(){
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

    function CmsCategoryController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service,Upload) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        
        var vm = this;

        vm.category = {};
        vm.categorys = [];
        vm.selectedCategories = [];

        vm.pageIndex = 0;
        vm.pageSize = 25;
        
        vm.getPageCategory = function () {
            service.getPageCategory(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.categorys = data.content;
                console.log( vm.categorys );
                vm.bsTableControl.options.data = vm.categorys;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        };
        
        vm.getPageCategory();
        vm.bsTableControl = {
            options: {
                data: vm.categorys,
                idField: 'id',
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                // showColumns: true,
                // showToggle: true,
                pagination: true,
                pageSize: vm.pageSize,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: service.getTableDefinition(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedCategories.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedCategories = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedCategories);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedCategories.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedCategories = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    vm.getPageCategory();
                }
            }
        };

        /**
         * New event account
         */
        vm.newCategory = function () {

            vm.category.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_category_modal.html',
                scope: $scope,
                backdrop:'static',
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (!vm.category.title || vm.category.title.trim() == '') {
                        toastr.error('Vui lòng nhập tiêu đề.', 'Lỗi');
                        return;
                    }

                    if (!vm.category.description || vm.category.description.trim() == '') {
                        toastr.error('Vui lòng nhập mô tả.', 'Lỗi');
                        return;
                    }
           
                    service.createCategory(vm.category, function success() {

                        // Refresh list
                        vm.getPageCategory();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một.', 'Thông báo');

                        // clear object
                        vm.category = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới.', 'Thông báo');
                    });
                }
            }, function () {
                vm.category = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };
        /**
         * Edit a account
         * @param accountId
         */
        $scope.editCategory = function (id) {
            service.getCategoryById(id).then(function (data) {

                vm.category = data;
                vm.category.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_category_modal.html',
                    scope: $scope,
                    backdrop: 'static',
                    size: 'md'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (!vm.category.title || vm.category.title.trim() == '') {
                            toastr.error('Vui lòng nhập tiêu đề.', 'Lỗi');
                            return;
                        }

                        if (!vm.category.description || vm.category.description.trim() == '') {
                            toastr.error('Vui lòng nhập mô tả.', 'Lỗi');
                            return;
                        }

                        service.updateCategory(vm.category, function success() {

                            // Refresh list
                            vm.getPageCategory();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object
                            vm.category = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.category = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        
        $scope.deleteCategory = function (id) {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    service.deleteCategoryById(id, function success() {

                        // Refresh list
                        vm.getPageCategory();

                        // Notify
                        toastr.info('Bạn đã xóa thành công bản ghi.', 'Thông báo');

                        // Clear selected accounts
                        vm.selectedCategories = [];
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