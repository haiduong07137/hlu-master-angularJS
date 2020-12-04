/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
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

    function LetterManagementController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service, Upload, $state, FileSaver, $sce, letterIndocumentFactory) {
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
        vm.selectedletterIns = [];
        vm.textSearch ="";
        vm.documentTypeIds =[];
        vm.letterDocFieldIds=[];
        vm.documentTypeId=0;
        vm.letterDocFieldId=0;
        vm.searchObject ={};
        /*
         * 
         */
        
        vm.searchByText = function() {
        	getPageLetterInByText(-1, -1, vm.pageSize, vm.pageIndex);
		}
        
        vm.enterSearchCode = function () {
			console.log(event.keyCode);
			vm.pageIndexUser = 0;
			if (event.keyCode == 13) {//Phím Enter
				vm.searchByText();
			}
		};
        
        $scope.ListRoleOfUser = [];
        
        vm.getUserRoles = function() {
        	service.getUserRoles().then(function (data) {
        		$scope.ListRoleOfUser = data.content;
            });
		}
        
        vm.getAllLetterDocFieldIds = function(){
        	service.getAllLetterDocFieldIds().then(function (data) {
        		vm.letterDocFieldIds = data.content;
            });
        }
        
        $scope.getLetterInDocumentById = function(id){
        	service.getLetterInDocumentById(id).then(function (data) {
        		console.log(data);
        		$scope.filePath = data.attachments[0].file.filePath;
        		console.log($scope.filePath);
        	});
        }
        
        vm.getAllDocumentTypeIds = function(){
        	service.getAllDocumentTypeIds().then(function (data) {
        		vm.documentTypeIds = data.content;
            });
        }
        
//        function getPageLetterInByText(stepIndex, currentParticipateStates, text, pageIndex, pageSize) {
//            service.getPageLetterInByText(stepIndex, currentParticipateStates, text, pageIndex, pageSize).then(function (data) {
//                vm.letterIns = data.content;
//                console.log(vm.letterIns);
//                vm.bsTableControl.options.data = vm.letterIns;
//                vm.bsTableControl.options.totalRows = data.totalElements;
//            });
//        }
        
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
        getPageLetterInByText(-1, -1, vm.pageIndex, vm.pageSize)
        
        vm.returnBackComment = null;
        vm.modalInstanceListFile = {}
        vm.letterIdTemp = null
        $scope.returnBack = function(letterId){
        	vm.letterIdTemp = letterId;
        	vm.returnBackComment = null;
        	vm.modalInstanceListFile = modal.open({
                animation: true,
                templateUrl: 'viewPopupInDocument.html',
                scope: $scope,
                backdrop: 'static',
                size: 'lg'
            });
        };
        
        vm.saveDataAndClose = function(){
        	if(vm.returnBackComment != null && vm.letterIdTemp != null){
        		let dto = {
            			"comment":vm.returnBackComment
    				}
            	service.saveComment(dto,vm.letterIdTemp).then(function() {
            		$state.go('application.new_letter_in');
            		getPageLetterInByText(0, -1, vm.pageIndex, vm.pageSize);
            	});
            	vm.modalInstanceListFile.close();
        	}
        }
        
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
                        var index = utils.indexOf(row, vm.selectedletterIns);
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
                        getPageLetterInByText(-1, -1, vm.searchObject, vm.pageIndex, vm.pageSize);
                    }
                }
            };
        
            $scope.forwardLetter = function(letterId){
                $state.go('application.forward_letter_in',{letter_id:letterId,state:'forward'});
            }
            
            $scope.editDocument = function(letterId){
                $state.go('application.edit_letter_in',{letter_id:letterId});
            }
    
            $scope.forwardLetterStepTwo = function(letterId){
                $state.go('application.transfer_process_letter_in',{letter_id:letterId})
            }
    
            $scope.viewDocument = function(letterId){
                letterIndocumentFactory.letterFunc(letterId,'view');
            }

            $scope.downloadListDocument = function (letterId) {
            	var attachments =[];
            	if (letterId != null && vm.letterIns != null && vm.letterIns.length > 0) {
					for (var i = 0; i < vm.letterIns.length; i++) {
						if (vm.letterIns[i].id == letterId) {
							attachments = vm.letterIns[i].attachments;
							console.log(attachments);
						}
					}
				}
            	if(attachments!=null && attachments.length>0){
            		//downloadDocument(attachments[0])
                    var  attachment= attachments[1];
                    service.getFileById(attachment.file.id).success(function (data) {
                    	 var file = new Blob([data], {type: attachment.file.contentType});
                    	 if(attachment.file.contentType=='application/pdf'){
                        	 var fileURL = URL.createObjectURL(file);
                             window.open(fileURL);
                          }else {
                        	  FileSaver.saveAs(file, attachment.file.name);
                          }
                    });;
            	}
            	//letterIndocumentFactory.letterFunc(letterId,'view');
//                if (attachment != null) {
//                    var  attachment= attachment;
//                    
//                    service.getFileById(attachment.file.id).success(function (data) {
//                    	 var file = new Blob([data], {type: attachment.file.contentType});
//                    	 FileSaver.saveAs(file, attachment.file.name);
//                    });;
//                }
            }
            
            $scope.assignLetter = function(letterId){
                $state.go('application.assigner_letter_in',{letter_id:letterId});
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
           
            $scope.viewFileInBrowser = function (attachment) {
            	vm.fileView = [];
            	vm.linkFile ="";
            	if (attachment != null) {
            		vm.fileView = attachment;
                   service.getFileById(attachment.file.id).success(function (data) {
               	   var file = new Blob([data], {type: attachment.file.contentType});
               	   var fileURL = URL.createObjectURL(file);
               	   $scope.content = $sce.trustAsResourceUrl(fileURL);
               	   window.open(fileURL);
                   });
				}
            };
            
//            $scope.viewDocument = function(letterId){
//            	letterIndocumentFactory.letterFunc(letterId,'view');
//            	console.log(letterId);
//            }
            $scope.viewDocumentPopup = function(id) {
            	vm.ListAttachment = [];
				for (var i = 0; i < vm.letterIns.length; i++) {
					if (vm.letterIns[i].id == id) {
						vm.ListAttachment = vm.letterIns[i].attachments;
					}
				}
                service.getLetterInDocumentById(id).then(function (data) {
                    vm.letterIn = data;
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
            
            
            vm.downloadDocument = function (attachment) {
                if (attachment != null) {
                    var  attachment= attachment;
                    
                    service.getFileById(attachment.file.id).success(function (data) {
                    	 var file = new Blob([data], {type: attachment.file.contentType});
                    	 FileSaver.saveAs(file, attachment.file.name);
                    });;
                }
            }
            vm.hasClerkRole = true;
            vm.checkClerkRole = function(){
                service.checkUserHasTaskRoleByUserNameAndRoleCode($scope.currentUser.username, "ClerkRole").then(function(data){
                    vm.hasClerkRole = data;
                }).catch(function (err){
                    vm.hasClerkRole = true;
                });
            }
            vm.checkClerkRole();
            
    }
})();