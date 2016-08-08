var isLoadGoogleMapLibraries = false;
function VMaps(mapId) {
	this.id = mapId;
	this.isSearchbox = false;
	this.tilt = '';
	this.ready = false;
	this.gmaps = null;
	this.infoWindow = null;
	this.controlPossion = 'TOP_CENTER';
	this.markerSearch = null; // Luu marker danh dau diem tim kiem
	this.polylines={}; // Luu polyline da ve
	this.polygon = null; // Luu polygon da ve
	this.drawingManager = null; // ve polyline va polygon
	this.centerPoint;// Center point of map
	this.zoom = 15;	//Muc zoom cua map
	this.mapTypeId; // Kieu map hien thi
	
}

/**
 * Khoi tao maps
 */
VMaps.prototype.initialize = function() {
	var divMap=zk.Widget.$(jq('$' + this.id + ''));
//	this.centerPoint = new google.maps.LatLng(20.9719317, 105.83685);
//	this.mapTypeId = 'ROADMAP';
	var mapProperties = {
		center : this.centerPoint,
		zoom : this.zoom,
		mapTypeId : google.maps.MapTypeId[this.mapTypeId]
	};
	this.infoWindow = null;
	var map = new google.maps.Map(zk.Widget.$(jq('$' + this.id + '')).$n(),
			mapProperties);
	this.gmaps = map;
	var centerData = {
		'center' : {
			'latitude' : this.centerPoint.lat(),
			'longtitude' : this.centerPoint.lng()
		}
	};

	google.maps.event.addListener(this.gmaps, 'tilesloaded', function() {
		this.ready=true;
	});
	
	google.maps.event.addDomListener(window, "resize", function() {
		 google.maps.event.trigger(this.gmaps, "resize");
		});
	
	if(this.tilt.length>0)
		this.gmaps.setTilt(this.tilt);
	if(this.isSearchbox == true){
		add_placecomplete_search(this.gmaps, this.controlPossion);
	}
	
	event_on_click(this.id, this.gmaps);
	event_bounds_changed(this.id, this.gmaps);
	event_center_change(this.id, this.gmaps);
	event_map_right_click(this.id, this.gmaps);
	event_mouse_move(this.id, this.gmaps);
	event_maptypeid_changed(this.id, this.gmaps);
	event_zoom_changed(this.id, this.gmaps);
	

	zAu.send(new zk.Event(zk.Widget.$('$' + this.id + ''), 'onCenterChange',
			centerData, {
				toServer : true
			}), 0);
	zAu.send(new zk.Event(zk.Widget.$('$' + this.id + ''), 'onZoomChanged',
			this.zoom, {
				toServer : true
			}), 0);
	var newDrawingManager = new google.maps.drawing.DrawingManager(
			{
				drawingMode : null,
				drawingControl : false,
				map : this.gmaps,
				markerOptions : {
					icon : 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png'
				},
				circleOptions : {
					fillColor : '#ffff00',
					fillOpacity : 1,
					strokeWeight : 5,
					clickable : false,
					editable : true,
					zIndex : 1
				}
			});
	this.drawingManager = newDrawingManager;
	var newPolygon;
	google.maps.event.addListener(newDrawingManager, 'overlaycomplete', function(
			event) {
		if (event.type == google.maps.drawing.OverlayType.POLYLINE) {
			newDrawingManager.setDrawingMode(null);
			var lines = event.overlay.getPath();
			newPolygon = new google.maps.Polygon({
				path : lines,
				geodesic : true,
				strokeColor : '#000000',
				strokeOpacity : 0.8,
				strokeWeight : 2,
				fillColor : '#000000',
				fillOpacity : 0.2
			});
			newPolygon.setMap(map);
			divMap.set('polygon',newPolygon);
			this.polygon = newPolygon;
			event.overlay.setMap(null);
		}
	});
	
};

function event_on_click(id, vmap){
	google.maps.event.addListener(vmap, 'click', function(e) {
		var lat = e.latLng.lat();
		var lng = e.latLng.lng();

		var jsonData = {
			'data' : {
				'latitude' : lat,
				'longtitude' : lng
			}
		}
		zAu.send(new zk.Event(zk.Widget.$('$' + id + ''), 'onMapClick',
				jsonData, {
					toServer : true
				}), 0);
	});
}
/**
 * Event bound_change maps
 */
