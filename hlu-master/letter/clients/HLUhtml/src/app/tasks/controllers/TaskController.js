/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.Task').controller('TaskController', TaskController);

    TaskController.$inject = [
        '$rootScope',
        '$scope',
        'toastr',
        '$timeout',
        'settings',
        'Utilities',
        '$uibModal',
        'Upload',
        'FileSaver',
        '$state',
        'TaskService'
    ];

    function TaskController($rootScope, $scope, toastr, $timeout, settings, utils, modal, Uploader, FileSaver, $state, service) {
        $scope.$on('$viewContentLoaded', function () {
            // initialize core components
            App.initAjax();
        });

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageContentWhite = true;
        $rootScope.settings.layout.pageBodySolid = false;
        $rootScope.settings.layout.pageSidebarClosed = false;

        $scope.isView = false;
        var vm = this;
        vm.WorkPlanFlowCode = "WORKPLANFLOW";
        vm.ProjectFlowCode = "PROJECTFLOW";
        vm.DaiLyWorksFlowCode = "DAILYWORKSFLOW";
        vm.LetterInFlowCode = "LetterInCode";
        vm.LetterOutFlowCode = "LETTEROUTFLOW";
        vm.ChairmanRole = "ChairmanRole";	//vai trò xử lý chính
        vm.ProcessRole = "ProcessRole";		//vai trò tham gia

        vm.flow = {};
        vm.taskOwnerRole = ""; 0

        vm.hardCodeWorkFlowCode = [
            { code: "WORKPLANFLOW", templateUrl: 'edit_WorkPlan_modal.html' },
            { code: "PROJECTFLOW", templateUrl: 'edit_Project_modal.html' },
            { code: "DAILYWORKSFLOW", templateUrl: 'edit_DaiLyWorks_modal.html' },
            {
                code: "LetterInCode", templateUrl: 'edit_LetterInDocument_modal.html',
                step1: { code: "LetterInCodeStep1" }, step2: { code: "LetterInCodeStep2" }, step3: { code: "LetterInCodeStep3" },
                step4: { code: "LetterInCodeStep4" }, step5: { code: "LetterInCodeStep5" }
            },
            {
                code: "LETTEROUTFLOW", templateUrl: 'edit_LetterOutDocument_modal.html',
                step1: { code: "LETTEROUTSTEP1" }, step2: { code: "LETTEROUTSTEP2" }
            }
        ];

        vm.task = {};
        vm.tasks = [];
        vm.selectedTasks = [];
        vm.prioritys = [];
        vm.flowId = -1;
        vm.currentStepIdByFlow = -1;

        vm.pageIndex = 1;
        vm.pageSize = 25;
        vm.pageIndexComment = 1;
        vm.pageSizeComment = 10;

        /* TINYMCE */
        vm.tinymceOptions = {
            height: 130,
            theme: 'modern',
            plugins: ['lists fullscreen' // autoresize
            ],
            toolbar1: 'bold underline italic | removeformat | bullist numlist outdent indent | fullscreen',
            content_css: [
                '//fonts.googleapis.com/css?family=Poppins:300,400,500,600,700',
                '/assets/css/tinymce_content.css'],
            autoresize_bottom_margin: 0,
            statusbar: false,
            menubar: false
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            timeFormat: 'hh mm',
            maxDate: new Date(2020, 5, 22),
            startingDay: 1
        };

        $scope.open1 = function () {
            $scope.popup1.opened = true;
        };

        $scope.open2 = function () {
            $scope.popup2.opened = true;
        };

        $scope.format = 'dd/MM/yyyy';
        $scope.altInputFormats = 'dd/MM/yyyy';

        $scope.popup1 = {
            opened: false
        };

        $scope.popup2 = {
            opened: false
        };


        vm.getAllTaskFlows = function () {
            service.getAllTaskFlows().then(function (data) {
                vm.taskFlows = data;
            });
        };
        vm.getAllTaskFlows();

        vm.getPrioritys = function () {
            service.getPrioritys(vm.pageIndex, vm.pageSize).then(function (data) {
                vm.prioritys = data.content;
            });
        }
        vm.getPrioritys();

        vm.getListTaskBy = function () {
            if (vm.flowId == null) {
                vm.flowId = -1;
            }
            if (vm.currentStepIdByFlow == null) {
                vm.currentStepIdByFlow = -1;
            }
            service.getListTaskBy(vm.flowId, vm.currentStepIdByFlow, vm.pageIndex, vm.pageSize).then(function (data) {
                vm.tasks = data.content;
                console.log(vm.tasks);
                vm.bsTableControl.options.data = vm.tasks;
                vm.bsTableControl.options.totalRows = data.totalElements;
            });
        }
        vm.getListTaskBy();

        vm.onTaskSelectedChange = function () {
            vm.pageIndex = 1;
            vm.currentStepIdByFlow = null;
            vm.searchListStepByFlow = [];
            if (vm.flowId != null && vm.flowId > 0) {
                service.getTaskFlowById(vm.flowId).then(function (data) {
                    if (data != null && data.steps != null && data.steps.length > 0) {

                        angular.forEach(data.steps, function (taskStepFlow, key) {
                            if (taskStepFlow.step != null) {
                                vm.searchListStepByFlow.push(taskStepFlow.step);
                            }
                        });
                    }
                });
            }
            vm.getListTaskBy();
        }

        vm.onCurrentStepSelectedChange = function () {
            vm.pageIndex = 1;
            vm.getListTaskBy();
        }

        vm.bsTableControl = {
            options: {
                data: vm.tasks,
                idField: 'id',
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: true,
                showToggle: true,
                pagination: true,
                pageSize: vm.pageSize,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: service.getTableDefinition(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedTasks.push(row);
                    });
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedTasks = rows;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedpositiontitles);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedTasks.splice(index, 1);
                        });
                    }
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedTasks = [];
                    });
                },
                onPageChange: function (index, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = index;
                    vm.getListTaskBy();
                }
            }
        };

        vm.getAllCommentByTaskId = function (id) {
            service.getAllCommentByTaskId(id, vm.pageIndexComment, vm.pageSizeComment).then(function (data) {
                vm.listComment = data.content;
                console.log(vm.listComment);
                vm.bsTableControlComment.options.data = vm.listComment;
                vm.bsTableControlComment.options.totalRows = data.totalElements;
            });
        }

        vm.bsTableControl4Files = {
            options: {
                data: vm.task.attachments,
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
                columns: service.getTableDefinition4Files()
            }
        };

        vm.bsTableControlComment = {
            options: {
                data: vm.comments,
                idField: "id",
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                pagination: true,
                pageSize: vm.pageSizeComment,
                pageList: [5, 10, 25, 50, 100, 200],
                locale: settings.locale,
                sidePagination: 'server',
                columns: service.getTableCommentDefinition(),
                onPageChange: function (index, pageSize) {
                    vm.pageSizeComment = pageSize;
                    vm.pageIndexComment = index;
                    if (vm.task != null && vm.task.id != null && vm.task.id != '') {
                        vm.getAllCommentByTaskId(vm.task.id);
                    }
                }
            }
        };

        vm.bsTableControlChairPerson = {
            options: {
                data: vm.listChairPerson,
                idField: 'id',
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                pagination: true,
                pageSize: vm.pageSize,
                pageList: [5, 10, 25, 50, 100],
                locale: settings.locale,
                sidePagination: 'server',
                columns: service.getTableDefinitionChairPerson(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedTaskOwnerChairman = row;
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.listChairPerson);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedTaskOwnerChairman = {};
                        });
                    }
                },
                onPageChange: function (pageIndex, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = pageIndex;
                    vm.searchTaskOwnersByName();
                }
            }
        };

        vm.bsTableControlOrtherPerson = {
            options: {
                data: vm.listOrtherPerson,
                idField: "id",
                sortable: true,
                striped: true,
                maintainSelected: true,
                clickToSelect: false,
                showColumns: true,
                showToggle: true,
                pagination: true,
                pageSize: vm.pageSize,
                pageList: [5, 10, 25, 50, 100, 200],
                locale: settings.locale,
                sidePagination: 'server',
                columns: service.getTableDefinitionOrtherPerson(),
                onCheck: function (row, $element) {
                    $scope.$apply(function () {
                        vm.selectedOrtherPerson.push(row);
                    });
                },
                onUncheck: function (row, $element) {
                    var index = utils.indexOf(row, vm.selectedOrtherPerson);
                    if (index >= 0) {
                        $scope.$apply(function () {
                            vm.selectedOrtherPerson.splice(index, 1);
                        });
                    }

                    if (vm.deleteListsOrtherPersons == null) {
                        vm.deleteListsOrtherPersons = [];
                    }
                    if (vm.listParticipateProcess != null) {
                        for (var i = 0; i < vm.listParticipateProcess.length; i++) {
                            if (vm.listParticipateProcess[i].taskOwner.id == row.id) {
                                vm.deleteListsOrtherPersons.push(vm.listParticipateProcess[i]);
                            }
                        }
                    }
                },
                onCheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedOrtherPerson = rows;
                    });
                },
                onUncheckAll: function (rows) {
                    $scope.$apply(function () {
                        vm.selectedOrtherPerson = [];

                        if (vm.deleteListsOrtherPersons == null) {
                            vm.deleteListsOrtherPersons = [];
                        }
                        for (var i = 0; i < rows.length; i++) {
                            var participate = {};
                            participate.taskOwner = rows[i];
                            vm.deleteListsOrtherPersons.push(participate);
                        }
                    });
                },
                onPageChange: function (pageIndex, pageSize) {
                    vm.pageSize = pageSize;
                    vm.pageIndex = pageIndex;
                    vm.searchOrtherPerson();
                }
            }
        };

        /*vm.getTaskCommentByTaskId = function getTaskCommentByTaskId(taskId) {
            vm.comments = [];
            vm.bsTableControlComment.options.data = null;
            vm.bsTableControlComment.options.totalRows = null;
            service.getTaskCommentByTaskId(taskId, vm.pageIndex, vm.pageSize).then(function (data) {
                vm.comments = data.content;
                vm.bsTableControlComment.options.data = vm.comments;
                vm.bsTableControlComment.options.totalRows = data.totalElements;
            });
        }*/

        vm.searchTaskOwnersByName = function () {

            service.getListTaskOwnerByRoleCode(vm.taskOwnerRole).then(function (data) {
                if (data != null && data.length > 0) {
                    if (vm.taskOwnerRole == vm.ChairmanRole) {	//Nếu là xử lý chính sẽ lấy ra list dựa theo role code
                        vm.listChairPerson = [];
                        vm.listChairPerson = data;

                        if (vm.listChairPerson != null && vm.listChairPerson.length > 0) {
                            for (var i = 0; i < vm.listChairPerson.length; i++) {
                                if (vm.participateChairman != null && vm.participateChairman.taskOwner != null && vm.listChairPerson[i].id == vm.participateChairman.taskOwner.id) {
                                    vm.listChairPerson[i].state = true;
                                }
                            }
                        }
                        vm.bsTableControlChairPerson.options.data = vm.listChairPerson;
                        vm.bsTableControlChairPerson.options.totalRows = data.totalElements;
                    }
                    else {	//Nếu lầ list người tham gia
                        vm.listOrtherPerson = data;

                        if (vm.listParticipateProcess != null && vm.listParticipateProcess.length > 0) {
                            for (var i = 0; i < vm.listParticipateProcess.length; i++) {
                                if (vm.listParticipateProcess[i].taskOwner != null) {
                                    for (var j = 0; j < vm.listOrtherPerson.length; j++) {
                                        if (vm.listParticipateProcess[i].taskOwner.id == vm.listOrtherPerson[j].id) {
                                            vm.listOrtherPerson[j].state = true;
                                        }
                                    }
                                }

                            }
                        }
                        vm.bsTableControlOrtherPerson.options.data = vm.listOrtherPerson;
                        vm.bsTableControlOrtherPerson.options.totalRows = data.totalElements;
                    }
                }
            });
        }

        vm.showModalChairPerson = function () {
            vm.taskOwnerRole = vm.ChairmanRole;	//set role để tìm task owner theo role (role: xử lý chính - tham gia)
            vm.chairPersonSearchKey = '';
            vm.searchTaskOwnersByName();

            vm.modalListChairPerson = modal.open({
                animation: true,
                backdrop: 'static',
                templateUrl: 'taskOwnerChairmanSelect.html',
                scope: $scope,
                size: 'lg'
            });

            vm.modalListChairPerson.result.then(function (confirm) {
                if (confirm == 'yes') {
                    if (vm.task.participates != null && vm.task.participates.length > 0) {

                        vm.participateChairman = vm.task.participates.find(function (participate) {
                            if (participate.role != null && participate.taskOwner != null) {
                                return participate.role.code == vm.taskOwnerRole && vm.selectedTaskOwnerChairman.id == participate.taskOwner.id;
                            }
                        });
                    }

                    if (vm.participateChairman == null || vm.participateChairman.taskOwner == null) {	//nếu chưa có paticipate của người xử lý chính thì tạo mới
                        vm.participateChairman = {
                            role: {
                                code: vm.ChairmanRole
                            },
                            taskOwner: vm.selectedTaskOwnerChairman,
                            displayName: vm.selectedTaskOwnerChairman.displayName,
                            participateType: 3,
                        };
                    }

                } else {
                    vm.selectedTaskOwnerChairman = null;
                }
            });
            vm.modalListChairPerson.close();
        }

        vm.taskOwnerChairmanRemove = function () {
        	vm.participateChairman = null;
        }

        vm.ortherPersonRemove = function (index) {
            if (vm.listParticipateProcess != null && vm.listParticipateProcess.length > 0) {
                for (var i = 0; i < vm.listParticipateProcess.length; i++) {
                    if (i == index) {
                        vm.listParticipateProcess.splice(i, 1);
                    }
                }
            }
        }

        vm.showModallistPerson = function () {
            vm.taskOwnerRole = vm.ProcessRole;	//set role để tìm task owner theo role (role: xử lý chính - tham gia)
            vm.selectedOrtherPerson = [];
            vm.deleteListsOrtherPersons = [];
            vm.ortherPersonSearchKey = '';

            vm.searchTaskOwnersByName();

            vm.modalListPerson = modal.open({
                animation: true,
                templateUrl: 'taskOwnerProcessSelect.html',
                scope: $scope,
                backdrop: 'static',
                size: 'lg'
            });

            vm.modalListPerson.result
                .then(function (confirm) {
                    if (confirm == 'yes') {

                        if (vm.selectedOrtherPerson != null && vm.selectedOrtherPerson.length > 0) {

                            angular.forEach(vm.selectedOrtherPerson, function (ortherPerson, indexOrtherPerson) {
                                var haveData = false;
                                if (vm.listParticipateProcess != null && vm.listParticipateProcess.length > 0) {

                                    angular.forEach(vm.listParticipateProcess, function (participateProcess, indexParticipateProcess) {
                                        if (participateProcess.taskOwner != null && participateProcess.taskOwner.id == ortherPerson.id) {
                                            haveData = true;
                                        }
                                    });
                                }

                                if (!haveData) {
                                    var participateProcess = {};
                                    participateProcess.taskOwner = {};
                                    participateProcess.taskOwner = ortherPerson;
                                    participateProcess.role = { code: vm.ProcessRole };
                                    participateProcess.participateType = 3,

                                        vm.listParticipateProcess.push(participateProcess);
                                }

                            });
                        }

                        if (vm.deleteListsOrtherPersons != null && vm.deleteListsOrtherPersons.length > 0) {

                            angular.forEach(vm.deleteListsOrtherPersons, function (deleteOrtherPerson, indexDeleteOrtherPerson) {
                                angular.forEach(vm.listParticipateProcess, function (participateProcess, indexParticipateProcess) {
                                    if (participateProcess.taskOwner != null && participateProcess.taskOwner.id == deleteOrtherPerson.taskOwner.id) {
                                        vm.listParticipateProcess.splice(indexParticipateProcess, 1);
                                    }
                                });
                            });
                        }
                    } else {
                        vm.selectedOrtherPerson = [];
                    }
                });
            vm.modalListPerson.close();
        }


        vm.addComment = function () {
            vm.taskComment = {};

            vm.modalAddComment = modal.open({
                animation: true,
                templateUrl: 'addComment.html',
                scope: $scope,
                backdrop: 'static',
                size: 'lg'
            });

        }

        $scope.editComment = function (id) {
            if (id == null || id == '') {
                toastr.error('Có lỗi xảy ra. Vui lòng thử lại.', 'Thông báo');
                return;
            }

            service.getCommentById(id).then(function (data) {
                vm.taskComment = data;

                vm.modalAddComment = modal.open({
                    animation: true,
                    templateUrl: 'addComment.html',
                    scope: $scope,
                    backdrop: 'static',
                    size: 'lg'
                });
            });
        }

        vm.cancelAddComment = function () {
            // Clear data
            vm.taskComment = {};

            if (vm.modalAddComment != null) {
                vm.modalAddComment.close();
            }
        }

        vm.saveCommnet = function () {
            if (vm.taskComment == null) {
                vm.taskComment = {};
            }
            if (vm.taskComment.participate == null || vm.taskComment.participate.id == null) {
                toastr.error('Chưa chọn người đóng góp ý kiến.', 'Thông báo');
                return;
            }
            if (vm.taskComment.comment == null || vm.taskComment.comment == '') {
                toastr.error('Bạn chưa nhập nội dung ý kiến đóng góp.', 'Thông báo');
                return;
            }

            service.saveComment(vm.taskComment, function success() {
                // Notify
                toastr.info('Cập nhật ý kiến thành công.', 'Thông báo');

                if (vm.task != null && vm.task.id != null && vm.task.id != '') {
                    vm.getAllCommentByTaskId(vm.task.id);
                }

                // Clear data
                vm.taskComment = {};

                if (vm.modalAddComment != null) {
                    vm.modalAddComment.close();
                }
            }, function failure() {
                toastr.error('Có lỗi xảy ra khi đóng góp ý kiến.', 'Lỗi');
            });
        }

        //delete Comment
        $scope.deleteComment = function (id) {
            if (id == null || id == '') {
                toastr.error('Có lỗi xảy ra. Vui lòng thử lại.', 'Thông báo');
                return;
            }

            service.deleteCommentById(id, function success() {
                // Notify
                toastr.info('Xóa ý kiến thành công.', 'Thông báo');

                if (vm.task != null && vm.task.id != null && vm.task.id != '') {
                    vm.getAllCommentByTaskId(vm.task.id);
                }

            }, function failure() {
                toastr.error('Có lỗi xảy ra khi xóa ý kiến.', 'Lỗi');
            });

        }

        //delete Task
        $scope.deleteTask = function (id) {
            if (id == null || id == '') {
                toastr.error('Có lỗi xảy ra. Vui lòng thử lại.', 'Thông báo');
                return;
            }

            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'confirm_delete_modal.html',
                scope: $scope,
                size: 'md'
            });

            modalInstance.result.then(function (confirm) {
                if (confirm == 'yes') {
                    service.deleteTaskById(id, function success() {
                        // Notify
                        toastr.info('Xóa công việc thành công.', 'Thông báo');

                        vm.getListTaskBy();

                    }, function failure() {
                        toastr.error('Có lỗi xảy ra khi xóa công việc.', 'Lỗi');
                    });
                }
            }, function () {
                console.log('Modal dismissed at: ' + new Date());
            });
        }
        

        function setNewValue() {
            vm.task = {};
            vm.project = {};
            vm.chairman = {};
            vm.participateChairman = null;
            vm.listParticipateProcess = [];
            vm.listStepByFlow = [];
            vm.flow = {};
            vm.selectedTaskOwnerChairman = null;
            vm.listOrtherPerson = [];
            return true
        }

        /**
         * New event account
         */
        vm.newTask = function () {
            if (setNewValue()) {
                vm.isNew = true;
                $scope.isView = false;
                vm.task.isNew = true;
                //tạm thời mặc định là tạo mới công việc hằng ngày
                var daiLyWorksFlow = vm.hardCodeWorkFlowCode.filter(flow => flow.code == vm.DaiLyWorksFlowCode);

                var daiLyWorksFlow = vm.hardCodeWorkFlowCode.find(function (flow) {
                    return flow.code == vm.DaiLyWorksFlowCode;
                });

                if (daiLyWorksFlow != null && daiLyWorksFlow.code != null) {
                    service.getTaskFlowByCode(daiLyWorksFlow.code).then(function (data) {
                        if (data != null) {
                            vm.task.flow = data;
                            if (data.steps != null) {
                                vm.listStepByFlow = [];
                                angular.forEach(data.steps, function (value, key) {
                                    if (value.step != null && value.step.id != null && value.step.id != '') {
                                        vm.listStepByFlow.push(value.step);
                                    }
                                });
                            }
                            if (vm.listStepByFlow != null && vm.listStepByFlow.length > 0) {
                                vm.task.currentStep = vm.listStepByFlow[0];
                                vm.modalInstanceEditTask = modal.open({
                                    animation: true,
                                    backdrop: 'static',
                                    templateUrl: daiLyWorksFlow.templateUrl,
                                    scope: $scope,
                                    size: 'lg'
                                });
                            }
                            else {
                                toastr.error('Không lấy được dữ liệu trang thái công việc. Vui lòng thử lại.', 'Lỗi');
                            }
                        }
                        else {
                            toastr.error('Có lỗi xảy ra lấy dữ liệu quy trình công việc.', 'Lỗi');
                        }
                    });
                }
            }
        };
        
        $scope.viewTask = function (taskId, flowCode) {
            if (taskId != null && taskId > 0 && flowCode != null && flowCode != '') {
                if (setNewValue()) {
                    $scope.isView = true;
                    vm.isNew = false;
                    vm.task.isNew = false;
                    
                    var flow = vm.hardCodeWorkFlowCode.find(x => x.code == flowCode);

                    if (flow != null) {
                        editByFlow(taskId, flow);	//edit theo flow
                    }
                    else {
                        toastr.error('Không tìm thấy quy trình công việc. Vui lòng kiểm tra và thử lại.', 'Lỗi');
                    }
                }
            }
            else {
                toastr.error('Có lỗi xảy ra lấy dữ liệu. Vui lòng thử lại', 'Lỗi');
            }
        };

        $scope.viewDocument = function (index) {
            if (vm.task != null && vm.task.attachments != null) {
                for (var i = 0; i < vm.task.attachments.length; i++) {
                    if (i == index) {
                        var  attachment= vm.task.attachments[i];
                        service.getFileById(attachment.file.id).success(function (data) {
                        	 var file = new Blob([data], {type: attachment.file.contentType});
	                          if(attachment.file.contentType == 'application/pdf'){
	                        	 var fileURL = URL.createObjectURL(file);
	                             window.open(fileURL);
	                          }else {
	                        	  toastr.warning('Không thể xem tệp tin. Hãy tải xuống', 'Thông báo');
	                          }
                        });
                    }
                }
            }
        }
        
        $scope.editTask = function (taskId, flowCode) {
            if (taskId != null && taskId > 0 && flowCode != null && flowCode != '') {
                if (setNewValue()) {
                    $scope.isView = false;
                    vm.isNew = false;
                    vm.task.isNew = false;
                    
                    var flow = vm.hardCodeWorkFlowCode.find(x => x.code == flowCode);

                    if (flow != null) {
                        editByFlow(taskId, flow);	//edit theo flow
                    }
                    else {
                        toastr.error('Không tìm thấy quy trình công việc. Vui lòng kiểm tra và thử lại.', 'Lỗi');
                    }
                }
            }
            else {
                toastr.error('Có lỗi xảy ra lấy dữ liệu. Vui lòng thử lại', 'Lỗi');
            }
        };

        function editByFlow(taskId, flow) {

            if (flow.code == vm.WorkPlanFlowCode) {
                loadDataByWorkPlan();
            }
            else if (flow.code == vm.ProjectFlowCode) {
                loadDataByProject();
            }
            else if (flow.code == vm.DaiLyWorksFlowCode) {
                service.getTask(taskId).then(function (data) {
                    if (data != null) {
                        vm.task = data;
                        console.log(vm.task);
                        loadDataByDaiLyWorks();

                        vm.modalInstanceEditTask = modal
                            .open({
                                animation: true,
                                templateUrl: flow.templateUrl,
                                scope: $scope,
                                backdrop: 'static',
                                size: 'lg'
                            });
                    } else {
                        toastr.error('Có lỗi xảy ra lấy dữ liệu công việc. Vui lòng thử lại', 'Lỗi');
                    }
                });
            }
            else if (flow.code == vm.LetterInFlowCode) {
                service.getLetterInDocumentByTaskId(taskId).then(function (data) {
                    if (data != null) {
                        loadDataByLetterIn(data, flow);
                    } else {
                        toastr.error('Có lỗi xảy ra lấy dữ liệu công việc. Vui lòng thử lại', 'Lỗi');
                    }
                });
            }
            else if (flow.code == vm.LetterOutFlowCode) {
                service.getLetterOutDocumentByTaskId(taskId).then(function (data) {
                    if (data != null) {
                        loadDataByLetterOut(data, flow);
                    } else {
                        toastr.error('Có lỗi xảy ra lấy dữ liệu công việc. Vui lòng thử lại', 'Lỗi');
                    }
                });
            }
        }

        function loadDataByDaiLyWorks() {
            if (vm.task != null && vm.task.id != null && vm.task.id != '') {
                vm.getAllCommentByTaskId(vm.task.id);
                /*vm.getTaskCommentByTaskId(vm.task.id);*/
            }

            if (vm.task.attachments != null) {
                vm.bsTableControl4Files.options.data = vm.task.attachments;
            }
            if (vm.task.participates != null) {
                vm.listParticipatesComment = vm.task.participates;
                for (var i in vm.task.participates) {
                    var p = vm.task.participates[i];
                    if (p.role != null && p.role.code == vm.ChairmanRole) {
                        vm.participateChairman = p;
                    } else {
                        vm.listParticipateProcess.push(p);
                    }
                }
            }

            if (vm.task.steps != null) {
                vm.listStepByFlow = [];
                angular.forEach(vm.task.steps, function (value, key) {
                    if (value.step != null && value.step.id != null && value.step.id != '') {
                        vm.listStepByFlow.push(value.step);
                    }
                });
            }
        }

        function loadDataByLetterIn(letterInDocument, flow) {
            if (letterInDocument) {
                console.log("in");
                console.log(letterInDocument);
                if ($scope.isView) {	//Nếu là ấn nút xem thì link đến trang view không thì sẽ vào trang edit
                    var url = $state.href('application.forward_letter_in',{state:'view', letter_id:letterInDocument.id});
                    window.open(url, '_blank');
				}
                else {
                    if (letterInDocument.task != null && letterInDocument.task.currentStep != null) {
                        if (letterInDocument.task.currentStep.code == flow.step1.code) {	//nếu đang ở trạng thái Văn bản mới vào sổ sẽ link đến trang chuyển phân luồng văn bản
                            //check quyền theo bước
                            if (letterInDocument.hasClerkRole) {
                                var url = $state.href('application.forward_letter_in', { letter_id: letterInDocument.id, state: 'forward' });
                                window.open(url, '_blank');
                            } else {
                                toastr.warning('Bạn không có quyền văn thư để chuyển phân luồng văn bản này.', 'Thông báo');
                            }
                        }
                        else if (letterInDocument.task.currentStep.code == flow.step2.code) {	//nếu đang ở trạng thái phân luồng sẽ link đến trang phân luồng văn bản
                            //check quyền theo bước
                            if (letterInDocument.hasFowardRole) {
                                var url = $state.href('application.transfer_process_letter_in', { letter_id: letterInDocument.id });
                                window.open(url, '_blank');
                            } else {
                                toastr.warning('Bạn không có quyền phân luồng để phân luồng văn bản này.', 'Thông báo');
                            }
                        }
                        else if (letterInDocument.task.currentStep.code == flow.step3.code) {	//nếu đang ở trạng thái giao xử lý sẽ link đến trang giao xử lý văn bản
                            //check quyền theo bước
                            if (letterInDocument.hasAssignerRole) {
                                var url = $state.href('application.assigner_letter_in', { letter_id: letterInDocument.id });
                                window.open(url, '_blank');
                            } else {
                                toastr.warning('Bạn không có quyền giao xử lý để giao xử lý văn bản này.', 'Thông báo');
                            }
                        }
                        else if (letterInDocument.task.currentStep.code == flow.step4.code) {	//nếu đang ở trạng thái đang xử lý sẽ link đến trang hoàn thành văn bản lý văn bản
                            //check quyền theo bước
                            if (letterInDocument.hasAssignerRole || letterInDocument.hasChairmanRole) {
                                var url = $state.href('application.forward_letter_in', { state: 'view', letter_id: letterInDocument.id });
                                window.open(url, '_blank');
                            } else {
                                toastr.warning('Bạn không có quyền giao xử lý hoặc xử lý chính để hoàn thành văn bản này.', 'Thông báo');
                            }
                        }
                        else if (letterInDocument.task.currentStep.code == flow.step5.code) {	//nếu văn bản đã hoàn thành thì thông báo
                            toastr.warning('Văn bản đã hoàn thành. Không thể thao tác.', 'Thông báo');
                        }
                    }
				}
                
            }
        }

        function loadDataByLetterOut(letterOutDocument, flow) {
            if (letterOutDocument) {
                console.log("out");
                console.log(letterOutDocument);
                if ($scope.isView) {	//Nếu là ấn nút xem thì link đến trang view không thì sẽ vào trang edit
                    var url = $state.href('application.letter_out_view',{letter_id:letterOutDocument.id, state: 'view'});
                    window.open(url, '_blank');
				}
                else {
                    if (letterOutDocument.task != null && letterOutDocument.task.currentStep != null) {
                        if (letterOutDocument.task.currentStep.code == flow.step1.code) {	//nếu đang ở trạng thái Văn bản mới vào sổ sẽ link đến trang văn bản chờ chuyển đi
                            //check quyền theo bước
                            if (letterOutDocument.hasClerkRole) {
                                var url = $state.href('application.letter_out_edit', { letter_id: letterOutDocument.id });
                                window.open(url, '_blank');
                            } else {
                                toastr.warning('Bạn không có quyền văn thư để chuyển văn bản đi.', 'Thông báo');
                            }
                        }
                        else {	//Nếu đã chuyển đi tạm thời chưa cho xử lý tiếp
                            toastr.warning('Văn bản đã chuyển đi. Không thể thao tác.', 'Thông báo');
                        }
                    }
				}

            }
        }

        vm.saveDaiLyWorks = function () {
            if (checkValidSaveDaiLyWorks()) {

                vm.task.participates = [];
                vm.task.participates.push(vm.participateChairman);	//thêm xử lý chính vào list participates

                angular.forEach(vm.listParticipateProcess, function (value, key) {
                    vm.task.participates.push(value);	//thêm tham gia vào list participates
                });

                service.saveDaiLyWorks(vm.task, function success() {
                    // Refresh list
                    vm.getListTaskBy();
                    // Notify
                    if (vm.isNew) {
                        toastr.info('Bạn đã thêm công việc thành công.', 'Thông báo');
                    }
                    else {
                        toastr.info('Bạn đã cập nhật công việc thành công.', 'Thông báo');
                    }
                    if (vm.modalInstanceEditTask != null) {
                        vm.modalInstanceEditTask.close();
                    }
                }, function failure() {
                    toastr.error('Có lỗi xảy ra. vui lòng thử lại', 'Lỗi');
                });
            }
        }

        function checkValidSaveDaiLyWorks() {
            if (vm.task == null) {
                toastr.error('Có lỗi xảy ra. Vui lòng tải lại trang.', 'Lỗi');
                return false;
            }
            if (vm.task.name == null || vm.task.name.trim() == '') {
                toastr.warning('Vui lòng nhập tên công việc.', 'Lỗi');
                return false;
            }
            if (!vm.task.dateStart) {
                toastr.warning('Vui lòng nhập thời gian bắt đầu.', 'Lỗi');
                return;
            }
            if (vm.task.currentStep == null || vm.task.currentStep.id == null || vm.task.currentStep.id == '') {
                toastr.warning('Vui lòng chọn trạng thái.', 'Lỗi');
                return;
            }
            if (!vm.task.dateDue) {
                toastr.warning('Vui lòng nhập thời gian kết thúc.', 'Lỗi');
                return;
            }
            if (vm.participateChairman == null || vm.participateChairman.taskOwner == null) {
                toastr.warning('Vui lòng người, đơn vị/ phòng ban xử lý chính.', 'Lỗi');
                return false;
            }
            if (vm.listParticipateProcess == null || vm.listParticipateProcess.length <= 0) {
                toastr.warning('Vui lòng chọn người, đơn vị/ phòng ban tham gia.', 'Lỗi');
                return false;
            }
            return true;
        }

        // // Upload file
        vm.uploadedFile = null;
        vm.errorFile = null;
        vm.uploadFiles = function (file, errFiles) {
            vm.uploadedFile = file;
            if (vm.uploadedFile != null) {
                Uploader
                    .upload(
                        {
                            url: settings.api.baseUrl
                                + settings.api.apiPrefix
                                + 'taskman/project/uploadattachment',
                            method: 'POST',
                            data: {
                                uploadfile: vm.uploadedFile
                            }
                        })
                    .then(
                        function (successResponse) {

                            var attachment = successResponse.data;
                            if (vm.uploadedFile
                                && (!vm.errorFile || (vm.errorFile && vm.errorFile.$error == null))) {
                                if (vm.task != null
                                    && vm.task.attachments == null) {
                                    vm.task.attachments = [];
                                }
                                vm.task.attachments.push(
                                    // { title: attachment.file.name,
                                    // contentLength:
                                    // attachment.file.contentSize,
                                    // contentType: fileDesc.contentType }
                                    attachment);
                                vm.bsTableControl4Files.options.data = vm.task.attachments;
                            }
                        },
                        function (errorResponse) {
                            toastr.error('Error submitting data...',
                                'Error');
                        },
                        function (evt) {
                            console.log('progress: '
                                + parseInt(100.0 * evt.loaded
                                    / evt.total) + '%');
                        });
            }
        };

        $scope.downloadDocument = function (index) {
            if (vm.task != null && vm.task.attachments != null) {
                for (var i = 0; i < vm.task.attachments.length; i++) {
                    if (i == index) {
                        var attachment = vm.task.attachments[i];
                        service.getFileById(attachment.file.id).success(function (data) {
                            var file = new Blob([data], { type: attachment.file.contentType });
                            FileSaver.saveAs(file, attachment.file.name);
                        });;
                    }
                }
            }
        }

        //delete file
        $scope.deleteDocument = function (index) {
            if (vm.task != null && vm.task.attachments != null) {
                for (var i = 0; i < vm.task.attachments.length; i++) {
                    if (i == index) {
                        vm.task.attachments.splice(i, 1);
                    }
                }
            }
        }


    }
})();