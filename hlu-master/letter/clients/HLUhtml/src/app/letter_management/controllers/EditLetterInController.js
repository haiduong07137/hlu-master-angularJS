/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function() {
    'use strict';

    angular.module('Hrm.LetterManagement').controller('EditLetterInController', EditLetterInController);

    EditLetterInController.$inject = [
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
        'DocumentTypeService',
        'DocumentFieldService',
        'DocumentPriorityService',
        'DocumentSecurityLevelService',
        '$window',
        'letterIndocumentFactory',
        'FileSaver',
        '$sce'
    ];

    function EditLetterInController($rootScope, $scope, toastr, $timeout, settings, utils,
        modal, service, Uploader, $state, typeService, fieldService, priorityService, securityService, $window, letterIndocumentFactory, FileSaver, $sce) {
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

        vm.isNew = true;

        vm.getLetterInDocumentById = getLetterInDocumentById;
        var letterId = $state.params['letter_id'];
        vm.letterId = $state.params['letter_id'];
        var state = $state.params['state'];
        if (letterId != null) {
            vm.isNew = false;
        }
        vm.isView = (state == 'forward') ? false : true;
        vm.isShowDetail = false;
        $scope.checked = true;
        $scope.check = true;
        vm.letterIn = {};
        vm.letterLines = [
            { id: 1, name: "Bưu điện" },
            { id: 2, name: "Chuyển trực tiếp" },
            { id: 3, name: "Fax" },
            { id: 4, name: "Email" }
        ];

        vm.saveLetterTypes = [
            { id: 1, name: "Bản gốc" },
            { id: 2, name: "Bản sao chép" },
            { id: 3, name: "Bản mềm" }
        ];
        vm.forwarderId = null;
        vm.showDetail = function() {
            vm.isShowDetail = !vm.isShowDetail;
        }
        vm.showBtnComplete = false;
        vm.showBtnEdit = false;
        // TREE ORGANIZATION//
        vm.treeData = [];
        vm.organizationDetail = {};
        $scope.treeInstance = null;
        // $scope.treeConfig.version = 0;
        $scope.treeConfig = {
            core: {
                error: function(error) {
                    $log.error('treeCtrl: error from js tree - ' + angular.toJson(error));
                },
                check_callback: true
            },
            plugins: ['types', 'state', 'search']
                // version: 1
        };

        $scope.readyCB = function() {
            // getOrganizationTree();
        }
        $scope.selectNode = function(node, selected, event) {
            vm.getOrganizationDetailById(selected.node.id);
        }

        $scope.search = '';

        $scope.applySearch = function() {
            // getOrganizationTree();
            var to = false;
            if (to) {
                clearTimeout(to);
            }
            to = setTimeout(function() {
                if ($scope.treeInstance) {
                    $scope.treeInstance.jstree(true).search($scope.search);
                }
            }, 250);
        };

        function getOrganizationTree() {
            service.getOrganizationTree().then(function(data) {
                vm.treeData = data;
                vm.organizations = data;
                $scope.treeConfig.version = 1;
            });
        }
        vm.reloadTree = function() {
            $scope.treeConfig.version++;
        };

        vm.selectOrganization = function() {
            getOrganizationTree();
            var modalInstance = modal.open({
                animation: true,
                templateUrl: 'select_organization_modal.html',
                scope: $scope,
                backdrop: 'static',
                size: 'lg'
            });

            modalInstance.result.then(function(confirm) {
                if (confirm == 'yes') {
                    vm.letterIn.issueOrgan = vm.organizationDetail;
                }
            }, function() {
                console.log('Modal dismissed at: ' + new Date());
            });
        };

        vm.newLetterIn = function() {
            if (vm.checkValid()) {
                service.newLetterIn(vm.letterIn, -1, vm.forwarderId, function successCallback() {
                    $state.go('application.new_letter_in');
                }, function errorCallback() {

                });
            }
        };
        vm.newLetterInAndView = function() {
            if (vm.checkValid()) {
                service.newLetterIn(vm.letterIn, -1, vm.forwarderId, function successCallback(data) {
                    $state.go('application.edit_letter_in', { letter_id: data.id });
                }, function errorCallback() {

                });
            }
        }

        vm.checkValid = function() {
            // let regrex =
            // /^([a-zA-Z]|[0-9])([a-zA-Z]|[0-9]|\/|\-)([a-zA-Z]|[0-9])+/gi;
            // console.log(regrex.exec(vm.letterIn.docOriginCode));
            // if(regrex.exec(vm.letterIn.docOriginCode)){
            // toastr.warning('Vui lòng nhập lại số ký hiệu văn bản.', 'Thông
            // báo');
            // return false;
            // }
            if (vm.letterIn == null) {
                toastr.error('Có lỗi xảy ra vui lòng tải lại trang.', 'Thông báo');
                return false;
            }
            if (vm.letterIn.title == null || vm.letterIn.title == '') {
                toastr.warning('Vui lòng nhập tiêu đề văn bản.', 'Thông báo');
                return false;
            }
            if (vm.letterIn.docOriginCode == null || vm.letterIn.docOriginCode == '') {
                toastr.warning('Vui lòng nhập số ký hiệu văn bản.', 'Thông báo');
                return false;
            }

            // if (vm.letterIn.docCode == null || vm.letterIn.docCode == '') {
            // toastr.warning('Vui lòng nhập số nội bộ.', 'Thông báo');
            // return false;
            // }
            if (vm.letterIn.briefNote && vm.letterIn.briefNote.length > 1000) {
                toastr.warning('Vui lòng nhập trích yếu không quá 1000 ký tự.', 'Thông báo');
                return false;
            }
            if (vm.letterIn.briefNote == null || vm.letterIn.briefNote == '') {
                toastr.warning('Vui lòng nhập trích yếu.', 'Thông báo');
                return false;
            }
            // if(vm.letterIn.description == null || vm.letterIn.description ==
            // ''){
            // toastr.warning('Vui lòng nhập ghi chú.', 'Thông báo');
            // return false;
            // }
            if (vm.letterIn.description && vm.letterIn.description.length > 1000) {
                toastr.warning('Vui lòng nhập ghi chú không quá 1000 ký tự.', 'Thông báo');
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
            // if (vm.letterIn.documentOrderNoByBook == null ||
            // vm.letterIn.documentOrderNoByBook == '') {
            // toastr.warning('Vui lòng nhập số đến theo sổ.', 'Thông báo');
            // return false;
            // }
            if (vm.letterIn.letterDocumentType == null || vm.letterIn.letterDocumentType.id == null || vm.letterIn.letterDocumentType.id == '') {
                toastr.warning('Vui lòng chọn loại văn bản.', 'Thông báo');
                return false;
            }
            if (vm.letterIn.deliveredDate == null || vm.letterIn.deliveredDate == '') {
                console.log("vm.letterIn.deliveredDate");
                toastr.warning('Vui lòng nhập ngày đến.', 'Thông báo');
                return false;
            }
            if (vm.letterIn.registeredDate == null || vm.letterIn.registeredDate == '') {
                toastr.warning('Vui lòng nhập ngày vào sổ.', 'Thông báo');
                return false;
            }
            if (vm.letterIn.attachments == null || vm.letterIn.attachments.length <= 0) {
                toastr.warning('Vui lòng chọn và tải file đính kèm.', 'Thông báo');
                return false;
            }
            var dateNow = new Date();
            if (vm.letterIn.deliveredDate != null && vm.letterIn.deliveredDate != '') {
                if (angular.isDate(vm.letterIn.deliveredDate)) {
                    if (vm.letterIn.deliveredDate > vm.letterIn.registeredDate) {
                        toastr.warning('Ngày đến không được lớn hơn ngày vào sổ.', 'Thông báo');
                        return false;
                    }
                }
            }
            if (vm.letterIn.issuedDate != null && vm.letterIn.issuedDate != '') {
                if (angular.isDate(vm.letterIn.issuedDate)) {
                    if (vm.letterIn.issuedDate > dateNow) {
                        toastr.warning('Ngày ban hành không được lớn hơn ngày hiện tại.', 'Thông báo');
                        return false;
                    }
                }
            }
            if (vm.isNew == true) {
                if (vm.letterIn.expiredDate != null && vm.letterIn.expiredDate != '') {
                    if (angular.isDate(vm.letterIn.expiredDate)) {
                        if (vm.letterIn.expiredDate < dateNow.getDate()) {
                            toastr.warning('Hạn trả lời không được nhỏ hơn ngày hiện tại.', 'Thông báo');
                            return false;
                        }
                    }
                }
            }
            if (vm.letterIn.officialDate != null && vm.letterIn.officialDate != '' && vm.letterIn.issuedDate != null && vm.letterIn.issuedDate != '') {
                if (angular.isDate(vm.letterIn.officialDate)) {
                    if (vm.letterIn.officialDate < vm.letterIn.issuedDate) {
                        toastr.warning('Ngày hiệu lực không được nhỏ hơn ngày ban hành.', 'Thông báo');
                        return false;
                    }
                }
            }
            if (vm.letterIn.chiefOfStaff != null && vm.letterIn.chiefOfStaff != '' && vm.forwarderId == null) {
                toastr.warning('Vui lòng chọn người phân luồng.', 'Thông báo');
                return false;
            }
            //        	if (vm.letterIn.officialDate != null && vm.letterIn.officialDate != '') {
            //				if (angular.isDate(vm.letterIn.officialDate)) {
            //					if (vm.letterIn.officialDate > dateNow) {
            //		                toastr.warning('Ngày hiệu lực không được lớn hơn ngày hiện tại.', 'Thông báo');
            //		                return false;
            //					}
            //				}
            //			}
            /*
             * if (vm.letterIn.issuedDate == null || vm.letterIn.issuedDate ==
             * '') { toastr.warning('Vui lòng nhập ngày ban hành.', 'Thông
             * báo'); return false; } if (vm.letterIn.officialDate == null ||
             * vm.letterIn.officialDate == '') { toastr.warning('Vui lòng nhập
             * ngày hiệu lực.', 'Thông báo'); return false; }
             */
            /*
             * if (vm.letterIn.expiredDate == null || vm.letterIn.expiredDate ==
             * '') { toastr.warning('Vui lòng nhập hạn trả lời.', 'Thông báo');
             * return false; }
             */
            /*
             * if (vm.letterIn.letterDocField == null ||
             * vm.letterIn.letterDocField.id == null ||
             * vm.letterIn.letterDocField.id == '') { toastr.warning('Vui lòng
             * chọn lĩnh vực.', 'Thông báo'); return false; }
             */
            if (vm.letterIn.isOtherIssueOrgan) {
                if (vm.letterIn.otherIssueOrgan == null || vm.letterIn.otherIssueOrgan == '') {
                    toastr.warning('Vui lòng nhập cơ quan ban hành .', 'Thông báo');
                    return false;
                }

            } else {
                if (vm.letterIn.issueOrgan == null || vm.letterIn.issueOrgan.id == null || vm.letterIn.issueOrgan.id == '') {
                    toastr.warning('Vui lòng chọn cơ quan ban hành.', 'Thông báo');
                    return false;
                }
            }

            if (vm.isForward) {}

            return true;
        }

        vm.getOrganizationDetailById = function(organizationId) {
            service.getOrganization(organizationId).then(function(data) {
                vm.organizationDetail = data;
            });
        };

        vm.docBookGroups = [];
        service.getListDocBookGroup(1, 10000).then(function(data) {
            vm.docBookGroups = data.content;
        });


        /*
         * service.getListDocBook(1,10000).then(function (data) { vm.docBooks =
         * data.content; console.log(vm.docBooks); });
         */

        vm.docBooks = [];
        vm.docBookGroups = [];
        service.getListDocBookGroup(1, 10000).then(function(data) {
            vm.docBookGroups = data.content;
        });

        vm.letterDocBookGroupSelected = function() {
            if (vm.letterIn != null) {
                vm.letterIn.letterDocBook = null;

                if (vm.letterIn.letterDocBookGroup != null && vm.letterIn.letterDocBookGroup.id != null && vm.letterIn.letterDocBookGroup.id != '') {
                    service.getListDocBookByGroupId(vm.letterIn.letterDocBookGroup.id).then(function(data) {
                        vm.docBooks = data;
                    });
                } else {
                    vm.docBooks = [];
                }
            }
        };

        vm.documentTypes = [];
        typeService.getDocumentTypes(1, 10000).then(function(data) {
            vm.documentTypes = data.content;
        });


        vm.isView = false;
        vm.uploadedFile = null;
        vm.errorFile = null;
        vm.uploadFiles = function(file, errFiles) {
            if (file != null) {
                vm.uploadedFile = file;
                if (vm.uploadedFile != null) {
                    Uploader.upload({
                        url: settings.api.baseUrl + settings.api.apiPrefix + 'letter/indocument/uploadattachment',
                        method: 'POST',
                        data: { uploadfile: vm.uploadedFile }
                    }).then(function(successResponse) {

                        var attachment = successResponse.data;
                        if (vm.uploadedFile && (!vm.errorFile || (vm.errorFile && vm.errorFile.$error == null))) {
                            if (vm.letterIn != null && vm.letterIn.attachments == null) {
                                vm.letterIn.attachments = [];
                            }
                            vm.letterIn.attachments.push(
                                // { title: attachment.file.name, contentLength:
                                // attachment.file.contentSize, contentType:
                                // fileDesc.contentType }
                                attachment
                            );
                            vm.bsTableControl4Files.options.data = vm.letterIn.attachments;
                        }
                    }, function(errorResponse) {
                        toastr.error('Error submitting data...', 'Error');
                    }, function(evt) {
                        console.log('progress: ' + parseInt(100.0 * evt.loaded / evt.total) + '%');
                    });
                }
            }
        };

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

        vm.task = {};
        vm.forwarder = {}; // sau thì cần phân lại theu user đăng nhập
        vm.assigner = {};

        function getLetterInDocumentById(id) {
            service.getLetterInDocumentById(id).then(function(data) {
                vm.letterIn = data;
                console.log(vm.letterIn);
                /*if(vm.letterIn.task.participates.length >= 4){
                    vm.showBtnComplete = true;
                }*/
                /*if(vm.letterIn.task && vm.letterIn.task.currentStep && vm.letterIn.task.currentStep.code == "LetterInCodeStep5"){
                    vm.showBtnComplete = false;
                }*/
                if (vm.letterIn.task.participates.length < 3) {
                    vm.showBtnEdit = true;
                }
                vm.forwarder = data.task.participates[1];
                vm.bsTableControl4Files.options.data = vm.letterIn.attachments;
                vm.bsTableControl4FilesProcess.options.data = vm.letterIn.attachments;
                if (data && data.task && data.task && data.task.participates && data.task.participates.length > 0) {
                    vm.assigner = data.task.participates.find(function(element) {
                        return element.role.code === "AssignerRole";
                    });
                    if (vm.assigner && vm.assigner.role.code !== "AssignerRole") {
                        vm.assigner = {};
                    }
                }
                if (vm.letterIn.attachments != null && vm.letterIn.attachments.length > 0) {
                    vm.bsTableControl4FilesProcess.options.data = vm.letterIn.attachments;
                }
                if (vm.letterIn.attachments != null && vm.letterIn.attachments.length > 0) {
                    vm.bsTableControl4Files.options.data = vm.letterIn.attachments;
                }
                if (vm.letterIn && vm.letterIn.task && vm.letterIn.task && vm.letterIn.task.participates) {
                    const participate = vm.letterIn.task.participates.find((el) => {
                        if (el.role && el.role.code === "FowardRole") {
                            return true;
                        }
                        return false;
                    });
                    if (participate && participate.taskOwner) {
                        vm.forwarderId = participate.taskOwner.id;
                    }
                }
            });
        }

        vm.checkShowBtnComplete = function() {
            if (vm.letterIn != null && vm.letterIn.task != null && vm.letterIn.task.currentStep != null && vm.letterIn.task.currentStep.code == "LetterInCodeStep3") {
                if (vm.letterIn.hasFinishPermission) {
                    return true;
                }
            }
            return false;
        }

        vm.checkShowBtnAddAssistant = function() {
            if (vm.letterIn != null && vm.letterIn.task != null && vm.letterIn.task.currentStep != null && vm.letterIn.task.currentStep.code == "LetterInCodeStep3") {
                if ((vm.letterIn.hasProcessRole || vm.letterIn.hasChairmanRole) && $rootScope.hasManagerRole) {
                    return true;
                }
            }
            return false;
        }

        vm.checkShowBtnReportComplete = function() {
            if (vm.letterIn != null && vm.letterIn.task != null && vm.letterIn.task.currentStep != null && vm.letterIn.task.currentStep.code == "LetterInCodeStep3") {
                if (!vm.letterIn.hasChairmanRole) {
                    return true;
                }
            }
            return false;
        }

        vm.forwardLetterStepTwo = function(letterId) {
            $state.go('application.transfer_process_letter_in', { letter_id: letterId })
        }

        vm.assignLetter = function(letterId) {
            $state.go('application.assigner_letter_in', { letter_id: letterId });
        }

        vm.checkShowBtnForword = function() {
            if (vm.letterIn != null && vm.letterIn.task != null && vm.letterIn.task.currentStep != null && vm.letterIn.task.currentStep.code == "LetterInCodeStep2") {
                if (vm.letterIn.hasFowardRole || vm.letterIn.hasChiefOfStaffRole) {
                    return true;
                }
            }
            return false;
        }

        vm.checkShowBtnAssign = function() {
            if (vm.letterIn != null && vm.letterIn.task != null && vm.letterIn.task.currentStep != null && vm.letterIn.task.currentStep.code == "LetterInCodeStep3") {
                if (vm.letterIn.hasAssignerRole) {
                    return true;
                }
            }
            return false;
        }

        vm.documentFields = [];
        fieldService.getDocumentFields(1, 10000).then(function(data) {
            vm.documentFields = data.content;
        });

        vm.documentPriorities = [];
        priorityService.getDocumentPriorities(1, 10000).then(function(data) {
            vm.documentPriorities = data.content;
        });

        vm.documentTypes = [];
        typeService.getDocumentTypes(1, 10000).then(function(data) {
            vm.documentTypes = data.content;
        });

        vm.documentSecurityLevels = [];
        securityService.getDocumentSecurityLevels(1, 10000).then(function(data) {
            vm.documentSecurityLevels = data.content;
        });

        vm.taskOwnersForwarders = [];
        service.getListTaskOwnerByRoleCode('FowardRole').then(function(data) {
            vm.taskOwnersForwarders = data.map(function(element) {
                if (element.person) {
                    element.displayName = element.person.displayName;
                }
                return element;
            });
            //            if(vm.taskOwnersForwarders.length == 1){
            //                vm.forwarderId = vm.taskOwnersForwarders[0].id;
            //            }
        });

        vm.taskOwnersAssigners = [];
        service.getListTaskOwnerByRoleCode('AssignerRole').then(function(data) {
            vm.taskOwnersAssigners = data;
        });

        vm.forwardTo = function() {
            var checkbox = element(by.model('check'));
            var checkElem = element(by.css('.check-element'));
            expect(checkElem.isDisplayed()).toBe(false);
            checkbox.click();
            expect(checkElem.isDisplayed()).toBe(true);
        };

        vm.viewLetterIn = function() {
            $state.go("application.letter_management");
        }

        vm.letterIn.isOtherIssueOrgan = true; // để thêm cơ quan ban hành bằng tay
        vm.generate = function() {
            service.getListTaskOwnerByRoleCode('FowardRole').then(function(data) {
                vm.taskOwnersForwarders = data.map(function(element) {
                    if (element.person) {
                        element.displayName = element.person.displayName;
                    }
                    return element;
                });
            });
            if (letterId) {
                vm.getLetterInDocumentById(letterId);
            } else {
                service.generateLetterInDto().then(function(data) {
                    vm.letterIn = data;
                    vm.letterIn.isOtherIssueOrgan = true; // để thêm cơ quan ban hành bằng tay
                    vm.letterIn.docCode = ""; // ko dùng số đếm tự động
                    vm.letterIn.registeredDate = moment(new Date()).toObject();
                    vm.letterIn.deliveredDate = vm.letterIn.registeredDate;
                    vm.letterIn.numberOfPages = 1;
                });
            }
            vm.isForward = true;
        };

        vm.generate();
        vm.reworkLetterIn = function() {
            vm.generate();
            vm.forwarderId = null;
        }
        vm.cancelLetterIn = function() {
            $state.go('application.letter_management');
        }
        $scope.deleteDocument = function(index) {
            if (vm.letterIn != null && vm.letterIn.attachments != null) {
                for (var i = 0; i < vm.letterIn.attachments.length; i++) {
                    if (i == index) {
                        vm.letterIn.attachments.splice(i, 1);
                    }
                }
            }
        }
        $scope.viewFile = function(index) {
            if (vm.letterIn != null && vm.letterIn.attachments != null) {
                for (var i = 0; i < vm.letterIn.attachments.length; i++) {
                    if (i == index) {
                        var attachment = vm.letterIn.attachments[i];
                        service.getFileById(attachment.file.id).success(function(data) {
                            var file = new Blob([data], { type: attachment.file.contentType });
                            if (attachment.file.contentType == 'application/pdf') {
                                var fileURL = URL.createObjectURL(file);
                                window.open(fileURL);
                            } else {
                                toastr.warning('Không thể xem tệp tin. Hãy tải xuống', 'Thông báo');
                            }
                        });;
                    }
                }
            }
        }
        $scope.downloadFile = function(index) {
            if (vm.letterIn != null && vm.letterIn.attachments != null) {
                for (var i = 0; i < vm.letterIn.attachments.length; i++) {
                    if (i == index) {
                        var attachment = vm.letterIn.attachments[i];
                        service.getFileById(attachment.file.id).success(function(data) {
                            var file = new Blob([data], { type: attachment.file.contentType });
                            FileSaver.saveAs(file, attachment.file.name);
                        });;
                    }
                }
            }
        }

        vm.isDisabled = function() {
            vm.isView = false;
            if (letterId && state == 'forward') {
                return true;
            } else if (letterId && state == 'view') {
                vm.isView = true;
                return true;
            } else
                return false;
        };

        vm.newForward = function() {

        }

        vm.editDocumentFromView = function(letterId) {
            letterIndocumentFactory.letterFunc(letterId, '');
        }
        vm.goBack = function() {
            $window.history.back();
        }

        vm.completeLetter = function(letterId) {
            service.processingLetterIn(letterId, function successCallback() {
                $state.go('application.finish_letter_in');
            }, function errorCallback() {

            });
        };
        vm.ShowOrHide = function() {
            var checkbox = element(by.model('checked'));
            var checkElem = element(by.css('.check-element'));

            expect(checkElem.isDisplayed()).toBe(false);
            checkbox.click();
            expect(checkElem.isDisplayed()).toBe(true);
        }

        vm.getMess = function(mess) {
            return "Chuyển";
        }

        vm.taskComment = {};
        vm.modalInstanceParticipateComment = {};
        vm.addComment = function(participate) {
            vm.taskComment.participate = participate;
            vm.taskComment.comment = null;
            console.log(vm.taskComment.participate.taskOwner.displayName);
            vm.modalInstanceParticipateComment = modal.open({
                animation: true,
                templateUrl: 'add_comment.html',
                scope: $scope,
                backdrop: 'static',
                size: 'md'
            });
        };

        vm.sortObject = function(list) {
            if (list === undefined) {
                return -1;
            }
            return list.sort(function(a, b) {
                return a.role.id - b.role.id;
            });
        }

        vm.confirmAddComment = function() {
            if (vm.taskComment.comment && vm.taskComment.comment.trim() != '') {
                service.addedittaskcomment(vm.taskComment, function(data) {
                    vm.modalInstanceParticipateComment.close();
                    vm.generate();
                }, function() {

                });
            } else {
                toastr.warning("Bạn phải nhập ý kiến xử lý!")
            }
        }

        vm.showAddComment = function() {

        }

        //        vm.letterDocBookSelected = function(){
        //            vm.letterIn.documentOrderNoByBook = vm.letterIn.letterDocBook.currentNumber+1;
        //        }

        vm.letterDocBookSelected = function() {
            if (vm.letterIn.documentOrderNoByBook == null) {
                vm.letterIn.documentOrderNoByBook = vm.letterIn.letterDocBook.currentNumber + 1;
            }
        }

        vm.editComment = function(comment, participate) {
            vm.taskComment = JSON.parse(JSON.stringify(comment));
            vm.taskComment.participate = JSON.parse(JSON.stringify(participate));
            vm.modalInstanceParticipateComment = modal.open({
                animation: true,
                templateUrl: 'add_comment.html',
                scope: $scope,
                backdrop: 'static',
                size: 'md'
            });
        }

        vm.isShowEdit = function(userNameParticipate) {
            if ($scope.currentUser.username == userNameParticipate) {
                return true;
            } else {
                return false;
            }
        };

        vm.fowardTo = function() {

        }

        $scope.viewFile = function(index) {
            if (vm.letterIn != null && vm.letterIn.attachments != null) {
                for (var i = 0; i < vm.letterIn.attachments.length; i++) {
                    if (i == index) {
                        var attachment = vm.letterIn.attachments[i];
                        service.getFileById(attachment.file.id).success(function(data) {
                            var file = new Blob([data], { type: attachment.file.contentType });
                            if (attachment.file.contentType == 'application/pdf') {
                                var fileURL = URL.createObjectURL(file);
                                window.open(fileURL);
                            } else {
                                toastr.warning('Không thể xem tệp tin. Hãy tải xuống', 'Thông báo');
                            }
                        });;
                    }
                }
            }
        }

        // $scope.getUrl = function (id) {
        // service.getFileById(id).success(function (data) {
        // var file = new Blob([data], {type: 'application/pdf'});
        // var fileURL = URL.createObjectURL(file);
        // $scope.content = $sce.trustAsResourceUrl(fileURL);
        // });
        // }
        // $scope.getUrl(29)

        service.getListTaskOwnerByRoleCode('ChiefOfStaffRole').then(function(data) {
            vm.taskOwnersChiefOfStaff = data.map(function(element) {
                if (element.person) {
                    element.displayName = element.person.displayName;
                }
                return element;
            });
        });

        vm.hasClerkRole = true;
        vm.checkClerkRole = function() {
            service.checkUserHasTaskRoleByUserNameAndRoleCode($scope.currentUser.username, "ClerkRole").then(function(data) {
                vm.hasClerkRole = data;
                if (vm.isNew && !vm.hasClerkRole) {
                    $state.go('application.letter_management');
                }
            }).catch(function(err) {
                vm.hasClerkRole = true;
            });
        }
        vm.checkClerkRole();
    }
})();