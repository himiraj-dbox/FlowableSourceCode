/**
 * @preserve AngularJS PDF viewer directive using pdf.js.
 *
 * https://github.com/akrennmair/ng-pdfviewer
 *
 * MIT license
 */

angular.module('flowableModeler').
directive('pdfviewer', ['$parse', function ($parse) {
			var canvas = null;
			var instance_id = null;
			var excludedPages = [];
			return {
				restrict : "E",
				template : "<div class='make-scrollable'></div>",
				scope : {
					onPageLoad : '&',
					loadProgress : '&',
					src : '@',
					id : '=',
					excludedPages : '='
				},
				controller : ['$scope', function ($scope) {
						$scope.pageNum = 1;
						$scope.pdfDoc = null;
						$scope.scale = 1.0;

						$scope.documentProgress = function (progressData) {
							if ($scope.loadProgress) {
								$scope.loadProgress({
									state : "loading",
									loaded : progressData.loaded,
									total : progressData.total
								});
							}
						};

						$scope.loadPDF = function (path) {
							console.log('loadPDF ', path);
							PDFJS.getDocument(path, null, null, $scope.documentProgress).then(function (_pdfDoc) {
								$scope.pdfDoc = _pdfDoc;
								$scope.renderPages($scope.pageNum, function (success) {
									if ($scope.loadProgress) {
										$scope.loadProgress({
											state : "finished",
											loaded : 0,
											total : 0
										});
									}
								});
							}, function (message, exception) {
								console.log("PDF load error: " + message);
								if ($scope.loadProgress) {
									$scope.loadProgress({
										state : "error",
										loaded : 0,
										total : 0
									});
								}
							});
						};

						$scope.renderPages = function (num, callback) {
							$scope.$apply(function () {
								$scope.onPageLoad({
									page : $scope.pageNum,
									total : $scope.pdfDoc.numPages
								});
							});
							for (var num = 1; num <= $scope.pdfDoc.numPages; num++){
								var exist = jQuery.inArray(num, excludedPages);
								if(exist===-1){
									$scope.pdfDoc.getPage(num).then(function(page) {
										$scope.renderPage(page, num)
									})
								}
							}
						};

						$scope.renderPage = function(page, num) {
							var viewport = page.getViewport($scope.scale);
							var canvas = document.createElement('canvas');
							var ctx = canvas.getContext('2d');
							canvas.height = viewport.height;
							canvas.width = viewport.width;
							jQuery('.make-scrollable').append(canvas);
							jQuery('.make-scrollable').height(viewport.height - 100);
							page.render({ canvasContext: ctx, viewport: viewport }).promise.then(
								function() { 
									console.log("Rendered");
								}
						   )
							
						}

						$scope.$on('pdfviewer.nextPage', function (evt, id) {
							if (id !== instance_id) {
								return;
							}

							if ($scope.pageNum < $scope.pdfDoc.numPages) {
								$scope.pageNum++;
								$scope.pdfDoc.getPage($scope.pageNum).then(function(page) {
										$scope.renderPage(page, $scope.pageNum)
									})
							}
						});

						$scope.$on('pdfviewer.prevPage', function (evt, id) {
							if (id !== instance_id) {
								return;
							}

							if ($scope.pageNum > 1) {
								$scope.pageNum--;
									$scope.pdfDoc.getPage($scope.pageNum).then(function(page) {
										$scope.renderPage(page, $scope.pageNum)
									})
							}
						});

						$scope.$on('pdfviewer.gotoPage', function (evt, id, page) {
							if (id !== instance_id) {
								return;
							}

							if (page >= 1 && page <= $scope.pdfDoc.numPages) {
								$scope.pageNum = page;
								$scope.renderPage($scope.pageNum);
							}
						});
					}
				],
				link : function (scope, iElement, iAttr) {
					canvas = iElement.find('canvas')[0];
					instance_id = iAttr.id;
					excludedPages = scope.$parent.excludePages;
					iAttr.$observe('src', function (v) {
						console.log('src attribute changed, new value is', v);
						if (v !== undefined && v !== null && v !== '') {
							scope.pageNum = 1;
							scope.loadPDF(scope.src);
						}
					});
				}
			};
		}
	]).
service("PDFViewerService", ['$rootScope', function ($rootScope) {

			var svc = {};
			svc.nextPage = function () {
				$rootScope.$broadcast('pdfviewer.nextPage');
			};

			svc.prevPage = function () {
				$rootScope.$broadcast('pdfviewer.prevPage');
			};

			svc.Instance = function (id) {
				var instance_id = id;

				return {
					prevPage : function () {
						$rootScope.$broadcast('pdfviewer.prevPage', instance_id);
					},
					nextPage : function () {
						$rootScope.$broadcast('pdfviewer.nextPage', instance_id);
					},
					gotoPage : function (page) {
						$rootScope.$broadcast('pdfviewer.gotoPage', instance_id, page);
					}
				};
			};

			return svc;
		}
	]);
