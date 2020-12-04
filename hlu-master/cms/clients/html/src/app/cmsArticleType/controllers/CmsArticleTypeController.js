/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.CmsArticleType').controller('CmsArticleTypeController', CmsArticleTypeController);

    CmsArticleTypeController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'CmsArticleTypeService',
        'Upload'
    ];
    
    angular.module('Hrm.CmsArticleType').directive('fileDownload',function(){
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

    function CmsArticleTypeController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service,Upload) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        
        var vm = this;

        vm.articleType = {};
        vm.articleTypes = [];
        vm.selecteddepartments = [];

        vm.pageIndex = 0;
        vm.pageSize = 25;
        
        vm.getArticleTypes = function () {
            service.getArticleTypes(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.articleTypes = data.content;
                console.log( vm.articleTypes );
                vm.bsTableControl.options.data = vm.articleTypes;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        };
        
        vm.getArticleTypes();
        vm.bsTableControl = {
            options: {
                data: vm.articleTypes,
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
                    vm.getArticleTypes();
                }
            }
        };

        /**
         * New event account
         */
        vm.newArticleType = function () {

            vm.articleType.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_article_type_modal.html',
                scope: $scope,
                backdrop:'static',
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (!vm.articleType.code || vm.articleType.code.trim() == '') {
                        toastr.error('Vui lòng nhập mã phòng ban.', 'Lỗi');
                        return;
                    }

                    if (!vm.articleType.name || vm.articleType.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên phòng ban.', 'Lỗi');
                        return;
                    }
                    service.createArticleType(vm.articleType, function success() {

                        // Refresh list
                        vm.getArticleTypes();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một phòng ban.', 'Thông báo');

                        // clear object
                        vm.articleType = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một phòng ban.', 'Thông báo');
                    });
                }
            }, function () {
                vm.articleType = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editArticleType = function (id) {
            service.getArticleType(id).then(function (data) {

                vm.articleType = data;
                vm.articleType.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_article_type_modal.html',
                    scope: $scope,
                    backdrop:'static',
                    size: 'md'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (!vm.articleType.code || vm.articleType.code.trim() == '') {
                            toastr.error('Vui lòng nhập mã phòng ban.', 'Lỗi');
                            return;
                        }

                        if (!vm.articleType.name || vm.articleType.name.trim() == '') {
                            toastr.error('Vui lòng nhập tên phòng ban.', 'Lỗi');
                            return;
                        }

                        service.createArticleType(vm.articleType, function success() {

                            // Refresh list
                            vm.getArticleTypes();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object
                            vm.articleType = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin phòng ban.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.articleType = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        /**
         * Delete accounts
         */
        $scope.deleteArticleType = function (id) {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    service.deleteArticleById(id, function success() {

                        // Refresh list
                        vm.getArticleTypes();

                        // Notify
                        toastr.info('Bạn đã xóa thành công bản ghi.', 'Thông báo');

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