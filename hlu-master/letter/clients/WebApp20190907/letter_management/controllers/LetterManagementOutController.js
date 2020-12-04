(function () {
    'use strict';

    angular.module('Hrm.LetterManagement').controller('LetterManagementOutController', LetterManagementOutController);

    LetterManagementOutController.$inject = [
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

    function LetterManagementOutController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service, Upload, $state, FileSaver, letterIndocumentFactory, $sce) {
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
        vm.isShowLetterIn = true;
        vm.isShowLetterOut = true;
        vm.letterOuts = [];
        vm.pageSize = 25;
        vm.pageIndex = 1;
        /*
         * 
         */
        /*
         * Thêm vào để search
         */
        //vm.letterOuts=[];
        vm.selectedletterOuts = [];
        vm.textSearch ="";
        vm.documentTypeIds =[];
        vm.letterDocFieldIds=[];
        vm.documentTypeId=0;
        vm.letterDocFieldId=0;
        vm.searchObject ={};
        /*
         * 
         */
       /*
        * Thêm vào hàm search
        * 
        */
        
        vm.searchByText = function() {
        	getPageLetterOutByText(-1, -1, vm.pageSize, vm.pageIndex);
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
        
        vm.getAllDocumentTypeIds = function(){
        	service.getAllDocumentTypeIds().then(function (data) {
        		vm.documentTypeIds = data.content;
            });
        }
        
//        function getPageLetterInByText(stepIndex, currentParticipateStates, text, pageIndex, pageSize) {
//            service.getPageLetterInByText(stepIndex, currentParticipateStates, text, pageIndex, pageSize).then(function (data) {
//                vm.letterOuts = data.content;
//                console.log(vm.letterOuts);
//                vm.bsTableControl.options.data = vm.letterOuts;
//                vm.bsTableControl.options.totalRows = data.totalElements;
//            });
//        }
        
        function getPageLetterOutByText(stepIndex, currentParticipateStates, pageSize, pageIndex) {
            service.searchPageLetterOut(stepIndex, currentParticipateStates, vm.searchObject, vm.pageSize, vm.pageIndex).then(function (data) {
                vm.letterOuts = data.content;
                vm.bsTableControl.options.data = vm.letterOuts;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        }
        vm.getAllDocumentTypeIds();
        vm.getAllLetterDocFieldIds();
        getPageLetterOutByText(-1, -1, vm.pageSize, vm.pageIndex);
        
//        vm.bsTableControl = {
//                options: {
//                    data: vm.letterOutss,
//                    idField: 'id',
//                    sortable: true,
//                    striped: true,
//                    maintainSelected: true,
//                    clickToSelect: false,
//                    showColumns: false,
//                    showToggle: false,
//                    pagination: true,
//                    pageSize: vm.pageSize,
//                    pageList: [5, 10, 25, 50, 100],
//                    locale: settings.locale,
//                    sidePagination: 'server',
//                    //columns: service.getTableDefinition(),
//                    columns: service.getTableLetterOutDocumentDefinition(),
//                    onCheck: function (row, $element) {
//                        $scope.$apply(function () {
//                            vm.selectedletterOuts.push(row);
//                        });
//                    },
//                    onCheckAll: function (rows) {
//                        $scope.$apply(function () {
//                            vm.selectedletterOuts = rows;
//                        });
//                    },
//                    onUncheck: function (row, $element) {
//                        var index = utils.indexOf(row, vm.selectedletterOuts);
//                        if (index >= 0) {
//                            $scope.$apply(function () {
//                                vm.selectedletterOuts.splice(index, 1);
//                            });
//                        }
//                    },
//                    onUncheckAll: function (rows) {
//                        $scope.$apply(function () {
//                            vm.selectedletterOuts = [];
//                        });
//                    },
//                    onPageChange: function (index, pageSize) {
//                        vm.pageSize = pageSize;
//                        vm.pageIndex = index;
//                        getPageLetterOutByText(-1, -1, vm.searchObject, vm.pageIndex, vm.pageSize);
//                    }
//                }
//            };

            $scope.forwardLetter = function(letterId){
                $state.go('application.forward_letter_out',{letter_id:letterId,state:'forward'});
            }
            
            $scope.editDocument = function(id){
                $state.go('application.letter_out_edit',{letter_id: id});
            }
    
            $scope.forwardLetterStepTwo = function(letterId){
                $state.go('application.transfer_process_letter_out',{letter_id:letterId})
            }
    
            $scope.viewDocument = function(letterId){
                letterIndocumentFactory.letterFunc(letterId,'view');
            }
            
            $scope.assignLetter = function(letterId){
                $state.go('application.assigner_letter_out',{letter_id:letterId});
            }

            $scope.viewAllFileInDocument = function (letterId) {
            	vm.ListAttachment = [];
            	if (letterId != null && vm.letterOuts != null && vm.letterOuts.length > 0) {
    				for (var i = 0; i < vm.letterOuts.length; i++) {
    					if (vm.letterOuts[i].id == letterId) {
    						vm.ListAttachment = vm.letterOuts[i].attachments;

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
            
            $scope.viewDocumentOutPopup = function(id) {
            	for (var i = 0; i < vm.letterOuts.length; i++) {
    				if (vm.letterOuts[i].id == id) {
    					vm.ListAttachment = vm.letterOuts[i].attachments;
    				}
    			}
                service.getLetterOutDocumentById(id).then(function (data) {
                    vm.letterOut = data;
                	console.log(data);
                	if(vm.letterOut == ""){
                		toastr.warning("Bạn không có quyền xem văn bản!")
                	}else{
                		if(vm.letterOut.task.currentStep != null){
                			if(vm.letterOut.task.currentStep.id == 13){
                            	vm.value1 = false;
                            	vm.value2 = true;
                            }else{
                            	vm.value1 = true;
                            	vm.value2 = false;
                            }
                		}
                        if(vm.letterOut.issueOrgan == null){
                        	vm.issueOrgan = vm.letterOut.otherIssueOrgan;
                        }else{
                        	vm.issueOrgan = vm.letterOut.issueOrgan.name;
                        }
                        vm.modalInstanceListFile = modal.open({
        	                    animation: true,
        	                    templateUrl: 'viewDocumentPopup.html',
        	                    scope: $scope,
        	                    backdrop: 'static',
        	                    size: 'lg'
        	            });
                	}
                    
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
                        url: vm.baseUrl + 'letter/outdocument/importdocument',
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
            
            vm.importDepartment = function () {
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
            
            vm.downloadDocument = function (attachment) {
                if (attachment != null) {
                    var  attachment= attachment;
                    
                    service.getFileById(attachment.file.id).success(function (data) {
                    	 var file = new Blob([data], {type: attachment.file.contentType});
                    	 FileSaver.saveAs(file, attachment.file.name);
                    });;
                }
            }
            
        /*
         * 
         */
        
//        function getAllLetterOutDocumentBy(stepIndex, currentParticipateStates, pageIndex, pageSize) {
//            service.getAllLetterOutDocumentBy(stepIndex, currentParticipateStates, pageIndex, pageSize).then(function (data) {
//                vm.letterOuts = data.content;
//                console.log(data.content);
//                vm.bsTableControl.options.data = vm.letterOuts;
//                vm.bsTableControl.options.totalRows = data.totalElements;
//            });
//        }
//        getAllLetterOutDocumentBy(1, -1, vm.pageIndex, vm.pageSize);
        vm.bsTableControl = {
            options: {
                data: vm.letterOutss,
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
                columns: service.getTableDefinitionFinishLetterOut(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedletterOuts.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedletterOuts = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedletterOuts.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedletterOuts = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    getPageLetterOutByText(-1, -1, vm.pageSize, vm.pageIndex);
                }
            }
        };

        $scope.viewDocument = function (id) {
            $state.go('application.letter_out_view', { letter_id: id, state: 'view' });
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