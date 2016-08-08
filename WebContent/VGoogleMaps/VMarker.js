function VMarker() {
	this.id = '';
	this.markerId = '';
	this.object = '';
	this.imgSrc = '';
	this.latitude = 0.0;
	this.longitude = 0.0;
	this.imgAngle = 0.0;
	this.label = '';
	this.maps = null;
	this.options;
	this.marker = null;
	this.content = '';
	this.infoWindow = new google.maps.InfoWindow({
		content : this.content
	});
	this.possion = new google.maps.LatLng(this.longitude, this.longitude);
	this.animation = google.maps.Animation.DROP;
	this.clickable = true;
	this.draggable = false;
	this.opacity = 1;
	this.visible = true;

}

VMarker.prototype.init = function(vmap, mId, markerID, image, angle, label,
		lati, longi, contentMsg, clickable, draggable, opacity, visible,
		animation) {
	this.maps = vmap;
	this.id = mId;
	this.markerId = markerID;
	this.imgSrc = image;
	this.imgAngle = angle;
	this.label = label;
	this.latitude = lati;
	this.longitude = longi;
	this.possion = new google.maps.LatLng(lati, longi);
	this.content = contentMsg;
	this.clickable = clickable;
	this.draggable = draggable;
	this.opacity = opacity;
	this.visible = visible;
	this.animation = google.maps.Animation[animation];
	this.marker = new MarkerWithLabel({
		position : this.possion,
		map : vmap.gmaps,
		labelContent : this.label,
		labelAnchor : new google.maps.Point(5, 16),
		labelClass : "vmarker_label", // the CSS class for the label
		labelInBackground : false
	});

	// this.marker = new google.maps.Marker({
	// icon : image,
	// map : vmap.gmaps,
	// position : this.possion,
	// labelContent: "ABCD",
	// labelAnchor: new google.maps.Point(15, 65),
	// labelClass: "vmarker_label", // the CSS class for the label
	// labelInBackground: true,
	// draggable : false
	// });
	if (typeof vmap != 'undefined') {
		this.marker.addListener('click', function() {

			zAu.send(new zk.Event(zk.Widget.$('$' + vmap.id + ''),
					'onMarkerClick', this.markerId, {
						toServer : true
					}), 0);
		});

		vmap.infoWindow = new google.maps.InfoWindow({
			content : contentMsg
		});
		google.maps.event.addListener(this.marker, 'click', function() {
			vmap.infoWindow.open(vmap.gmaps, this.marker);
		});

	}
	this.marker.setIcon({
		url : RotateIcon.makeIcon(image).setRotation({
			deg : angle
		}).getUrl(),
	});

	// this.infoWindow = new google.maps.InfoWindow({
	// content : contentMsg
	// });

	// if(vmap != null){
	// if(typeof vmap.gmaps !='undefined'){
	// google.maps.event.addListener(this.marker, 'click', function() {
	// console.log(this.infoWindow);
	// this.infoWindow.open(vmap.gmaps, this.marker);
	// });
	// }
	//		
	// }

	if (this.id.length > 0) {
		this.marker.set('id', this.id);
	}
	this.marker.set('markerId', markerID);

}

function vmarkerSetMap(markerObj, vmap) {
	markerObj.infoWindow = new google.maps.InfoWindow({
		content : markerObj.content,
	});
	markerObj.marker.setMap(vmap.gmaps);
}

function vmarkerSetLable(markerObj, lable) {
	markerObj.marker.set('labelContent', lable);
}

function vmarkerSetContent(markerObj, strContent) {
	markerObj.content = strContent;

}

function vmarkerOpenContent(markerObj) {
	markerObj.infoWindow = new google.maps.InfoWindow({
		content : markerObj.content
	});
	markerObj.infoWindow.open(markerObj.maps.gmaps, markerObj.marker);
}

function vmarkerSetImage(markerObj, image) {
	markerObj.imgSrc = image;
	markerObj.marker.setIcon({
		url : RotateIcon.makeIcon(image).setRotation({
			deg : markerObj.imgAngle
		}).getUrl(),
		anchor : new google.maps.Point(16, 16),
	});
}

function vmarkerSetRotate(markerObj, angle) {
	markerObj.imgAngle = angle;
	markerObj.marker.setIcon({
		url : RotateIcon.makeIcon(markerObj.imgSrc).setRotation({
			deg : angle
		}).getUrl(),
		anchor : new google.maps.Point(16, 16),
	});
}

function vmarkerSetPossion(markerObj, latLng) {
	markerObj.latitude = latLng.lat();
	markerObj.longitude = latLng.lng();
	markerObj.marker.setPosition(latLng);
}

function vmarkerSetClickable(markerObj, flag) {
	markerObj.clickable = flag;
	markerObj.marker.setClickable(flag);
}

function vmarkerSetDraggable(markerObj, flag) {
	markerObj.draggable = flag;
	markerObj.marker.setDraggable(flag);
}

function vmarkerSetAnimation(markerObj, animation) {
	markerObj.marker.setAnimation(google.maps.Animation[animation]);
}

function vmarkerSetOpacity(markerObj, opacity) {
	markerObj.opacity = opacity;
	markerObj.marker.setOpacity(opacity);
}

function vmarkerSetOptions(markerObj, options) {
	markerObj.options = options;
	markerObj.marker.setOptions(options);
}

function vmarkerSetVisible(markerObj, flag) {
	markerObj.visible = flag;
	markerObj.marker.setVisible(flag);
}