function event_bounds_changed(id, vmap) {
		google.maps.event.addListener(vmap, 'bounds_changed', function() {
			if (typeof vmap.getBounds() != 'undefined') {
				var bounds = vmap.getBounds();
				var lat0 = vmap.getBounds().getNorthEast().lat();
				var lng0 = vmap.getBounds().getNorthEast().lng();
				var lat1 = vmap.getBounds().getSouthWest().lat();
				var lng1 = vmap.getBounds().getSouthWest().lng();
				var jsonData = {
					'bounds' : {
						'NorthEastLat' : lat0,
						'NorthEastLng' : lng0,
						'SouthWestLat' : lat1,
						'SouthWestLng' : lng1
					}
				};
				zAu.send(new zk.Event(zk.Widget.$('$' + id + ''),
						'onBoundsChanged', jsonData, {
							toServer : true
						}), 0);
			}
		});
}

function event_mouse_move(id, vmap) {
		google.maps.event.addListener(vmap, 'mousemove', function(e) {
			var lat = e.latLng.lat();
			var lng = e.latLng.lng();
			var latLng = {
				'data' : {
					'latitude' : lat,
					'longtitude' : lng
				}
			}
			zAu.send(new zk.Event(zk.Widget.$('$' + id + ''), 'onMouseMove',
					latLng, {
						toServer : true
					}), 0);
		});
}
/**
 * Event center_change map
 * 
 * @param mapId
 * 
 */
function event_center_change(id, vmap) {
	google.maps.event.addListener(vmap, 'center_changed', function(){
		if (typeof vmap.getCenter() !== 'undefined') {
			var centerData = {
				'center' : {
					'latitude' : vmap.getCenter().lat(),
					'longtitude' : vmap.getCenter().lng()
				}
			};
			zAu.send(new zk.Event(zk.Widget.$('$' + id + ''),
					'onCenterChange', centerData, {
						toServer : true
					}), 0);
		}
		google.maps.event.clearListeners(vmap, 'center_changed');
	});
}

/**
 * Khoi tao textbox tim kiem khi khoi tao map Chi goi trong ham khoi tao map
 */
function add_placecomplete_search(vmap, controlsPossion) {
	var input = document.createElement("input");
	input.id = "search_" + this.id;
	input.type = "text";
	input.style = "position: absolute; width: 320px; height: 30px; font-size: 14px; font-family: sans-serif; border: 1px solid green; padding: 3px; margin-top: 10px; border-radius: 3px;"
	document.getElementsByTagName('body')[0].appendChild(input);
	vmap.controls[google.maps.ControlPosition[controlsPossion]].push(input);
	var autocomplete = new google.maps.places.Autocomplete(input);
	autocomplete.bindTo('bounds', vmap);
	autocomplete
			.addListener(
					'place_changed',
					function() {
						var place = autocomplete.getPlace();
						if (!place.geometry) {
							window
									.alert("Autocomplete's returned place contains no geometry");
							return;
						}
						vmap.setCenter(place.geometry.location);
						if(this.markerSearch==null){
							this.markerSearch = new google.maps.Marker({
								map : vmap,
								anchorPoint : new google.maps.Point(
										place.geometry.location)
							});
							this.markerSearch.setIcon(/** @type {google.maps.Icon} */
							({
								url : place.icon,
								size : new google.maps.Size(71, 71),
								origin : new google.maps.Point(0, 0),
								anchor : new google.maps.Point(17, 34),
								scaledSize : new google.maps.Size(35, 35)
							}));
						}
						this.markerSearch.setPosition(place.geometry.location);
						this.markerSearch.setVisible(true);
						var infowindow = new google.maps.InfoWindow();
						infowindow.setContent('<div><strong>' + place.name
								+ '</strong></div>');
						this.markerSearch.addListener('click', function() {
							infowindow.open(vmap, this.markerSearch);
						});

						var address = '';
						if (place.address_components) {
							address = [
									(place.address_components[0]
											&& place.address_components[0].short_name || ''),
									(place.address_components[1]
											&& place.address_components[1].short_name || ''),
									(place.address_components[2]
											&& place.address_components[2].short_name || '') ]
									.join(' ');
						}
					});
}

function event_map_right_click(id, vmap) {
	google.maps.event.addListener(vmap, 'rightclick', function(e) {
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
		zAu.send(new zk.Event(zk.Widget.$('$' + id + ''), 'onMapRightClick',
				jsonData, {
					toServer : true
				}), 0);
	});
}

/**
 * Khi thay doi kieu hien thi maps
 */
function event_maptypeid_changed(id, vmap) {
	google.maps.event.addListener(vmap, 'maptypeid_changed', function(e) {
		var mapType = vmap.getMapTypeId();
		zAu.send(new zk.Event(zk.Widget.$('$' + id + ''),
				'onMapTypeChanged', mapType, {
					toServer : true
				}), 0);
	});
}

/**
 * Khi thay doi gia tri zoom
 */
function event_zoom_changed(id, vmap) {
	vmap.addListener('zoom_changed', function() {
		this.zoom = vmap.getZoom();
		zAu.send(new zk.Event(zk.Widget.$('$' + id + ''), 'onZoomChanged',
				this.zoom, {
					toServer : true
				}), 0);
	  });
}

