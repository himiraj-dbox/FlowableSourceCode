/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
angular.module('flowableApp')
    .controller('RenderFormController', ['$rootScope', '$scope', '$http', '$translate', '$modal', 'appResourceRoot', 'FormService', 'UserService', 'FunctionalGroupService', 'RelatedContentService', '$sce', '$timeout', 'TaskService', 'hotkeys', 'uiGridConstants','$popover',
        function ($rootScope, $scope, $http, $translate, $modal, appResourceRoot, FormService, UserService, FunctionalGroupService, RelatedContentService, $sce, $timeout, TaskService, hotkeys, uiGridConstants,$popover) {

            // when you bind it to the controller's scope, it will automatically unbind
            // the hotkey when the scope is destroyed (due to ng-if or something that changes the DOM)
            hotkeys.bindTo($scope)
                .add({
                    combo: 'tab',
                    description: 'forward tab navigation',
                    allowIn: ['INPUT', 'SELECT', 'TEXTAREA'],
                    callback: function (event) {
                        var currentFormElement = $scope.detectCurrentFormElement(event.target);
                        var nextElement = $scope.getNextTabFormElement(currentFormElement);
                        focusFormElement(nextElement, event);
                    }
                }).add({
                    combo: 'shift+tab',
                    description: 'Backward tab navigation',
                    allowIn: ['INPUT', 'SELECT', 'TEXTAREA'],
                    callback: function (event) {
                        var currentFormElement = $scope.detectCurrentFormElement(event.target);
                        var prevElement = $scope.getPrevTabFormElement(currentFormElement);
                        focusFormElement(prevElement, event);
                    }
                });

          
          
            
            function focusFormElement(formElement, event) {
                if (formElement && formElement != null) {
                    if (formElement.type == "radio-buttons") {
                        formElement = angular.element("#activiti-" + formElement.id + ">div>label>input:nth-child(1)");
                    }  else {
                        formElement = angular.element("#activiti-" + formElement.id);
                    }
                    if (formElement[0]) {
                        if (typeOf('HTMLLIElement', formElement[0]))            {
                            event.preventDefault();
                        } else {
                            formElement[0].focus();
                            event.preventDefault();
                        }
                    }
                }
            }

            function typeOf(name, obj) {
                return Object.prototype.toString.call(obj) === '[object ' + name + ']';
            }

            function initModel () {
                $scope.model = {
                    loading: false,
                    valid: false,
                    uploads: {},
                    completeButtonDisabled: false,
                    saveButtonDisabled: false,
                    uploadInProgress: false,
                    isTaskForm: false,
                    processiinstancetask : $rootScope.root.processInstanceIDPDF
                  
                };
                $rootScope.firstRender = true;
                
                $scope.pdfUrlDynamic = '';
                //pdf
                $scope.pdfjsLib = window['pdfjs-dist/build/pdf'];
                $scope.pdfjsLib.GlobalWorkerOptions.workerSrc = '//mozilla.github.io/pdf.js/build/pdf.worker.js';
                $scope.pageNum = 1;
                $scope.pdfScale = 1.75; // make pdfScale a global variable
                $scope.shownPdf; 
                $scope.ocrRubberBanding  = [];
                //pdf
                //tesaract.js function
                $scope.input = document.getElementById('input');
                $scope.input_overlay = document.getElementById('input-overlay');
                //$scope.ioctx =  $scope.input_overlay.getContext('2d')
                $scope.output_text = document.getElementById('log');
                $scope.demo_instructions = document.getElementById('demo-instructions');
                 $scope.drop_instructions = [].slice.call(document.querySelectorAll('.drop-instructions'));
                 $scope.options = [].slice.call(document.querySelectorAll('.option'));
                 $scope.language = 'eng'
                	 $scope.demoStarted = false
                	 $scope.lang_demo_images = {
                	 	eng: 'img/eng_bw.png',
                	 	chi_sim: 'img/chi_sim.png',
                	 	rus: 'img/rus.png'
                	 };
                 $scope.lang_drop_instructions = {
                			eng: 'an English'
                		}
                 
                 $scope.allowedKeyMap  = ["Order Id" , "PO Number", "PO Date", "Vendor ID", "Order Id"];
                 $scope.allowedDelimeters = [":","-"]
               //  $scope.setUp();
                //tesaract.js function ends
            
            }
           

            initModel();
            
            //tesaract function
            $scope.setUp = function (){
            	 $scope.input_overlay.width =  $scope.input.naturalWidth
            	 $scope.input_overlay.height =  $scope.input.naturalHeight

            	 $scope.output_text.style.height =  $scope.input.height + 'px'
            }
            
           $scope.isOutputVisible =  function (){
            	return  $scope.output_text.getBoundingClientRect().top < dimensions.height
            }
           
           $scope.startDemoIfVisible =function(argument) {
        		if($scope.isOutputVisible() && !$scope.demoStarted) $scope.startDemo();
        	}
           
           $scope.startDemo =function(){
        	   $scope.demoStarted = true

        		function start(){
        			Tesseract.recognize(input)
        			.progress( $scope.progressUpdate)
        			.then( $scope.result)

        			 $scope.input.removeEventListener('load', start)
        		}

        		if( $scope.input.complete) start();
        		else  $scope.input.addEventListener('load', start)
        	}

           $scope.progressUpdate =function(packet){
        		var log = document.getElementById('log');

        		if(log.firstChild && log.firstChild.status === packet.status){
        			if('progress' in packet){
        				var progress = log.firstChild.querySelector('progress')
        				progress.value = packet.progress
        			}
        		}else{
        			var line = document.createElement('div');
        			line.status = packet.status;
        			var status = document.createElement('div')
        			status.className = 'status'
        			status.appendChild(document.createTextNode(packet.status))
        			line.appendChild(status)

        			if('progress' in packet){
        				var progress = document.createElement('progress')
        				progress.value = packet.progress
        				progress.max = 1
        				line.appendChild(progress)
        			}


        			if(packet.status == 'done'){
        				var pre = document.createElement('pre')
        				pre.appendChild(document.createTextNode(packet.data.text))
        				line.innerHTML = ''
        				line.appendChild(pre)

        			}

        			log.insertBefore(line, log.firstChild)
        		}
        	}
           
           Array.prototype.contains = function(obj) {
               var i = this.length;
               while (i--) {
                   if (this[i] === obj) {
                       return true;
                   }
               }
               return false;
           } 
           var objectForRubberBanding  =function(){

			   this.key = "";
			    this.previousKey ="";
			    this.value = "";
			    this.previosValue = "";
			    this.baselineCoordinated = "";
			   
			    this.box = {
				   
			   };
			    this.previousBox ={
				   
			   };
			    this.boxValue ={
				   
			   };
			    this.previousBoxValue = {
				   
			   };
			    this.completeBox ={
				   
			   };
	   
			this.assignedFormElement ={};
           }
           $scope.result =function(res){
        		// octx.clearRect(0, 0, output.width, output.height)
        		// octx.textAlign = 'left'

        		console.log('result was:', res)
        		// output_overlay.style.display = 'none'
        		// output_text.innerHTML = res.text

        		$scope.progressUpdate({ status: 'done', data: res })
                $scope.ocrRubberBanding  = [];
        		var countForKey = 0;
        		var countForValue =0;
        		var fetchingKey = true;
        		var fetchingValue = false;
        		var objectForRubberObj   = new objectForRubberBanding();
        		res.words.forEach(function(w){
        			
        			
        			var b = w.bbox;
        			if(fetchingKey){
                     if(countForKey == 0){
                     	if($scope.allowedDelimeters.contains(specialCharacterWithoutSpace)){
                    	   		fetchingKey = false;
                    		    fetchingValue = true ;
                    		   w.text =  w.text.substr(0,  w.text.length-1);
                    	   	}
                     	objectForRubberObj.key = w.text;
                     	objectForRubberObj.previousKey = w.text;
                     	countForKey= countForKey +1;
                     	objectForRubberObj.box = w.bbox;
                     	objectForRubberObj.previousBox = w.bbox;
                     }else{
                    	   if($scope.allowedDelimeters.contains(w.text)){
                    		   fetchingKey = false;
                    		   fetchingValue = true ;
                    	   }else{
                    	   	var specialCharacterWithoutSpace = w.text.charAt(w.text.length-1);
                    	   	if($scope.allowedDelimeters.contains(specialCharacterWithoutSpace)){
                    	   		fetchingKey = false;

                    		    fetchingValue = true ;
                    		   w.text =  w.text.substr(0,  w.text.length-1);
                    	   	}
                    	   	objectForRubberObj.key = 	objectForRubberObj.key + w.text;
                    	   	var formelemtSearchableemelemt = objectForRubberObj.key.replace(/\s+/g, '').toLowerCase()
                    	   	var elementSearcher  = document.getElementById('activiti-'+formelemtSearchableemelemt);
                    	   	if(elementSearcher){
                    	   //	document.getElementById('activiti-'+formelemtSearchableemelemt).classList.add("doubleBroderOverInput");
                    	   objectForRubberObj.assignedFormElement = 'activiti-'+formelemtSearchableemelemt;
                    	     objectForRubberObj.baselineCoordinated = w.baseline;
                    	   	}else{
                    	   		 objectForRubberObj.assignedFormElement =false;
                    	   	}
                    	   	objectForRubberObj.previousKey = w.text;
                    	   	objectForRubberObj.box.x1 = w.bbox.x1;
                    	    objectForRubberObj.box.y1 = w.bbox.y1;	


                    	   }

                     }
        			}
        			else{
        				if(countForValue == 0){
        					objectForRubberObj.value = w.text;
        						objectForRubberObj.box.x1 = w.bbox.x1;
                    	    objectForRubberObj.box.y1 = w.bbox.y1;	
        					countForValue = countForValue + 1;
        				
        					fetchingKey = true;
                 		   fetchingValue = false ;
        				}
        			}
        			
        			if(countForValue >0 && countForKey >0 ){
        				//JSON.parse(JSON.stringify(objectForRubberBanding))
        				 $scope.ocrRubberBanding.push(JSON.parse(JSON.stringify(objectForRubberObj)));
        				 objectForRubberObj   = new objectForRubberBanding();
        				 countForValue =0;
        				 countForKey = 0;
        			}
        			
        			/* $scope.ioctx.strokeWidth = 2

        			 $scope.ioctx.strokeStyle = 'red';
        				 $scope.ioctx.strokeRect(b.x0, b.y0, b.x1-b.x0, b.y1-b.y0);
        			 $scope.ioctx.beginPath();
        			 $scope.ioctx.moveTo(w.baseline.x0, w.baseline.y0);
        			 $scope.ioctx.lineTo(w.baseline.x1, w.baseline.y1);
        			 $scope.ioctx.strokeStyle = 'green';
        				 $scope.ioctx.stroke();*/


        	        // octx.font = '20px Times';
        	        // octx.font = 20 * (b.x1 - b.x0) / octx.measureText(w.text).width + "px Times";
        	        // octx.fillText(w.text, b.x0, w.baseline.y0);
        		})
        	}
           $scope.onFieldFocus= function(field){
        	   var a= 10;
        	   var b = a;
           }
           
           $scope.clearOverLayAndOutput =function(){
        	   $scope.ioctx.clearRect(0,0, input_overlay.width, input_overlay.height)

        		$scope.output_text.style.display = 'none'

        			$scope.demo_instructions.style.display = 'block'

        		// octx.clearRect(0,0,output.width, output.height)
        	}
           
           $scope.play = function(){
        	   $scope.input_overlay = document.getElementById('input-overlay');
        	   $scope.ioctx =  $scope.input_overlay.getContext('2d')
        	   $scope.demo_instructions = document.getElementById('demo-instructions');
        	   $scope.output_text = document.getElementById('log');
        	   $scope.demo_instructions.style.display = 'none'
        		   $scope.output_text.style.display = 'block'
        			   $scope.output_text.innerHTML = ''
        		// output_overlay.innerHTML = ''

        		Tesseract.recognize($scope.imageObectForOCR, $scope.language)
        		.progress( $scope.progressUpdate )
        		.then( $scope.result )
        	}
            //tesaract function
           

            $scope.detectCurrentFormElement = function (currentHtmlElement) {
                var currentHtmlElementId = currentHtmlElement.id;
                if (typeOf('HTMLInputElement', currentHtmlElement) && currentHtmlElement.type == "radio") {
                    //the current input is a radio option
                    //then the parent id #activiti-<name>
                    currentHtmlElementId = "activiti-" + currentHtmlElement.name;
                }

                var fields = $scope.model.allFormFields;

                //calculate the index of the current element in the fields array.
                var indexInSorted = 0;
                var elementToBeSelected = null;
                for (indexInSorted = 0; indexInSorted < fields.length && elementToBeSelected==null; indexInSorted++) {
                    if (elementToBeSelected == null && "activiti-" + fields[indexInSorted].id == currentHtmlElementId) {
                        //find the element in the form elements for the next element to be selected
                        elementToBeSelected = fields[indexInSorted];
                    }
                }
                return elementToBeSelected;
            };

            $scope.getNextTabFormElement = function (currentElement) {
                var elementToBeSelected = null;
                if (currentElement && currentElement != null) {
                    var fields = $scope.model.allFormFields;

                    var sortedElements = filterAndSortElements(fields);

                    //calculate the index of the next element in the sorted array.
                    var indexInSorted = 0;
                    var foundElementIndex = -1;
                    for (indexInSorted = 0; indexInSorted < sortedElements.length && foundElementIndex == -1; indexInSorted++) {
                        if (sortedElements[indexInSorted].id == currentElement.id) {
                            foundElementIndex = indexInSorted;
                        }
                    }

                    if (foundElementIndex >= 0 && foundElementIndex < sortedElements.length) {
                        while (foundElementIndex < sortedElements.length-1 && elementToBeSelected == null) {
                            //find the element in the form elements for the next element to be selected
                            elementToBeSelected = sortedElements[++foundElementIndex];
                        }
                    }
                }
                return elementToBeSelected;
            };

            $scope.getPrevTabFormElement = function (currentElement) {
                var elementToBeSelected = null;
                if (currentElement && currentElement != null) {

                    var fields = $scope.model.allFormFields;
                    var sortedElements = filterAndSortElements(fields);

                    //calculate the index of the next element in the sorted array.
                    var indexInSorted = 0;
                    var foundElementIndex = -1;
                    for (indexInSorted = 0; indexInSorted < sortedElements.length && foundElementIndex == -1; indexInSorted++) {
                         if (sortedElements[indexInSorted].id == currentElement.id) {
                            foundElementIndex = indexInSorted;
                        }
                    }

                    if (foundElementIndex > 0) {
                        while (foundElementIndex > 0 && elementToBeSelected == null) {
                            //find the element in the form elements for the prev element to be selected
                            elementToBeSelected = sortedElements[--foundElementIndex];
                        }
                    }
                }
                return elementToBeSelected;
            };

            function filterAndSortElements(fields) {
                var sortedElements = [];
                for (var i = 0; i < fields.length; i++) {
                    sortedElements.push(fields[i]);
                }

                sortedElements = sortedElements.filter(function (field) {
                    return !(
                       field.isVisible == false
                        || field.type === 'people'
                        || field.type === 'functional-group'
                        || field.type === 'expression'
                        || field.type === 'upload');
                });

                sortedElements = sortedElements.sort(function (field1, field2) {
                    var htmlElement1 = angular.element("#activiti-" + field1.id);
                    var htmlElement2 = angular.element("#activiti-" + field2.id);
                    var xPosition1 = 999999;
                    var yPosition1 = 999999;

                    var xPosition2 = 999999;
                    var yPosition2 = 999999;

                    if (htmlElement1) {
                        var testedElementRect1 = htmlElement1[0].getBoundingClientRect();
                        xPosition1 = testedElementRect1.left;
                        yPosition1 = testedElementRect1.top;
                    }

                    if (htmlElement2) {
                        var testedElementRect2 = htmlElement2[0].getBoundingClientRect();
                        xPosition2 = testedElementRect2.left;
                        yPosition2 = testedElementRect2.top;
                    }

                    if (yPosition1 == yPosition2) {
                        if (xPosition1 == xPosition2) return 0;
                        return (xPosition1 - xPosition2) / Math.abs(xPosition1 - xPosition2);
                    }

                    return (yPosition1 - yPosition2) / Math.abs(yPosition1 - yPosition2);

                });

                return sortedElements;
            }

            $scope.isEmpty = function (field) {
                return (field.value === undefined || field.value == null || field.value.length == 0);
            };

            $scope.isEmptyDropdown = function(field) {

                // Manual
                if (field.hasEmptyValue !== null && field.hasEmptyValue !== undefined && field.hasEmptyValue === true) {
                    if (field.options !== null && field.options !== undefined && field.options.length > 0) {
                        var emptyValue = field.options[0];
                        if (emptyValue === field.value) {
                            return true;
                        }
                    }
                } else if (field.value === '') {
                    return true
                }
                return false;
            };

            $scope.appResourceRoot = appResourceRoot;

            $scope.model.outcomesOnly = $scope.outcomesOnly !== null && $scope.outcomesOnly !== undefined
                && ($scope.outcomesOnly === true || $scope.outcomesOnly === 'true');

            // needed for selecting today in date popover

            $scope.clearDate = function(field, callback) {
                field.value = '';
                if (callback) {
                    callback(field.value);
                }
                $("#" + $rootScope.activitiFieldIdPrefix + field.id).blur();
            };

            $scope.selectToday = function(field, callback) {
                var today = new Date();
                today = new Date(today.getFullYear(), today.getMonth(), today.getDate(), 0, 0, 0, 0);
                field.value = today;
                if (callback) {
                    callback(field.value);
                }
                $("#" + $rootScope.activitiFieldIdPrefix + field.id).blur();
            };

            $scope.closeDatePopup = function (field) {
                $("#" + $rootScope.activitiFieldIdPrefix + field.id).blur();
            };

            $scope.combineFormVariables = function () {

                var fields = $scope.model.allFormFields;
                var localVariables = [];

                for (var fieldArrayIndex = 0; fieldArrayIndex < fields.length; fieldArrayIndex++) {
                    // Only adding value fields for form elements currently supported in condition evaluation
                    if (fields[fieldArrayIndex].type !== 'expression') {
                        localVariables.push(fields[fieldArrayIndex]);
                    }
                }

                $scope.currentAndHistoricFormFields = localVariables;
            };

            /**
             * Helper method: find a form field with a given id in a collection of fields
             */
            $scope.findFormFieldWithId = function (fields, id) {
                if (fields && fields.length > 0) {

                    // First check the current form fields
                    for (var i = 0; i < fields.length; i++) {
                        if (fields[i].id === id && fields[i].hasOwnProperty('isVisible')) {
                            return fields[i];
                        }
                    }

                    // Then check the historical ones
                    for (var i = 0; i < fields.length; i++) {
                        if (fields[i].id === id) {
                            return fields[i];
                        }
                    }

                }
                return undefined;
            };

            // Pre-process any previous values, if needed
            $scope.preProcessFields = function (fields) {
                for (var i = 0; i < fields.length; i++) {
                    var field = fields[i];
                    field.styleClassVal ='normal';
                    // set visibility bool if no condition is present
                    if (!field.visibilityCondition) {
                        field.isVisible = true;
                    }

                    if (field.type == 'dropdown' && field.value && field.options && !field.readOnly) {
                        for (var j = 0; j < field.options.length; j++) {
                            if (field.options[j].name == field.value) {
                                field.value = field.options[j];
                                break;
                            }
                        }

                    } else if (field.type == 'date' && field.value && !field.readOnly) {
                        var dateArray = field.value.split('-');
                        if (dateArray && dateArray.length == 3) {
                            field.value = new Date(dateArray[0],dateArray[1]-1,dateArray[2]);
                        }

                    } else if (field.type == 'people' && field.value) {
                        UserService.getUserInfoForForm(field.value, i).then(function (userInfoFormObject) {
                            fields[userInfoFormObject.index].value = userInfoFormObject.userData;
                        });

                    } else if (field.type == 'functional-group' && field.value) {
                        FunctionalGroupService.getGroupInfoForForm(field.value, i).then(function (groupInfoFormObject) {
                            fields[groupInfoFormObject.index].value = groupInfoFormObject.groupData;
                        });

                    } else if (field.type == 'upload' && field.value) {
                        $scope.model.uploads[field.id] = [];
                        var newUploadValue = '';
                        for (var j = 0; j < field.value.length; j++) {
                            $scope.model.uploads[field.id].push(field.value[j]);
                            if (newUploadValue.length > 0) {
                                newUploadValue += ',';
                            }
                            newUploadValue += field.value[j].id;
                        }
                        field.value = newUploadValue;
					}
                }
            };

            /**
             * Helper method: prepares the form fields for usage in the form template
             */
            var prepareFormFields = function (formData) {

                $scope.model.allFormFields = formData.fields;

                $scope.model.restValues = {};

                // populate only REST values in case of outcomesOnly
                if (!$scope.outcomesOnly) {
                    $scope.preProcessFields($scope.model.allFormFields);
                }
            };

            $scope.validateField = function () {

                function arrayContains(selectOptions, key, value) {
                    var found = false;
                    selectOptions.every(function (element, index, array) {
                        if (element[key] == value) {
                            found = true;
                            return false;
                        }
                        return true;
                    });
                    return found;
                }

                function findMatchingItem(items, key, value) {
                    var foundItem = undefined;
                    if (items && items.length > 0) {
                        items.every(function (item, index, array) {
                            if (item[key] == value) {
                                foundItem = item;
                                return false;
                            }
                            return true;
                        });
                    }
                    return foundItem;
                }

                if ($scope.model.allFormFields) {
                    var formValid = true;
                    for (var fieldIndex = 0; fieldIndex < $scope.model.allFormFields.length; fieldIndex++) {
                        var field = $scope.model.allFormFields[fieldIndex];

                        if (field) {
                            // Required field check
                            if (field && field.required) {
                                switch (field.type) {

                                    case 'boolean':
                                        if ((field.value === undefined || field.value == false || field.value == null)) {
                                            formValid = false;
                                        }
                                        break;

                                    case 'radio-buttons':
                                        var selectOptions = field.options;

                                        if (field.value === undefined || field.value == '' || field.value == null) {
                                            formValid = false;
                                        } else {
                                            formValid = arrayContains(selectOptions, "name", field.value);
                                            if (!formValid) {
                                                field.value = undefined;
                                            }
                                        }
                                        break;

                                    case 'dropdown':

                                        var emptyValue;
                                        if (field.hasEmptyValue !== null && field.hasEmptyValue !== undefined && field.hasEmptyValue === true) {
                                            if (field.options !== null && field.options !== null && field.options.length > 0) {
                                                emptyValue = field.options[0];
                                            }
                                        }

                                        if (emptyValue !== undefined && emptyValue !== null) {
                                            if (field.value.name === emptyValue.name) {
                                                formValid = false;
                                            }
                                        }

                                        break;

                                    default: //any other type
                                        if (field.value === undefined || field.value === '' || field.value === null) {
                                            formValid = false;
                                        }
                                        break;
                                }

                                if (!formValid) {
                                    break;
                                }
                            } else {

                            }
                        }
                    }

                    $scope.model.valid = formValid;
                }
            };


            // Deep watch form data fields to call validation
            $scope.$watch('formData', function () {
                $scope.validateField();
            }, true);

            /*
             * Fetching the task form if task id was provided, a start form if process definition id was
             * provided and otherwise the model should be on the scope
             */

            var fetchAndRenderForm = function () {

                $scope.model.loading = true;

                if ($scope.formDefinition) {

                    $scope.formData = $scope.formDefinition;
                     $scope.viewPdf = false;
                  $scope.formItems = $scope.formElements;
                     $scope.formelementsitem =  $scope.formDefinition.fields;
                  for (var i = 0; i < $scope.formelementsitem.length; i++) {
                var fieldelem = $scope.formelementsitem[i];
                if(fieldelem.type == 'pdf-viewer'){
                     $scope.viewPdf = true;
                }
            }
                    prepareFormFields($scope.formData);

                    if ($scope.model.outcomesOnly !== true) {

                        $scope.combineFormVariables();
                        $scope.model.loading = false;
                    }

                    $scope.model.loading = false;

                } else if ($scope.taskId) {

                    FormService.getTaskForm($scope.taskId).then(function (formData) {

                        $scope.formData = formData;
                        prepareFormFields($scope.formData); // Prepare the form fields to allow for layouting

                        if ($scope.model.outcomesOnly !== true) {
                            $scope.combineFormVariables();
                            $scope.model.loading = false;

                        }

                        $scope.model.loading = false;

                    });

                } else if ($scope.processDefinitionId) {

                    FormService.getStartForm($scope.processDefinitionId).then(function (formData) {
                        $scope.formData = formData;
                        prepareFormFields($scope.formData); // Prepare the form fields to allow for layouting
                        $scope.model.loading = false;

                        $scope.combineFormVariables();
                    });
                } else if ($scope.caseDefinitionId) {

                    FormService.getCaseStartForm($scope.caseDefinitionId).then(function (formData) {
                        $scope.formData = formData;
                        prepareFormFields($scope.formData); // Prepare the form fields to allow for layouting
                        $scope.model.loading = false;

                        $scope.combineFormVariables();
                    });
                }
            };

            // Fetch and show on first usage
            fetchAndRenderForm();

            // Re-render when process definition || caseDefinitionId changes
            $scope.$watch('processDefinitionId', function (newValue, oldValue) {
                if (newValue !== oldValue) {
                    // Check if actually changed
                    initModel();
                    fetchAndRenderForm();
                }
            }, true);
            $scope.$watch('caseDefinitionId', function (newValue, oldValue) {
                if (newValue !== oldValue) {
                    // Check if actually changed
                    initModel();
                    fetchAndRenderForm();
                }
            }, true);

            /**
             * Generates a default button text based on what is passed as config
             */
            $scope.getDefaultCompleteButtonText = function () {
                if ($scope.processDefinitionId) {
                    return $translate.instant('FORM.DEFAULT-OUTCOME.START-PROCESS');
                } else if ($scope.caseDefinitionId) {
                    return $translate.instant('FORM.DEFAULT-OUTCOME.START-CASE');
                } else {
                    return $translate.instant('FORM.DEFAULT-OUTCOME.COMPLETE');
                }
            };

            $scope.saveForm = function () {

                $scope.model.loading = true;
                $scope.model.completeButtonDisabled = true;

                // Prep data
                var postData = $scope.createPostData();
                postData.formId = $scope.formData.id;

                 FormService.saveTaskForm($scope.taskId, postData).then(
                     function (data) {
                         $rootScope.addAlertPromise($translate('TASK.ALERT.SAVED'));
                         $scope.model.completeButtonDisabled = false;
                         $scope.model.loading = false;
                     },
                     function (errorResponse) {
                         $scope.model.completeButtonDisabled = false;
                         $scope.model.loading = false;
                         $scope.$emit('task-save-error', {
                             taskId: $scope.taskId,
                             error: errorResponse
                         });
                     });
            };

            $scope.completeForm = function (outcome) {

                $scope.model.loading = true;
                $scope.model.completeButtonDisabled = true;

                // Prep data
                var postData = $scope.createPostData();
                postData.formId = $scope.formData.id;

                if (outcome) {
                    postData.outcome = outcome.name;
                }

                if ($scope.processDefinitionId) {

                    // Add right process-definition for this form
                    postData.processDefinitionId = $scope.processDefinitionId;

                    if ($scope.processName) {
                        postData.name = $scope.processName;
                    }
                    if((outcome && (outcome == 'Duplicate' || outcome.name == 'Duplicate' ||outcome == 'duplicate' || outcome.name == 'duplicate' || outcome.name =='duplicate' || outcome == 'duplicatecheck'))){
                    	var duplcateData = postData;
                    	duplcateData.values.taskIdActual = $scope.taskId;
						FormService
								.duplicateCheck(postData)
								.then(
										function(
												matcherpostData) {
											var a = 50;
											var b = a;
											;
											if (matcherpostData.message != "SUCCESS") {
												alert('Duplicate Data found')
											
											}else{
													alert('No Duplicate Data found')
											}
											$scope.model.completeButtonDisabled = false;
											$scope.model.loading = false;
										},
										function(errorResponse) {
											$scope.model.completeButtonDisabled = false;
											$scope.model.loading = false;
											$scope
													.$emit(
															'process-started-error',
															{
																processDefinitionId : $scope.processDefinitionId,
																error : errorResponse
															});
										});
						$scope.model.completeButtonDisabled = false;
						$scope.model.loading = false;
                    }
                    else if ((outcome && outcome == 'Matcher')
											|| (outcome && outcome.name && outcome.name == 'Matcher')) {
										var matcherpostData = postData;
										FormService
												.matherProcess(postData)
												.then(
														function(
																matcherpostData) {
															var a = 50;
															var b = a;
															;
															if (!matcherpostData.values.ponumber) {
																var fields = $scope.model.allFormFields;

																// calculate the
																// index of the
																// current
																// element in
																// the fields
																// array.
																var index = 0;

																for (index = 0; index < fields.length; index++) {
																	var str = fields[index].id;
																	var n = str
																			.search(/matchto/i);
																	var kk = n;
																	if (n == 0) {
																		fields[index].styleClassVal = 'redbordermatcher';
																	}
																}
															}
														},
														function(errorResponse) {
															$scope.model.completeButtonDisabled = false;
															$scope.model.loading = false;
															$scope
																	.$emit(
																			'process-started-error',
																			{
																				processDefinitionId : $scope.processDefinitionId,
																				error : errorResponse
																			});
														});
										$scope.model.completeButtonDisabled = false;
										$scope.model.loading = false;
}else{
	 FormService.formFieldSave(postData).then(
             function (data) {
                 console.log("Values persisted in DB");
             },
             function (errorResponse) {
                 $scope.model.completeButtonDisabled = false;
                 $scope.model.loading = false;
                 $scope.$emit('case-started-error', {
                     caseDefinitionId: $scope.caseDefinitionId,
                     error: errorResponse
                 });
             });
                    FormService.completeStartForm(postData).then(
                        function (data) {
                            $scope.$emit('process-started', data);
                            $scope.model.completeButtonDisabled = false;
                            $scope.model.loading = false;
                        },
                        function (errorResponse) {
                            $scope.model.completeButtonDisabled = false;
                            $scope.model.loading = false;
                            $scope.$emit('process-started-error', {
                            	processDefinitionId: $scope.processDefinitionId,
                            	error: errorResponse
                           	});
                        });}


                } else if ($scope.caseDefinitionId) {

                    // Add right process-definition for this form
                    postData.caseDefinitionId = $scope.caseDefinitionId;

                    if ($scope.caseName) {
                        postData.name = $scope.caseName;
                    } 
                    if((outcome && (outcome == 'Duplicate' || outcome.name == 'Duplicate' ||outcome == 'duplicate' || outcome.name == 'duplicate' || outcome.name =='duplicate' || outcome == 'duplicatecheck'))){

                    	var duplcateData = postData;
                    	duplcateData.values.taskIdActual = $scope.taskId;
						FormService
								.duplicateCheck(postData)
								.then(
										function(
												matcherpostData) {
											var a = 50;
											var b = a;
											;
											if (matcherpostData.message != "SUCCESS") {
												alert('Duplicate Data found')
											
											}else{
													alert('No Duplicate Data found')
											}
											$scope.model.completeButtonDisabled = false;
											$scope.model.loading = false;
										},
										function(errorResponse) {
											$scope.model.completeButtonDisabled = false;
											$scope.model.loading = false;
											$scope
													.$emit(
															'process-started-error',
															{
																processDefinitionId : $scope.processDefinitionId,
																error : errorResponse
															});
										});
						$scope.model.completeButtonDisabled = false;
						$scope.model.loading = false;
                    }
                    else  if((outcome && outcome == 'Matcher')
											|| (outcome && outcome.name && outcome.name == 'Matcher')) {
										var matcherpostData = postData;
										FormService
												.matherProcess(postData)
												.then(
														function(
																matcherpostData) {
															var a = 50;
															var b = a;
															;
															if (!matcherpostData.ponumber) {
																var fields = $scope.model.allFormFields;

																// calculate the
																// index of the
																// current
																// element in
																// the fields
																// array.
																var index = 0;

																for (index = 0; index < fields.length; index++) {
																	var str = fields[index].id;
																	var n = str
																			.search(/matchto/i);
																	var kk = n;
																	if (n == 0) {
																		fields[index].styleClassVal = 'redbordermatcher';
																	}
																}
															} else {

																var amount = 0;
																var amounttomatch = matcherpostData.po_amount;
																index = 0;
																for (index = 0; index < fields.length; index++) {
																	var str = fields[index].id;
																	if ('invoiceamount' == str) {
																		amount = fields[index].value;
																	}
																	

																}
																
																if(amount != amounttomatch){
																	for (index = 0; index < fields.length; index++) {
																		var str = fields[index].id; 
																		 fields[index].styleClassVal = 'normal';
																		if('matchtoinvoiceamount' == str){
												 	                		fields[index].value =amounttomatch;
												 	                		 fields[index].styleClassVal = 'redbordermatcher';
												 	                	}
																	}
																	
																	
																}else{
																	for (index = 0; index < fields.length; index++) {
																		var str = fields[index].id; 
																		 fields[index].styleClassVal = 'normal';
												 	                	if('matchtoinvoiceamount' == str){
												 	                		fields[index].value =amounttomatch;
												 	                		
												 	                	}
																	}
																}
															}

														},
														function(errorResponse) {
															$scope.model.completeButtonDisabled = false;
															$scope.model.loading = false;
															$scope
																	.$emit(
																			'process-started-error',
																			{
																				processDefinitionId : $scope.processDefinitionId,
																				error : errorResponse
																			});
														});
										$scope.model.completeButtonDisabled = false;
										$scope.model.loading = false;
}else{
	 FormService.formFieldSave(postData).then(
             function (data) {
                 console.log("Values persisted in DB");
             },
             function (errorResponse) {
                 $scope.model.completeButtonDisabled = false;
                 $scope.model.loading = false;
                 $scope.$emit('case-started-error', {
                     caseDefinitionId: $scope.caseDefinitionId,
                     error: errorResponse
                 });
             });
                    FormService.completeCaseStartForm(postData).then(
                        function (data) {
                            $scope.$emit('case-started', data);
                            $scope.model.completeButtonDisabled = false;
                            $scope.model.loading = false;
                        },
                        function (errorResponse) {
                            $scope.model.completeButtonDisabled = false;
                            $scope.model.loading = false;
                            $scope.$emit('case-started-error', {
                                caseDefinitionId: $scope.caseDefinitionId,
                                error: errorResponse
                            });
                        });}


                } else {
                    if((outcome && (outcome == 'Duplicate' || outcome.name == 'Duplicate' || outcome == 'duplicate' || outcome.name == 'duplicate' || outcome.name =='duplicate' || outcome == 'duplicatecheck'))){

                    	var duplcateData = postData;
                    	duplcateData.values.taskIdActual = $scope.taskId;
						FormService
								.duplicateCheck(duplcateData)
								.then(
										function(
												matcherpostData) {
											var a = 50;
											var b = a;
											;
											if (matcherpostData.message != "SUCCESS") {
												alert('Duplicate Data found')
											
											}else{
													alert('No Duplicate Data found')
											}
											$scope.model.completeButtonDisabled = false;
											$scope.model.loading = false;
										},
										function(errorResponse) {
											$scope.model.completeButtonDisabled = false;
											$scope.model.loading = false;
											$scope
													.$emit(
															'process-started-error',
															{
																processDefinitionId : $scope.processDefinitionId,
																error : errorResponse
															});
										});
						$scope.model.completeButtonDisabled = false;
						$scope.model.loading = false;
                }
                else if ((outcome && outcome == 'Matcher') || (outcome && outcome.name && outcome.name == 'Matcher') ){
	var matcherpostData = postData;
	FormService
			.matherProcess(postData)
			.then(
					function(
							matcherpostData) {
						var a = 50;
						var b = a;
						;
						if (!matcherpostData.poNumber) {
							var fields = $scope.model.allFormFields;

							// calculate the
							// index of the
							// current
							// element in
							// the fields
							// array.
							var index = 0;

							for (index = 0; index < fields.length; index++) {
								var str = fields[index].id;
								var n = str
										.search(/matchto/i);
								var kk = n;
								if (n == 0) {
									fields[index].styleClassVal = 'redbordermatcher';
								}
							}
						} else {
							var fields = $scope.model.allFormFields;
							var amount = 0;
							var amounttomatch = matcherpostData.po_amount;
							index = 0;
							for (index = 0; index < fields.length; index++) {
								var str = fields[index].id;
								if ('invoiceamount' == str) {
									amount = fields[index].value;
								}
								

							}
							
							if(amount != amounttomatch){
								var fields = $scope.model.allFormFields;
								for (index = 0; index < fields.length; index++) {
									var str = fields[index].id; 
									 fields[index].styleClassVal = 'normal';
									if('matchtoinvoiceamount' == str){
			 	                		fields[index].value =amounttomatch;
			 	                		 fields[index].styleClassVal = 'redbordermatcher';
			 	                	}
								}
								
								
							}else{
								var fields = $scope.model.allFormFields;
								for (index = 0; index < fields.length; index++) {
									var str = fields[index].id; 
									 fields[index].styleClassVal = 'normal';
			 	                	if('matchtoinvoiceamount' == str){
			 	                		fields[index].value =amounttomatch;
			 	                		
			 	                	}
								}
							}
						}

					},
					function(errorResponse) {
						$scope.model.completeButtonDisabled = false;
						$scope.model.loading = false;
						$scope
								.$emit(
										'process-started-error',
										{
											processDefinitionId : $scope.processDefinitionId,
											error : errorResponse
										});
					});
	$scope.model.completeButtonDisabled = false;
	$scope.model.loading = false;
}else{
	var duplcateData = postData;
	duplcateData.values.taskIdActual = $scope.taskId;
	 FormService.formFieldSave(duplcateData).then(
             function (data) {
                 console.log("Values persisted in DB");
             },
             function (errorResponse) {
                 $scope.model.completeButtonDisabled = false;
                 $scope.model.loading = false;
                 $scope.$emit('case-started-error', {
                     caseDefinitionId: $scope.caseDefinitionId,
                     error: errorResponse
                 });
             });
                    FormService.completeTaskForm($scope.taskId, postData).then(
                        function (data) {
                            $scope.$emit('task-completed', {taskId: $scope.taskId});
                            $scope.model.completeButtonDisabled = false;
                            $scope.model.loading = false;
                        },
                        function (errorResponse) {
                            $scope.model.completeButtonDisabled = false;
                            $scope.model.loading = false;
                            $scope.$emit('task-completed-error', {
                            	taskId: $scope.taskId,
                            	error: errorResponse
                            });
                        });}

                }
            };

            $scope.fieldPersonSelected = function (user, field) {
                field.value = user;
            };

            $scope.fieldPersonRemoved = function (user, field) {
                field.value = undefined;
            };

            $scope.fieldGroupSelected = function (group, field) {
                field.value = group;
            };

            $scope.fieldGroupRemoved = function (group, field) {
                field.value = undefined;
            };

            $scope.contentUploaded = function (content, field) {
                if (!$scope.model.uploads[field.id]) {
                    $scope.model.uploads[field.id] = [];
                }
                $scope.model.uploads[field.id].push(content);
                $scope.updateContentValue(field);
                loadRelatedContentPDF();
            };
            

            $scope.removeContent = function (content, field) {
                if ($scope.model.uploads[field.id]) {

                    $scope.model.uploads[field.id] = $.grep($scope.model.uploads[field.id], function (elem, index) {
                        return elem !== content;
                    });
                    $scope.updateContentValue(field);
                }
            };

            $scope.updateContentValue = function (field) {
                if (!$scope.model.uploads[field.id]) {
                    field.value = undefined;
                } else {
                    var newValue = '';
                    for (var i = 0; i < $scope.model.uploads[field.id].length; i++) {
                        if (i > 0) {
                            newValue += ',';
                        }
                        newValue += $scope.model.uploads[field.id][i].id;
                    }

                    field.value = newValue;
                }
            };

            $scope.onFieldValueChange = function (field) {
            };

            $scope.uploadInProgress = function (state) {
                if (state !== 'undefined') {
                    $scope.model.uploadInProgress = state;
                }
            };

            $scope.createPostData = function() {
                var postData = {values: {}};
                if (!$scope.model.allFormFields) return postData;

                for (var fieldArrayIndex = 0; fieldArrayIndex < $scope.model.allFormFields.length; fieldArrayIndex++) {
                    var field = $scope.model.allFormFields[fieldArrayIndex];
                    if (!field || !field.isVisible) continue;

                    if (field.type === 'boolean' && field.value == null) {
                        field.value = false;
                    }

                    if (field && field.type !== 'expression' && !field.readOnly) {

                        if (field.type === 'dropdown' && field.hasEmptyValue !== null && field.hasEmptyValue !== undefined && field.hasEmptyValue === true) {

                            // Manually filled dropdown
                            if (field.options !== null && field.options !== undefined && field.options.length > 0) {

                                var emptyValue = field.options[0];
                                if (field.value != null && field.value != undefined && emptyValue.name !== field.value.name) {
                                    postData.values[field.id] = field.value;
                                }
                            }

                        } else if (field.type === 'date' && field.value) {
                            postData.values[field.id] = field.value.getFullYear() + '-' + (field.value.getMonth() + 1) + '-' + field.value.getDate();

                        } else {
                            postData.values[field.id] = field.value;
                        }
                    }
                }

                return postData;
            };

            $scope.togglePasswordFieldType = function(field){
        			if(field.params.type === undefined || !field.params.type)
        				field.params.type = true;	
        			else
        				field.params.type = false;	
            };

            // Place methods that are used by controls into an object which is pushed won the container hierarchy
            // Note that these callbacks must be mapped inside the formElement directive as well (workflow-directives.js)
            $scope.controlCallbacks = {
                onFieldValueChange: $scope.onFieldValueChange,
                isEmpty: $scope.isEmpty,
                isEmptyDropdown: $scope.isEmptyDropdown,
                fieldPersonSelected: $scope.fieldPersonSelected,
                fieldPersonRemoved: $scope.fieldPersonRemoved,
                fieldGroupSelected: $scope.fieldGroupSelected,
                fieldGroupRemoved: $scope.fieldGroupRemoved,
                removeContent: $scope.removeContent,
                contentUploaded: $scope.contentUploaded,
                uploadInProgress: $scope.uploadInProgress,
                handleReadonlyClick: $scope.handleReadonlyClick,
                clearDate: $scope.clearDate,
                selectToday: $scope.selectToday,
                closeDatePopup: $scope.closeDatePopup,
                togglePasswordFieldType: $scope.togglePasswordFieldType
            };

            if ($scope.taskId) {
                $scope.model.isTaskForm = true;
            }

    loadRelatedContent();
    function loadRelatedContent() {
        $scope.model.content = undefined;
        TaskService.getRelatedContent($scope.taskId).then(function (data) {
            $scope.model.content = data;
        });
    };
    
    loadRelatedContentPDF();
    function loadRelatedContentPDF() {
        $scope.model.contentPDF = undefined;
        TaskService.getRelatedContentPDF($rootScope.root.processInstanceIDPDF).then(function (data) {
            $scope.model.contentPDF = data;
            $scope.pdfUrlDynamic = $scope.model.contentPDF.data[0].rawUrl;
            $scope.pdfDocFuntion();
          //  $scope.runOCR($scope.model.contentPDF.data[0].rawUrl);
        });
    };
    

    
    $scope.deleteContent = function(content) {
        var modalInstance = _internalCreateModal({
            template: appResourceRoot + 'views/modal/delete-content.html',
            show: true
        }, $modal, $scope);

        modalInstance.$scope.popup = {
            content: content,
            loading: false
        };

        modalInstance.$scope.ok = function() {
            RelatedContentService.deleteContent(content.id, task && task.id).then(function() {
                $scope.$emit('content-deleted', {content: content});
                $scope.model.selectedContent = null;
                $scope.model.selectedTask = null;
            });
        };
        loadRelatedContentPDF();
    };
    
    $scope.onContentDeletedPDF = function(content) {
    	try{
        if ($scope.model.contentPDF && $scope.model.contentPDF.data) {
            $scope.model.contentPDF.data.forEach(function(value, i, arr){
                if (content === value) {
                    arr.splice(i, 1);
                }
            })
        }
        
        loadRelatedContentPDF();
    	}catch(err){
    		console.log(err);
    	}
    };
  

  ///for pdf
   // another global we'll use for the buttons
    // var url = './helloworld.pdf' // PDF to load: change this to a file that exists;
 


    
    

    $scope.firstRender = false;
    $scope.renderPage =function(page) {
    	if($rootScope.firstRender){
    	try {
    	 $scope.scale =  $scope.pdfScale; // render with global pdfScale variable
    	 $scope.viewport = page.getViewport($scope.scale);
    	 $scope.PRINT_RESOLUTION = 600;
    	 $scope.PRINT_UNITS =  $scope.PRINT_RESOLUTION / 600;
    	 $scope.canvas = document.getElementById('input-overlay');
    	 $scope.context =  $scope.canvas.getContext('2d');
    	 $scope.canvas.height =  $scope.viewport.height;
    	 $scope.canvas.width =  $scope.viewport.width;
    	 $scope.canvas.addEventListener('click', $scope.on_canvas_click, false);
    	 $scope.canvas.addEventListener('mousemove', $scope.on_canvas_move, false);
       var renderContext = {
          canvasContext: $scope.context,
          transform: [$scope.PRINT_UNITS, 0, 0, $scope.PRINT_UNITS, 0, 0],
          viewport: $scope.viewport
       };
       $scope.imageForOCR = 	 page.render(renderContext);
       $scope.imageForOCR.promise.then(function(){
           $scope.imageObectForOCR =   $scope.canvas.toDataURL() ;
           $scope.runOCR();
        
            })  .catch(function(err) {

               
                //$scope.onclickzoominbutton();
                
                console.log('Page render error', err);
            });;
            $rootScope.firstRender =    !$rootScope.firstRender;
       if($scope.firstRender){
      /*  imageForOCR.promise.then(function(){
        $scope.imageObectForOCR =   $scope.canvas.toDataURL() ;
        $scope.runOCR();
     
         });*/}
       $scope.firstRender = true;
       
    	}catch(err){
    		
         //   $scope.onclickzoominbutton();
            
            console.log('Page render error', err);
	};
    }else{
    	$rootScope.firstRender = !$rootScope.firstRender;
    }};
    
    $scope.higlightOCRImageOnthebasisofTag = function(){
    	if($scope.previousStroke){
    		$scope.ioctx.beginPath();
    		$scope.ioctx.strokeStyle = 'white';
    		 $scope.ioctx.strokeRect($scope.previousStroke.x0, $scope.previousStroke.y0, $scope.previousStroke.x1-$scope.previousStroke.x0, $scope.previousStroke.y1-$scope.previousStroke.y0);
            $scope.ioctx.stroke();
    	}
    	for(var i in $scope.ocrRubberBanding){
    		if($scope.ocrRubberBanding[i].assignedFormElement){
    			var dummyEl =document.getElementById($scope.ocrRubberBanding[i].assignedFormElement);
    			
    			var isFocused = (document.activeElement === dummyEl)
    				if(isFocused){
    					$scope.ioctx.strokeWidth = 2

           			 $scope.ioctx.strokeStyle = 'red';
    					
           				 $scope.ioctx.strokeRect($scope.ocrRubberBanding[i].box.x0, $scope.ocrRubberBanding[i].box.y0, $scope.ocrRubberBanding[i].box.x1-$scope.ocrRubberBanding[i].box.x0, $scope.ocrRubberBanding[i].box.y1-$scope.ocrRubberBanding[i].box.y0);
           				 $scope.previousStroke = $scope.ocrRubberBanding[i].box;
    				}
    			}
    		}
    	
  	  
    }
    
    $scope.copyFormFieldfoundbyOCR =function(x,y){
       	for(var i in $scope.ocrRubberBanding){
       		if($scope.ocrRubberBanding[i].assignedFormElement && null != $scope.ocrRubberBanding[i].assignedFormElement ){
       			if(x>=$scope.ocrRubberBanding[i].box.x0 && x<=$scope.ocrRubberBanding[i].box.x1  && y>=$scope.ocrRubberBanding[i].box.y0 && y<=$scope.ocrRubberBanding[i].box.y1)
       			document.getElementById($scope.ocrRubberBanding[i].assignedFormElement).value =$scope.ocrRubberBanding[i].value;
       		}
       	}
       }
    
    
       $scope.highlighFormFieldfoundbyOCR =function(x,y){
       	for(var i in $scope.ocrRubberBanding){
       		if($scope.ocrRubberBanding[i].assignedFormElement && null != $scope.ocrRubberBanding[i].assignedFormElement ){
       			document.getElementById($scope.ocrRubberBanding[i].assignedFormElement).classList.remove("doubleBroderOverInput");
       			if(x>=$scope.ocrRubberBanding[i].box.x0 && x<=$scope.ocrRubberBanding[i].box.x1  && y>=$scope.ocrRubberBanding[i].box.y0 && y<=$scope.ocrRubberBanding[i].box.y1)
       			document.getElementById($scope.ocrRubberBanding[i].assignedFormElement).classList.add("doubleBroderOverInput");
       		}
       	}
       }
    
       $scope.on_canvas_click =function(ev) {
           var x = ev.layerX;
           var y = ev.layerY;
           $scope.copyFormFieldfoundbyOCR(x,y);
           

           // ... x,y are the click coordinates relative to the
           // canvas itself
       }   
       
    $scope.on_canvas_move =function(ev) {
        var x = ev.layerX;
        var y = ev.layerY;
        $scope.highlighFormFieldfoundbyOCR(x,y);
        

        // ... x,y are the click coordinates relative to the
        // canvas itself
    }

    $scope.displayPage =function(pdf, num) {
    	try{
       pdf.getPage(num).then(function getPage(page) {
       	 $scope.renderPage(page); });
    	}catch(err){
    		console.log(err);
    	}
    }
    $scope.pdfDocFuntion = function(){
    	try{
    		
    	  $scope.pdfDoc =  $scope.pdfjsLib.getDocument($scope.pdfUrlDynamic).then(function getPdfHelloWorld(pdf) {
    	    	$scope.displayPage(pdf, 1);
    	    	$scope.shownPdf = pdf;
    	    });
    		
    	}catch(err){
    		console.log(err);
    	}
    }
  

    $scope.nextbutton = document.getElementById("nextbutton");
    $scope.onclicknextbutton = function() {
    	$rootScope.firstRender= true;
       if ( $scope.pageNum >=  $scope.shownPdf.numPages) {
          return;
       }
       $scope.pageNum++;
       $scope.displayPage( $scope.shownPdf,  $scope.pageNum);
    }

    $scope.prevbutton = document.getElementById("prevbutton");
    $scope.onclickprevbutton = function() {
    	$rootScope.firstRender= true;
       if ( $scope.pageNum <= 1) {
          return;
       }
       $scope.pageNum--;
       $scope.displayPage( $scope.shownPdf,  $scope.pageNum);
    }

    $scope.zoominbutton = document.getElementById("zoominbutton");
    $scope.onclickzoominbutton = function() {
    	$rootScope.firstRender= true;
    	 $scope.pdfScale = $scope.pdfScale + 0.25;
    	 $scope.displayPage($scope.shownPdf, $scope.pageNum);
    }

    $scope.zoomoutbutton = document.getElementById("zoomoutbutton");
    $scope.onclickzoomoutbutton = function() {
    	$rootScope.firstRender= true;
       if ( $scope.pdfScale <= 0.25) {
          return;
       }
       $scope.pdfScale = $scope.pdfScale - 0.25;
       $scope.displayPage($scope.shownPdf, $scope.pageNum);
    }           
    
//for pdf ends

//for Tessaract
     $scope.runOCR =function() {
    	 try{
     
    	 $scope.play();   
    /*    Tesseract.recognize($scope.imageObectForOCR )
             .then(function(result) {
            	 $scope.tesaractResult  = result.text;
            	 $scope.tessaractImageElement = result.html;
              var b = a;
             }).progress(function(result) {
                var d =  " (" +
                            (result["progress"] * 100) + "%)";
             });*/
        
    	 }catch(err){
     		console.log(err);
     	}
    	 var a =  $scope.tesaractResult;
    	 var b =  $scope.tesaractResult;
    }
//ends
    
    
$scope.openSearchFieldDialog= function(title){
    var a  ='ABC';
 
                // Create popover
              

                    $scope.searchvendorinformation = $popover(angular.element('#toggle-search-vendor'), {
                        template: appResourceRoot+ 'views/popover/search-vendor-information.html',
                        
                        show: true,
                        scope: $scope
                    });

                    $scope.searchvendorinformation.$scope.$on('tooltip.hide', function () {
                        $scope.searchvendorinformation.$scope.$destroy();
                        $scope.searchvendorinformation.destroy();
                        $scope.searchvendorinformation = undefined;

                        $scope.newTask = undefined;
                    });
                }
            ;



        }]);
