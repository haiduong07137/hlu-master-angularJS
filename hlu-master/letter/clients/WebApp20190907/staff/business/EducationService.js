(function () {
    'use strict';

    angular.module('Hrm.Staff').service('EducationService', EducationService);

    EducationService.$inject = [
        '$http',
        '$q',
        '$filter',
        'settings',
        'Utilities'
    ];

    function EducationService($http, $q, $filter, settings, utils) {
        var self = this;
        var baseUrl = settings.api.baseUrl + settings.api.apiV1Url;



        self.getEducationHistoryPages = getEducationHistoryPages;
        self.getEducationHistoryAll = getEducationHistoryAll;
        self.getEducationHistoryById = getEducationHistoryById;
        self.saveEducationHistory = saveEducationHistory;
        self.removeEducationHistory = removeEducationHistory; // theo Id
        //self.removeLists = removeLists;
        //self.getTableDefinition = getTableDefinition;

        var restUrl = 'education';


        function getEducationHistoryAll(id){
            if (!id ) {
                return $q.when(null);
            }

            var url = baseUrl + restUrl+'/getall' + '/'+ id ;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }

        function getEducationHistoryPages(pageIndex, pageSize) {
            
            var url = baseUrl + 'education/'+pageIndex+'/'+pageSize;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        
        function saveEducationHistory(education, successCallback, errorCallback) {
            var url = baseUrl + restUrl + '/create';

            return utils.resolveAlt(url, 'POST', null, education, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        

        function getEducationHistoryById(id) {
            if (!id ) {
                return $q.when(null);
            }

            var url = baseUrl +'/'+  restUrl+'/' + id ;
            return utils.resolve(url, 'GET', angular.noop, angular.noop);
        }
        function removeEducationHistory(id, successCallback, errorCallback) {
           
            var url = baseUrl+  restUrl + '/delete'+ '/' + id;
            return utils.resolveAlt(url, 'DELETE', null, id, {
                'Content-Type': 'application/json; charset=utf-8'
            }, successCallback, errorCallback);
        }
        var _dateFormatter = function (value, row, index) {
            if (!value) {
                return '';
            }
            return moment(value).format('DD/MM/YYYY');
        };

       

        

    }
})();