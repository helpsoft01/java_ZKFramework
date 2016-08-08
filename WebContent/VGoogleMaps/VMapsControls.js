/**
 * 
 */

$(document).ready(function() {
	loadScript();
});

// Load script object vmaps
function loadScript() {
	var x = document.getElementsByTagName("script");
	var vmaps_added = false;
	var vmarker_added = false;
	var rotate_added = false;
	var markerlabel_added = false;
	var mapapi_added = false;
	// Check js already exist
	for (var i = 0; i < x.length; i++) {
		if (x[i].src == "./VGoogleMaps/VMaps.js") {
			vmaps_added = true;
		}
		if (x[i].src == './VGoogleMaps/VMarker.js') {
			vmarker_added = true;
		}
		if (x[i].src == './VGoogleMaps/RotateIcon.js') {
			rotate_added = true;
		}
		if (x[i].src == '.VGoogleMaps/markerwithlabel.js') {
			markerlabel_added = true;
		}
	}
	
	if (vmaps_added == false) {
		var scriptTag = document.createElement("script");
		scriptTag.type = "text/javascript";
		scriptTag.src = "./VGoogleMaps/VMaps.js";
		scriptTag.charset = "UTF-8";
		(document.getElementsByTagName("head")[0] || document.documentElement)
				.appendChild(scriptTag);
	}
	if (vmarker_added == false) {
		var scriptTag = document.createElement("script");
		scriptTag.type = "text/javascript";
		scriptTag.src = "./VGoogleMaps/VMarker.js";
		scriptTag.charset = "UTF-8";
		(document.getElementsByTagName("head")[0] || document.documentElement)
				.appendChild(scriptTag);
	}
	if (rotate_added == false) {
		var scriptTag = document.createElement("script");
		scriptTag.type = "text/javascript";
		scriptTag.src = "./VGoogleMaps/RotateIcon.js";
		scriptTag.charset = "UTF-8";
		(document.getElementsByTagName("head")[0] || document.documentElement)
				.appendChild(scriptTag);
	}
	if (markerlabel_added == false) {
		var scriptTag = document.createElement("script");
		scriptTag.type = "text/javascript";
		scriptTag.src = "./VGoogleMaps/markerwithlabel.js";
		scriptTag.charset = "UTF-8";
		(document.getElementsByTagName("head")[0] || document.documentElement)
				.appendChild(scriptTag);
	}
}

function addMapAPIwithKey(src){
	var x = document.getElementsByTagName("script");
	var added = false;
	for (var i = 0; i < x.length; i++) {
		if (x[i].src == "https://maps.googleapis.com/maps/api/js?sensor=false&API_KEY=AIzaSyAocRnSkbbpbc2yed6l-JnO28vmLo5Z0n0&libraries=places,drawing") {
			console.log('added google api: '+added);
			x[i].src = src;
			added = true;
		}
	}
	
	if(added === false){
		var scriptTag = document.createElement("script");
		scriptTag.type = "text/javascript";
		scriptTag.src = src;
		scriptTag.charset = "UTF-8";
		$('body').prepend(scriptTag);
		(document.getElementsByTagName("body")[0] || document.documentElement)
				.appendChild(scriptTag);
	}
}


var ListMaps = {}; // HashMap cac maps
var ListMarkers = {}; // HashMap cac marker thuoc cac vmap

/**
 * Khoi tao mot map moi
 * 
 * @param mapId
 */
function createVMaps(mapId) {
	var vmap = new VMaps(mapId);
	vmap.initialize();
	vmap.gmaps.addEventListener('google-map-ready', function(e) {
		alert('Map loaded!');
	});

	google.maps.event.addListener(vmap.gmaps, 'idle', function() {
		vmap.ready = true;
		ListMaps[mapId] = vmap;
		// Check da co marker nao duoc khoi tao hay chua
		var markers = ListMarkers[mapId];
		if (typeof markers !== 'undefined') {
			for ( var x in markers) {
				var markerObj = markers[x];
				vmarkerSetMap(markerObj, vmap.gmaps);
			}
		}
	});

}

function createVMaps(mapId, latCenter, lngCenter, mapType, tilt, zoom,
		isSearchbox, controlPossion) {
	var vmap = new VMaps(mapId);
	vmap.id = mapId;
	vmap.isSearchbox = true;
	vmap.tilt = tilt;
	vmap.controlPossion = controlPossion;
	var center = new google.maps.LatLng(latCenter, lngCenter);
	vmap.centerPoint = center;
	vmap.zoom = zoom;
	vmap.mapTypeId = mapType;
	vmap.initialize();
	google.maps.event.addListener(vmap.gmaps, 'tilesloaded', function() {
		vmap.ready = true;
		ListMaps[mapId] = vmap;
		// Check da co marker nao duoc khoi tao hay chua
		var markers = ListMarkers[mapId];
		if (typeof markers !== 'undefined') {
			for ( var x in markers) {
				var markerObj = markers[x];
				vmarkerSetMap(markerObj, vmap);
			}
		}
	});
}

