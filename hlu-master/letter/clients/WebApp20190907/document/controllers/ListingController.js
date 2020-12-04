(function () {
    'use strict';

    angular.module('Hrm.Document').controller('ListingController', ListingController);

    ListingController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'Upload',
        'FileSaver',
        'ListingService',
        'DocumentTypeService',
        'DocumentFieldService',
        'DocumentPriorityService',
        'DocumentSecurityLevelService',
        '$stateParams',
        'DepartmentService',
        'DocumentBookService',
        'TaskFlowService',
        'DocumentBookGroupService',
        'TaskOwnerService'
        
    ];

    function ListingController($rootScope, $scope, toastr, 
    		$timeout, 
    		settings, utils, 
    		modal,Uploader,FileSaver, 
    		service,typeService,
    		fieldService,priorityService,
    		securityService
    		,$stateParams,departmentService,documentBookService,taskService,groupBookService,taskOwnerService
    		) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        //Initial page
        var vm = this;

        vm.letterIn = {};
        vm.date = new Date();
        vm.letterIn.deliveredDate = vm.date;
        vm.letterIn.registeredDate = vm.date;
        vm.letterIns = [];
        vm.selectedletterIns = [];

        vm.pageIndex = 1;
        vm.pageSize = 25;
        vm.isSetInBook = true;
        vm.selectedTaskOwner = {displayName:null};        
        vm.getSelectedTaskOwners = getSelectedTaskOwners;
        vm.letterLines = [
            {id:1,name: "Bưu điện"},
            {id:2,name: "Chuyển trực tiếp"},
            {id:3,name: "Fax"},
            {id:4,name: "Email"}
        ];

        vm.saveLetterTypes = [
            {id:1,name: "Bản gốc"},
            {id:2,name: "Bản sao chép"},
            {id:3,name: "Bản mềm"}
        ];

        /* TINYMCE */
        vm.tinymceOptions = {
            height: 130,
            theme: 'modern',
            plugins: [
                'lists fullscreen' //autoresize
            ],
            toolbar1: 'bold underline italic | removeformat | bullist numlist outdent indent | fullscreen',
            content_css: [
                '//fonts.googleapis.com/css?family=Poppins:300,400,500,600,700',
                '/assets/css/tinymce_content.css'
            ],
            autoresize_bottom_margin: 0,
            statusbar: false,
            menubar: false
        };

        vm.documentFields = [];
        fieldService.getDocumentFields(1,10000).then(function (data) {
            vm.documentFields = data.content;
        });

        vm.documentPriorities = [];
        priorityService.getDocumentPriorities(1,10000).then(function (data) {
            vm.documentPriorities = data.content;
        });

        vm.documentTypes = [];
        typeService.getDocumentTypes(1,10000).then(function (data) {
            vm.documentTypes = data.content;
        });

        vm.documentSecurityLevels = [];
        securityService.getDocumentSecurityLevels(1,10000).then(function (data) {
            vm.documentSecurityLevels = data.content;
        });

        vm.taskOwnersForwarders = [];
        service.getListTaskOwnerByRoleCode('FowardRole').then(function (data) {
            vm.taskOwnersForwarders = data;
        });

        vm.taskOwnersAssigners = [];
        service.getListTaskOwnerByRoleCode('AssignerRole').then(function (data) {
            vm.taskOwnersAssigners = data;
        });

        vm.docBooks = [];
        /*service.getListDocBook(1,10000).then(function (data) {
            vm.docBooks = data.content;
            console.log(vm.docBooks);
        });*/

        vm.docBookGroups = [];
        service.getListDocBookGroup(1,10000).then(function (data) {
            vm.docBookGroups = data.content;
        });
        
        vm.letterDocBookGroupSelected = function() {
        	if (vm.letterIn != null) {
        		vm.letterIn.letterDocBook = null;
        		
    			if (vm.letterIn.letterDocBookGroup != null && vm.letterIn.letterDocBookGroup.id != null && vm.letterIn.letterDocBookGroup.id != '') {
    				service.getListDocBookByGroupId(vm.letterIn.letterDocBookGroup.id).then(function (data) {
    		            vm.docBooks = data;
    		        });
    			}
    			else {
    				vm.docBooks = [];
    			}
			}
		}
        //--------------//

        vm.documentsRecieved = [
            {id: 0, name: "Quản lý văn bản đến", active: true},
            {id: 1, name: "Vào sổ văn bản đến", active: false},
            {id: 2, name: "Văn bản mới vào sổ", active: false, stepIndex: 0},
            {id: 9, name: "Phân luồng", active: false, stepIndex: 1},
            {id: 3, name: "Văn bản chờ giao xử lý", active: false, stepIndex: 2},
            {id: 4, name: "Văn bản đang xử lý", active: false, stepIndex: 3},
            //{id: 5, name: "Chưa tham gia xử lý", active: false, stepIndex: 4},
            //{id: 6, name: "Đã tham gia xử lý", active: false, stepIndex: 5},
            {id: 7, name: "Văn bản đã hoàn thành", active: false, stepIndex: 4},
            {id: 8, name: "Toàn bộ văn bản đến", active: false, stepIndex: 7},
        ];

        vm.documentsTranfered = [
            {id: 0, name: "Quản lý văn bản đi", active: false},
            {id: 1, name: "Tạo mới văn bản đi", active: false},
            {id: 2, name: "Văn bản đi mới tạo", active: false},
            {id: 3, name: "Văn bản đi chờ xin ý kiến", active: false},
            {id: 4, name: "Văn bản đang xin ý kiến", active: false},
            //{id: 5, name: "Chưa tham gia xử lý", active: false},
            //{id: 6, name: "Đã tham gia xử lý", active: false},
            {id: 7, name: "Văn bản đi đã phê duyệt", active: false},
            {id: 8, name: "Văn bản đi đã xuất bản", active: false},
            {id: 9, name: "Tìm kiếm văn bản đi", active: false},
            {id: 10, name: "Báo cáo thống kê", active: false}
        ];
        
        vm.documentsConfig = [
            {id: 0, name: "Thiết lập hệ thống", active: false},
            {id: 1, name: "Nhóm sổ văn bản", active: false},
            {id: 2, name: "Sổ văn bản", active: false},
            {id: 3, name: "Quy trình xử lý văn bản", active: false},
            {id: 4, name: "Loại văn bản", active: false},
            {id: 5, name: "Lĩnh vực văn bản", active: false},
            {id: 6, name: "Độ ưu tiên văn bản", active: false},
            {id: 7, name: "Độ mật văn bản", active: false}
        ];

        //GET CURRENT TASK OWNER
        vm.taskOwner = {};
        function getCurrentTaskOwner() {
            service.getCurrentTaskOwner().then(function (data) {
                vm.taskOwner = data;
            });
        }
        getCurrentTaskOwner();
        //------------------------------//



        //LETTER IN//
        vm.stepProcess = 8;
        vm.stepIndex = -1;
        vm.isShowLetterIn = true;
        vm.isShowLetterOut = false;
        vm.isShowConfig = false;
        vm.documentsRecieved[7].active = true;//set toàn bộ danh sách active
		getPageLetterInByIndex(vm.stepIndex,-1,vm.pageIndex,vm.pageSize);
        //// Upload file
        vm.uploadedFile = null;
        vm.errorFile = null;
        vm.uploadFiles = function (file, errFiles) {
            vm.uploadedFile = file;
            if(vm.uploadedFile!=null){
	            Uploader.upload({
	                url: settings.api.baseUrl+ settings.api.apiPrefix + 'letter/indocument/uploadattachment',
	                method: 'POST',
	                data: {uploadfile: vm.uploadedFile}
	            }).then(function (successResponse) {
	            	
	            	var attachment = successResponse.data;
	                if (vm.uploadedFile && (!vm.errorFile || (vm.errorFile && vm.errorFile.$error == null))) {
	                    if (vm.letterIn != null && vm.letterIn.attachments == null) {
	                        vm.letterIn.attachments = [];
	                    }
                        vm.letterIn.attachments.push(
                                //{ title: attachment.file.name, contentLength: attachment.file.contentSize, contentType: fileDesc.contentType }
                        		attachment
                            );
	                    vm.bsTableControl4Files.options.data = vm.letterIn.attachments;
	                }
	            }, function (errorResponse) {
	                toastr.error('Error submitting data...', 'Error');
	            }, function (evt) {
	                console.log('progress: ' + parseInt(100.0 * evt.loaded / evt.total) + '%');
	            });
            }
        };
        
        $scope.deleteDocument = function (index) {
            if (vm.letterIn != null && vm.letterIn.attachments != null) {
                for (var i = 0; i < vm.letterIn.attachments.length; i++) {
                    if (i == index) {
                        vm.letterIn.attachments.splice(i, 1);
                    }
                }
            }
        }

        $scope.downloadDocument = function (index) {
            if (vm.letterIn != null && vm.letterIn.attachments != null) {
                for (var i = 0; i < vm.letterIn.attachments.length; i++) {
                    if (i == index) {
                        var  attachment= vm.letterIn.attachments[i];
                        service.getFileById(attachment.file.id).success(function (data) {
                        	 var file = new Blob([data], {type: attachment.file.contentType});
                        	 FileSaver.saveAs(file, attachment.file.name);
                        });;
                    }
                }
            }
        }

        vm.bsTableControl4Files = {
            options: {
                data: vm.letterIn.attachments,
                idField: 'id',
                sortable: false,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: false,
                singleSelect: true,
                showToggle: false,
                pagination: false,
                locale: settings.locale,
                columns: service.getTableAttachmentFileDefinition()
            }
        };
        vm.bsTableControl4FilesProcess = {
            options: {
                data: vm.letterIn.attachments,
                idField: 'id',
                sortable: false,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: false,
                singleSelect: true,
                showToggle: false,
                pagination: false,
                locale: settings.locale,
                columns: service.getTableAttachmentProcessFileDefinition()
            }
        };
        //--END UPLOAD--//

        function resetStatus() {
            for(var i = 0; i < vm.documentsConfig.length; i++){
                vm.documentsConfig[i].active = false;
            }
            for(var i = 0; i < vm.documentsTranfered.length; i++){
                vm.documentsTranfered[i].active = false;
            }
            for(var i = 0; i < vm.documentsRecieved.length; i++){
                vm.documentsRecieved[i].active = false;
            }
            vm.letterIn = {};
            vm.date = new Date();
            vm.letterIn.deliveredDate = vm.date;
            vm.letterIn.registeredDate = vm.date;
            vm.forwardStepTwo = false;
            vm.isClickAssign = false;
            vm.isView=false;
			vm.taskComment.comment='';
            vm.assignerId=null;
            // vm.isNew = true;
        }

        vm.setStepProcessLetterIn = function (number,index,stepIndex) {
            vm.stepProcess = number;
            vm.isNew = true;
            resetStatus();
            vm.documentsRecieved[index].active = true;
            if(angular.isDefined(stepIndex) && stepIndex != null&& vm.stepProcess!=8){
                vm.stepIndex = stepIndex;
                getPageLetterInByIndex(vm.stepIndex,-1,vm.pageIndex,vm.pageSize);
            }else if(vm.stepProcess==8){
                vm.stepIndex = stepIndex;
                getPageLetterInByIndex(-1,-1,vm.pageIndex,vm.pageSize);
            }

            vm.letterIn.deliveredDate = vm.date;
            vm.letterIn.registeredDate = vm.date;
            vm.isClickForward = false;
            vm.isShowPage = true;            // if(vm.stepProcess==3){
            //     vm.isClickAssign=true;
            // }
        };

        vm.showLetterIn = function (number,index,stepIndex) {
            number=8;
            index=9;
            stepIndex=-1;
            vm.stepProcess = number;
            resetStatus();
            vm.documentsRecieved[index].active = true;
            vm.isShowLetterIn = true;
            vm.isShowLetterOut = false;
            vm.isShowConfig = false;
            if(angular.isDefined(stepIndex) && stepIndex != null){
                vm.stepIndex = stepIndex;
                getPageLetterInByIndex(vm.stepIndex,-1,vm.pageIndex,vm.pageSize);
            }
        };

        vm.hideLetterIn = function (number,index) {
            vm.stepProcess = number;
            resetStatus();
            vm.documentsRecieved[index].active = true;
            vm.isShowLetterIn = false;
        };
        //-------------------------------//

        //LETTER OUT//
        vm.setStepProcessLetterOut = function (number,index) {
            vm.stepProcess = number;
            resetStatus();
            vm.documentsTranfered[index].active = true;
            vm.isShowLetterIn = false;
            vm.isShowLetterOut = true;
            vm.isShowConfig = false;
        };

        vm.showLetterOut = function (number,index) {
            number=number+1;
            index=index+1;
            vm.stepProcess = number;
            resetStatus();
            vm.documentsTranfered[index].active = true;
            vm.isShowLetterOut = true;
            vm.isShowLetterIn = false;
            vm.isShowConfig = false;
        };

        vm.hideLetterOut = function (number,index) {
            vm.stepProcess = number;
            resetStatus();
            vm.documentsTranfered[index].active = true;
            vm.isShowLetterOut = false;

        };
        //---------------------------//

        //Show Config//
        vm.setProcessShowConfig = function (number,index) {
            vm.stepProcess = number;
            resetStatus();
            vm.documentsConfig[index].active = true;
            vm.isShowLetterIn = false;
            vm.isShowLetterOut = false;
            vm.isShowConfig = true;
            if(vm.stepProcess==0 ||vm.stepProcess==1){//nhóm sổ
                getListDocumentBookGroup(vm.pageIndexDocGroup, vm.pageSizeDocGroup);
            }else if(vm.stepProcess==2){//sổ văn bản
                getListDocumentBookGroup(1,1000);
                getListDocumentBook(vm.pageIndexBook,vm.pageSizeBook);

            }else if(vm.stepProcess==3){//quy trình xử lý vb
                vm.getSteps();
                vm.getTaskFlows();

            }else if(vm.stepProcess==4){//loại văn bản
                vm.getDocumentTypes();

            }
            else if(vm.stepProcess==5){//lĩnh vực văn bản
                vm.getDocumentFields();

            }
            else if(vm.stepProcess==6){//độ ưu tiên văn bản
                vm.getDocumentPriorities();

            }
            else if(vm.stepProcess==7){//độ mật văn bản
                vm.getDocumentSecurityLevels();
            }
        };

        vm.showConfig = function (number,index) {
            number=number+1;
            index=index+1;
            vm.stepProcess = number;
            resetStatus();
            vm.documentsConfig[index].active = true;
            vm.isShowLetterOut = false;
            vm.isShowLetterIn = false;
            vm.isShowConfig = true;
            getListDocumentBookGroup(vm.pageIndexDocGroup, vm.pageSizeDocGroup);
        };

        vm.hideConfig = function (number,index) {
            vm.stepProcess = number;
            resetStatus();
            vm.documentsConfig[index].active = true;
            vm.isShowConfig = false;

        };
        //---------------------------//

        //LIST LETTER IN
        //SHOW PAGE//
        vm.isShowPage = true;
        vm.showPage = function () {
            if(vm.isShowPage){
                vm.isShowPage = false;
                return;
            }
            if(!vm.isShowPage){
                vm.isShowPage = true;
                return;
            }
        };

        //SHOW DETAIL//
        vm.isShowDetail = true;
        vm.showDetail = function () {
            if(vm.isShowDetail){
                vm.isShowDetail = false;
                return;
            }
            if(!vm.isShowDetail){
                vm.isShowDetail = true;
                return;
            }
        };
        //=============//

        function getPageLetterInByIndex(stepIndex, currentParticipateStates, pageIndex, pageSize) {
            service.getPageLetterInByIndex(stepIndex, currentParticipateStates, pageIndex, pageSize).then(function (data) {
                vm.letterIns = data.content;
                console.log(data.content);
                vm.bsTableControl.options.data = vm.letterIns;
                vm.bsTableControl.options.totalRows = data.totalElements;
                console.log(vm.letterIns);
            });
        }

        vm.task = {};
        vm.forwarder = {}; //sau thì cần phân lại theu user đăng nhập
        vm.assigner = {};
        function getLetterInDocumentById(id) {
            service.getLetterInDocumentById(id).then(function (data) {
                vm.letterIn = data;
                console.log(vm.letterIn);
                vm.forwarder = data.task.participates[1];
                vm.bsTableControl4Files.options.data = vm.letterIn.attachments;
                if(data && data.task && data.task && data.task.participates && data.task.participates.length > 0){
                    vm.assigner = data.task.participates.find(function(element){
                        return element.role.code === "AssignerRole"; 
                    });
                    if(vm.assigner.role.code !== "AssignerRole"){
                        vm.assigner = {};
                    }
                }
                if (vm.letterIn.attachments != null && vm.letterIn.attachments.length > 0) {
                	vm.bsTableControl4FilesProcess.options.data = vm.letterIn.attachments;
                	// vm.bsTableControl4FilesProcess.options.totalRows = vm.letterIn.attachments.length;
                }
                vm.getSelectedTaskOwners();
            });
        }
        vm.bsTableControlTaskOwner = {
            options: {
                data: [],
                idField: 'id',
                sortable: true,
                striped: false,
                maintainSelected: false,
                clickToSelect: false,
                showColumns: true,
                showToggle: true,
                pagination: true,
                pageSize: vm.pageSize,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: service.getTableDefinitionTaskOwner(),
                onCheck: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedTaskOwner = row;
                        selectedTaskOwner.push()
                        vm.modalInstanceTaskOwner.close();
                        vm.selectedletterIns.push(row);
                    });
                },
               
                onPageChange: function (index, pageSize) {
                    vm.pageSizeTaskOwner = pageSize;
                    vm.pageIndexTaskOwner = index - 1;
                    vm.getAllTaskOwner(vm.pageIndexTaskOwner,vm.pageSizeTaskOwner);
                }
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
                    getPageLetterInByIndex(vm.stepIndex,-1,vm.pageIndex,vm.pageSize);
                }
            }
        };


        //SAVE LETTER
        vm.forwarderId = null;
        vm.isNew = true;
        function createLetterIn() {
            service.newLetterIn(vm.letterIn,-1,vm.forwarderId, function success() {
                toastr.info('Bạn đã vào sổ văn bản thành công.', 'Thông báo');
                getPageLetterInByIndex(vm.stepIndex,-1,vm.pageIndex,vm.pageSize);
                vm.letterIn = {};
                vm.isNew = true;
            }, function failure() {
                toastr.error('Có lỗi xảy ra khi vào sổ văn bản.', 'Thông báo');
            });
        }

        vm.assignerId = null;
        function createLetterInStepAssign() {
            service.newLetterInStepAssign(vm.letterIn,-1,vm.forwarderId,vm.assignerId, function success() {
                service.saveParticipate(vm.forwarder, function success() {
                    getPageLetterInByIndex(vm.stepIndex,-1,vm.pageIndex,vm.pageSize);
                    vm.letterIn = {};
                    vm.isNew = true;
                    toastr.info('Bạn đã vào sổ văn bản thành công.', 'Thông báo');
                });
            }, function failure() {
                toastr.error('Có lỗi xảy ra khi vào sổ văn bản.', 'Thông báo');
            });
        }
        
        vm.checkValid = function() {
            console.log("vm.letterIn");
            console.log(vm.letterIn.officialDate);
        	if (vm.letterIn == null) {
                toastr.error('Có lỗi xảy ra vui lòng tải lại trang.', 'Thông báo');
                return false;
			}
        	if (vm.letterIn.docOriginCode == null || vm.letterIn.docOriginCode == '') {
                toastr.warning('Vui lòng nhập số ký hiệu văn bản.', 'Thông báo');
                return false;
			}
        	if (vm.letterIn.docCode == null || vm.letterIn.docCode == '') {
                toastr.warning('Vui lòng nhập số nội bộ.', 'Thông báo');
                return false;
			}
        	if (vm.letterIn.briefNote == null || vm.letterIn.briefNote == '') {
                toastr.warning('Vui lòng nhập trích yếu.', 'Thông báo');
                return false;
			}
        	if (vm.letterIn.letterDocBookGroup == null || vm.letterIn.letterDocBookGroup.id == null || vm.letterIn.letterDocBookGroup.id == '') {
                toastr.warning('Vui lòng chọn nhóm sổ văn bản.', 'Thông báo');
                return false;
			}
        	if (vm.letterIn.letterDocBook == null || vm.letterIn.letterDocBook.id == null || vm.letterIn.letterDocBook.id == '') {
                toastr.warning('Vui lòng chọn sổ văn bản.', 'Thông báo');
                return false;
			}
        	if (vm.letterIn.documentOrderNoByBook == null || vm.letterIn.documentOrderNoByBook == '') {
                toastr.warning('Vui lòng nhập số đến theo sổ.', 'Thông báo');
                return false;
			}
        	if (vm.letterIn.letterDocumentType == null || vm.letterIn.letterDocumentType.id == null || vm.letterIn.letterDocumentType.id == '') {
                toastr.warning('Vui lòng chọn loại văn bản.', 'Thông báo');
                return false;
			}
        	if (vm.letterIn.deliveredDate == null || vm.letterIn.deliveredDate == '') {
                console.log("vm.letterIn.deliveredDate");
                console.log(vm.letterIn);
                toastr.warning('Vui lòng nhập ngày đến.', 'Thông báo');
                return false;
			}
        	if (vm.letterIn.registeredDate == null || vm.letterIn.registeredDate == '') {
                toastr.warning('Vui lòng nhập ngày vào sổ.', 'Thông báo');
                return false;
			}
        	if (vm.letterIn.attachments == null || vm.letterIn.attachments.length <=0) {
                toastr.warning('Vui lòng chọn và tải file đính kèm.', 'Thông báo');
                return false;
			}
        	var dateNow = new Date();
        	if (vm.letterIn.issuedDate != null && vm.letterIn.issuedDate != '') {
				if (angular.isDate(vm.letterIn.issuedDate)) {
					if (vm.letterIn.issuedDate > dateNow) {
		                toastr.warning('Ngày ban hành không được lớn hơn ngày hiện tại.', 'Thông báo');
		                return false;
					}
				}
			}
        	if (vm.letterIn.officialDate != null && vm.letterIn.officialDate != '') {
				if (angular.isDate(vm.letterIn.officialDate)) {
					if (vm.letterIn.officialDate > dateNow) {
		                toastr.warning('Ngày hiệu lực không được lớn hơn ngày hiện tại.', 'Thông báo');
		                return false;
					}
				}
			}
        	/*if (vm.letterIn.issuedDate == null || vm.letterIn.issuedDate == '') {
                toastr.warning('Vui lòng nhập ngày ban hành.', 'Thông báo');
                return false;
			}
        	if (vm.letterIn.officialDate == null || vm.letterIn.officialDate == '') {
                toastr.warning('Vui lòng nhập ngày hiệu lực.', 'Thông báo');
                return false;
			}*/
        	/*if (vm.letterIn.expiredDate == null || vm.letterIn.expiredDate == '') {
                toastr.warning('Vui lòng nhập hạn trả lời.', 'Thông báo');
                return false;
			}*/
        	/*if (vm.letterIn.letterDocField == null || vm.letterIn.letterDocField.id == null || vm.letterIn.letterDocField.id == '') {
                toastr.warning('Vui lòng chọn lĩnh vực.', 'Thông báo');
                return false;
			}*/
        	if (vm.letterIn.isOtherIssueOrgan) {
	        	if (vm.letterIn.otherIssueOrgan == null || vm.letterIn.otherIssueOrgan == '') {
	                toastr.warning('Vui lòng nhập cơ quan ban hành .', 'Thông báo');
	                return false;
				}
				
			}else {
	        	if (vm.letterIn.issueOrgan == null || vm.letterIn.issueOrgan.id == null || vm.letterIn.issueOrgan.id == '') {
	                toastr.warning('Vui lòng chọn cơ quan ban hành.', 'Thông báo');
	                return false;
				}
			}
        	
        	if (vm.isForward) {
			}
        	
            return true;
		}
        
        vm.newLetterIn = function () {
            // resetStatus();
        	if (vm.checkValid()) {
                for(var i = 0; i < vm.documentsConfig.length; i++){
                    vm.documentsConfig[i].active = false;
                }
                for(var i = 0; i < vm.documentsTranfered.length; i++){
                    vm.documentsTranfered[i].active = false;
                }
                for(var i = 0; i < vm.documentsRecieved.length; i++){
                    vm.documentsRecieved[i].active = false;
                }
                vm.isClickForward = false;
                vm.isShowPage = true;
                vm.forwardStepTwo = false;
                vm.stepProcess = 0;
                vm.isNew = true;
                vm.isView=false;
                if(vm.isForward==true){//trường hợp chuyển để phân luông sẽ set menu phân luồng active
                    vm.documentsRecieved[3].active = true;
                    vm.stepProcess=9;
                    vm.stepIndex=1;
                }else{ // không chuyển phân luồng sẽ set menu văn bản mới vào sổ active
                    vm.documentsRecieved[2].active = true;
                    vm.stepProcess=2;
                    vm.stepIndex=0;
                }

                createLetterIn();
			}
        };
		vm.viewLetterIn=function(){
		
		}
		vm.reworkLetterIn=function(){
			vm.letterIn={};
			resetStatus();
			vm.forwarderId=null;
			vm.isForward=false;
		}
		
		vm.cancelLetterIn=function(){
			for(var i = 0; i < vm.documentsConfig.length; i++){
				vm.documentsConfig[i].active = false;
			}
			for(var i = 0; i < vm.documentsTranfered.length; i++){
				vm.documentsTranfered[i].active = false;
			}
			for(var i = 0; i < vm.documentsRecieved.length; i++){
				vm.documentsRecieved[i].active = false;
			}
			vm.documentsRecieved[2].active = true;
			vm.stepProcess=2;
			vm.stepIndex=0;
			getPageLetterInByIndex(vm.stepIndex,-1,vm.pageIndex,vm.pageSize);
		}

        $scope.editDocument = function (id) {
            vm.isNew = false;
            vm.stepProcess = 1;
            resetStatus();
            vm.documentsRecieved[1].active = true;
            getLetterInDocumentById(id);
        };
        vm.isView=false;
        $scope.viewDocument = function (id) {
            vm.isView = true;
            vm.isShowPage=false;
            getLetterInDocumentById(id);
        };
		vm.cancelViewDocument=function () {
             vm.isView=false;
            vm.isShowPage = true;
        }
		
		vm.deleteDocument = function (documentId) {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            getLetterInDocumentById(documentId);

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    if(vm.letterIn != null){
                        if(!angular.isUndefined(vm.letterIn.id)){
                            deleteDocument(vm.letterIn.id);
                        }
                    }
                }
            }, function () {
                console.log("cancel");
            });
        }
		function deleteDocument(documentId) {
            service.deleteDocument(documentId).then(function (data) {
               vm.isShowPage=true;
			   vm.isView=false;
			    if(angular.isDefined(vm.stepIndex) && vm.stepIndex != null){ 
                getPageLetterInByIndex(vm.stepIndex,-1,vm.pageIndex,vm.pageSize);
            }
                toastr.info('Xóa thành công.', 'Thông báo');
            });
        }


        //END SAVE LETTER//

        //GET ONE LETTER//
        //Forward//
        vm.isForward = false;
        vm.isClickForward = false;
        $scope.forwardLetter = function (id) {
            // vm.stepProcess=1;
            vm.isClickForward = true;
            vm.isShowPage = false;
            getLetterInDocumentById(id);
        };

        vm.saveForwardLetterIn= function () {
            for(var i = 0; i < vm.documentsConfig.length; i++){
                vm.documentsConfig[i].active = false;
            }
            for(var i = 0; i < vm.documentsTranfered.length; i++){
                vm.documentsTranfered[i].active = false;
            }
            for(var i = 0; i < vm.documentsRecieved.length; i++){
                vm.documentsRecieved[i].active = false;
            }
            vm.isShowPage = true;
            vm.isClickForward = false;
            createLetterIn();
            //sau khi chuyển phân luồng sẽ sang menu phân luồng
            vm.documentsRecieved[3].active = true;
            vm.stepProcess=9;
            vm.stepIndex=1;
        };
        vm.cancelForwardLetterIn= function () {
            vm.isClickForward = false;
            vm.isShowPage = true;
        };

        vm.forwardTo = function () {
            if(!vm.isForward){
                vm.forwarderId = null;
            }
        };
        //Forward STEP TWO//
        vm.forwardStepTwo = false;
        $scope.forwardLetterStepTwo = function (id) {
            vm.taskComment.comment='';
            vm.assignerId=null;
            
            // vm.stepProcess=1;
            vm.forwardStepTwo = true;
            vm.isClickForward = true;
            vm.isShowPage = false;
            getLetterInDocumentById(id);
        };

        vm.taskComment = {
            comment: '',
            participate: vm.forwarder
        };
        vm.implementForwardLetterStepTwo = function () {
            if(vm.taskComment.comment.length <= 0){
                vm.taskComment.comment = 'Kính gửi lãnh đạo xử lý';
            }
            //cập nhật comment cho participate vừa phân luồng
            if(vm.forwarder.comments == null || vm.forwarder.comments.length <= 0){
                vm.forwarder.comments = [];
            }
            vm.forwarder.comments.push(vm.taskComment);
            createLetterInStepAssign();
            for(var i = 0; i < vm.documentsConfig.length; i++){
                vm.documentsConfig[i].active = false;
            }
            for(var i = 0; i < vm.documentsTranfered.length; i++){
                vm.documentsTranfered[i].active = false;
            }
            for(var i = 0; i < vm.documentsRecieved.length; i++){
                vm.documentsRecieved[i].active = false;
            }
            vm.isShowPage = true;
            //sau khi  phân luồng sẽ sang menu văn bản chờ giao xử lý
            vm.documentsRecieved[4].active = true;
            vm.stepProcess=3;
            vm.stepIndex=2;
        };
        vm.cancelForwardLetterStepTwo=function () {
            vm.forwardStepTwo = false;
            vm.isClickForward = false;
            vm.isShowPage = true;
        }
        //=============//

        //Assign//
        vm.isClickAssign = false;
        vm.assinger = {};
        $scope.assignLetter = function (id) {
            vm.isClickAssign = true;
            vm.isShowPage = false;
            getLetterInDocumentById(id);
            // if(vm.letterIn != null){
            //     if(vm.letterIn.task != null && vm.letterIn.participates != null){
            //         for(var i = 0; i < vm.letterIn.participates.length; i++){
            //             if(vm.letterIn.participates[i].role != null){
            //                 if(vm.letterIn.participates[i].role.code === 'AssignerRole'){
            //                     vm.letterIn.participates
            //                 }
            //             }
            //         }
            //     }
            // }
        };
        
        vm.assignProcess = function (){
            // vm.chairmanStaffs 
            // vm.participateStaffs
            vm.participates = [];
            vm.participates.push({
                role:{
                    code:'ChairmanRole'
                },
                taskOwner:vm.selectedTaskOwner,
                displayName:vm.selectedTaskOwner.displayName,
                task:{
                    id: vm.letterIn.task.id
                },
                participateType:3,
            });
            vm.listSelectedTaskOwner.forEach(function(taskOwner){
                vm.participates.push({
                    role:{
                        code:"ProcessRole",
                    },
                    taskOwner:taskOwner,
                    displayName:taskOwner.displayName,
                    task:{
                        id: vm.letterIn.task.id
                    },
                    participateType:3,
                })
            });
            console.log(vm.chairmanStaffs);
            service.assignTask(vm.letterIn.id,vm.participates,function success(){
                    vm.listSelectedTaskOwner = [];
                    vm.selectedTaskOwner = {};
                },function failure(){
                    
                });
            //head department
            if($scope.selectedHeadDepartments != null && $scope.selectedHeadDepartments.length > 0){
                var participate = {};
                participate.displayName = $scope.selectedHeadDepartments[0].text;
                participate.department = {};
                participate.department.id = $scope.selectedHeadDepartments[0].id;
                participate.participateType = 0;
                participate.role = {};
                participate.role.id = 4; // tạm thời
                participate.task = {};
                participate.task.id = vm.letterIn.task.id;
                vm.letterIn.task.participates.push(participate);
            }


            //Chairman
            if(vm.chairmanStaffs != null && vm.chairmanStaffs.length > 0){
                var participate = {};
                participate.displayName = vm.chairmanStaffs[0].text;
                participate.employee = {};
                participate.employee.id = vm.chairmanStaffs[0].id;
                participate.participateType = 1;
                participate.role = {};
                participate.role.id = 4; // tạm thời
                participate.task = {};
                participate.task.id = vm.letterIn.task.id;
                vm.letterIn.task.participates.push(participate);
            }

            //participate department
            if($scope.selectedParticipateDepartments != null && $scope.selectedParticipateDepartments.length > 0){
                for(var i = 0; i < $scope.selectedParticipateDepartments.length; i++){
                    var participate = {};
                    participate.displayName = $scope.selectedParticipateDepartments[i].text;
                    participate.department = {};
                    participate.department.id = $scope.selectedParticipateDepartments[i].id;
                    participate.participateType = 0;
                    participate.role = {};
                    participate.role.id = 5; // tạm thời
                    participate.task = {};
                    participate.task.id = vm.letterIn.task.id;
                    vm.letterIn.task.participates.push(participate);
                    participate = null;
                }
            }

            //participate staff
            if(vm.participateStaffs != null && vm.participateStaffs.length > 0){
                for(var i = 0; i < vm.participateStaffs.length; i++){
                    var participate = {};
                    participate.displayName = vm.participateStaffs[i].text;
                    participate.employee = {};
                    participate.employee.id = vm.participateStaffs[i].id;
                    participate.participateType = 1;
                    participate.role = {};
                    participate.role.id = 5; // tạm thời
                    participate.task = {};
                    participate.task.id = vm.letterIn.task.id;
                    vm.letterIn.task.participates.push(participate);
                    participate = null;
                }
            }

            // service.assignProcess(vm.letterIn,-1,vm.forwarderId,vm.assignerId, function success() {
            // 	alert(vm.assignerId);
            // 	console.log(vm.assignerId);
            //     service.saveListParticipate(vm.letterIn.task.participates, function success() {
            //         getPageLetterInByIndex(vm.stepIndex,-1,vm.pageIndex,vm.pageSize);
            //         vm.letterIn = {};
            //         vm.isNew = true;
            //         toastr.info('Bạn đã vào sổ văn bản thành công.', 'Thông báo');
            //     });
            // }, function failure() {
            //     toastr.error('Có lỗi xảy ra khi vào sổ văn bản.', 'Thông báo');
            // });
        };
        vm.cancelassignProcess=function () {
            vm.isClickAssign=false;
            vm.isShowPage=true;
        }
        //------//
        
        //TREE ORGANIZATION//
        vm.treeData = [];
        vm.organizationDetail = {};
        $scope.treeInstance = null;
        // $scope.treeConfig.version = 0;
        $scope.treeConfig = {
            core: {
                error: function (error) {
                    $log.error('treeCtrl: error from js tree - ' + angular.toJson(error));
                },
                check_callback: true
            },
            plugins: ['types', 'state', 'search']
            // version: 1
        };

        $scope.readyCB = function () {
            // getOrganizationTree();
        }
        $scope.selectNode = function (node, selected, event) {
            vm.getOrganizationDetailById(selected.node.id);
        }

        $scope.search = '';

        $scope.applySearch = function () {
            // getOrganizationTree();
            var to = false;
            if (to) {
                clearTimeout(to);
            }
            to = setTimeout(function () {
                if ($scope.treeInstance) {
                    $scope.treeInstance.jstree(true).search($scope.search);
                }
            }, 250);
        };

        function getOrganizationTree() {
            service.getOrganizationTree().then(function (data) {
                vm.treeData = data;
                vm.organizations = data;
                $scope.treeConfig.version = 1;
            });
        }
        vm.reloadTree = function () {
            $scope.treeConfig.version++;
        };
        getOrganizationTree();
        
        vm.selectOrganization = function () {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'select_organization_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'lg'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    vm.letterIn.issueOrgan = vm.organizationDetail;
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        vm.getOrganizationDetailById = function (organizationId) {
            service.getOrganization(organizationId).then(function (data) {
                vm.organizationDetail = data;
                vm.bsTableControl.options.data = vm.childrens;
            });
        };
        //---------------//

        //TREE DEPARTMENT//
        // Tree view
        vm.treeDepartmentData = [];
        vm.selectHead = false;
        $scope.selectedHeadDepartments = [];
        $scope.selectedParticipateDepartments = [];
        vm.tempParticipateDepartments = [];
        $scope.treeDepartmentInstance = null;
        // $scope.treeConfig.version = 0;
        $scope.treeDepartmentConfig = {
            core: {
                error: function (error) {
                    $log.error('treeCtrl: error from js tree - ' + angular.toJson(error));
                },
                check_callback: true,
            },
            checkbox: {
                three_state: false
            },
            plugins: ['types', 'state', 'search','checkbox','changed']
            // version: 1
        };

        $scope.readyDepartmentCB = function (node, instance) {
            $("#tree").jstree(true).uncheck_all();
            if(vm.isSelectStaff){
                // vm.staffSearchDto.department.id = null;
                vm.getStaffs();
            }
            vm.tempParticipateDepartments = [];
            // if(!vm.selectHead){
            //     vm.tempParticipateDepartments = [];
            // }
        };

        $scope.selectDepartmentNode = function (node, selected, event) {
            if(vm.isSelectStaff){
                if(vm.staffSearchDto.department == null){
                    vm.staffSearchDto.department = {};
                }
                vm.staffSearchDto.department.id = selected.node.id;
                // if(!vm.isSelectStaff){
                vm.searchByDto();
                // }
                // if(vm.isSelectStaff){
                //     vm.getStaffs();
                // }
            }

            if(!vm.selectHead){
                var selectedDepartment = {};
                selectedDepartment.text = selected.node.text;
                selectedDepartment.id = selected.node.id;
                var dup = false;
                for(var i = 0; i < vm.tempParticipateDepartments.length; i++){
                    if(vm.tempParticipateDepartments[i].id === selectedDepartment.id){
                        dup = true;
                    }
                }
                if(!dup){
                    vm.tempParticipateDepartments.push(selectedDepartment);
                    selectedDepartment = null;
                }
            }

            vm.getDepartmentDetailById(selected.node.id);
        };

        $scope.unCheckDepartment = function (node, selected, event) {
            for(var i = 0; i < vm.tempParticipateDepartments.length; i++){
                if(vm.tempParticipateDepartments[i].id === selected.node.id){
                    vm.tempParticipateDepartments.splice(i,1);
                }
            }
        }

        $scope.searchDepartment = '';

        $scope.applySearchDepartment = function () {
            var to = false;
            if (to) {
                clearTimeout(to);
            }
            to = setTimeout(function () {
                if ($scope.treeDepartmentInstance) {
                    $scope.treeDepartmentInstance.jstree(true).search($scope.searchDepartment);
                }
            }, 250);
        };

        function getDepartmentTree() {
            departmentService.getDepartmentTree().then(function (data) {
                vm.treeDepartmentData = data;
                $scope.treeDepartmentConfig.version = 1;
            });
        }
        vm.reloadTreeDepartment = function () {
            $scope.treeDepartmentConfig.version++;
        };
        getDepartmentTree();
        vm.pageIndexTaskOwner = 1;
        vm.pageSizeTaskOwner = 25;
        vm.tableDataTaskOwner = [];
        vm.type = 0;
        vm.listSelectedTaskOwner = []
        vm.getAllTaskOwner = function (){
            taskOwnerService.getTaskOwners(vm.pageIndexTaskOwner,vm.pageSizeTaskOwner).then(function(data){
                if(vm.type=== 0){
                    vm.tableDataTaskOwner = data.content.map(el=>{
                        if(vm.selectedTaskOwner && vm.selectedTaskOwner.id === el.id){
                            el.selected = true;
                        }else {
                            el.selected = false;
                        }
                        return el;
                    });
                }else{
                    if(vm.type === 1){
                        // let listSelectedTaskOwnerID = vm.letterIn.task.participates.map(function(element){
                        //     return element.id;
                        // });
                        let listSelectedTaskOwnerID = vm.listSelectedTaskOwner.map(function(element){
                            return element.id;
                        });

                        vm.tableDataTaskOwner = data.content.map(el=>{
                            if(listSelectedTaskOwnerID.indexOf(el.id) !== -1){
                                el.selected = true;
                            }else {
                                el.selected = false;
                            }
                            return el;
                        });
                        
                    }
                }
                
            });
            
        }
        function getSelectedTaskOwners(){
            vm.listSelectedTaskOwner = vm.tableDataTaskOwner.filter(function(el){
                return el.selected === true;
            });
        }
        vm.removeTaskOwner = function(index){
            vm.listSelectedTaskOwner.splice(index,1);
        }
        $scope.onClicked = function(index,value){
           if(vm.type == 0){
            for(let i = 0,temp = vm.tableDataTaskOwner,len = vm.tableDataTaskOwner.length;i < len;i++){
                if(index === i)  {
                    temp[index].selected = value;
                    vm.selectedTaskOwner = temp[index];
                    continue;
                }
                temp[i].selected = false;
            }
            console.log(vm.tableDataTaskOwner.map(el=>el.selected))
           }else {
               if(vm.type === 1){
                    vm.tableDataTaskOwner[index].selected = value;
               }
           }
        }
        vm.selectChairman = function (type) {
            vm.type = type;
            vm.getAllTaskOwner();
            vm.modalInstanceTaskOwner = modal.open({
                animation: true,
                templateUrl: 'select_taskowner_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'lg'
            }); 

            vm.modalInstanceTaskOwner.result.then(function (confirm) {
                if (confirm == 'yes') {
                  
                    if(type===0){
                        // chọn chủ trì
                        for(let index = 0,temp = vm.bsTableControlTaskOwner.options.data ,len = vm.bsTableControlTaskOwner.options.data.length;index < len;index++){
                            vm.selectedTaskOwner = temp[index];
                        }
                    }
                    if(type===1){
                        // chọn  người tham gia
                        vm.getSelectedTaskOwners();
                    }
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        vm.getDepartmentDetailById = function (departmentId) {
            departmentService.getDepartment(departmentId).then(function (data) {
                vm.departmentDetail = data;
                // vm.bsTableControl.options.data = vm.childrens;
            });
        };
        //---------------//

        //Staff//
        vm.staffSearchDto = {};
        vm.staffs = [];
        vm.staff = {};
        vm.selectedStaffs = [];
        vm.isSelectStaff = false;
        vm.staffPageIndex = 1;
        vm.staffPageSize = 15;
        vm.isSelectChairman = false;
        vm.tempSelected = [];
        vm.chairmanStaffs = [];
        vm.participateStaffs = [];

        vm.searchByDtoPageChane = function () {
            service.searchDto(vm.staffSearchDto,vm.staffPageIndex,vm.staffPageSize).then(function (data) {
                vm.staffs = data.content;
                for(var i = 0; i < vm.staffs.length; i++){
                    vm.staffs[i].checkboxSelected = false;
                }
                for(var i = 0; i < vm.staffs.length; i++){
                    for(var j = 0; j < vm.tempSelected.length; j++){
                        if(vm.staffs[i].id == vm.tempSelected[j].id){
                            vm.staffs[i].checkboxSelected = true;
                        }
                    }
                }
                vm.bsStaffTableControl.options.data = vm.staffs;
                vm.bsStaffTableControl.options.totalRows = data.totalElements;
            });
        };

        vm.searchByDto = function () {
            vm.staffPageIndex = 1;
            vm.bsStaffTableControl.state.pageNumber = 1;
            service.searchDto(vm.staffSearchDto,vm.staffPageIndex,vm.staffPageSize).then(function (data) {
                vm.staffs = data.content;
                for(var i = 0; i < vm.staffs.length; i++){
                    vm.staffs[i].checkboxSelected = false;
                }
                for(var i = 0; i < vm.staffs.length; i++){
                    for(var j = 0; j < vm.tempSelected.length; j++){
                        if(vm.staffs[i].id == vm.tempSelected[j].id){
                            vm.staffs[i].checkboxSelected = true;
                        }
                    }
                }
                vm.bsStaffTableControl.options.data = vm.staffs;
                vm.bsStaffTableControl.options.totalRows = data.totalElements;
            });
        };

        vm.getStaffs = function () {
            service.getStaffs(vm.staffPageIndex, vm.staffPageSize).then(function (data) {
                vm.staffs = data.content;
                for(var i = 0; i < vm.staffs.length; i++){
                    vm.staffs[i].checkboxSelected = false;
                }
                for(var i = 0; i < vm.staffs.length; i++){
                    for(var j = 0; j < vm.tempSelected.length; j++){
                        if(vm.staffs[i].id == vm.tempSelected[j].id){
                            vm.staffs[i].checkboxSelected = true;
                        }
                    }
                }
                vm.bsStaffTableControl.options.data = vm.staffs;
                vm.bsStaffTableControl.options.totalRows = data.totalElements;
            });
        };

        function getStaffByCode(textSearch, pageIndex, pageSize) {
            service.getStaffByCode(textSearch, pageIndex, pageSize).then(function (data) {
                vm.staffs = data.content;
                for(var i = 0; i < vm.staffs.length; i++){
                    vm.staffs[i].checkboxSelected = false;
                }
                for(var i = 0; i < vm.staffs.length; i++){
                    for(var j = 0; j < vm.tempSelected.length; j++){
                        if(vm.staffs[i].id == vm.tempSelected[j].id){
                            vm.staffs[i].checkboxSelected = true;
                        }
                    }
                }
                vm.bsStaffTableControl.options.data = vm.staffs;
                vm.bsStaffTableControl.options.totalRows = data.totalElements;
            });
        }

        vm.textSearch = '';
        vm.searchByCode = function () {
            vm.textSearch = String(vm.textSearch).trim();
            if (vm.textSearch != '') {
                getStaffByCode(vm.textSearch, vm.staffPageIndex, vm.staffPageSize);
            }
            if (vm.textSearch == '') {
                vm.getStaffs();
            }
        };

        vm.enterSearchCode = function(){
            vm.bsStaffTableControl.state.pageNumber = 1;
            vm.staffPageIndex = 1;
            if(event.keyCode == 13){//Phím Enter
                vm.searchByCode();
            }
        };

        vm.bsStaffTableControl = {
            options: {
                data: vm.staffs,
                idField: 'id',
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: false,
                showToggle: false,
                singleSelect: true,
                pagination: true,
                pageSize: vm.staffPageSize,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: service.getStaffTableDefinition(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        if(vm.isSelectChairman){
                            vm.tempSelected = [];
                        }
                        var item = {};
                        item.id = row.id;
                        item.text = row.displayName;
                        vm.tempSelected.push(item);
                        item = null;
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        if(rows == null){
                            rows == [];
                        }
                        for(var i = 0; i < rows.length; i++) {
                            if(vm.tempSelected == null){
                                vm.tempSelected = [];
                            }
                            var dup = false;
                            for(var j = 0; j < vm.tempSelected.length; j++){
                                if(rows[i].id == vm.tempSelected[j].id){
                                    dup = true;
                                }
                            }
                            if(!dup){
                                var item = {};
                                item.id = rows[i].id;
                                item.text = rows[i].displayName;
                                vm.tempSelected.push(item);
                                item = null;
                            }
                        }
                    });
                },
                onUncheck: function (row, $element) {
                    $scope.$apply(function () {
                        for(var j = 0; j < vm.tempSelected.length; j++){
                            if(vm.tempSelected[j].id == row.id){
                                vm.tempSelected.splice(j, 1);
                            }
                        }
                    });
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        for(var i = 0; i < rows.length; i++) {
                            if(vm.tempSelected != null && vm.tempSelected.length > 0){
                                for(var j = 0; j < vm.tempSelected.length; j++){
                                    if(rows[i].id != vm.tempSelected[j].id){
                                        vm.tempSelected.splice(j, 1);
                                    }
                                }
                            }
                        }
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.staffPageSize = pageSize;
                    vm.staffPageIndex = index;
                    vm.searchByDtoPageChane();
                }
            }
        };

        vm.selectStaff = function (type) {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'select_staff_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'lg'
            });

            vm.isSelectStaff = true;
            vm.tempSelected = [];
            if(type === 0){
                vm.bsStaffTableControl.options.singleSelect = true;
                vm.isSelectChairman = true;
            }
            if(type === 1){
                vm.bsStaffTableControl.options.singleSelect = false;
                vm.isSelectChairman = false;
            }
            $scope.treeDepartmentConfig.core.multiple = false;

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    vm.isSelectStaff = false;
                    if(type === 0){
                        vm.chairmanStaffs = [];
                        if(vm.tempSelected != null && vm.tempSelected.length > 0){
                            vm.chairmanStaffs.push(vm.tempSelected[0]);
                        }
                    }
                    if(type === 1){
                        for(var j = 0; j < vm.tempSelected.length; j++){
                            var selectedStaff = {};
                            selectedStaff = vm.tempSelected[j];
                            var dup = false;
                            for(var i = 0; i < vm.participateStaffs.length; i++){
                                if(vm.participateStaffs[i].id === selectedStaff.id){
                                    dup = true;
                                }
                            }
                            if(!dup){
                                vm.participateStaffs.push(selectedStaff);
                                selectedStaff = null;
                            }
                        }
                    }
                }
            }, function () {
                vm.isSelectStaff = false;
                console.log('Modal dismissed at: ' + new Date());
            });
        };
        //------//

        //----------Document Book - Sổ văn bản ---------//
        vm.documentBook = null;
        vm.documentBooks = [];
        vm.selectedDocumentBooks = [];

        // pagination
        vm.pageIndexBook = 1;
        vm.pageSizeBook =25;

        //check duplicate code
        vm.viewCheckDuplicateCodeBook = {};
        vm.tempCodeBook = '';

        // function getListDocumentBookGroup() {
        //     documentBookService.getListDocumentBookGroup(1, 1000).then(function (data) {
        //         vm.documentBookGroups = data.content;
        //     });
        // }

        function getListDocumentBook(pageIndex, pageSize){
            documentBookService.getListDocumentBook(pageIndex,pageSize).then(function(data) {
                vm.documentBooks = data.content;
                console.log(data.content);
                console.log(vm.bsTableControlDocBook);
                if(vm.documentBooks.length <= 0 && data.totalElements != 0){
                    $state.reload();
                }

                vm.bsTableControlDocBook.options.data = vm.documentBooks;
                vm.bsTableControlDocBook.options.totalRows = data.totalElements;
                console.log(data);
            });
        }
        function newDocumentBook(examSkill) {
            documentBookService.newDocumentBook(examSkill).then(function(data) {
                getListDocumentBook(vm.pageIndexBook,vm.pageSizeBook);
                modalInstance.close();
                toastr.info('Thêm mới thành công','Thông báo');
            });
        }

        function getDocumentBookById(documentBookId){
            documentBookService.getDocumentBookById(documentBookId).then(function(data) {
                vm.documentBook = data;
                vm.tempCodeBook = vm.documentBook.code;
                console.log(vm.documentBook);
            });
        }

        function editDocumentBook(documentBook) {
            documentBookService.editDocumentBook(documentBook).then(function (data) {
                getListDocumentBook(vm.pageIndexBook,vm.pageSizeBook);
                modalInstance.close();
                toastr.info('Lưu thành công','Thông báo');
            });
        }

        function deleteDocumentBook(documentBookId) {
            documentBookService.deleteDocumentBook(documentBookId).then(function (data) {
                getListDocumentBook(vm.pageIndexBook,vm.pageSizeBook);
                toastr.info('Xóa thành công.', 'Thông báo');
            });
        }

        //check duplicate code
        function validateEducationLevelBook() {
            console.log(vm.documentBook);
            if(vm.documentBook == null){
                toastr.warning("Chưa nhập dữ liệu");
                return false;
            }
            if(angular.isUndefined(vm.documentBook.code) || vm.documentBook.code == null || vm.documentBook.code.length <= 0){
                toastr.warning("Chưa nhập mã");
                return false;
            }
            return true;
        }

        function checkDuplicateCodeBook(code,type,action){ //type: 1 -> save; 2 -> edit;   action: 1 -> just check code; 2 -> save or edit
            documentBookService.checkDuplicateCode(code).then(function(data) {
                vm.viewCheckDuplicateCodeBook = data;
                if(action == 1){
                    if(type == 1){
                        if(vm.viewCheckDuplicateCodeBook != null && vm.viewCheckDuplicateCodeBook.duplicate == true){
                            toastr.warning("Mã bị trùng");
                        }
                        if(vm.viewCheckDuplicateCodeBook != null && vm.viewCheckDuplicateCodeBook.duplicate == false){
                            toastr.success("Mã không bị trùng");
                        }
                    }
                    if(type == 2){
                        if(vm.tempCodeBook.toLowerCase().trim() != code.toLowerCase().trim()){
                            checkDuplicateCode(vm.tempCodeBook,1,1);
                        }else{
                            toastr.info("Mã chưa thay đổi");
                        }
                    }
                }
                if(action == 2){
                    if(type == 1){
                        if(vm.viewCheckDuplicateCodeBook != null && vm.viewCheckDuplicateCodeBook.duplicate == true){
                            toastr.warning("Mã bị trùng");
                        }
                        if(vm.viewCheckDuplicateCodeBook != null && vm.viewCheckDuplicateCodeBook.duplicate == false){
                            newDocumentBook(vm.documentBook);
                        }
                    }
                    if(type == 2){
                        if(vm.tempCodeBook.toLowerCase().trim() != code.toLowerCase().trim()){
                            documentBookService.checkDuplicateCode(vm.tempCodeBook).then(function(data) {
                                vm.viewCheckDuplicateCodeBook = data;
                                if(vm.viewCheckDuplicateCodeBook != null && vm.viewCheckDuplicateCodeBook.duplicate == true){
                                    toastr.warning("Mã bị trùng");
                                }
                                if(vm.viewCheckDuplicateCodeBook != null && vm.viewCheckDuplicateCodeBook.duplicate == false){
                                    vm.documentBook.code = vm.tempCodeBook.trim();
                                    editDocumentBook(vm.documentBook);
                                }
                            });
                        }else{
                            vm.documentBook.code = vm.tempCodeBook.trim();
                            editDocumentBook(vm.documentBook);
                        }
                    }
                }
                console.log(data);

            });
        }

        vm.checkDuplicateCodeBook = function (type,action) {
            if(validateEducationLevelBook()){
                checkDuplicateCodeBook(vm.documentBook.code,type,action);
            }
        }

        var modalInstance;

        vm.newDocumentBook = function () {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'new_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'md'
            });

            vm.documentBook = {};

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    newDocumentBook(vm.documentBook);
                }
            }, function () {
                console.log("cancel");
            });
        }

        $scope.editDocumentBook = function (documentBookId) {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'md'
            });

            getDocumentBookById(documentBookId);

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    editDocumentBook(vm.documentBook);
                }
            }, function () {
                console.log("cancel");
            });
        }

        $scope.viewDocumentBook = function (documentBookId) {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'view_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'md'
            });

            getDocumentBookById(documentBookId);

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                }
            }, function () {
                console.log("cancel");
            });
        }

        $scope.deleteDocumentBook = function (documentBookId) {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            getDocumentBookById(documentBookId);

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    if(vm.documentBook != null){
                        if(!angular.isUndefined(vm.documentBook.id)){
                            deleteDocumentBook(vm.documentBook.id);
                        }
                    }
                }
            }, function () {
                console.log("cancel");
            });
        }

        // getListDocumentBook(vm.pageIndexBook,vm.pageSizeBook);
        vm.bsTableControlDocBook = {
            options: {
                data: vm.documentBooks,
                idField: 'id',
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: true,
                showToggle: true,
                pagination: true,
                pageSize: vm.pageSizeBook,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: documentBookService.getTableDefinition(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedDocumentBooks.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentBooks = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedDocumentBooks);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedDocumentBooks.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentBooks = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSizeBook = pageSize;
                    vm.pageIndexBook = index;
                    getListDocumentBook(vm.pageIndexBook,vm.pageSizeBook);
                }
            }
        };

        //----------------------------------//

        //---Document Type - Loại văn bản------//
        vm.documentType = {};
        vm.documentTypes = [];
        vm.selectedDocumentTypes = [];

        vm.pageIndexDocType = 1;
        vm.pageSizeDocType = 25;

        vm.getDocumentTypes = function () {
            typeService.getDocumentTypes(vm.pageIndexDocType, vm.pageSizeDocType).then(function (data) {
                vm.documentTypes = data.content;
                console.log(vm.documentTypes);
                vm.bsTableControlDocType.options.data = vm.documentTypes;
                vm.bsTableControlDocType.options.totalRows = data.totalElements;
            });
        };

        // vm.getDocumentTypes();

        vm.bsTableControlDocType = {
            options: {
                data: vm.documentTypes,
                idField: 'id',
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: true,
                showToggle: true,
                pagination: true,
                pageSize: vm.pageSizeDocType,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: typeService.getTableDefinition(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedDocumentTypes.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentTypes = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedDocumentTypes.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentTypes = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSizeDocType = pageSize;
                    vm.pageIndexDocType = index;
                    vm.getDocumentTypes();
                }
            }
        };

        /**
         * New event account
         */
        vm.newDocumentType = function () {

            vm.documentType.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (!vm.documentType.code || vm.documentType.code.trim() == '') {
                        toastr.error('Vui lòng nhập mã.', 'Lỗi');
                        return;
                    }

                    if (!vm.documentType.name || vm.documentType.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên.', 'Lỗi');
                        return;
                    }

                    typeService.saveDocumentType(vm.documentType, function success() {

                        // Refresh list
                        vm.getDocumentTypes();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một tài khoản.', 'Thông báo');

                        // clear object
                        vm.documentType = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một tài khoản.', 'Thông báo');
                    });
                }
            }, function () {
                vm.documentType = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editDocumentType = function (id) {
            typeService.getDocumentType(id).then(function (data) {

                vm.documentType = data;
                vm.documentType.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_modal.html',
                    scope: $scope,
                    backdrop: 'static',
                    size: 'md'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (!vm.documentType.code || vm.documentType.code.trim() == '') {
                            toastr.error('Vui lòng nhập mã.', 'Lỗi');
                            return;
                        }

                        if (!vm.documentType.name || vm.documentType.name.trim() == '') {
                            toastr.error('Vui lòng nhập tên.', 'Lỗi');
                            return;
                        }

                        typeService.saveDocumentType(vm.documentType, function success() {

                            // Refresh list
                            vm.getDocumentTypes();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object
                            vm.documentType = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin tài khoản.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.documentType = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        /**
         * Delete accounts
         */
        $scope.deleteDocumentType = function (id) {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    typeService.deleteDocumentType(id, function success() {

                        vm.getDocumentTypes();

                        toastr.info('Bạn đã xóa thành công bản ghi.', 'Thông báo');

                        vm.selectedDocumentTypes = [];
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi xóa bản ghi.', 'Lỗi');
                    });
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };
        //----------------------------------//
        //----------Task Flow- Quy trình xử lý VB------//
        vm.taskFlow = {};
        vm.taskFlows = [];
        vm.selectedTaskFlows = [];
        vm.steps = [];
        vm.step = null;
        vm.taskFlowSteps = [];
        vm.pageIndexTaskFlow = 1;
        vm.pageSizeTaskFlow = 25;
        vm.autoCreate = false;

        vm.getSteps = function () {
            taskService.getSteps(1, 100000).then(function (data) {
                vm.steps = data.content;
            });
        };
        // vm.getSteps();
        // vm.getTaskFlows();

        vm.getTaskFlows = function () {
            taskService.getTaskFlows(vm.pageIndexTaskFlow, vm.pageSizeTaskFlow).then(function (data) {
                vm.taskFlows = data.content;
                vm.bsTableControlTaskFlow.options.data = vm.taskFlows;
                vm.bsTableControlTaskFlow.options.totalRows = data.totalElements;
                console.log(vm.taskFlows);
            });
        };
        vm.bsTableControlTaskFlow = {
            options: {
                data: vm.taskFlows,
                idField: 'id',
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: true,
                showToggle: true,
                pagination: true,
                pageSize: vm.pageSizeTaskFlow,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: taskService.getTableDefinition(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedTaskFlows.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedTaskFlows = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedTaskFlows.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedTaskFlows = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSizeTaskFlow = pageSize;
                    vm.pageIndexTaskFlow = index;
                    vm.getTaskFlows();
                }
            }
        };
        //---------------------//


        //Step process//
        vm.autoCreateTaskFlowStep = function () {
            if(vm.taskFlow == null){
                vm.taskFlow = {};
            }
            if(vm.steps != null && vm.steps.length > 0){
                if(angular.isUndefined(vm.taskFlow.steps) ||vm.taskFlow.steps == null){
                    vm.taskFlow.steps = [];
                }
                if(vm.autoCreate){
                    for(var j = 0; j < vm.steps.length; j++){
                        var dup = false;
                        for(var i = 0; i < vm.taskFlow.steps.length; i++){
                            if(vm.steps[j].id == vm.taskFlow.steps[i].step.id){
                                dup = true;
                            }
                        }
                        if(!dup){
                            var flowStep = {};
                            flowStep.step = vm.steps[j]
                            vm.taskFlow.steps.push(flowStep);
                        }
                    }
                    console.log(vm.taskFlow);
                }else{
                    vm.taskFlow.steps = [];
                }
            }
        };

        vm.addStep = function () {
            if(vm.taskFlow == null){
                vm.taskFlow = {};
            }
            if(angular.isUndefined(vm.taskFlow.steps) ||vm.taskFlow.steps == null){
                vm.taskFlow.steps = [];
            }
            var dup = false;
            for(var i = 0; i < vm.taskFlow.steps.length; i++){
                if(vm.step.id == vm.taskFlow.steps[i].step.id){
                    dup = true;
                    toastr.warning('Bị trùng');
                }
            }
            if(!dup){
                var flowStep = {};
                flowStep.step = vm.step;
                vm.taskFlow.steps.push(flowStep);
            }
        };
        //----------//
        /**
         * New event account
         */
        vm.newTaskFlow = function () {

            vm.taskFlow.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (!vm.taskFlow.code || vm.taskFlow.code.trim() == '') {
                        toastr.error('Vui lòng nhập mã.', 'Lỗi');
                        return;
                    }

                    if (!vm.taskFlow.name || vm.taskFlow.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên.', 'Lỗi');
                        return;
                    }

                    taskService.saveTaskFlow(vm.taskFlow, function success() {

                        // Refresh list
                        vm.getTaskFlows();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một tài khoản.', 'Thông báo');

                        // clear object
                        vm.taskFlow = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một tài khoản.', 'Thông báo');
                    });
                }
            }, function () {
                vm.taskFlow = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editTaskFlow = function (id) {
            taskService.getTaskFlow(id).then(function (data) {

                vm.taskFlow = data;
                vm.taskFlow.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_modal.html',
                    scope: $scope,
                    backdrop: 'static',
                    size: 'md'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (!vm.taskFlow.code || vm.taskFlow.code.trim() == '') {
                            toastr.error('Vui lòng nhập mã.', 'Lỗi');
                            return;
                        }

                        if (!vm.taskFlow.name || vm.taskFlow.name.trim() == '') {
                            toastr.error('Vui lòng nhập tên.', 'Lỗi');
                            return;
                        }

                        taskService.saveTaskFlow(vm.taskFlow, function success() {

                            // Refresh list
                            vm.getTaskFlows();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object
                            vm.taskFlow = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin tài khoản.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.taskFlow = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        /**
         * Delete accounts
         */
        $scope.deleteTaskFlow = function (id) {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    taskService.deleteTaskFlow(id, function success() {

                        vm.getTaskFlows();

                        toastr.info('Bạn đã xóa thành công bản ghi.', 'Thông báo');

                        vm.selectedTaskFlows = [];
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi xóa bản ghi.', 'Lỗi');
                    });
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };
        //----------------------------------------------//
        //------------Document Fileda------------------//
        vm.documentField = {};
        vm.documentFields = [];
        vm.selectedDocumentFields = [];

        vm.pageIndexDocFiled = 1;
        vm.pageSizeDocFiled = 25;

        vm.getDocumentFields = function () {
            fieldService.getDocumentFields(vm.pageIndexDocFiled, vm.pageSizeDocFiled).then(function (data) {
                vm.documentFields = data.content;
                vm.bsTableControlDocFiled.options.data = vm.documentFields;
                vm.bsTableControlDocFiled.options.totalRows = data.totalElements;
            });
        };
        // vm.getDocumentFields();

        vm.bsTableControlDocFiled = {
            options: {
                data: vm.documentFields,
                idField: 'id',
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: true,
                showToggle: true,
                pagination: true,
                pageSize: vm.pageSizeDocFiled,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: fieldService.getTableDefinition(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedDocumentFields.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentFields = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedDocumentFields.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentFields = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSizeDocFiled = pageSize;
                    vm.pageIndexDocFiled = index;
                    vm.getDocumentFields();
                }
            }
        };

        /**
         * New event account
         */
        vm.newDocumentField = function () {

            vm.documentField.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (!vm.documentField.code || vm.documentField.code.trim() == '') {
                        toastr.error('Vui lòng nhập mã.', 'Lỗi');
                        return;
                    }

                    if (!vm.documentField.name || vm.documentField.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên.', 'Lỗi');
                        return;
                    }

                    fieldService.saveDocumentField(vm.documentField, function success() {

                        // Refresh list
                        vm.getDocumentFields();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một tài khoản.', 'Thông báo');

                        // clear object
                        vm.documentField = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một tài khoản.', 'Thông báo');
                    });
                }
            }, function () {
                vm.documentField = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editDocumentField = function (id) {
            fieldService.getDocumentField(id).then(function (data) {

                vm.documentField = data;
                vm.documentField.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_modal.html',
                    scope: $scope,
                    backdrop: 'static',
                    size: 'md'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (!vm.documentField.code || vm.documentField.code.trim() == '') {
                            toastr.error('Vui lòng nhập mã.', 'Lỗi');
                            return;
                        }

                        if (!vm.documentField.name || vm.documentField.name.trim() == '') {
                            toastr.error('Vui lòng nhập tên.', 'Lỗi');
                            return;
                        }

                        fieldService.saveDocumentField(vm.documentField, function success() {

                            // Refresh list
                            vm.getDocumentFields();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object
                            vm.documentField = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin tài khoản.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.documentField = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        /**
         * Delete accounts
         */
        $scope.deleteDocumentField = function (id) {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    fieldService.deleteDocumentField(id, function success() {

                        vm.getDocumentFields();

                        toastr.info('Bạn đã xóa thành công bản ghi.', 'Thông báo');

                        vm.selectedDocumentFields = [];
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi xóa bản ghi.', 'Lỗi');
                    });
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };
        //---------------------------------------------//
        //-----------Document Priorities--------------//
        vm.documentPriority = {};
        vm.documentPriorities = [];
        vm.selectedDocumentPriorities = [];

        vm.pageIndexDocPriorities = 1;
        vm.pageSizeDocPriorities = 25;

        vm.getDocumentPriorities = function () {
            priorityService.getDocumentPriorities(vm.pageIndexDocPriorities, vm.pageSizeDocPriorities).then(function (data) {
                vm.documentPriorities = data.content;
                console.log(vm.documentPriorities);
                vm.bsTableControlDocPriorities.options.data = vm.documentPriorities;
                vm.bsTableControlDocPriorities.options.totalRows = data.totalElements;
            });
        };
        // vm.getDocumentPriorities();

        vm.bsTableControlDocPriorities = {
            options: {
                data: vm.documentPriorities,
                idField: 'id',
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: true,
                showToggle: true,
                pagination: true,
                pageSize: vm.pageSizeDocPriorities,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: priorityService.getTableDefinition(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedDocumentPriorities.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentPriorities = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedDocumentPriorities.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentPriorities = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSizeDocPriorities = pageSize;
                    vm.pageIndexDocPriorities = index;
                    vm.getDocumentPriorities();
                }
            }
        };

        /**
         * New event account
         */
        vm.newDocumentPriority = function () {

            vm.documentPriority.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (!vm.documentPriority.code || vm.documentPriority.code.trim() == '') {
                        toastr.error('Vui lòng nhập mã.', 'Lỗi');
                        return;
                    }

                    if (!vm.documentPriority.name || vm.documentPriority.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên.', 'Lỗi');
                        return;
                    }

                    priorityService.saveDocumentPriority(vm.documentPriority, function success() {

                        // Refresh list
                        vm.getDocumentPriorities();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một tài khoản.', 'Thông báo');

                        // clear object
                        vm.documentPriority = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một tài khoản.', 'Thông báo');
                    });
                }
            }, function () {
                vm.documentPriority = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editDocumentPriority = function (id) {
            priorityService.getDocumentPriority(id).then(function (data) {

                vm.documentPriority = data;
                vm.documentPriority.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_modal.html',
                    scope: $scope,
                    backdrop: 'static',
                    size: 'md'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (!vm.documentPriority.code || vm.documentPriority.code.trim() == '') {
                            toastr.error('Vui lòng nhập mã.', 'Lỗi');
                            return;
                        }

                        if (!vm.documentPriority.name || vm.documentPriority.name.trim() == '') {
                            toastr.error('Vui lòng nhập tên.', 'Lỗi');
                            return;
                        }

                        priorityService.saveDocumentPriority(vm.documentPriority, function success() {

                            // Refresh list
                            vm.getDocumentPriorities();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object
                            vm.documentPriority = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin tài khoản.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.documentPriority = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        /**
         * Delete accounts
         */
        $scope.deleteDocumentPriority = function (id) {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    priorityService.deleteDocumentPriority(id, function success() {

                        vm.getDocumentPriorities();

                        toastr.info('Bạn đã xóa thành công bản ghi.', 'Thông báo');

                        vm.selectedDocumentPriorities = [];
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi xóa bản ghi.', 'Lỗi');
                    });
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };
        //--------------------------------------------//
        //--------Document security level- độ mật văn bản---//
        vm.documentSecurityLevel = {};
        vm.documentSecurityLevels = [];
        vm.selectedDocumentSecurityLevels = [];

        vm.pageIndexDocSecurity = 1;
        vm.pageSizeDocSecurity = 25;

        vm.getDocumentSecurityLevels = function () {
            securityService.getDocumentSecurityLevels(vm.pageIndexDocSecurity, vm.pageSizeDocSecurity).then(function (data) {
                vm.documentSecurityLevels = data.content;
                console.log(vm.documentSecurityLevels);
                vm.bsTableControlDocSecurity.options.data = vm.documentSecurityLevels;
                vm.bsTableControlDocSecurity.options.totalRows = data.totalElements;
            });
        };

        // vm.getDocumentSecurityLevels();

        vm.bsTableControlDocSecurity = {
            options: {
                data: vm.documentSecurityLevels,
                idField: 'id',
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: true,
                showToggle: true,
                pagination: true,
                pageSize: vm.pageSizeDocSecurity,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: securityService.getTableDefinition(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedDocumentSecurityLevels.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentSecurityLevels = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedDocumentSecurityLevels.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentSecurityLevels = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSizeDocSecurity = pageSize;
                    vm.pageIndexDocSecurity = index;
                    vm.getDocumentSecurityLevels();
                }
            }
        };

        /**
         * New event account
         */
        vm.newDocumentSecurityLevel = function () {

            vm.documentSecurityLevel.isNew = true;

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {

                    if (!vm.documentSecurityLevel.code || vm.documentSecurityLevel.code.trim() == '') {
                        toastr.error('Vui lòng nhập mã.', 'Lỗi');
                        return;
                    }

                    if (!vm.documentSecurityLevel.name || vm.documentSecurityLevel.name.trim() == '') {
                        toastr.error('Vui lòng nhập tên.', 'Lỗi');
                        return;
                    }

                    securityService.saveDocumentSecurityLevel(vm.documentSecurityLevel, function success() {

                        // Refresh list
                        vm.getDocumentSecurityLevels();

                        // Notify
                        toastr.info('Bạn đã tạo mới thành công một tài khoản.', 'Thông báo');

                        // clear object
                        vm.documentSecurityLevel = {};
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi thêm mới một tài khoản.', 'Thông báo');
                    });
                }
            }, function () {
                vm.documentSecurityLevel = {};
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        /**
         * Edit a account
         * @param accountId
         */
        $scope.editDocumentSecurityLevel = function (id) {
            securityService.getDocumentSecurityLevel(id).then(function (data) {

                vm.documentSecurityLevel = data;
                vm.documentSecurityLevel.isNew = false;

                var modalInstance = modal.open({
                    animation: true,
                    templateUrl: 'edit_modal.html',
                    scope: $scope,
                    backdrop: 'static',
                    size: 'md'
                });

                modalInstance.result.then(function (confirm) {
                    if (confirm == 'yes') {

                        if (!vm.documentSecurityLevel.code || vm.documentSecurityLevel.code.trim() == '') {
                            toastr.error('Vui lòng nhập mã.', 'Lỗi');
                            return;
                        }

                        if (!vm.documentSecurityLevel.name || vm.documentSecurityLevel.name.trim() == '') {
                            toastr.error('Vui lòng nhập tên.', 'Lỗi');
                            return;
                        }

                        securityService.saveDocumentSecurityLevel(vm.documentSecurityLevel, function success() {

                            // Refresh list
                            vm.getDocumentSecurityLevels();

                            // Notify
                            toastr.info('Bạn đã lưu thành công một bản ghi.', 'Thông báo');

                            // clear object
                            vm.documentSecurityLevel = {};
                        }, function failure() {
                            toastr.error('Có lỗi xảy ra khi lưu thông tin tài khoản.', 'Lỗi');
                        });
                    }
                }, function () {
                    vm.documentSecurityLevel = {};
                    console.log('Modal dismissed at: ' + new Date());
                });
            });
        };

        /**
         * Delete accounts
         */
        $scope.deleteDocumentSecurityLevel = function (id) {
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    securityService.deleteDocumentSecurityLevel(id, function success() {

                        vm.getDocumentSecurityLevels();

                        toastr.info('Bạn đã xóa thành công bản ghi.', 'Thông báo');

                        vm.selectedDocumentSecurityLevels = [];
                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi xóa bản ghi.', 'Lỗi');
                    });
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        };
        //--------------------------------------------------//
        //--------Document Group Book----------------------//
        vm.documentBookGroup = null;
        vm.documentBookGroups = [];
        vm.selectedDocumentBookGroups = [];
        console.log("group");
        // pagination
        vm.pageIndexDocGroup = 1;
        vm.pageSizeDocGroup = 10;

        //check duplicate code
        vm.viewCheckDuplicateCode = {};
        vm.tempCode = '';

        function getListDocumentBookGroup(pageIndex, pageSize) {
            groupBookService.getListDocumentBookGroup(pageIndex, pageSize).then(function (data) {
                vm.documentBookGroups = data.content;
                vm.bsTableControlDocBookGroup.options.data = vm.documentBookGroups;
                vm.bsTableControlDocBookGroup.options.totalRows = data.totalElements;
            });
        }

        function newDocumentBookGroup(examSkill) {
            groupBookService.newDocumentBookGroup(examSkill).then(function (data) {
                getListDocumentBookGroup(vm.pageIndexDocGroup, vm.pageSizeDocGroup);
                modalInstance.close();
                toastr.info('Thêm mới thành công', 'Thông báo');
            });
        }

        function getDocumentBookGroupById(documentBookGroupId) {
            groupBookService.getDocumentBookGroupById(documentBookGroupId).then(function (data) {
                vm.documentBookGroup = data;
                vm.tempCode = vm.documentBookGroup.code;
                console.log(vm.documentBookGroup);
            });
        }

        function editDocumentBookGroup(documentBookGroup) {
            groupBookService.editDocumentBookGroup(documentBookGroup).then(function (data) {
                getListDocumentBookGroup(vm.pageIndexDocGroup, vm.pageSizeDocGroup);
                modalInstance.close();
                toastr.info('Lưu thành công', 'Thông báo');
            });
        }

        function deleteDocumentBookGroup(documentBookGroupId) {
            groupBookService.deleteDocumentBookGroup(documentBookGroupId).then(function (data) {
                getListDocumentBookGroup(vm.pageIndexDocGroup, vm.pageSizeDocGroup);
                toastr.info('Xóa thành công.', 'Thông báo');
            });
        }

        //check duplicate code
        function validateEducationLevel() {
            console.log(vm.documentBookGroup);
            if (vm.documentBookGroup == null) {
                toastr.warning("Chưa nhập dữ liệu");
                return false;
            }
            if (angular.isUndefined(vm.documentBookGroup.code) || vm.documentBookGroup.code == null || vm.documentBookGroup.code.length <= 0) {
                toastr.warning("Chưa nhập mã");
                return false;
            }
            return true;
        }

        function checkDuplicateCode(code, type, action) { //type: 1 -> save; 2 -> edit;   action: 1 -> just check code; 2 -> save or edit
            groupBookService.checkDuplicateCode(code).then(function (data) {
                vm.viewCheckDuplicateCode = data;
                if (action == 1) {
                    if (type == 1) {
                        if (vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == true) {
                            toastr.warning("Mã bị trùng");
                        }
                        if (vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == false) {
                            toastr.success("Mã không bị trùng");
                        }
                    }
                    if (type == 2) {
                        if (vm.tempCode.toLowerCase().trim() != code.toLowerCase().trim()) {
                            checkDuplicateCode(vm.tempCode, 1, 1);
                        } else {
                            toastr.info("Mã chưa thay đổi");
                        }
                    }
                }
                if (action == 2) {
                    if (type == 1) {
                        if (vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == true) {
                            toastr.warning("Mã bị trùng");
                        }
                        if (vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == false) {
                            newDocumentBookGroup(vm.documentBookGroup);
                        }
                    }
                    if (type == 2) {
                        if (vm.tempCode.toLowerCase().trim() != code.toLowerCase().trim()) {
                            groupBookService.checkDuplicateCode(vm.tempCode).then(function (data) {
                                vm.viewCheckDuplicateCode = data;
                                if (vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == true) {
                                    toastr.warning("Mã bị trùng");
                                }
                                if (vm.viewCheckDuplicateCode != null && vm.viewCheckDuplicateCode.duplicate == false) {
                                    vm.documentBookGroup.code = vm.tempCode.trim();
                                    editDocumentBookGroup(vm.documentBookGroup);
                                }
                            });
                        } else {
                            vm.documentBookGroup.code = vm.tempCode.trim();
                            editDocumentBookGroup(vm.documentBookGroup);
                        }
                    }
                }
                console.log(data);

            });
        }

        vm.checkDuplicateCode = function (type, action) {
            if (validateEducationLevel()) {
                checkDuplicateCode(vm.documentBookGroup.code, type, action);
            }
        }

        var modalInstance;

        vm.newDocumentBookGroup = function () {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'new_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'md'
            });

            vm.documentBookGroup = {};

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    newDocumentBookGroup(vm.documentBookGroup);
                }
            }, function () {
                console.log("cancel");
            });
        }

        $scope.editDocumentBookGroup = function (documentBookGroupId) {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'edit_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'md'
            });

            getDocumentBookGroupById(documentBookGroupId);

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    editDocumentBookGroup(vm.documentBookGroup);
                }
            }, function () {
                console.log("cancel");
            });
        }

        $scope.viewDocumentBook = function (documentBookGroupId) {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'view_modal.html',
                scope: $scope,
                size: 'md'
            });

            getDocumentBookGroupById(documentBookGroupId);

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                }
            }, function () {
                console.log("cancel");
            });
        }

        $scope.deleteDocumentBookGroup = function (documentBookGroupId) {
            modalInstance = modal.open({
                animation: true,
                templateUrl: 'delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            getDocumentBookGroupById(documentBookGroupId);

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    if (vm.documentBookGroup != null) {
                        if (!angular.isUndefined(vm.documentBookGroup.id)) {
                            deleteDocumentBookGroup(vm.documentBookGroup.id);
                        }
                    }
                }
            }, function () {
                console.log("cancel");
            });
        }

        // getListDocumentBookGroup(vm.pageIndexDocGroup, vm.pageSizeDocGroup);

        vm.bsTableControlDocBookGroup = {
            options: {
                data: vm.documentBookGroups,
                idField: 'id',
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: true,
                showToggle: true,
                pagination: true,
                pageSize: vm.pageSizeDocGroup,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: groupBookService.getTableDefinition(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedDocumentBookGroups.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentBookGroups = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedDocumentBookGroups);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedDocumentBookGroups.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedDocumentBookGroups = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSizeDocGroup = pageSize;
                    vm.pageIndexDocGroup = index;
                    getListDocumentBookGroup(vm.pageIndexDocGroup, vm.pageSizeDocGroup);
                }
            }
        };
        //------------------------------------------------//
    }

})();