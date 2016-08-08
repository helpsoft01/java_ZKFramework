/**
 * 
 */
String.prototype.replaceAll = function (find, replace) {
				var str = this;
				return str.replace(new RegExp(find.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&'), 'g'), replace);
			};
vietek = {
	runJSCallBack : function(widgetId) {
		zAu.send(new zk.Event(zk.Widget.$('$' + widgetId + ''),
				'onFinishJSCall', null, {
					toServer : true
				}), 0);
	},
	checkWidget : function(widgetId) {
		var widget = zk.Widget.$('$' + widgetId + '').$n();
		if (""+ widget != 'undefined' && +""+widget != 'null') {
			zAu.send(new zk.Event(zk.Widget.$('$' + widgetId + ''),
					'onFinishJSCall', "didCreateObject", {
						toServer : true
					}), 0);
		}
	},
	isGoogleMapLibraryLoaded : false,
	didLoadGoogleMapLibrary : function() {
		vietek.isGoogleMapLibraryLoaded = true;
	},
	VMap : function() {
		var polygon = null;
		var polylines = {};
		var map = null;
		var id = "";
		var infoWindow = null;
		var drawingManager = null;
		var self = this;
		this.initMap = function(mapId) {
			while (vietek.isGoogleMapLibraryLoaded) {
				break;
			}
			map = new google.maps.Map(zk.Widget.$('$' + mapId + '').$n(), {
		          zoom: 5,
		          center: {lat: 16.3776538, lng: 105.0040875}
		        });
			
			self.map = map;
			self.id = mapId;
			google.maps.event.trigger(map, 'resize');
			//TODO
			google.maps.event.addListener(this.map, 'click', function() {
				zAu.send(new zk.Event(zk.Widget.$('$' + mapId + ''), 'onClickVMap',
						mapId, {
							toServer : true
						}), 0);
			});
			// Event mousemove of map, send latlng of cursor to server
			google.maps.event.addListener(this.map, 'mousemove', function(e) {
				var lat = e.latLng.lat();
				var lng = e.latLng.lng();
				var latLng = {
					'data' : {
						'latitude' : lat,
						'longtitude' : lng
					}
				}
				zAu.send(new zk.Event(zk.Widget.$('$' + mapId + ''), 'onMouseMoveVMap',
						latLng, {
							toServer : true
						}), 0);
			});
			// right click on map, send point of cursor to server
			google.maps.event.addListener(this.map, 'rightclick', function(e) {
				var lat = e.latLng.lat();
				var lng = e.latLng.lng();
				var point = new google.maps.LatLng(lat, lng);
				var address = '';
				var geocoder = new google.maps.Geocoder(); // create a geocoder object
				geocoder.geocode({
					'latLng' : point
				}, function(results, status) {
					if (status == google.maps.GeocoderStatus.OK) { // if geocode success
						address = results[0].formatted_address;
						
					}
				});

				var jsonData = {
					'data' : {
						'latitude' : lat,
						'longtitude' : lng,
						'address' : address
					}
				}
				zAu.send(new zk.Event(zk.Widget.$('$' + mapId + ''), 'onRightClickVMap',
						jsonData, {
							toServer : true
						}), 0);
			});
			// Event zoom changed
			self.map.addListener('zoom_changed', function() {
				var zoom = map.getZoom();
				zAu.send(new zk.Event(zk.Widget.$('$' + mapId + ''), 'onZoomChangedVmap',
						zoom, {
							toServer : true
						}), 0);
			});
			// Event drag
			google.maps.event.addListener(map, 'drag', function(){
				zAu.send(new zk.Event(zk.Widget.$('$' + mapId + ''), 'onDragVmap',
						null, {
							toServer : true
						}), 0);
			});
			//TODO Evvent dragstart
			google.maps.event.addListener(map, 'dragstart', function(){
				zAu.send(new zk.Event(zk.Widget.$('$' + mapId + ''), 'onDragStartVmap',
						null, {
							toServer : true
						}), 0);
			});
			//TODO Event dragend
			google.maps.event.addListener(map, 'dragend', function(){
				zAu.send(new zk.Event(zk.Widget.$('$' + mapId + ''), 'onDragEndVmap',
						null, {
							toServer : true
						}), 0);
			});
			//TODO Event bounds_changed
			google.maps.event.addListener(map, 'bounds_changed', function() {
				if ("" + this.getBounds() !== 'undefined' ) {
					var bounds = this.getBounds();
					var lat0 = this.getBounds().getNorthEast().lat();
					var lng0 = this.getBounds().getNorthEast().lng();
					var lat1 = this.getBounds().getSouthWest().lat();
					var lng1 = this.getBounds().getSouthWest().lng();
					var jsonData = {
						'bounds' : {
							'NorthEastLat' : lat0,
							'NorthEastLng' : lng0,
							'SouthWestLat' : lat1,
							'SouthWestLng' : lng1
						}
					};
					zAu.send(new zk.Event(zk.Widget.$('$' + mapId + ''),
							'onBoundsChangedVMap', jsonData, {
								toServer : true
							}), 0);
				}
			});
			//TODO
			google.maps.event.addListener(map, 'center_changed', function(){
				if (""+ this.getCenter() !== 'undefined') {
					var centerData = {
						'center' : {
							'latitude' : this.getCenter().lat(),
							'longtitude' : this.getCenter().lng()
						}
					};
					zAu.send(new zk.Event(zk.Widget.$('$' + mapId + ''),
							'onCenterChangeVMap', centerData, {
								toServer : true
							}), 0);
				}
//				google.maps.event.clearListeners(this, 'center_changed');
			});
			
			google.maps.event.addListener(map, 'maptypeid_changed', function(e) {
				var mapType = map.getMapTypeId();
				zAu.send(new zk.Event(zk.Widget.$('$' + mapId + ''),
						'onTypeChangedVMap', mapType, {
							toServer : true
						}), 0);
			});
			
			zAu.send(new zk.Event(zk.Widget.$('$' + mapId + ''),
					'onDidLoadMap', null, {
						toServer : true
					}), 0);
			
		};
		this.panTo = function(latLng) {
			this.map.panTo(latLng);
		};
		this.panBy = function(x, y){
			this.map.panBy(x,y);
		};
		this.fitBounds = function(bounds){
			this.map.fitBounds(bounds);
		};
		this.panToBounds = function(bounds){
			this.map.panToBounds(bounds);
		};
		this.setCenter = function(possion){
			this.map.setCenter(possion);
		};
		this.setClickableIcons = function(flag){
			this.map.setClickableIcons(flag);
		};
		this.setMapTypeId = function(mapType){
			this.map.setMapTypeId(google.maps.MapTypeId[mapType]);
		};
		this.setZoom = function(zoom){
			this.map.setZoom(zoom);
		};
		this.drawPolygon = function() {
			self.polygon.setMap(null);
			if (self.drawingManager !== null) {
				self.drawingManager.setDrawingMode(google.maps.drawing.OverlayType.POLYLINE);
			}
		};
		this.removeChild = function(childId) {
			var marker = vietek.mapController.markers[childId];
			if(""+marker !== "null" && ""+marker !== "undefined"){
				marker.setMap(null);
			}
			if("" + self.polygon !== 'null' && "" + self.polygon !== 'undefined'){
				self.polygon = null;
			}
			var polyline = vietek.mapController.polylines[childId];
			if("" + polyline !== 'null' && "" + polyline !== 'undefined'){
				polyline.setMap(null);
			}
		};
		this.removeAllChild = function(){
			for(var x in vietek.mapController.markers){
				var marker = vietek.mapController.markers[x];
				if(marker.mapObj !== null){
					if(marker.mapObj === this){
						marker.setMap(null);
					}
				}
			}
			self.polygon = null;
			for(var y in vietek.mapController.polylines){
				var polyline = vietek.mapController.polylines[y];
				if(polyline.mapObj != null){
					if(polyline.mapObj === this){
						polyline.setMap(null);
					}
				}
			}
		};
		this.removeAllMarker = function(){
			for(var x in vietek.mapController.markers){
				var vmarker = vietek.mapController.markers[x];
				if(vmarker.marker.map === this.map){
					vmarker.marker.setMap(null);
				}
			}
		}
	},
	
	VMarker : function() {
		var marker = null;
		var mapObj = null;
		var id = "";
		var self = this;
		var rotate = 0.0;
		var imgSrc = "";
		var content = "";
		var infowindow = null;
		this.setLabelAnchor = function(x, y){
			var anchor = new google.maps.Point(x, y);
			this.marker.set('labelAnchor', anchor);
		},
		this.setVisible = function(flag){
			this.marker.setVisible(flag);
			if(flag===false){
				if(this.infowindow != null){
					this.infowindow.close();
				}
			}
		},
		this.setClickable = function(flag){
			this.marker.setClickable(flag);
			this.setRotate(this.rotate);
		},
		this.setLabel = function(lable){
			this.marker.set('labelContent', lable);
			this.marker.set('labelClass', "vmarker_label");
			this.setRotate(this.rotate);
		},
		this.setContent = function(strContent){
			this.content = strContent;
			if(""+ this.map !== 'undefined' && ""+ this.map !== 'null'){
//				if(""+ this.map.infoWindow !== 'null' && ""+ this.map.infoWindow !== 'undefined'){
//					this.map.infoWindow.close();
//					this.map.infoWindow = null;
//				}
				var gmap = this.map.map;
				if(this.infowindow == null){
					this.infowindow = new google.maps.InfoWindow({
					    content: strContent
					});
				}
				this.infowindow.setOptions({disableAutoPan : false});
				this.marker.addListener('click', function() {
					self.infowindow.open(gmap, this);
				});
			}
			this.setRotate(this.rotate);
		},
		this.setDraggable = function(draggable) {
			this.marker.setDraggable(draggable);
			this.setRotate(this.rotate);
		},
		this.setPosition = function(position) {
			this.setRotate(this.rotate);
			this.marker.setPosition(position);
		},
		this.setRotate = function(angle){
			$("img[src='" + this.imgSrc + "#" + this.marker.id + "']").rotate({angle: this.rotate, animateTo: angle});
			this.rotate = angle;
		},
		this.setIcon = function(imageUrl) {
			this.imgSrc = imageUrl;
			var self = this;
			var image = {
				url: imageUrl + "#" + this.marker.id,
			};
			this.marker.setIcon(image);
//			this.setRotate(this.rotate);
		};
		this.setId = function(newId){
			this.id = newId;
			this.marker.set("id", newId);
			this.marker.id = newId;
		}
		this.setMap = function(mapId) {
			var vmap = null;
			var map = null;
			if(mapId !== null){
				vmap = vietek.mapController.maps[mapId];
				var map = vmap.map;
			}
			this.mapObj = vmap;
			this.marker.setMap(map);
			this.setRotate(this.rotate);
			if(vmap !== null){
				this.marker.addListener('click', function() {
					zAu.send(new zk.Event(zk.Widget.$('$' + vmap.id + ''),
							'onVMarkerClick', self.id, {
								toServer : true
							}), 0);
				});
				this.marker.addListener('drag', function(e) {
					var lat = e.latLng.lat();
					var lng = e.latLng.lng();
					var data = {
							'data' : {
								'markerId' : self.id,
								'latitude' : lat,
								'longtitude' : lng
							}
					}
					zAu.send(new zk.Event(zk.Widget.$('$' + vmap.id + ''),
							'onVMarkerDrag', data, {
								toServer : true
							}), 0);
				});
				this.marker.addListener('dragstart', function(e) {
					var lat = e.latLng.lat();
			        var lng = e.latLng.lng();
			        var data = {
			    			'data' : {
			    				'markerId': self.id,
			    				'latitude' : lat,
			    				'longtitude' : lng
			    			}
			    	};
					zAu.send(new zk.Event(zk.Widget.$('$' + vmap.id + ''),
							'onVMarkerDragStart', data, {
								toServer : true
							}), 0);
				});
				google.maps.event.addListener(this.marker,'dragend',function(e) {
			        var lat = e.latLng.lat();
			        var lng = e.latLng.lng();
			        var data = {
			    			'data' : {
			    				'markerId': self.id,
			    				'latitude' : lat,
			    				'longtitude' : lng
			    			}
			    		};
			        zAu.send(new zk.Event(zk.Widget.$('$' + vmap.id + ''),
							'onVMarkerDragEnd', data, {
								toServer : true
							}), 0);
			    });
			}
		}
		this.initMarker = function(markerId, options){
			this.id = markerId;
			this.marker = new MarkerWithLabel({
				position: options["position"],
				draggable: false,
				labelContent: "",
				anchor: new google.maps.Point(-15, 10),
				labelAnchor : new google.maps.Point(-10, 10),
				labelInBackground : false,
				labelStyle: {opacity: 0.75}
			});
			this.marker.set("id", markerId);
			this.marker.id = markerId;
		}
	},
	
	VPolyline : function(){
		var id = "";
		var path = [];
		var option = [];
		var polyline;
		var mapObj = null;
		this.setVisible = function(visible){
			this.polyline.setVisible(visible);
		}
		this.setWeight = function(val){
			this.polyline.setOptions({strokeWeight : val});
		};
		this.setOpacity = function(opacity){
			this.polyline.setOptions({strokeOpacity: opacity});
		};
		this.setColor = function(color){
			this.polyline.setOptions({strokeColor: color});
		};
		this.setEditable = function(flag){
			this.polyline.setEditable(flag);
		};
		this.setDraggable = function(flag){
			this.polyline.setDraggable(flag);
		};
		this.setOptions = function(options){
			this.polyline.setOptions(options);
		};
		this.setPath = function(path){
			this.polyline.setPath(path);
		};
		this.setMap = function(mapId) {
			var vmap = null;
			var map = null;
			if(mapId !== null){
				vmap = vietek.mapController.maps[mapId];
				map = vmap.map;
			}
			this.mapObj = vmap;
			this.polyline.setMap(map);
		};
		this.initPolyline = function(pId, option){
			this.id = pId;
			this.polyline = new google.maps.Polyline(option);
			this.polyline.set("id", pId);
		}
	},
	
	mapController : {
		maps : {},
		markers : {},
		polylines : {},
		createMap : function(mapId) {
			var map = new vietek.VMap();
			map.initMap(mapId);
			vietek.mapController.maps[mapId] = map;
		},
		deleteMap : function(mapId) {
			var vmap = vietek.mapController.maps[mapId];
			if ("" + vmap !== 'undefined' && "" + vmap !== 'null') {
				delete vietek.mapController.maps[mapId];
				vmap = null;
			}
		},
		checkChild : function(mapId){
			var vmap = vietek.mapController.maps[mapId];
			if ("" + vmap !== 'undefined' && "" + vmap !== 'null') {
				var size = 0;
				for (var key in vmap.markers) {
			        if (vmap.markers.hasOwnProperty(key)) size++;
			    }
			}
		},
		panTo : function(mapId, lat, lng){
			var vmap = vietek.mapController.maps[mapId];
			if ("" + vmap !== 'undefined' && "" + vmap !== 'null') {
				vmap.panTo({"lat" :lat, "lng": lng});
			}
		},
		removeChild : function(mapId, childId) {
			var vmap = vietek.mapController.maps[mapId];
			if ("" + vmap !== 'undefined' && "" + vmap !== 'null') {
				vmap.removeChild(childId);
			}
		},
		removeAllChild : function(mapId){
			var vmap = vietek.mapController.maps[mapId];
			if ("" + vmap !== 'undefined' && "" + vmap !== 'null') {
				vmap.removeAllChild();
			}
		},
		removeAllMarker : function(mapId){
			var vmap = vietek.mapController.maps[mapId];
			//TODO: Sao lai = null?
			if (typeof vmap !== 'undefined') {
				vmap.removeAllMarker();
			}
		},
		hideAllMarker : function(mapId, flag){
			var vmap = vietek.mapController.maps[mapId];
			for(var x in vietek.mapController.markers){
				vietek.mapController.markers[x].setVisible(flag);
			}
		},
		closeAllInfo : function(mapId){
			var vmap = vietek.mapController.maps[mapId];
			for(var x in vietek.mapController.markers){
				var markerObj = vietek.mapController.markers[x];
				if(markerObj.mapObj === vmap){
					if(markerObj.infowindow !== null && typeof markerObj.infowindow !== 'undefined'){
						markerObj.infowindow.setOptions({disableAutoPan: true});
						markerObj.infowindow.close();
					}
				}
			}
		},
		panBy : function(mapId, x, y){
			var vmap = vietek.mapController.maps[mapId];
			if ("" + vmap !== 'undefined' && "" + vmap !== 'null') {
				vmap.panBy(x, y);
			}
		},
		fitBounds : function(mapId, points, sum) {
			var vmap = vietek.mapController.maps[mapId];
			if ("" + vmap !== 'undefined' && "" + vmap !== 'null') {
				var parsedData = JSON.parse(points.toString());
				var bounds = new google.maps.LatLngBounds();
				for (var i = 0; i < sum; i++) {
					var pointsData = parsedData[i];
					var point = new google.maps.LatLng(pointsData['lat'],
							pointsData['lng']);
					bounds.extend(point);
				}
				vmap.fitBounds(bounds);
			}
		},
		panToBounds : function(mapId, data) {
			var vmap = vietek.mapController.maps[mapId];
			if ("" + vmap !== 'undefined' && "" + vmap !== 'null') {
				var parsedData = JSON.parse(data.toString());
				var bounds = new google.maps.LatLngBounds();
				var pointsData = parsedData['bounds'];
				var northEast = new google.maps.LatLng(pointsData['NorthEastLat'],
						pointsData['NorthEastLng']);
				var southWest = new google.maps.LatLng(pointsData['SouthWestLat'],
						pointsData['SouthWestLng']);
				bounds.extend(northEast);
				bounds.extend(southWest);
				vmap.panToBounds(bounds);
			} else {
				var str = 'Cannot use panToBounds() map before map load complete!'
				zAu.send(new zk.Event(zk.Widget.$('$' + mapId + ''), 'onErrorMap', str,
						{
							toServer : true
						}), 0);
			}
		},
		setCenter : function(mapId, lat, lng) {
			var vmap = vietek.mapController.maps[mapId];
			if ("" + vmap !== 'undefined' && "" + vmap !== 'null') {
				var possion = new google.maps.LatLng(lat, lng);
				vmap.setCenter(possion);
			}
		},
		setClickableIcons : function(mapId, flag) {
			var vmap = vietek.mapController.maps[mapId];
			if ("" + vmap !== 'undefined' && "" + vmap !== 'null') {
				vmap.setClickableIcons(flag);
			}
		},
		setMapTypeId : function(mapId, mapType){
			var vmap = vietek.mapController.maps[mapId];
			if ("" + vmap !== 'undefined' && "" + vmap !== 'null') {
				vmap.setMapTypeId(mapType);
			}
		},
		setZoom : function(mapId, zoom){
			var vmap = vietek.mapController.maps[mapId];
			if ("" + vmap !== 'undefined' && "" + vmap !== 'null') {
				vmap.setZoom(zoom);
			}
		},
		drawPolygon : function(mapId){
			var vmap = vietek.mapController.maps[mapId];
			if ("" + vmap !== 'undefined' && "" + vmap !== 'null') {
				vmap.drawPolygon();
			}
		},
		
		addMarker : function(markerId, options) {
			var vmarker = new vietek.VMarker();
			vmarker.initMarker(markerId, options);
			vietek.mapController.markers[markerId] = vmarker;
			zAu.send(new zk.Event(zk.Widget.$('$' + markerId + ''),
					'onDidLoadMarker', null, {
					toServer : true
			}), 0);
		},
		setIdMarker : function(oldId, newId){
			var vmarker = vietek.mapController.markers[oldId];
			if(""+ vmarker !== 'undefined' && "" + vmarker !== 'null'){
				vmarker.setId(newId);
				delete vietek.mapController.markers[oldId];
				vietek.mapController.markers[newId] = vmarker;
			}
			
		},
		setMap : function(mapId, markerId) {
			var vmarker = vietek.mapController.markers[markerId];
//			if (typeof vmarker != 'undefined') {
				vmarker.setMap(mapId);
//			}
		},
		setLabel : function(markerId, label){
			var vmarker = vietek.mapController.markers[markerId];
			vmarker.setLabel(label);
		},
		setLabelAnchor : function(markerId, x, y){
			var vmarker = vietek.mapController.markers[markerId];
			vmarker.setLabelAnchor(x, y);
		},
		setIcon : function(markerId, imgSrc){
			var vmarker = vietek.mapController.markers[markerId];
//			if (""+ vmarker !== 'undefined' && ""+ vmarker !== 'null'){
				vmarker.setIcon(imgSrc);
//			}
		},
		setRotate : function(markerId, angle){
			var vmarker = vietek.mapController.markers[markerId];
			vmarker.setRotate(angle);
		},
		
		setPosition : function(markerId, lat, lng) {
			var vmarker = vietek.mapController.markers[markerId];
//			if ("" + vmarker !== 'undefined' && "" + vmarker !== 'null') {
				var position = {"lat" : lat, "lng" : lng};
				vmarker.setPosition(position);
//			}
		},
		setContent : function(markerId, strContent){
			var vmarker = vietek.mapController.markers[markerId];
			if(strContent.indexOf("&#39;") > -1){
				strContent = strContent.replaceAll("&#39;", "'");
			}
			if(strContent.indexOf('&quot;') > -1){
				strContent = strContent.replaceAll("&quot;", '"');
			}
			if(strContent.indexOf('&#92;') > -1){
				strContent = strContent.replaceAll("&#92;", "\\");
			}
			vmarker.setContent(strContent);
		},
		setOpenContent : function(mapId, markerId, flag){
			var vmap = vietek.mapController.maps[mapId];
			if("" + vmap !== 'undefined' && "" + vmap !== 'null'){
				var markerObj = vietek.mapController.markers[markerId];
				if(""+ markerObj !== 'undefined' && ""+ markerObj !== 'null'){
					if(flag === true){
						if(markerObj.infowindow == null){
							markerObj.infowindow = new google.maps.InfoWindow({
							    content: markerObj.content
							});
							markerObj.infowindow.setOptions({disableAutoPan : false});
						}
						markerObj.marker.addListener('click', function() {
							markerObj.infowindow.open(vmap.map, this);
					    });
						markerObj.infowindow.open(vmap.map, markerObj.marker);
					} else {
						if(markerObj.infowindow!= null){
							markerObj.infowindow.close();
						}
					}
				}
			}
		},
		setDraggable : function(markerId, flag){
			var vmarker = vietek.mapController.markers[markerId];
			//TODO: Sao lai = null?
//			if (typeof vmarker !== 'undefined')
				vmarker.setDraggable(flag);
		},
		setClickable : function(markerId, flag) {
			var marker = vietek.mapController.markers[markerId];
			marker.setClickable(flag);
		},
		setVisible : function(markerId, flag) {
			var marker = vietek.mapController.markers[markerId];
			if(""+ marker !== 'undefined' && ""+ marker !== 'null'){
				marker.setVisible(flag);
			}
			
		},
		removeMarker : function(markerId) {
			var vmarker = vietek.mapController.markers[markerId];
//			if (typeof vmarker != 'undefined') {
				vmarker.marker.setMap(null);
				delete vietek.mapController.markers[markerId];
//			}
		},
		autoPanVMarker : function(markerId, flag){
			var vmarker = vietek.mapController.markers[markerId];
			if(typeof vmarker.infowindow !== 'undefined' && vmarker.infowindow !== null){
				vmarker.infowindow.setOptions({disableAutoPan : flag});
			}
		},
		
		/**
		 * VPolyline
		 */
		addVPolyline : function(polylineId, options){
			while(options.indexOf("&quot;")!= -1 || options.indexOf("&#39;")!= -1){
				options = options.replace('&quot;', '"');
				options = options.replace("&#39;", "'");
			}
			var option = JSON.parse(options);
			var polyline = new vietek.VPolyline();
			polyline.initPolyline(polylineId, option);
			vietek.mapController.polylines[polylineId] = polyline;
		},
		setMapVPolyline : function(mapId, pId){
			var polyline = vietek.mapController.polylines[pId];
			polyline.setMap(mapId);
		},
		setPathVPolyline : function(pId, options){
			var polyline = vietek.mapController.polylines[pId];
			while(options.indexOf("&quot;")!= -1 || options.indexOf("&#39;")!= -1){
				options = options.replace('&quot;', '"');
				options = options.replace("&#39;", "'");
			}
			var option = JSON.parse(options);
			polyline.setPath(option);
		},
		setOptionsVPolyline : function(pId, strOptions){
			var polyline = vietek.mapController.polylines[pId];
			while(strOptions.indexOf("&quot;")!= -1 || strOptions.indexOf("&#39;")!= -1){
				strOptions = strOptions.replace('&quot;', '"');
				strOptions = strOptions.replace("&#39;", "'");
			}
			var options = JSON.parse(strOptions);
			polyline.setOptions(options);
		},
		setDraggableVPolyline : function(pId, flag){
			var polyline = vietek.mapController.polylines[pId];
			polyline.setDraggable(flag);
		},
		setEditableVpolyline : function(pId, flag){
			var polyline = vietek.mapController.polylines[pId];
			polyline.setDraggable(flag);
		},
		setColorVPolyline : function(pId, color){
			var polyline = vietek.mapController.polylines[pId];
			polyline.setColor(color);
		},
		setOpacityVPolyline : function(pId, opacity){
			var polyline = vietek.mapController.polylines[pId];
			polyline.setOpacity(opacity);
		},
		setWeightVPolyline : function(pId, val){
			var polyline = vietek.mapController.polylines[pId];
			polyline.setWeight(val);
		},
		setVisibleVPolyline : function(pId, visible){
			var polyline = vietek.mapController.polylines[pId];
			polyline.setVisible(visible);
		},
		
		/**
		 * VInfoWindow
		 */
		addInfo : function(wId){
			
		}
	}
}
