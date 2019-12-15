var map = L.map('map', {
		center: [37.8, -96],
		zoom: 5,
		maxBounds: bounds,
		zoomControl: false,
		doubleClickZoom: false,
		scrollWheelZoom: false,
		touchZoom: false,
		inertia: false
});
//map.zoomControl.setPosition('bottomright');
L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
	maxZoom: 18, minZoom: 5,
	attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
	'<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
	'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
	id: 'mapbox.light'
}).addTo(map);

geojson = L.geoJson(statesData, {
	style: stateStyle,
	onEachFeature: stateEffects
});

texas = L.geoJson(txStateData, {
	style: stateStyle,
	onEachFeature: stateEffects
}).addTo(map);

// console.log(texas._layers[104]);
texasDistrictsLayer =  L.geoJson(texas_districts, {
                  	style: districtStyle,
                  	onEachFeature: districtEffects
                  });

recenterMap();
outerMenuBtn.hide();
mapContent.hide();

map.on('drag', function() {
	map.panInsideBounds(bounds, { animate: false });
});
