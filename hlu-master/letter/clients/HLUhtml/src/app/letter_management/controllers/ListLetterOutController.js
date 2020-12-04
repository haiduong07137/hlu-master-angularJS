/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function() {
    'use strict';

    angular.module('Hrm.LetterManagement').controller('ListLetterOutController', ListLetterOutController);

    ListLetterOutController.$inject = [
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
        '$window',
        'FileSaver'
    ];
    angular.module('Hrm.LetterManagement').directive('fileDownload',function(){
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
                        var isChrome = !!window.chrome && !!window.chrome.webstore || window.chrome!=null;;
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
    function ListLetterOutController($rootScope, $scope, toastr, $timeout, settings, utils,
        modal, service, Uploader, $state, $window, FileSaver) {
        $scope.$on('$viewContentLoaded', function() {
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
        vm.letterIns = [];
        vm.pageSize = 25;
        vm.pageIndex = 1;
        vm.selectedletterIns = [];
        vm.searchObject = {};
        vm.letterDocFieldIds = [];
        vm.documentTypeIds = [];
        /*
         * 
         */
        function getAllLetterOutDocumentBy(stepIndex, currentParticipateStates, pageIndex, pageSize) {
            service.getAllLetterOutDocumentBy(stepIndex, currentParticipateStates, pageIndex, pageSize).then(function(data) {
                vm.letterIns = data.content;
                console.log(vm.letterIns);
                vm.bsTableControl.options.data = vm.letterIns;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        }
        getAllLetterOutDocumentBy(0, -1, vm.pageIndex, vm.pageSize);

        function searchPageLetterOut(stepIndex, currentParticipateStates, pageIndex, pageSize) {
            service.searchPageLetterOut(stepIndex, currentParticipateStates, vm.searchObject, pageIndex, pageSize).then(function(data) {
                vm.letterIns = data.content;
                console.log(vm.letterIns);
                vm.bsTableControl.options.data = vm.letterIns;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        }
        searchPageLetterOut(0, -1, vm.pageIndex, vm.pageSize);

        vm.searchByText = function() {
            if (vm.searchObject.dateFrom != null && vm.searchObject.dateTo == null) {
                if ((vm.searchObject.dateFrom).getTime() > new Date().getTime()) {
                    toastr.warning("Khoảng thời gian tìm kiếm không thỏa mãn!");
                } else {
                    vm.searchObject.dateTo = null;
                    searchPageLetterOut(0, -1, vm.pageIndex, vm.pageSize);
                }
            }
            if (vm.searchObject.dateFrom == null && vm.searchObject.dateTo != null) {
                if (vm.searchObject.dateTo > new Date()) {
                    toastr.warning("Khoảng thời gian tìm kiếm không thỏa mãn!");
                } else {
                    vm.searchObject.dateTo = new Date((vm.searchObject.dateTo).getTime() + 86399000);
                    searchPageLetterOut(0, -1, vm.pageIndex, vm.pageSize);
                }
            }
            if (vm.searchObject.dateFrom != null && vm.searchObject.dateTo != null) {
                if ((vm.searchObject.dateFrom).getTime() > new Date().getTime() || vm.searchObject.dateTo > new Date() || (vm.searchObject.dateFrom).getTime() > (vm.searchObject.dateTo).getTime()) {
                    toastr.warning("Khoảng thời gian tìm kiếm không thỏa mãn!");
                } else {
                    vm.searchObject.dateTo = new Date((vm.searchObject.dateTo).getTime() + 86399000);
                    searchPageLetterOut(0, -1, vm.pageIndex, vm.pageSize);
                }
            }
            if (vm.searchObject.dateFrom == null && vm.searchObject.dateTo == null) {
                vm.searchObject.dateTo = null;
                searchPageLetterOut(0, -1, vm.pageIndex, vm.pageSize);
            }
        }

        vm.getAllLetterDocFieldIds = function() {
            service.getAllLetterDocFieldIds().then(function(data) {
                vm.letterDocFieldIds = data.content;
            });
        }
        vm.getAllLetterDocFieldIds();

        vm.getAllDocumentTypeIds = function() {
            service.getAllDocumentTypeIds().then(function(data) {
                vm.documentTypeIds = data.content;
            });
        }
        vm.getAllDocumentTypeIds();

        vm.enterSearchCode = function() {
            console.log(event.keyCode);
            vm.pageIndexUser = 0;
            if (event.keyCode == 13) { //Phím Enter
                vm.searchByText();
            }
        };

        //        function getAllLetter(pageIndex, pageSize) {
        //            service.getAllLetter(pageIndex, pageSize).then(function (data) {
        //                vm.letterIns = data.content;
        //                console.log(vm.letterIns);
        //                vm.bsTableControl.options.data = vm.letterIns;
        //                vm.bsTableControl.options.totalRows = data.totalElements;
        //            });
        //        }
        //        getAllLetter(vm.pageIndex, vm.pageSize);

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
                columns: service.getTableLetterOutDocumentDefinition(),
                onCheck: function(row, $element) {
                    $scope.$apply(function() {
                        vm.selectedletterIns.push(row);
                    });
                },
                onCheckAll: function(rows) {
                    $scope.$apply(function() {
                        vm.selectedletterIns = rows;
                    });
                },
                onUncheck: function(row, $element) {
                    var index = utils.indexOf(row, vm.selectedletterIns);
                    if (index >= 0) {
                        $scope.$apply(function() {
                            vm.selectedletterIns.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function(rows) {
                    $scope.$apply(function() {
                        vm.selectedletterIns = [];
                    });
                },
                onPageChange: function(index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    getAllLetterOutDocumentBy(0, -1, vm.pageIndex, vm.pageSize);
                }
            }
        };

        $scope.viewDocument = function(id) {
            $state.go('application.letter_out_view', { letter_id: id, state: 'view' });
        }

        $scope.editDocument = function(id) {
            $state.go('application.letter_out_edit', { letter_id: id });
        }
        vm.hasClerkRole = true;
        vm.checkClerkRole = function() {
            service.checkUserHasTaskRoleByUserNameAndRoleCode($scope.currentUser.username, "ClerkRole").then(function(data) {
                vm.hasClerkRole = data;
            }).catch(function(err) {
                vm.hasClerkRole = true;
            });
        }
        vm.checkClerkRole();

        //export
		$scope.myBlobObject=null;
        $scope.getExportExcelLetterOut=function(){
            console.log('download started, you can show a wating animation');
            service.getExportExcelLetterOut(vm.searchObject)
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