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
		
        vm.getAllDocumentTypeIds = function(){
        	service.getAllDocumentTypeIds().then(function (data) {
        		vm.documentTypeIds = data.content;
            });
        }
        vm.getAllDocumentTypeIds();
        
        vm.getAllLetterDocFieldIds = function(){
        	service.getAllLetterDocFieldIds().then(function (data) {
        		vm.letterDocFieldIds = data.content;
            });
        }
        vm.getAllLetterDocFieldIds();
        vm.searchByText = function() {
        	getPageLetterInByText(-1, -1, vm.textSearch, vm.pageIndex, vm.pageSize);
		}
        
        $scope.ListRoleOfUser = [];
        
        vm.getUserRoles = function() {
        	service.getUserRoles().then(function (data) {
        		$scope.ListRoleOfUser = data.content;
            });
		}
        
        function getPageLetterInByText(stepIndex, currentParticipateStates, pageSize, pageIndex) {
            service.searchPageLetter(stepIndex, currentParticipateStates, vm.searchObject, vm.pageSize, vm.pageIndex).then(function (data) {
                vm.letterIns = data.content;
                console.log(vm.letterIns);
                vm.bsTableControl.options.data = vm.letterIns;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        }
        
        getPageLetterInByText(-1, -1, vm.textSearch, vm.pageIndex, vm.pageSize);
        
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
                        getPageLetterInByText(-1, -1, vm.textSearch, vm.pageIndex, vm.pageSize);
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