/**
 * 
 * Cac ham su dung cho VMap
 * 
 */
function vmapFitBounds(mapObj, bounds) {
	mapObj.gmaps.fitBounds(bounds);
}

function vmapPanBy(mapObj, x, y) {
	mapObj.gmaps.panBy(x, y);
}

function vmapPanTo(mapObj, latLng){
	mapObj.gmaps.panTo(latLng);
}

function vmapPanToBounds(mapObj, bounds) {
	mapObj.gmaps.panToBounds(bounds);
}

function vmapSetCenter(mapObj, possion){
	mapObj.centerPoint = possion;
	if(mapObj.gmaps !== null)
		mapObj.gmaps.setCenter(possion);
}

function vmapSetClickableIcons(mapObj,  flag){
	mapObj.gmaps.setClickableIcons(flag);
}

function vmapSetMapTypeId(mapObj, mapType){
	mapObj.mapTypeId = mapType;
	mapObj.gmaps.setMapTypeId(google.maps.MapTypeId[mapType]);
}

function vmapSetOptions(mapObj, options){
	mapObj.gmaps.setOptions(options);
}

function vmapSetZoom(mapObj, zoom){
	mapObj.zoom = zoom;
	mapObj.gmaps.setZoom(zoom);
}

function vmapDrawPolyline(mapObj) {
	var divMap=zk.Widget.$(jq('$' + mapObj.id + ''));
	var polygon = divMap.get('polygon');
	if(typeof polygon!=='undefined')
		polygon.setMap(null);
	if (mapObj.drawingManager !== null) {
		mapObj.drawingManager.setDrawingMode(google.maps.drawing.OverlayType.POLYLINE);
	}
}

function vmapSetTilt(mapObj, tilt){
	mapObj.tilt = tilt;
	mapObj.gmaps.setTilt(tilt);
}

/**
 * Tao hoac xoa textbox tim kiem dia chi tren map
 * 
 * @param mapId
 * @param vmap
 */
function vmapAutocompleteSearch(mapObj, flag, possion) {
	if(flag == true){
		vmapCreateSearch(mapObj, possion)
	} else {
		vmapDeleteSearch(mapObj);
	}
}

/**
 * Code tao textbox tim kiem dia chi tren map
 */
function vmapCreateSearch(mapObj, possion){
	var input = document.createElement("input");
	input.id = "search_" + mapObj.id;
	input.type = "text";
	input.style = "width: 320px; height: 30px; font-size: 14px; font-family: sans-serif; border: 1px solid green; padding: 3px; margin-top: 10px; border-radius: 3px;"
	document.getElementsByTagName('body')[0].appendChild(input);

	vmap.controls[google.maps.ControlPosition[possion]].push(input);
	
	var autocomplete = new google.maps.places.Autocomplete(input);
	autocomplete.bindTo('bounds', mapObj.gmaps);
	autocomplete
			.addListener(
					'place_changed',
					function() {
						var place = autocomplete.getPlace();
						if (!place.geometry) {
							window
									.alert("Autocomplete's returned place contains no geometry");
							return;
						}
						mapObj.gmaps.setCenter(place.geometry.location);
						var marker = null;
						if (mapObj.markerSearch == null) {
							marker = new google.maps.Marker({
								map : mapObj.gmaps,
								anchorPoint : new google.maps.Point(
										place.geometry.location)
							});
							marker.setIcon(/** @type {google.maps.Icon} */
									({
										url : place.icon,
										size : new google.maps.Size(71, 71),
										origin : new google.maps.Point(0, 0),
										anchor : new google.maps.Point(17, 34),
										scaledSize : new google.maps.Size(35, 35)
									}));
							
						} else {
							marker = mapObj.markerSearch;
						}

						marker.setPosition(place.geometry.location);
						marker.setVisible(true);
						var infowindow = new google.maps.InfoWindow();
						infowindow.setContent('<div><strong>' + place.name
								+ '</strong></div>');
						marker.addListener('click', function() {
							infowindow.open(mapObj.gmaps, marker);
						});

						var address = '';
						if (place.address_components) {
							address = [
									(place.address_components[0]
											&& place.address_components[0].short_name || ''),
									(place.address_components[1]
											&& place.address_components[1].short_name || ''),
									(place.address_components[2]
											&& place.address_components[2].short_name || '') ]
									.join(' ');
						}
					});
}

/**
 * Xoa textbox tim kiem dia chi tren maps
 * 
 * @param mapObj
 */
function vmapDeleteSearch(mapObj){
	var inputId = "search_" + mapObj.id;
	var input = document.getElementById(inputId);
	if (typeof input != 'undefined') {
		document.getElementsByTagName('body')[0].removeChild(input);
	}
}



