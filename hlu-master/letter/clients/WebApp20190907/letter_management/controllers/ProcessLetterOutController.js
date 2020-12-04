/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.LetterManagement').controller('ProcessLetterOutController', ProcessLetterOutController);

    ProcessLetterOutController.$inject = [
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

    function ProcessLetterOutController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service, Upload,$state,FileSaver,letterIndocumentFactory,$sce) {
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
        vm.pageSize = 25;
        vm.pageIndex = 1;
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
        
        function getPageLetterInByText(stepIndex, currentParticipateStates, pageSize, pageIndex) {
            service.searchPageLetterOut(stepIndex, currentParticipateStates, vm.searchObject, vm.pageSize, vm.pageIndex).then(function (data) {
                vm.letterOuts = data.content;
                console.log(vm.letterOuts);
                vm.bsTableControl.options.data = vm.letterOuts;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        }
        getPageLetterInByText(1, -1, vm.pageIndex, vm.pageSize);
        vm.getAllDocumentTypeIds();
        vm.getAllLetterDocFieldIds();
        
        vm.searchByText = function() {
        	getPageLetterInByText(1, -1, vm.pageIndex, vm.pageSize);
		}
        
        vm.enterSearchCode = function () {
			console.log(event.keyCode);
			vm.pageIndexUser = 0;
			if (event.keyCode == 13) {//Phím Enter
				vm.searchByText();
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
                service.addView($scope.currentUser.id, vm.letterOut.id, function success() {
            		
                }, function failure() {
                	toastr.error('Có lỗi xảy ra.', 'Thông báo');
                });
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
                        getPageLetterInByText(1, -1, vm.searchObject, vm.pageIndex, vm.pageSize);
                    }
                }
            };

            $scope.forwardLetter = function(letterId){
                $state.go('application.forward_letter_out',{letter_id:letterId,state:'forward'});
            }
            $scope.editDocument = function(letterId){
                $state.go('application.edit_letter_out',{letter_id:letterId,state:'edit'});
            }
            $scope.transferLeader = function(letterId){
                $state.go('application.transfer_leader_letter_out',{letter_id:letterId,state:'transfer'})
            }
            $scope.viewDocument = function(id){
                $state.go('application.letter_out_view',{letter_id: id, state: 'view'});
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
            vm.downloadDocument = function (attachment) {
                if (attachment != null) {
                    var  attachment= attachment;
                    
                    service.getFileById(attachment.file.id).success(function (data) {
                    	 var file = new Blob([data], {type: attachment.file.contentType});
                    	 FileSaver.saveAs(file, attachment.file.name);
                    });;
                }
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
            
            $scope.checkViewDocument = function (id){
            	vm.letterId = id;
                vm.searchObject.fullname = null;
            	service.checkViewDocument(vm.letterId, vm.searchObject, vm.searchUserIndex, vm.searchUserSize).then(function (data) {
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
            
            vm.searchUser = function(){
            	service.checkViewDocument(vm.letterId, vm.searchObject, vm.searchUserIndex, vm.searchUserSize).then(function (data) {
            		vm.users = data.content;
            		vm.totalItemsUser = data.totalElements;
                });
            }
            
            vm.enterSearchUser = function () {
    			console.log(event.keyCode);
    			vm.pageIndexUser = 0;
    			if (event.keyCode == 13) {//Phím Enter
    				service.checkViewDocument(vm.letterId, vm.searchObject, vm.searchUserIndex, vm.searchUserSize).then(function (data) {
    	        		vm.users = data.content;
    	        		vm.totalItemsUser = data.totalElements;
    	            });
    			}
    		};
    		vm.searchUserIndex = 1;
            vm.searchUserSize = 10;
            vm.pageChange = function() {
            	service.checkViewDocument(vm.letterId, vm.searchObject, vm.searchUserIndex, vm.searchUserSize).then(function (data) {
            		vm.users = data.content;
            		vm.totalItemsUser = data.totalElements;
                });
    		}
    }
})();