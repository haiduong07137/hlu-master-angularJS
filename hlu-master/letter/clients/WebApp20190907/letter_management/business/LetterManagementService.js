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
        'Utilities',
        '$rootScope'
    ];

    function LetterManagementService($http, $q, $filter, settings, utils, $rootScope) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;
        var baseUrlNews = settings.api.baseUrl + 'api/news/cmsArticle';
        self.newLetterIn = newLetterIn;
        self.getTableDefinition = getTableDefinition;
        self.getTableDefinitionReturn = getTableDefinitionReturn;
        self.getPageLetterInByIndex = getPageLetterInByIndex;
        self.getPageLetterInByText = getPageLetterInByText;
        self.getCurrentTaskOwner = getCurrentTaskOwner;
        self.getOrganizationTree = getOrganizationTree;
        self.getOrganization = getOrganization;
        self.getTableAttachmentFileDefinition = getTableAttachmentFileDefinition;
        self.getListTaskOwnerByRoleCode = getListTaskOwnerByRoleCode;
        self.getLetterInDocumentById = getLetterInDocumentById;
        self.getAllLetterDocument = getAllLetterDocument;
        self.newLetterInStepAssign = newLetterInStepAssign;
        self.saveParticipate = saveParticipate;
        self.saveListParticipate = saveListParticipate;
        self.getTableAttachmentProcessFileDefinition = getTableAttachmentProcessFileDefinition;
        self.getTableLetterOutDocumentDefinition = getTableLetterOutDocumentDefinition;
        // self.getTableDefinitionTaskOwner = getTableDefinitionTaskOwner;
        self.assignProcess = assignProcess;
        self.getFileById = getFileById;
        self.processingLetterIn = processingLetterIn;
        self.transferLeader1 = transferLeader1;
        
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
        self.getTableAllLetterDocument = getTableAllLetterDocument;
        self.getUserRoles = getUserRoles;
        self.getTableDefinitionFinishLetterOut = getTableDefinitionFinishLetterOut;
        self.checkUserHasTaskRoleByUserNameAndRoleCode = checkUserHasTaskRoleByUserNameAndRoleCode;// check have username and role bye task_role have exists
        self.getAllDocumentTypeIds = getAllDocumentTypeIds;
        self.getAllLetterDocFieldIds = getAllLetterDocFieldIds;
        self.searchPageLetter = searchPageLetter;
        self.searchPageLetterOut = searchPageLetterOut;
        self.saveComment = saveComment;
        self.returnBack = returnBack;
        self.getUsers = getUsers;
        self.searchPageAllLetter = searchPageAllLetter;
        self.getByDocumentById = getByDocumentById;
        self.forwardLetterOut = forwardLetterOut;
        self.transferLeader = transferLeader;
        self.signLetterOutDocument = signLetterOutDocument;
        self.getListNews = getListNews;
        self.addView = addView;
        self.checkViewLetter = checkViewLetter;
        self.getPublicUsers = getPublicUsers;
        self.checkViewDocument = checkViewDocument;
        
        function checkViewLetter(username, roleCode){
            var url = baseUrl + 'taskman/user_task_owner/check_user_has_taskrole/'+username+'/'+roleCode;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        function checkViewDocument(documentId, searchDto, pageIndex, pageSize, successCallback, errorCallback) {
            var url = baseUrl + 'letter/search/view/' + documentId + '/' + pageIndex + '/' + pageSize;
            return utils.resolveAlt(url, 'POST', null, searchDto, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        function addView(userId, documentId, successCallback, errorCallback) {
            var url = baseUrl + 'letter/add/view' + '/' + userId + '/' + documentId;

            return utils.resolveAlt(url, 'POST', null, null, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        function getListNews(pageIndex, pageSize) {

            var url = baseUrlNews + '/getListArticleByPage';
            url += '/' + pageIndex;
            url += '/' + pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        function getByDocumentById(id){
        	var url = baseUrl + restUrl + 'document/' + id;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        function getUsers(filter, pageIndex, pageSize) {

            var url = baseUrl + 'users/search';
            url += '/' + ((pageIndex > 0) ? pageIndex : 1);
            url += '/' + ((pageSize > 0) ? pageSize : 25);

            return utils.resolveAlt(url, 'POST', null, filter, {
                'Content-Type': 'application/json; charset=utf-8'
            }, angular.noop, angular.noop);
        }
        function getPublicUsers(pageIndex, pageSize) {
        	var url = baseUrl + 'letter/core/user' + '/' + pageSize + '/' + pageIndex;
        	return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        function getAllLetterDocument(pageSize, pageIndex){
        	var url = baseUrl + restUrl + 'document/getbypage' + '/' + pageSize + '/' + pageIndex;
        	return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
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
        
        function forwardLetterOut(letterOut, docClerkId, draftersId, forwarderId, successCallback, errorCallback) {
            if (angular.isUndefined(forwarderId) || forwarderId == null) {
                forwarderId = 0;
            }
            if (angular.isUndefined(draftersId) || draftersId == null) {
            	draftersId = 0;
            }
            var url = baseUrl + restUrl + 'outdocument/create/' + docClerkId + '/' + draftersId + '/' + forwarderId;

            return utils.resolveAlt(url, 'POST', null, letterOut, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        function transferLeader(letterOut, docClerkId, draftersId, forwarderId, leaderId, successCallback, errorCallback) {
            if (angular.isUndefined(forwarderId) || forwarderId == null) {
                forwarderId = 0;
            }
            if (angular.isUndefined(draftersId) || draftersId == null) {
            	draftersId = 0;
            }
            if (angular.isUndefined(leaderId) || leaderId == null) {
            	leaderId = 0;
            }
            var url = baseUrl + restUrl + 'outdocument/create/' + docClerkId + '/' + draftersId + '/' + forwarderId + '/' + leaderId;

            return utils.resolveAlt(url, 'POST', null, letterOut, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        function transferLeader1(letterOut, docClerkId, forwarderId, leaderId, successCallback, errorCallback) {
            if (angular.isUndefined(forwarderId) || forwarderId == null) {
                forwarderId = 0;
            }
            if (angular.isUndefined(leaderId) || leaderId == null) {
            	leaderId = 0;
            }
            var url = baseUrl + restUrl + 'outdocument/managercreate/' + docClerkId + '/' + forwarderId + '/' + leaderId;

            return utils.resolveAlt(url, 'POST', null, letterOut, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        function signLetterOutDocument(letterOut, docClerkId, draftersId, forwarderId, leaderId, successCallback, errorCallback) {
            if (angular.isUndefined(forwarderId) || forwarderId == null) {
                forwarderId = 0;
            }
            if (angular.isUndefined(draftersId) || draftersId == null) {
            	draftersId = 0;
            }
            if (angular.isUndefined(leaderId) || leaderId == null) {
            	leaderId = 0;
            }
            var url = baseUrl + restUrl + 'outdocument/sign/' + docClerkId + '/' + draftersId + '/' + forwarderId + '/' + leaderId;

            return utils.resolveAlt(url, 'POST', null, letterOut, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        function saveComment(dto,letterId) {
            var url = baseUrl + restUrl + 'indocument/sendBack/' +letterId+ '/1';

            return utils.resolveAlt(url, 'POST', null, dto, {
                'Content-Type': 'application/json; charset=utf-8'
            }, angular.noop, angular.noop);
        }
        
        function returnBack(dto,letterId) {
            var url = baseUrl + restUrl + 'outdocument/sendBack/' +letterId+ '/1';

            return utils.resolveAlt(url, 'POST', null, dto, {
                'Content-Type': 'application/json; charset=utf-8'
            }, angular.noop, angular.noop);
        }

        function generateLetterInDto() {
            var url = baseUrl + restUrl + 'indocument/generatedtoletterin';
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        var restUrl = 'letter/';

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
            var url = baseUrl + restUrl + 'indocument/getbystep/' + stepIndex + '/' + currentParticipateStates + '/' + pageIndex + '/' + pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getPageLetterInByText(stepIndex, currentParticipateStates, text, pageIndex, pageSize, successCallback, errorCallback) {
            var searchDto = {};
            if (text != null) {
                searchDto.text = text;
            }
            var url = baseUrl + restUrl + 'indocument/getletterby/' + stepIndex + '/' + currentParticipateStates + '/' + pageIndex + '/' + pageSize;

            return utils.resolveAlt(url, 'POST', null, searchDto, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        function searchPageAllLetter(searchDto, pageIndex, pageSize, successCallback, errorCallback) {
            var url = baseUrl + restUrl + 'document/search' + '/' + pageIndex + '/' + pageSize;

            return utils.resolveAlt(url, 'POST', null, searchDto, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        
        // Thêm vào để search Letter out
        
        function searchPageLetterOut(stepIndex, currentParticipateStates, searchDto, pageSize, pageIndex, successCallback, errorCallback) {
            var url = baseUrl + restUrl + 'outdocument/getletterby/' + stepIndex + '/' + currentParticipateStates + '/' + pageSize + '/' + pageIndex;
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

            var _downloadDocument = function (value, row, index) {
                if (!value) {
                    if (row.attachments!=null) {
                    	var str = "";
                        str += '<a class="green-dark margin-right-10" title="Xem tài liệu đính kèm" href="#" data-ng-click="$parent.viewAllFileInDocument(' + "'" + row.id + "'" + ')"><img src="assets/images/letter_management/view_documet.png" /></a>'
                        return str;
                    }
                    return 'abc';
                }
                return value.name;
            };
            var _tableOperation = function (value, row, index) {
                // return '<a class="green-dark margin-right-20"  ng-show="'+ "'" + row.task.currentStep.code + "'" + ' === ' + "'LetterInCodeStep1'" +'" href="#" data-ng-click="$parent.editDocument(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i> Sửa</a>'
                //     +  '<a class="green-dark margin-right-20"  ng-show="'+ "'" + row.task.currentStep.code + "'" + ' === ' + "'LetterInCodeStep1'" +'" href="#" data-ng-click="$parent.forwardLetter(' + "'" + row.id + "'" + ')"><i class="fa fa-mail-forward margin-right-5"></i> Chuyển phân luồng</a>'
                //     +  '<a class="green-dark margin-right-20"   ng-show="'+ "'" + row.task.currentStep.code + "'" + ' === ' + "'LetterInCodeStep2'" +'" href="#" data-ng-click="$parent.forwardLetterStepTwo(' + "'" + row.id + "'" + ')"><i class="fa fa-forward" aria-hidden="true"></i> Phân luồng</a>'
                //     +  '<a class="green-dark margin-right-20"   ng-show="'+ "'" + row.task.currentStep.code + "'" + ' === ' + "'LetterInCodeStep3'" +'" href="#" data-ng-click="$parent.assignLetter(' + "'" + row.id + "'" + ')"><i class="fa fa-cog" aria-hidden="true"></i> Giao xử lý</a>';

                var hardCodeCodition = false;
                var str = "";

                // xem hồ sơ
                str += '<a class="green-dark margin-right-10" title="Xem tình trạng hồ sơ" href="#" data-ng-click="$parent.viewDocumentPopup(' + "'" + row.id + "'" + ')"><img src="assets/images/letter_management/document.png" /></a>'
                
                // Phân quyền đọc văn bản
                if (hardCodeCodition) {
                    str += '<a class="green-dark margin-right-10" title="Phân quyền đọc văn bản"><img src="assets/images/letter_management/share.png" /></a>'
                }
                else {
                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Phân quyền đọc văn bản" ><img src="assets/images/letter_management/share_disabled.png" /></a></span>'
                }

                // Trình Hiệu trưởng
                /*
                if (hardCodeCodition) {
                    str += '<a class="green-dark margin-right-10" title="Trình Hiệu trưởng"><img src="assets/images/letter_management/forward.png" /></a>'
                }
                else {
                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Trình Hiệu trưởng" ><img src="assets/images/letter_management/forward_disabled.png" /></a></span>'
                }
				*/
				
                // Trả lại văn thư
                if (row.task.currentStep.code === "LetterInCodeStep2" && (row.hasFowardRole)) {
                    str += '<a class="green-dark margin-right-10" ng-click="$parent.returnBack(' + "'" + row.id + "'" + ')" title="Trả lại văn thư"><img src="assets/images/letter_management/back.png" /></a>'
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
                if ((row.task.currentStep.code === "LetterInCodeStep2" && (row.hasAssignerRole)) || (row.task.currentStep.code === "LetterInCodeStep2" && (row.hasFowardRole))) {
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
                    str += '<a class="green-dark margin-right-10" title="Sửa thông tin văn bản" href="#" data-ng-click="$parent.editDocument(' + "'" + row.id + "'" + ')"><i class="fa fa-pencil"></i></a>'
                    str += '<a class="green-dark margin-right-10" title="Chuyển phân luồng" href="#" data-ng-click="$parent.forwardLetter(' + "'" + row.id + "'" + ')"><i class="fa fa-mail-forward"></i></a>';
                }
                else {
                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Sửa thông tin văn bản" ><i class="fa fa-pencil5"></i></a></span>'
                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Chuyển phân luồng" ><i class="fa fa-mail-forward"></i></a></span>'
                }
                
                if ($rootScope.hasClerkRole) {
                    str += '<a class="green-dark margin-right-10" title="Kiểm người đã xem" href="#" data-ng-click="$parent.checkViewDocument(' + "'" + row.id + "'" + ')"><i class="fa fa-user-circle"></i></a>';
                }
                // phân luồng
//                if (row.task.currentStep.code === "LetterInCodeStep2" && (row.hasFowardRole)) {
//                    str += '<a class="green-dark margin-right-10" title="Phân luồng" href="#" data-ng-click="$parent.forwardLetterStepTwo(' + "'" + row.id + "'" + ')"><i class="fa fa-forward" aria-hidden="true"></i></a>'
//                }
//                else {
//                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Phân luồng" ><i class="fa fa-forward margin-right-5"></i></a></span>'
//                }

                return str;
            };

            var _tableOperation1 = function (value, row, index) {
                return '<a class="green-dark" title="' + row.briefNote + '" href="#" data-ng-click="$parent.viewDocument(' + "'" + row.id + "'" + ')"><span style="font-size: 15px">' + value + '<span></a>';
                /*return '<a class="green-dark" title="' + value + '" href="#" data-ng-click="$parent.viewAllFileInDocument(' + "'" + row.id + "'" + ')"><span style="font-size: 15px">' + value + '<span></a>';*/
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
            var _cellNowrap3 = function (value, row, index, field) {
                if (row.task.currentStep.code === "LetterInCodeStep1") {
                    return {
                        classes: '',
                        css: { 'word-wrap': 'break-word', 'font-weight': 'bold', 'width': '170px'}
                    };
                }
                return {
                    classes: '',
                    css: { 'word-wrap': 'break-word', 'width': '170px'}
                };
            };
            
            var _cellNowrap2 = function (value, row, index, field) {
                return {
                    classes: '',
                    css: {'width': '150px'}
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

            var _formatterLetterDocPriority = function (value, row, index) {
            	if (!value) {
                    if (row.LetterDocPriority !== undefined) {
                        return row.LetterDocPriority;
                    }
                    return '';
                }
                return value.name;
            }
            var _formatterDocument = function (value, row, index) {
                return value.currentStep.name;
            }
            
            return [
                {
                    field: 'docOriginCode',
                    title: 'Số hiệu văn bản',
            		// sortable: true,
            		switchable: true,
		            visible: true,
                    formatter: _tableOperation1,
                    cellStyle: _cellNowrap3
                }
                , {
                    field: 'title',
                    title: 'Tiêu đề',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation1,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'issuedDate',
                    title: 'Ngày ban hành',
                    switchable: true,
                    visible: true,
                    formatter: _dateFormatter,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'registeredDate',
                    title: 'Ngày đăng',
                    // sortable: true,
                    switchable: true,
                    visible: true,
                    formatter: _dateFormatter,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'issueOrgan',
                    title: 'Nơi gửi',
                    switchable: true,
                    visible: true,
                    formatter: _issueaOrganFormatter,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'task',
                    title: 'Tình trạng văn bản',
                    switchable: true,
                    visible: true,
                    formatter: _formatterDocument,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'letterDocPriority',
                    title: 'Độ khẩn',
                    switchable: true,
                    visible: true,
                    formatter: _formatterLetterDocPriority,
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
//                , {
//                    field: '',
//                    title: 'Tài liệu',
//                    switchable: true,
//                    visible: true,
//                    formatter: _downloadDocument,
//                    cellStyle: _cellNowrap2
//                }
            ]
        }

        function getTableDefinitionReturn() {

            var _downloadDocument = function (value, row, index) {
                if (!value) {
                    if (row.attachments!=null) {
                    	var str = "";
                        str += '<a class="green-dark margin-right-10" title="Xem tài liệu đính kèm" href="#" data-ng-click="$parent.viewAllFileInDocument(' + "'" + row.id + "'" + ')"><img src="assets/images/letter_management/view_documet.png" /></a>'
                        return str;
                    }
                    return 'abc';
                }
                return value.name;
            };
            var _tableOperation = function (value, row, index) {
                // return '<a class="green-dark margin-right-20"  ng-show="'+ "'" + row.task.currentStep.code + "'" + ' === ' + "'LetterInCodeStep1'" +'" href="#" data-ng-click="$parent.editDocument(' + "'" + row.id + "'" + ')"><i class="icon-pencil margin-right-5"></i> Sửa</a>'
                //     +  '<a class="green-dark margin-right-20"  ng-show="'+ "'" + row.task.currentStep.code + "'" + ' === ' + "'LetterInCodeStep1'" +'" href="#" data-ng-click="$parent.forwardLetter(' + "'" + row.id + "'" + ')"><i class="fa fa-mail-forward margin-right-5"></i> Chuyển phân luồng</a>'
                //     +  '<a class="green-dark margin-right-20"   ng-show="'+ "'" + row.task.currentStep.code + "'" + ' === ' + "'LetterInCodeStep2'" +'" href="#" data-ng-click="$parent.forwardLetterStepTwo(' + "'" + row.id + "'" + ')"><i class="fa fa-forward" aria-hidden="true"></i> Phân luồng</a>'
                //     +  '<a class="green-dark margin-right-20"   ng-show="'+ "'" + row.task.currentStep.code + "'" + ' === ' + "'LetterInCodeStep3'" +'" href="#" data-ng-click="$parent.assignLetter(' + "'" + row.id + "'" + ')"><i class="fa fa-cog" aria-hidden="true"></i> Giao xử lý</a>';

                var hardCodeCodition = false;
                var hardCodeClerck = true;
                var str = "";

                // xem hồ sơ
                str += '<a class="green-dark margin-right-10" title="Xem tình trạng hồ sơ" href="#" data-ng-click="$parent.viewDocumentPopup(' + "'" + row.id + "'" + ')"><img src="assets/images/letter_management/document.png" /></a>'

                // Phân quyền đọc văn bản
                if (hardCodeCodition) {
                    str += '<a class="green-dark margin-right-10" title="Phân quyền đọc văn bản"><img src="assets/images/letter_management/share.png" /></a>'
                }
                else {
                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Phân quyền đọc văn bản" ><img src="assets/images/letter_management/share_disabled.png" /></a></span>'
                }

                // Trình Hiệu trưởng
                /*
                if (hardCodeCodition) {
                    str += '<a class="green-dark margin-right-10" title="Trình Hiệu trưởng"><img src="assets/images/letter_management/forward.png" /></a>'
                }
                else {
                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Trình Hiệu trưởng" ><img src="assets/images/letter_management/forward_disabled.png" /></a></span>'
                }
				*/
				
                // Trả lại văn thư
                if (row.task.currentStep.code === "LetterInCodeStep2" && (row.hasFowardRole)) {
                    //str += '<a class="green-dark margin-right-10" ng-click="$parent.returnBack(' + "'" + row.id + "'" + ')" title="Trả lại văn thư 1"><img src="assets/images/letter_management/back.png" /></a>'
                    str += '<a class="green-dark margin-right-10" title="Trả lại văn thư" href="#" data-ng-click="$parent.returnBack(' + "'" + row.id + "'" + ')"><img src="assets/images/letter_management/back.png" /></a>'
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
                if ((row.task.currentStep.code === "LetterInCodeStep3" && (row.hasAssignerRole)) || (row.task.currentStep.code === "LetterInCodeStep2" && (row.hasFowardRole)) || (row.task.currentStep.code === "LetterInCodeStep3" && (row.hasFowardRole))) {
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
                    str += '<a class="green-dark margin-right-10" title="Sửa thông tin văn bản" href="#" data-ng-click="$parent.editDocument(' + "'" + row.id + "'" + ')"><i class="fa fa-pencil"></i></a>'
                    str += '<a class="green-dark margin-right-10" title="Chuyển phân luồng" href="#" data-ng-click="$parent.forwardLetter(' + "'" + row.id + "'" + ')"><i class="fa fa-mail-forward"></i></a>';
                }
                else {
                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Sửa thông tin văn bản" ><i class="fa fa-pencil5"></i></a></span>'
                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Chuyển phân luồng" ><i class="fa fa-mail-forward"></i></a></span>'
                }

                // phân luồng
//                if (row.task.currentStep.code === "LetterInCodeStep2" && (row.hasFowardRole)) {
//                    str += '<a class="green-dark margin-right-10" title="Phân luồng" href="#" data-ng-click="$parent.forwardLetterStepTwo(' + "'" + row.id + "'" + ')"><i class="fa fa-forward" aria-hidden="true"></i></a>'
//                }
//                else {
//                    str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Phân luồng" ><i class="fa fa-forward margin-right-5"></i></a></span>'
//                }
                if ($rootScope.hasClerkRole) {
                    str += '<a class="green-dark margin-right-10" title="Kiểm người đã xem" href="#" data-ng-click="$parent.checkViewDocument(' + "'" + row.id + "'" + ')"><i class="fa fa-user-circle"></i></a>';
                }
                return str;
            };

            var _tableOperation1 = function (value, row, index) {
                return '<a class="green-dark" title="' + row.briefNote + '" href="#" data-ng-click="$parent.viewDocument(' + "'" + row.id + "'" + ')"><span style="font-size: 15px">' + value + '<span></a>';
                /*return '<a class="green-dark" title="' + value + '" href="#" data-ng-click="$parent.viewAllFileInDocument(' + "'" + row.id + "'" + ')"><span style="font-size: 15px">' + value + '<span></a>';*/
            };

            var _cellNowrap = function (value, row, index, field) {
//                if (row.task.currentStep.code === "LetterInCodeStep1") {
//                    return {
//                        classes: '',
//                        css: { 'word-wrap': 'break-word', 'font-weight': 'bold' }
//                    };
//                }
                return {
                    classes: '',
                    css: { 'word-wrap': 'break-word' }
                };
            };
            
            var _cellNowrap2 = function (value, row, index, field) {
                return {
                    classes: '',
                    css: {'width': '150px'}
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

            var _formatterLetterDocPriority = function (value, row, index) {
            	if (!value) {
                    if (row.LetterDocPriority !== undefined) {
                        return row.LetterDocPriority;
                    }
                    return '';
                }
                return value.name;
            }
            var _cellNowrap3 = function (value, row, index, field) {
//                if (row.task.currentStep.code === "LetterInCodeStep1") {
//                    return {
//                        classes: '',
//                        css: { 'word-wrap': 'break-word', 'font-weight': 'bold', 'width': '170px'}
//                    };
//                }
                return {
                    classes: '',
                    css: { 'word-wrap': 'break-word', 'width': '170px'}
                };
            };
            return [
            	{
                    field: 'docOriginCode',
                    title: 'Số hiệu văn bản',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation1,
                    cellStyle: _cellNowrap3
                }
                , {
                    field: 'title',
                    title: 'Tiêu đề',
                    switchable: true,
                    visible: true,
                    formatter: _tableOperation1,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'issuedDate',
                    title: 'Ngày ban hành',
                    switchable: true,
                    visible: true,
                    formatter: _dateFormatter,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'registeredDate',
                    title: 'Ngày đăng',
                    // sortable: true,
                    switchable: true,
                    visible: true,
                    formatter: _dateFormatter,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'issueOrgan',
                    title: 'Nơi gửi',
                    switchable: true,
                    visible: true,
                    formatter: _issueaOrganFormatter,
                    cellStyle: _cellNowrap
                }
                , {
                    field: 'letterDocPriority',
                    title: 'Độ khẩn',
                    switchable: true,
                    visible: true,
                    formatter: _formatterLetterDocPriority,
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
//                , {
//                    field: '',
//                    title: 'Tài liệu',
//                    switchable: true,
//                    visible: true,
//                    formatter: _downloadDocument,
//                    cellStyle: _cellNowrap2
//                }
//                {
//                    field: 'docOriginCode',
//                    title: 'Số hiệu văn bản',
//                    // sortable: true,
//                    switchable: true,
//                    visible: true,
//                    formatter: _tableOperation1,
//                    cellStyle: _cellNowrap
//                }
//                , {
//                    field: 'briefNote',
//                    title: 'Trích yếu',
//                    switchable: true,
//                    visible: true,
//                    formatter: _tableOperation1,
//                    cellStyle: _cellNowrap
//                }
//                , {
//                    field: 'deliveredDate',
//                    title: 'Ngày đến',
//                    switchable: true,
//                    visible: true,
//                    formatter: _dateFormatter,
//                    cellStyle: _cellNowrap
//                }
//                , {
//                    field: 'expiredDate',
//                    title: 'Hạn trả lời',
//                    switchable: true,
//                    visible: true,
//                    formatter: _dateFormatter,
//                    cellStyle: _cellNowrap
//                },
//                {
//                    field: 'issueOrgan',
//                    title: 'Nơi gửi',
//                    switchable: true,
//                    visible: true,
//                    formatter: _issueaOrganFormatter,
//                    cellStyle: _cellNowrap
//                },
//                {
//                    field: '',
//                    title: 'Lãnh Đạo',
//                    switchable: true,
//                    visible: true,
//                    formatter: _formatterName,
//                    cellStyle: _cellNowrap
//                }
//                , {
//                    field: '',
//                    title: 'Thao tác',
//                    switchable: true,
//                    visible: true,
//                    formatter: _tableOperation,
//                    cellStyle: _cellNowrap2
//                }
//                , {
//                    field: '',
//                    title: 'Tài liệu',
//                    switchable: true,
//                    visible: true,
//                    formatter: _downloadDocument,
//                    cellStyle: _cellNowrap2
//                }
            ]
        }
        /** upload file */
        function getTableAttachmentFileDefinition() {
            var _tableOperation = function (value, row, index) {
                var ret = '';
                ret += '<a class="text-danger margin-right-10" uib-tooltip="Xóa tệp tin" tooltip-trigger="mouseenter" href="#" data-ng-click="$parent.deleteDocument(' + "'" + index + "'" + ')"><i class="icon-trash"></i></a>';
                ret += '<a class="green-dark margin-right-10" uib-tooltip="Tải về" tooltip-trigger="mouseenter" href="#" data-ng-click="$parent.downloadDocument(' + "'" + index + "'" + ')"><i class="icon-cloud-download"></i></a>';
                ret += '<a class="green-dark" uib-tooltip="Xem tài liệu" tooltip-trigger="mouseenter" href="#" data-ng-click="$parent.viewDocument(' + "'" + index + "'" + ')"><i class="icon-info"></i></a>';
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
                    // // sortable: true,
                    switchable: false,
                    cellStyle: _operationColStyle
                }
                , {
                    field: 'file.contentSize',
                    title: 'Kích thước tệp tin',
                    // sortable: true,
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
                ret += '<a class="green-dark margin-right-10" uib-tooltip="Tải về tài liệu" tooltip-trigger="mouseenter" href="#" data-ng-click="$parent.downloadDocument(' + "'" + index + "'" + ')"><i class="icon-cloud-download"></i></a>';
                ret += '<a class="green-dark" uib-tooltip="Xem tài liệu" tooltip-trigger="mouseenter" href="#" data-ng-click="$parent.viewDocument(' + "'" + index + "'" + ')"><i class="icon-info"></i></a>';
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
                    // sortable: true,
                    switchable: false,
                    cellStyle: _operationColStyle
                }
                , {
                    field: 'file.contentSize',
                    title: 'Kích thước tệp tin',
                    // sortable: true,
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
                str += '<a class="green-dark margin-right-10" title="Xem tình trạng hồ sơ" href="#" data-ng-click="$parent.viewDocumentOutPopup(' + "'" + row.id + "'" + ')"><img src="assets/images/letter_management/document.png" /></a>'
                
//                if (row.task.currentStep.code === 'LETTEROUTSTEP1' && row.hasClerkRole) {
//                    str += '<a class="green-dark margin-right-10" title="Sửa hồ sơ" href="#" data-ng-click="$parent.editDocument(' + "'" + row.id + "'" + ')"><i class="fa fa-pencil"></i> </a>'
//                    str += '<a class="green-dark margin-right-10" title="Chuyển lãnh đạo phòng" href="#" data-ng-click="$parent.forwardLetter(' + "'" + row.id + "'" + ')"><i class="fa fa-mail-forward"></i></a>';
//				}
                if ((row.task.currentStep.code === 'LETTEROUTSTEP2' && row.hasManagerRole) || (row.task.currentStep.code === 'LETTEROUTSTEP2' && row.hasDraftersRole)) {
                	str += '<a class="green-dark margin-right-10" title="Sửa thông tin văn bản" href="#" data-ng-click="$parent.editDocument(' + "'" + row.id + "'" + ')"><i class="fa fa-pencil"></i></a>';
				}
                if ((row.task.currentStep.code === 'LETTEROUTSTEP2' && row.hasManagerRole)) {
                    str += '<a class="green-dark margin-right-10" title="Chuyển lãnh đạo ký và xử lý" href="#" data-ng-click="$parent.transferLeader(' + "'" + row.id + "'" + ')"><i class="fa fa-arrow-up"></i></a>';
				}
				if (row.task.currentStep.code === 'LETTEROUTSTEP3' && row.hasFowardRole) {
					str += '<a class="green-dark margin-right-10" title="Trả lại dự thảo" href="#" data-ng-click="$parent.returnBack(' + "'" + row.id + "'" + ')"><img src="assets/images/letter_management/back.png" /></a>';
                    str += '<a class="green-dark margin-right-10" title="Xử lý & Ký duyệt" href="#" data-ng-click="$parent.checkLetter(' + "'" + row.id + "'" + ')"><i class="fa fa-edit"></i></a>';
				}
				if (row.task.currentStep.code === 'LETTEROUTSTEP4' && row.hasClerkRole) {
                    str += '<a class="green-dark margin-right-10" title="Vào sổ văn bản" href="#" data-ng-click="$parent.editDocument(' + "'" + row.id + "'" + ')"><i class="fa fa-edit"></i></a>';
				}
				if ($rootScope.hasClerkRole) {
                    str += '<a class="green-dark margin-right-10" title="Kiểm người đã xem" href="#" data-ng-click="$parent.checkViewDocument(' + "'" + row.id + "'" + ')"><i class="fa fa-user-circle"></i></a>';
                }
                return str;
            };
            var _downloadDocument = function (value, row, index) {
                if (!value) {
                    if (row.attachments!=null) {
                    	var str = "";
                        str += '<a class="green-dark margin-right-10" title="Xem tài liệu đính kèm" href="#" data-ng-click="$parent.viewAllFileInDocument(' + "'" + row.id + "'" + ')"><img src="assets/images/letter_management/view_documet.png" /></a>'
                        return str;
                    }
                    return 'abc';
                }
                return value.name;
            };
            var _tableOperation1 = function (value, row, index) {
                return '<a class="green-dark" title="' + row.briefNote + '" href="#" data-ng-click="$parent.viewDocument(' + "'" + row.id + "'" + ')"><span style="font-size: 15px">' + value + '<span></a>';
            };
            var _cellNowrap = function (value, row, index, field) {
//            	if (row.task.currentStep.code === "LETTEROUTSTEP1") {
//                    return {
//                        classes: '',
//                        css: { 'word-wrap': 'break-word', 'font-weight': 'bold' }
//                    };
//                }
                return {
                    classes: '',
                    css: { 'word-wrap': 'break-word' }
                };
            };
            var _cellNowrap2 = function (value, row, index, field) {
                return {
                    classes: '',
                    css: {'width': '150px'}
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
                    return row.otherIssueOrgan;
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
            
            var _formatterLetterDocPriority = function (value, row, index) {
            	if (!value) {
                    if (row.LetterDocPriority !== undefined) {
                        return row.LetterDocPriority;
                    }
                    return '';
                }
                return value.name;
            }
            var _cellNowrap3 = function (value, row, index, field) {
//            	if (row.task.currentStep.code === "LETTEROUTSTEP1") {
//                    return {
//                        classes: '',
//                        css: { 'word-wrap': 'break-word', 'font-weight': 'bold' }
//                    };
//                }
                return {
                    classes: '',
                    css: { 'word-wrap': 'break-word', 'width': '170px'}
                };
            };
            return [
//            	{
//            		field: 'docOriginCode',
//            		title: 'Số hiệu văn bản',
//            		switchable: true,
//		            visible: true,
//		            formatter: _tableOperation1,
//		            cellStyle: _cellNowrap3
//		        },
	            {
		        	field: 'registeredDate',
		        	title: 'Ngày đăng',
		        	switchable: true,
		        	visible: true,
		        	formatter: _dateFormatter,
	              	cellStyle: _cellNowrap
	            },
	            {
	            	field: 'title',
	            	title: 'Tiêu đề',
	            	switchable: true,
	            	visible: true,
	            	formatter: _tableOperation1,
	            	cellStyle: _cellNowrap
	            },
	            {
                    field: 'letterDocPriority',
                    title: 'Độ khẩn',
                    switchable: true,
                    visible: true,
                    formatter: _formatterLetterDocPriority,
                    cellStyle: _cellNowrap
                },
//                {
//                	field: 'issueOrgan',
//                	title: 'Cơ quan tiếp nhận',
//                	switchable: true,
//                	visible: true,
//                	formatter: _issueaOrganFormatter,
//                	cellStyle: _cellNowrap
//                },
                {
                	field: '',
                	title: 'Thao tác',
                	switchable: true,
                	visible: true,
                	formatter: _tableOperation,
                	cellStyle: _cellNowrap2
                }
//                ,{
//                    field: '',
//                    title: 'Tài liệu',
//                    switchable: true,
//                    visible: true,
//                    formatter: _downloadDocument,
//                    cellStyle: _cellNowrap2
//                }
//                {
//                    field: 'docOriginCode',
//                    title: 'Số hiệu văn bản',
//                    // sortable: true,
//                    switchable: true,
//                    visible: true,
//                    formatter: _tableOperation1,
//                    cellStyle: _cellNowrap
//                }, 
//                {
//                    field: 'briefNote',
//                    title: 'Trích yếu',
//                    switchable: true,
//                    visible: true,
//                    formatter: _tableOperation1,
//                    cellStyle: _cellNowrap
//                }, 
//                {
//                    field: 'deliveredDate',
//                    title: 'Ngày đến',
//                    switchable: true,
//                    visible: true,
//                    formatter: _dateFormatter,
//                    cellStyle: _cellNowrap
//                }, 
//                {
//                    field: 'expiredDate',
//                    title: 'Hạn trả lời',
//                    switchable: true,
//                    visible: true,
//                    formatter: _dateFormatter,
//                    cellStyle: _cellNowrap
//                },
//                {
//                	field: 'issueOrgan',
//                	title: 'Cơ quan tiếp nhận',
//                	switchable: true,
//                	visible: true,
//                	formatter: _issueaOrganFormatter,
//                	cellStyle: _cellNowrap
//                },
//                {
//                	field: '',
//                	title: 'Thao tác',
//                	switchable: true,
//                	visible: true,
//                	formatter: _tableOperation,
//                	cellStyle: _cellNowrap2
//                }
            ]
        }
        


        function getTableDefinitionFinishLetterOut() {

            var _tableOperation = function (value, row, index) {
            	var str = "";

                // xem hồ sơ
                str += '<a class="green-dark margin-right-10" title="Xem tình trạng hồ sơ" href="#" data-ng-click="$parent.viewDocumentOutPopup(' + "'" + row.id + "'" + ')"><img src="assets/images/letter_management/document.png" /></a>'
                
//                if (row.task.currentStep.code === 'LETTEROUTSTEP1' && row.hasClerkRole) {
//                    str += '<a class="green-dark margin-right-10" title="Sửa hồ sơ" href="#" data-ng-click="$parent.editDocument(' + "'" + row.id + "'" + ')"><i class="fa fa-pencil"></i> </a>'
//                    str += '<a class="green-dark margin-right-10" title="Chuyển lãnh đạo phòng" href="#" data-ng-click="$parent.forwardLetter(' + "'" + row.id + "'" + ')"><i class="fa fa-mail-forward"></i></a>';
//				}
                if (row.task.currentStep.code === 'LETTEROUTSTEP2' && row.hasManagerRole) {
                    str += '<a class="green-dark margin-right-10" title="Chuyển lãnh đạo ký và xử lý" href="#" data-ng-click="$parent.transferLeader(' + "'" + row.id + "'" + ')"><i class="fa fa-arrow-up"></i></a>';
				}	
				if (row.task.currentStep.code === 'LETTEROUTSTEP3' && row.hasFowardRole) {
                    str += '<a class="green-dark margin-right-10" title="Xử lý & Ký duyệt" href="#" data-ng-click="$parent.checkLetter(' + "'" + row.id + "'" + ')"><i class="fa fa-edit"></i></a>';
				}
				if (row.task.currentStep.code === 'LETTEROUTSTEP4' && row.hasClerkRole) {
                    str += '<a class="green-dark margin-right-10" title="Vào sổ văn bản" href="#" data-ng-click="$parent.editDocument(' + "'" + row.id + "'" + ')"><i class="fa fa-edit"></i></a>';
				}
				if ($rootScope.hasClerkRole) {
                    str += '<a class="green-dark margin-right-10" title="Kiểm người đã xem" href="#" data-ng-click="$parent.checkViewDocument(' + "'" + row.id + "'" + ')"><i class="fa fa-user-circle"></i></a>';
                }
                return str;
            };
            var _downloadDocument = function (value, row, index) {
                if (!value) {
                    if (row.attachments!=null) {
                    	var str = "";
                        str += '<a class="green-dark margin-right-10" title="Xem tài liệu đính kèm" href="#" data-ng-click="$parent.viewAllFileInDocument(' + "'" + row.id + "'" + ')"><img src="assets/images/letter_management/view_documet.png" /></a>'
                        return str;
                    }
                    return 'abc';
                }
                return value.name;
            };
            var _tableOperation1 = function (value, row, index) {
                return '<a class="green-dark" title="' + row.briefNote + '" href="#" data-ng-click="$parent.viewDocument(' + "'" + row.id + "'" + ')"><span style="font-size: 15px">' + value + '<span></a>';
            };
            var _cellNowrap = function (value, row, index, field) {
//            	if (row.task.currentStep.code === "LETTEROUTSTEP1") {
//                    return {
//                        classes: '',
//                        css: { 'word-wrap': 'break-word', 'font-weight': 'bold' }
//                    };
//                }
                return {
                    classes: '',
                    css: { 'word-wrap': 'break-word' }
                };
            };
            var _cellNowrap2 = function (value, row, index, field) {
                return {
                    classes: '',
                    css: {'width': '150px'}
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
                    return row.otherIssueOrgan;
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
            
            var _formatterLetterDocPriority = function (value, row, index) {
            	if (!value) {
                    if (row.LetterDocPriority !== undefined) {
                        return row.LetterDocPriority;
                    }
                    return '';
                }
                return value.name;
            }
            var _cellNowrap3 = function (value, row, index, field) {
//            	if (row.task.currentStep.code === "LETTEROUTSTEP1") {
//                    return {
//                        classes: '',
//                        css: { 'word-wrap': 'break-word', 'font-weight': 'bold' }
//                    };
//                }
                return {
                    classes: '',
                    css: { 'word-wrap': 'break-word', 'width': '170px'}
                };
            };
            return [
            	{
            		field: 'docOriginCode',
            		title: 'Số hiệu văn bản',
            		switchable: true,
		            visible: true,
		            formatter: _tableOperation1,
		            cellStyle: _cellNowrap3
		        },
	            {
		        	field: 'registeredDate',
		        	title: 'Ngày đăng',
		        	switchable: true,
		        	visible: true,
		        	formatter: _dateFormatter,
	              	cellStyle: _cellNowrap
	            },
	            {
	            	field: 'title',
	            	title: 'Tiêu đề',
	            	switchable: true,
	            	visible: true,
	            	formatter: _tableOperation1,
	            	cellStyle: _cellNowrap
	            },
	            {
                    field: 'letterDocPriority',
                    title: 'Độ khẩn',
                    switchable: true,
                    visible: true,
                    formatter: _formatterLetterDocPriority,
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
//                ,{
//                    field: '',
//                    title: 'Tài liệu',
//                    switchable: true,
//                    visible: true,
//                    formatter: _downloadDocument,
//                    cellStyle: _cellNowrap2
//                }
//                {
//                    field: 'docOriginCode',
//                    title: 'Số hiệu văn bản',
//                    // sortable: true,
//                    switchable: true,
//                    visible: true,
//                    formatter: _tableOperation1,
//                    cellStyle: _cellNowrap
//                }, 
//                {
//                    field: 'briefNote',
//                    title: 'Trích yếu',
//                    switchable: true,
//                    visible: true,
//                    formatter: _tableOperation1,
//                    cellStyle: _cellNowrap
//                }, 
//                {
//                    field: 'deliveredDate',
//                    title: 'Ngày đến',
//                    switchable: true,
//                    visible: true,
//                    formatter: _dateFormatter,
//                    cellStyle: _cellNowrap
//                }, 
//                {
//                    field: 'expiredDate',
//                    title: 'Hạn trả lời',
//                    switchable: true,
//                    visible: true,
//                    formatter: _dateFormatter,
//                    cellStyle: _cellNowrap
//                },
//                {
//                	field: 'issueOrgan',
//                	title: 'Cơ quan tiếp nhận',
//                	switchable: true,
//                	visible: true,
//                	formatter: _issueaOrganFormatter,
//                	cellStyle: _cellNowrap
//                },
//                {
//                	field: '',
//                	title: 'Thao tác',
//                	switchable: true,
//                	visible: true,
//                	formatter: _tableOperation,
//                	cellStyle: _cellNowrap2
//                }
            ]
        }
        
        function getTableAllLetterDocument() {

            var _tableOperation = function (value, row, index) {
            	var str = "";
                // xem hồ sơ
            	if (row.typeOfClass == 0) {
            		str += '<a class="green-dark margin-right-10" title="Xem tình trạng hồ sơ" href="#" data-ng-click="$parent.viewDocumentIn(' + "'" + row.id + "'" + ')"><img src="assets/images/letter_management/document.png" /></a>'
            		//            		if (row.task.currentStep.code === "LetterInCodeStep1") {
//                        str += '<a class="green-dark margin-right-10" title="Sửa thông tin văn bản" href="#" data-ng-click="$parent.editDocumentIn(' + "'" + row.id + "'" + ')"><i class="fa fa-pencil"></i></a>';
//                    }
//                    else {
//                        str += '<span class="isDisabled"><a class="green-dark margin-right-10" title="Sửa thông tin văn bản" ><i class="fa fa-pencil5"></i></a></span>';
//                    }
            	}
            	if (row.typeOfClass == 1) {
            		str += '<a class="green-dark margin-right-10" title="Xem tình trạng hồ sơ" href="#" data-ng-click="$parent.viewDocumentOut(' + "'" + row.id + "'" + ')"><img src="assets/images/letter_management/document.png" /></a>'
//            		if (row.task.currentStep.code === 'LETTEROUTSTEP1') {
//                        str += '<a class="green-dark margin-right-10" title="Sửa hồ sơ" href="#" data-ng-click="$parent.editDocumentOut(' + "'" + row.id + "'" + ')"><i class="fa fa-pencil"></i> </a>';
//    				}
            	}
            	if ($rootScope.hasClerkRole) {
                    str += '<a class="green-dark margin-right-10" title="Kiểm người đã xem" href="#" data-ng-click="$parent.checkViewDocument(' + "'" + row.id + "'" + ')"><i class="fa fa-user-circle"></i></a>';
                }
                return str;
            };
            var _downloadDocument = function (value, row, index) {
                if (!value) {
                    if (row.attachments!=null) {
                    	var str = "";
                        str += '<a class="green-dark margin-right-10" title="Xem tài liệu đính kèm" href="#" data-ng-click="$parent.viewAllFileInDocument(' + "'" + row.id + "'" + ')"><img src="assets/images/letter_management/view_documet.png" /></a>'
                        return str;
                    }
                    return 'abc';
                }
                return value.name;
            };
            var _tableOperation1 = function (value, row, index) {
            	if(value == null){
            		if (row.typeOfClass == 0) {
            			return '<a class="green-dark" title="' + row.briefNote + '" href="#" data-ng-click="$parent.viewDocumentInPopup(' + "'" + row.id + "'" + ')"><span style="font-size: 15px"></span></a>';
                	}
                	if (row.typeOfClass == 1) {
                		return '<a class="green-dark" title="' + row.briefNote + '" href="#" data-ng-click="$parent.viewDocumentOutPopup(' + "'" + row.id + "'" + ')"><span style="font-size: 15px;"></span></a>';
                    }
                
            	}else if(row.task.currentStep != null) {
                    //xác định văn bản đã hoàn thành hay chưa?
                    if( row.task.currentStep.code ==="LetterInCodeStep1" || row.task.currentStep.code === "LetterInCodeStep2" || row.task.currentStep.code === "LetterInCodeStep4" || row.task.currentStep.code === "LetterInCodeStep3"){
                        return '<span style="background-color:  #ffff80; font-weight:bold;">[Chưa hoàn thành]</span><a class="green-dark" title="' + row.briefNote + '" href="#" data-ng-click="$parent.viewDocumentInPopup(' + "'" + row.id + "'" + ')"><span style="font-size: 15px">'+value+'</span></a>';
                    }
            		if (row.typeOfClass == 0) {//Văn bản đã xem
            			return '<a class="green-dark" title="' + row.briefNote + '" href="#" data-ng-click="$parent.viewDocumentInPopup(' + "'" + row.id + "'" + ')"><span style="font-size: 15px">'+value+'</span></a>';
                	}
                	if (row.typeOfClass == 1) {//Văn bản chưa xem
                		return '<a class="green-dark" title="' + row.briefNote + '" href="#" data-ng-click="$parent.viewDocumentOutPopup(' + "'" + row.id + "'" + ')"><span style="font-size: 15px;">'+value+'</span></a>';
                	}
            	}
            };
            var _cellNowrap = function (value, row, index, field) {
            	if(row.task.currentStep != null){
            		if (row.task.currentStep.code === "LETTEROUTSTEP1" || row.task.currentStep.code === "LetterInCodeStep5") {
                        
                    }else{
                    	return {
                            classes: '',
                            css: { 'word-wrap': 'break-word'}
                        };
                    }
            	}
                return {
                    classes: '',
                    css: { 'word-wrap': 'break-word' }
                };
            };
            var _cellNowrap2 = function (value, row, index, field) {
                return {
                    classes: '',
                    css: {'width': '100px'}
                };
            };
            var _cellNowrap4 = function (value, row, index, field) {
                return {
                    classes: '',
                    css: {'width': '100px'}
                };
            };
            var _cellNowrap3 = function (value, row, index, field) {
//            	if (row.task.currentStep.code === "LETTEROUTSTEP1" || row.task.currentStep.code === "LetterInCodeStep1") {
//                    return {
//                        classes: '',
//                        css: { 'width': '200px' , 'word-wrap': 'break-word', 'font-weight': 'bold' }
//                    };
//                }
                return {
                    classes: '',
                    css: {'width': '170px'}
                };
            };
            
            var _formatTypeOfClass = function (value, row, index) {
                    if (row.typeOfClass == 0) {
                        return 'Văn bản đến';
                    }
                    if (row.typeOfClass == 1){
                    	return 'Văn bản đi';
                    }
            }
            var _dateFormatter = function (value, row, index) {
//                if (!value) {
//                    return '';
//                }
            	return moment(value).format('DD/MM/YYYY');
//                return moment(value.createDate).format('DD/MM/YYYY');
            };
            return [
            	{
            		field: 'docOriginCode',
            		title: 'Số hiệu văn bản',
            		switchable: true,
		            visible: true,
		            cellStyle: _cellNowrap3
		        },
	            {
	            	field: 'typeOfClass',
	            	title: 'Loại văn bản',
	            	switchable: true,
	            	visible: true,
	            	formatter: _formatTypeOfClass,
	            	cellStyle: _cellNowrap4
	            },
	            {
	            	field: 'title',
	            	title: 'Tiêu đề',
	            	switchable: true,
	            	visible: true,
	            	formatter: _tableOperation1,
	            	cellStyle: _cellNowrap
	            },
	            {
	            	field: 'registeredDate',
	            	title: 'Ngày đăng',
	            	switchable: true,
	            	visible: true,
	            	formatter: _dateFormatter,
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
//	            ,{
//                    field: '',
//                    title: 'Tài liệu',
//                    switchable: true,
//                    visible: true,
//                    formatter: _downloadDocument,
//                    cellStyle: _cellNowrap2
//                }
            ]
        }
        
    }
})();