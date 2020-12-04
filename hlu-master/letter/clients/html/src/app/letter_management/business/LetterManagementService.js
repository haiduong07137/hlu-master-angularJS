/**
 * Created by nguyen the dat on 22/3/2018.
 */
(function () {
    'use strict';

    angular.module('Hrm.LetterManagement').service('LetterManagementService', LetterManagementService);

    LetterManagementService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function LetterManagementService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;
        var restUrl = 'letter/';
        
        self.newLetterIn = newLetterIn;
        self.getTableDefinition = getTableDefinition;
        self.getPageLetterInByIndex = getPageLetterInByIndex;
        self.getPageLetterInByText = getPageLetterInByText;
        self.getCurrentTaskOwner = getCurrentTaskOwner;
        self.getOrganizationTree = getOrganizationTree;
        self.getOrganization = getOrganization;
        self.getTableAttachmentFileDefinition = getTableAttachmentFileDefinition;
        self.getListTaskOwnerByRoleCode = getListTaskOwnerByRoleCode;
        self.getLetterInDocumentById = getLetterInDocumentById;
        self.newLetterInStepAssign = newLetterInStepAssign;
        self.saveParticipate = saveParticipate;
        self.saveListParticipate = saveListParticipate;
        self.getTableAttachmentProcessFileDefinition = getTableAttachmentProcessFileDefinition;
        self.getTableLetterOutDocumentDefinition = getTableLetterOutDocumentDefinition;
        // self.getTableDefinitionTaskOwner = getTableDefinitionTaskOwner;
        self.assignProcess = assignProcess;
        self.getFileById = getFileById;
        self.processingLetterIn = processingLetterIn;

        self.getListDocBook = getListDocBook;
        self.getListDocBookGroup = getListDocBookGroup;
        self.getListDocBookByGroupId = getListDocBookByGroupId;
        self.deleteDocument = deleteDocument;
        self.assignTask = assignTask;
        self.addedittaskcomment = addedittaskcomment;
        self.generateLetterInDto = generateLetterInDto;
        self.createLetterOut = createLetterOut;
        self.getAllLetterOutDocument = getAllLetterOutDocument;
        self.getAllLetterOutDocumentBy = getAllLetterOutDocumentBy;
        self.generateLetterOutDto = generateLetterOutDto;
        self.getLetterOutDocumentById = getLetterOutDocumentById;
        self.getUserRoles = getUserRoles;
        self.getTableDefinitionFinishLetterOut = getTableDefinitionFinishLetterOut;
        self.checkUserHasTaskRoleByUserNameAndRoleCode = checkUserHasTaskRoleByUserNameAndRoleCode;// check have username and role bye task_role have exists
        self.getAllDocumentTypeIds = getAllDocumentTypeIds;
        self.getAllLetterDocFieldIds = getAllLetterDocFieldIds;
        self.searchPageLetter = searchPageLetter;
        
        function getAllLetterDocFieldIds() {
            var url = baseUrl + restUrl + 'doc_field/1/100';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        function getAllDocumentTypeIds() {
            var url = baseUrl + restUrl + 'doc_type/1/100';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        function checkUserHasTaskRoleByUserNameAndRoleCode(username, roleCode){
            var url = baseUrl + 'taskman/user_task_owner/check_user_has_taskrole/'+username+'/'+roleCode;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getUserRoles() {
            var url = baseUrl + restUrl + 'indocument/getUserRoles';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getLetterOutDocumentById(id) {
            var url = baseUrl + restUrl + 'outdocument/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        function generateLetterOutDto() {
            var url = baseUrl + restUrl + 'outdocument/generatedtoletterout';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        function getAllLetterOutDocument(pageIndex, pageSize) {
            var url = baseUrl + 'letter/outdocument/' + pageIndex + '/' + pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        function getAllLetterOutDocumentBy(stepId, currentParticipateStates, pageIndex, pageSize) {
            var url = baseUrl + 'letter/outdocument/getbystep/' + stepId + '/' + currentParticipateStates + '/' + pageIndex + '/' + pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        function searchPageLetter(stepIndex, currentParticipateStates, searchDto, pageSize, pageIndex, successCallback, errorCallback) {
            var url = baseUrl + restUrl + 'indocument/getletterby/' + stepIndex + '/' + currentParticipateStates + '/' + pageSize + '/' + pageIndex;
            return utils.resolveAlt(url, 'POST', null, searchDto, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        function createLetterOut(dto, successCallback, errorCallback) {
            var url = baseUrl + 'letter/outdocument/create';

            return utils.resolveAlt(url, 'POST', null, dto, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function generateLetterInDto() {
            var url = baseUrl + restUrl + 'indocument/generatedtoletterin';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }


        function addedittaskcomment(dto, successCallback, errorCallback) {
            var url = baseUrl + '/taskman/taskcomment/addedittaskcomment';

            return utils.resolveAlt(url, 'POST', null, dto, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function processingLetterIn(documentId, successCallback, errorCallback) {
            var url = baseUrl + restUrl + 'indocument/processingletterin/' + documentId;

            return utils.resolveAlt(url, 'POST', null, null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function assignTask(documentId, dtos, successCallback, errorCallback) {
            var url = baseUrl + restUrl + 'indocument/assigntask/' + documentId;

            return utils.resolveAlt(url, 'POST', null, dtos, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getListDocBookByGroupId(groupId) {
            var url = baseUrl + restUrl + 'docbook/getByGroupId/' + groupId;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getListDocBook(pageIndex, pageSize) {
            var url = baseUrl + restUrl + 'docbook/' + pageIndex + '/' + pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getListDocBookGroup(pageIndex, pageSize) {
            var url = baseUrl + restUrl + 'docbookgroup/' + pageIndex + '/' + pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getFileById(id) {
            var url = baseUrl + 'workplan/planfile/' + id;
            return $http.get(url, { responseType: 'arraybuffer' });
        }

        function assignProcess(letterIn, docClerkId, forwarderId, assignerId, successCallback, errorCallback) {
            if (angular.isUndefined(forwarderId) || forwarderId == null) {
                forwarderId = 0;
            }
            if (angular.isUndefined(assignerId) || assignerId == null) {
                assignerId = 0;
            }

            var url = baseUrl + restUrl + 'indocument/assign_step/create/' + docClerkId + '/' + forwarderId + '/' + assignerId;

            return utils.resolveAlt(url, 'POST', null, letterIn, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function saveParticipate(object, successCallback, errorCallback) {
            var url = baseUrl + 'taskman/participate/';

            return utils.resolveAlt(url, 'POST', null, object, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function saveListParticipate(object, successCallback, errorCallback) {
            var url = baseUrl + 'taskman/participate/save_list';

            return utils.resolveAlt(url, 'POST', null, object, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getLetterInDocumentById(id) {
            if (!id) {
                return $q.when(null);
            }
            var url = baseUrl + restUrl + 'indocument/get_one/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function newLetterIn(letterIn, docClerkId, forwarderId, successCallback, errorCallback) {
            if (angular.isUndefined(forwarderId) || forwarderId == null) {
                forwarderId = 0;
            }
            var url = baseUrl + restUrl + 'indocument/create/' + docClerkId + '/' + forwarderId;

            return utils.resolveAlt(url, 'POST', null, letterIn, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function newLetterInStepAssign(letterIn, docClerkId, forwarderId, assignerId, successCallback, errorCallback) {
            if (angular.isUndefined(forwarderId) || forwarderId == null) {
                forwarderId = 0;
            }
            if (angular.isUndefined(assignerId) || assignerId == null) {
                assignerId = 0;
            }
            var url = baseUrl + restUrl + 'indocument/create/' + docClerkId + '/' + forwarderId + '/' + assignerId;

            return utils.resolveAlt(url, 'POST', null, letterIn, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        function deleteDocument(documentId, successCallback, errorCallback) {
            var url = baseUrl + 'letter/indocument/remove/' + documentId;
            return utils.resolveAlt(url, 'DELETE', null, null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getPageLetterInByIndex(stepIndex, currentParticipateStates, pageIndex, pageSize) {
            var url = baseUrl + restUrl + 'indocument/getbystep/' + stepIndex + '/' + currentParticipateStates + '/' + pageSize + '/' + pageIndex;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getPageLetterInByText(stepIndex, currentParticipateStates, text, pageIndex, pageSize, successCallback, errorCallback) {
            var searchDto = {};
            if (text != null) {
                searchDto.text = text;
            }
            var url = baseUrl + restUrl + 'indocument/getletterby/' + stepIndex + '/' + currentParticipateStates + '/' + pageSize + '/' + pageIndex;

            return utils.resolveAlt(url, 'POST', null, searchDto, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }

        function getCurrentTaskOwner() {
            // var url
            // ='http://localhost:8080/taskman/api/taskman/taskowner/getcurrenttaskowner';
            var url = baseUrl + 'taskman/taskowner/getcurrenttaskowner';
            // http://localhost:8080/letter/api/taskman/taskowner/getcurrenttaskowner
            // http://localhost:8080/letter/api/taskman/taskowner/getcurrenttaskowner

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getListTaskOwnerByRoleCode(roleCode) {
            // var url
            // ='http://localhost:8080/taskman/api/taskman/taskowner/getcurrenttaskowner';
            var url = baseUrl + 'taskman/taskowner/gettaskownerbyrolecode/' + roleCode;
            // http://localhost:8080/letter/api/taskman/taskowner/getcurrenttaskowner
            // http://localhost:8080/letter/api/taskman/taskowner/getcurrenttaskowner

            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getOrganizationTree() {
            var url = baseUrl + 'letter/core/organization_tree';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getOrganization(id) {
            if (!id) {
                return $q.when(null);
            }

            var url = baseUrl + 'letter/core/organization/dto/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getTableDefinition() {
            var _tableOperation = function (value, row, index) {
                // return '<a class="green-dark margin-right-20"  ng-show="'+ "'" + row.task.currentStep.code + "'" + ' === ' + "'LetterInCodeStep1'" +'" href="#" data-ng-click="$parent.editDocument(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i> Sửa</a>'
                //     +  '<a class="green-dark margin-right-20"  ng-show="'+ "'" + row.task.currentStep.code + "'" + ' === ' + "'LetterInCodeStep1'" +'" href="#" data-ng-click="$parent.forwardLetter(' + "'" + row.id + "'" + ')"><i class="fa fa-mail-forward margin-right-5"></i> Chuyển phân luồng</a>'
                //     +  '<a class="green-dark margin-right-20"   ng-show="'+ "'" + row.task.currentStep.code + "'" + ' === ' + "'LetterInCodeStep2'" +'" href="#" data-ng-click="$parent.forwardLetterStepTwo(' + "'" + row.id + "'" + ')"><i class="fa fa-forward" aria-hidden="true"></i> Phân luồng</a>'
                //     +  '<a class="green-dark margin-right-20"   ng-show="'+ "'" + row.task.currentStep.code + "'" + ' === ' + "'LetterInCodeStep3'" +'" href="#" data-ng-click="$parent.assignLetter(' + "'" + row.id + "'" + ')"><i class="fa fa-cog" aria-hidden="true"></i> Giao xử lý</a>';

                var hardCodeCodition = false;
                var str = "";

                // xem hồ sơ
                str += '<a class="green-dark margin-right-10" title="Xem hồ sơ xử lý" href="#" data-ng-click="$parent.viewDocument(' + "'" + row.id + "'" + ')"><img src="assets/images/letter_management/document.png" /></a>'

                // Phân quyền đọc văn bản
                if (hardCodeCodition) {
                    str += '<a class="green-dark margin-right-10" title="Phân quyền đọc văn bản"><img src="assets/images/letter_management/share.png" /></a>'
                }
                else {
                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Phân quyền đọc văn bản" ><img src="assets/images/letter_management/share_disabled.png" /></a></span>'
                }

                // Trình Hiệu trưởng
                if (hardCodeCodition) {
                    str += '<a class="green-dark margin-right-10" title="Trình Hiệu trưởng"><img src="assets/images/letter_management/forward.png" /></a>'
                }
                else {
                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Trình Hiệu trưởng" ><img src="assets/images/letter_management/forward_disabled.png" /></a></span>'
                }

                // Trả lại văn thư
                if (hardCodeCodition) {
                    str += '<a class="green-dark margin-right-10" title="Trả lại văn thư"><img src="assets/images/letter_management/back.png" /></a>'
                }
                else {
                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Trả lại văn thư" ><img src="assets/images/letter_management/back_disabled.png" /></a></span>'
                }

                // Chuyển giao xử lý
                if (hardCodeCodition) {
                    str += '<a class="green-dark margin-right-10" title="Chuyển giao xử lý"><img src="assets/images/letter_management/transfer.png" /></a>'
                }
                else {
                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Chuyển giao xử lý" ><img src="assets/images/letter_management/transfer_disabled.png" /></a></span>'
                }

                // Trả lại Văn phòng
                if (hardCodeCodition) {
                    str += '<a class="green-dark margin-right-10" title="Trả lại Văn phòng"><img src="assets/images/letter_management/return.png" /></a>'
                }
                else {
                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Trả lại Văn phòng" ><img src="assets/images/letter_management/return_disabled.png" /></a></span>'
                }

                // Giao xử lý
                if (row.task.currentStep.code === "LetterInCodeStep3" && (row.hasAssignerRole)) {
                    str += '<a class="green-dark margin-right-10" title="Giao xử lý" href="#" data-ng-click="$parent.assignLetter(' + "'" + row.id + "'" + ')"><img src="assets/images/letter_management/go-down.png" /></a>'
                }
                else {
                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Giao xử lý" ><img src="assets/images/letter_management/go-down_disabled.png" /></a></span>'
                }

                // Gửi ý kiến Xử lý
                if (hardCodeCodition) {
                    str += '<a class="green-dark margin-right-10" title="Gửi ý kiến Xử lý"><img src="assets/images/letter_management/process.png" /></a>'
                }
                else {
                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Gửi ý kiến Xử lý" ><img src="assets/images/letter_management/process_disabled.png" /></a></span>'
                }

                // Đóng hồ sơ và kết thúc xử lý
                if (hardCodeCodition) {
                    str += '<a class="green-dark margin-right-10" title="Đóng hồ sơ và kết thúc xử lý"><img src="assets/images/letter_management/locked.png" /></a>'
                }
                else {
                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Đóng hồ sơ và kết thúc xử lý" ><img src="assets/images/letter_management/locked_disabled.png" /></a></span>'
                }

                // Mở lại hồ sơ
                if (hardCodeCodition) {
                    str += '<a class="green-dark margin-right-10" title="Mở lại hồ sơ"><img src="assets/images/letter_management/unlocked.png" /></a>'
                }
                else {
                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Mở lại hồ sơ" ><img src="assets/images/letter_management/unlocked_disabled.png" /></a></span>'
                }

                // sửa
                // chuyển phân luồng
                if (row.task.currentStep.code === "LetterInCodeStep1" && row.hasClerkRole) {
                    str += '<a class="green-dark margin-right-10" title="Sửa thông tin văn bản" href="#" data-ng-click="$parent.editDocument(' + "'" + row.id + "'" + ')"><i class="fa fa-pencil margin-right-5"></i></a>'
                    str += '<a class="green-dark margin-right-10" title="Chuyển phân luồng" href="#" data-ng-click="$parent.forwardLetter(' + "'" + row.id + "'" + ')"><i class="fa fa-mail-forward margin-right-5"></i></a>';
                }
                else {
                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Sửa thông tin văn bản" ><i class="fa fa-pencil margin-right-5"></i></a></span>'
                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Chuyển phân luồng" ><i class="fa fa-mail-forward margin-right-5"></i></a></span>'
                }

                // phân luồng
                if (row.task.currentStep.code === "LetterInCodeStep2" && (row.hasFowardRole)) {
                    str += '<a class="green-dark margin-right-10" title="Phân luồng" href="#" data-ng-click="$parent.forwardLetterStepTwo(' + "'" + row.id + "'" + ')"><i class="fa fa-forward" aria-hidden="true"></i></a>'
                }
                else {
                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Phân luồng" ><i class="fa fa-forward margin-right-5"></i></a></span>'
                }

                return str;
            };

            var _tableOperation1 = function (value, row, index) {
                return '<a class="green-dark margin-left-5" title="' + value + '" href="#" data-ng-click="$parent.viewDocument(' + "'" + row.id + "'" + ')"><span style="font-size: 15px">' + value + '<span></a>';
                /*return '<a class="green-dark margin-left-5" title="' + value + '" href="#" data-ng-click="$parent.viewAllFileInDocument(' + "'" + row.id + "'" + ')"><span style="font-size: 15px">' + value + '<span></a>';*/
            };

            var _cellNowrap = function (value, row, index, field) {
                if (row.task.currentStep.code === "LetterInCodeStep1") {
                    return {
                        classes: '',
                        css: { 'word-wrap': 'break-word', 'font-weight': 'bold' }
                    };
                }
                return {
                    classes: '',
                    css: { 'word-wrap': 'break-word' }
                };
            };
            var _cellNowrap2 = function (value, row, index, field) {
                return {
                    classes: ''
                    // css: {'white-space': 'nowrap'}
                };
            };

            var _dateFormatter = function (value, row, index) {
                if (!value) {
                    return '';
                }
                return moment(value).format('DD/MM/YYYY');
            };

            var _issueaOrganFormatter = function (value, row, index) {
                if (!value) {
                    if (row.otherIssueOrgan) {
                        return row.otherIssueOrgan;
                    }
                    return '';
                }
                return value.name;
            };

            var _formatterName = function (value, row, index) {
                const codeRole = "AssignerRole";
                if (!row) {
                    return '';
                } else {
                    if (row.task && row.task.participates && row.task.participates.length > 1) {
                        const participate = row.task.participates.find(participate => {
                            if (participate && participate.taskOwner && participate.taskOwner.ownerType === 1) {
                                return participate.role.code === codeRole
                            }
                        });
                        if (participate !== undefined && participate.role.code === codeRole) {
                            return participate.taskOwner.displayName;
                        } else {
                            return '';
                        }
                    } else {
                        return '';
                    }

                }
            };

            return [
                {
                    field: 'docOriginCode',
                    title: 'Số hiệu văn bản',
                    sortable: true,
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation1,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'briefNote',
                    title: 'Trích yếu',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation1,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'deliveredDate',
                    title: 'Ngày đến',
                    switchable: true,
                    visible: true,
                    formatter: _dateFormatter,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'expiredDate',
                    title: 'Hạn trả lời',
                    switchable: true,
                    visible: true,
                    formatter: _dateFormatter,
                    cellStyle: _cellNowrap
                },
                {
                    field: 'issueOrgan',
                    title: 'Nơi gửi',
                    switchable: true,
                    visible: true,
                    formatter: _issueaOrganFormatter,
                    cellStyle: _cellNowrap
                },
                {
                    field: '',
                    title: 'Lãnh Đạo',
                    switchable: true,
                    visible: true,
                    formatter: _formatterName,
                    cellStyle: _cellNowrap
                }
                , {
                    field: '',
                    title: 'Thao tác',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation,
                    cellStyle: _cellNowrap2
                }
            ]
        }


        /** upload file */
        function getTableAttachmentFileDefinition() {
            var _tableOperation = function (value, row, index) {
                var ret = '';
                ret += '<a class="text-danger margin-right-10" uib-tooltip="Xóa tệp tin" tooltip-trigger="mouseenter" href="#" data-ng-click="$parent.deleteDocument(' + "'" + index + "'" + ')"><i class="icon-trash"></i></a>';
                ret += '<a class="green-dark" uib-tooltip="Tải về" tooltip-trigger="mouseenter" href="#" data-ng-click="$parent.downloadDocument(' + "'" + index + "'" + ')"><i class="icon-cloud-download"></i></a>';

                return ret;
            };


            var _fileSize = function (value, row, index) {
                return $filter('fileSize')(value);
            };

            var _fileType = function (value, row, index) {
                return $filter('contentType')(value);
            };

            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap' }
                };
            };

            var _operationColStyle = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap', 'width': '90px', 'text-align': 'center' }
                };
            };

            return [
                {
                    field: '',
                    title: 'Thao tác',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation,
                    cellStyle: _operationColStyle
                }
                , {
                    field: 'file.name',
                    title: 'Tên tệp tin',
                    sortable: true,
                    switchable: false,
                    cellStyle: _operationColStyle
                }
                , {
                    field: 'file.contentSize',
                    title: 'Kích thước tệp tin',
                    sortable: true,
                    switchable: false,
                    cellStyle: _operationColStyle,
                    formatter: _fileSize
                }
            ]
        }


        function getTableAttachmentProcessFileDefinition() {
            var _tableOperation = function (value, row, index) {
                var ret = '';
                // ret += '<a class="text-danger margin-right-10"
                // uib-tooltip="Xóa tệp tin" tooltip-trigger="mouseenter"
                // href="#" data-ng-click="$parent.deleteDocument(' + "'" +
                // index + "'" + ')"><i class="icon-trash"></i></a>';
                ret += '<a class="green-dark" uib-tooltip="Tải về" tooltip-trigger="mouseenter" href="#" data-ng-click="$parent.downloadDocument(' + "'" + index + "'" + ')"><i class="icon-cloud-download"></i></a>';

                return ret;
            };


            var _fileSize = function (value, row, index) {
                return $filter('fileSize')(value);
            };

            var _fileType = function (value, row, index) {
                return $filter('contentType')(value);
            };

            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap' }
                };
            };

            var _operationColStyle = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap', 'width': '90px', 'text-align': 'center' }
                };
            };

            return [
                {
                    field: '',
                    title: 'Thao tác',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation,
                    cellStyle: _operationColStyle
                }
                , {
                    field: 'file.name',
                    title: 'Tên tệp tin',
                    sortable: true,
                    switchable: false,
                    cellStyle: _operationColStyle
                }
                , {
                    field: 'file.contentSize',
                    title: 'Kích thước tệp tin',
                    sortable: true,
                    switchable: false,
                    cellStyle: _operationColStyle,
                    formatter: _fileSize
                }
            ]
        }

        function getTableLetterOutDocumentDefinition() {

            var _tableOperation = function (value, row, index) {
            	var str = "";

                // xem hồ sơ
                str += '<a class="green-dark margin-right-10" title="Xem hồ sơ xử lý" href="#" data-ng-click="$parent.viewDocument(' + "'" + row.id + "'" + ')"><img src="assets/images/letter_management/document.png" /></a>'
                
                if (row.task.currentStep.code === 'LETTEROUTSTEP1' && row.hasClerkRole) {
                    str += '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editDocument(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i> </a>';
				}
                return str;
            };
            var _tableOperation1 = function (value, row, index) {
                return '<a class="green-dark margin-left-5" title="' + value + '" href="#" data-ng-click="$parent.viewDocument(' + "'" + row.id + "'" + ')"><span style="font-size: 15px">' + value + '<span></a>';
            };
            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'word-wrap': 'break-word' }
                };
            };
            var _cellNowrap2 = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap' }
                };
            };

            var _dateFormatter = function (value, row, index) {
                if (!value) {
                    return '';
                }
                return moment(value).format('DD/MM/YYYY');
            };

            var _issueaOrganFormatter = function (value, row, index) {
                if (!value) {
                    return '';
                }
                return value.name;
            };

            var _formatterName = function (value, row, index) {
                let signedPost = '';
                let signedPerson = '';

                if (!row) {
                    return '';
                } else {
                    if (row.signedPerson) {
                        signedPerson = row.signedPerson;
                    }
                    if (row.signedPost) {
                        signedPost = '<small>' + row.signedPost + ' </small>';
                    }

                }

                return signedPost + signedPerson;
            };

            return [
                {
                    field: 'docOriginCode',
                    title: 'Số hiệu văn bản',
                    sortable: true,
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation1,
                    cellStyle: _cellNowrap
                }, 
                {
                    field: 'briefNote',
                    title: 'Trích yếu',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation1,
                    cellStyle: _cellNowrap
                }, 
                {
                    field: 'deliveredDate',
                    title: 'Ngày đến',
                    switchable: true,
                    visible: true,
                    formatter: _dateFormatter,
                    cellStyle: _cellNowrap
                }, 
                {
                    field: 'expiredDate',
                    title: 'Hạn trả lời',
                    switchable: true,
                    visible: true,
                    formatter: _dateFormatter,
                    cellStyle: _cellNowrap
                },
                {
                	field: 'issueOrgan',
                	title: 'Cơ quan tiếp nhận',
                	switchable: true,
                	visible: true,
                	formatter: _issueaOrganFormatter,
                	cellStyle: _cellNowrap
                },
                {
                	field: '',
                	title: 'Thao tác',
                	switchable: true,
                	visible: true,
                	formatter: _tableOperation,
                	cellStyle: _cellNowrap2
                }
            ]
        }
        


        function getTableDefinitionFinishLetterOut() {

            var _tableOperation = function (value, row, index) {
            	var str = "";

                // xem hồ sơ
                str += '<a class="green-dark margin-right-10" title="Xem hồ sơ xử lý" href="#" data-ng-click="$parent.viewDocument(' + "'" + row.id + "'" + ')"><img src="assets/images/letter_management/document.png" /></a>'
                
                if (row.task.currentStep.code === 'LETTEROUTSTEP1' && row.hasClerkRole) {
                    str += '<a class="green-dark margin-right-20" href="#" data-ng-click="$parent.editDocument(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i> </a>';
				}
                return str;
            };
            var _tableOperation1 = function (value, row, index) {
                return '<a class="green-dark margin-left-5" title="' + value + '" href="#" data-ng-click="$parent.viewDocument(' + "'" + row.id + "'" + ')"><span style="font-size: 15px">' + value + '<span></a>';
            };
            var _cellNowrap = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'word-wrap': 'break-word' }
                };
            };
            var _cellNowrap2 = function (value, row, index, field) {
                return {
                    classes: '',
                    css: { 'white-space': 'nowrap' }
                };
            };

            var _dateFormatter = function (value, row, index) {
                if (!value) {
                    return '';
                }
                return moment(value).format('DD/MM/YYYY');
            };

            var _issueaOrganFormatter = function (value, row, index) {
                if (!value) {
                    return '';
                }
                return value.name;
            };

            var _formatterName = function (value, row, index) {
                let signedPost = '';
                let signedPerson = '';

                if (!row) {
                    return '';
                } else {
                    if (row.signedPerson) {
                        signedPerson = row.signedPerson;
                    }
                    if (row.signedPost) {
                        signedPost = '<small>' + row.signedPost + ' </small>';
                    }
                }
                return signedPost + signedPerson;
            };

            return [
                {
                    field: 'docOriginCode',
                    title: 'Số hiệu văn bản',
                    sortable: true,
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation1,
                    cellStyle: _cellNowrap
                }, 
                {
                    field: 'briefNote',
                    title: 'Trích yếu',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation1,
                    cellStyle: _cellNowrap
                }, 
                {
                    field: 'deliveredDate',
                    title: 'Ngày đến',
                    switchable: true,
                    visible: true,
                    formatter: _dateFormatter,
                    cellStyle: _cellNowrap
                }, 
                {
                    field: 'expiredDate',
                    title: 'Hạn trả lời',
                    switchable: true,
                    visible: true,
                    formatter: _dateFormatter,
                    cellStyle: _cellNowrap
                },
                {
                	field: 'issueOrgan',
                	title: 'Cơ quan tiếp nhận',
                	switchable: true,
                	visible: true,
                	formatter: _issueaOrganFormatter,
                	cellStyle: _cellNowrap
                }
                /*,{
                	field: '',
                	title: 'Thao tác',
                	switchable: true,
                	visible: true,
                	formatter: _tableOperation,
                	cellStyle: _cellNowrap2
                }*/
            ]
        }
        
    }
})();