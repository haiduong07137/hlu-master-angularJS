/***
 GLobal Directives
 ***/
(function () {
    'use strict';

    var Hrm = angular.module('Hrm');

    // Route State Load Spinner(used on page or content load)
    // Hrm.directive('ngSpinnerBar', ['$rootScope',
    //     function ($rootScope) {
    //         return {
    //             link: function (scope, element, attrs) {
    //
    //                 // by defult hide the spinner bar
    //                 element.addClass('hide'); // hide spinner bar by default
    //
    //                 // display the spinner bar whenever the route changes(the content part started loading)
    //                 $rootScope.$on('$stateChangeStart', function () {
    //                     element.removeClass('hide'); // show spinner bar
    //                 });
    //
    //                 // hide the spinner bar on rounte change success(after the content loaded)
    //                 $rootScope.$on('$stateChangeSuccess', function () {
    //                     element.addClass('hide'); // hide spinner bar
    //                     $('body').removeClass('page-on-load'); // remove page loading indicator
    //
    //                     Layout.setSidebarMenuActiveLink('match'); // activate selected link in the sidebar menu
    //
    //                     // auto scorll to page top
    //                     setTimeout(function () {
    //                         App.scrollTop(); // scroll to the top on content load
    //                     }, $rootScope.settings.layout.pageAutoScrollOnLoad);
    //                 });
    //
    //                 // handle errors
    //                 $rootScope.$on('$stateNotFound', function () {
    //                     element.addClass('hide'); // hide spinner bar
    //                 });
    //
    //                 // handle errors
    //                 $rootScope.$on('$stateChangeError', function () {
    //                     element.addClass('hide'); // hide spinner bar
    //                 });
    //
    //                 // handle errors
    //                 $rootScope.$on('$unauthorized', function () {
    //                     element.addClass('hide'); // hide spinner bar
    //                 });
    //             }
    //         };
    //     }
    // ]);

    Hrm.directive('ngSpinnerBar', ['$rootScope', '$state',
        function ($rootScope, $state) {
            return {
                link: function (scope, element, attrs) {
                    // by defult hide the spinner bar
                    element.addClass('hide'); // hide spinner bar by default

                    // display the spinner bar whenever the route changes(the content part started loading)
                    $rootScope.$on('$stateChangeStart', function () {
                        element.removeClass('hide'); // show spinner bar
                        Layout.closeMainMenu();
                    });

                    // hide the spinner bar on rounte change success(after the content loaded)
                    $rootScope.$on('$stateChangeSuccess', function () {
                        element.addClass('hide'); // hide spinner bar
                        $('body').removeClass('page-on-load'); // remove page loading indicator
                        Layout.setAngularJsMainMenuActiveLink('match', null, $state); // activate selected link in the sidebar menu

                        // auto scorll to page top
                        setTimeout(function () {
                            App.scrollTop(); // scroll to the top on content load
                        }, $rootScope.settings.layout.pageAutoScrollOnLoad);
                    });

                    // handle errors
                    $rootScope.$on('$stateNotFound', function () {
                        element.addClass('hide'); // hide spinner bar
                    });

                    // handle errors
                    $rootScope.$on('$stateChangeError', function () {
                        element.addClass('hide'); // hide spinner bar
                    });

                    // handle errors
                    $rootScope.$on('$unauthorized', function () {
                        element.addClass('hide'); // hide spinner bar
                    });
                }
            };
        }
    ]);

    // Handle global LINK click
    Hrm.directive('a', function () {
        return {
            restrict: 'E',
            link: function (scope, elem, attrs) {
                if (attrs.ngClick || attrs.href === '' || attrs.href === '#') {
                    elem.on('click', function (e) {
                        e.preventDefault(); // prevent link click for above criteria
                    });
                }
            }
        };
    });

    Hrm.directive('responsiveMenuItem', ['$window', function ($window) {
        return {
            restrict: 'E',
            link: function (scope, elem, attrs) {
                let lText = attrs.longText;
                let sText = attrs.shortText;

                function display() {
                    if ($window.innerWidth >= 1270) {
                        elem.html(lText);
                    } else {
                        elem.html(sText);
                    }
                }

                display();

                angular.element($window).bind('resize', function () {
                    display();
                });
            }
        };
    }]);

    // Handle Dropdown Hover Plugin Integration
    Hrm.directive('dropdownMenuHover', function () {
        return {
            link: function (scope, elem) {
                elem.dropdownHover();
            }
        };
    });

    // Apply external libs to SELECT elements
    Hrm.directive('select', function () {
        return {
            restrict: 'E',
            link: function (scope, elem, attrs) {
                if (elem.hasClass('select2') || elem.hasClass('select2-multiple')) {
                    elem.select2({
                        allowClear: elem.hasClass('allow-clear'),
                        placeholder: attrs.placeholder,
                        width: null
                    });
                }
            }
        };
    });

    /**
     * Required for ui-select
     */
    Hrm.directive('uiSelectRequired', function () {
        return {
            require: 'ngModel',
            link: function (scope, element, attr, ctrl) {
                ctrl.$validators.uiSelectRequired = function (modelValue, viewValue) {
                    if (attr.uiSelectRequired) {
                        var isRequired = scope.$eval(attr.uiSelectRequired)
                        if (isRequired == false)
                            return true;
                    }
                    var determineVal;
                    if (angular.isArray(modelValue)) {
                        determineVal = modelValue;
                    } else if (angular.isArray(viewValue)) {
                        determineVal = viewValue;
                    } else if (angular.isObject(modelValue)) {
                        determineVal = angular.equals(modelValue, {}) ? [] : ['true'];
                    } else if (angular.isObject(viewValue)) {
                        determineVal = angular.equals(viewValue, {}) ? [] : ['true'];
                    } else {
                        return false;
                    }
                    return determineVal.length > 0;
                };
            }
        };
    });

    /**
     * Tab component
     */
    Hrm.directive('tabdrop', [function () {
        return {
            restrict: 'A',
            link: function (scope, elem, attrs) {
                elem.find('.nav-tabs > li > a').on('click', function (ev) {
                    ev.preventDefault();
                });

                elem.tabdrop({align: 'left'});
            }
        };
    }]);

    Hrm.directive('submenuToggler', ['$window', '$timeout', function ($window, $timeout) {
        return {
            restrict: 'A',
            link: function (scope, elem, attrs) {
                function display() {
                    $timeout(function () {
                        if ($window.innerWidth >= 992) {
                            elem.fadeOut('fast');
                        } else {
                            elem.fadeIn('fast');
                        }
                    }, 800);
                }

                display();

                angular.element($window).bind('resize', function () {
                    display();
                });

                let menu = elem.parent().find('#letter_container');
                elem.on('click', function () {
                    if (!menu.hasClass('mobile-submenu')) {
                        menu.addClass('mobile-submenu');
                        elem.css('left', '280px');
                        elem.html('<i class="fa fa-close"></i>');
                    } else {
                        menu.removeClass('mobile-submenu');
                        elem.css('left', '0');
                        elem.html('<i class="fa fa-long-arrow-right"></i>');
                    }
                });
            }
        };
    }]);

    /**
     * Modal draggable
     */
    Hrm.directive('modalMovable', ['$document',
        function ($document) {
            return {
                restrict: 'AC',
                link: function (scope, elem, attrs) {
                    var startX = 0,
                        startY = 0,
                        x = 0,
                        y = 0;

                    var dialogWrapper = elem.parent();
                    var dialogHeader = dialogWrapper.find('.modal-header');

                    dialogHeader.css('cursor', 'move');

                    dialogWrapper.css({
                        position: 'relative'
                    });

                    dialogHeader.on('mousedown', function (event) {
                        // Prevent default dragging of selected content
                        event.preventDefault();
                        startX = event.pageX - x;
                        startY = event.pageY - y;
                        $document.on('mousemove', mousemove);
                        $document.on('mouseup', mouseup);

                        dialogHeader.css('background-color', '#f8f8f8');
                    });

                    function mousemove(event) {
                        y = event.pageY - startY;
                        x = event.pageX - startX;
                        dialogWrapper.css({
                            top: y + 'px',
                            left: x + 'px'
                        });
                    }

                    function mouseup() {
                        $document.unbind('mousemove', mousemove);
                        $document.unbind('mouseup', mouseup);

                        dialogHeader.css('background-color', '#ffffff');
                    }
                }
            };
        }
    ]);

    /**
     * Replace the ng-include node with the included contents
     */
    Hrm.directive('includeReplace', function () {
        return {
            require: 'ngInclude',
            restrict: 'A',
            link: function (scope, el, attrs) {
                el.replaceWith(el.children());
            }
        };
    });

    /**
     * Toggle details
     */
    Hrm.directive('toggleView', function () {
        return {
            restrict: 'A',
            link: function ($scope, $elem, $attrs) {
                var target = $attrs.target || '';

                $elem.on('click', function () {
                        var $fa = $elem.find('i.fa');
                        if (target != '') {
                            var $target = $(target);
                            if ($target.hasClass('hidden')) {
                                $target.removeClass('hidden');
                                $fa.removeClass('fa-caret-down').addClass('fa-caret-up');
                            } else {
                                $target.addClass('hidden');
                                $fa.removeClass('fa-caret-up').addClass('fa-caret-down');
                            }
                        }
                    }
                );
            }
        };
    });

    /**
     * On key press
     */
    Hrm.directive('onKeyPress', [function () {
        return {
            restrict: 'A',
            link: function ($scope, $element, $attrs) {
                $element.bind("keypress", function (event) {
                    var keyCode = event.which || event.keyCode;

                    if (keyCode == $attrs.whenKeyCode) {
                        $scope.$apply(function () {
                            $scope.$eval($attrs.onKeyPress, {$event: event});
                        });

                    }
                });
            }
        };
    }]);

    /**
     * Apply styles for login screen
     */
    Hrm.directive('loginScreen', [function () {
        return {
            restrict: 'AC',
            link: function (scope, elem, attrs) {
                var $body = $('body');

                if ($body.hasClass('public-pages')) {
                    $body.removeClass('public-pages');
                }

                if (!$body.hasClass('login')) {
                    $body.addClass('login');
                }
            }
        };
    }]);

    /**
     * Apply styles for admin page
     */
    Hrm.directive('adminScreen', [function () {
        return {
            restrict: 'AC',
            link: function (scope, elem, attrs) {
                var $body = $('body');

                if ($body.hasClass('login')) {
                    $body.removeClass('login');
                }
            }
        };
    }]);

    Hrm.directive('slimScroll', [function () {
        return {
            restrict: 'A',
            link: function (scope, elem, attrs) {

                var color = attrs.color || '#ffffff';
                var height = attrs.containerHeight || '400px';

                elem.slimScroll({
                    color: color,
                    size: '10px',
                    height: height,
                    alwaysVisible: true,
                    railVisible: true,
                    disableFadeOut: true
                });
            }
        };
    }]);

    Hrm.directive('webuiPopover', ['$timeout', function ($timeout) {
        return {
            restrict: 'A',
            link: function (scope, elem, attrs) {
                var url = attrs.url || '#';
                var title = attrs.title || '';
                var moveTo = attrs.moveTo || '';
                var targetType = attrs.targetType || '';

                var popover = elem.webuiPopover({
                    url: url,
                    title: title,
                    placement: 'bottom',
                    type: 'html',
                    closeable: true,
                    arrow: false,
                    trigger: 'manual',
                    animation: 'pop',
                    backdrop: false,
                    dismissible: true,
                    onHide: function ($element) {
                        if (moveTo != '') {
                            if (targetType == 'ui-select') {
                                var uiSelect = angular.element($(moveTo).get(0)).controller('uiSelect');
                                $timeout(function () {
                                    uiSelect.activate(false, true);
                                }, 50);
                            } else {
                                $(moveTo).focus();
                            }
                        }
                    }
                });

                elem.on('focus', function () {
                    popover.webuiPopover('show');
                    $(this).trigger('blur');
                });

                $(url).find('button.btn').on('click', function () {
                    popover.webuiPopover('hide');
                });
            }
        };
    }]);

    Hrm.directive('dlEnterKey', function () {
        return function (scope, element, attrs) {

            element.bind("keydown keypress", function (event) {
                var keyCode = event.which || event.keyCode;

                // If enter key is pressed
                if (keyCode === 13) {
                    scope.$apply(function () {
                        // Evaluate the expression
                        scope.$eval(attrs.dlEnterKey);
                    });

                    event.preventDefault();
                }
            });
        };
    });

    Hrm.directive('myDatePicker', function () {
        return {
            restrict: 'A',
            require: '?ngModel',
            link: function (scope, element, attrs, ngModelController) {

                // Private variables
                var datepickerFormat = 'dd/mm/yyyy',
                    momentFormat = 'DD/MM/YYYY',
                    datepicker,
                    elPicker;

                // Init date picker and get objects http://bootstrap-datepicker.readthedocs.org/en/release/index.html
                datepicker = element.datepicker({
                    autoclose: true,
                    keyboardNavigation: false,
                    todayHighlight: true,
                    format: datepickerFormat
                });
                elPicker = datepicker.data('datepicker').picker;

                // Adjust offset on show
                datepicker.on('show', function (evt) {
                    elPicker.css('left', parseInt(elPicker.css('left')) + +attrs.offsetX);
                    elPicker.css('top', parseInt(elPicker.css('top')) + +attrs.offsetY);
                });

                // Only watch and format if ng-model is present https://docs.angularjs.org/api/ng/type/ngModel.NgModelController
                if (ngModelController) {
                    // So we can maintain time
                    var lastModelValueMoment;

                    ngModelController.$formatters.push(function (modelValue) {
                        //
                        // Date -> String
                        //

                        // Get view value (String) from model value (Date)
                        var viewValue,
                            m = moment(modelValue);
                        if (modelValue && m.isValid()) {
                            // Valid date obj in model
                            lastModelValueMoment = m.clone(); // Save date (so we can restore time later)
                            viewValue = m.format(momentFormat);
                        } else {
                            // Invalid date obj in model
                            lastModelValueMoment = undefined;
                            viewValue = undefined;
                        }

                        // Update picker
                        element.datepicker('update', viewValue);

                        // Update view
                        return viewValue;
                    });

                    ngModelController.$parsers.push(function (viewValue) {
                        //
                        // String -> Date
                        //

                        // Get model value (Date) from view value (String)
                        var modelValue,
                            m = moment(viewValue, momentFormat, true);
                        if (viewValue && m.isValid()) {
                            // Valid date string in view
                            if (lastModelValueMoment) { // Restore time
                                m.hour(lastModelValueMoment.hour());
                                m.minute(lastModelValueMoment.minute());
                                m.second(lastModelValueMoment.second());
                                m.millisecond(lastModelValueMoment.millisecond());
                            }
                            modelValue = m.toDate();
                        } else {
                            // Invalid date string in view
                            modelValue = undefined;
                        }

                        // Update model
                        return modelValue;
                    });

                    datepicker.on('changeDate', function (evt) {
                        // Only update if it's NOT an <input> (if it's an <input> the datepicker plugin trys to cast the val to a Date)
                        if (evt.target.tagName !== 'INPUT') {
                            ngModelController.$setViewValue(moment(evt.date).format(momentFormat)); // $seViewValue basically calls the $parser above so we need to pass a string date value in
                            ngModelController.$render();
                        }
                    });
                }

            }
        };
    });

    Hrm.directive('fileDownload', function () {
        return {
            restrict: 'A',
            scope: {
                fileDownload: '=',
                fileName: '=',
            },

            link: function (scope, elem, atrs) {


                scope.$watch('fileDownload', function (newValue, oldValue) {

                    if (newValue != undefined && newValue != null) {
                        console.debug('Downloading a new file');
                        var isFirefox = typeof InstallTrigger !== 'undefined';
                        var isSafari = Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0;
                        var isIE = /*@cc_on!@*/false || !!document.documentMode;
                        var isEdge = !isIE && !!window.StyleMedia;
                        var isChrome = !!window.chrome && !!window.chrome.webstore;
                        var isOpera = (!!window.opr && !!opr.addons) || !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;
                        var isBlink = (isChrome || isOpera) && !!window.CSS;

                        if (isFirefox || isIE || isChrome) {
                            if (isChrome) {
                                console.log('Manage Google Chrome download');
                                var url = window.URL || window.webkitURL;
                                var fileURL = url.createObjectURL(scope.fileDownload);
                                var downloadLink = angular.element('<a></a>');//create a new  <a> tag element
                                downloadLink.attr('href', fileURL);
                                downloadLink.attr('download', scope.fileName);
                                downloadLink.attr('target', '_self');
                                downloadLink[0].click();//call click function
                                url.revokeObjectURL(fileURL);//revoke the object from URL
                            }
                            if (isIE) {
                                console.log('Manage IE download>10');
                                window.navigator.msSaveOrOpenBlob(scope.fileDownload, scope.fileName);
                            }
                            if (isFirefox) {
                                console.log('Manage Mozilla Firefox download');
                                var url = window.URL || window.webkitURL;
                                var fileURL = url.createObjectURL(scope.fileDownload);
                                var a = elem[0];//recover the <a> tag from directive
                                a.href = fileURL;
                                a.download = scope.fileName;
                                a.target = '_self';
                                a.click();//we call click function
                            }


                        } else {
                            alert('SORRY YOUR BROWSER IS NOT COMPATIBLE');
                        }
                    }
                });

            }
        }
    });

})();
