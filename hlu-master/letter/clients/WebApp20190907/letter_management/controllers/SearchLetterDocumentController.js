(function () {
    'use strict';

    angular.module('Hrm.LetterManagement').controller('SearchLetterDocumentController', SearchLetterDocumentController);

    SearchLetterDocumentController.$inject = [
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

    function SearchLetterDocumentController($rootScope, $scope, toastr, $timeout, settings, utils, modal, service, Upload, $state, FileSaver, letterIndocumentFactory, $sce) {
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
        vm.letter = [];
        vm.pageSize = 25;
        vm.pageIndex = 1;
        vm.searchObject ={};
        vm.isShowLetterIn=true;
        vm.isShowLetterOut=true;
        vm.letterIns=[];
        vm.documentTypeIds =[];
        vm.letterDocFieldIds=[];
        vm.documentTypeId=0;
        vm.letterDocFieldId=0;
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
        vm.getAllDocumentTypeIds();
        vm.getAllLetterDocFieldIds();
        vm.searchByText = function() {
        	getPageLetterByText(vm.pageIndex, vm.pageSize);
		}
        
        vm.enterSearchCode = function () {
			console.log(event.keyCode);
			vm.pageIndexUser = 0;
			if (event.keyCode == 13) {//Phím Enter
				vm.searchByText();
			}
		};
        
		function getPageLetterByText(pageIndex, pageSize) {
            service.searchPageAllLetter(vm.searchObject, vm.pageIndex, vm.pageSize).then(function (data) {
                vm.letter = data.content;
                vm.bsTableControl.options.data = vm.letter;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        }
		
        function getAllLetterDocument(pageSize, pageIndex) {
            service.getAllLetterDocument(vm.pageSize, vm.pageIndex).then(function (data) {
                vm.letter = data.content;
                console.log(vm.letter);
                vm.bsTableControl.options.data = vm.letter;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        }
        getAllLetterDocument(vm.pageSize, vm.pageIndex);
        
        $scope.viewDocumentIn = function(letterId){
            letterIndocumentFactory.letterFunc(letterId,'view');
        }

        $scope.viewDocumentOut = function (id) {
        	service.getLetterOutDocumentById(id).then(function (data) {
                vm.letterOut = data;
            	console.log(data);
            	if(vm.letterOut == ""){
            		toastr.warning("Bạn không có quyền xem văn bản!")
            	}else{
            		$state.go('application.letter_out_view', { letter_id: id, state: 'view' });
            	}
            });
        }
        
        $scope.viewAllFileInDocument = function (letterId) {
        	vm.ListAttachment = [];
        	if (letterId != null && vm.letter != null && vm.letter.length > 0) {
				for (var i = 0; i < vm.letter.length; i++) {
					if (vm.letter[i].id == letterId) {
						vm.ListAttachment = vm.letter[i].attachments;

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
        
        $scope.viewDocumentInPopup = function(id) {
        	vm.ListAttachment = [];
			for (var i = 0; i < vm.letter.length; i++) {
				if (vm.letter[i].id == id) {
					vm.ListAttachment = vm.letter[i].attachments;
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
	                    templateUrl: 'viewDocumentInPopup.html',
	                    scope: $scope,
	                    backdrop: 'static',
	                    size: 'lg'
	                });
            });
        }
        
        vm.clear = function(){
        	vm.searchObject.issueOrgan = null;
        	vm.searchObject.otherIssueOrgan = null;
        }
        service.getOrganizationTree().then(function (data) {
            vm.organizations = data;
            console.log(data);
        });
        
        $scope.viewDocumentOutPopup = function(id) {
        	for (var i = 0; i < vm.letter.length; i++) {
				if (vm.letter[i].id == id) {
					vm.ListAttachment = vm.letter[i].attachments;
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
    	                    templateUrl: 'viewDocumentOutPopup.html',
    	                    scope: $scope,
    	                    backdrop: 'static',
    	                    size: 'lg'
    	            });
            	}
                
            });
        }
        
        vm.bsTableControl = {
            options: {
                data: vm.letter,
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
                columns: service.getTableAllLetterDocument(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedletter.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedletter = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedletter.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedletter = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    if(vm.searchObject == null){
                    	getAllLetterDocument(vm.pageSize, vm.pageIndex);
                    }else{
                    	getPageLetterByText(vm.pageIndex, vm.pageSize);
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
        
        vm.getListNewsMenuRight = function () {
            service.getListNews(1, 5).then(function (data) {
                vm.listNewsMenuRight = data.content;
            });
        };
        vm.getListNewsMenuRight();
        vm.linkToDetail = function (newsId) {
            $state.go('application.news_entry', { newsId: newsId });
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