/**
 * Xoa hoac them textbox tim kiem cho map Goi khi thu tuc
 * setAutoCompleteSearch() tren server dc goi
 * 
 * @param mapId
 * @param visble
 */
function initSearchVMaps(mapId, visble, possion) {
	var vmap = ListMaps[mapId];
	if (typeof vmap != 'undefined') {
		if (!visble) {
			if (typeof input != 'undefined') {
				var inputId = "search_" + mapId;
				var input = document.getElementById(inputId);
				document.getElementsByTagName('body')[0].removeChild(input);
			} else {
				createAutocompleteSearch(mapId, vmap, possion);
			}
		}
	}
}

/**
 * Sets the viewport to contain the given bounds
 * 
 * @param mapId
 * @param points
 * @param sum
 */
function fitBoundsVMaps(mapId, points, sum) {
	var vmap = ListMaps[mapId];
	if (typeof vmap != 'undefined') {
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
}

/**
 * 
 * @param mapId
 * @param x
 * @param y
 */
function panByVMaps(mapId, x, y) {
	var vmap = ListMaps[mapId];
	if (typeof vmap !== 'undefined') {
		vmapPanBy(vmap, x, y);
	} else {
		var str = 'Cannot use panBy() map before map load complete!'
		zAu.send(new zk.Event(zk.Widget.$('$' + mapId + ''), 'onErrorMap', str,
				{
					toServer : true
				}), 0);
	}
}

function panToVMaps(mapId, lat, lng) {
	var vmap = ListMaps[mapId];
	if (typeof vmap !== 'undefined') {
		var possion = new google.maps.LatLng(lat, lng);
		vmapPanTo(vmap, possion);
	} else {
		var str = 'Cannot use panTo() map before map load complete!'
		zAu.send(new zk.Event(zk.Widget.$('$' + mapId + ''), 'onErrorMap', str,
				{
					toServer : true
				}), 0);
	}
}

function panToBoundsVMaps(mapId, data) {
	var vmap = ListMaps[mapId];
	if (typeof vmap !== 'undefined') {
		var parsedData = JSON.parse(data.toString());
		var bounds = new google.maps.LatLngBounds();
		var pointsData = parsedData['bounds'];
		var northEast = new google.maps.LatLng(pointsData['NorthEastLat'],
				pointsData['NorthEastLng']);
		var southWest = new google.maps.LatLng(pointsData['SouthWestLat'],
				pointsData['SouthWestLng']);
		bounds.extend(northEast);
		bounds.extend(southWest);
		vmapPanToBounds(vmap, bounds);
	} else {
		var str = 'Cannot use panToBounds() map before map load complete!'
		zAu.send(new zk.Event(zk.Widget.$('$' + mapId + ''), 'onErrorMap', str,
				{
					toServer : true
				}), 0);
	}
}

function setCenterVMaps(mapId, lat, lng) {
	var vmap = ListMaps[mapId];
	if (typeof vmap !== 'undefined') {
		var possion = new google.maps.LatLng(lat, lng);
		vmapSetCenter(vmap, possion);
	}
}

function setClickableIconVMaps(mapId, flag) {
	var vmap = ListMaps[mapId];
	if (typeof vmap !== 'undefined') {
		vmapSetClickableIcons(vmap, flag);
	}
}

function setMapTypeIdVMaps(mapId, type) {
	var vmap = ListMaps[mapId];
	if (typeof vmap !== 'undefined') {
		vmapSetMapTypeId(vmap, type);
	}
}

function setZoomVMaps(mapId, zoom) {
	var vmap = ListMaps[mapId];
	if (typeof vmap !== 'undefined') {
		vmapSetZoom(vmap, zoom);
	}
}

/**
 * Tao textbox tim kiem dia chi tren maps
 * 
 * @param mapId
 * @param vmap
 * @param possion
 */
function setSearchVMaps(mapId, flag, possion) {
	var mapObj = ListMaps[mapId];
	if (typeof mapObj !== 'undefined') {
		if(typeof mapObj.gmaps !='undefined')
			vmapAutocompleteSearch(mapObj, flag, possion);
	}
}

/**
 * Draw polylines on map
 * 
 * @param mapId
 */
function drawingPolylineVMaps(mapId) {
	var vmap = ListMaps[mapId];
	if (typeof vmap !== 'undefined') {
		vmapDrawPolyline(vmap);
	}
}

function setTiltVMaps(mapId, tilt) {
	var mapObj = ListMaps[mapId];
	if (typeof mapObj != 'undefined') {
		vmapSetTilt(mapObj, tilt);
	}
}

function hideAllMarker(mapId) {
	var mapObj = ListMaps[mapId];
	if(typeof mapObj != 'undefined'){
		if (mapObj.infoWindow != null) {
			mapObj.infoWindow.close();
			mapObj.infoWindow = null;
		}
	}
	var markers = ListMarkers[mapId];
	if (typeof markers != 'undefined') {
		for ( var x in markers) {
			vmarkerSetVisible(markers[x], false);
		}
	}
}

/**
 * Khoi tao 1 marker moi
 * 
 * @param mapId
 * @param id
 * @param markerID
 * @param image
 * @param angle
 * @param label
 * @param lati
 * @param longi
 * @param contentMsg
 */

function addVMarker(mapId, id, markerID, image, angle, label, lati, longi,
		contentMsg, clickable, draggable, opacity, visible, animation) {
	var vmap = ListMaps[mapId];
	var markerObj = new VMarker();
	if (typeof vmap != 'undefined') {
		markerObj.init(vmap, id, markerID, image, angle, label, lati, longi,
				contentMsg);
	} else {
		markerObj.init(null, id, markerID, image, angle, label, lati, longi,
				contentMsg);
	}

	// Them marker vao list, neu chua map chua load xong se render marker trong
	// list
	var markers = ListMarkers[mapId];
	if (typeof markers !== 'undefined') {
		markers[markerID] = markerObj;
	} else {
		markers = {};
		markers[markerID] = markerObj;
		ListMarkers[mapId] = markers;
	}
}

function setImageVMarker(mapId, markerID, image) {
	var markers = ListMarkers[mapId];
	if (typeof markers !== 'undefined') {
		var markerObj = markers[markerID];
		if (typeof markerObj !== 'undefined') {
			vmarkerSetImage(markerObj, image);
		}
	}

}

function setContentVMarker(mapId, markerID, content) {
	var mapObj = ListMaps[mapId];
	var markers = ListMarkers[mapId];
	if (typeof markers !== 'undefined') {
		var markerObj = markers[markerID];
		if (typeof markerObj !== 'undefined') {
			vmarkerSetContent(markerObj, content);
		}
	}
}

function setOpenContentVMarker(mapId, markerID) {
	var mapObj = ListMaps[mapId];
	var markers = ListMarkers[mapId];
	if (typeof markers !== 'undefined') {
		var markerObj = markers[markerID];
		if (typeof markerObj != 'undefined') {
			// vmarkerOpenContent(markerObj);
			if (mapObj.infoWindow != null) {
				mapObj.infoWindow.close();
				mapObj.infoWindow = null;
			}
			mapObj.infoWindow = new google.maps.InfoWindow({
				content : markerObj.content
			});
			mapObj.infoWindow.open(mapObj.gmaps, markerObj.marker);
		}
	}
}

function setPossionVMarker(mapId, markerID, lati, longi) {
	var markers = ListMarkers[mapId];
	if (typeof markers !== 'undefined') {
		var markerObj = markers[markerID];
		if (typeof markerObj !== 'undefined') {
			var latLng = new google.maps.LatLng(lati, longi);
			vmarkerSetPossion(markerObj, latLng);
		}
	}
}

function setRotationVMarker(mapId, markerID, angle) {
	var markers = ListMarkers[mapId];
	if (typeof markers !== 'undefined') {
		var markerObj = markers[markerID];
		if (typeof markerObj !== 'undefined') {
			vmarkerSetRotate(markerObj, angle);
		}
	}
}

function setClickableVMarker(mapId, markerID, flag) {
	var markers = ListMarkers[mapId];
	if (typeof markers !== 'undefined') {
		var markerObj = markers[markerID];
		if (typeof markerObj !== 'undefined') {
			vmarkerSetClickable(markerObj, flag);
		}
	}
}

function setDraggableVMarker(mapId, markerID, flag) {
	var markers = ListMarkers[mapId];
	if (typeof markers !== 'undefined') {
		var markerObj = markers[markerID];
		if (typeof markerObj !== 'undefined') {
			vmarkerSetDraggable(markerObj, flag);
		}
	}
}

function setAnimationVMarker(mapId, markerID, animation) {
	var markers = ListMarkers[mapId];
	if (typeof markers !== 'undefined') {
		var markerObj = markers[markerID];
		if (typeof markerObj !== 'undefined') {
			vmarkerSetAnimation(markerObj, animation);
		}
	}
}

function setOpacityVMarker(mapId, markerID, opacity) {
	var markers = ListMarkers[mapId];
	if (typeof markers !== 'undefined') {
		var markerObj = markers[markerID];
		if (typeof markerObj !== 'undefined') {
			vmarkerSetOpacity(markerObj, opacity);
		}
	}
}

function setVisibleVMarker(mapId, markerID, flag) {
	var markers = ListMarkers[mapId];
	if (typeof markers !== 'undefined') {
		var markerObj = markers[markerID];
		if (typeof markerObj !== 'undefined') {
			vmarkerSetVisible(markerObj, flag);
		}
	}
}

function setLabelVMarker(mapId, markerID, label) {
	var markers = ListMarkers[mapId];
	if (typeof markers !== 'undefined') {
		var markerObj = markers[markerID];
		if (typeof markerObj !== 'undefined') {
			vmarkerSetLable(markerObj, label);
		}
	}
}

function setVMapVMarker(mapId, markerID) {
	var vmap = ListMaps[mapId];
	var markers = ListMarkers[mapId];
	if (typeof markers !== 'undefined') {
		var markerObj = markers[markerID];
		if (typeof markerObj !== 'undefined') {
			if (typeof vmap !== 'undefined') {
				vmarkerSetMap(markerObj, vmap);
			}
		}
	}
}
