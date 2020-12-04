/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.LetterManagement').controller('FinishLetterInController', FinishLetterInController);

    FinishLetterInController.$inject = [
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
        'letterIndocumentFactory',
        '$sce'
    ];

    function FinishLetterInController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service, Upload,$state,FileSaver,letterIndocumentFactory,$sce) {
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
        vm.isShowLetterIn=true;
        vm.isShowLetterOut=true;
        vm.letterIns=[];
        vm.pageSize = 25;
        vm.pageIndex = 1;
        vm.textSearch ="";
        vm.documentTypeIds =[];
        vm.letterDocFieldIds=[];
        vm.documentTypeId=0;
        vm.letterDocFieldId=0;
        vm.searchObject ={};
        /*
         * 
         */
        vm.getAllDocumentTypeIds = function(){
        	service.getAllDocumentTypeIds().then(function (data) {
        		vm.documentTypeIds = data.content;
            });
        }
        vm.getAllLetterDocFieldIds = function(){
        	service.getAllLetterDocFieldIds().then(function (data) {
        		vm.letterDocFieldIds = data.content;
            });
        }
        
        service.getOrganizationTree().then(function (data) {
            vm.organizations = data;
        });
        
        function getPageLetterInByText(stepIndex, currentParticipateStates, pageSize, pageIndex) {
            service.searchPageLetter(stepIndex, currentParticipateStates, vm.searchObject, vm.pageSize, vm.pageIndex).then(function (data) {
                vm.letterIns = data.content;
                console.log(vm.letterIns);
                vm.bsTableControl.options.data = vm.letterIns;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        }
        vm.getAllDocumentTypeIds();
        vm.getAllLetterDocFieldIds();
        getPageLetterInByText(4, -1, vm.pageIndex, vm.pageSize);
        
        vm.clear = function(){
        	delete vm.searchObject.issueOrgan;
        	delete vm.searchObject.otherIssueOrgan;
        }
        
        vm.searchByText = function() {
        	if(vm.searchObject.dateFrom != null && vm.dateTo == null){
        		if((vm.searchObject.dateFrom).getTime() > new Date().getTime()){
        			toastr.warning("Khoảng thời gian tìm kiếm không thỏa mãn!");
        		}else{
        			getPageLetterInByText(4, -1, vm.pageIndex, vm.pageSize);
        		}
        	}
        	if(vm.searchObject.dateFrom == null && vm.dateTo != null){
        		if(vm.dateTo > new Date()){
        			toastr.warning("Khoảng thời gian tìm kiếm không thỏa mãn!");
        		}else{
        			vm.searchObject.dateTo = new Date((vm.dateTo).getTime() + 86399000);
        			getPageLetterInByText(4, -1, vm.pageIndex, vm.pageSize);
        		}
        	}
        	if(vm.searchObject.dateFrom != null && vm.dateTo != null){
        		if((vm.searchObject.dateFrom).getTime() > new Date().getTime() || vm.dateTo > new Date() || (vm.searchObject.dateFrom).getTime() > (vm.dateTo).getTime()){
        			toastr.warning("Khoảng thời gian tìm kiếm không thỏa mãn!");
        		}else{
        			vm.searchObject.dateTo = new Date((vm.dateTo).getTime() + 86399000);
        			getPageLetterInByText(4, -1, vm.pageIndex, vm.pageSize);
        		}
        	}
        	if(vm.searchObject.dateFrom == null && vm.dateTo == null){
        		getPageLetterInByText(4, -1, vm.pageIndex, vm.pageSize);
        	}
		}
        
        vm.enterSearchCode = function () {
			console.log(event.keyCode);
			vm.pageIndexUser = 0;
			if (event.keyCode == 13) {//Phím Enter
				vm.searchByText();
			}
		};
        
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
                    columns: service.getTableDefinitionReturn(),
                    onCheck: function (row, $element) {
                        $scope.$apply(function () {
                            vm.selectedletterIns.push(row);
                        });
                    },
                    onCheckAll: function (rows) {
                        $scope.$apply(function () {
                            vm.selectedletterIns = rows;
                        });
                    },
                    onUncheck: function (row, $element) {
                        var index = utils.indexOf(row, vm.selectedpositiontitles);
                        if (index >= 0) {
                            $scope.$apply(function () {
                                vm.selectedletterIns.splice(index, 1);
                            });
                        }
                    },
                    onUncheckAll: function (rows) {
                        $scope.$apply(function () {
                            vm.selectedletterIns = [];
                        });
                    },
                    onPageChange: function (index, pageSize) {
                        vm.pageSize = pageSize;
                        vm.pageIndex = index;
                        getPageLetterInByText(4, -1, vm.searchObject, vm.pageIndex, vm.pageSize);
                    }
                }
            };

            $scope.viewDocument = function(letterId){
                letterIndocumentFactory.letterFunc(letterId,'view');
            }
            $scope.viewDocumentPopup = function(id) {
            	vm.ListAttachment = [];
				for (var i = 0; i < vm.letterIns.length; i++) {
					if (vm.letterIns[i].id == id) {
						vm.ListAttachment = vm.letterIns[i].attachments;
					}
				}
                service.getLetterInDocumentById(id).then(function (data) {
                    vm.letterIn = data;
                    service.addView($scope.currentUser.id, vm.letterIn.id, function success() {
                		
                    }, function failure() {
                    	toastr.error('Có lỗi xảy ra.', 'Thông báo');
                    });
                    if(vm.letterIn.task.currentStep != null){
                    	if(vm.letterIn.task.currentStep.id == 5){
                        	vm.value1 = false;
                        	vm.value2 = true;
                        }else{
                        	vm.value1 = true;
                        	vm.value2 = false;
                        }
                    }
                    vm.fowardRole = "";
                    vm.assignerRole = [];
                    vm.chairmanRole = "";
                    vm.processRole = [];
                    for(var i = 0 ; i < vm.letterIn.task.participates.length ; i++){
                    	if(vm.letterIn.task.participates[i].role.code == "FowardRole"){
                    		vm.fowardRole = vm.letterIn.task.participates[i].displayName;
                    	}
                    	if(vm.letterIn.task.participates[i].role.code == "AssignerRole"){
                    		vm.assignerRole.push(vm.letterIn.task.participates[i].taskOwner.displayName);
                    	}
                    	if(vm.letterIn.task.participates[i].role.code == "ChairmanRole"){
                    		vm.chairmanRole = vm.letterIn.task.participates[i].taskOwner.displayName;
                    	}
                    	if(vm.letterIn.task.participates[i].role.code == "ProcessRole"){
                    		vm.processRole.push(vm.letterIn.task.participates[i].taskOwner.displayName);
                    	}
                    }
                    if(vm.letterIn.issueOrgan == null){
                    	vm.issueOrgan = vm.letterIn.otherIssueOrgan;
                    }else{
                    	vm.issueOrgan = vm.letterIn.issueOrgan.name;
                    }
                    vm.modalInstanceListFile = modal.open({
  	                    animation: true,
  	                    templateUrl: 'viewDocumentPopup.html',
  	                    scope: $scope,
  	                    backdrop: 'static',
  	                    size: 'lg'
  	                });
                });
            }
            $scope.viewAllFileInDocument = function (letterId) {
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
            vm.downloadDocument = function (attachment) {
                if (attachment != null) {
                    var  attachment= attachment;
                    
                    service.getFileById(attachment.file.id).success(function (data) {
                    	 var file = new Blob([data], {type: attachment.file.contentType});
                    	 FileSaver.saveAs(file, attachment.file.name);
                    });;
                }
            }
            
            $scope.viewFileInBrowser = function (attachment) {
            	vm.fileView = [];
            	vm.linkFile ="";
            	if (attachment != null) {
            		vm.fileView = attachment;
                   service.getFileById(attachment.file.id).success(function (data) {
               	   var file = new Blob([data], {type: attachment.file.contentType});
               	   var fileURL = URL.createObjectURL(file);
               	   window.open(fileURL);
                    });
    			}
            };
            vm.hasClerkRole = true;
            vm.checkClerkRole = function(){
                service.checkUserHasTaskRoleByUserNameAndRoleCode($scope.currentUser.username, "ClerkRole").then(function(data){
                    vm.hasClerkRole = data;
                }).catch(function (err){
                    vm.hasClerkRole = true;
                });
            }
            vm.checkClerkRole();
            
            $scope.MAX_FILE_SIZE = '10MB';
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
                        url: vm.baseUrl + 'letter/indocument/importdocument',
                        data: {uploadfile: file}
                    });

                    file.upload.then(function (response) {
                        console.log(response);
                        file.result = response.data;
                        toastr.info('Import thành công.', 'Thông báo');
                    }, function errorCallback(response) {
                        toastr.error('Import lỗi.', 'Lỗi');
                    });
                }
            };
            
            vm.importDocument = function () {
                vm.modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'import_modal.html',
                    scope: $scope,
                    backdrop: 'static',
                    size: 'md'
                });

                $scope.f = null;
                $scope.errFile = null;

                vm.modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {
                        vm.startUploadFile($scope.f);
                        console.log($scope.f);
                    }
                }, function () {
                	
                });
            }
            
            $scope.checkViewLetter = function (id){
            	vm.letterId = id;
                vm.searchObject.fullname = null;
            	service.checkViewLetter(vm.letterId, vm.searchObject, vm.searchUserIndex, vm.searchUserSize).then(function (data) {
            		vm.users = data.content;
            		vm.totalItemsUser = data.totalElements;
            		vm.modalInstanceListUser = modal.open({
                        animation: true,
                        templateUrl: 'checkViewLetter.html',
                        scope: $scope,
                        backdrop: 'static',
                        size: 'md'
            		});
                });
            }
            
            vm.searchUser = function(){
            	service.checkViewLetter(vm.letterId, vm.searchObject, vm.searchUserIndex, vm.searchUserSize).then(function (data) {
            		vm.users = data.content;
            		vm.totalItemsUser = data.totalElements;
                });
            }
            
            vm.enterSearchUser = function () {
    			console.log(event.keyCode);
    			vm.pageIndexUser = 0;
    			if (event.keyCode == 13) {//Phím Enter
    				service.checkViewLetter(vm.letterId, vm.searchObject, vm.searchUserIndex, vm.searchUserSize).then(function (data) {
    	        		vm.users = data.content;
    	        		vm.totalItemsUser = data.totalElements;
    	            });
    			}
    		};
    		vm.searchUserIndex = 1;
            vm.searchUserSize = 10;
            vm.pageChange = function() {
            	service.checkViewLetter(vm.letterId, vm.searchObject, vm.searchUserIndex, vm.searchUserSize).then(function (data) {
            		vm.users = data.content;
            		vm.totalItemsUser = data.totalElements;
                });
    		}
            
    }
})();