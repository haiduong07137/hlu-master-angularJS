/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function() {
    'use strict';

    angular.module('Hrm.LetterManagement').controller('LetterManagementController', LetterManagementController);

    LetterManagementController.$inject = [
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
        'FileSaver',
        '$sce',
        'letterIndocumentFactory'
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
    
    function LetterManagementController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service, Upload, $state, FileSaver, $sce, letterIndocumentFactory) {
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
        vm.textSearch = "";
        vm.documentTypeIds = [];
        vm.letterDocFieldIds = [];
        vm.documentTypeId = 0;
        vm.letterDocFieldId = 0;
        vm.searchObject = {};
        /*
         * 
         */

        vm.searchByText = function() {
            getPageLetterInByText(-1, -1, vm.pageSize, vm.pageIndex);
        }

        vm.enterSearchCode = function() {
            console.log(event.keyCode);
            vm.pageIndexUser = 0;
            if (event.keyCode == 13) { //Phím Enter
                vm.searchByText();
            }
        };

        vm.getAllDocumentTypeIds = function() {
            service.getAllDocumentTypeIds().then(function(data) {
                vm.documentTypeIds = data.content;
            });
        }
        vm.getAllDocumentTypeIds();

        vm.getAllLetterDocFieldIds = function() {
            service.getAllLetterDocFieldIds().then(function(data) {
                vm.letterDocFieldIds = data.content;
            });
        }
        vm.getAllLetterDocFieldIds();
        vm.searchByText = function() {
            if (vm.searchObject.dateFrom != null && vm.searchObject.dateTo == null) {
                if ((vm.searchObject.dateFrom).getTime() > new Date().getTime()) {
                    toastr.warning("Khoảng thời gian tìm kiếm không thỏa mãn!");
                } else {
                    vm.searchObject.dateTo = null;
                    getPageLetterInByText(-1, -1, vm.pageIndex, vm.pageSize);
                }
            }
            if (vm.searchObject.dateFrom == null && vm.searchObject.dateTo != null) {
                if (vm.searchObject.dateTo > new Date()) {
                    toastr.warning("Khoảng thời gian tìm kiếm không thỏa mãn!");
                } else {
                    vm.searchObject.dateTo = new Date((vm.searchObject.dateTo).getTime());
                    getPageLetterInByText(-1, -1, vm.pageIndex, vm.pageSize);
                }
            }
            if (vm.searchObject.dateFrom != null && vm.searchObject.dateTo != null) {
                if ((vm.searchObject.dateFrom).getTime() > new Date().getTime() || vm.searchObject.dateTo.getTime() > new Date().getTime() || (vm.searchObject.dateFrom).getTime() > (vm.searchObject.dateTo).getTime()) {
                    toastr.warning("Khoảng thời gian tìm kiếm không thỏa mãn!");
                } else {
                    vm.searchObject.dateTo = new Date((vm.searchObject.dateTo).getTime());
                    getPageLetterInByText(-1, -1, vm.pageIndex, vm.pageSize);
                }
            }
            if (vm.searchObject.dateFrom == null && vm.searchObject.dateTo == null) {
                vm.searchObject.dateTo = null;
                getPageLetterInByText(-1, -1, vm.pageIndex, vm.pageSize);
            }
        }

        $scope.ListRoleOfUser = [];

        vm.getUserRoles = function() {
            service.getUserRoles().then(function(data) {
                $scope.ListRoleOfUser = data.content;
            });
        }

        function getPageLetterInByText(stepIndex, currentParticipateStates, pageSize, pageIndex) {
            service.searchPageLetter(stepIndex, currentParticipateStates, vm.searchObject, vm.pageSize, vm.pageIndex).then(function(data) {
                vm.letterIns = data.content;
                console.log(vm.letterIns);
                vm.bsTableControl.options.data = vm.letterIns;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        }

        getPageLetterInByText(-1, -1, vm.pageIndex, vm.pageSize);

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
                    getPageLetterInByText(-1, -1, vm.textSearch, vm.pageIndex, vm.pageSize);
                }
            }
        };

        $scope.forwardLetter = function(letterId) {
            $state.go('application.forward_letter_in', { letter_id: letterId, state: 'forward' });
        }

        $scope.editDocument = function(letterId) {
            $state.go('application.edit_letter_in', { letter_id: letterId });
        }

        $scope.forwardLetterStepTwo = function(letterId) {
            $state.go('application.transfer_process_letter_in', { letter_id: letterId })
        }

        $scope.viewDocument = function(letterId) {
            letterIndocumentFactory.letterFunc(letterId, 'view');
        }
        $scope.assignLetter = function(letterId) {
            $state.go('application.assigner_letter_in', { letter_id: letterId });
        }

        $scope.viewAllFileInDocument = function(letterId) {
            vm.ListAttachment = [];
            if (letterId != null && vm.letterIns != null && vm.letterIns.length > 0) {
                for (var i = 0; i < vm.letterIns.length; i++) {
                    if (vm.letterIns[i].id == letterId) {
                        vm.ListAttachment = vm.letterIns[i].attachments;

                        vm.modalInstanceListFile = modal.open({
                            animation: true,
                            templateUrl: 'viewAllFileInDocument.html',
                            scope: $scope,
                            backdrop: 'static',
                            size: 'lg'
                        });

                        return;
                    }
                }
            }
        };

        $scope.viewFileInBrowser = function(attachment) {
            vm.fileView = [];
            vm.linkFile = "";
            if (attachment != null) {
                vm.fileView = attachment;
                service.getFileById(attachment.file.id).success(function(data) {
                    var file = new Blob([data], { type: attachment.file.contentType });
                    var fileURL = URL.createObjectURL(file);
                    $scope.content = $sce.trustAsResourceUrl(fileURL);

                    vm.modalInstanceListFile = modal.open({
                        animation: true,
                        templateUrl: 'viewFileInBrowser.html',
                        scope: $scope,
                        backdrop: 'static',
                        size: 'lg'
                    });

                });
            }
        };


        vm.downloadDocument = function(attachment) {
            if (attachment != null) {
                var attachment = attachment;

                service.getFileById(attachment.file.id).success(function(data) {
                    var file = new Blob([data], { type: attachment.file.contentType });
                    FileSaver.saveAs(file, attachment.file.name);
                });;
            }
        }
        vm.currentUser = {};
        vm.getCurrentUser = function (){
            debugger
            service.getCurrentUser().then(function(data){
                if(data!= null){
                    vm.currentUser = data;
                    console.log(vm.currentUser);
                }
            });
        }
        vm.getCurrentUser();

        vm.hasClerkRole = true;
        vm.checkClerkRole = function() {
            debugger
            service.checkUserHasTaskRoleByUserNameAndRoleCode(vm.currentUser.username, "ClerkRole").then(function(data) {
                vm.hasClerkRole = data;
            }).catch(function(err) {
                vm.hasClerkRole = true;
            });
        }
        vm.checkClerkRole();

        $scope.checkViewDocument = function(id) {
            vm.letterId = id;
            vm.searchObject.fullname = null;
            service.checkViewDocument(vm.letterId, vm.searchObject, vm.searchUserIndex, vm.searchUserSize).then(function(data) {
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

        vm.searchUser = function() {
            service.checkViewDocument(vm.letterId, vm.searchObject, vm.searchUserIndex, vm.searchUserSize).then(function(data) {
                vm.users = data.content;
                vm.totalItemsUser = data.totalElements;
            });
        }

        vm.enterSearchUser = function() {
            console.log(event.keyCode);
            vm.pageIndexUser = 0;
            if (event.keyCode == 13) { //Phím Enter
                service.checkViewDocument(vm.letterId, vm.searchObject, vm.searchUserIndex, vm.searchUserSize).then(function(data) {
                    vm.users = data.content;
                    vm.totalItemsUser = data.totalElements;
                });
            }
        };
        vm.searchUserIndex = 1;
        vm.searchUserSize = 10;
        vm.pageChange = function() {
            service.checkViewDocument(vm.letterId, vm.searchObject, vm.searchUserIndex, vm.searchUserSize).then(function(data) {
                vm.users = data.content;
                vm.totalItemsUser = data.totalElements;
            });
        }
        //export
		$scope.myBlobObject=null;
        $scope.getExportExcelLetterIn=function(){
            console.log('download started, you can show a wating animation');
            service.getExportExcelLetterIn(vm.searchObject)
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