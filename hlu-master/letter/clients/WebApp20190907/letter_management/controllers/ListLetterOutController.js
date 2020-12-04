/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
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
        'FileSaver',
        '$sce'
    ];

    function ListLetterOutController($rootScope, $scope, toastr, $timeout, settings, utils,
        modal, service, Uploader, $state, $window, FileSaver, $sce) {
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
        vm.letterOuts=[];
        vm.pageSize = 25;
        vm.pageIndex = 1;
        vm.selectedletterOuts = [];
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
        getPageLetterInByText(2, -1, vm.pageIndex, vm.pageSize);
        vm.getAllDocumentTypeIds();
        vm.getAllLetterDocFieldIds();
        
        vm.searchByText = function() {
        	getPageLetterInByText(2, -1, vm.pageIndex, vm.pageSize);
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
            	console.log(data);
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
                    data: vm.letterOuts,
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
                            vm.selectedletterOuts.push(row);
                        });
                    },
                    onCheckAll: function (rows) {
                        $scope.$apply(function () {
                            vm.selectedletterOuts = rows;
                        });
                    },
                    onUncheck: function (row, $element) {
                        var index = utils.indexOf(row, vm.selectedletterOuts);
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
                        getPageLetterInByText(2, -1, vm.pageIndex, vm.pageSize);
                    }
                }
            };
            $scope.viewDocument = function(id){
                $state.go('application.letter_out_view',{letter_id: id, state: 'view'});
            }
            $scope.editDocument = function(id){
                $state.go('application.letter_out_edit',{letter_id: id});
            }
            $scope.checkLetter = function(letterId){
                $state.go('application.check_letter_out',{letter_id:letterId,state:'check'})
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
            vm.hasClerkRole = true;
            vm.checkClerkRole = function(){
                service.checkUserHasTaskRoleByUserNameAndRoleCode($scope.currentUser.username, "ClerkRole").then(function(data){
                    vm.hasClerkRole = data;
                }).catch(function (err){
                    vm.hasClerkRole = true;
                });
            }
            vm.checkClerkRole();
            
            vm.returnBackComment = null;
            vm.modalInstanceListFile = {}
            vm.letterIdTemp = null
            $scope.returnBack = function(letterId){
            	vm.letterIdTemp = letterId;
            	vm.returnBackComment = null;
            	vm.modalInstanceListFile = modal.open({
                    animation: true,
                    templateUrl: 'viewPopupOutDocument.html',
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
                	service.returnBack(dto, vm.letterIdTemp).then(function () {
                		vm.modalInstanceListFile.close();
                		getPageLetterInByText(2, -1, vm.pageIndex, vm.pageSize);
                	});
            	}
            }
            
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
